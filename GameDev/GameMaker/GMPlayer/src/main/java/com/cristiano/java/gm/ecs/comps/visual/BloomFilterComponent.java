package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class BloomFilterComponent extends AbstractFilterComponent {
	
	public float intensity;
	public float exposurePower;
	public float blurScale;


	public BloomFilterComponent() {
		super(GameComps.COMP_BLOOM_FILTER);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		intensity = ge.getPropertyAsFloat(GameProperties.INTENSITY);
		exposurePower = ge.getPropertyAsFloat(GameProperties.EXPOSURE_POWER);
		blurScale = ge.getPropertyAsFloat(BLUR_SCALE);
	}

	@Override
	public IGameComponent clonaComponent() {
		BloomFilterComponent ret = new BloomFilterComponent();
		ret.intensity = intensity;
		ret.exposurePower = exposurePower;
		ret.blurScale = blurScale;
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.INTENSITY,intensity);
		obj.put(GameProperties.EXPOSURE_POWER,exposurePower);
		obj.put(BLUR_SCALE,blurScale);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		intensity=CRJsonUtils.getFloat(obj,GameProperties.INTENSITY);
		exposurePower=CRJsonUtils.getFloat(obj,GameProperties.EXPOSURE_POWER);
		blurScale=CRJsonUtils.getFloat(obj,BLUR_SCALE);
		
	}
	
}
