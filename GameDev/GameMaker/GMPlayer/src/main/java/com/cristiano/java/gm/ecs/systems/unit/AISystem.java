package com.cristiano.java.gm.ecs.systems.unit;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcConditions.AbstractNPCConditionComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;


public class AISystem extends JMEAbstractSystem {

	public AISystem() {
		super(GameComps.COMP_AI);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		AIComponent aiComp=(AIComponent) component;
		if (aiComp.firstTick){
			aiComp.firstTick=false;
			aiComp.npcStates.clear();
			aiComp.npcBehaviours.clear();
			spawnComponentsFromList(aiComp.elStates,aiComp.npcStates);
			spawnComponentsFromList(aiComp.elBehaviours,aiComp.npcBehaviours);
			
			if (aiComp.conditionComponents == null) {
				loadConditions(aiComp);
			}
		}
		checkStates(ent,aiComp);
		checkConditions(ent, aiComp);
	}

	///////////////////// STATES

	private void checkStates(IGameEntity ent, AIComponent aiComp) {
		List<IGameComponent> states = ent.getComponents(GameComps.TAG_COMPS_NPC_STATES);
		if (states.size()>1){
			//only one state may be active...
			Log.error("Entity "+ent+" has 2 or more npcstates...");
			ent.removeComponent(GameComps.TAG_COMPS_NPC_STATES);
			addStartableState(ent,aiComp);
		} else if (states.isEmpty()){
			addStartableState(ent,aiComp);
		}
		
	}

	private void addStartableState(IGameEntity ent, AIComponent aiComp) {
		
		String tag=aiComp.getRandomStartableState();
		IGameComponent startState = aiComp.getTemplateStateWithIdentifier(tag);
		if (startState==null){
			Log.fatal("Startable State not found!!");
			return;
		}
		Log.info("Starting entity '"+ent+"' with state:"+startState.getIdentifier());
		//startState.hasTag(GameComps.TAG_COMPS_NPC_STATES);
		ent.attachComponent(startState);
		
	}

	
	////////////////////// Condition checker
	
	private void loadConditions(AIComponent comp) {
		comp.conditionComponents = new ArrayList<IGameComponent>();
		for (IGameElement elCondition : comp.elConditions) {
			IGameComponent condComp = entMan.spawnComponent(elCondition.getIdentifier());
			if (condComp == null) {
				Log.error("Condition '" + elCondition.getIdentifier() + "' not found.");
				return;
			}
			condComp.loadFromElement(elCondition);
			comp.conditionComponents.add(condComp);
		}

	}
	
	private void checkConditions(IGameEntity ent, AIComponent comp) {
		for (IGameComponent condition : comp.conditionComponents) {
			boolean conditionOk = checkCondition(ent, (AbstractNPCConditionComponent) condition);
			updateCondition(conditionOk, condition, ent);
		}

	}

	private void updateCondition(boolean conditionOk, IGameComponent condition, IGameEntity ent) {
		if (!conditionOk) {
			if (ent.containsComponent(condition)) {
				entMan.removeComponentFromEntity(condition, ent);
			}

		} else {
			// Log.debug("Condition is true:"+condition.getIdentifier());
			entMan.addIfNotExistsComponent(condition, ent);
		}

	}

	// check if the entity has/hasnt all the components specified in the status
	// arrays...
	private boolean checkCondition(IGameEntity ent, AbstractNPCConditionComponent condition) {
		for (String compToCheck : condition.statusTrue) {
			if (!ent.containsComponent(compToCheck)) {
				return false;
			}
		}
		for (String compToCheck : condition.statusFalse) {
			if (ent.containsComponent(compToCheck)) {
				return false;
			}
		}
		// the condition must have at least one status check to be considered
		// true...
		return (condition.statusTrue.length + condition.statusFalse.length) > 0;

	}

	
}
