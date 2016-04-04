package com.cristiano.galactic.controller.messenger;

import com.cristiano.galactic.model.Entity.Abstract.Item;

public class EvtUpdate extends ObjUpdate {

	public EvtUpdate(Item from,String property,Object obj) {
		super(from);
	}

}
