package com.cristiano.java.gm.ecs.comps.unit.npcBehaviours;

import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.systems.unit.npcBehaviours.BehaviourSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.rigidBody.ActionController;
import com.jme3.ai.navmesh.Plane;
import com.jme3.math.Vector3f;

/*
 * 
 * */
public abstract class AbstractNPCBehaviours extends GameComponent {

	
	// set here for quick retrieval and reuse...
	public final Vector3f _targetRelPos= new Vector3f();
	public final Vector3f _targetRelVelocity = new Vector3f();
	public final Vector3f _targetVelocity = new Vector3f();
	public final Vector3f _targetLocation = new Vector3f();
	public final Vector3f _entityPos = new Vector3f();
	public final Vector3f _forwardVector = new Vector3f();
	public final Vector3f _rotatedForwardVector = new Vector3f();
	public final Plane _plane = new Plane();
	public IControlBody _vehicle;
	public ActionController actionController=null;
	public float _distanceTarget;
	public final Vector3f _entityVelocity = new Vector3f();

	public Vector3f dimension;

	public AbstractNPCBehaviours(String tipo) {
		super(tipo);
	}
	
	@Override
	public void free() {
		super.free();
		_vehicle=null;
		actionController=null;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	protected void finishComponent(AbstractNPCBehaviours comp) {

	}

	public abstract void executeBehavior(BehaviourSystem system,
			IGameEntity entity,float tpf);

	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		
	}

	public void initVehicle(IControlBody controlBody) {
		_vehicle=controlBody;
		actionController=controlBody.getActionController();
		
		
		
	}
}
