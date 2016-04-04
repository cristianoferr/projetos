package com.cristiano.java.gm.ecs.systems.unit.resources;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.resources.BatteryComponent;
import com.cristiano.java.gm.ecs.systems.AbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


public class BatterySystem extends AbstractSystem {

	public BatterySystem() {
		super(GameComps.COMP_BATTERY);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		BatteryComponent battery=(BatteryComponent)component;
		if (battery.getMaxValue()==battery.getCurrValue())return;
		float v=tpf*battery.getMaxValue()/battery.rechargeSec;
		battery.setCurrValue(battery.getCurrValue() + v);
		if (battery.getCurrValue()>battery.getMaxValue())battery.setCurrValue(battery.getMaxValue());
	}

	

	
}
