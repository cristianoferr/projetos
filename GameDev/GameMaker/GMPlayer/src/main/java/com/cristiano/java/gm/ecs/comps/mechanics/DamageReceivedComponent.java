package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class DamageReceivedComponent extends GameComponent {

	public String damageType=null;
	public float damage=0;
	public IGameEntity sentBy=null;
	public boolean applied=false;

	public DamageReceivedComponent(){
		super(GameComps.COMP_DAMAGE_RECEIVED);
	}
	@Override
	public void free() {
		super.free();
		sentBy=null;
		applied=false;
		damage=0;
		damageType=null;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		damage=ge.getPropertyAsFloat(GameProperties.DAMAGE);
		damageType=ge.getProperty(GameProperties.DAMAGE_TYPE);
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
