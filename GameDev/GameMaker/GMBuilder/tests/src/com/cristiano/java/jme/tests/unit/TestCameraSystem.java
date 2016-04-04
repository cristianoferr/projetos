package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.visual.CameraSystem;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public class TestCameraSystem extends MockAbstractTest {


	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}


	@Test
	public void testCameraSystem() {
		CameraSystem camS = initCameraSystem();
		GameEntity ent=new GameEntity();
		
		RenderComponent renderC = (RenderComponent) entMan.addComponent(GameComps.COMP_RENDER, ent);
		assertNotNull(renderC);
		renderC.node=new Node();
		
		DimensionComponent dimensionC = (DimensionComponent) entMan.addComponent(GameComps.COMP_DIMENSION, ent);
		assertNotNull(dimensionC);
		dimensionC.dimension.set(Vector3f.UNIT_XYZ);
		
		CamComponent camC = validateCameraType(camS, ent, CameraSystem.CHASE);
		assertNotNull(camC.chaseCam);
		camC =validateCameraType(camS, ent, CameraSystem.FIRST_PERSON);
		assertNotNull(camC.camNode);
		camC =validateCameraType(camS, ent, CameraSystem.FOLLOW);
		assertNotNull(camC.camNode);
		
	}

	private CamComponent validateCameraType(CameraSystem camS, GameEntity ent, String tipo) {
		
		CamComponent camC = startCamComponent(ent);
		assertNotNull(camC.camType);
		assertFalse(camC.isFirstTick());
		camC.cam=new Camera(500,500);
		
		camC.firstTick=true;
		camC.camType=tipo;
		assertNotNull(camC.getElement());
		camS.iterateEntity(ent, camC, 0);
		assertFalse(camC.isFirstTick());
		
		ent.removeComponent(camC);
		return camC;
	}

	
}