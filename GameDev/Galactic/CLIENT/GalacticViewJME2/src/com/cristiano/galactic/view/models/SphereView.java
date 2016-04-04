package com.cristiano.galactic.view.models;

import com.cristiano.galactic.view.View;
import com.cristiano.galactic.view.models.ISphereModel;


public class SphereView extends ItemView implements ISphereModel {

	public SphereView( View view,double radius) {
		super( view);
		this.radius=radius;
		
		
	}
	public double getRelativeScreenSize(){
		double distancia=getCoord().magnitude();
		return radius/distancia;
	}
	

	public void update(){
		super.update();
	}
}
