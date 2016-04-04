package com.cristiano.java.gm.ecs.comps.unit;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class UnitResourcesComponent extends GameComponent {

	public List<ResourceComponent> unitResources;

	public UnitResourcesComponent() {
		super(GameComps.COMP_UNIT_RESOURCES);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		List<IGameElement> elUnitResources = ge
				.getObjectList(GameProperties.UNIT_RESOURCES);
		unitResources = ResourceComponent
				.loadResources(elUnitResources, entMan,ge.getPropertyAsGE(GameProperties.GAME_OPPOSITION));
		if (unitResources.isEmpty()) {
			Log.fatal("No UnitResources loaded...");
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		UnitResourcesComponent ret = new UnitResourcesComponent();
		ret.unitResources = unitResources;
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}

	public void setResourceScope(String resource, int scope) {
		ResourceComponent res = getResource(resource);
		res.setScope(scope);
	}

	public ResourceComponent getResource(String resource) {
		for (ResourceComponent res : unitResources) {
			if (res.getIdentifier().equals(resource)) {
				return res;
			}
		}
		return null;
	}

	@Override
	public void resetComponent() {
	}

	public List<ResourceComponent> getResourcesWithScope(int scope) {
		List<ResourceComponent> ret = new ArrayList<ResourceComponent>();
		for (ResourceComponent res : unitResources) {
			if (res.getScope() == scope) {
				ret.add(res);
			}
		}
		return ret;
	}

}
