package com.cristiano.java.jme.tests.genres;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.builder.map.ArenaMapSolver;
import com.cristiano.java.gm.builder.map.ShooterMapSolver;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.directors.DirectorComponent;
import com.cristiano.java.gm.ecs.comps.macro.directors.ShooterDirectorComponent;
import com.cristiano.java.gm.ecs.comps.map.LandmarksComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.JoystickComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.systems.macro.directors.DirectorSystem;
import com.cristiano.java.gm.ecs.systems.map.MapLoaderSystem;
import com.cristiano.java.gm.ecs.systems.unit.JoystickSystem;
import com.cristiano.java.gm.ecs.systems.visual.CameraSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
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

public class TestGenreArena extends MockAbstractTest {
	
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

	
	protected void setupTest() {
		mapS = initMapLoaderSystem();
		mapLocal = mapS.getMapLocationComponent();
		mapLocal.mapSolverFilter="arena";
		landMark = (LandmarksComponent) entMan.addComponent(GameComps.COMP_LANDMARKS, mapLocal);
		landMark.loadFromElement(em.pickFinal("surreal arena landMarkGroup leaf"));
		//assertTrue(landmark.landmarks.size()>0);
		assertTrue(landMark.obstacles.size()>0);
		assertNotNull(landMark.landMarkInfo);
		assertNotNull(landMark.obstacleInfo);
		
		genre = mockGameGenre();
		genre.elGameAxis=mock25DAxis();
		
		mapLocal.mapLocation.setProperty(GameProperties.MIN_LENGTH, 10);
		
		map = startMapComponent();
		map.setStage(MapComponent.TERRAIN_GENERATED);
		map.maxRooms=1;
		map.length=1000;
		map.roomHeight=100;
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
	public void testArenaMap() {
		setupTest();
		ArenaMapSolver solver=(ArenaMapSolver) map.mapSolver;
		assertNotNull(solver);
		
		assertNotNull(solver.room);
		assertTrue(solver.room.getDimensions().length()>500);
		assertTrue(solver.room.getDimensions().length()<map.length*map.length);
		assertTrue(solver.room.getDimensions().x>0);
		assertTrue(solver.room.getDimensions().y>0);
		assertTrue(solver.room.getDimensions().z>0);
		
		boolean complete=solver.hasCompleted();
		assertTrue(complete);
		assertTrue(solver.room.containsComponent(GameComps.COMP_CHILD));
		
		List<IGameComponent> comps = solver.room.getComponents(GameComps.COMP_CHILD);
		for (IGameComponent comp:comps){
			ChildComponent child=(ChildComponent) comp;
			Vector3f dim = child.getElementDimension();
			Vector3f posc = child.getElementPosition();
			assertTrue(dim.length()>0);
			assertTrue(posc.length()>0);
		}
	}
	
	@Test
	public void testArenaController() {
		setupTest();
		
		//DirectionalController
		controllers.setProperty("class","gyroAxis");
		controllers.setProperty(GameProperties.AXIS_MOVEMENT,"Z");
		controllers.setProperty(GameProperties.ALLOW_Y,"0");
		controllers.setProperty(GameProperties.ALLOW_X,"1");
		controllers.setProperty(GameProperties.ALLOW_Z,"1");
		
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
		assertTrue(gyro.axis.equals("Z"));
		//assertTrue(gyro.axisIndex==2);
		
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		
		AndroidUtils.orient.y=1;
		gyro.updateJoystick(1);
		assertTrue(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
		
		AndroidUtils.orient.y=-1;
		gyro.updateJoystick(1);
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertTrue(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
		
		AndroidUtils.orient.y=0;
		gyro.updateJoystick(1);
		assertFalse(actionController.containsActiveAction(gyro.getForwardAction()));
		assertFalse(actionController.containsActiveAction(gyro.getBackwardAction()));
		actionController.update(0);
	}
}
