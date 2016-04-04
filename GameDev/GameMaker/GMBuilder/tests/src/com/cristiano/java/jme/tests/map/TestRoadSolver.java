package com.cristiano.java.jme.tests.map;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.builder.map.FixedTestMapSolver;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.map.RoadSolverSystem;
import com.cristiano.java.gm.ecs.systems.map.RoomSolverSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainGeneratorSystem;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.jme3.utils.JMEUtils;
import com.jme3.math.Vector3f;

public class TestRoadSolver extends MockAbstractTest {

	@Test
	public void testRotatePoint() {
		RoadSolverSystem roadS = initRoadSolverSystem();

		Vector3f pt0 = new Vector3f(50, 0, 0);
		Vector3f pt1 = new Vector3f(150, 0, 0);

		/*
		 * p0.0 p1.1 | | ant ------------------ atual | | p0.1 p1.0
		 */

		int roadWidth = 20;
		List<Vector3f> pts0 = JMEUtils.getPerpendicularPointsXZ(pt0, pt1, roadWidth);
		assertTrue(pts0.size() == 2);

		assertTrue(pts0.get(0).equals(pt0.add(0, 0, -roadWidth)));
		assertTrue(pts0.get(1).equals(pt0.add(0, 0, roadWidth)));

		List<Vector3f> pts1 = JMEUtils.getPerpendicularPointsXZ(pt1, pt0, roadWidth);
		assertTrue(pts1.size() == 2);
		assertTrue(pts1.get(0).equals(pt1.add(0, 0, roadWidth)));
		assertTrue(pts1.get(1).equals(pt1.add(0, 0, -roadWidth)));
	}

	@Test
	public void testRoadSolver() {
		MapComponent mapC = startMapComponent();
		TerrainGeneratorSystem terrainS = initTerrainSystem();
		RoadSolverSystem roadS = initRoadSolverSystem();
		FixedTestMapSolver solver = new FixedTestMapSolver();
		mapC.mapSolver = solver;
		solver.initVars(entMan);
		// Verifica Terreno...
		TerrainComponent terrainC = mockTerrainComponent(mapC);
		mapC.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		terrainS.iterateEntity(mapC, terrainC, 1);

		TestMapSolver.validateTerrainSize(mapC, terrainC);

		assertTrue(mapC.isOnStage(MapComponent.TERRAIN_GENERATED));
		TestMapSolver.validaFlatMap(terrainC, mapC);

		TestMapSolver.validaTerrainSetter(terrainC);

		// Verifica Road
		RoomComponent roomI = new RoomComponent();
		roomI.setPosition(30, 0, 30);
		roomI.setDimension(20, 20, 20);
		mapC.attachComponent(roomI);
		RoomComponent roomF = new RoomComponent();
		roomF.setPosition(600, 0, 600);
		roomF.setDimension(20, 20, 20);
		mapC.attachComponent(roomF);
		RoadComponent road = ComponentRecipes.linkRooms(entMan, roomI, roomF);
		mapC.stageControl.setCurrentStage(MapComponent.ROAD_SOLVING);
		entMan.cleanup();
		roadS.iterateEntity(roomI, road, 1);

		// validaFlattenRoad(road,terrainC);

		// roomS.iterateEntity(mapC,roomC, 1);
	}

