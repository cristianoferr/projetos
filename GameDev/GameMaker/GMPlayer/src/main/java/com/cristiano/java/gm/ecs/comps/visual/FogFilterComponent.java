package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class FogFilterComponent extends AbstractFilterComponent {
	
	
	public float distance;
	public float density;


	public FogFilterComponent() {
		super(GameComps.COMP_FOG_FILTER);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		distance = ge.getPropertyAsFloat(GameProperties.DISTANCE);
		density = ge.getPropertyAsFloat(GameProperties.DENSITY);
	}

	@Override
	public IGameComponent clonaComponent() {
		FogFilterComponent ret = new FogFilterComponent();
		ret.density = density;
		ret.distance = distance;
		return ret;
	}

	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.DISTANCE, distance);
		obj.put(GameProperties.DENSITY, density);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		distance=(float) obj.get(GameProperties.DISTANCE);
		density=(float) obj.get(GameProperties.DENSITY);
	}
}
