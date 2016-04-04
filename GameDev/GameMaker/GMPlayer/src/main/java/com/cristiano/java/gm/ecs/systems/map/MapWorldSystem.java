package com.cristiano.java.gm.ecs.systems.map;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.scene.Node;

/*
 * 
 * 
 */

public class MapWorldSystem extends JMEAbstractSystem {

	public static MapComponent currentMap=null;

	public MapWorldSystem() {
		super(GameComps.COMP_MAP_WORLD);
	}

	@Override
	// TODO: ent=gameEntity
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {

		MapWorldComponent comp = (MapWorldComponent) component;
		String stage = comp.getStage();

		if (stage.equals(MapWorldComponent.RUNNING)) {
			checkRunningMap(ent, comp);
			return;
		}

		if (stage.equals(MapWorldComponent.INITIAL)) {
			Log.info("MapWorld: Initial...");
			NiftyComponent nifty = ECS.getNifty(ent);
			if (nifty == null) {
				return;
			}
			if (!nifty.screensStarted && !CRJavaUtils.IS_EDITOR) {
				return;
			}

			Bench.end(BenchConsts.EV_INITIALIZATION);
			GameState.currentStage=GameState.STG_START;
			stage = comp.nextStage();
		}

		if (stage.equals(MapWorldComponent.WAITING)) {
			Log.info("MapWorld: waiting...");
			NiftyComponent nifty = ECS.getNifty(ent);
			if (nifty.isRunning() || CRJavaUtils.IS_EDITOR) {
				stage = comp.nextStage();
				nifty.gotoSplashScreen();
				Bench.start(BenchConsts.EV_FROM_INIT_TO_RUNNING,
						BenchConsts.CAT_INITIALIZING);
				GameState.currentStage=GameState.STG_LOADING;
			}
		}

		if (stage.equals(MapWorldComponent.NEXT_MAP)) {
			Log.info("MapWorld: next map");
			archiveMap(comp);
			comp.nextMap();
			stage = comp.nextStage();
		}

		if (stage.equals(MapWorldComponent.RESET_MAP)) {
			stage = comp.nextStage();
		}

		if (stage.equals(MapWorldComponent.LOAD_MAP)) {
			Log.info("MapWorld: loading map");
			loadMap(ent, comp);
			stage = comp.nextStage();
		}

		if (stage.equals(MapWorldComponent.MAP_SUCCESS)) {
			Log.info("MapWorld: player has success on the map...");
			sendAction(ent, GameActions.ACTION_GOTO_VICTORY_SCREEN);
			stage = comp.nextStage();
		}
		if (stage.equals(MapWorldComponent.MAP_FAIL)) {
			Log.info("MapWorld: player has failed on the map...");
			sendAction(ent, GameActions.ACTION_GOTO_FAIL_SCREEN);
			stage = comp.nextStage();
		}

	}

	private void checkRunningMap(IGameEntity ent, MapWorldComponent comp) {
		if (currentMap==null){
			return;
		}
		if (currentMap.isCompleted) {
			Log.info("Player has completed current map");
			if (currentMap.playerVictorious) {
				comp.setStage(MapWorldComponent.MAP_SUCCESS);
			} else {
				comp.setStage(MapWorldComponent.MAP_FAIL);
			}
		}

	}

	private void loadMap(IGameEntity ent, MapWorldComponent mapWorldC) {
		MapComponent mapC = null;
		if (CRJavaUtils.isRelease()) {
			mapC = mapWorldC.getMapComponent();
		} else {
			IGameElement elMap = mapWorldC.getMap();

			mapC = (MapComponent) entMan.addComponent(GameComps.COMP_MAP,
					mapWorldC);
			mapC.loadFromElement(elMap);
		}
		mapC.resetComponent();
		mapC.activate();
		mapC.nextStage();

		addRender(ent, mapC);

		currentMap = mapC;

		// mapC.attachComponent(mapWorldC);
	}

	private void addRender(IGameEntity ent, MapComponent mapC) {
		RenderComponent renderC = (RenderComponent) entMan
				.addIfNotExistsComponent(GameComps.COMP_RENDER, mapC);

		RenderComponent worldRender = ECS.getRenderComponent(ent);
		if (renderC.node == null) {
			renderC.node = new Node();
		} else {
			renderC.node.removeFromParent();
		}
		renderC.firstTick = false;
		worldRender.node.attachChild(renderC.node);

	}

	private void archiveMap(MapWorldComponent mapWorldC) {
		IGameComponent map = mapWorldC
				.getComponentWithIdentifier(GameComps.COMP_MAP);
		if (map != null) {
			map.deactivate();
		}
		// mapWorldC.removeComponent(GameComps.COMP_MAP);

	}

}