	@Test
	public void testCalcWallPoints() {
		MapComponent mapC = startMapComponent();
		RoadSolverSystem roadS = initRoadSolverSystem();

		/*
		 * 0: 100,25 600,25
		 * 
		 * 150,50 550,50
		 * 
		 * 1: 200,75 500,75
		 */

		// caso 0
		List<Vector3f> edges0 = new ArrayList<Vector3f>();
		edges0.add(new Vector3f(100, 0, 25));
		edges0.add(new Vector3f(600, 0, 25));

		/**/

		Vector3f ptI = new Vector3f(150, 0, 50);
		Vector3f ptF = new Vector3f(500, 0, 50);
		float distance = ptI.distance(ptF);
		int qtdPts = 5;
		List<Vector3f> pts = new ArrayList<Vector3f>();
		Vector3f pt = new Vector3f(ptI);
		Vector3f dif = ptF.subtract(ptI).divide(qtdPts);
		pts.add(pt);
		for (int i = 1; i <= qtdPts; i++) {
			pt = pt.add(dif);
			pts.add(pt);
		}

		List<Vector3f> retPts0 = roadS.transposeLine(edges0, distance, pts);
		assertTrue(pts.size() == retPts0.size());

		// caso 1:
		List<Vector3f> edges1 = new ArrayList<Vector3f>();
		edges1.add(new Vector3f(200, 0, 75));
		edges1.add(new Vector3f(500, 0, 75));
		List<Vector3f> retPts1 = roadS.transposeLine(edges1, distance, pts);
		assertTrue(pts.size() == retPts1.size());

		RoomComponent roomI = new RoomComponent();
		roomI.setPosition(ptI);
		roomI.edgePt0 = edges0.get(0);
		roomI.edgePt1 = edges1.get(0);
		RoomComponent roomF = new RoomComponent();
		roomF.setPosition(ptF);
		roomF.edgePt0 = edges0.get(1);
		roomF.edgePt1 = edges1.get(1);

		List<Vector3f> findPoints = roadS.findPoints(true, roomI, roomF);
		assertTrue(findPoints.size() == 2);
		assertTrue(findPoints.contains(roomI.edgePt0));
		assertTrue(findPoints.contains(roomF.edgePt0));

		findPoints = roadS.findPoints(false, roomI, roomF);
		assertTrue(findPoints.size() == 2);
		assertTrue(findPoints.contains(roomI.edgePt1));
		assertTrue(findPoints.contains(roomF.edgePt1));

		findPoints = roadS.findPoints(true, roomF, roomI);
		assertTrue(findPoints.size() == 2);
		assertTrue(findPoints.contains(roomI.edgePt0));
		assertTrue(findPoints.contains(roomF.edgePt0));
	}

	@Test
	public void testCalcConnectionPointsCaso1() {

		// caso1

		/*
		 * --- 0:100,25-------------------------- 0:600,25
		 * 
		 * ------ 150,50---------------------550,50
		 * 
		 * -------- 1: 200,75------------ 1:500,75
		 */
		Vector3f ptI = new Vector3f(150, 0, 50);
		Vector3f ptF = new Vector3f(550, 0, 50);

		Vector3f edge00 = new Vector3f(100, 0, 25);
		Vector3f edge01 = new Vector3f(200, 0, 75);
		Vector3f edge10 = new Vector3f(600, 0, 25);
		Vector3f edge11 = new Vector3f(500, 0, 75);

		RoomComponent roomI = new RoomComponent();
		roomI.setPosition(ptI);

		RoomComponent roomF = new RoomComponent();
		roomF.setPosition(ptF);

		List<Vector3f> edges0 = new ArrayList<Vector3f>();
		edges0.add(edge00);
		edges0.add(edge01);

		List<Vector3f> edges1 = new ArrayList<Vector3f>();
		edges1.add(edge10);
		edges1.add(edge11);

		List<Vector3f> esperado1 = new ArrayList<Vector3f>();
		esperado1.add(edge00);
		esperado1.add(edge10);
		List<Vector3f> esperado2 = new ArrayList<Vector3f>();
		esperado2.add(edge01);
		esperado2.add(edge11);

		validaVariacoes(edges0, edges1, roomI, roomF, esperado1, esperado2);
	}

