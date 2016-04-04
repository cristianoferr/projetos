package com.cristiano.java.gm.ecs.systems;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.lifeCycle.MeshLoaderComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class RenderSystem extends JMEAbstractSystem {

	public RenderSystem() {
		super(GameComps.COMP_RENDER);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		RenderComponent comp = (RenderComponent) component;
		if (comp.firstTick) {
			comp.firstTick = false;
			
			if (comp.node==null){
				comp.node=new Node(GMUtils.getNodeName(ent,"root"));
			}

			if ((comp.meshElement != null) && (comp.node.getChildren().size() == 0)) {
				carregaMesh(ent, comp);
			}

			if (comp.actionGroups != null) {
				if (comp.actionGroups.size() > 0) {
					addActionGroups(ent, comp.actionGroups);
				}
			}

			attach(ent, comp.node, comp);
			
			initPosition(ent,comp);

			// comp.archive();
		}
		if (comp.node.getParent()==null){
			attachParent(ent, comp);
		}
	/*	if (ent.containsComponent(GameComps.COMP_TRANSIENT)) {
			updateNodeName(ent, comp.node, "root");
		}*/
	}

	private void initPosition(IGameEntity ent, RenderComponent comp) {
		PositionComponent pos=ECS.getPositionComponent(ent);
		if (pos!=null){
			pos.setNode(comp.node);
		}
		
		OrientationComponent orient = ECS.getOrientationComponent(ent);
		if (orient!=null){
			orient.setNode(comp.node);
		}
		
		
	}

	private void attach(IGameEntity ent, Node node, RenderComponent comp) {
		if (ent.containsComponent(GameComps.COMP_GUI)) {
			game.getGuiNode().attachChild(node);
			return;
		}
		attachParent(ent, comp);
	}

	private void attachParent(IGameEntity ent, RenderComponent comp) {
		Node nodeParent = initParentNode(ent);
		if (nodeParent != null) {
			nodeParent.attachChild(comp.node);
			updateNodeName(ent, comp.node, "root");
		}
	}

	// needed because checks are made based on the node name
	public static void updateNodeName(IGameEntity ent, Spatial spat, String suffix) {

		String name = GMUtils.getNodeName(ent, suffix);
		spat.setName(name);
		if (spat instanceof Node) {
			Node node = (Node) spat;
			for (int i = 0; i < node.getChildren().size(); i++) {
				updateNodeName(ent, node.getChildren().get(i), suffix + "-" + i);
			}
		}

	}

	private Node initParentNode(IGameEntity ent) {
		if (ent.containsComponent(GameComps.COMP_MASTER)) {
			return null;
		}
		Node nodeParent = getParentNode(ent);
		if (nodeParent == null) {
			// Log.error("ERRO: parentNode nulo!!:"+ent.getId());
			nodeParent = game.getRootNode();
			// return;
		}
		return nodeParent;
	}

	private void addActionGroups(IGameEntity ent, List<IGameElement> actionGroups) {
		PhysicsComponent physC = (PhysicsComponent) ent.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		if (physC == null) {
			Log.error("No physicsComponent defined for entity: " + ent);
			return;
		}
		physC.addActionGroups(actionGroups);

	}

	private void carregaMesh(IGameEntity entityMesh, RenderComponent comp) {
		MeshLoaderComponent meshLoader = (MeshLoaderComponent) entMan.addComponent(GameComps.COMP_MESH_LOADER, entityMesh);
		meshLoader.meshElement = comp.meshElement;
	}

	private Node getParentNode(IGameEntity ent) {
		IGameEntity parent=null;
		if (ent instanceof ChildComponent){
			ChildComponent child=(ChildComponent) ent;
			parent=entMan.getEntityWithComponent(child);
		}
		
		RenderComponent render = ECS.getRenderComponent(parent);
		if (render == null) {
			render = (RenderComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_RENDER);
		}
		return render.node;

	}
}
