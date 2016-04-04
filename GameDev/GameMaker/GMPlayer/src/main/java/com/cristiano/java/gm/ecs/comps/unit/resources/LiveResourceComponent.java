package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class LiveResourceComponent extends ResourceComponent {


	public LiveResourceComponent(){
		super(GameComps.COMP_RESOURCE_LIVES);
		
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	
	@Override
	public IGameComponent clonaComponent() {
		LiveResourceComponent ret = new LiveResourceComponent();
		finishClone(ret);
		return ret;
	}
	
	

	@Override
	public void resetComponent() {
	}	
}
