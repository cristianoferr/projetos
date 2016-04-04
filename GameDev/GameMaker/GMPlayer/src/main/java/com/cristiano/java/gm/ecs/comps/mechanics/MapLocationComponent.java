package com.cristiano.java.gm.ecs.comps.mechanics;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class MapLocationComponent extends GameComponent {
	public IGameElement mapLocation;
	public String roomBubbleFilter; // becomes bubbleFilter in the bubble
	public String mapSolverFilter;

	public MapLocationComponent() {
		super(GameComps.COMP_MAP_LOCATION);
	}

	@Override
	public void free() {
		super.free();
		mapLocation = null;
		roomBubbleFilter = null;
		mapSolverFilter = null;
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.mapLocation = ge.getPropertyAsGE(GameProperties.MAP_LOCATION);
		if (mapLocation == null) {
			Log.fatal("MapLocation property is null.");
		}
		this.roomBubbleFilter = mapLocation
				.getProperty(GameProperties.ROOM_BUBBLE_FILTER);
		this.mapSolverFilter = mapLocation
				.getProperty(GameProperties.MAP_SOLVER_FILTER);
		IGameElement landmarks = mapLocation
				.getPropertyAsGE(GameProperties.LANDMARKS);
		if (!CRJavaUtils.isRelease()) {
			if (landmarks != null) {
				IGameComponent landC = entMan.addComponent(
						GameComps.COMP_LANDMARKS, this);
				landC.loadFromElement(landmarks);
			}
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		MapLocationComponent ret = new MapLocationComponent();
		finishClone(ret);
		ret.mapLocation = mapLocation;
		ret.roomBubbleFilter = roomBubbleFilter;
		ret.mapSolverFilter = mapSolverFilter;
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
		loadFromElement(getElement());
	}

}
