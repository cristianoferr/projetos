package com.cristiano.java.gm.ecs.comps.macro;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class VictoryCheckInitComponent extends ResourceComponent {

	public VictoryCheckInitComponent() {
		super(GameComps.COMP_VICTORY_CHECKER_INIT);

	}

	@Override
	public void loadFromElement(IGameElement el) {
		super.loadFromElement(el);
	}

	@Override
	public IGameComponent clonaComponent() {
		VictoryCheckInitComponent ret = (VictoryCheckInitComponent) entMan.spawnComponent(ident);
		return ret;
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
		Log.debug("victorycheckinit");
	}

}
