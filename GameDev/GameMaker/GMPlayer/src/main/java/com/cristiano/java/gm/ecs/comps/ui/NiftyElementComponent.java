package com.cristiano.java.gm.ecs.comps.ui;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;

import de.lessvoid.nifty.elements.Element;

public class NiftyElementComponent extends GameComponent {

	public Element control;
	
	public NiftyElementComponent(){
		super(GameComps.COMP_UI_NIFTY_ELEMENT);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		NiftyElementComponent ret = new NiftyElementComponent();
		ret.control=control;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	//Created automatically... 
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
}
