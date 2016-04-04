package com.cristiano.java.gm.ecs.systems.macro.directors;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.directors.DirectorComponent;
import com.cristiano.java.gm.ecs.systems.GenericComponentAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


public class DirectorSystem extends GenericComponentAbstractSystem{

	public DirectorSystem() {
		super(GameComps.COMP_DIRECTOR);
	}

	@Override  //TODO: ent=gameEntity?
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		DirectorComponent comp=(DirectorComponent) component;
		if (comp.firstTick){
			comp.firstTick=false;
		}
		
		comp.update(this,tpf);
	}

	
}
