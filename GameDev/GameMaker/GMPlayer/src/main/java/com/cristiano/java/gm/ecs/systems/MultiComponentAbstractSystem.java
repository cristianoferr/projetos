package com.cristiano.java.gm.ecs.systems;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


/*Used when the system uses a tag instead of a identifier for the component*/
public abstract class MultiComponentAbstractSystem extends JMEAbstractSystem {

	private String compRequired2;
	protected static final List<IGameComponent> _reusableCompsFinal = new ArrayList<IGameComponent>();
	protected static final List<IGameEntity> _reusableEntsFinal = new ArrayList<IGameEntity>();

	public MultiComponentAbstractSystem(String compRequired,String compRequired2) {
		super(compRequired);
		this.compRequired2=compRequired2;
	}
	
	protected List<IGameComponent> retrieveComponents(String compClass, IGameEntity ent) {
		_reusableCompsFinal.clear();
		List<IGameComponent> comps = ent.getComponents(compClass,_reusableComps);
		_reusableCompsFinal.addAll(comps);
		comps = ent.getComponents(compRequired2,_reusableComps);
		_reusableCompsFinal.addAll(comps);
		return _reusableCompsFinal;
	}
	
	protected List<IGameEntity> retrieveEntities(String compClass) {
		_reusableEntsFinal.clear();
		List<IGameEntity> ents = entMan.getEntitiesWithComponentTag(compClass,_reusableEnts);
		_reusableEntsFinal.addAll(ents);
		ents = entMan.getEntitiesWithComponentTag(compRequired2,_reusableEnts);
		_reusableEntsFinal.addAll(ents);
		return _reusableEntsFinal;
	}
}
