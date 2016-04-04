package com.cristiano.galactic.model.Entity.Logic.representation;

import com.cristiano.cyclone.utils.GenericDTO;
import com.cristiano.cyclone.utils.Vector3;

public class MemoryDTO extends GenericDTO{


/*	public void addItem(Item item){
		addParam(item);	
	}*/

	public void addItem(MemoryItem item){
		addParam(item);
	}


	public MemoryItem getItem(int i){
		Object obj=getParam(i);
		if (obj instanceof MemoryItem)
			return (MemoryItem)obj;
		return null;
	}
	
	
	public Vector3 getVector(int i){
		Object obj=getParam(i);
		if (obj instanceof Vector3)
		return (Vector3)obj;
		return null;
	}
}
