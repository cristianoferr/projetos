package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.utils.Log;


public abstract class BuilderSystem extends JMEAbstractSystem {

	public BuilderSystem(String compRequired) {
		super(compRequired);
	}
	
	public ElementManager getElementManagerBuilder() {
		if (em == null) {
			ElementManagerComponent comp = (ElementManagerComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_ELEMENT_MANAGER);
			if (comp == null) {
				Log.error("ElementManagerComponent is null");
				return null;
			}
			em = comp.em;
		}
		return (ElementManager) em;
	}
}
