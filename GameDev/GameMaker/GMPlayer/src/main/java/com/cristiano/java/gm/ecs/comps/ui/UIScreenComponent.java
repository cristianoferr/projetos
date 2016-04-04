package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class UIScreenComponent extends AbstractUIComponent {

	// public boolean current = false;

	public boolean isStartScreen = false;

	public UIScreenComponent() {
		super(GameComps.COMP_UI_SCREEN);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elUI = ge.getPropertyAsGE(GameProperties.SCREEN);
	}

	@Override
	public void free() {
		super.free();
		isStartScreen = false;
		// current=false;
	}

	@Override
	public IGameComponent clonaComponent() {
		UIScreenComponent ret = new UIScreenComponent();
		finishClone(ret);
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.STARTING_POSITION, isStartScreen);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
		Object startScreenObj = obj.get(GameProperties.STARTING_POSITION);
		if (startScreenObj != null) {
			isStartScreen = (boolean) startScreenObj;
		}
	}

}
