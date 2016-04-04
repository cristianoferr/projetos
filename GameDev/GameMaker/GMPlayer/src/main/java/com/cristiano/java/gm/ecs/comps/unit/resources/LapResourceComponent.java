package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class LapResourceComponent extends ResourceComponent {
	
	public LapResourceComponent(){
		super(GameComps.COMP_RESOURCE_LAP);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		setMaxValue(ge.getPropertyAsInt(GameProperties.LAP_GOAL));
	}
	
	@Override
	public IGameComponent clonaComponent() {
		LapResourceComponent ret = new LapResourceComponent();
		finishClone(ret);
		return ret;
	}

	@Override
	public void resetComponent() {
		setCurrValue(0);
		setMaxValue(0);
	}

	
	
}
