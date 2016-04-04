package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.BillboardControl.Alignment;

/*
 * Unit Test: TestActuators
 * */
public class TargetSystem extends JMEAbstractSystem {
	private static final Object VISUAL_TARGET_TYPE_NO_TARGET = "none";
	// visualTargetTypeEnum
	public String VISUAL_TARGET_TYPE_ARROW = "arrow";
	public String VISUAL_TARGET_TYPE_SQUARE = "square";

	Node _previousBillboard = null;

	public TargetSystem() {
		super(GameComps.COMP_TARGET);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (!component.isFirstTick()) {
			return;
		}
		TargetComponent comp = (TargetComponent) component;
		comp.firstTick = false;
		if (!ent.containsComponent(GameComps.COMP_PLAYER)) {
			return;
		}
		attachTargetBillBoard(comp.target, comp);

	}

	

	private void attachTargetBillBoard(IGameEntity target, TargetComponent comp) {
		consts = getGameConstsComponent();
		if (_previousBillboard != null) {
			return;
		}
		if (consts.visualTargetType.equals(VISUAL_TARGET_TYPE_NO_TARGET)) {
			return;
		}
		// main

		DimensionComponent dimC = (DimensionComponent) target.getComponentWithIdentifier(GameComps.COMP_DIMENSION);
		int redux = 20;
		float x = dimC.dimension.x / redux;
		float y = dimC.dimension.y / redux;
		float z = dimC.dimension.z / (redux);
		Node billboard = generateBillboard(target);

		if (consts.visualTargetType.equals(VISUAL_TARGET_TYPE_ARROW)) {
			generateArrow(dimC, x, z, billboard);
		} else if (consts.visualTargetType.equals(VISUAL_TARGET_TYPE_ARROW)) {
			generateSquared(dimC, x, y, z, billboard);
		} else {
			Log.error("Unknown Targetting type:" + consts.visualTargetType);
		}

	}

	private Node generateBillboard(IGameEntity target) {
		RenderComponent renderC = (RenderComponent) target.getComponentWithIdentifier(GameComps.COMP_RENDER);
		float scale = 5;

		Node billboard = new Node("Billboard");
		BillboardControl billboardControl = new BillboardControl();
		billboardControl.setAlignment(Alignment.Screen);
		billboard.addControl(billboardControl);
		billboard.setShadowMode(ShadowMode.Off);
		billboard.setBatchHint(BatchHint.Never);

		billboard.setLocalScale(1);
		try {
			
		billboard.center();
		} catch (Exception e) {
			Log.error("Error centering billboard");
			//e.printStackTrace();
		}
		billboard.setLocalScale(scale);
		billboard.move(0, 0, 0);

		renderC.node.attachChild(billboard);
		_previousBillboard = billboard;
		return billboard;
	}

	private void generateSquared(DimensionComponent dimC, float x, float y, float z, Node billboard) {
		attachBeam(billboard, new Vector3f(-dimC.dimension.x, 0, 0), new Vector3f(x, dimC.dimension.y, z));
		attachBeam(billboard, new Vector3f(dimC.dimension.x, 0, 0), new Vector3f(x, dimC.dimension.y, z));

		attachBeam(billboard, new Vector3f(0, dimC.dimension.y, 0), new Vector3f(dimC.dimension.x, y, z));
		attachBeam(billboard, new Vector3f(0, -dimC.dimension.y, 0), new Vector3f(dimC.dimension.x, y, z));
	}

	private void generateArrow(DimensionComponent dimC, float x, float z, Node billboard) {
		attachBeam(billboard, new Vector3f(0, dimC.dimension.y * 2.2f, 0), new Vector3f(x, dimC.dimension.y, z));
		float px = 1.9f;
		Geometry box = attachBeam(billboard, new Vector3f(x * px, dimC.dimension.y * 1.44f, 0), new Vector3f(x, dimC.dimension.y / 2, z));
		JMESnippets.rotaciona(box, 0, 0, -45);
		box = attachBeam(billboard, new Vector3f(-x * px, dimC.dimension.y * 1.44f, 0), new Vector3f(x, dimC.dimension.y / 2, z));
		JMESnippets.rotaciona(box, 0, 0, 45);
	}

	private Geometry attachBeam(Node billboard, Vector3f pos, Vector3f size) {
		Geometry box = getSnippets().generateBox(ColorRGBA.Red, size, pos);
		billboard.attachChild(box);
		return box;
	}

}
