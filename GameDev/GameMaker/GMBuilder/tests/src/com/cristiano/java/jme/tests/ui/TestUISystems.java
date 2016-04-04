package com.cristiano.java.jme.tests.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.UIControlComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.ecs.systems.ui.UIControlSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIPanelSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

public class TestUISystems extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	@Test
	public void testControlSystem() {
		UIControlSystem controlS = initUIControlSystem();
		UIControlComponent controlC = (UIControlComponent) entMan.spawnComponent(GameComps.COMP_UI_CONTROL);

		UIPanelComponent panel = mockPanelComponent();
		
		controlS.iterateEntity(panel, controlC, 0);
		
	}

	@Test
	public void testPanelComponent() {
		UIPanelSystem panelS = initUIPanelSystem();
		IGameEntity ent = entMan.createEntity();
		UIPanelComponent panelCHealth = mockPanelComponent();
		UIPanelComponent panelCEmpty = mockPanelComponent();
		panelCEmpty.visibleConditions = new String[0];
		UIPanelComponent panelCInexiste = mockPanelComponent();
		panelCInexiste.visibleConditions = new String[] { GameComps.COMP_ACTUATOR_WEAPON };

		IGameEntity playerEnt = mockPlayer();
		entMan.cleanup();

		assertTrue(panelCHealth.isVisible(playerEnt));
		assertTrue(panelCEmpty.isVisible(playerEnt));
		assertFalse(panelCInexiste.isVisible(playerEnt));

		// panelCHealth
		panelS.checkVisibility(playerEnt, panelCHealth);
		assertTrue(panelCHealth.isVisible);
		playerEnt.removeComponent(GameComps.COMP_RESOURCE_HEALTH);
		panelS.checkVisibility(playerEnt, panelCHealth);
		assertFalse(panelCHealth.isVisible);

		// panelCEmpty
		panelS.checkVisibility(playerEnt, panelCEmpty);
		assertTrue(panelCEmpty.isVisible);
		panelCEmpty.visibleConditions = new String[] { GameComps.COMP_ACTUATOR_WEAPON };
		panelS.checkVisibility(playerEnt, panelCEmpty);
		assertFalse(panelCEmpty.isVisible);

		// panelCInexiste
		panelS.checkVisibility(playerEnt, panelCInexiste);
		assertFalse(panelCInexiste.isVisible);
		mockWeapon(playerEnt);
	}

	

	private UIPanelComponent mockPanelComponent() {
		UIPanelComponent panel = (UIPanelComponent) entMan.spawnComponent(GameComps.COMP_UI_PANEL);
		IGameElement el = em.createElement();
		el.setProperty("visibleConditions", "{" + GameComps.COMP_RESOURCE_HEALTH + " comp2}");
		panel.loadFromElement(el);
		assertTrue(panel.visibleConditions.length == 2);
		assertTrue(panel.visibleConditions[0].equals(GameComps.COMP_RESOURCE_HEALTH));
		assertTrue(panel.visibleConditions[1].equals("comp2"));
		// panel.niftyElement=new Element(null, null, null, null, null, false,
		// null, null);
		return panel;
	}

}
