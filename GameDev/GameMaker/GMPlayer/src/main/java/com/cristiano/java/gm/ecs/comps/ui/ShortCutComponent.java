package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class ShortCutComponent extends GameComponent {

	public int shortCut;
	public String action;
	public IGameEntity entityOriginator;
	public boolean isAnalog=false;
	
	public ShortCutComponent(){
		super(GameComps.COMP_SHORTCUT);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		shortCut=ge.getParamAsInt(Extras.LIST_PROPERTY,GameProperties.VALUE);
		action=ge.getProperty(GameProperties.ACTION);
		isAnalog=ge.getPropertyAsBoolean(GameProperties.IS_ANALOG);
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}
	
	@Override
	public IGameComponent clonaComponent() {
		ShortCutComponent ret = new ShortCutComponent();
		ret.shortCut=shortCut;
		ret.action=action;
		ret.entityOriginator=entityOriginator;
		ret.isAnalog=isAnalog;
		return ret;
	}
	@Override
	public void resetComponent() {
	}
}
