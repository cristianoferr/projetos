package com.cristiano.java.gm.ecs.comps.unit.npcStatus;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public  class HealthPackNearStatusComponent extends AbstractNPCStatusComponents {

	public HealthPackNearStatusComponent(){
		super(GameComps.COMP_NPC_STATUS_HEALTH_PACK_NEAR);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	
	@Override
	public IGameComponent clonaComponent() {
		HealthPackNearStatusComponent ret = new HealthPackNearStatusComponent();
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
