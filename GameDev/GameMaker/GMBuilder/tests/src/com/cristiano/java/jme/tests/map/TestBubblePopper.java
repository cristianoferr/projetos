package com.cristiano.java.jme.tests.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.map.BubbleComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.systems.macro.BubblePopperSystem;
import com.cristiano.java.gm.ecs.systems.map.RoomSolverSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class TestBubblePopper extends MockAbstractTest {

	IGameEntity entity = new GameEntity();

	
	
	

	@Test
	public void testBubbleData() {
		entity.removeAllComponents();
		BubbleDataComponent data = validaBubbleData();
		/*
		 * for (BubbleComponent enviro:data.enviros){
		 * 
		 * }
		 */

	}

	@Test
	public void testFitsInto() {
		BubbleComponent bubble = new BubbleComponent();
		bubble.setFittings(100, 10, 100, 10, 100, 10);

		verificaFitInto(bubble, new Vector3f(50, 50, 50));
		verificaFitInto(bubble, new Vector3f(50, 20, 90));
		bubble.setFittings(-1, -1, 100, 10, 100, 10);

		verificaFitInto(bubble, new Vector3f(1, 20, 90));
		verificaFitInto(bubble, new Vector3f(150, 20, 90));
		bubble.setFittings(100, -1, 100, 10, 100, 10);
		verificaNoFitInto(bubble, new Vector3f(150, 20, 90));
		bubble.setFittings(100, 10, 100, 10, 100, 10);
		verificaNoFitInto(bubble, new Vector3f(5, 20, 90));
	}

	private void verificaNoFitInto(BubbleComponent bubble, Vector3f dim) {
		assertFalse("bubble fito into:" + dim, bubble.fitsInto(dim));
	}

	private void verificaFitInto(BubbleComponent bubble, Vector3f dim) {
		assertTrue("bubble doesnt fito into:" + dim, bubble.fitsInto(dim));
	}

	@Test
	public void testBubblePopperSystemFunctions() {
		BubbleDataComponent data = validaBubbleData();
		BubblePopperSystem bubbleS = initBubblePopperSystem();
		MapComponent mapC = startMapComponent();
		int width = 500;
		int depth = 500;
		int x = width / 2;
		int z = depth / 2;
		RoomComponent room = startRoomComponent(width, depth, x, z, mapC);
		BubbleComponent mainBubble = validaRoomSolver(mapC, room);

		Vector3f dimension = bubbleS.calcValidDimension(mainBubble, 0);
		assertTrue("x<=0:" + dimension, dimension.x > 0);
		assertTrue("y<0:" + dimension, dimension.y >= 0);
		assertTrue("z<=0:" + dimension, dimension.z > 0);
		assertTrue("x>width:" + dimension, dimension.x < width);
		assertTrue("y>bubble.y:" + dimension, dimension.y <= mainBubble.dimensions.y);
		assertTrue("z>depth:" + dimension, dimension.z < depth);
		// float areaEmUso = EnviroUtils.getAreaEmUso(mainBubble);
		assertTrue("area error:" + mainBubble.getArea(), mainBubble.getArea() == (mapC.enviroSpacing + width)
				* (mapC.enviroSpacing + depth));

		// Verifica point inside...
		assertTrue("1)point ouside when should be inside:", mainBubble.isPointInside(new Vector3f(250, 0, 250)));
		assertTrue("2)point ouside when should be inside:", mainBubble.isPointInside(new Vector3f(500, 0, 250)));
		assertTrue("3)point ouside when should be inside:", mainBubble.isPointInside(new Vector3f(500, 1, 250)));
		assertFalse("4)point inside when should be outside:", mainBubble.isPointInside(new Vector3f(501, 0, 250)));

		validaEixo(mainBubble, new Vector3f(250, 0, 250), new Vector3f(100, 0, 100), true);
		validaEixo(mainBubble, new Vector3f(550, 0, 250), new Vector3f(150, 0, 100), true);
		validaEixo(mainBubble, new Vector3f(550, 0, 250), new Vector3f(100, 0, 100), true);
		validaEixo(mainBubble, new Vector3f(550, 0, 250), new Vector3f(99, 0, 100), false);

		validaEixo(mainBubble, new Vector3f(550, 0, 1250), new Vector3f(100, 0, 100), false);
		validaEixo(mainBubble, new Vector3f(1550, 0, 250), new Vector3f(100, 0, 100), false);
		validaEixo(mainBubble, new Vector3f(1000, 0, 1000), new Vector3f(10000, 0, 10000), true);
		validaEixo(mainBubble, new Vector3f(2000, 0, 2000), new Vector3f(10000, 0, 10000), true);
		validaEixo(mainBubble, new Vector3f(1550, 0, 1250), new Vector3f(10000, 0, 10000), true);

		validaQuadrant(mainBubble, new Vector3f(250, 0, 250), new Vector3f(100, 0, 100), 0);
		validaQuadrant(mainBubble, new Vector3f(550, 0, 250), new Vector3f(100, 0, 100), 0);
		validaQuadrant(mainBubble, new Vector3f(650, 0, 250), new Vector3f(100, 0, 100), -1);

		mainBubble.dimensions.x = 100;
		mainBubble.dimensions.z = 100;
		mainBubble.spacing = 1;
		// pos=250,250 dim=100,100

		// sugestions
		// quad 1
		validaSuggestion(mainBubble, new Vector3f(175, 0, 325), 175, 351);

		// quad 3
		validaSuggestion(mainBubble, new Vector3f(175, 0, 175), 175, 149);

		// quad 2
		validaSuggestion(mainBubble, new Vector3f(325, 0, 175), 325, 149);

		// quad 0
		validaSuggestion(mainBubble, new Vector3f(325, 0, 325), 325, 351);
	}

	private void validaSuggestion(BubbleComponent mainBubble, Vector3f pt, int x, int z) {
		Vector3f dim = new Vector3f(100, 0, 100);
		mainBubble.suggestPosition(pt, dim);
		assertTrue("x difere:" + pt.x, pt.x == x);
		assertTrue("z difere:" + pt.z, pt.z == z);
	}

	private void validaQuadrant(BubbleComponent mainBubble, Vector3f point, Vector3f dimensions, int exp) {
		int b = mainBubble.getIntersectionQuadrant(point, dimensions);
		assertTrue("point:" + point + " dim:" + dimensions + " esperado:" + exp + " result:" + b, b == exp);
	}

	private void validaEixo(BubbleComponent mainBubble, Vector3f point, Vector3f dimensions, boolean exp) {
		boolean b = mainBubble.isPositionInside(point, dimensions);
		assertTrue("point:" + point + " dim:" + dimensions + " esperado:" + exp + " result:" + b, b == exp);
	}

	@Test
	public void testRoomSolver() {
		//entity.removeAllComponents();
		BubbleDataComponent data = validaBubbleData();
		int width = 500;
		int depth = 500;
		int x = width / 2;
		int z = depth / 2;

		MapComponent mapC = startMapComponent();

		RoomComponent room = startRoomComponent(width, depth, x, z, mapC);

		// Validando roomSolver...
		BubbleComponent mainBubble = validaRoomSolver(mapC, room);

		// Validando bubbleSolver...
		BubblePopperSystem bubbleS = initBubblePopperSystem();
		bubbleS.iterateEntity(room, mainBubble, 0);

		validaEnviroBubble(mainBubble);

		List<IGameComponent> bubbles = mainBubble.getComponentsWithIdentifier(GameComps.COMP_BUBBLE);
		assertTrue("novas bubbles não foram criadas na room..." + bubbles.size(), bubbles.size() > 0);
	}

	private RoomComponent startRoomComponent(int width, int depth, int x, int z, MapComponent mapC) {
		RoomComponent room = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		room.setPosition(x, 0, z);
		room.setDimension(width, 0, depth);
		room.setHeight(mapC.roomHeight);
		room.bubbleFilter = TestStrings.ROOM_ENVIRO_TEST_TAG;
		return room;
	}

	private void validaEnviroBubble(BubbleComponent mainBubble) {
		// floor
		IGameElement floorElement = mainBubble.getFloorElement();
		assertNotNull("floor element empty", floorElement);
		String pointList = floorElement.getProperty(GameProperties.POINT_LIST);
		assertFalse("pointList empty", pointList.equals(""));

		// separator
		assertNotNull("wallElement empty", mainBubble.getWallMeshTag());
		
		// mesh
		assertNotNull("meshTag is null",mainBubble.getEntityMeshTag());
		
		List<IGameComponent> loadComps = mainBubble.getComponentsWithIdentifier(GameComps.COMP_CHILD);
		assertTrue("no loadcomps were added to the bubble...", loadComps.size() > 0);

	}

	private BubbleComponent validaRoomSolver(MapComponent mapC, RoomComponent room) {
		mapC.stageControl.setCurrentStage(MapComponent.ROOM_SOLVING);
		RoomSolverSystem roomS = initRoomSolverSystem();
		assertNotNull("RoomSolverSystem null", roomS);
		roomS.iterateEntity(mapC, room, 1);

		List<IGameComponent> bubbles = room.getComponentsWithIdentifier(GameComps.COMP_BUBBLE);
		assertTrue("bubbles não foram criadas na room..." + bubbles.size(), bubbles.size() >0);
		
		IGameComponent bubble = bubbles.get(0);
		entMan.addEntity(bubble);
		//Log.debug("Bubble id:"+bubble.getId());
		return (BubbleComponent) bubble;
	}

	
	private BubbleDataComponent validaBubbleData() {
		BubbleDataComponent data = mockBubbleData();
		assertNotNull("bubble data null", data);

		assertTrue("enviros vazios", data.enviros.size() > 0);
		return data;
	}

}
