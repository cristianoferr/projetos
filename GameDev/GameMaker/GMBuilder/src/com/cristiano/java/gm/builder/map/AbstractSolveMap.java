package com.cristiano.java.gm.builder.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public abstract class AbstractSolveMap implements ISolveMap {

	private static final int LINE_POINTS = 100;
	protected EntityManager entMan;

	protected RoomComponent roomInicial;
	protected RoomComponent roomFinal;

	// TODO: remover essa referencias...
	CRDebugDraw draw;

	protected IGameElement elSolver;
	protected MapComponent mapC;
	protected MapLocationComponent mapLocal;
	protected GameGenreComponent genre;
	protected int seed;
	protected ElementManager em;

	public void initVars(EntityManager entMan) {
		this.entMan = entMan;

	}

	protected void defineInitialRoomsPositions() {
		int valX = (int) CRJavaUtils.random(0, mapC.length / 4f);
		int valZ = (int) CRJavaUtils.random(0, mapC.length / 4f);
		int quad = (int) CRJavaUtils.random(0, 4);
		int ix = 0, iz = 0, fx = 0, fz = 0;
		if (quad == 0) {
			ix = valX;
			iz = valZ;
			fx = mapC.length - valX;
			fz = mapC.length - valZ;
		}
		if (quad == 1) {
			ix = mapC.length - valX;
			iz = valZ;
			fx = valX;
			fz = mapC.length - valZ;
		}
		if (quad == 2) {
			ix = valX;
			iz = mapC.length - valZ;
			fx = mapC.length - valX;
			fz = valZ;
		}
		if (quad == 3) {
			fx = valX;
			fz = valZ;
			ix = mapC.length - valX;
			iz = mapC.length - valZ;
		}
		if (roomInicial != null) {
			roomInicial.setPosition(ix, getTerrainHeightAt(ix, iz), iz);
		}
		if (roomFinal != null) {
			roomInicial.setPosition(fx, getTerrainHeightAt(fx, fz), fz);
		}
	}

	protected void addStaticElement(RoomComponent room, Vector3f dimension,
			String mesh, Vector3f pos) {
		Log.info("Adding static element with mesh '"+mesh+"' at pos:"+pos+"  with dimension:"+dimension);
		BuilderUtils.addStaticElement(mesh, entMan, em, room, dimension, pos);
	}


	// Called last by the solvers...
	protected void finishSolving() {
		List<IGameComponent> rooms = getRooms();
		int countStarting = 0;
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			if (room.startingRoom) {
				countStarting++;
			}
		}

		if (countStarting != 1) {
			Log.error("The number of starting rooms is different from 1:" + countStarting);
		}
	}

	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre) {
		this.elSolver = elSolver;
		this.genre=genre;
		this.mapC = mapC;
		this.em=(ElementManager)elSolver.getElementManager();
		this.seed=mapC.seed;
		this.mapLocal = mapLocation;
		if (mapLocal == null) {
			Log.error("MapLocation is null.");
		}
		initDebugDraw();
	}

	protected void createInitialRooms(boolean link) {
		roomInicial = createNewRoom();
		roomInicial.startingRoom = true;
		roomFinal = createNewRoom();
		roomFinal.endingRoom = true;
		if (link) {
			ComponentRecipes.linkRooms(entMan,roomInicial, roomFinal);
			roomFinal.parentRoom = roomInicial.roomId;
		}
	}

	protected List<IGameEntity> getConnectedRoomsToRoad(RoadComponent road) {
		List<IGameEntity> connectedRooms = entMan.getEntitiesWithComponent(road);

		return connectedRooms;
	}

	

	public RoomComponent createNewRoom() {
		RoomComponent room = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		room.floorHeight = mapC.roomFloorHeight;
		room.bubbleFilter = mapLocal.roomBubbleFilter;
		room.roomId=countRooms()-1;
		float mult=mapC.length;
		if (!mapC.isRoomSizeRelative){
			mult=1;
		}
		entMan.addComponent(GameComps.COMP_RENDER,room);
		room.setDimension(new Vector3f(CRJavaUtils.random(mapC.minRoomSize, mapC.maxRoomSize) * mult, 0,
				CRJavaUtils.random(mapC.minRoomSize, mapC.maxRoomSize) * mult));
		Log.debug("Creating new Room:  " + room);

		return room;
	}

	protected int countRooms() {
		List<IGameComponent> components = getRooms();
		return components.size();
	}

	public List<IGameComponent> getRooms() {
		return ECS.getRooms(mapC);
	}

	public List<IGameComponent> getRoads() {
		List<IGameComponent> rooms = getRooms();
		List<IGameComponent> roads = new ArrayList<IGameComponent>();
		for (IGameComponent room : rooms) {
			List<IGameComponent> comps = room.getComponentsWithIdentifier(GameComps.COMP_ROAD);
			for (IGameComponent comp : comps) {
				if (!roads.contains(comp)) {
					roads.add(comp);
				}
			}
		}
		return roads;
	}

	@Override
	public void finishGeneration() {
		// finishDebugDraw("unsolvedMap");
		simplificaRoads();
		// listLinks();
		finishDebugDraw("solvedMap");
	}

	// se uma road indo de uma room �� outra atravessa rooms, ent��o eu
	// removo a
	// road e adiciono roads linkando a room...
	protected void simplificaRoads() {
		Log.debug("Simplificando...");
		List<IGameComponent> rooms = getRooms();
		boolean crossingRoad = false;
		do {
			Log.debug("----");
			removeDuplicados(rooms);
			crossingRoad = checkRoads(rooms);
		} while (crossingRoad);
	}

	private void removeDuplicados(List<IGameComponent> rooms) {
		List<IGameComponent> roads = getRoads();
		List<IGameComponent> roadsToRemove = new ArrayList<IGameComponent>();
		for (int i = 0; i < roads.size() - 1; i++) {
			IGameComponent roadI = roads.get(i);
			for (int j = i + 1; j < roads.size(); j++) {
				IGameComponent roadJ = roads.get(j);
				if (areRoadsEquals(roadI, roadJ)) {
					roadsToRemove.add(roadJ);
				}
			}
		}
		removeRoads(roadsToRemove);
	}

	private void removeRoads(List<IGameComponent> roadsToRemove) {
		for (IGameComponent comp : roadsToRemove) {
			entMan.removeComponent(comp);
			entMan.removeEntity(comp);
		}
	}

	private boolean areRoadsEquals(IGameComponent roadI, IGameComponent roadJ) {
		List<IGameEntity> entsJ = entMan.getEntitiesWithComponent(roadI);
		List<IGameEntity> entsI = entMan.getEntitiesWithComponent(roadJ);
		int t = 0;
		for (IGameEntity entI : entsI) {
			for (IGameEntity entJ : entsJ) {
				if (entI == entJ)
					t++;
			}

		}
		return t == 2;
	}

	private boolean checkRoads(List<IGameComponent> rooms) {
		List<IGameComponent> roads = getRoads();
		boolean ret = false;
		for (IGameComponent road : roads) {
			List<IGameEntity> roadRooms = entMan.getEntitiesWithComponent(road);
			RoomComponent crossedRoom = checkRoadAgainstRooms((RoadComponent) road, rooms, roadRooms);
			if (crossedRoom != null) {
				ret = true;
			}
		}
		return ret;
	}

	// verifico se houve interseções...
	public RoomComponent checkRoadAgainstRooms(RoadComponent road, List<IGameComponent> rooms,
			List<IGameEntity> roadRooms) {
		RoomComponent roomI = (RoomComponent) roadRooms.get(0);
		RoomComponent roomF = (RoomComponent) roadRooms.get(1);
		Vector3f ptInicial = roomI.getPosition();
		Vector3f ptFinal = roomF.getPosition();
		Vector3f ptDif = ptFinal.subtract(ptInicial).divide(LINE_POINTS);
		for (IGameComponent comp : rooms) {
			if (!roadRooms.contains(comp)) {
				RoomComponent crossedRoom = (RoomComponent) comp;
				if (checkRoadOnRoom(new Vector3f(ptInicial), ptDif, crossedRoom)) {
					relinkRoad(road, roadRooms, crossedRoom);
					return crossedRoom;
				}
			}
		}
		return null;
	}

	private boolean checkRoadOnRoom(Vector3f ptInicial, Vector3f ptDif, RoomComponent crossedRoom) {
		for (int i = 0; i < LINE_POINTS; i++) {
			ptInicial.addLocal(ptDif);
			if (crossedRoom.isPointInside(ptInicial)) {
				return true;
			}
		}
		return false;
	}

	// removo o link da road e gero novos links entre a room atravessada
	private void relinkRoad(IGameComponent road, List<IGameEntity> roadRooms, RoomComponent crossedRoom) {
		RoomComponent roomI = (RoomComponent) roadRooms.get(0);
		RoomComponent roomF = (RoomComponent) roadRooms.get(1);

		Log.info("Relinking rooms... " + roomI.getId() + "<>" + crossedRoom.getId() + "<>" + roomF.getId());
		entMan.removeComponentFromEntity(road, roomI);
		entMan.removeComponentFromEntity(road, roomF);
		removeLink(roomI, roomF);
		entMan.removeEntity(road);
		entMan.removeComponent(road);
		ComponentRecipes.linkRooms(entMan,roomI, crossedRoom);
		ComponentRecipes.linkRooms(entMan,roomF, crossedRoom);

	}

	private void removeLink(RoomComponent roomI, RoomComponent roomF) {
		List<IGameComponent> roadsI = roomI.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		List<IGameComponent> roadsF = roomF.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		List<IGameComponent> roadToRemove = new ArrayList<IGameComponent>();
		for (IGameComponent roadI : roadsI) {
			for (IGameComponent roadF : roadsF) {
				if (roadI == roadF) {
					roadToRemove.add(roadI);
				}
			}
		}
		removeRoads(roadToRemove);

	}

	// ///////

	private void initDebugDraw() {
		draw = new CRDebugDraw(mapC.getLength());

	}

	protected void finishDebugDraw(String fileName) {
		if (CRJavaUtils.IS_DEBUG) {
			drawRooms();
			drawRoads();
			draw.finishDebugDraw(fileName);
		}
	}

	private void drawRoads() {
		List<IGameComponent> roads = getRoads();
		for (IGameComponent road : roads) {
			List<IGameEntity> rooms = entMan.getEntitiesWithComponent(road);
			Vector3f[] pts = new Vector3f[2];
			for (int i = 0; i < rooms.size(); i++) {
				RoomComponent room = (RoomComponent) rooms.get(i);
				pts[i] = room.getPosition();
			}
			if (rooms.size() != 2) {
				Log.error("Road has the wrong number of connected rooms:" + rooms.size() + "(devia ser 2) " + road.getId());
			} else {
				draw.setColor(Color.black);
				draw.drawLine((int) pts[0].x, (int) pts[0].z, (int) pts[1].x, (int) pts[1].z);
			}
		}
	}

	private void drawRooms() {
		List<IGameComponent> rooms = getRooms();
		draw.setColor(Color.red);
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			room.debugDraw(draw);
		}
	}

	protected float getTerrainHeightAt(int x, int z) {
		TerrainComponent terrain = (TerrainComponent) mapC.getComponentWithIdentifier(GameComps.COMP_TERRAIN);
		if (terrain != null) {
			return terrain.getValueAt(x, z);
		}
		return 0;
	}
	
	protected void wallMap(MapComponent mapC, IGameElement elSolver, RoomComponent room) {
		//map walls
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,20,60,mapC.getLength(),mapC.getLength(),0,mapC.getLength()/2);
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,20,60,mapC.getLength(),0,0,mapC.getLength()/2);
		
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,mapC.getLength(),60,20,mapC.getLength()/2,0,mapC.getLength());
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,mapC.getLength(),60,20,mapC.getLength()/2,0,0);
		
		
	}

}
