package com.cristiano.java.gm.ecs.systems.unit.resources;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.macro.QueryBestiaryComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.map.WaypointComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.RaceGoalComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

/**
 * 
 * @author CMM4
 * 
 * 
 *         Test: TestNPCStuff
 */
public class RaceGoalSystem extends JMEAbstractSystem {

	public RaceGoalSystem() {
		super(GameComps.COMP_RESOURCE_RACE_GOAL);
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		RaceGoalComponent goal = (RaceGoalComponent) component;
		if (goal.firstTick) {
			if (!ent.containsComponent(GameComps.COMP_TRANSIENT)) {
				return;
			}
			goal.firstTick = false;
			goal.applyArrow = isPlayer(ent);
			if (goal.applyArrow) {
				requestArrow(ent, goal);
			}
		}

		if (goal.isEmpty()) {
			Log.info("No waypoints defined, calculating waypoints...");
			goal._entityPos = (PositionComponent) ECS.getPositionComponent(ent);
			calculateWaypoints(ent, goal);

		} else {
			if (goal._entityPos == null) {
				Log.error("No PositionComponent defined for entity:" + ent);
				return;
			}
			updateCurrentDestination(ent, goal);
			checkDistanceToWaypoint(ent, goal);
		}
	}

	private void initArrow(RaceGoalComponent goal, IGameEntity arrowEntity, IGameEntity ent) {
		PositionComponent posComp = (PositionComponent) arrowEntity.getComponentWithIdentifier(GameComps.COMP_POSITION);
		PositionComponent entPos = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
		RenderComponent renderArrow = (RenderComponent) arrowEntity.getComponentWithIdentifier(GameComps.COMP_RENDER);
		RenderComponent renderComp = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
		renderArrow.node.removeFromParent();
		goal._renderArrow = renderArrow;
		if (renderComp == null) {
			Log.fatal("Render component is null for entity:" + ent);
		}
		goal._renderComp = renderComp;
		// renderComp.node.setLocalTranslation(new
		// Vector3f(goal.arrowX*game.getWidth(), goal.arrowY*game.getHeight(),
		// 1));
		goal.arrowPosition = posComp;
		if (posComp == entPos) {
			Log.fatal("Arrow Position = entity position!");
		}

		renderArrow.node.setLocalScale(goal.arrowScale);
	}

	private void checkQuery(IGameEntity ent, RaceGoalComponent goal) {
		if (goal.arrowQuery.readyToDeliver()) {

			goal.arrowEntity = goal.arrowQuery.retrieveEntity();
			if (goal.arrowEntity == ent) {
				Log.fatal("ArrowEntity = Entity!");
			}
			goal.arrowQuery.selfRemoval = true;
			goal.arrowQuery = null;
			Log.info("Arrow Entity was delivered...(" + goal.arrowEntity + ") query:" + goal.arrowQuery);

		}

	}

	private void requestArrow(IGameEntity ent, RaceGoalComponent goal) {
		if (goal.arrowTag == null) {
			Log.info("No Arrow to be displayed...");
			return;
		}
		goal.arrowQuery = (QueryBestiaryComponent) entMan.addComponent(GameComps.COMP_BESTIARY_QUERY, ent);
		goal.arrowQuery.budgetToSpend = 0;
		goal.arrowQuery.selfRemoval = false;
		goal.arrowQuery.entityType = LogicConsts.ENTITY_STATIC;
		goal.arrowQuery.tagSource = goal.arrowTag;

		PositionComponent posC = (PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, goal.arrowQuery);
		posC.setPos(new Vector3f(0, 10, 0));

		// ParentEntityComponent parent = (ParentEntityComponent)
		// entMan.addComponent(GameComps.COMP_PARENT_ENTITY,
		// goal.arrowQuery);
		// parent.parent = ent;

	}

	private void checkDistanceToWaypoint(IGameEntity ent, RaceGoalComponent goal) {

		float distance = calcDistanceWaypoint(goal);
		// Log.info(ent+"=> dist:  "+distance);
		if (distance < 0) {
			Log.fatal("Distance between waypoint is " + distance + "!");
		}

		checkArrow(ent, goal);

		if (distance < goal.idealDistance) {
			goal.currDestination = null;
			goal.waypoints.remove(0);
			String s = "";
			if (goal.waypoints.size() > 0) {
				WaypointComponent waypointComponent = (WaypointComponent) goal.waypoints.get(0);
				s = " Position:" + waypointComponent.position;
				if (isPlayer(ent)){
					game.addDebugBox(waypointComponent.position.add(0, 5, 0), "goal");
				}
				
			}
			Log.info("Waypoint reached, moving to next... distance:" + distance + s);
		}

	}

	public static float calcDistanceWaypoint(RaceGoalComponent goal) {
		if (goal._entityPos == null) {
			return -1;
		}
		if (goal.currDestination == null) {
			return -2;
		}
		goal.relDistance.set(goal._entityPos.getPos()).subtractLocal(goal.currDestination);
		float distance = goal.relDistance.length();
		return distance;
	}

	private void checkArrow(IGameEntity ent, RaceGoalComponent goal) {
		if (goal.arrowTag == null) {
			return;
		}
		if (!goal.applyArrow) {
			return;
		}
		if (goal.arrowEntity == null) {
			if (goal.arrowQuery != null) {
				checkQuery(ent, goal);
			} else {
				Log.error("arrowQuery wasnt generated...");
			}
			// Log.warn("Arrow is undefined...");
			return;
		}
		if (goal.arrowOrientation == null) {
			goal.arrowOrientation = (OrientationComponent) goal.arrowEntity.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);

			if (goal.arrowOrientation == null) {
				return;
			}

			goal.arrowOrientation.check(goal.arrowEntity);
			initArrow(goal, goal.arrowEntity, ent);
		}

