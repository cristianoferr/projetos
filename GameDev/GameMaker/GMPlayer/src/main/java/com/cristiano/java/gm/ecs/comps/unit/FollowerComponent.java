package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class FollowerComponent extends GameComponent {

	public IGameEntity master=null;

	public FollowerComponent(){
		super(GameComps.COMP_RADAR);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	@Override
	public void free() {
		super.free();
		master=null;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		FollowerComponent ret = new FollowerComponent();
		ret.master=master;
		return ret;
	}	
	@Override
	public void resetComponent() {
	}

	//TODO: como persistir isso??
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
}
