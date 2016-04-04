package com.cristiano.java.jme.tests.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.builder.map.BSPTreeMapSolver;
import com.cristiano.java.gm.builder.map.LinearMapSolver;
import com.cristiano.java.gm.builder.map.MultiLinearMapSolver;
import com.cristiano.java.gm.builder.map.RaceMapSolver;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.systems.map.MapWorldSystem;
import com.cristiano.java.gm.ecs.systems.map.RoomSolverSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIActionListenerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class TestMapSolver extends MockAbstractTest {


	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testMapComponentRoomFunctions() {
		MapComponent mapC = startMapComponent();
		mapC.removeComponent(GameComps.COMP_ROOM);
		
		RoomComponent room0=addRoom(mapC,true,0);
		RoomComponent room1=addRoom(mapC,false,1);
		RoomComponent room2=addRoom(mapC,false,2);
		RoomComponent room3=addRoom(mapC,false,3);
		room3.endingRoom=true;
		
		assertTrue(mapC.getStartingRoom()==room0);
		assertTrue(mapC.getNextRoom(room0)==room1);
		assertTrue(mapC.getNextRoom(room1)==room2);
		assertTrue(mapC.getNextRoom(room2)==room3);
		assertTrue(mapC.getNextRoom(room3)==null);
		assertTrue(mapC.getEndingRoom()==room3);
	}
	

	private RoomComponent addRoom(MapComponent mapC, boolean b, int i) {
		RoomComponent room0=mockRoom(mapC);
		room0.startingRoom=b;
		room0.roomId=i;
		return room0;
	}


	@Test
	public void testMapWorldStages() {
		MapComponent mapC = startMapComponent();
		
		MapWorldComponent mapWC = mockMapWorld();
		
		MapWorldSystem mapWS = initMapWorldSystem();
		validateSuccessMap(mapC, mapWC, mapWS);
		validateFailMap(mapC, mapWC, mapWS);
	}



	private void validateSuccessMap(MapComponent mapC, MapWorldComponent mapWC, MapWorldSystem mapWS) {
		mapWC.setStage(MapWorldComponent.RUNNING);
		MapWorldSystem.currentMap=mapC;
		
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.RUNNING));
		
		mapC.isCompleted=true;
		mapC.playerVictorious=true;
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.MAP_SUCCESS));
		
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.WAIT_MAP_SUCCESS));
		
		UIActionListenerSystem listenS = initUIActionListenerSystem();
		UIActionComponent action = listenS.sendAction(entity, GameActions.ACTION_NEXT_MAP);
		listenS.iterateEntity(entity, action, 0);
		
		assertTrue(mapWC.isOnStage(MapWorldComponent.NEXT_MAP));
	}

	

	private void validateFailMap(MapComponent mapC, MapWorldComponent mapWC, MapWorldSystem mapWS) {
		mapWC.setStage(MapWorldComponent.RUNNING);
		mapC.isCompleted=false;
		mapC.playerVictorious=false;
		MapWorldSystem.currentMap=mapC;
		
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.RUNNING));
		
		mapC.isCompleted=true;
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.MAP_FAIL));
		
		mapWS.iterateEntity(entity, mapWC, 0);
		assertTrue(mapWC.isOnStage(MapWorldComponent.WAIT_MAP_FAIL));
		
		UIActionListenerSystem listenS = initUIActionListenerSystem();
		UIActionComponent action = listenS.sendAction(entity, GameActions.ACTION_RELOAD_GAME);
		listenS.iterateEntity(entity, action, 0);
		
		assertTrue(mapWC.isOnStage(MapWorldComponent.RESET_MAP));
	}
	
	
	@Test
	public void testRaceMap() {
		MapComponent mapC = startMapComponent();
		mockRaceMap(mapC);
		
		mapC.maxRoomSize=0.01f;
		mapC.minRoomSize=0.01f;
		mapC.removeComponent(GameComps.COMP_ROOM);
		RaceMapSolver solver = new RaceMapSolver();
		solver.initVars(entMan);
		
		MapLocationComponent mapLocal = startMapLocationComponent();
		solver.startFrom(mapC, mapLocal, (GenericElement) em.pickFinal(TestStrings.MAP_POPULATE_LINEAR),mockGameGenre());
		do {
			entMan.cleanup();
		} while (!solver.hasCompleted());
		entMan.cleanup();
		List<IGameComponent> rooms = mapC.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		assertTrue("Rooms <> esperado (" + mapC.maxRooms + "):" + rooms.size(), rooms.size() > mapC.maxRooms/2);
		checkRoomsRoads(rooms);
		
		checkRoadWall(mapC);
	}

	private void mockRaceMap(MapComponent mapC) {
		IGameElement el = em.pickFinal(TestStrings.TAG_MAP_RACE);
		if (el!=null){
		} else {
			IGameElement elRoadEnviro=new GenericElement(em);
			elRoadEnviro.setProperty(GameProperties.MESH_TAG,"{box mesh leaf}");
			elRoadEnviro.setProperty(GameProperties.WALL_HEIGHT,10);
			
			el=new GenericElement(em);
			el.setProperty(GameProperties.ROOM_MAX,5);
			el.setProperty("maxRoomSize",100);
			el.setProperty("minRoomSize",100);
			el.setProperty("length",1024);
			el.setProperty("isRoomSizeRelative",0);
			el.setProperty(GameProperties.ROAD_WALL_ENVIRO,"["+elRoadEnviro.id()+"]");
			
			
		}
		mapC.loadFromElement(el);
		if (mapC.lapDistance==0){
			mapC.lapDistance=2000;
		}
		
	}

	private void checkRoadWall(MapComponent mapC) {
		assertNotNull("mapC.roadWallMesh is null",mapC.getRoadEnviro());
		assertFalse("mapC.roadWallMesh is empty",mapC.getRoadEnviro().getMeshTag().equals(""));
		assertTrue("mapC.roadWallMesh is empty",mapC.getRoadEnviro().getWallHeight()>0);
		
	}

	@Test
	public void testLinearMap() {
		MapComponent mapC = startMapComponent();
		// /mapC.populateMap="com.cristiano.java.jme.builder.map.LinearMapSolver";
		mapC.removeComponent(GameComps.COMP_ROOM);
		LinearMapSolver solver = new LinearMapSolver();
		solver.initVars(entMan);
		MapLocationComponent mapLocal = startMapLocationComponent();
		
		
		
		solver.startFrom(mapC, mapLocal, mockMapPopulateElement(TestStrings.MAP_POPULATE_LINEAR),mockGameGenre());
		do {
			entMan.cleanup();
		} while (!solver.hasCompleted());
		entMan.cleanup();
		List<IGameComponent> rooms = mapC.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		assertTrue("Rooms <> esperado (" + mapC.maxRooms + "):" + rooms.size(), rooms.size() == mapC.maxRooms);
		checkRoomsRoads(rooms);
	}

	private void checkRoomsRoads(List<IGameComponent> rooms) {
		for (IGameComponent room : rooms) {
			List<IGameComponent> roads = room.getComponentsWithIdentifier(GameComps.COMP_ROAD);
			assertTrue("Pelo menos 1 road por room:" + roads.size(), roads.size() > 0);
			for (IGameComponent road : roads) {
				List<IGameEntity> roomsFromRoad = entMan.getEntitiesWithComponent(road);
				assertTrue("Devia ser 2 rooms por roads, mas tem:" + roomsFromRoad.size(), roomsFromRoad.size() == 2);
			}
		}
	}

	@Test
	public void testRoomSolver() {
		MapComponent mapC = startMapComponent();
		mapC.removeComponent(GameComps.COMP_ROOM);
		TerrainGeneratorSystem terrainS = initTerrainSystem();
		RoomSolverSystem roomS = initRoomSolverSystem();
		MapLocationComponent mapLocal = startMapLocationComponent();

		// Verifica Terreno...
		TerrainComponent terrainC = mockTerrainComponent(mapC);
		mapC.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		terrainS.iterateEntity(mapC, terrainC, 1);

		validateTerrainSize(mapC, terrainC);

		assertTrue(mapC.isOnStage(MapComponent.TERRAIN_GENERATED));
		validaFlatMap(terrainC, mapC);

		validaTerrainSetter(terrainC);

		// Verifica Room
		RoomComponent roomC = (RoomComponent) entMan.spawnComponent(GameComps.COMP_ROOM);
		roomC.setPosition(30, 0, 30);
		roomC.setDimension( 20, 20, 20);
		mapC.attachComponent(roomC);
		mapC.stageControl.setCurrentStage(MapComponent.ROOM_SOLVING);
		roomC.bubbleFilter = mapLocal.roomBubbleFilter;
		roomS.iterateEntity(mapC, roomC, 1);

		// validaFlattenRoom(roomC, terrainC);

		// roomS.iterateEntity(mapC,roomC, 1);
	}



	static void validateTerrainSize(MapComponent mapC, TerrainComponent terrainC) {
		assertNotNull(terrainC.getHeightMap());
		// int width=mapC.getLength();
		// int depth=mapC.getLength();
		int tamanhoMapa = (int) Math.pow(2, terrainC.length);
		tamanhoMapa *= tamanhoMapa;
		assertTrue("Map size should be " + tamanhoMapa + " but is " + terrainC.getHeightMap().length, terrainC.getHeightMap().length == tamanhoMapa);
	}

	public static void validaTerrainSetter(TerrainComponent terrainC) {
		float valueAt = terrainC.getValueAt(10, 10);
		assertTrue(valueAt == TERRAIN_HEIGHT);
		float newVal = TERRAIN_HEIGHT / 2;
		// scaled
		terrainC.setScaledValueAt(10, 10, newVal);
		valueAt = terrainC.getScaledValueAt(10, 10);
		assertTrue(valueAt == newVal);

		// direct
		terrainC.setValueAt(10, 10, newVal);
		valueAt = terrainC.getValueAt(10, 10);
		assertTrue(valueAt == newVal);

		terrainC.setScaledValueAt(10, 10, TERRAIN_HEIGHT);
	}

	static void validaFlatMap(TerrainComponent terrainC, MapComponent mapC) {
		for (int i = 0; i < terrainC.getHeightMap().length; i++) {
			assertTrue("Altura do terreno não é " + TERRAIN_HEIGHT + ": " + terrainC.getHeightMap()[i], terrainC.getHeightMap()[i] == TERRAIN_HEIGHT);
		}

	}

	@Test
	public void testAbstractMapFunctions() {
		MapComponent mapC = startMapComponent();
		MapLocationComponent mapLocal = startMapLocationComponent();
		BSPTreeMapSolver solver = new BSPTreeMapSolver();
		GameGenreComponent genre=mockGameGenre();
		solver.initVars(entMan);
		solver.startFrom(mapC, mapLocal, mockMapPopulateElement(TestStrings.MAP_POPULATE_LINEAR),genre);

		RoomComponent roomI = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		roomI.setPosition(10, 0, 10 );
		roomI.setDimension(10, 10, 10);
		RoomComponent roomF = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		roomF.setPosition(50, 0, 10);
		roomF.setDimension( 10, 10, 10);
		RoomComponent roomCross = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		roomCross.setPosition(25, 0, 10);
		roomCross.setDimension( 10, 10, 10);
		RoomComponent roomOk1 = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		roomOk1.setPosition(75, 0, 10);
		roomOk1.setDimension(10, 10, 10);
		RoomComponent roomOk2 = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		roomOk2.setPosition(25, 0, 50);
		roomOk2.setDimension( 10, 10, 10);

		Vector3f point = new Vector3f(10, 0, 10);
		assertTrue("Ponto devia estar dentro:" + point, roomI.isPointInside(point));
		point = new Vector3f(15, 0, 10);
		assertTrue("Ponto devia estar dentro:" + point, roomI.isPointInside(point));
		point = new Vector3f(15, 0, 15);
		assertTrue("Ponto devia estar dentro:" + point, roomI.isPointInside(point));
		point = new Vector3f(5, 0, 5);
		assertTrue("Ponto devia estar dentro:" + point, roomI.isPointInside(point));

		// Está dentro devido ao spacing...
		point = new Vector3f(0, 0, 5);
		boolean pointInside = roomI.isPointInside(point);
		assertTrue("Ponto devia estar dentro:" + point, pointInside);

		// Está dentro devido ao spacing...
		point = new Vector3f(5, 0, 0);
		assertTrue("Ponto devia estar dentro:" + point, roomI.isPointInside(point));

		point = new Vector3f(35, 0, 20);
		assertFalse("Ponto devia estar fora:" + point, roomI.isPointInside(point));

		List<IGameComponent> rooms = solver.getRooms();
		assertTrue("Rooms.size<=2:" + rooms.size(), rooms.size() > 2);
		RoadComponent road = ComponentRecipes.linkRooms(entMan,roomI, roomF);
		assertNotNull(road);
		entMan.cleanup();
		List<IGameEntity> roadRooms = entMan.getEntitiesWithComponent(road);
		assertTrue("RoadRooms devia ser 2 mas é " + roadRooms.size(), roadRooms.size() == 2);

		RoomComponent crossedRoom = solver.checkRoadAgainstRooms(road, rooms, roadRooms);
		assertNotNull("CrossedRoom nullo (devia retornar roomCross)", crossedRoom);
		//assertTrue("crossedRoom<>roomCross", crossedRoom == roomCross);

	}

	

	// TODO: erro no BSP
