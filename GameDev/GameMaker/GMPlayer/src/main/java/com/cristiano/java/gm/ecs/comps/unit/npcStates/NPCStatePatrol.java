package com.cristiano.java.gm.ecs.comps.unit.npcStates;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class NPCStatePatrol extends AbstractNPCStateComponent{

	public NPCStatePatrol(){
		super(GameComps.COMP_NPC_STATE_PATROL);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	@Override
	public IGameComponent clonaComponent() {
		AbstractNPCStateComponent state = new NPCStatePatrol();
		completeStatus(state);
		return state;
	}
	
	@Override
	public void stateEnter(IGameEntity ent,
			NPCStateManagerSystem npcStateManagerSystem) {
	}

	@Override
	public void stateTick(IGameEntity ent,
			NPCStateManagerSystem system) {
		//TODO: finish this behaviour...
		//system.changeBehaviourTo(ent,GameComps.COMP_BEHAVIOR_CHASE,this);
	}

	@Override
	public void stateExit(IGameEntity ent,
			NPCStateManagerSystem npcStateManagerSystem) {
	}
	@Override
	public void resetComponent() {
	}
	
}
