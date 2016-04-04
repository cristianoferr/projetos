package com.cristiano.galactic.view.models;

import com.cristiano.galactic.view.View;


public class SphereView extends ItemView implements ISphereModel {

	public SphereView( View view,double radius) {
		super( view);
		setRadius(radius);
		
	}
	
}
