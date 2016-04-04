package com.cristiano.java.gm.ecs.comps.persists;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;

/*
 * 
 * */
public class AssetLoadRequestComponent extends GameComponent {

	public int type;
	public String fileName;

	public AssetLoadRequestComponent(){
		super(GameComps.COMP_ASSET_LOAD_REQUEST);
	}
	@Override
	public void free() {
		super.free();
		fileName=null;
	}

	@Override
	public IGameComponent clonaComponent() {
		return null;
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
