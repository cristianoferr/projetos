package com.cristiano.java.gameObjects.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.macro.GameOppositionComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.FXLibraryComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.systems.DamageCalculatorSystem;
import com.cristiano.java.gm.ecs.systems.DamageOverTimeSystem;
import com.cristiano.java.gm.ecs.systems.LoadEntitySystem;
import com.cristiano.java.gm.ecs.systems.MeshLoaderSystem;
import com.cristiano.java.gm.ecs.systems.RenderSystem;
import com.cristiano.java.gm.ecs.systems.ThemeSystem;
import com.cristiano.java.gm.ecs.systems.TimerSystem;
import com.cristiano.java.gm.ecs.systems.art.ImageRequestSystem;
import com.cristiano.java.gm.ecs.systems.art.TextureGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.macro.BubblePopperSystem;
import com.cristiano.java.gm.ecs.systems.macro.GameEventSystem;
import com.cristiano.java.gm.ecs.systems.macro.QueryBestiarySystem;
import com.cristiano.java.gm.ecs.systems.macro.TeamSystem;
import com.cristiano.java.gm.ecs.systems.macro.VictoryCheckerSystem;
import com.cristiano.java.gm.ecs.systems.macro.directors.DirectorSystem;
import com.cristiano.java.gm.ecs.systems.map.MapLoaderSystem;
import com.cristiano.java.gm.ecs.systems.map.MapWorldSystem;
import com.cristiano.java.gm.ecs.systems.map.RoadSolverSystem;
import com.cristiano.java.gm.ecs.systems.map.RoomSolverSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainLoaderSystem;
import com.cristiano.java.gm.ecs.systems.persists.AssetLoadRequestSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIActionListenerSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIControlSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIPanelSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIScreenSystem;
import com.cristiano.java.gm.ecs.systems.unit.AISystem;
import com.cristiano.java.gm.ecs.systems.unit.BulletSystem;
import com.cristiano.java.gm.ecs.systems.unit.DeathSystem;
import com.cristiano.java.gm.ecs.systems.unit.JoystickSystem;
import com.cristiano.java.gm.ecs.systems.unit.PhysicsSystem;
import com.cristiano.java.gm.ecs.systems.unit.PlayerSystem;
import com.cristiano.java.gm.ecs.systems.unit.TargetSystem;
import com.cristiano.java.gm.ecs.systems.unit.TargettingSystem;
import com.cristiano.java.gm.ecs.systems.unit.UnitRoleSystem;
import com.cristiano.java.gm.ecs.systems.unit.actuators.WeaponSystem;
import com.cristiano.java.gm.ecs.systems.unit.fx.FXLibrarySystem;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.BatterySystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.RaceGoalSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.UnitPositionSystem;
import com.cristiano.java.gm.ecs.systems.unit.sensors.RadarSystem;
import com.cristiano.java.gm.ecs.systems.visual.BillboardSystem;
import com.cristiano.java.gm.ecs.systems.visual.CameraSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.java.jme.tests.mocks.MockFactory;
import com.cristiano.java.jme.tests.mocks.Mockery;
import com.cristiano.java.jme.tests.persistence.TestGameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public abstract class AbstractTest extends Mockery {
	public static final float TERRAIN_HEIGHT = 500;

	// MockFactory factory=null;
	public static IGameElement world;
	public static IGameFactory factory;
	public static IFinalGame game;
	public static BPBuilderState integr;
	public static IGameElement macroDefs;
	
	public static IGameSystem initSystem(String name) {
		IGameSystem system = entMan.getSystem(name);
		if (system != null) {
			return system;
		}
		IGameElement elSystem = em.pickFinal(name);
		if (elSystem == null) {
			// CRJavaUtils.instanciaClasse(className);
			system = MockFactory.instantiateSystem(name);

		} else {
			system = (IGameSystem) factory.createSystemFrom(elSystem);
		}
		if (system==null){
			return null;
		}
		// assertNotNull("System element is null:" + name, elSystem);
		assertNotNull("System is null:" + name, system);
		system.initSystem(entMan, game);
		entMan.addSystem(system);
		return system;
	}

	@Test
	public void testImportExportGame() {
		TestGameState.validateExportImport(integr,game);
	}	
	@Test
	public void testWriteReadJSON() {
		TestGameState.validateWriteReadJSON(integr);
	}	
	
	
	public AIComponent initConditionDataComponent() {
		AIComponent dataComp = (AIComponent) entMan.spawnComponent(GameComps.COMP_NPC_CONDITION_CHECKER);
		IGameElement el = em.pickFinal(GameComps.COMP_NPC_CONDITION_CHECKER);
		dataComp.loadFromElement(el);
		assertNotNull(dataComp);
		assertNotNull(dataComp.elConditions);
		return dataComp;
	}

	// systems

	// systems


	public BubblePopperSystem initBubblePopperSystem() {
		return (BubblePopperSystem) initSystem("BubblePopperSystem");
	}

	public AssetLoadRequestSystem initAssetLoadRequestSystem() {
		return (AssetLoadRequestSystem) initSystem("AssetLoadRequestSystem");
	}

	public UIActionListenerSystem initUIActionListenerSystem() {
		return (UIActionListenerSystem) initSystem("UIActionListenerSystem");
	}

	

	public LoadEntitySystem initLoadEntitySystem() {
		return (LoadEntitySystem) initSystem("LoadEntitySystem");
	}

	public DeathSystem initDeathSystem() {
		return (DeathSystem) initSystem("DeathSystem");
	}

	public BatterySystem initBatterySystem() {
		return (BatterySystem) initSystem("BatterySystem");
	}
	
	
	public DirectorSystem initDirectorSystem() {
		return (DirectorSystem) initSystem(DirectorSystem.class.getSimpleName());
	}
	public AISystem initAISystem() {
		return (AISystem) initSystem(AISystem.class.getSimpleName());
	}
	public BillboardSystem initBillboardSystem() {
		return (BillboardSystem) initSystem(BillboardSystem.class.getSimpleName());
	}
	public UIScreenSystem initUIScreenSystem() {
		return (UIScreenSystem) initSystem(UIScreenSystem.class.getSimpleName());
	}
	public UIPanelSystem initUIPanelSystem() {
		return (UIPanelSystem) initSystem(UIPanelSystem.class.getSimpleName());
	}
	public UIControlSystem initUIControlSystem() {
		return (UIControlSystem) initSystem(UIControlSystem.class.getSimpleName());
	}
	public JoystickSystem initJoystickSystem() {
		return (JoystickSystem) initSystem(JoystickSystem.class.getSimpleName());
	}
	public UnitPositionSystem initUnitPositionSystem() {
		return (UnitPositionSystem) initSystem(UnitPositionSystem.class.getSimpleName());
	}

	public VictoryCheckerSystem initVictoryCheckerSystem() {
		return (VictoryCheckerSystem) initSystem("VictoryCheckerSystem");
	}

	public RaceGoalSystem initRaceGoalSystem() {
		return (RaceGoalSystem) initSystem("RaceGoalSystem");
	}

	public NPCStateManagerSystem initNPCStateManagerSystem() {
		return (NPCStateManagerSystem) initSystem("NPCStateManagerSystem");
	}


	public RenderSystem initRenderSystem() {
		return (RenderSystem) initSystem("RenderSystem");
	}

	public ImageRequestSystem initImageRequestSystem() {
		return (ImageRequestSystem) initSystem("ImageRequestSystem");
	}

	public GameEventSystem initGameEventSystem() {
		return (GameEventSystem) initSystem("GameEventSystem");
	}

	public static CameraSystem initCameraSystem() {
		return (CameraSystem) initSystem("CameraSystem");
	}

	public HealthSystem initHealthSystem() {
		return (HealthSystem) initSystem(HealthSystem.class.getSimpleName());
	}

	public DamageCalculatorSystem initDamageCalculatorSystem() {
		return (DamageCalculatorSystem) initSystem(DamageCalculatorSystem.class.getSimpleName());
	}

	public DamageOverTimeSystem initDamageOverTimeSystem() {
		return (DamageOverTimeSystem) initSystem(DamageOverTimeSystem.class.getSimpleName());
	}

	public QueryBestiarySystem initQueryBestiarySystem() {
		return (QueryBestiarySystem) initSystem(QueryBestiarySystem.class.getSimpleName());
	}

	public PhysicsSystem initPhysicsSystem() {
		return (PhysicsSystem) initSystem(PhysicsSystem.class.getSimpleName());
	}

	public RadarSystem initRadarSystem() {
		return (RadarSystem) initSystem(RadarSystem.class.getSimpleName());
	}

	public TargettingSystem initTargettingSystem() {
		return (TargettingSystem) initSystem(TargettingSystem.class.getSimpleName());
	}
	public TargetSystem initTargetSystem() {
		return (TargetSystem) initSystem(TargetSystem.class.getSimpleName());
	}

	public BulletSystem initBulletSystem() {
		return (BulletSystem) initSystem(BulletSystem.class.getSimpleName());
	}

	public UnitRoleSystem initUnitRoleSystem() {
		return (UnitRoleSystem) initSystem(UnitRoleSystem.class.getSimpleName());
	}

	public WeaponSystem initWeaponSystem() {
		return (WeaponSystem) initSystem(WeaponSystem.class.getSimpleName());
	}

	public PlayerSystem initPlayerSystem() {
		return (PlayerSystem) initSystem(PlayerSystem.class.getSimpleName());
	}

	public TimerSystem initTimerSystem() {
		return (TimerSystem) initSystem(TimerSystem.class.getSimpleName());
	}

	public TextureGeneratorSystem initTextureSystem() {
		return (TextureGeneratorSystem) initSystem(TextureGeneratorSystem.class.getSimpleName());
	}

	public RoomSolverSystem initRoomSolverSystem() {
		return (RoomSolverSystem) initSystem(RoomSolverSystem.class.getSimpleName());
	}

	public RoadSolverSystem initRoadSolverSystem() {
		return (RoadSolverSystem) initSystem(RoadSolverSystem.class.getSimpleName());
	}

	public TerrainGeneratorSystem initTerrainSystem() {
		return (TerrainGeneratorSystem) initSystem(TerrainGeneratorSystem.class.getSimpleName());
	}
	public TerrainLoaderSystem initTerrainLoaderSystem() {
		return (TerrainLoaderSystem) initSystem(TerrainLoaderSystem.class.getSimpleName());
	}

	public TeamSystem initTeamSystem() {
		return (TeamSystem) initSystem(TeamSystem.class.getSimpleName());
	}

	public ThemeSystem initThemeSystem() {
		return (ThemeSystem) initSystem(ThemeSystem.class.getSimpleName());
	}

	public static FXLibrarySystem initFXLibrarySystem() {
		return (FXLibrarySystem) initSystem(FXLibrarySystem.class.getSimpleName());
	}

	public MapLoaderSystem initMapLoaderSystem() {
		return (MapLoaderSystem) initSystem(MapLoaderSystem.class.getSimpleName());
	}
	public MapWorldSystem initMapWorldSystem() {
		return (MapWorldSystem) initSystem(MapWorldSystem.class.getSimpleName());
	}

	public MeshLoaderSystem initMeshLoaderSystem() {
		return (MeshLoaderSystem) initSystem(MeshLoaderSystem.class.getSimpleName());
	}

	// components
	public void startBestiaryLibComponent() {
		IGameComponent genreC = entMan.addIfNotExistsComponent(GameComps.COMP_BESTIARY_LIB, entity);
		assertNotNull("BestiaryLib null", genreC);
	}

	public CamComponent startCamComponent(IGameEntity ent) {
		CamComponent comp = (CamComponent) entMan.addComponent(GameComps.COMP_CAM, ent);
		IGameElement ge = em.pickFinal(GameComps.COMP_CAM);
		if (ge==null){
			Log.warn("CamComponent element is null, mocking it...");
			ge=Mockery.mockCamElement();
		}
		comp.loadFromElement(ge);
		
		return comp;
	}
	
	public static BubbleDataComponent startBubbleDataComponent() {
		BubbleDataComponent genreC = (BubbleDataComponent) entMan.addIfNotExistsComponent(GameComps.COMP_BUBBLE_DATA, entity);
		assertNotNull("COMP_BUBBLE_DATA null", genreC);
		return genreC;
	}

	

	public static FXLibraryComponent startFXLibraryComponent() {
		FXLibraryComponent fxLibC = (FXLibraryComponent) entMan.addIfNotExistsComponent(GameComps.COMP_FX_LIB, entity);
		IGameElement el = em.pickFinal(GameComps.COMP_FX_LIB);
		if (el==null){
			Log.error("FXLibraryComponent is null");
		}
		fxLibC.loadFromElement(el);
		fxLibC._reuseComp=startReuseComponent();
		assertNotNull("COMP_FX_LIB null", fxLibC);
		assertNotNull("COMP_FX_LIB.library null", fxLibC.effects);
		assertTrue("library empty", fxLibC.effects.size() > 0);
		return fxLibC;
	}

	public MapLocationComponent startMapLocationComponent() {
		MapLocationComponent genreC = (MapLocationComponent) entMan.addIfNotExistsComponent(GameComps.COMP_MAP_LOCATION, entity);
		IGameElement el = em.pickFinal(GameComps.COMP_MAP_LOCATION);
		if (el == null) {
			Log.info("MapLocationComponent is null, mocking it...");

		} else {
			genreC.loadFromElement(el);
		}
		assertNotNull("MapLocationComponent null", genreC);

		return genreC;
	}
	

	protected HealthComponent startHealthComponent() {
		HealthComponent healthC = (HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, entity);
		healthC.setMaxValue(100);
		healthC.setCurrValue(100);
		return healthC;
	}


	public ImageRequestComponent startImageRequestComponent() {
		ImageRequestComponent genreC = (ImageRequestComponent) entMan.addIfNotExistsComponent(GameComps.COMP_REQUEST_IMAGE, entity);
		genreC.loadFromElement(em.pickFinal(GameComps.COMP_REQUEST_IMAGE));
		assertNotNull("ImageRequestComponent null", genreC);
		return genreC;
	}

	public HealthComponent startHealthComponent(IGameEntity ent) {
		HealthComponent healthC = (HealthComponent) entMan.addIfNotExistsComponent(GameComps.COMP_RESOURCE_HEALTH, ent);
		healthC.setMaxValue(100);
		healthC.setCurrValue(100);
		return healthC;
	}

	public InternationalComponent startInternationalComponent() {
		InternationalComponent genreC = (InternationalComponent) entMan.addIfNotExistsComponent(GameComps.COMP_INTERNATIONAL, entity);
		IGameElement element = em.pickFinal(GameComps.COMP_INTERNATIONAL);
		if (element==null){
			Log.info("InternationalComponent element is null, mocking it");
			element=new GenericElement(em);
			element.setProperty(GameProperties.CURRENT_LANGUAGE, "en");
			element.setProperty(GameProperties.DEFAULT_LANGUAGE, "en");
			element.setParam(Extras.LIST_OBJECT,GameProperties.LANGUAGES+"#0", new GenericElement(em));
			
			GenericElement elText = new GenericElement(em);
			elText.setName("testText");
			GenericElement elTextEN = new GenericElement(em);
			elTextEN.setProperty("value=en");
			elTextEN.setProperty("text=Testing");
			GenericElement elTextPT = new GenericElement(em);
			elTextPT.setProperty("value=ptbr");
			elTextPT.setProperty("text=Testando");
			elText.setParam(Extras.LIST_OBJECT,"translation#0",elTextEN);
			elText.setParam(Extras.LIST_OBJECT,"translation#1",elTextPT);
			
			element.setParam(Extras.LIST_OBJECT,"strings#0",elText);
		}
		genreC.loadFromElement(element);
		assertNotNull("COMP_INTERNATIONAL null", genreC);
		assertNotNull("COMP_INTERNATIONAL.library null", genreC.languages);
		assertTrue("languages empty", genreC.languages.size() > 0);
		return genreC;
	}

	public static ReuseManagerComponent startReuseComponent() {
		ReuseManagerComponent genreC=(ReuseManagerComponent) entity.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER);
		if (genreC!=null){
			return genreC;
		}
		genreC = (ReuseManagerComponent) entMan.addIfNotExistsComponent(GameComps.COMP_REUSE_MANAGER, entity);
		if (genreC.getElement() == null) {
			IGameElement el = em.pickFinal(GameComps.COMP_REUSE_MANAGER);
			if (el==null){
				Log.info("ReuseManagerComponent element is null, mocking it");
				el=new GenericElement(em);
				el.setProperty("createdON","startReuseComponent");
			}
			genreC.loadFromElement(el);
		}
		assertNotNull("ReuseManagerComponent null", genreC);
		entity.attachComponent(genreC);
		return genreC;
	}
	/*
	public ReuseManagerComponent startReuseComponent() {
		ReuseManagerComponent genreC = (ReuseManagerComponent) entMan.addIfNotExistsComponent(
				GameComps.COMP_REUSE_MANAGER, entity);
		if (genreC.getElement() == null) {
			genreC.loadFromElement(em.pickFinal("ReuseManagerComponent leaf"));
		}
		assertNotNull("ReuseManagerComponent null", genreC);
		entity.attachComponent(genreC);
		return genreC;
	}*/
