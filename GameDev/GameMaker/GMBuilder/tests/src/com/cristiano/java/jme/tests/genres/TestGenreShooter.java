package com.cristiano.java.jme.tests.genres;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.builder.map.ShooterMapSolver;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.directors.DirectorComponent;
import com.cristiano.java.gm.ecs.comps.macro.directors.ShooterDirectorComponent;
import com.cristiano.java.gm.ecs.comps.map.LandmarksComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.unit.JoystickComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.systems.macro.directors.DirectorSystem;
import com.cristiano.java.gm.ecs.systems.map.MapLoaderSystem;
import com.cristiano.java.gm.ecs.systems.unit.JoystickSystem;
import com.cristiano.java.gm.ecs.systems.visual.CameraSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IReceiveAction;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.jme3.rigidBody.CharActions;
import com.cristiano.jme3.ui.gameController.GyroAxisJoystick;
import com.cristiano.jme3.ui.gameController.IGameJoystick;
import com.cristiano.jme3.utils.AndroidUtils;
import com.cristiano.utils.CRJavaUtils;
import com.jme3.math.Vector3f;

public class TestGenreShooter extends MockAbstractTest {
	
	private MapLoaderSystem mapS;
	private MapLocationComponent mapLocal;
	private LandmarksComponent landMark;
	private GameGenreComponent genre;
	private MapComponent map;
	private ActionController actionController;
	private JoystickComponent joystick;
	private IGameElement controllers;
	private PlayerComponent player;
	private IGameEntity ent;
	private JoystickSystem joyS;


	@BeforeClass
	public static void setUpTest() throws IOException {
		startHeadless();
		em.loadBlueprintsFromFile(Extras.BLUEPRINTS_LANDMARKS_PATH);
	}

	@Test
	public void testShooterMap() {
		setupTest();
		
		ShooterMapSolver shooterSolver=(ShooterMapSolver) map.mapSolver;
		assertTrue(shooterSolver.roadSize>0);
		assertNotNull(shooterSolver.axis);
		assertTrue(shooterSolver.room.getDimensions().x>0);
		assertTrue(shooterSolver.room.getDimensions().y==map.roadWidth);
		assertTrue(shooterSolver.room.getDimensions().z==shooterSolver.minLength);
		assertTrue(shooterSolver.legDistance>0);
		assertTrue(shooterSolver.minLength>0);
		
		assertNotNull(landMark.random);
		
		//Landmark Component
		assertTrue(landMark.getMaxObstacles()>0);
		IGameElement obstacle = landMark.getRandomObstacle();
		assertNotNull(obstacle);
		Vector3f dimension=shooterSolver.getValidDimension(true,obstacle);
		//Vector3f dimension=landmark.getObstacleRandomDimension();
		assertNotNull(dimension);
		assertTrue(dimension.length()>0);
		//build
		
		//valida Position
		
		for (int i=0;i<100;i++){
			Vector3f pos=shooterSolver.getRandomPos(true,dimension,obstacle);
			assertNotNull(pos);
			assertTrue(pos.x<=shooterSolver.room.getDimensions().x);
			assertTrue(pos.y<=shooterSolver.room.getDimensions().y);
			assertTrue(pos.z<=shooterSolver.room.getDimensions().z);
			assertTrue(pos.x>=dimension.x/2);
			assertTrue(pos.y>=dimension.y/2);
			assertTrue(pos.z>=dimension.z/2);
			assertTrue(pos.x<=shooterSolver.room.getDimensions().x-dimension.x/2);
			assertTrue(pos.y<=shooterSolver.room.getDimensions().y-dimension.y/2);
			assertTrue(pos.z<=shooterSolver.room.getDimensions().z-dimension.z/2);
		}
		
	}

