package com.cristiano.java.gm.ecs.comps;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IManageElements;

public class ElementManagerComponent extends GameComponent {
	public IManageElements em=null;

	public ElementManagerComponent(){
		super(GameComps.COMP_ELEMENT_MANAGER);
		
	}
	
	@Override
	public void free() {
		super.free();
		em=null;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		ElementManagerComponent ret = new ElementManagerComponent();
		ret.em=em;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	public void init(IManageElements em) {
		this.em=em;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		//gameConsts=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.GAME_CONSTS));
	}
}
