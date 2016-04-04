package com.cristiano.java.jme.tests.mocks;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.gameObjects.tests.AbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.macro.GameObjectiveComponent;
import com.cristiano.java.gm.ecs.comps.macro.GameOppositionComponent;
import com.cristiano.java.gm.ecs.comps.macro.VictoryCheckerComponent;
import com.cristiano.java.gm.ecs.comps.macro.directors.ShooterDirectorComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.JoystickComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.TargettingComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.WeaponComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.SpeedComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.RadarComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.comps.visual.ThemeComponent;
import com.cristiano.java.gm.ecs.systems.visual.CameraSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Mockery {

	public static ElementManager em;
	public static EntityManager entMan;
	public static IGameEntity entity;
	protected static JMESnippets snippets;

	protected SpatialComponent mockSpatial(IGameEntity ent) {
		SpatialComponent spat=(SpatialComponent) entMan.addComponent(GameComps.COMP_SPATIAL, ent);
		spat.spatial(snippets.generateBox(ColorRGBA.Black, Vector3f.UNIT_XYZ, Vector3f.UNIT_XYZ));
		IGameElement ge=mockSpatialElement();
		spat.loadFromElement(ge);
		return spat;
	}

	
	private IGameElement mockSpatialElement() {
		IGameElement ge = em.createElement();
		return ge;
	}

	
	protected ShooterDirectorComponent mockShooterComponent() {
		ShooterDirectorComponent comp=(ShooterDirectorComponent) entMan.spawnComponent(GameComps.COMP_DIRECTOR_SHOOTER);
		IGameElement mockShooterElement=mockShooterElement();
		assertNotNull(mockShooterElement);
		comp.loadFromElement(mockShooterElement);
		return comp;
	}

	protected IGameElement mockShooterElement() {
		IGameElement el=em.createElement();
		return el;
	}

	protected IGameElement mockElementTurret() {
		IGameElement elTurret = em.pickFinal(TestStrings.PARTE_TURRET);
		if (elTurret == null) {
			Log.info("Element turret is null, mocking it");
			elTurret = new GenericElement(em);
			elTurret.setProperty("angleLimit", "180");
			elTurret.setProperty("defaultDepth", "50");
			elTurret.setProperty("defaultHeight", "10");
			elTurret.setProperty("mass", "1");
			elTurret.setProperty("shotMargin", "10");
			elTurret.setProperty("turnRate", "100");
			elTurret.setProperty(GameProperties.BULLET_EFFECT_TYPE, "1");

			GenericElement elWeapon = mockWeaponElement();
			elTurret.setParam(Extras.LIST_ACTUATOR, "weapon", elWeapon);
		}
		return elTurret;
	}

	protected GenericElement mockWeaponElement() {
		GenericElement elWeapon = new GenericElement(em);
		elWeapon.setProperty("addToAction", "1");
		elWeapon.setProperty("angleLimit", "0");
		elWeapon.setProperty(GameProperties.TURN_RATE, "100");
		elWeapon.setProperty(GameProperties.MASS, "1");
		elWeapon.setProperty(GameProperties.BULLET_EFFECT_TYPE, "1");
		elWeapon.setProperty(Extras.PROPERTY_IDENTIFIER, GameComps.COMP_ACTUATOR_WEAPON);
		return elWeapon;
	}

	public static IGameElement mockMapElement() {
		IGameElement elMap = em.createElement();
		elMap.setProperty(GameProperties.MAP_POPULATE_ID, "{mapPopulate leaf}");
		elMap.setProperty(GameProperties.LENGTH, 3000);
		elMap.setProperty(GameProperties.ROOM_MAX, 8);
		elMap.setProperty(GameProperties.ENVIRO_SPACING, 10);
		elMap.setProperty(GameProperties.LAP_DISTANCE, 1000);
		elMap.setProperty(GameProperties.IS_ROOM_SIZE_RELATIVE, 1);
		elMap.setProperty(GameProperties.SEED, 1000);
		elMap.setProperty(GameProperties.ROAD_WALL_ENVIRO, mockRoadEnviroElement());
		elMap.setProperty(GameProperties.ROAD_FUNCTION_TAG, TestStrings.TAG_LINE);
		elMap.setProperty(GameProperties.ROAD_WIDTH, 10);
		elMap.setProperty(GameProperties.ROAD_POINTS, 3);
		elMap.setProperty(GameProperties.ROAD_PER_ROOM, 2);
		elMap.setProperty(GameProperties.ROOM_MAX_SIZE, 0.8f);
		elMap.setProperty(GameProperties.ROOM_MIN_SIZE, 0.8f);
		elMap.setProperty(GameProperties.AMBIENCE_THEME, mockAmbienceThemeElement());
		elMap.setProperty(GameProperties.MAP_OPPOSITION, mockOppositionElement());

		return elMap;
	}

	public static IGameElement mockAmbienceThemeElement() {
		IGameElement el = em.createElement();
		el.setProperty(GameProperties.AMBIENCE_ROAD_TEXTURE, "Textures/terrain/grass/clover1.jpg");
		el.setProperty(GameProperties.AMBIENCE_HIGH_HEIGHT_TEXTURE, "Textures/terrain/grass/clover1.jpg");
		el.setProperty(GameProperties.AMBIENCE_LOW_HEIGHT_TEXTURE, "Textures/terrain/grass/clover1.jpg");
		return el;
	}

	public static IGameElement mockRoadEnviroElement() {
		IGameElement geEnviro = em.createElement();
		geEnviro.setProperty(GameProperties.CARRIER_TAG, TestStrings.TAG_TEST_ENTITY);
		geEnviro.setProperty(GameProperties.MESH_TAG, TestStrings.TAG_MESH_BOX);
		geEnviro.setProperty(GameProperties.WALL_HEIGHT, 10);
		geEnviro.setProperty(GameProperties.WALL_WIDTH, 1);
		return geEnviro;
	}

	public static GameOppositionComponent mockOpposition(IGameComponent map) {
		GameOppositionComponent oppos = (GameOppositionComponent) entMan.addIfNotExistsComponent(GameComps.COMP_GAME_OPPOSITION, map);
		IGameElement mockOppositionElement = mockOppositionElement();
		oppos.loadFromElement(mockOppositionElement);
		oppos.scopeGame = new String[] { "TimeResourceComponent" };
		oppos.scopeTeam = new String[] { "PointsResourceComponent" };
		oppos.scopeUnit = new String[] { "HealthComponent" };
		return mockOppositionFor(map, GameOppositionComponent.TYPE_OPPOSITION_PLAYER_VERSUS);
	}

	private static IGameElement mockOppositionElement() {
		GenericElement ge = new GenericElement(em);
		ge.setName("mockOppos");
		IGameElement gePlayer= em.createElement();
		gePlayer.setProperty(GameProperties.LIVES, 10);
		gePlayer.setProperty(GameProperties.UNIT_ROLE, mockUnitRoleElement());
		gePlayer.setProperty(GameProperties.MIN_VARIATIONS, 4);
		
		IGameElement geEnemy= em.createElement();
		geEnemy.setProperty(GameProperties.LIVES, 10);
		geEnemy.setProperty(GameProperties.UNIT_ROLE, mockUnitRoleElement());
		geEnemy.setProperty(GameProperties.MIN_VARIATIONS, 4);
		
		ge.setProperty(GameProperties.PLAYER_SIDE,gePlayer);
		ge.setProperty(GameProperties.ENEMY_SIDE,geEnemy);
		
		
		IGameElement elStartingPosition = mockElementStartingPositionGrid();
		ge.setProperty(GameProperties.STARTING_POSITION, elStartingPosition);
		return ge;
	}

	public static GameOppositionComponent mockOppositionFor(IGameComponent mapC, String type) {

		GameOppositionComponent oppos = (GameOppositionComponent) mapC.getComponentWithIdentifier(GameComps.COMP_GAME_OPPOSITION);
		IGameElement element = oppos.getElement();
		if (type.equals(GameOppositionComponent.TYPE_OPPOSITION_MULTI_TEAMS)) {
			element.setProperty(GameProperties.MAX_TEAMS, "4");
			element.setProperty(GameProperties.MIN_TEAMS, "3");
			element.setProperty(GameProperties.SAME_TEAM_RELATION, "3");// 3=friend,
																		// 1=enemy;
			element.setName("multiTeams");
		}
		if (type.equals(GameOppositionComponent.TYPE_OPPOSITION_FREEFORALL)) {
			element.setProperty(GameProperties.MAX_TEAMS, "1");
			element.setProperty(GameProperties.MIN_TEAMS, "1");
			element.setProperty(GameProperties.SAME_TEAM_RELATION, "1");// 3=friend,
																		// 1=enemy;
			element.setName("freeforall");
		}
		if (type.equals(GameOppositionComponent.TYPE_OPPOSITION_PLAYER_VERSUS)) {
			element.setProperty(GameProperties.MAX_TEAMS, "2");
			element.setProperty(GameProperties.MIN_TEAMS, "2");
			element.setProperty(GameProperties.PLAYER_ONLY_TEAM, "1");
			element.setProperty(GameProperties.SAME_TEAM_RELATION, "3");// 3=friend,
																		// 1=enemy;
			
			/*IGameElement gePlayer= em.createElement();
			IGameElement geEnemy= em.createElement();
			
			gePlayer.setProperty(GameProperties.LIVES, 10);
			gePlayer.setProperty(GameProperties.RESPAWN, 1);
			geEnemy.setProperty(GameProperties.LIVES, 10);
			geEnemy.setProperty(GameProperties.RESPAWN, 1);
			
			element.setProperty(GameProperties.PLAYER_SIDE,gePlayer);
			element.setProperty(GameProperties.ENEMY_SIDE,geEnemy);*/
			
			element.setName("versus");
		}
		oppos.loadFromElement(element);
		return oppos;
	}

	public static UnitClassComponent mockUnitRole() {
		GenericElement ge = mockUnitRoleElement();

		UnitClassComponent ur = (UnitClassComponent) entMan.spawnComponent(GameComps.COMP_UNIT_CLASS);
		ur.loadFromElement(ge);
		ur.unitResources = new ArrayList<ResourceComponent>();
		ur.unitResources.add((ResourceComponent) entMan.spawnComponent(GameComps.COMP_RESOURCE_HEALTH));
		return ur;
	}

	public static GenericElement mockUnitRoleElement() {
		GenericElement ge = new GenericElement(em);
		ge.setProperty("budgetMultiplier", 1); // 1=default value
		ge.setProperty("dimensionMultiplier", 1);
		ge.setProperty("spawnChance", 1); // 1=100%
		ge.setProperty("rateOfFire", 1); // 1=100%
		ge.setProperty("unitRootTag", TestStrings.TAG_TEST_ENTITY);
		ge.setProperty("identifier", "mockRole");
		ge.setProperty("createdOn", "abstractTest.mockUnitRole");

		ge.setObject(GameProperties.RESOURCES, 0, mockHealthElement());

		// mocking bulletEntity=pickFinal
		GenericElement elBullet = new GenericElement(em);
		elBullet.setProperty("createdOn", "abstractTest.mockUnitRole");
		elBullet.setProperty(GameProperties.MESH_TAG, TestStrings.TAG_RACE_GOAL_ARROW);
		elBullet.setParam(Extras.LIST_DOMAIN, GameProperties.CLASS_PROPERTY, GameConsts.ENTITY_TYPE);
		elBullet.setParam(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS_NAME, JavaConsts.OBJECT_GAME_ENTITY);
		elBullet.setProperty(GameProperties.BULLET_CONTROLLER, "com.cristiano.java.gm.units.controls.GMBombControl"); // 1=100%
		elBullet.setProperty(GameProperties.BULLET_SPEED, 1);
		elBullet.setProperty(GameProperties.BLAST_RADIUS, 1);
		elBullet.setProperty(GameProperties.BULLET_GRAVITY, 1);
		elBullet.setProperty(GameProperties.BULLET_SPEED, 1);
		elBullet.setProperty(GameProperties.BULLET_RADIUS, 1);
		elBullet.setProperty(GameProperties.PROXIMITY_DETONATION, 1);
		elBullet.setProperty(GameProperties.BULLET_MASS, 1);

		ge.setProperty(GameProperties.BULLET_ENTITY, elBullet);
		return ge;
	}

	public static IGameElement mockHealthElement() {
		IGameElement ge = em.createElement();
		ge.setProperty(Extras.PROPERTY_IDENTIFIER, GameComps.COMP_RESOURCE_HEALTH);
		ge.setProperty(GameProperties.VALUE, 100);
		ge.setProperty(GameProperties.STARTING_VALUE, 100);
		ge.setProperty(GameProperties.VALUE, 100);
		return ge;
	}

	public static JoystickComponent mockJoystick(IGameEntity ent) {
		JoystickComponent joystick = (JoystickComponent) entMan.addComponent(GameComps.COMP_JOYSTICKS, ent);
		assertNotNull(joystick);
		IGameElement elJoystick = em.createElement();

		IGameElement elControllers = em.createElement();

		IGameElement elController = em.createElement();
		elController.setProperty("class", JMEConsts.JOYSTICK_MOCK_PAD);
		elController.setProperty("propClass", "propertyClass");
		elController.setProperty("createdAt", "mockJoystick");
		elControllers.setProperty("createdAt", "mockJoystick");
		elControllers.setProperty(GameProperties.CONTROLLERS, elController);
		elJoystick.setProperty("playerController", elControllers);
		joystick.loadFromElement(elJoystick);
		return joystick;
	}

	public static void mockUnit(IGameEntity ent) {
		DPSComponent dpsC = (DPSComponent) entMan.addComponent(GameComps.COMP_RESOURCE_DPS, ent);
		dpsC.setCurrValue(100);

		entMan.addComponent(GameComps.COMP_TRANSIENT, ent);
		HealthComponent hpC = (HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, ent);
		hpC.setCurrValue(100);
		hpC.setMaxValue(100);

		PositionComponent posC = (PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, ent);
		posC.setPos(new Vector3f(CRJavaUtils.random() * 100, 0, 0));

		RenderComponent renderC = (RenderComponent) entMan.addComponent(GameComps.COMP_RENDER, ent);
		renderC.node = new Node("MockEntityNode");
		OrientationComponent orientC = (OrientationComponent) entMan.addComponent(GameComps.COMP_ORIENTATION, ent);
		DimensionComponent dimC = (DimensionComponent) entMan.addComponent(GameComps.COMP_DIMENSION, ent);
		dimC.dimension.set(Vector3f.UNIT_XYZ);

		BillboardComponent billC = (BillboardComponent) entMan.addComponent(GameComps.COMP_BILLBOARD, ent);
		mockPhysComponent(ent);

		// PhysicsComponent physC = (PhysicsComponent)
		// entMan.addComponent(GameComps.COMP_PHYSICS, ent);

		SpeedComponent speedC = (SpeedComponent) entMan.addComponent(GameComps.COMP_SPEED, ent);
		speedC.setCurrValue(100);
		speedC.velocity = new Vector3f(10, 0, 0);

		UnitClassComponent roleC = Mockery.mockUnitRole();// (UnitClassComponent)
		// entMan.addComponent(GameComps.COMP_UNIT_CLASS,
		// ent);
		ent.attachComponent(roleC);

		assertNotNull(roleC);
	}

	public static void mockPhysComponent(IGameEntity ent) {
		PhysicsComponent physC = (PhysicsComponent) entMan.addComponent(GameComps.COMP_PHYSICS, ent);
		IGameElement gePhysC = em.createElement();
		gePhysC.setProperty(GameProperties.GRAVITY, 10);
		gePhysC.setProperty("createdAd", "mockPhysComponent");
		physC.loadFromElement(gePhysC);

		physC.firstTick = false;
		physC.actionGroups = new ArrayList<IGameElement>();
		IGameElement elActionGroup = em.createElement();
		elActionGroup.setProperty(GameProperties.ACTION_GROUP, "moveForward moveBackward turnRight turnLeft");
		physC.actionGroups.add(elActionGroup);
	}

	public static TargetComponent mockTarget(IGameEntity ent) {
		GameEntity targetEnt = new GameEntity();
		mockUnit(targetEnt);
		TargetComponent targetC = (TargetComponent) entMan.addComponent(GameComps.COMP_TARGET, ent);
		targetC.target = targetEnt;

		ECS.updateTargetPosition(ent, targetC);
		return targetC;
	}

	public static IGameElement mockMapLocationElement() {
		IGameElement el = em.createElement();
		IGameElement elML = em.createElement();
		elML.setProperty(GameProperties.ROOM_BUBBLE_FILTER, "{leaf}");
		elML.setProperty(GameProperties.MAP_SOLVER_FILTER, "{raceMapTest}");
		el.setProperty(GameProperties.MAP_LOCATION, elML);
		return el;
	}
	
	protected static GameGenreComponent mockGameGenre() {
		GameGenreComponent genre = (GameGenreComponent) entMan.addIfNotExistsComponent(GameComps.COMP_GAME_GENRE, entity);
		genre.loadFromElement(em.pickFinal(GameComps.COMP_GAME_GENRE));
		return genre;
	}

	public static IGameEntity mockPlayer() {
		return mockPlayer(entMan.createEntity());
	}

	public static IGameEntity mockPlayer(IGameEntity ent) {
		mockUnit(ent);

		entMan.addComponent(GameComps.COMP_PLAYER, ent);

		mockJoystick(ent);
		return ent;
	}

	public static void mockGame() {
		mockMapWorld();

		ThemeComponent theme = (ThemeComponent) entMan.addIfNotExistsComponent(GameComps.COMP_THEME, entity);
		theme.loadFromElement(MockThemeElement());
		MapLocationComponent mapLC = (MapLocationComponent) entMan.addIfNotExistsComponent(GameComps.COMP_MAP_LOCATION, entity);
		mapLC.loadFromElement(mockMapLocationElement());
		AbstractTest.startMapComponent();

		ReuseManagerComponent reuseC = (ReuseManagerComponent) entMan.addIfNotExistsComponent(GameComps.COMP_REUSE_MANAGER, entity);
		ThemeComponent themeC = (ThemeComponent) entMan.addIfNotExistsComponent(GameComps.COMP_THEME, entity);

		mockPhysSpaceComponent(entity);

		GameGenreComponent gameGenreC = mockGameGenre();
		

		NiftyComponent niftyC = (NiftyComponent) entMan.addIfNotExistsComponent(GameComps.COMP_NIFTY, entity);
		niftyC.loadFromElement(mockNiftyElement());
		niftyC.currentScreenName = GameConsts.SCREEN_RUNNING;

		CamComponent camC = (CamComponent) entMan.addIfNotExistsComponent(GameComps.COMP_CAM, entity);
		camC.firstTick = true;
		GenericElement ge = mockCamElement();
		camC.loadFromElement(ge);
		CameraSystem camS = AbstractTest.initCameraSystem();
		// camS.iterateEntity(entity, camC, 0);
	}

	public static GenericElement mockCamElement() {
		GenericElement ge = new GenericElement(em);

		GenericElement geCam = new GenericElement(em);
		ge.setProperty(GameProperties.CAM_TYPE, geCam);

		geCam.setProperty(GameProperties.VALUE, CameraSystem.FOLLOW);
		geCam.setProperty(GameProperties.DIST_X, 10);
		geCam.setProperty(GameProperties.DIST_Y, 10);
		geCam.setProperty(GameProperties.MAX_DIST, 10);
		geCam.setProperty(GameProperties.MIN_DIST, 10);
		return ge;
	}

	private static void mockPhysSpaceComponent(IGameEntity entity) {
		PhysicsSpaceComponent physC = (PhysicsSpaceComponent) entMan.addIfNotExistsComponent(GameComps.COMP_PHYSICS_SPACE, entity);
		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.GRAVITY, -10);
		physC.loadFromElement(ge);
	}

	private static IGameElement MockThemeElement() {
		return em.pickFinal(GameComps.COMP_THEME);
	}

	protected static IGameElement mockNPCStateRaceDriver() {
		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.ENTRY_CONDITIONS, "{}");
		ge.setProperty(GameProperties.KEEP_CONDITIONS, "{}");
		ge.setProperty(GameProperties.DESTINATION_STATES, "{}");

		return ge;
	}

	private static IGameElement mockNiftyElement() {
		IGameElement el = em.createElement();
		el.setProperty(GameProperties.DEFAULT_WIDTH, 1280);
		el.setProperty(GameProperties.DEFAULT_HEIGHT, 720);
		IGameElement elNiftyEffect = em.createElement();
		elNiftyEffect.setProperty(Extras.PROPERTY_VALUE, TestStrings.BATTLE_NOTIF_EFFECT);
		el.setObject(GameProperties.EFFECTS, 0, elNiftyEffect);
		return el;
	}

	public static MapWorldComponent mockMapWorld() {
		MapWorldComponent mapWC = AbstractTest.startMapWorldComponent();
		GenericElement ge = new GenericElement(em);
		GenericElement geMapWC = new GenericElement(em);
		ge.setProperty(GameProperties.MAP_WORLD, geMapWC);
		geMapWC.setProperty(GameProperties.MAP_SEQUENCE, GameConsts.SEQUENCING_SERIAL);
		geMapWC.setProperty(GameProperties.ALLOW_MAP_CHOOSING, 0);
		geMapWC.setProperty(GameProperties.CURRENT_MAP, 0);
		geMapWC.setObject(GameProperties.MAPS, 0, mockMapElement());
		mapWC.loadFromElement(ge);
		return mapWC;
	}

	public GameObjectiveComponent mockGameObjective(IGameEntity mapC) {
		GameObjectiveComponent objC = (GameObjectiveComponent) entMan.addComponent(GameComps.COMP_GAME_OBJECTIVE, mapC);
		IGameElement elTestObjective = em.pickFinal(TestStrings.TESTING_OBJECTIVE_TAG);
		if (elTestObjective == null) {
			Log.info("gameObjective is null... mocking it");
			objC.name = "TestObjectives";
			VictoryCheckerComponent testVictory = mockVictoryCondition();
			objC.victoryConditions.add(testVictory);
		} else {
			objC.loadFromElement(elTestObjective);

		}
		return objC;
	}

	public IGameElement mockMapPopulateElement(String tag) {
		IGameElement el = em.pickFinal(tag);
		if (el == null) {
			el = new GenericElement(em);
			el.setProperty("lineFunctionTag", "{line functions leaf}");
			el.setProperty("init", "'com.cristiano.java.gm.builder.map.LinearMapSolver'");
		}
		return el;
	}

	public TeamComponent startTeamComponent() {
		TeamComponent teamC = (TeamComponent) entMan.spawnComponent(GameComps.COMP_TEAM);
		assertNotNull(teamC);
		teamC.idTeam = 0;
		teamC.hasRespawn = false;
		teamC.isPlayerTeam = true;
		teamC.sameTeamRelation = LogicConsts.RELATION_FRIEND;
		IGameElement elStartingPosition = mockElementStartingPositionGrid();

		teamC.startingPosition = elStartingPosition;
		return teamC;
	}

	public static IGameElement mockGenericComponentElement(String[] tags) {
		GenericElement ge = new GenericElement(em);
		for (String tag : tags) {
			ge.addTag(tag);
		}
		return ge;
	}

	public VictoryCheckerComponent mockVictoryCondition() {
		
		IGameElement elCondiction=em.createElement();
		VictoryCheckerComponent vic = (VictoryCheckerComponent) entMan.spawnComponent(GameComps.COMP_VICTORY_CHECKER);
		List<IGameElement> elFinishs = new ArrayList<IGameElement>();
		List<IGameElement> elVictories = new ArrayList<IGameElement>();
		GenericElement elVictory = new GenericElement(em);
		elVictory.setProperty(VictoryCheckerComponent.CHECK0, "#c0Value");
		elVictory.setProperty(VictoryCheckerComponent.OPER,"<=");
		elVictory.setProperty(VictoryCheckerComponent.CHECK1,"1");
		elVictory.setProperty(VictoryCheckerComponent.COMPONENTS, "{UnitPositionComponent}");
		elVictories.add(elVictory);

		GenericElement elFinish = new GenericElement(em);
		elFinish.setProperty(VictoryCheckerComponent.CHECK0, "#c0Value");
		elFinish.setProperty(VictoryCheckerComponent.OPER,">=");
		elFinish.setProperty(VictoryCheckerComponent.CHECK1,"#c0MaxValue");
		elFinish.setProperty(VictoryCheckerComponent.COMPONENTS, "{LapResourceComponent}");
		elFinishs.add(elFinish);
		// vic.loadConditions(vic.finishConditions, elFinishs);
		// vic.loadConditions(vic.victoryConditions, elVictories);

		elCondiction.setProperty(VictoryCheckerComponent.FINISH_CONDITIONS, elFinish);
		elCondiction.setProperty(VictoryCheckerComponent.VICTORY_CONDITIONS, elVictory);
		vic.loadFromElement(elCondiction);
		return vic;
	}

	public AIComponent mockAIUnit(IGameEntity ent) {
		mockUnit(ent);
		AIComponent aiComp = (AIComponent) entMan.addComponent(GameComps.COMP_AI, ent);
		mockAIElement(ent, aiComp);

		return aiComp;
	}

	public AIComponent mockAIElement(IGameEntity ent, AIComponent stateCheckerComp) {

		IGameElement elAI;
		elAI = new GenericElement(em);
		elAI.setProperty("createdAd", "mockAIUnit");
		GenericElement elFollow = new GenericElement(em);
		elFollow.setProperty("identifier=" + GameComps.COMP_BEHAVIOR_FOLLOW_WAYPOINT);
		elFollow.setProperty("createdAd=mockAIUnit");
		elFollow.addTag(GameComps.COMP_BEHAVIOR_FOLLOW_WAYPOINT);
		elAI.setParam(Extras.LIST_OBJECT, "behaviours#0", elFollow);

		GenericElement elState = new GenericElement(em);
		elState.setProperty("identifier=" + GameComps.COMP_NPC_RACE_DRIVER);
		elState.setProperty("createdAd=mockAIUnit");
		elState.addTag(GameComps.COMP_NPC_RACE_DRIVER);
		elAI.setParam(Extras.LIST_OBJECT, "states#0", elState);

		elState = new GenericElement(em);
		elState.setProperty("identifier=" + GameComps.COMP_NPC_STATE_ATTACK);
		elState.setProperty("createdAd=mockAIUnit");
		elState.addTag(GameComps.COMP_NPC_STATE_ATTACK);
		elAI.setParam(Extras.LIST_OBJECT, "states#1", elState);

		elAI.setProperty("startableStates", GameComps.COMP_NPC_STATE_ATTACK);

		GenericElement elCond = new GenericElement(em);
		elCond.setProperty("statusTrue={EnemyNearStatusComponent}");
		elCond.setProperty("statusFalse={WeaponComponent}");
		elCond.setProperty("identifier=UnarmedAndEnemyNearCondition");
		elCond.setParam("status enemyNear=1");
		elCond.setParam("status armed=0");
		elAI.setParam(Extras.LIST_OBJECT, "conditions#0", elCond);

		stateCheckerComp.loadFromElement(elAI);
		return stateCheckerComp;
	}

	public static IGameElement mockElementStartingPositionGrid() {
		IGameElement elStartingPosition = em.createElement();
		elStartingPosition.setName(LogicConsts.STARTING_POSITION_GRID);

		elStartingPosition.setProperty(GameProperties.COLS, 2);
		elStartingPosition.setProperty(GameProperties.DEPTH, 20);
		elStartingPosition.setProperty(GameProperties.WIDTH, 30);
		return elStartingPosition;
	}

	public static BubbleDataComponent mockBubbleData() {
		BubbleDataComponent data = (BubbleDataComponent) entMan.addIfNotExistsComponent(GameComps.COMP_BUBBLE_DATA, entity);
		IGameElement el = em.pickFinal(GameComps.COMP_BUBBLE_DATA);
		if (el == null) {
			Log.warn("Element BubbleDataComponent null, moking it...");
			el = new GenericElement(em);

			GenericElement elEnviro = new GenericElement(em);// EnviroObjects
			el.setParam(Extras.LIST_OBJECT, "enviros#0", elEnviro);
			elEnviro.addTag(TestStrings.ROOM_ENVIRO_TEST_TAG);
			elEnviro.setProperty("subBubble=test");

			GenericElement elFitting = new GenericElement(em);
			elEnviro.setProperty("fitting", elFitting);
			elFitting.setProperty("maxX=1500");
			elFitting.setProperty("maxY=1500");
			elFitting.setProperty("maxZ=1500");
			elFitting.setProperty("minX=1");
			elFitting.setProperty("minY=-1");
			elFitting.setProperty("minZ=1");

			GenericElement elProps = new GenericElement(em);
			elEnviro.setProperty("properties", elProps);
			elProps.setProperty("isTerminal=0");
			elProps.setProperty("maxDimensionRatio=0.6");
			elProps.setProperty("maxAreaPerc=20");
			elProps.setProperty("minArea=200");
			elProps.setProperty("minDimensionRatio=0.6");
			elProps.setProperty("wallHeightMulti=0.6");
			elProps.setProperty("wallWidth=1");

			GenericElement elFloor = new GenericElement(em);
			elEnviro.setProperty("floor", elFloor);
			elFloor.setProperty("meshTag={box mesh leaf}");

			GenericElement elSeparator = new GenericElement(em);
			elEnviro.setProperty("separatorObj", elSeparator);
			elSeparator.setProperty("meshTag={mesh box leaf}");
			elSeparator.setProperty("wallHeight=10");
			elSeparator.setProperty("wallWidth=1");

			GenericElement elEntityObj = new GenericElement(em);
			elEnviro.setProperty("entityObj", elEntityObj);
			elEntityObj.setProperty("meshTag={mesh box leaf}");

		}
		data.loadFromElement(el);
		return data;
	}

	public static ResourceComponent mockUnitResource() {
		ResourceComponent resource = (ResourceComponent) entMan.spawnComponent(GameComps.COMP_RESOURCE_HEALTH);
		return resource;
	}
