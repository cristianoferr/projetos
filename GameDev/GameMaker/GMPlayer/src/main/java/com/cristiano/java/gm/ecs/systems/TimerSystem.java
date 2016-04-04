package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.mechanics.TimerComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


/*
 * Unit Test: TestEntitySystem
 * */
public class TimerSystem extends JMEAbstractSystem {

	

	public TimerSystem() {
		super(GameComps.COMP_TIMER);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		TimerComponent timer=(TimerComponent) component;
		timer.currTime+=tpf;
		if (timer.currTime>=timer.limitTime){
			ent.attachComponents(timer.getAllComponents());
			ent.removeComponent(timer);
		}
	}

	
}

	