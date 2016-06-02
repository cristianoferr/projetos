package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

/*
 * Target: entidade que pode ser selecionada, nem todas podem (tiros, por ex)
 * */
public class TargetableComponent extends GameComponent {

	public TargetableComponent(){
		super(GameComps.COMP_TARGETABLE);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		TargetableComponent ret = new TargetableComponent();
		return ret;
	}	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
	@Override
	public void resetComponent() {
	}
}