package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class DepthOfFieldComponent extends AbstractFilterComponent {

	public float focusDistance;
	public float focusRange;
	public float blurScale;

	public DepthOfFieldComponent() {
		super(GameComps.COMP_DEPTH_OF_FIELD);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		focusDistance = ge.getPropertyAsFloat(FOCUS_DISTANCE);
		focusRange = ge.getPropertyAsFloat(FOCUS_RANGE);
		blurScale = ge.getPropertyAsFloat(BLUR_SCALE);
	}

	@Override
	public IGameComponent clonaComponent() {
		DepthOfFieldComponent ret = new DepthOfFieldComponent();
		ret.focusDistance = focusDistance;
		ret.focusRange = focusRange;
		ret.blurScale = blurScale;
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(FOCUS_DISTANCE,this.focusDistance);
		obj.put(FOCUS_RANGE,this.focusRange);
		obj.put(BLUR_SCALE,this.blurScale);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		focusDistance = CRJsonUtils.getFloat(obj,FOCUS_DISTANCE);
		focusRange = CRJsonUtils.getFloat(obj,FOCUS_RANGE);
		blurScale = CRJsonUtils.getFloat(obj,BLUR_SCALE);
		
	}
}
