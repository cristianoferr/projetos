package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class UIPanelComponent extends AbstractUIComponent {

	public UIPanelComponent(){
		super(GameComps.COMP_UI_PANEL);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elUI=ge.getPropertyAsGE(GameProperties.PANEL);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		UIPanelComponent ret = new UIPanelComponent();
		finishClone(ret);
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	
	//Loaded automatically
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		loadFromElement(getElement());
	}
}
