package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.unit.PhysicsSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class TestPhysicsSystem extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testPhysicsSystem() {
		IGameEntity ent = entMan.createEntity();
		mockUnit(ent);
		
		RenderComponent renderC = ECS.getRenderComponent(ent);
		renderC.firstTick=false;
		renderC.node=new Node();
		Node node=new Node();
		node.attachChild(renderC.node);
		PhysicsComponent physC = ECS.getPhysicsComponent(ent);
		assertNotNull(physC);
		PhysicsSystem physS = initPhysicsSystem();
		physC.firstTick=true;
		physC.mass=100;
		assertTrue(physC.isFirstTick());
		//first tick...
		SpatialComponent spatialC=(SpatialComponent) entMan.addComponent(GameComps.COMP_SPATIAL,ent);
		spatialC.spatial(snippets.generateBox(ColorRGBA.Red, Vector3f.UNIT_XYZ, Vector3f.UNIT_XYZ));
		renderC.node.attachChild(spatialC.spatial());
		
		physS.iterateEntity(ent, physC, 0);
		assertTrue(physC.isFirstTick());
		assertNotNull("physNode is null!",physC.physNode);
		//anexaControleFisico
		physS.iterateEntity(ent, physC, 0);
		//assertFalse(physC.isFirstTick());
		
		//final tick
		physS.iterateEntity(ent, physC, 0);
		assertFalse(physC.isFirstTick());
	}

}