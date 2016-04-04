package com.cristiano.java.jme.tests.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.unit.JoystickComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.systems.map.MapWorldSystem;
import com.cristiano.java.gm.ecs.systems.unit.JoystickSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.jme.tests.mocks.MockActionReceiver;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.jme3.ui.gameController.MockPad;





public class TestJoystickSystem extends MockAbstractTest {
	
	private ActionController actionController;
	
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testJoystickSystem() {
		mockGame();
		setRuningGame();
		
		JoystickSystem joyS = initJoystickSystem();
		IGameEntity playerEnt=entMan.createEntity();
		mockPlayer(playerEnt);
		assertTrue(playerEnt.containsComponent(GameComps.COMP_PLAYER));
		
		JoystickComponent joyC= (JoystickComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_JOYSTICKS);
		assertNotNull(joyC);
		assertFalse(joyC.elControllers.isEmpty());
		
		PlayerComponent playerC= (PlayerComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_PLAYER);
		assertNotNull(playerC);
		
		actionController = new ActionController();
		
		playerC.setActionController(actionController);
		
		joyS.iterateEntity(playerEnt, joyC, 0);
		
		assertFalse("No Controllers added...",joyC.controllers.isEmpty());
		
		MockPad mockPad=(MockPad) joyC.controllers.get(0);
		mockPad.mockAction(MockActionReceiver.action1);
		
		MockActionReceiver actionReceiver = mockActionReceiver();
		
		assertTrue("increment on c1 before update:"+actionReceiver.c1,actionReceiver.c1==0);
		joyS.iterateEntity(playerEnt, joyC, 1);
		actionController.update(1);
		
		assertTrue(mockPad.showStatus);
		
		assertTrue("No increment on c1:"+actionReceiver.c1,actionReceiver.c1==1);
		assertTrue("Increment on c2:"+actionReceiver.c2,actionReceiver.c2==0);
		
	}

	protected void setRuningGame() {
		MapWorldSystem.currentMap.setStage(MapComponent.RUNNING);
	}

	private MockActionReceiver mockActionReceiver() {
		MockActionReceiver actionReceiver = new MockActionReceiver();
		actionController.enableAction(MockActionReceiver.action1, actionReceiver);
		return actionReceiver;
	}

	
	@Test
	public void testActionController() {
		actionController = new ActionController();
		assertFalse(actionController.isActionEnabled(MockActionReceiver.action1));
		MockActionReceiver actionReceiver = mockActionReceiver();
		assertTrue(actionController.isActionEnabled(MockActionReceiver.action1));
		assertFalse(actionController.isActionEnabled(MockActionReceiver.action2));
		
		actionController.sendAction(MockActionReceiver.action1);
		actionController.sendAction(MockActionReceiver.action2);
		assertTrue("increment on c1 before update:"+actionReceiver.c1,actionReceiver.c1==0);
		
		actionController.update(1);
		
		assertTrue("No increment on c1:"+actionReceiver.c1,actionReceiver.c1==1);
		assertTrue("increment on c2 (not enabled)"+actionReceiver.c2,actionReceiver.c2==0);
	}


}
