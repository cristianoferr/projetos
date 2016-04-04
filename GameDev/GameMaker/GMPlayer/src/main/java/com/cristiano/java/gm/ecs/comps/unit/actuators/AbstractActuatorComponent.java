package com.cristiano.java.gm.ecs.comps.unit.actuators;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class AbstractActuatorComponent extends GameComponent {

	private static final String ADD_TO_ACTION = "addToAction";

	//public int spatialID=-1;
	//public SpatialComponent spatialComp;
	public List<Integer> spatialIDs=new ArrayList<Integer>();
	public List<SpatialComponent> spatialComponents=new ArrayList<SpatialComponent>();
	private Node node=new Node();
	
	private final Vector3f position = new Vector3f();
	public int addToAction = 0;// if >0 then the actuator will be linked to an
								// action

	public AbstractActuatorComponent(String tipo) {
		super(tipo);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		addToAction = ge.getPropertyAsInt(ADD_TO_ACTION);
	}

	protected void finishClone(AbstractActuatorComponent comp) {
		super.finishClone(comp);
		comp.spatialIDs=spatialIDs;
		
		comp.addToAction = addToAction;
		comp.setPosition(position);
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		spatialIDs.clear();
		for (SpatialComponent spatial:spatialComponents){
			spatialIDs.add(spatial.getId());
		}
		obj.put(GameProperties.SPATIAL_ID,CRJsonUtils.exportIntegerList(spatialIDs));
		CRJsonUtils.exportVector3f(obj, GameProperties.POSITION, position);
		obj.put(GameProperties.ACTION, addToAction);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		spatialIDs=CRJsonUtils.importIntegerList(obj,GameProperties.SPATIAL_ID);
		position.set(CRJsonUtils.importVector3f(obj, GameProperties.POSITION));
		addToAction=CRJsonUtils.getInteger(obj,GameProperties.ACTION);
	}

	public Spatial getNode() {
		/*if (spatialComp.spatial.getParent()==null){
			Log.error("Actuator Spatial has no parent!!");
		}
		return spatialComp.spatial;*/
		return node;
	}

	
	public void setPosition(Vector3f pontoObj) {
		this.position.set(pontoObj);
		//node.setLocalTranslation(position);
	}

	public Vector3f getPosition() {
		return position;
	}

	// if true the node mesh will be added to the parent mesh... wheels by
	// default arent.
	public boolean isAddingNode() {
		return true;
	}

	public void addSpatial(SpatialComponent spatial, Vector3f pontoObj) {
		spatialComponents.add(spatial);
		Geometry geometry = spatial.spatial();
		node.attachChild(geometry);
		geometry.setLocalTranslation(pontoObj.subtract(position));
		
	}
}
