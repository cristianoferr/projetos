package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.jme3.math.Vector3f;

/*
 * 
 * */
public class ApplyForceComponent extends GameComponent {

	public final Vector3f force=new Vector3f();
	public boolean applyForce=false;
	public boolean applyVelocity=false;
	
	public ApplyForceComponent(){
		super(GameComps.COMP_APPLY_FORCE);
	}

	@Override
	public void free() {
		super.free();
		force.set(0,0,0);
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
