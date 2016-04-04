package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class PointsResourceComponent extends ResourceComponent {


	public PointsResourceComponent(){
		super(GameComps.COMP_RESOURCE_POINTS);
		
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	
	@Override
	public IGameComponent clonaComponent() {
		PointsResourceComponent ret = new PointsResourceComponent();
		finishClone(ret);
		return ret;
	}
	
	

	@Override
	public void resetComponent() {
	}	
}
