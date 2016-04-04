package com.cristiano.java.gm.ecs.comps.map;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class RoadComponent extends GameComponent {

 
	public RoadComponent() {
		super(GameComps.COMP_ROAD);
	}
	
	@Override
	public void free(){
		super.free();
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		IGameComponent road = entMan.spawnComponent(GameComps.COMP_ROAD);
		return road;
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
