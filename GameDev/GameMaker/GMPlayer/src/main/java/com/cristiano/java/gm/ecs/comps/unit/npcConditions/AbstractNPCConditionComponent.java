package com.cristiano.java.gm.ecs.comps.unit.npcConditions;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public abstract class AbstractNPCConditionComponent extends GameComponent {

	public String[] statusTrue = new String[0];
	public String[] statusFalse = new String[0];

	public AbstractNPCConditionComponent(String comp) {
		super(comp);
	}

	@Override
	public void free() {
		super.free();
		statusTrue = new String[0];
		statusFalse = new String[0];

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		String stTrue = StringHelper.removeChaves(ge.getProperty(GameProperties.STATUS_TRUE));
		String stFalse = StringHelper.removeChaves(ge.getProperty(GameProperties.STATUS_FALSE));
		if (!stTrue.equals("")) {
			statusTrue = stTrue.trim().split(" ");
		}
		if (!stFalse.equals("")) {
			statusFalse = stFalse.trim().split(" ");
		}
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}

}