	private void setupTest() {
		mapS = initMapLoaderSystem();
		mapLocal = mapS.getMapLocationComponent();
		mapLocal.mapSolverFilter="shooterSolver";
		landMark = (LandmarksComponent) entMan.addComponent(GameComps.COMP_LANDMARKS, mapLocal);
		landMark.loadFromElement(em.pickFinal("surreal shooter landMarkGroup leaf"));
		//assertTrue(landmark.landmarks.size()>0);
		assertTrue(landMark.obstacles.size()>0);
		assertNotNull(landMark.landMarkInfo);
		assertNotNull(landMark.obstacleInfo);
		
		genre = mockGameGenre();
		genre.elGameAxis=mockPlatformAxis();
		mapLocal.mapLocation.setProperty(GameProperties.MIN_LENGTH, 10);
		
		map = startMapComponent();
		map.setStage(MapComponent.TERRAIN_GENERATED);
		mapS.iterateEntity(entity, map, 0);
		assertNotNull(map.mapSolver);
		CRJavaUtils.IS_ANDROID=true;
		ent = mockPlayer();
		joystick = mockJoystick(ent);
		controllers = joystick.playerController.getPropertyAsGE(GameProperties.CONTROLLERS);
		
		player = ECS.getPlayerComponent(ent);
		actionController = new ActionController();
		player.setActionController(actionController);
		IReceiveAction actionReceiver = new IReceiveAction(){

			@Override
			public void processAction(String action, float mult, float tpf) {
			}
			
		};
		actionController.enableAction(CharActions.MOVE_FORWARD, actionReceiver);
		actionController.enableAction(CharActions.MOVE_BACKWARD, actionReceiver);
		
		joyS = initJoystickSystem();
	}

	
	@Test
	public void testShooterController() {
		setupTest();
		
		//DirectionalController
		controllers.setProperty("class","gyroAxis");
		controllers.setProperty(GameProperties.AXIS_MOVEMENT,"X");
		controllers.setProperty(GameProperties.ALLOW_Y,"1");
		
		joystick.loadFromElement(joystick.getElement());
		
		joyS.iterateEntity(ent, joystick, 0);
		
		assertTrue(joystick.controllers.size()>0);
		IGameJoystick gyro=joystick.controllers.get(0);
		assertNotNull(gyro);
		
		if (gyro instanceof GyroAxisJoystick){
			validaGyroAxis((GyroAxisJoystick)gyro,actionController);
		}
	}

	private void validaGyroAxis(GyroAxisJoystick gyro, ActionController actionController) {
		assertTrue(gyro.axis.equals("X"));
		//assertTrue(gyro.axisIndex==0);
		
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		
		AndroidUtils.orient.x=1;
		gyro.updateJoystick(1);
		assertTrue(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
		
		AndroidUtils.orient.x=-1;
		gyro.updateJoystick(1);
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertTrue(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
		
		AndroidUtils.orient.x=0;
		gyro.updateJoystick(1);
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
	}
	
	
	@Test
	public void testShooterDirector() {
		setupTest();
		
		DirectorSystem directorS = initDirectorSystem();
		
		ShooterDirectorComponent comp=mockShooterComponent(); 
		
		directorS.iterateEntity(map, comp, 0);
		
		
	}
	
	@Test
	public void testShooterCamera() {
		setupTest();
		IGameElement elCamType = genre.elGameAxis.getPropertyAsGE(GameProperties.CAM_TYPE);
		assertNotNull(elCamType);
		//assertTrue(elCamType.getProperty(GameProperties.VALUE).equals(CameraSystem.UP_DOWN));
		
		CameraSystem camS = initCameraSystem();
		CamComponent cam = startCamComponent(ent);
		cam.firstTick=true;
		IGameElement camElement = cam.getElement().getPropertyAsGE(GameProperties.CAM_TYPE);
		camElement.setProperty(GameProperties.DIST_X, 5);
		camElement.setProperty(GameProperties.DIST_Y, 0);
		camElement.setProperty(GameProperties.DIST_Z, 0);
		cam.loadFromElement(cam.getElement());
		camS.iterateEntity(ent, cam, 1);
	
		assertNotNull(cam.camNode);
		
		assertTrue("DistX errada:"+cam.distX,cam.distX>0);
		assertTrue("DistY errada:"+cam.distY,cam.distY==0);
		assertTrue("DistZ errada:"+cam.distZ,cam.distZ==0);
		
		
	}
}
