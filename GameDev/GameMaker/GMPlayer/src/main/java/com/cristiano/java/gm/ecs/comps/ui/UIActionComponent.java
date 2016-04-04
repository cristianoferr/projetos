package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class UIActionComponent extends GameComponent {

	public String action=null;
	public IGameEntity originComponent=null;
	public IGameEntity entityGenerated=null;
	public String auxInfo=null;
	
	public UIActionComponent(){
		super(GameComps.COMP_UI_ACTION);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		action=ge.getProperty(GameProperties.ACTION);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		return null;
	}
	@Override
	public void resetComponent() {
		action=null;
		originComponent=null;
		entityGenerated=null;
		auxInfo=null;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
}
