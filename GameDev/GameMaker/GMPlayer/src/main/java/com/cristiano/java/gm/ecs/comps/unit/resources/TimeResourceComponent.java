package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class TimeResourceComponent extends ResourceComponent {


	public TimeResourceComponent(){
		super(GameComps.COMP_RESOURCE_TIME);
		
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	
	@Override
	public IGameComponent clonaComponent() {
		TimeResourceComponent ret = new TimeResourceComponent();
		finishClone(ret);
		return ret;
	}
	
	

	@Override
	public void resetComponent() {
	}	
}
