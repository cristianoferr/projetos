package com.cristiano.java.gm.ecs.comps.unit.npcStates;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class NPCStateGuard extends AbstractNPCStateComponent{

	public NPCStateGuard(){
		super(GameComps.COMP_NPC_STATE_GUARD);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	@Override
	public IGameComponent clonaComponent() {
		AbstractNPCStateComponent state = new NPCStateGuard();
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
	}

	@Override
	public void stateExit(IGameEntity ent,
			NPCStateManagerSystem npcStateManagerSystem) {
	}
	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
	}
	
}
