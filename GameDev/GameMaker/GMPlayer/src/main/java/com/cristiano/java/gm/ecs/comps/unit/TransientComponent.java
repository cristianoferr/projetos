package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class TransientComponent extends GameComponent {

	public TransientComponent(){
		super(GameComps.COMP_TRANSIENT);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		TransientComponent ret = new TransientComponent();
		return ret;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
	


	@Override
	public void resetComponent() {
	}
}