/*
	public void startGameGenreComponent() {
		IGameComponent genreC = entMan.addIfNotExistsComponent(GameComps.COMP_GAME_GENRE, entity);
		assertNotNull("COMP_GAME_GENRE null", genreC);
		IGameElement elGameGenre = em.pickFinal(TestStrings.TEST_GENRE_TAG);
		
		if (elGameGenre == null) {
			Log.warn("elGameGenre is null, generating a mock element...");
			elGameGenre = new GenericElement(em);
			elGameGenre.setProperty(GameProperties.BUDGET_PLAYER,100);
			elGameGenre.setProperty(GameProperties.BUDGET_NPC,100);

		}

		genreC.loadFromElement(elGameGenre);
	}
*/
	public static RoomComponent mockRoom(MapComponent mapC) {
		RoomComponent roomI = (RoomComponent) entMan.addComponent(GameComps.COMP_ROOM, mapC);
		assertNotNull("room null", roomI);
		roomI.setPosition(100, 0, 100);
		roomI.setDimension(50, 10, 50);
		roomI.startingRoom = true;
		return roomI;
	}

	public TerrainComponent mockTerrainComponent(MapComponent mapC) {
		TerrainComponent terrainC = (TerrainComponent) entMan.spawnComponent(GameComps.COMP_TERRAIN);
		IGameElement el=em.createElement();
		el.setProperty(GameProperties.SCALE, 1);
		el.setProperty(GameProperties.LENGTH, 7);
		el.setProperty(GameProperties.DEFAULT_HEIGHT, TERRAIN_HEIGHT);
		el.setProperty(GameProperties.TERRAIN_TYPE, "flat");
		terrainC.loadFromElement(el);
		if (mapC != null) {
			mapC.attachComponent(terrainC);
		}
		
		terrainC.setHeightMap(new float[terrainC.lengthOnPower]);
		return terrainC;
	}

	public static GameConstsComponent startGameConstsComponent() {
		GameConstsComponent data = (GameConstsComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_GAME_CONSTS);
		if (data == null) {
			data = (GameConstsComponent) entMan.addComponent(GameComps.COMP_GAME_CONSTS, game.getGameEntity());
			
			IGameElement elGame=em.createElement();
			
			IGameElement elUiConsts=em.createElement();
			IGameElement elTheme=em.createElement();
			//elGame.setProperty(GameProperties.RANGE_DETECT, val);
			
			IGameElement elGameComp=em.createElement();
			
			elGameComp.setProperty(GameProperties.GAME_CONSTS,elGame);
			elGameComp.setProperty(GameProperties.UI_CONSTS,elUiConsts);
			elGameComp.setProperty(GameProperties.THEME,elTheme);
			data.loadFromElement(elGameComp);
			data.visualTargetType="arrow";
			entMan.cleanup();
		}
		return data;
	}

	public static MapComponent startMapComponent() {
		IGameComponent mapWorld = getMapWorld();
		MapComponent data = (MapComponent) mapWorld.getComponentWithIdentifier(GameComps.COMP_MAP);
		if (data == null) {
			data = (MapComponent) entMan.addComponent(GameComps.COMP_MAP, mapWorld);
			Log.info("Mocking map element...");
			data.loadFromElement(Mockery.mockMapElement());
			// data.loadFromElement(em.pickFinal(TestStrings.TAG_MAPGD));
			//mockMapComponent(data);

			entMan.cleanup();
			//GameGenreComponent genre = (GameGenreComponent) entMan.addComponent(GameComps.COMP_GAME_GENRE, data);
			// genre.loadFromElement(em.pickFinal(TestStrings.TAG_GAME_GENRE));
			//entMan.cleanUp();
			
			mockRoom(data);
		}
		if (!data.containsComponent(GameComps.COMP_GAME_OPPOSITION)) {
			Mockery.mockOpposition(data);
		}
		
		
		return data;
	}

	
	
	private static MapWorldComponent getMapWorld() {
		MapWorldComponent data = (MapWorldComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_MAP_WORLD);
		if (data == null) {
			data = (MapWorldComponent) entMan.addComponent(GameComps.COMP_MAP_WORLD, game.getGameEntity());
			entMan.cleanup();
		}
		return data;
	}
	
	
	public static MapWorldComponent startMapWorldComponent(){
		MapWorldComponent mapW=(MapWorldComponent)entMan.addIfNotExistsComponent(GameComps.COMP_MAP_WORLD, entity);
		assertNotNull(mapW);
		return mapW;
	}


	public IGameElement defineMapOpposition(MapComponent mapC, String tag) {
		mapC.elMapOpposition = em.pickFinal(GameOppositionComponent.GAME_OPPOSITION_TAG + " " + tag);
		assertNotNull(mapC.elMapOpposition);
		return mapC.elMapOpposition;
	}
	
	

}
