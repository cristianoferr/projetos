package com.cristiano.java.gm.ecs.systems.unit.npcStates;

import java.util.ArrayList;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcStates.AbstractNPCStateComponent;
import com.cristiano.java.gm.ecs.systems.GenericComponentAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class NPCStateManagerSystem extends GenericComponentAbstractSystem {

	// a reusable array... cleaned every time its used
	ArrayList<IGameComponent> temp_array = new ArrayList<IGameComponent>();

	public NPCStateManagerSystem() {
		super(GameComps.TAG_COMPS_NPC_STATES);
	}


	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		AbstractNPCStateComponent stateComp = (AbstractNPCStateComponent) component;
		AIComponent stateData = ECS.getAIComp(ent);
		if (stateData == null) {
			Log.error("No NPCStateChecker found on entity! " + ent);
			return;
		}

		if (stateComp.isFirstTick()) {
			stateEnter(ent, stateComp);
		}

		// I check if the destination states have the conditions met...
		if (!hasChangedNextState(ent, stateComp, stateData)) {

			if (!stateConditionsMet(ent, stateComp.keepConditions)) {
				goToPreviousState(ent, stateComp);
			} else {
				stateTick(ent, stateComp);
			}
		}
	}

	private void goToPreviousState(IGameEntity ent, AbstractNPCStateComponent stateComp) {
		IGameComponent prevState = stateComp.previousState;
		if (prevState == null) {
			// Log.warn(stateComp + " do not have a previous state for entity:"+
			// ent);
			return;
		}
		stateExit(ent, stateComp);
		entMan.removeComponentFromEntity(stateComp, ent);
		entMan.addComponent(prevState, ent);
		Log.info(ent + " is reverting to a previous state: " + prevState);

	}

	// checks if the entity has the conditions for the state...
	public boolean stateConditionsMet(IGameEntity ent, String[] conditions) {

		if (conditions == null) {
			return true;
		}

		//String failedConditions = "";
		// If any condition is met, then return true...
		for (String condition : conditions) {
			if (ent.containsComponent(condition)) {
				return true;
			}
			//failedConditions += condition + " ";
		}
		// Log.debug("Failed Conditions:"+failedConditions);
		// true only if there are no conditions defined (no going back from
		// this, only forward)
		return false;
	}

	// I check all the destinations states to see if there is any met...
	private boolean hasChangedNextState(IGameEntity ent, AbstractNPCStateComponent stateComp, AIComponent stateData) {
		ArrayList<IGameComponent> statesOK = getValidDestinationStates(ent, stateComp, stateData);

		if (goToNextState(ent, stateComp, statesOK)) {
			return true;
		}

		return false;
	}

	private boolean goToNextState(IGameEntity ent, AbstractNPCStateComponent stateComp, ArrayList<IGameComponent> statesOK) {
		if (statesOK.size() > 0) {
			AbstractNPCStateComponent nextState = (AbstractNPCStateComponent) statesOK.get((int) (statesOK.size() * CRJavaUtils.random()));
			if (nextState.getIdentifier().equals(stateComp.getIdentifier())) {
				return false;
			}
			stateExit(ent, stateComp);
			Log.info(ent + " next state is: " + nextState);
			entMan.addComponent(nextState, ent);
			nextState.previousState = stateComp;
			nextState.firstTick = true;

			ent.removeComponent(stateComp);
			return true;
		}
		return false;
	}

	private ArrayList<IGameComponent> getValidDestinationStates(IGameEntity ent, AbstractNPCStateComponent stateComp, AIComponent stateData) {
		temp_array.clear();
		ArrayList<IGameComponent> statesOK = temp_array;
		if (stateComp.destinationStates == null) {
			return statesOK;
		}
		for (String stateName : stateComp.destinationStates) {
			IGameComponent possibleState = stateData.getTemplateStateWithIdentifier(stateName);
			if (stateConditionsMet(ent, ((AbstractNPCStateComponent) possibleState).entryConditions)) {
				statesOK.add(possibleState);
			}
		}
		return statesOK;
	}

	private void stateExit(IGameEntity ent, AbstractNPCStateComponent stateComp) {
		Log.debug(ent + " is exiting " + stateComp);
		stateComp.stateExit(ent, this);

	}

	private void stateEnter(IGameEntity ent, AbstractNPCStateComponent stateComp) {
		Log.debug(ent + " is entering " + stateComp);
		stateComp.firstTick = false;

		if (stateComp.states == null) {
			stateComp.states = ECS.getAIComp(ent);
		}
		stateComp.stateEnter(ent, this);
	}

	protected void stateTick(IGameEntity ent, AbstractNPCStateComponent stateComp) {
		// Log.debug(ent + " is ticking " + stateComp);
		stateComp.stateTick(ent, this);
	}

	public IGameComponent changeBehaviourTo(IGameEntity ent, String behaviour, AbstractNPCStateComponent stateComp) {
		IGameComponent behaviourComp = ent.getComponentWithTag(behaviour);
		if (behaviourComp != null) {
			return behaviourComp;
		} else {
			Log.debug("Changing behaviour to " + behaviour);
			if (ent.containsComponentWithTag(GameComps.TAG_COMPS_NPC_BEHAVIOURS)) {
				ent.removeComponent(GameComps.TAG_COMPS_NPC_BEHAVIOURS);
			}
			behaviourComp = stateComp.states.getTemplateBehaviourWithIdentifier(behaviour);
			ent.attachComponent(behaviourComp);
			return behaviourComp;
		}
	}
}
