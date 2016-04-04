package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

/*
 * Componente que tem um timer decrescente em segundos
 * */
public class TimerComponent extends GameComponent {

	public float initialTimer=0;
	public float currTime=0;
	public float limitTime=0;
	
	public TimerComponent(){
		super(GameComps.COMP_TIMER);
		//deactivate the ENTITY (not the component)
		deactivate();
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		deactivate();
	}
	
	@Override
	public IGameComponent clonaComponent() {
		return null;
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
