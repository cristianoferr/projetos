package com.cristiano.galactic.model.Entity;

import com.cristiano.galactic.model.Entity.Abstract.StationEntity;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.wares.Ware;

public class Station extends StationEntity {

	
	public Station(int id,DataManager dataManager,Ware shipWare, String name) {
		super(id,dataManager,shipWare, name);
		setType(EntityType.ET_STATION);
		 
		
	}
	

}
