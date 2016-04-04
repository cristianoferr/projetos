package com.cristiano.galactic.view.models;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.view.View;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class StarView extends SphereView {

	PointLight light;
	Vector3f pos=new Vector3f();
	public StarView(View view, double radius) {
		super(view, radius);
	}

	
	public void createVisualNodeFromGeom(Item item) {
		super.createVisualNodeFromGeom(item);
		light=new PointLight();
		light.setPosition(Vector3f.ZERO);
		//light.setRadius((float)getRadius());
		light.setColor(ColorRGBA.White);
		
		
		
		((View)getView()).getRootNode().addLight(light);
		
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
		Vector3f coordf=new Vector3f(coord.getXf(),coord.getYf(),coord.getZf());
		light.setPosition(coordf);
	}
}
