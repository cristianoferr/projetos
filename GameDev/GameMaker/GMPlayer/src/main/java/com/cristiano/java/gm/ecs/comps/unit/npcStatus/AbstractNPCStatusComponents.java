package com.cristiano.java.gm.ecs.comps.unit.npcStatus;

import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.product.IGameElement;

public abstract class AbstractNPCStatusComponents extends GameComponent {

	public AbstractNPCStatusComponents(String tipo) {
		super(tipo);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	protected void completeStatus(AbstractNPCStatusComponents status) {

	}
	
	@Override
	public void free() {
		super.free();
	}

}
