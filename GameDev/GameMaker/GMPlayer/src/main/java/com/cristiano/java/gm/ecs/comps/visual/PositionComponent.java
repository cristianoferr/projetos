package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class PositionComponent extends GameComponent {

	private final Vector3f pos = new Vector3f();
	private Node node = null;
	public PhysicsComponent physics;// added automatically by the jmefactory

	public PositionComponent() {
		super(GameComps.COMP_POSITION);
	}

	@Override
	public void free() {
		super.free();
		pos.zero();
		node = null;
		physics = null;
	}

	public Vector3f getPos() {
		Vector3f physPos = getPhysPos();
		if (physPos != null) {
			return physPos;
		}

		if (node != null) {
			return node.getLocalTranslation();
		}
		return pos;
	}

	private Vector3f getPhysPos() {
		if (physics != null) {
			if (physics.controlBody != null) {
				return physics.controlBody.getPhysicsLocation(pos);
			}
		}
		return null;
	}

	public void setPos(Vector3f pos) {
		this.pos.set(pos);
		if (pos == null) {
			Log.errorIfRunning("Trying to set a null position...");
			return;
		}
		if (setPhysPos(pos)) {
			return;
		}

		if (node != null) {
			node.setLocalTranslation(pos);
			return;
		}
	}

	private boolean setPhysPos(Vector3f pos2) {
		if (physics != null) {
			if (physics.controlBody != null) {
				physics.controlBody.setPhysicsLocation(pos2);
				return true;
			}
		}
		return false;
	}

	@Override
	public IGameComponent clonaComponent() {
		/*if (node != null) {
			return null;// nesses casos eu vou regerar esse componente
		}*/
		PositionComponent ret = (PositionComponent) entMan.spawnComponent(ident);
		if (pos != null) {
			ret.pos.set(pos);
		}
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		CRJsonUtils.exportVector3f(obj, GameProperties.POSITION, getPos());
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		setPos(CRJsonUtils.importVector3f(obj, GameProperties.POSITION));
	}

	@Override
	public void merge(IGameComponent comp) {
		Vector3f pt = ((PositionComponent) comp).getPos();
		setPos(pt);
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
		node.setLocalTranslation(pos);
	}
	

	@Override
	public void resetComponent() {
	}

}
