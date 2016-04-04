package com.cristiano.java.gm.ecs.comps.unit;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

/*
 * 
 * */
public class PhysicsComponent extends GameComponent {

	public IRigidBody physNode;
	public IControlBody controlBody;
	public boolean isControllable = false;
	public float mass;
	public List<IGameElement> actionGroups;
	public boolean actionsRegistered = false;

	// used when its a bullet...
	public final Vector3f _finalPosition = new Vector3f();
	public Vector3f relSourcePos = null;
	public PositionComponent sourcePosition = null;
	public IRigidBody originalPhysNode;

	public PhysicsComponent() {
		super(GameComps.COMP_PHYSICS);
	}

	@Override
	public void free() {
		super.free();
		physNode = null;
		controlBody = null;
		isControllable = false;
		mass = 0;
		_finalPosition.zero();
		actionGroups = null;
		actionsRegistered = false;
		relSourcePos = null;
		sourcePosition = null;
		originalPhysNode = null;

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		isControllable = ge
				.getPropertyAsBoolean(GameProperties.IS_CONTROLLABLE);
		mass = ge.getPropertyAsFloat(GameProperties.MASS);
	}

	@Override
	public IGameComponent clonaComponent() {
		// noo linko o physNode para que o sistema carregue automaticamente
		PhysicsComponent ret = new PhysicsComponent();
		ret.isControllable = isControllable;
		ret.mass = mass;
		// ret.physNode=physNode.
		ret.originalPhysNode = physNode; // this needs the spatial clone to be
											// cloned...
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.IS_CONTROLLABLE, isControllable);
		obj.put(GameProperties.MASS, mass);

		// static meshes are regenerated on the fly...
		if (isControllable) {
			String path = entMan.getFactory().exportSavable(getId(),
					GameConsts.ASSET_PHYSICS, physNode);
		}
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		mass = CRJsonUtils.getFloat(obj, GameProperties.MASS);
		isControllable = (boolean) obj.get(GameProperties.IS_CONTROLLABLE);

		// static meshes are regenerated on the fly...
		if (isControllable) {
			physNode = (IRigidBody) entMan.getFactory().importSavable(getId(),
					GameConsts.ASSET_PHYSICS);
		}
	}

	public void addActionGroups(List<IGameElement> actionGroups) {
		if (this.actionGroups == null) {
			this.actionGroups = new ArrayList<IGameElement>();
		}
		this.actionGroups.addAll(actionGroups);
	}

	@Override
	public void resetComponent() {
		if (controlBody == null) {
			return;
		}
		controlBody.setEnabled(false);
		controlBody.setPhysicsSpace(null);
	}

	public void setEnabled(boolean flagCamera) {
		if (controlBody == null) {
			Log.errorIfRunning("ControlBody for physicsComponent is null...");
			return;
		}
		controlBody.setEnabled(flagCamera);

	}

}
