package com.cristiano.java.gm.ecs.systems.visual;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.ui.billboard.IBillboardElement;
import com.cristiano.utils.Log;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.BillboardControl.Alignment;

public class BillboardSystem extends JMEAbstractSystem {

	public BillboardSystem() {
		super(GameComps.COMP_BILLBOARD);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		BillboardComponent comp=(BillboardComponent) component;
		if (component.isFirstTick()) {
			if (!checkOk(ent)){
				return;
			}
			
			comp.firstTick = false;
			initBillboard(ent,comp);
		} else {
			checkAppendBillboards(ent,comp);
		}
	}

	private void checkAppendBillboards(IGameEntity ent, BillboardComponent comp) {
		for (IBillboardElement item:comp.listToAdd){
			addItem(ent,comp,item);	
		}
		comp.listToAdd.clear();
		
	}

	private void addItem(IGameEntity ent, BillboardComponent comp, IBillboardElement item) {
		//Log.info("Adding new billboardItem for "+ent);
		Node nodeItem=new Node("BillboardNode");
		DimensionComponent dimC = ECS.getDimensionComponent(ent);
		nodeItem.move(dimC.dimension.mult(item.getRelativePosition()));
		nodeItem.attachChild(item.getSpatial());
		comp.billboard.attachChild(nodeItem);
		
	}

	private boolean checkOk(IGameEntity ent) {
		RenderComponent renderC = ECS.getRenderComponent(ent);
		
		DimensionComponent dimC = ECS.getDimensionComponent(ent);
		PhysicsComponent physC = ECS.getPhysicsComponent(ent);
		if ((renderC == null) || (dimC == null)|| (physC == null)) {
			return false;
		}
		if (physC.isFirstTick()){
			return false;
		}
		return true;
		
	}

	private void initBillboard(IGameEntity ent, BillboardComponent comp) {
		Log.info("Initializing billboard for "+ent);
		RenderComponent renderC = ECS.getRenderComponent(ent);
		Node billboard = new Node("Billboard");
		renderC.node.attachChild(billboard);
		BillboardControl billboardControl = new BillboardControl();
		billboardControl.setAlignment(Alignment.Screen);
		billboard.addControl(billboardControl);
		billboard.setShadowMode(ShadowMode.Off);
		billboard.setBatchHint(BatchHint.Never);
		//billboard.center();
		comp.billboard=billboard;
		
	}

}
