package com.cristiano.java.gm.visualizadores;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.state.IIntegrationGameState;
import com.cristiano.java.gm.states.BPPlayerState;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.jme3.visualizadores.CRSimpleGame;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

/*
 * Essa camada foca no produto final, considerando que os elementos foram carregados
 * */
public class GMPlayer extends CRSimpleGame implements IFinalGame {

	protected IIntegrationGameState integr;
	protected String rootTag = "macroDefinition release leaf";
	IGameEntity gameEntity = null;
	//protected boolean IS_RELEASE = true;

	public static void main(String[] args) {
		//CRJavaUtils.NIFTY_DEBUG=true;
		GMPlayer app = new GMPlayer(true);
		app.start();
	}

	public GMPlayer(boolean isRelease) {
		super();
		CRJavaUtils.IS_RELEASE_INTERNAL = isRelease;
		Bench.start(BenchConsts.EV_INITIALIZATION, BenchConsts.CAT_INITIALIZING);
	}
	
	public GMPlayer() {
		this(true);
	}

	public void startHeadless() {
		super.startHeadless();
		try {
			while (getGameEntity() == null) {
				Thread.sleep(1000);
				if (Log.showStopper){
					break;
				}
			}
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void simpleInitApp() {
		super.simpleInitApp();

		inputManager.setCursorVisible(true);
		flyCam.setMoveSpeed(250f);

		initObjects();
		cam.setFrustumFar(8000);

		setDisplayFps(true);
		setDisplayStatView(false);

		// disableFlyCam();
		flyCam.setEnabled(false);
	}

	public IGameEntity getGameEntity() {
		return gameEntity;
	}

	public void initObjects() {
		Log.debug("Loading JSON from file...");
		Bench.start(BenchConsts.EV_LOAD_JSON_FILE);
		initBlueprintIntegration();
		Bench.end(BenchConsts.EV_LOAD_JSON_FILE);

		Bench.start("Init JMESnippets",BenchConsts.CAT_INITIALIZING);
		Log.debug("Initialising Snippets and tonegod... release? "
				+ CRJavaUtils.isRelease());
		this.snippets = new JMESnippets(this, rootNode, cam, inputManager,
				assetManager);
		Bench.end("Init JMESnippets");
	}

	@Override
	public void initBlueprintIntegration() {
		this.integr = new BPPlayerState();
		stateManager.attach(integr);
	}

	public void setGameEntity(IGameEntity entity) {
		gameEntity = entity;
	}

	public IIntegrationGameState getIntegrationState() {
		return integr;
	}

	@Override
	public String getMacroDefinitionTag() {
		return rootTag;
	}

}
