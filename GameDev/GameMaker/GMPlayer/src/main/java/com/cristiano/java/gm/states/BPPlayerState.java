package com.cristiano.java.gm.states;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.interfaces.state.IIntegrationGameState;
import com.cristiano.java.gm.product.factory.ProductFactory;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.gm.utils.JSONGameLoader;
import com.cristiano.java.product.ElementStore;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.Listener;

import de.lessvoid.nifty.Nifty;

public class BPPlayerState extends JMEAbstractState implements
		IIntegrationGameState {

	protected IGameElement world;
	protected IGameFactory factory;
	protected IManageElements em;
	protected AppStateManager stateManager;

	ArrayList<IGameState> states = new ArrayList<IGameState>();
	protected IGameElement macroDefs;

	// holds the data to be loaded... will be nulled after being loaded...
	// public JSONObject json;

	public BPPlayerState() {
		this(true);
	}

	public BPPlayerState(boolean load) {
		if (load) {
			JSONGameLoader.loadJSON(GameProperties.FACTORY);
			JSONGameLoader.loadJSON(GameProperties.ELEMENT_MANAGER);
			JSONGameLoader.loadJSON(GameProperties.ENTITY_MANAGER);
			JSONGameLoader.loadJSON(GameProperties.WORLD_ELEMENT);
		}
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.stateManager = app.getStateManager();
		initObjects();

	}

	public void initObjects() {
		Bench.start(BenchConsts.EV_INITOBJECTS, BenchConsts.CAT_INITIALIZING);

		initElementManager();
		if (entMan != null) {
			Log.warn("entMan already instanciated... cancelling initObjects()");
			return;
		}
		initEntityManager();

		initFactory();
		startWorldEntity();

		Log.debug("Loading Game States...");
		Bench.start(BenchConsts.EV_INITOBJECTS + "loadGameStates",
				BenchConsts.CAT_INITIALIZING);
		loadGameStates();
		Bench.end(BenchConsts.EV_INITOBJECTS + "loadGameStates");

		Log.debug("Loading Game Systems...");
		Bench.start(BenchConsts.EV_INITOBJECTS + "loadGameSystems",
				BenchConsts.CAT_INITIALIZING);
		loadGameSystems();
		Bench.end(BenchConsts.EV_INITOBJECTS + "loadGameSystems");

		Log.debug("Loading Complete!");
		game.loadingComplete();

		Bench.end(BenchConsts.EV_INITOBJECTS);
	}

	private void initEntityManager() {
		entMan = new EntityManager();

	}

	protected void initElementManager() {
		Bench.start(BenchConsts.EV_IMPORT_EM, BenchConsts.CAT_INITIALIZING);

		em = new ElementStore();
		BPUtils.em = em;
		JSONObject emobjs = JSONGameLoader
				.getJSON(GameProperties.ELEMENT_MANAGER);
		em.importFromJSON(emobjs);

		Bench.end(BenchConsts.EV_IMPORT_EM);
	}

	protected void startWorldEntity() {
		Log.info(">> startWorldEntity()");
		Bench.start(BenchConsts.EV_START_WORLD, BenchConsts.CAT_INITIALIZING);

		IGameEntity entity = entMan
				.getEntityWithComponent(GameComps.COMP_WORLD);

		if (entity == null) {
			Log.fatal("No entity with world component was found!");
		}
		world = entity.getElement();

		if (this.game == null) {
			Log.fatalIfRunning("Game is null!");
			return;
		}
		this.game.setGameEntity(entity);

		entMan.setReuseManager((ReuseManagerComponent) entity
				.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER));

		ElementManagerComponent emC = (ElementManagerComponent) entMan
				.addIfNotExistsComponent(GameComps.COMP_ELEMENT_MANAGER, entity);
		emC.init(em);

		addRootNodeComponent(entity);

		GameConstsComponent gameConstsC = (GameConstsComponent) entity
				.getComponentWithIdentifier(GameComps.COMP_GAME_CONSTS);
		if (gameConstsC == null) {
			Log.fatal("GameConstsComponent is null!!!");
		}

		initCamera();

		Bench.start(BenchConsts.EV_START_WORLD + "-initNifty",
				BenchConsts.CAT_INITIALIZING);
		initNifty(gameConstsC, entity);
		Bench.end(BenchConsts.EV_START_WORLD + "-initNifty");

		Log.info(">> startWorldEntity() :: end");
		Bench.end(BenchConsts.EV_START_WORLD);
	}

	private void initCamera() {
		IGameEntity entCam = entMan.getEntityWithComponent(GameComps.COMP_CAM);
		CamComponent camC = null;
		if (entCam == null) {
			// Log.fatal("No entity with camera attached!");
			entCam = game.getGameEntity();
			camC = (CamComponent) entMan.addComponent(GameComps.COMP_CAM,
					entCam);
			IGameElement geCam = em.pickFinal(GameComps.COMP_CAM);
			if (geCam == null) {
				Log.fatal("No Camera Element Found!");
			}
			camC.loadFromElement(geCam);

		} else {
			camC = (CamComponent) entCam
					.getComponentWithIdentifier(GameComps.COMP_CAM);
		}
		camC.cam = cam;
	}

	protected void addRootNodeComponent(IGameEntity entity) {
		RenderComponent renderC = ECS.getRenderComponent(entity);
		if (renderC == null) {
			renderC = (RenderComponent) factory
					.createComponentFromClass(GameComps.COMP_RENDER);
			entMan.addComponent(renderC, entity);
		}
		renderC.node = rootNode;
		renderC.firstTick = false;
	}

	protected void initNifty(GameConstsComponent gameConstsC,
			IGameEntity gameEntity) {

		String style = gameConstsC.gameConsts
				.getProperty(GameProperties.NIFTY_STYLE);
		String controls = gameConstsC.gameConsts
				.getProperty(GameProperties.NIFTY_CONTROLS);

		JMESnippets snippets = game.getSnippets();
		if (snippets == null) {
			Log.fatalIfRunning("Snippets is null, cant initialize nifty");
			return;
		}
		try {
			Bench.start(BenchConsts.EV_START_WORLD + "-nifty-p3-1",
					BenchConsts.CAT_INITIALIZING);
			snippets.initNifty(style, controls, true);
			FutureManager.requestFuture(GameProperties.SNIPPETS,
					snippets.loadNifty);
			snippets.hasFuture = true;
			Bench.end(BenchConsts.EV_START_WORLD + "-nifty-p3-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		try {
			checkNiftyLoader();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		entMan.update(tpf);

		updateSoundLocation();
	}

	protected void updateSoundLocation() {
		// for 3d sound
		if (cam == null) {
			return;
		}
		Listener listener = game.getListener();
		if (listener == null) {
			return;
		}
		listener.setLocation(cam.getLocation());
		listener.setRotation(cam.getRotation());
	}

	private void checkNiftyLoader() throws InterruptedException,
			ExecutionException {
		if (snippets.hasFuture) {
			if (FutureManager.isDone(GameProperties.SNIPPETS)) {
				Nifty nifty = (Nifty) FutureManager
						.retrieveFuture(GameProperties.SNIPPETS);
				IGameEntity gameEntity = game.getGameEntity();
				NiftyComponent niftyComp = (NiftyComponent) gameEntity
						.getComponentWithIdentifier(GameComps.COMP_NIFTY);
				if (niftyComp == null) {
					niftyComp = (NiftyComponent) entMan.addComponent(
							GameComps.COMP_NIFTY, gameEntity);
					niftyComp.loadFromElement(em
							.pickFinal(GameComps.COMP_NIFTY));
				}
				niftyComp.loaded = true;
				niftyComp.initNifty(nifty);
				nifty.setDebugOptionPanelColors(CRJavaUtils.NIFTY_DEBUG);
				snippets.hasFuture = false;
			}
		}
	}

	public void initFactory() {
		Log.info("Init Factory...");
		// Bench: Negligivel
		factory = new ProductFactory(em, entMan, game.getAssetManager());

		JSONObject jsonFact = JSONGameLoader.getJSON(GameProperties.FACTORY);
		factory.importFromJSON(jsonFact);
		// /Bench

		// Bench: +5s>1.5s
		Bench.start(BenchConsts.EV_IMPORT_ENTMAN, BenchConsts.CAT_INITIALIZING);
		JSONObject jsonEntMan = JSONGameLoader
				.getJSON(GameProperties.ENTITY_MANAGER);
		entMan.importFromJSON(jsonEntMan);
		entMan.cleanup();
		Bench.end(BenchConsts.EV_IMPORT_ENTMAN);
	}

	private void addState(IGameState state) {
		if (state != null) {
			states.add(state);
			if (stateManager == null) {
				Log.errorIfRunning("StateManager is null!");
				return;
			}
			stateManager.attach((AppState) state);
		}
	}

	@Override
	public void loadGameStates() {
		if (world != null) {
			List<IGameElement> lista = world
					.getObjectList(GameConsts.IDENT_STATES);
			for (IGameElement elState : lista) {
				IGameState state = factory.createStateFrom(elState);
				addState(state);
			}
		}

	}

	@Override
	public void loadGameSystems() {
		Bench.start(BenchConsts.EV_LOAD_SYSTEMS);
		if (world != null) {
			List<IGameElement> lista = world
					.getObjectList(GameConsts.IDENT_SYSTEM);
			for (IGameElement elSystem : lista) {
				addSystem(elSystem);
			}
		}
		Bench.end(BenchConsts.EV_LOAD_SYSTEMS);

	}

	private void addSystem(IGameElement elSystem) {
		if (!systemIsRunnable(elSystem)) {
			return;
		}
		IGameSystem system = factory.createSystemFrom(elSystem);
		if (system != null) {
			// initSystem chama o entMan.addSystem
			system.initSystem(entMan, this.game);
		}
	}

	private boolean systemIsRunnable(IGameElement elSystem) {
		String target = elSystem.getParamAsText(Extras.LIST_DOMAIN,
				GameProperties.TARGET);
		if (CRJavaUtils.isRelease()) {
			return target.contains(GameConsts.TARGET_RELEASE);
		} else {
			return target.contains(GameConsts.TARGET_DEV);
		}
	}

	public IGameFactory getFactory() {
		return factory;
	}

	public IManageElements getElementManager() {
		return em;
	}

	public ArrayList<IGameState> getStates() {
		return states;
	}

	@Override
	public IGameElement getWorldElement() {
		return world;
	}

	public EntityManager getEntityManager() {
		return entMan;
	}

	@Override
	public void resetComponents() {
		internalEntity.resetComponents();
	}

	public IGameElement getMacroDefs() {
		return macroDefs;
	}

	@Override
	public JSONObject exportToJSON() {
		Log.throwUnsupported("This state cant export!");
		return null;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		Log.throwUnsupported("importing isnt done like this...");
	}

	@Override
	public int size() {
		return internalEntity.size();
	}

	@Override
	public boolean writeState() {
		Log.throwUnsupported("This state cant save!");
		return false;
	}

}
