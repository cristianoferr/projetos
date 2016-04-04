package com.cristiano.java.gm.ecs.systems;

import java.util.List;

import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


/*Used when the system uses a tag instead of a identifier for the component*/
public abstract class GenericComponentAbstractSystem extends JMEAbstractSystem {

	public GenericComponentAbstractSystem(String compRequired) {
		super(compRequired);
	}
	
	protected List<IGameComponent> retrieveComponents(String compClass, IGameEntity ent) {
		List<IGameComponent> comps = ent.getComponents(compClass,_reusableComps);
		return comps;
	}
	
	protected List<IGameEntity> retrieveEntities(String compClass) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponentTag(compClass,_reusableEnts);
		return ents;
	}
}
