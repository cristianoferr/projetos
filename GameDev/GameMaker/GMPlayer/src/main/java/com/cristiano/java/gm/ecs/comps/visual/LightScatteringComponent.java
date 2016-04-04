package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class LightScatteringComponent extends AbstractFilterComponent {
	
	private static final String LIGHT_DENSITY = "lightDensity";
	public float lightDensity;
	public float exposurePower;


	public LightScatteringComponent() {
		super(GameComps.COMP_LIGHT_SCATTERING);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		lightDensity = ge.getPropertyAsFloat(LIGHT_DENSITY);
	}

	@Override
	public IGameComponent clonaComponent() {
		LightScatteringComponent ret = new LightScatteringComponent();
		ret.lightDensity = lightDensity;
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(LIGHT_DENSITY,lightDensity);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		lightDensity=(float) obj.get(LIGHT_DENSITY);
		
	}
	
}
