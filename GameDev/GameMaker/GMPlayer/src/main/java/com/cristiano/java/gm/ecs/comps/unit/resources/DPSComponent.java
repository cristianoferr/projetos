package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class DPSComponent extends ResourceComponent {

	public DPSComponent(){
		super(GameComps.COMP_RESOURCE_DPS);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	@Override
	public IGameComponent clonaComponent() {
		DPSComponent ret = new DPSComponent();
		ret.setCurrValue(getCurrValue());
		ret.setMaxValue(getMaxValue());
		finishClone(ret);
		return ret;
	}

	public float calcBulletDmg(float rateOfFire) {
		return rateOfFire*currValue;
	}
	@Override
	public void resetComponent() {
	}

	
}
