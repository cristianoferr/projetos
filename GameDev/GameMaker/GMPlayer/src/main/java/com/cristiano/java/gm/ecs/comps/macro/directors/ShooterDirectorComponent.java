package com.cristiano.java.gm.ecs.comps.macro.directors;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public abstract class ShooterDirectorComponent extends DirectorComponent{
	
	public ShooterDirectorComponent(String tipo) {
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

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}
	

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}
	
	
	
	@Override
	public IGameComponent clonaComponent() {
		ShooterDirectorComponent ret = (ShooterDirectorComponent) entMan.spawnComponent(GameComps.COMP_DIRECTOR_SHOOTER);
		ret.setEntityManager(entMan);
		finishClone(ret);
		return ret;
	}

	@Override
	public void resetComponent() {
		super.resetComponent();
	}
	
}
