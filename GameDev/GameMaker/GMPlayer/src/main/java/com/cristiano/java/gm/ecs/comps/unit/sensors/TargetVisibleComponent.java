package com.cristiano.java.gm.ecs.comps.unit.sensors;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class TargetVisibleComponent extends GameComponent {

	public TargetVisibleComponent(){
		super(GameComps.COMP_TARGET_VISIBLE);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		return null;
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
