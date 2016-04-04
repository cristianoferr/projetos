package com.cristiano.java.gm.ecs.comps.unit.npcConditions;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class NoTargetCondition extends AbstractNPCConditionComponent {

	public NoTargetCondition(){
		super(GameComps.COMP_NPC_CONDITION_NO_TARGET);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	@Override
	public IGameComponent clonaComponent() {
		return new NoTargetCondition();
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
