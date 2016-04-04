package com.cristiano.java.gm.ecs.comps.map;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class WaypointComponent extends GameComponent {

 
	public final Vector3f position=new Vector3f();
	public WaypointComponent() {
		super(GameComps.COMP_WAYPOINT);
	}
	
	@Override
	public void free(){
		super.free();
		position.zero();
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		WaypointComponent ret=new WaypointComponent();
		ret.position.set(position);
		finishClone(ret);
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		CRJsonUtils.exportVector3f(obj, GameProperties.POSITION, position);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		position.set(CRJsonUtils.importVector3f(obj, GameProperties.POSITION));
	}
}
