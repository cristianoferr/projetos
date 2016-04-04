package com.cristiano.java.gm.ecs.comps.mechanics;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.states.JMEPhysicsSpaceState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/*
 * Armazena o state com a f�sica do jogo...
 * */
public class PhysicsSpaceComponent extends GameComponent {

	public JMEPhysicsSpaceState physics = null;
	public ArrayList<Object> controlsToAdd = new ArrayList<Object>();
	public Vector3f gravity;
	public boolean status=false;

	public PhysicsSpaceComponent() {
		super(GameComps.COMP_PHYSICS_SPACE);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		gravity = new Vector3f(0,
				ge.getPropertyAsFloat(GameProperties.GRAVITY), 0);
	}

	public void addControl(Object rigidControl) {
		controlsToAdd.add(rigidControl);
	}

	@Override
	public IGameComponent clonaComponent() {
		PhysicsSpaceComponent ret = (PhysicsSpaceComponent) entMan
				.spawnComponent(GameComps.COMP_PHYSICS_SPACE);
		if (gravity != null) {
			ret.gravity = gravity.clone();
		}
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void setGravity(Vector3f grav) {
		if (physics == null) {
			Log.errorIfRunning("PhysicsState is null!");
			return;
		}
		physics.getPhysicsSpace().setGravity(grav);

	}

	@Override
	public void deactivate() {
		if (physics != null) {
			physics.deactivate();
			status=false;
		}
	}

	@Override
	public void activate() {
		if (physics != null) {
			physics.activate();
			status=true;
		}
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}

	public void addSpatial(Spatial terrain) {
		if (physics == null) {
			Log.fatal("Physics is null!");
		}
		if (physics.getPhysicsSpace() == null) {
			Log.fatal("physics.getPhysicsSpace() is null!");
		}
		physics.getPhysicsSpace().add(terrain);

	}

	public void checkState() {
		if (status){
			activate();
		} else {
			deactivate();
		}
		
	}
}
