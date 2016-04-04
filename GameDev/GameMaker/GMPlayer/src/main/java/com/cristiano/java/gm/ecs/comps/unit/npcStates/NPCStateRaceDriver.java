package com.cristiano.java.gm.ecs.comps.unit.npcStates;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class NPCStateRaceDriver extends AbstractNPCStateComponent{

	public NPCStateRaceDriver(){
		super(GameComps.COMP_NPC_RACE_DRIVER);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	@Override
	public IGameComponent clonaComponent() {
		AbstractNPCStateComponent state = new NPCStateRaceDriver();
		completeStatus(state);
		return state;
	}

	@Override
	public void stateEnter(IGameEntity ent,
			NPCStateManagerSystem system) {
		system.changeBehaviourTo(ent,GameComps.COMP_BEHAVIOR_FOLLOW_WAYPOINT,this);
	}

	@Override
	public void stateTick(IGameEntity ent,
			NPCStateManagerSystem system) {
		//BehaviourFollowWaypointComponent behaviour = (BehaviourFollowWaypointComponent) 
		
		
	}

	
	@Override
	public void stateExit(IGameEntity ent,
			NPCStateManagerSystem npcStateManagerSystem) {
	}
	@Override
	public void resetComponent() {
	}
}
