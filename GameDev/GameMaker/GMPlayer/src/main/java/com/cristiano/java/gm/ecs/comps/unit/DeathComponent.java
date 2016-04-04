package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class DeathComponent extends GameComponent {

	public float delay=0;//delay � o tempo que leva para remover a entidade do jogo
	
	public IGameElement actionDeath=null;

	public DeathComponent(){
		super(GameComps.COMP_DEATH);
		
	}
	
	@Override
	public void free() {
		super.free();
		delay=0;
		actionDeath=null;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		actionDeath=ge.getPropertyAsGE(GameProperties.ACTION_DEATH);
		delay=ge.getPropertyAsFloat(GameProperties.DELAY);
	}	

	@Override
	public IGameComponent clonaComponent() {
		return null;
	}
	
	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
}