		if (goal._renderArrow != null) {
			attachArrow(ent, goal);
		}

		Vector3f pos = goal._entityPos.getPos();

		// Log.debug("Dist:"+goal.relDistance.length());
		float angle = CRMathUtils.calcDegreesXZ2(Vector3f.ZERO, goal.relDistance);
		goal.relDistance.normalizeLocal().multLocal(goal.arrowDistance);
		goal.relDistance.x = pos.x - goal.relDistance.x;
		goal.relDistance.y = pos.y - goal.relDistance.y;
		goal.relDistance.z = pos.z - goal.relDistance.z;
		goal.arrowOrientation.fromAngleAxis(-angle, Vector3f.UNIT_Y);

		// goal.arrowPosition.setPos(goal.currDestination);
		goal.arrowPosition.setPos(goal.relDistance);

		// Log.debug("angle:"+angle+" ");
	}

	private void attachArrow(IGameEntity ent, RaceGoalComponent goal) {
		if (goal._renderComp.node.getParent() != null) {
			goal._renderComp.node.getParent().attachChild(goal._renderArrow.node);
			goal._renderComp = null;
			goal._renderArrow = null;
		}

	}

	private boolean updateCurrentDestination(IGameEntity ent, RaceGoalComponent goal) {
		if (goal.currDestination == null) {
			if (goal.waypoints.isEmpty()) {
				Log.warn("Waypoints is empty.");
				return false;
			}
			WaypointComponent way = (WaypointComponent) goal.waypoints.get(0);
			goal.currDestination = way.position;
		}
		return true;
	}

	/*
	 * When I dont know the waypoints...
	 */
	public void calculateWaypoints(IGameEntity ent, RaceGoalComponent goal) {
		MapComponent map = getMap();
		RoomComponent roomCurr = ECS.getRoomWithId(map, (int) goal.currValue);
		goal.incValue();

		RoomComponent roomDest = getNextRoom(ent, goal, map);
		Log.info("RoomReached:  " + roomCurr.roomId + "=>" + roomDest.roomId);
		RoadComponent road = ECS.findRoadInCommon(roomDest, roomCurr);
		if (road == null) {
			Log.fatal("No road between rooms!");
			return;
		}
		List<IGameComponent> waypoints = ECS.getWaypointsFrom(road, goal.waypoints);
		if (waypoints.isEmpty()) {
			Log.fatal("No Waypoints on road:" + road);
			return;
		}

		Vector3f destPosition = roomDest.getPosition();
		Vector3f pos = goal._entityPos.getPos();
		waypoints = sortWayPoints(waypoints, pos);
		removeInvalidWaypoints(pos, waypoints, destPosition);
	}

	private RoomComponent getNextRoom(IGameEntity ent, RaceGoalComponent goal, MapComponent map) {
		RoomComponent roomDest = ECS.getRoomWithId(map, (int) goal.getCurrValue());
		if (roomDest == null) {
			LapResourceComponent lapC = ECS.getLapResource(ent);
			if (lapC == null) {
				Log.error("LapResource is null on entity: " + ent);
			}
			lapC.incValue();
			Log.info("Increasing lap for " + ent + ", it is now:" + lapC.currValue);
			goal.currValue = 0;
			return getNextRoom(ent, goal, map);
		}
		return roomDest;
	}

	// I remove waypoints which were already visited (checking distance, if
	// distance>entDistance then remove)
	private void removeInvalidWaypoints(Vector3f entPos, List<IGameComponent> waypoints, Vector3f destPosition) {
		float entDist = entPos.distance(destPosition);
		for (int i = waypoints.size() - 1; i >= 0; i--) {
			IGameComponent comp = waypoints.get(i);
			WaypointComponent way = (WaypointComponent) comp;
			float dist = way.position.distance(destPosition);
			if (dist > entDist) {
				Log.debug("Removing waypoint with distance: " + dist + " (bigger than " + entDist + ")");
				waypoints.remove(i);
			}
		}
	}

	/*
	 * I check the correct order of waypoints to reach the position... the first
	 * waypoint is the one near from the target
	 */
	public List<IGameComponent> sortWayPoints(List<IGameComponent> waypoints, Vector3f entPos) {
		int size=waypoints.size();
		for (int i=0;i<size-1;i++){
			for (int j=i+1;j<size;j++){
				WaypointComponent  wayI = (WaypointComponent) waypoints.get(i);
				WaypointComponent  wayJ = (WaypointComponent) waypoints.get(j);
				float distI=wayI.position.distance(entPos);
				float distJ=wayJ.position.distance(entPos);
				if (distI>distJ){
					waypoints.set(i, wayJ);
					waypoints.set(j, wayI);
				}
			}
		}
		/*
		WaypointComponent wayInitial = (WaypointComponent) waypoints.get(0);
		WaypointComponent wayFinal = (WaypointComponent) waypoints.get(waypoints.size() - 1);
		float distIni = wayInitial.position.distance(entPos);
		float distFim = wayFinal.position.distance(entPos);
		if (distIni > distFim) {
			return waypoints;
		}
		// reordering...
		List<IGameComponent> ret = new ArrayList<IGameComponent>();
		for (int i = 0; i < waypoints.size(); i++) {
			ret.add(0, waypoints.get(i));
		}*/
		return waypoints;
	}

}
