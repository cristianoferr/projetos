package com.cristiano.java.gm.ecs.comps.effects;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.hull.Faces2D;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;

public class SpatialComponent extends GameComponent {

	private Geometry spatial;
	public Faces2D faces2D;
	public boolean addToRender = true;
	public Vector3f position;
	public List<Vector3f> sourcePoints;
	public String objName;
	private boolean needsImporting = false;

	public SpatialComponent() {
		super(GameComps.COMP_SPATIAL);
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);

	}

	@Override
	public void free() {
		super.free();
		spatial = null;
		faces2D = null;
		position = null;
		sourcePoints = null;
		objName = null;

	}

	@Override
	public IGameComponent clonaComponent() {
		SpatialComponent ret = new SpatialComponent();
		ret.setElement(getElement());
		ret.spatial = spatial().clone(false);
		ret.spatial.setName(spatial.getName());
		ret.faces2D = faces2D;
		ret.position = position;
		ret.addToRender = addToRender;
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.ADDTORENDER, addToRender);
		Spatial nClone = spatial.clone(false);
		JMEUtils.removeUnnecessaryChildren(nClone);
		entMan.getFactory().exportSavable(getId(), GameConsts.ASSET_MESH,
				nClone);
		CRJsonUtils.exportVector3f(obj, GameProperties.POSITION, position);
		
		if (spatial.getMesh()==null){
			Log.fatal("Mesh is null on export!");
		}
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		addToRender = (boolean) obj.get(GameProperties.ADDTORENDER);
		needsImporting = true;
		position = CRJsonUtils.importVector3f(obj, GameProperties.POSITION);
	}

	public Geometry spatial() {
		if (spatial == null && needsImporting) {
			importSpatial();
		}

		return spatial;
	}

	private void importSpatial() {
		spatial=null;
		while (spatial == null) {
			spatial = (Geometry) entMan.getFactory().importSavable(getId(),
					GameConsts.ASSET_MESH);
			if (spatial!=null && spatial.getMesh()==null){
				Log.warn("Mesh is null after importing:"+this);
				spatial=null;
			}
			if (spatial == null) {
				Log.warn("Spatial was null when importing:" + this
						+ ", trying again...");
				CRJavaUtils.sleep(10);
				
			}
		}
	}

	public Mesh getMesh() {
		return spatial().getMesh();
	}

	public void spatial(Geometry spatial) {
		this.spatial = spatial;
	}

}