/*
	@Test
	public void testBSPMap() {
		MapComponent mapC = startMapComponent();
		startReuseComponent();
		// mapC.populateMap="com.cristiano.java.jme.builder.map.LinearMapSolver";
		mapC.minRoomSize = 300;
		mapC.maxRoomSize = 500;
		mapC.removeComponent(GameComps.COMP_ROOM);
		BSPTreeMapSolver solver = new BSPTreeMapSolver();
		solver.initVars(entMan);
		MapLocationComponent mapLocal = startMapLocationComponent();
		solver.startFrom(mapC,mapLocal, (GenericElement) em.pickFinal(TestStrings.MAP_POPULATE_BSP));
		do {
			entMan.cleanUp();
		} while (!solver.hasCompleted());
		entMan.cleanUp();
		solver.finishGeneration();
		List<IGameComponent> rooms = mapC.getComponentsWithIdentifier(GameComps.COMP_ROOM); //
		//assertTrue("Rooms <> esperado:" + rooms.size(), rooms.size() == mapC.maxRooms);
		assertTrue("No Rooms:" + rooms.size(), rooms.size() >0);
		checkRoomsRoads(rooms);
	}*/

	

	@Test
	public void testMultiLinearMap() {
		MapComponent mapC = startMapComponent();
		mapC.removeComponent(GameComps.COMP_ROOM);
		MapLocationComponent mapLocal = startMapLocationComponent();
		// /mapC.populateMap="com.cristiano.java.jme.builder.map.LinearMapSolver";

		MultiLinearMapSolver solver = new MultiLinearMapSolver();
		solver.initVars(entMan);
		solver.startFrom(mapC, mapLocal, (GenericElement) em.pickFinal("mapPopulate bsp"),mockGameGenre());
		do {
			entMan.cleanup();
		} while (!solver.hasCompleted());
		entMan.cleanup();
		List<IGameComponent> rooms = mapC.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		assertTrue("Rooms <> esperado (" + mapC.maxRooms + "):" + rooms.size(), rooms.size() == mapC.maxRooms);
		checkRoomsRoads(rooms);
	}

}
