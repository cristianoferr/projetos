package com.cristiano.java.gm.ecs.comps.unit.npcStatus;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public  class EnemyNearStatusComponent extends AbstractNPCStatusComponents {

	public EnemyNearStatusComponent(){
		super(GameComps.COMP_NPC_STATUS_ENEMY_NEAR);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	
	
	@Override
	public IGameComponent clonaComponent() {
		EnemyNearStatusComponent ret = new EnemyNearStatusComponent();
		completeStatus(ret);
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

}
