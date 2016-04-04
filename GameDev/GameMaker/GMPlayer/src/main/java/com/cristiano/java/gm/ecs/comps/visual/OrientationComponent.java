package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class OrientationComponent extends GameComponent {

	private Quaternion orientation = new Quaternion();
	public PhysicsComponent physics;// added automatically by the jmefactory
	private Node node = null;

	// Set by the physics System, they point to a final object, so no need to
	// keep updating them:
	public Vector3f rotatedViewDirection = null;
	public Vector3f rotatedUPDirection = null;
	public Vector3f rotatedDownDirection = null;

	public OrientationComponent() {
		super(GameComps.COMP_ORIENTATION);
	}
	
	@Override
	public void free() {
		super.free();
		physics=null;
		node=null;
		orientation=new Quaternion();
		rotatedDownDirection=null;
		rotatedUPDirection = null;
		rotatedDownDirection = null;
	}

	public Quaternion getOrientation() {

		Quaternion physPos = getPhysOrient();
		if (physPos != null) {
			return physPos;
		}

		if (node != null) {
			return node.getLocalRotation();
		}

		return orientation;
	}

	private Quaternion getPhysOrient() {
		if (physics != null) {
			if (physics.controlBody != null) {
				return physics.controlBody.getPhysicsRotation(orientation);
			}
		}
		return null;
	}

	public void setOrientation(Quaternion pos) {
		if (setPhysOrient(pos)) {
			return;
		}
		if (node != null) {
			node.setLocalRotation(pos);
		}
		this.orientation.set(pos);
	}

	private boolean setPhysOrient(Quaternion pos2) {
		if (physics != null) {
			if (physics.controlBody != null) {
				physics.controlBody.setPhysicsRotation(pos2);
				return true;
			}
		}
		return false;
	}

	@Override
	public IGameComponent clonaComponent() {
		OrientationComponent ret = (OrientationComponent) entMan.spawnComponent(ident);
		if (orientation != null) {
			ret.orientation.set(orientation);
		}
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void fromAngleAxis(float angle, Vector3f unitY) {
		if (orientation == null) {
			orientation = node.getLocalRotation();
		}
		orientation.fromAngleAxis(angle * FastMath.DEG_TO_RAD, unitY);
		node.setLocalRotation(orientation);
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		CRJsonUtils.exportQuaternion(obj, GameProperties.ORIENTATION, getOrientation());
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		orientation = CRJsonUtils.importQuaternion(obj, GameProperties.ORIENTATION);
	}
 
	public void check(IGameEntity arrowEntity) {
		if (node == null) {
			RenderComponent renderComponent = ECS
					.getRenderComponent(arrowEntity);
			if (renderComponent != null) {
				node = renderComponent.node;
			}
		}
	}

	public void setNode(Node node) {
		this.node = node;
		node.setLocalRotation(orientation);
		
	}
}
