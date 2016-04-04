package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.DeathComponent;
import com.cristiano.java.gm.ecs.systems.AbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

/*O que fazer quando a entidade morre? Talvez tocar algum som? Animacao?*/
public class DeathSystem extends AbstractSystem {

	

	public DeathSystem() {
		super(GameComps.COMP_DEATH);
		
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		DeathComponent death=(DeathComponent)component;
		if (death.delay<=0){
			Log.info("Killing entity:"+ent);
			entMan.removeEntity(ent);
		} else {
			death.delay-=tpf;
		}
		
	}
}
