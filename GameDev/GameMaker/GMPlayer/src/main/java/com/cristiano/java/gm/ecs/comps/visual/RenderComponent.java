package com.cristiano.java.gm.ecs.comps.visual;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.Log;
import com.jme3.export.Savable;
import com.jme3.scene.Node;

public class RenderComponent extends GameComponent {

	public Node node = null;
	public IGameElement meshElement;
	public List<IGameElement> actionGroups = null;
	public String controlInit = null;
	private IGameElement controller;
	public boolean isGround = true;

	public RenderComponent() {
		super(GameComps.COMP_RENDER);
	}
	
	@Override
	public void free() {
		super.free();
		if (node!=null){
			node.removeFromParent();
		}
		node=null;
		meshElement=null;
		actionGroups=null;
		controlInit=null;
		controller=null;
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		meshElement = ge.getPropertyAsGE(GameProperties.MESH_SOURCE);
		node = new Node("renderComponent:" + meshElement.getIdentifier());
		if (meshElement == null) {
			Log.fatal("MeshElement is null");
			return;
		}
		loadController(meshElement.getPropertyAsGE(GameProperties.CONTROLLER));
	}

	public void loadController(IGameElement controller) {
		this.controller=controller;
		if (controller != null) {
			this.controlInit = controller.getProperty(Extras.PROPERTY_INIT);
			actionGroups = controller.getParamAsGEList(Extras.LIST_PROPERTY, GameProperties.ACTION_GROUP);
			isGround = controller.getPropertyAsBoolean(GameProperties.IS_GROUND);
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		RenderComponent ret = new RenderComponent();
		if (node != null) {
			ret.node = node.clone(false);
			ret.node.setName(node.getName());
		}
		ret.setElement(getElement());
		ret.meshElement = meshElement;
		ret.actionGroups = actionGroups;
		ret.controlInit = controlInit;
		ret.controller = controller;
		ret.isGround = isGround;

		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		//exportNode();
		
		obj.put(Extras.PROPERTY_INIT, controlInit);
		obj.put(GameProperties.ACTION_GROUP,CRJsonUtils.exportList(actionGroups));
		obj.put(GameProperties.IS_GROUND, isGround);
		if (controller!=null){
			obj.put(GameProperties.CONTROLLER,controller.exportToJSON());
		}
		return true;
	}


	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		node=new Node();
		controlInit=(String) obj.get(Extras.PROPERTY_INIT);
		controller = (IGameElement) factory.assembleJSON(obj.get(GameProperties.CONTROLLER));
		isGround=(boolean) obj.get(GameProperties.IS_GROUND);
		actionGroups=CRJsonUtils.importList((JSONObject) obj.get(GameProperties.ACTION_GROUP),factory);
	}

	@Override
	public void resetComponent() {
		if (node != null) {
			node.removeFromParent();
		}
	}

}
