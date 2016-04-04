package com.cristiano.java.gm.ecs.comps.effects;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class DamageEffectComponent extends GameComponent {

	public String damageType=null;

	public DamageEffectComponent(){
		super(GameComps.COMP_DMG_EFFECT);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		damageType=ge.getProperty(GameProperties.DAMAGE_TYPE);
	}	
	@Override
	public void free() {
		super.free();
		damageType=null;
	}

	@Override
	public IGameComponent clonaComponent() {
		DamageEffectComponent ret = new DamageEffectComponent();
		ret.damageType=damageType;
		
		return ret;
	}

	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.DAMAGE_TYPE,this.damageType);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		damageType = (String) obj.get(GameProperties.DAMAGE_TYPE);
		
	}
}
