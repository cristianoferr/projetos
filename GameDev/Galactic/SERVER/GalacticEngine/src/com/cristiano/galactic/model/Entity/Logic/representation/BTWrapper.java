package com.cristiano.galactic.model.Entity.Logic.representation;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;

public class BTWrapper {
	private IContext context;
	ArtificialEntity entityOwner;

	public BTWrapper(ArtificialEntity entityOwner){
		this.entityOwner=entityOwner;
		context=entityOwner.getDataManager().createBTContext();
		context.setVariable("CurrentEntityID",entityOwner);

	}
	
public IContext getContext() {
		
		return context;
	}




}
