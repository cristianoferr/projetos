package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class UILayerComponent extends AbstractUIComponent {

	
	public UILayerComponent(){
		super(GameComps.COMP_UI_LAYER);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		UILayerComponent ret = new UILayerComponent();
		super.finishClone(ret);
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	
	//Loaded automatically
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}
}
