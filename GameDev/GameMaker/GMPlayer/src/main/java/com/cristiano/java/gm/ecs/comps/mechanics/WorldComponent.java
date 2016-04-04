package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class WorldComponent extends GameComponent {


	public WorldComponent() {
		super(GameComps.COMP_WORLD);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	
	}

	@Override
	public IGameComponent clonaComponent() {
		WorldComponent ret = new WorldComponent();
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	
	}

	@Override
	public void resetComponent() {
	}
}
