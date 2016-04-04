package com.cristiano.java.bpM.entidade.blueprint;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.product.IGameElement;

public class Mod extends Blueprint {

	public Mod(ElementManager elementManager, IGameElement base) {
		super(elementManager, base);
		setObjectType(Extras.OBJECT_TYPE_MOD);
	}

	public String id() {
		return "MOD" + super.ID;
	}

}