/*
	public static UnitRolesComponent mockUnitRoles(IGameEntity entity) {
		UnitRolesComponent rolesC = (UnitRolesComponent) entMan.addComponent(GameComps.COMP_UNIT_ROLES, entity);
		if (rolesC.getElement() == null) {
			IGameElement el = em.pickFinal("UnitRolesComponent leaf");
			if (el == null) {
				el = em.createElement();
				el.setParam(Extras.LIST_OBJECT, GameProperties.UNIT_CLASSES + "#0", Mockery.mockUnitRoleElement());
				// rolesC.classes.add(mockUnitRole());
			}
			rolesC.loadFromElement(el);
		}
		return rolesC;
	}*/

	protected static RoomComponent addRoom(MapComponent mapC) {
		RoomComponent roomI = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		assertNotNull("room null", roomI);
		roomI.setPosition(100, 0, 100);
		roomI.setDimension(50, 10, 50);
		roomI.startingRoom = true;
		return roomI;
	}

	protected static TargettingComponent mockTargetting(IGameEntity ent) {
		TargettingComponent targetting = (TargettingComponent) entMan.addComponent(GameComps.COMP_TARGETTING, ent);

		IGameElement geFixation = em.createElement();
		geFixation.setProperty(GameProperties.TIMEOUT_TARGET, 10);

		IGameElement geSelType = em.createElement();
		geSelType.setProperty(GameProperties.SELECTION_TYPE, LogicConsts.TARGETTING_RANDOM);

		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.TARGET_FIXATION, geFixation);
		ge.setProperty(GameProperties.TARGET_SELECTION, geSelType);

		targetting.loadFromElement(ge);
		return targetting;
	}

	protected static RadarComponent mockRadar(IGameEntity ent) {
		RadarComponent radarC = (RadarComponent) entMan.addComponent(GameComps.COMP_RADAR, ent);
		;

		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.RANGE_DETECT, 100);
		ge.setProperty(GameProperties.RANGE_NEAR, 30);
		radarC.loadFromElement(ge);
		return radarC;
	}

	protected static void mockWeapon(IGameEntity playerEnt) {
		WeaponComponent weapon = (WeaponComponent) entMan.addComponent(GameComps.COMP_ACTUATOR_WEAPON, playerEnt);

		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.ANGLE_LIMIT, 50);
		ge.setProperty(GameProperties.TURN_RATE, 1);
		ge.setProperty(GameProperties.SHOT_MARGIN, 1);
		ge.setProperty(GameProperties.MASS, 1);
		ge.setProperty(GameProperties.BULLET_EFFECT_TYPE, 1);
		weapon.loadFromElement(ge);
	}
	
	
	protected static IGameElement mock25DAxis() {
		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.ALLOW_X, 1);
		ge.setProperty(GameProperties.ALLOW_Y, 0);
		ge.setProperty(GameProperties.ALLOW_Z, 1);
		ge.setProperty(GameProperties.AXIS_MOVEMENT, "{xz}");
		
		IGameElement geCam = mockCamElement().getPropertyAsGE(GameProperties.CAM_TYPE);
		ge.setProperty(GameProperties.CAM_TYPE, geCam);
		return ge;
	}
	
	protected static IGameElement mockPlatformAxis() {
		IGameElement ge = em.createElement();
		ge.setProperty(GameProperties.ALLOW_X, 1);
		ge.setProperty(GameProperties.ALLOW_Y, 1);
		ge.setProperty(GameProperties.ALLOW_Z, 0);
		ge.setProperty(GameProperties.AXIS_MOVEMENT, "{x}");
		
		IGameElement geCam = mockCamElement().getPropertyAsGE(GameProperties.CAM_TYPE);
		ge.setProperty(GameProperties.CAM_TYPE, geCam);
		return ge;
	}
}
