package com.cristiano.java.gm.ecs.comps.persists;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public class MasterComponent extends GameComponent {

	public MasterComponent(){
		super(GameComps.COMP_MASTER);
		
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public void free() {
		super.free();
	}
	
	@Override
	public IGameComponent clonaComponent() {
		MasterComponent ret = new MasterComponent();
		return ret;
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
