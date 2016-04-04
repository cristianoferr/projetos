package com.cristiano.java.gm.ecs.comps.macro.directors;

import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.systems.macro.directors.DirectorSystem;
import com.cristiano.java.product.IGameElement;

public abstract class DirectorComponent extends GameComponent {
	
	public DirectorComponent(String tipo) {
		super(tipo);

	}
	
	@Override
	public void free() {
		super.free();
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	public void finishClone(DirectorComponent director){
		super.finishClone(director);
	}
	
	@Override
	public void resetComponent() {
	}

	public void update(DirectorSystem directorSystem, float tpf) {
		
	}
	
}
