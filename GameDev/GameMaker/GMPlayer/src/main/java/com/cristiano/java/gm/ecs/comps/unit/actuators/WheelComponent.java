package com.cristiano.java.gm.ecs.comps.unit.actuators;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class WheelComponent extends AbstractActuatorComponent {

	public boolean isFrontWheel;
	public float radius;

	public WheelComponent(){
		super(GameComps.COMP_ACTUATOR_WHEEL);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.isFrontWheel=ge.getPropertyAsBoolean(GameProperties.IS_FRONT_WHEEL);
		this.radius=ge.getPropertyAsFloat(GameProperties.RADIUS);
	}	
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		loadFromElement(getElement());
	}

	@Override
	public IGameComponent clonaComponent() {
		WheelComponent ret = new WheelComponent();
		ret.isFrontWheel=isFrontWheel;
		ret.radius=radius;
		finishClone(ret);
		return ret;
	}

	//it´s added internally (gmancestorcontrol)
	public boolean isAddingNode() {
		return false;
	}
	@Override
	public void resetComponent() {
	}

	
}
