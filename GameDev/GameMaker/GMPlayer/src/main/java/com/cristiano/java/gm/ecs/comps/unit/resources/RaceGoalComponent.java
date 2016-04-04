package com.cristiano.java.gm.ecs.comps.unit.resources;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.QueryBestiaryComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

/*
 * This component is responsible for generating/storing waypoints for the current race objective
 * The value is the roomID for the next room 
 * */
public class RaceGoalComponent extends ResourceComponent {
	public final List<IGameComponent> waypoints = new ArrayList<IGameComponent>();
	public Vector3f currDestination = null;

	public PositionComponent _entityPos;
	// used to show the distance... no need to save
	public final Vector3f relDistance = new Vector3f();

	public float idealDistance = 10;
	public boolean applyArrow = false;

	// used to draw an arrow above the player...
	public IGameEntity arrowEntity;
	public OrientationComponent arrowOrientation = null;
	public PositionComponent arrowPosition;
	public QueryBestiaryComponent arrowQuery;
	public String arrowTag;
	public float arrowScale;
	public float arrowX;
	public float arrowY;
	public float arrowDistance;
	public RenderComponent _renderArrow;
	public RenderComponent _renderComp;

	public RaceGoalComponent() {
		super(GameComps.COMP_RESOURCE_RACE_GOAL);
	}

	@Override
	public void free() {
		super.free();
		_renderArrow = null;
		_renderComp = null;
		arrowEntity = null;
		waypoints.clear();
		currDestination = null;
		arrowEntity = null;
		arrowOrientation = null;
		arrowPosition = null;
		arrowQuery = null;
		arrowTag = null;
		arrowScale = 1;
		arrowX = 0;
		arrowY = 0;
		arrowDistance = 0;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		idealDistance = ge.getPropertyAsFloat(GameProperties.IDEAL_DISTANCE);
		arrowTag = ge.getProperty(GameProperties.ARROW_TAG);
		arrowScale = ge.getPropertyAsFloat(GameProperties.ARROW_SCALE);
		arrowX = ge.getPropertyAsFloat(GameProperties.ARROW_X);
		arrowY = ge.getPropertyAsFloat(GameProperties.ARROW_Y);
		arrowDistance = ge.getPropertyAsFloat(GameProperties.ARROW_DISTANCE);
		if ("".equals(arrowTag)) {
			arrowTag = null;
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		RaceGoalComponent ret = (RaceGoalComponent) entMan.spawnComponent(GameComps.COMP_RESOURCE_RACE_GOAL);
		ret.idealDistance = idealDistance;
		ret.arrowTag = arrowTag;
		ret.arrowScale = arrowScale;
		ret.arrowDistance = arrowDistance;
		ret.arrowX = arrowX;
		ret.arrowY = arrowY;
		finishClone(ret);
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public boolean isEmpty() {
		if (waypoints == null) {
			return true;
		}
		return waypoints.isEmpty();
	}
}
