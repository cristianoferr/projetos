package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

public class LoadEntitySystem extends AbstractSystem {

	public LoadEntitySystem() {
		super(GameComps.COMP_CHILD);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (component.isFirstTick()) {

			ChildComponent comp = (ChildComponent) component;
			if (comp.load) {
				Log.debug("Carregando entidade:"+comp);
				loadComponents(comp, ent);
			}
			comp.firstTick = false;
		}

	}

	private void loadComponents(ChildComponent comp, IGameEntity ent) {
		if (comp.elementSource != null) {
			factory.loadComponents(comp, comp.elementSource);
		} else if (comp.tag != null) {
			factory.loadComponents(comp, comp.tag);
		} else {
			Log.fatal("Undefined how to load components...");
		}

	}

}
