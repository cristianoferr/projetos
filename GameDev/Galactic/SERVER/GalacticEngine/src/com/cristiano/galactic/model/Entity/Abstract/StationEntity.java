package com.cristiano.galactic.model.Entity.Abstract;

import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.wares.Ware;


public abstract class StationEntity extends ArtificialEntity {

	public StationEntity(int id,DataManager dataManager,Ware shipWare, 
			String name) {
		super(id,dataManager,shipWare,name);
		
	}

}
