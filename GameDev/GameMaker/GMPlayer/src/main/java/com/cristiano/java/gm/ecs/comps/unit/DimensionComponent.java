package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

/*
 * 
 * */
public class DimensionComponent extends GameComponent {

	public final Vector3f dimension=new Vector3f();
	public DimensionComponent(){
		super(GameComps.COMP_DIMENSION);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public void free() {
		super.free();
		dimension.zero();
	}
	
	@Override
	public IGameComponent clonaComponent() {
		DimensionComponent ret = new DimensionComponent();
		ret.dimension.set(dimension);
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		CRJsonUtils.exportVector3f(obj,GameProperties.DIMENSION,dimension);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		dimension.set(CRJsonUtils.importVector3f(obj,GameProperties.DIMENSION));
		
	}
}
