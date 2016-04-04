package com.cristiano.java.gm.builder.map;

import java.util.List;
import java.util.Random;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.map.LandmarksComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public abstract class GenericMapSolver extends AbstractSolveMap implements
		ISolveMap {

	public float legDistance;
	public IGameElement gameAxis;
	public String axis;
	public int roadSize;
	public LandmarksComponent landMark;
	public RoomComponent room, prevRoom;
	public int rooms = 0;
	public float minLength;
	public float groundHeight;

	@Override
	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation,
			IGameElement elSolver, GameGenreComponent genre) {
		super.startFrom(mapC, mapLocation, elSolver, genre);

		// mapC.maxRooms;
		room = createNewRoom();
		
		groundHeight=mapC.roomFloorHeight;

		legDistance = mapC.lapDistance / mapC.maxRooms;

		minLength = mapLocation.mapLocation
				.getPropertyAsFloat(GameProperties.MIN_LENGTH);

		gameAxis = genre.elGameAxis;

		axis = BPUtils
				.clear(gameAxis.getProperty(GameProperties.AXIS_MOVEMENT));

		roadSize = mapC.roadWidth;

		initDimensions();

		landMark = (LandmarksComponent) mapLocal
				.getComponentWithTag(GameComps.COMP_LANDMARKS);
		landMark.random = new Random(seed);

	}

	protected void initDimensions() {
		Vector3f buildDimension = room.getDimensions();
		buildDimension.x = gameAxis.getPropertyAsFloat(GameProperties.ALLOW_X)
				* roadSize;
		buildDimension.y = gameAxis.getPropertyAsFloat(GameProperties.ALLOW_Y)
				* roadSize;
		buildDimension.z = gameAxis.getPropertyAsFloat(GameProperties.ALLOW_Z)
				* roadSize;
		if (buildDimension.x < minLength) {
			buildDimension.x = minLength;
		}
		if (buildDimension.y < minLength) {
			buildDimension.y = minLength;
		}
		if (buildDimension.z < minLength) {
			buildDimension.z = minLength;
		}

		if (axis.toUpperCase().contains("X")) {
			buildDimension.x = legDistance;
		}
		if (axis.toUpperCase().contains("Y")) {
			buildDimension.y = legDistance;
		}
		if (axis.toUpperCase().contains("Z")) {
			buildDimension.z = legDistance;
		}
	}

	@Override
	public boolean hasCompleted() {
		// addStaticElement(LogicConsts.TAG_MESH_WALL, entMan, em, ent, new
		// Vector3f(w, h, d), new Vector3f(x, y, z));
		// BuilderUtils.addWall(entMan,(ElementManager)
		// elSolver.getElementManager(),
		// room,20,60,mapC.getLength()/4,room.getPosition().x/2,0,room.getPosition().z/2);
		// BuilderUtils.addStaticElement(LogicConsts.TAG_MESH_WALL, entMan,
		// elSolver.getElementManager(), room, new Vector3f(w, h, d), new
		// Vector3f(x, y, z));

		addObstacles(room);

		addLandmarks(room);
		rooms++;
		if (rooms < mapC.maxRooms) {
			room = createNewRoom();
		}
		prevRoom = room;
		return rooms >= mapC.maxRooms;
	}

	protected void addLandmarks(RoomComponent room) {

	}

	protected void addObstacles(RoomComponent room) {

		int qtdObjects = 0;
		while (qtdObjects < landMark.getMaxObstacles()) {
			addObstacle(room);
			qtdObjects++;
		}
	}

	private void addObstacle(RoomComponent room) {
		IGameElement obstacle = landMark.getRandomObstacle();
		String mesh = obstacle.getProperty(GameProperties.MESH_TAG);
		Vector3f dimension;
		Vector3f pos;
		do {

			dimension = getValidDimension(true, obstacle);
			pos = getRandomPos(true, dimension,obstacle);
		} while (!checkIntersection(dimension, pos));
		
		
		addStaticElement(room, dimension, mesh, pos);
	}

	private boolean checkIntersection(Vector3f dimension, Vector3f pos) {
		// JMEUtils.boxIntersection(
		List<IGameComponent> comps = room.getComponents(GameComps.COMP_CHILD);
		for (IGameComponent comp : comps) {
			ChildComponent child = (ChildComponent) comp;
			Vector3f dim = child.getElementDimension();
			Vector3f posc = child.getElementPosition();
			if (JMEUtils.boxIntersection(pos, dimension, posc, dim)) {
				Log.debug("Intersection on " + dimension + "::" + pos + "=>"
						+ dim + "::" + posc + "");
				return false;
			}
		}
		return true;
	}

	public Vector3f getValidDimension(boolean inside, IGameElement obstacle) {
		Vector3f buildDimension = room.getDimensions();

		Vector3f dimension = landMark.getDoodadDimension(obstacle,
				inside ? landMark.obstacleInfo : landMark.landMarkInfo);
		if (dimension.x < minLength) {
			dimension.x = minLength;
		}
		if (dimension.y < minLength) {
			dimension.y = minLength;
		}
		if (dimension.z < minLength) {
			dimension.z = minLength;
		}
		if (dimension.x > buildDimension.x) {
			dimension.x = buildDimension.x;
		}
		if (dimension.y > buildDimension.y) {
			dimension.y = buildDimension.y;
		}
		if (dimension.z > buildDimension.z) {
			dimension.z = buildDimension.z;
		}
		return dimension;
	}

	public Vector3f getRandomPos(boolean inside, Vector3f dimension, IGameElement obstacle) {
		Vector3f buildDimension = room.getDimensions();
		
		boolean placeSky=obstacle.getPropertyAsBoolean(GameProperties.PLACE_SKY);

		Vector3f minPos = new Vector3f(dimension.x / 2, dimension.z / 2,
				dimension.z / 2);
		Vector3f maxPos = new Vector3f(buildDimension.x - dimension.x / 2,
				buildDimension.y - dimension.z / 2, buildDimension.z
						- dimension.z / 2);
		Vector3f dif = maxPos.subtract(minPos);
		dif.x = CRJavaUtils.random(0, dif.x);
		dif.y = CRJavaUtils.random(0, dif.y);
		dif.z = CRJavaUtils.random(0, dif.z);

		Vector3f pos = minPos.add(dif);
		
		if (!placeSky){
			pos.y=groundHeight;//+dimension.y/2;
		}
		
		Vector3f difToEnd=maxPos.subtract(mapC.getLength(),0,mapC.getLength());
		
		pos.subtractLocal(difToEnd.mult(0.5f));
		return pos;
	}

}
