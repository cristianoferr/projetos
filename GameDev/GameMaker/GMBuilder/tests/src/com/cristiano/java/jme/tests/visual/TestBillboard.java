package com.cristiano.java.jme.tests.visual;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.ecs.systems.visual.BillboardSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;

public class TestBillboard extends MockAbstractTest {
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	@Test
	public void testBillboardSystem() {
		BillboardSystem billS=initBillboardSystem();
		
		IGameEntity ent = entMan.createEntity();
		mockUnit(ent);
		
		BillboardComponent billC=ECS.getBillboardComponent(ent);
		assertNotNull(billC);
		
		HealthComponent health=ECS.getHealthComponent(ent);
		assertNotNull(health);
		
		HealthSystem healthS = initHealthSystem();
		
		healthS.iterateEntity(ent, health, 0);
		assertNotNull("healthBillboard not generated..",health.healthBillboard);
		assertFalse("no billboard elements added",billC.listToAdd.isEmpty());
		
		billS.iterateEntity(ent, billC, 0);
		assertFalse("no billboard elements added",billC.listToAdd.isEmpty());
		assertNotNull(billC.billboard);
		
		billS.iterateEntity(ent, billC, 0);
		assertTrue("billboard elements werent consumed",billC.listToAdd.isEmpty());
	}

}
