package com.cristiano.java.gm.ecs.systems.visual;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;
import com.jme3.input.ChaseCamera;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl.ControlDirection;

public class CameraSystem extends JMEAbstractSystem {

	public static final String FOLLOW = "follow";
	public static final String CHASE = "chase";
	public static final String FIRST_PERSON = "firstperson";

	public CameraSystem() {
		super(GameComps.COMP_CAM);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		CamComponent camC = (CamComponent) component;
		if (component.isFirstTick()) {

			RenderComponent renderC = (RenderComponent) ent
					.getComponentWithIdentifier(GameComps.COMP_RENDER);
			DimensionComponent dimC = (DimensionComponent) ent
					.getComponentWithIdentifier(GameComps.COMP_DIMENSION);

			if (camC.camType.equals(FOLLOW)) {
				attachFollow(camC, renderC, dimC);
			} else if (camC.camType.equals(CHASE)) {
				attachChase(camC, renderC, dimC);
			} else if (camC.camType.equals(FIRST_PERSON)) {
				attachFirstPerson(camC, renderC, dimC);
			}  else {
				Log.error("Unknown camera type:" + camC.camType);
			}
			camC.firstTick = false;
		}
		// camC.firstTick = true;
	}

	private void attachFirstPerson(CamComponent camC, RenderComponent renderC,
			DimensionComponent dimC) {
		CameraNode camNode = initCamNode(camC, renderC);
		
		camNode.setLocalTranslation(new Vector3f(0, 3, 3));
		Quaternion quat = new Quaternion();
		quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
		camNode.setLocalRotation(quat);
	}

	private void attachChase(CamComponent camC, RenderComponent renderC,
			DimensionComponent dimC) {
		ChaseCamera chaseCam = new ChaseCamera(camC.cam, game.getInputManager());
		chaseCam.setDefaultDistance(150);
		chaseCam.setSmoothMotion(true);
		chaseCam.setDragToRotate(false);
		chaseCam.setMaxDistance(camC.maxDist * dimC.dimension.z);
		chaseCam.setMinDistance(camC.minDist * dimC.dimension.z);
		chaseCam.setTrailingEnabled(true);
		renderC.node.addControl(chaseCam);
		camC.chaseCam = chaseCam;
	}

	private void attachFollow(CamComponent camC, RenderComponent renderC,
			DimensionComponent dimC) {

		CameraNode camNode = initCamNode(camC, renderC);
		
		camNode.setLocalTranslation(new Vector3f(dimC.dimension.x*camC.distX, dimC.dimension.y*camC.distY,dimC.dimension.z*camC.distZ));
		// These coordinates are local, the camNode is attached to the character
		// node!
		// quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
		// camNode.setLocalRotation(quat);
		// camNode.lookAt(renderC.node.getLocalTranslation().add(0, 0,
		// absoluteDepth), Vector3f.UNIT_Y);
		camNode.setLocalRotation(new Quaternion());
		snippets.rotaciona(camNode, camC.angleRotateX, 0, 0);
		
	}

	private CameraNode initCamNode(CamComponent camC, RenderComponent renderC) {
		CameraNode camNode = camC.camNode;
		if (camNode == null) {
			camNode = new CameraNode("CamNode", camC.cam);
		}
		camNode.removeFromParent();
		renderC.node.attachChild(camNode);
		camNode.setEnabled(true);
		camNode.setControlDir(ControlDirection.SpatialToCamera);
		camC.camNode = camNode;
		return camNode;
	}
}