	@Test
	public void testCalcConnectionPointsCaso2() {
		// caso2
		Vector3f ptI = new Vector3f(150, 0, 50);
		Vector3f ptF = new Vector3f(550, 0, 50);
		/*
		 * 0: 100,25 ----------------0:500,25
		 * 
		 * ------150,50 ----------------------550,50
		 * 
		 * 1:------- 200,75 ----------------------1:600,75
		 */
		Vector3f edge00 = new Vector3f(100, 0, 25);
		Vector3f edge01 = new Vector3f(200, 0, 75);
		Vector3f edge10 = new Vector3f(500, 0, 25);
		Vector3f edge11 = new Vector3f(600, 0, 75);
		List<Vector3f> edges0 = new ArrayList<Vector3f>();
		edges0.add(edge00);
		edges0.add(edge01);

		List<Vector3f> edges1 = new ArrayList<Vector3f>();
		edges1.add(edge10);
		edges1.add(edge11);

		List<Vector3f> esperado1 = new ArrayList<Vector3f>();
		esperado1.add(edge00);
		esperado1.add(edge10);
		List<Vector3f> esperado2 = new ArrayList<Vector3f>();
		esperado2.add(edge01);
		esperado2.add(edge11);

		RoomComponent roomI = new RoomComponent();
		roomI.setPosition(ptI);

		RoomComponent roomF = new RoomComponent();
		roomF.setPosition(ptF);

		validaVariacoes(edges0, edges1, roomI, roomF, esperado1, esperado2);
		/*
		 * findPoints = roadS.findPoints(true, roomI, roomF);
		 * assertTrue(findPoints.size() == 2);
		 * assertTrue(findPoints.contains(roomI.edgePt0));
		 * assertTrue(findPoints.contains(roomF.edgePt0));
		 */
	}

	private void validaVariacoes(List<Vector3f> edges0, List<Vector3f> edges1, RoomComponent roomI, RoomComponent roomF, List<Vector3f> esperado1,
			List<Vector3f> esperado2) {
		assertTrue(esperado1.size() == 2);
		assertTrue(esperado2.size() == 2);
		RoadSolverSystem roadS = initRoadSolverSystem();
		roomI.edgePt0 = edges0.get(0);
		roomI.edgePt1 = edges0.get(1);
		roomF.edgePt0 = edges1.get(0);
		roomF.edgePt1 = edges1.get(1);

		validaVariacao(roomI, roomF, esperado1, esperado2, roadS);
		validaVariacao(roomF, roomI, esperado1, esperado2, roadS);

		roomI.edgePt0 = edges0.get(1);
		roomI.edgePt1 = edges0.get(0);
		roomF.edgePt0 = edges1.get(1);
		roomF.edgePt1 = edges1.get(0);

		validaVariacao(roomF, roomI, esperado1, esperado2, roadS);
		validaVariacao(roomI, roomF, esperado1, esperado2, roadS);
	}

	private void validaVariacao(RoomComponent roomI, RoomComponent roomF, List<Vector3f> esperado1, List<Vector3f> esperado2, RoadSolverSystem roadS) {
		boolean e1;
		boolean e2;
		List<Vector3f> findPointsTrue = roadS.findPoints(true, roomI, roomF);
		assertTrue(findPointsTrue.size() == 2);
		e1 = findPointsTrue.contains(esperado1.get(0)) && findPointsTrue.contains(esperado1.get(1));
		e2 = findPointsTrue.contains(esperado2.get(0)) && findPointsTrue.contains(esperado2.get(1));
		assertTrue("Pontos retornados não possuem os pontos esperados", e1 || e2);

		List<Vector3f> findPointsFalse = roadS.findPoints(false, roomI, roomF);
		assertTrue(findPointsFalse.size() == 2);
		e1 = findPointsFalse.contains(esperado1.get(0)) && findPointsFalse.contains(esperado1.get(1));
		e2 = findPointsFalse.contains(esperado2.get(0)) && findPointsFalse.contains(esperado2.get(1));
		assertTrue("Pontos retornados não possuem os pontos esperados", e1 || e2);
	}

