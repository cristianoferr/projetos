package com.cristiano.galactic.model.Entity.Abstract;

import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.wares.Ware;


public abstract class AbstractMovableEntity extends ArtificialEntity {

	public AbstractMovableEntity(int id,DataManager dataManager,Ware shipWare, 
			String name) {
		super(id,dataManager,shipWare, name);
		setType(EntityType.ET_MOVABLE_OBJ);
		
		
	}

}
