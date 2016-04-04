package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class UnitPositionComponent extends ResourceComponent {

	public UnitPositionComponent(){
		super(GameComps.COMP_RESOURCE_UNIT_POSITION);
		
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	
	@Override
	public IGameComponent clonaComponent() {
		UnitPositionComponent ret = new UnitPositionComponent();
		finishClone(ret);
		return ret;
	}
	

	@Override
	public void resetComponent() {
	}	
}
