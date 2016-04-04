package com.cristiano.galactic.view.models;

import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.view.View;
import com.cristiano.math.Vector3;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;

public class StarView extends SphereView {

	PointLight light;
	Vector3f pos=new Vector3f();
	public StarView(View view, double radius) {
		super(view, radius);
	}

	
	public void createVisualNodeFromGeom(Item item) {
		super.createVisualNodeFromGeom(item);
		light=new PointLight();
		light.setLocation(Vector3f.ZERO);
		//light.setRadius((float)getRadius());
		light.setAmbient(ColorRGBA.black);
		light.setDiffuse(ColorRGBA.white);
		//((View)getView()).getLightState().setGlobalAmbient(ColorRGBA.black);
		light.setEnabled(true);
		light.setShadowCaster(true);
		
		((View)getView()).getLightState().attach(light);
		
	/*	DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1,0,2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);*/
	}
	
	public void update(){
		super.update();
		Vector3 coord=new Vector3(getCoord());
		if (coord.magnitude()>50000){
			coord.normalise(50000);
		}
		ColorRGBA black=ColorRGBA.black;
		black.r=0.1f;
		black.g=0.1f;
		black.b=0.1f;
		light.setAmbient(black);
		light.setDiffuse(ColorRGBA.white);
		light.setSpecular(black);
		
		//light.setLightMask(0);
		
		//light.setEnabled(true);
		//((View)getView()).getLightState().get(0).setEnabled(false);
		//((View)getView()).getLightState().setLightMask(LightState.MASK_DIFFUSE | LightState.MASK_SPECULAR);
		
		Vector3f coordf=new Vector3f(coord.getXf(),coord.getYf(),coord.getZf());
		//light.setLocation(rootNode.getLocalTranslation());
		light.setLocation(coordf);
	}
}
