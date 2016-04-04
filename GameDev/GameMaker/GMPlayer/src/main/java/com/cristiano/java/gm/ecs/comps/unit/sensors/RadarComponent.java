package com.cristiano.java.gm.ecs.comps.unit.sensors;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class RadarComponent extends GameComponent {

	public float detectRange=0;
	public float nearRange=0;

	public RadarComponent(){
		super(GameComps.COMP_RADAR);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		detectRange=ge.getPropertyAsFloat(GameProperties.RANGE_DETECT);
		nearRange=ge.getPropertyAsFloat(GameProperties.RANGE_NEAR);
	}	

	@Override
	public IGameComponent clonaComponent() {
		RadarComponent ret = new RadarComponent();
		ret.detectRange=detectRange;
		ret.nearRange=nearRange;
		return ret;
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
		loadFromElement(getElement());
	}
	
}
