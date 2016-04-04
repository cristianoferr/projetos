package com.cristiano.java.gm.ecs.comps.map;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class FlattenTerrainComponent extends GameComponent {

	public Vector3f position;
	
	//optional: one or the other
	public Vector3f dimension=null;
	public IGameElement elMeshFunction=null;
 
	public FlattenTerrainComponent() {
		super(GameComps.COMP_FLATTEN_TERRAIN);
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public void free() {
		super.free();
		position=null;
		dimension=null;
		elMeshFunction=null;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		return null;
	}

	public void setPosition(Vector3f position) {
		this.position=position;
	}

	public void setMeshFunction(IGameElement mesh) {
		this.elMeshFunction=mesh;
		
	}
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

}