	@Test
	public void testCloseEdges() {
		//CRJavaUtils.IS_TEST = false;
		MapComponent mapC = startMapComponent();
		TerrainGeneratorSystem terrainS = initTerrainSystem();
		RoadSolverSystem roadS = initRoadSolverSystem();
		FixedTestMapSolver solver = new FixedTestMapSolver();

		/*
		 * R0 | | road0 | R1-----R2 road1
		 */
		int mult = 10;
		int offsetX = 0;
		int offsetY = 250;

		RoomComponent room0 = new RoomComponent();
		room0.setPosition(50 * mult + offsetX, 0, 0 * mult + offsetY);
		room0.setDimension(20, 20, 20);

		mapC.attachComponent(room0);
		RoomComponent room1 = new RoomComponent();
		room1.setPosition(50 * mult + offsetX, 0, 50 * mult + offsetY);
		room1.setDimension(20, 20, 20);
		mapC.attachComponent(room1);

		RoomComponent room2 = new RoomComponent();
		room2.setPosition(100 * mult + offsetX, 0, 50 * mult + offsetY);
		room2.setDimension(20, 20, 20);
		mapC.attachComponent(room2);

		RoomComponent room3 = new RoomComponent();
		room3.setPosition(175 * mult + offsetX, 0, 25 * mult + offsetY);
		room3.setDimension(20, 20, 20);
		mapC.attachComponent(room3);

		mapC.roadWidth = 100;

		RoadComponent road0 = ComponentRecipes.linkRooms(entMan, room0, room1);
		RoadComponent road1 = ComponentRecipes.linkRooms(entMan, room1, room2);
		RoadComponent road2 = ComponentRecipes.linkRooms(entMan, room0, room3);
		RoadComponent road3 = ComponentRecipes.linkRooms(entMan, room3, room2);

		List<RoomComponent> rooms = new ArrayList<RoomComponent>();
		rooms.add(room0);
		rooms.add(room1);
		rooms.add(room2);
		rooms.add(room3);
		RoomSolverSystem roomS = initRoomSolverSystem();
		roomS.initDebugDraw(2000);
		mapC.setStage(MapComponent.ROOM_SOLVING);

		for (int i = 0; i < rooms.size(); i++) {
			RoomComponent room = rooms.get(i);
			entMan.addComponent(room, mapC);
			entMan.cleanup();
			roomS.iterateEntity(mapC, room, 0);

			if (i > 0) {
				RoomComponent prevRoom = rooms.get(i - 1);
				drawRoad(roadS, rooms, roomS,  room, prevRoom);
			}
		}
		
		drawRoad(roadS, rooms, roomS,  rooms.get(0), rooms.get(rooms.size()-1));

		// assertFalse(roomS.areRoadsFromRoomSolved(room1));

		RoomComponent connectedRoom1 = roomS.getConnectedRoom(mapC, room1, road1);
		assertTrue(connectedRoom1 == room2);
		RoomComponent connectedRoom2 = roomS.getConnectedRoom(mapC, room1, road0);
		assertTrue(connectedRoom2 == room0);

		/*
		 * List<IGameComponent> loadEnts = mapC
		 * .getComponentsWithIdentifier(GameComps.COMP_LOAD_ENTITY); //
		 * assertTrue(loadEnts.isEmpty());
		 */
		roomS.draw.finishDebugDraw("testRoomEdge");

		// loadEnts = mapC.getComponentsWithIdentifier(GameComps.COMP_LOAD_ENTITY);
		// assertFalse(loadEnts.isEmpty());

	}

	private void drawRoad(RoadSolverSystem roadS, List<RoomComponent> rooms, RoomSolverSystem roomS,  RoomComponent room, RoomComponent prevRoom) {
		List<Vector3f> findPoints = roadS.findPoints(true, room, prevRoom);
		roomS.draw.drawLineXZ(findPoints.get(0), findPoints.get(1));
		findPoints = roadS.findPoints(false, room, prevRoom);
		roomS.draw.drawLineXZ(findPoints.get(0), findPoints.get(1));
	}

}
