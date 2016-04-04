package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class FlagResourceComponent extends ResourceComponent {


	public FlagResourceComponent(){
		super(GameComps.COMP_RESOURCE_FLAG);
		
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	
	@Override
	public IGameComponent clonaComponent() {
		FlagResourceComponent ret = new FlagResourceComponent();
		finishClone(ret);
		return ret;
	}
	
	

	@Override
	public void resetComponent() {
	}	
}
