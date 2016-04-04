package com.cristiano.java.gm.states;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.factory.BuilderFactory;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.proc.nameGen.IGenerateNames;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class BPBuilderState extends BPPlayerState {

	public BPBuilderState() {
		super(false);
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		game.getRootNode().attachChild(
				snippets.generateBox(ColorRGBA.Green,
						new Vector3f(1f, 100f, 1f), Vector3f.ZERO));
	}

	@Override
	protected void initElementManager() {
		if (em != null) {
			return;
		}
		Log.info("Loading blueprints... release? " + CRJavaUtils.isRelease());

		ElementManager em = new ElementManager();
		try {
			em.loadBlueprintsFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.info("EM:Loading Completed...");
		String worldTag = initMacroDefinititions(em);

		Log.info("Creating world element... release? "
				+ CRJavaUtils.isRelease());
		IGameElement world = em.createFinalElementFromTag(worldTag);
		if (world == null) {
			Log.fatal("World is null (and empty)... ");
		}
		Log.info("World element created... release? " + CRJavaUtils.isRelease());
		initElementManager(em, world);
	}

	@Override
	public void initFactory() {
		factory = new BuilderFactory(em, entMan, game.getAssetManager());
	}

	@Override
	protected void startWorldEntity() {
		// IGameEntity entity =
		// entMan.getEntityWithComponent(GameComps.COMP_WORLD);
		IGameEntity entity = factory.createEntityFrom(world);

		if (this.game == null) {
			Log.fatal("No world entity was defined...");
			return;
		}
		configureGameName(entity);

		this.game.setGameEntity(entity);
		entMan.setReuseManager((ReuseManagerComponent) entity
				.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER));

		ElementManagerComponent emC = (ElementManagerComponent) entMan
				.addComponent(GameComps.COMP_ELEMENT_MANAGER, entity);
		emC.init(em);

		addRootNodeComponent(entity);

		GameConstsComponent gameConstsC = (GameConstsComponent) entity
				.getComponentWithIdentifier(GameComps.COMP_GAME_CONSTS);

		if (gameConstsC == null) {
			Log.fatal("GameConstsComponent is null!!!");
		}
		CamComponent camC = (CamComponent) entity
				.getComponentWithIdentifier(GameComps.COMP_CAM);
		camC.cam = cam;
		initNifty(gameConstsC, entity);
	}

	private void configureGameName(IGameEntity entity) {
		InternationalComponent inter = ECS.getInternationalComponent(entity);
		if (inter == null) {
			Log.fatalIfRunning("International Component is null!");
			return;
		}
		inter.addText(GameProperties.GAME_TITLE,
				macroDefs.getProperty(GameProperties.GAME_TITLE));
	}

	protected String initMacroDefinititions(ElementManager em) {
		Log.info("InitMacroDefinitions...");
		this.macroDefs = em.createFinalElementFromTag(game
				.getMacroDefinitionTag());
		if (this.macroDefs == null) {
			Log.fatal("MacroDefinitions is null... ");
			return null;
		}

		generateGameName();

		Log.info("Loading genre element...");

		IGameElement gameGenre = this.macroDefs
				.getPropertyAsGE(GameProperties.GAME_GENRE);
		if (gameGenre == null) {
			Log.fatal("GameGenre is null!! ");
			return null;
		}
		Log.info("Game Genre Loaded.");
		String worldTag = gameGenre.getProperty(GameProperties.WORLD_TAG);
		return worldTag;
	}

	private void generateGameName() {
		Log.info("Generating name...");
		IGameElement elNameGen = macroDefs
				.getPropertyAsGE(GameProperties.NAME_GEN);
		String classInit = elNameGen.getProperty(Extras.PROPERTY_INIT);
		int minWords = elNameGen.getPropertyAsInt(GameProperties.MIN_WORDS);
		int maxWords = elNameGen.getPropertyAsInt(GameProperties.MAX_WORDS);
		String dictionary = elNameGen.getProperty(GameProperties.DICTIONARY);
		String nameCheck = elNameGen.getProperty(GameProperties.NAME_CHECK);
		IGenerateNames nameGen = (IGenerateNames) CRJavaUtils
				.instanciaClasse(classInit);
		nameGen.loadChecks(nameCheck);

		nameGen.initialize(dictionary.split(","), minWords, maxWords);
		String gameName = nameGen.generateName();
		Log.info("Game name generated is:" + gameName);
		macroDefs.setProperty(GameProperties.GAME_TITLE, gameName);

	}

	public void initElementManager(ElementManager em, IGameElement worldElement) {
		this.em = em;
		world = worldElement;

	}

	@Override
	public JSONObject exportToJSON() {
		Log.fatal("This method shouldnt be used.");
		return null;
	}

	public boolean writeState() {
		writeGameProperties();
		JSONObject jsonFactory = factory.exportToJSON();
		JSONObject jsonentMan = entMan.exportToJSON();
		JSONObject jsonWorld = world.exportToJSON();
		JSONObject jsonEM = em.exportToJSON();// em must be the last one
		CRJsonUtils.writeJSON(GMAssets.getFullFilePath(GMAssets
				.getJSONPath(GameProperties.FACTORY)), jsonFactory);
		CRJsonUtils.writeJSON(GMAssets.getFullFilePath(GMAssets
				.getJSONPath(GameProperties.ELEMENT_MANAGER)), jsonEM);
		CRJsonUtils.writeJSON(GMAssets.getFullFilePath(GMAssets
				.getJSONPath(GameProperties.ENTITY_MANAGER)), jsonentMan);
		CRJsonUtils.writeJSON(GMAssets.getFullFilePath(GMAssets
				.getJSONPath(GameProperties.WORLD_ELEMENT)), jsonWorld);
		Log.info("Finished writing state.");
		return true;
	}

	public void writeGameProperties() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(GMAssets.getFullFilePath(GMAssets
					.getPropertiesPath()), "UTF-8");
			writer.println("# This file is automatically generated.");
			String title = "Undefined";
			String strCategory = "Undefined";
			if (macroDefs != null) {
				title = macroDefs.getProperty(GameProperties.GAME_TITLE);
				IGameElement gameGenre = this.macroDefs
						.getPropertyAsGE(GameProperties.GAME_GENRE);
				// gameCategory=
				IGameElement gameCategory = gameGenre
						.getPropertyAsGE(GameProperties.GAME_CATEGORY);
				strCategory = gameCategory.getProperty(Extras.PROPERTY_VALUE);
			} else {
				Log.fatalIfRunning("Macrodefs is null when writing game properties.");
			}

			writer.println("gameTitle=" + title);
			writer.println("gameCategory=" + strCategory);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFactory(IGameFactory factory) {
		this.factory = factory;
	}

}
