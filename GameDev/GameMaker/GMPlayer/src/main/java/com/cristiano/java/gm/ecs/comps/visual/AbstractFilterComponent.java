package com.cristiano.java.gm.ecs.comps.visual;

import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.post.Filter;
import com.jme3.scene.Spatial;

public abstract class AbstractFilterComponent extends GameComponent {
	protected static final String BLUR_SCALE = "blurScale";
	protected static final String FOCUS_DISTANCE = "focusDistance";
	protected static final String FOCUS_RANGE = "focusRange";
	protected static final String LIGHT_DENSITY = "lightDensity";

	public Filter filter;

	public AbstractFilterComponent(String tipo) {
		super(tipo);
	}

	@Override
	public void free() {
		super.free();
	}


	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		
	}

	
	public void applyFilterChildren(Spatial node){
		
	}
}
