package com.cristiano.java.gm.states;

import org.json.simple.JSONObject;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

import de.lessvoid.nifty.input.NiftyInputEvent;

public class JMECameraLightState extends JMEAbstractState {
	SpotLight spot;
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		spot.setDirection(cam.getDirection());
    	spot.setPosition(cam.getLocation());
    	spot.setColor(ColorRGBA.White.mult(.5f));
	}

	@Override
	public void cleanup() {
		super.cleanup();

	}

	public boolean keyEvent(final NiftyInputEvent inputEvent) {
		return true;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		spot=new SpotLight();
    	spot.setSpotRange(300);
    	spot.setSpotOuterAngle(35*FastMath.DEG_TO_RAD);
    	spot.setSpotInnerAngle(10*FastMath.DEG_TO_RAD);
    	spot.setColor(spot.getColor().mult((float) 2));
    	rootNode.addLight(spot);
	}

	@Override
	public void resetComponents() {
		internalEntity.resetComponents();
	}

	@Override
	public JSONObject exportToJSON() {
		return null;
	}

	@Override
	public void importFromJSON(JSONObject json) {
	}	

	@Override
	public int size() {
		return internalEntity.size();
	}
}
