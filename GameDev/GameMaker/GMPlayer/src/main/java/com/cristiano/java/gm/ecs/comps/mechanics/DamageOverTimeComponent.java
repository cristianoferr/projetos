package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class DamageOverTimeComponent extends GameComponent {

	public String damageType="generic";
	public float damageLeft=0;
	public float damageSecond=1;//qtd de dano ser� aplicadado a cada segundo
	public IGameEntity sentBy=null;

	public DamageOverTimeComponent(){
		super(GameComps.COMP_DAMAGE_OVER_TIME);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		damageType=ge.getProperty(GameProperties.DAMAGE_TYPE);
	}
	@Override
	public void free() {
		super.free();
		sentBy=null;
		
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
