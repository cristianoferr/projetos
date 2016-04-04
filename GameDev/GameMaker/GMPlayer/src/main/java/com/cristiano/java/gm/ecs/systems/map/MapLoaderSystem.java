package com.cristiano.java.gm.ecs.systems.map;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.visual.SkyBoxComponent;
import com.cristiano.java.gm.ecs.systems.macro.AbstractMacroSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class MapLoaderSystem extends AbstractMacroSystem {

	public MapLoaderSystem() {
		super(GameComps.COMP_MAP);
	}

	@Override
	// ent is a mapWorldComponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {

		//boolean run=isGameRunning();
		MapComponent comp = (MapComponent) component;
		String stage=comp.getStage();
		if (stage.equals(MapComponent.WAITING)) {
			if (!isPhysicsLoaded()) {
				return;
			}
		}

		if (stage.equals(MapComponent.STARTING)) {
			Log.info("Map: Starting map...");
			startTerrain(comp);
			comp.nextStage();
			return;
		}

		if (stage.equals(MapComponent.LOAD_SKYBOX)) {
			Log.info("Map: Loading Skybox...");
			if (comp.elSkybox == null) {
				comp.elSkybox = comp.getElement().getPropertyAsGE(
						GameProperties.SKYBOX);
				loadSkyBox(ent, comp);
			}
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.LOAD_OBJECTIVES)) {
			Log.info("Map: Loading objectives...");
			loadMapObjectives(ent, comp);
			stage=comp.nextStage();
		}
		if (stage.equals(MapComponent.LOAD_OPPOSITION)) {
			Log.info("Map: Loading opposition definitions...");
			loadMapOpponents(ent, comp);
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.ROOM_CHECK)) {
			/*if (comp.containsComponent(GameComps.COMP_ROOM)) {
				comp.setStage(MapComponent.ROOM_SOLVED);
			} else {*/
				stage=comp.nextStage();
		//}
		}

		if (stage.equals(MapComponent.MAP_SOLVING)) {
			stepGeneration(comp, ent);
			return;
		}

		if (stage.equals(MapComponent.ROOM_SOLVING)) {
			if (checkRoomsSolved(comp)) {
				Log.info("Map: Rooms Solved...");
				stage=comp.nextStage();
			}
		}

		if (stage.equals(MapComponent.ROOM_SOLVED)) {
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.ROAD_SOLVING)) {
			if (checkRoadsSolved(comp)) {
				Log.info("Map: Roads Solved...");
				comp.nextStage();
			}
			return;
		}

		if (stage.equals(MapComponent.ROAD_SOLVED)) {
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.BUBBLE_SOLVING)) {
			if (checkBubblesSolved(comp)) {
				stage=comp.nextStage();
			} else {
				return;
			}
		}

		if (stage.equals(MapComponent.BUBBLE_SOLVED)) {
			stage=comp.nextStage();
			if (CRJavaUtils.IS_EDITOR && CRJavaUtils.IS_PHYSICS_ON){
				getPhysicsSpace().activate();	
			}
		}

		if (stage.equals(MapComponent.TERRAIN_GENERATING)) {
			checkTerrain(comp);
		}
		if (stage.equals(MapComponent.TERRAIN_FINISHING)) {
			checkTerrain(comp);
		}

		// room placing: gera a posição das rooms, solving: pega essas rooms
		// e transforma em elementos gráficos e/ou aplica a posição no
		// terreno.
		if (stage.equals(MapComponent.TERRAIN_GENERATED)) {
			Log.info("Map: Terrain Generated...");
			startMapSolver(comp, ent);
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.TERRAIN_FINISHED)) {
			Log.info("Map: Terrain Finished...");
			stage=comp.nextStage();
		}

		if (stage.equals(MapComponent.LOADED)) {
			Log.info("Map Loaded.");
			comp.firstTick = false;
			stage=comp.nextStage();
		}
		
		if (stage.equals(MapComponent.GENERATING_TEAMS)) {
			generateTeams(comp);
			putTeamsOnStage(comp, TeamComponent.START);
			stage = comp.nextStage();
		}
		
		if (stage.equals(MapComponent.WAITING_TEAMS)) {
			boolean areTeamsOk = areTeamsOk(comp);
			if (areTeamsOk) {
				stage = comp.nextStage();
			} else {
				return;
			}
		}
		if (stage.equals(MapComponent.TEAMS_READY)) {
			putTeamsOnCountdownStage(comp);
			Bench.end(BenchConsts.EV_FROM_INIT_TO_RUNNING);
			stage = comp.nextStage();
		}

		if (stage.equals(MapComponent.COUNTDOWN_TIMER)) {
			if (areTeamsRunning(ent)) {
				stage = comp.nextStage();
			}
		}
	}
	

	private boolean areTeamsRunning(IGameEntity ent) {
		List<IGameComponent> teams = ent.getComponentsWithIdentifier(GameComps.COMP_TEAM);
		for (IGameComponent comp : teams) {
			TeamComponent team = (TeamComponent) comp;
			if (!team.isOnStage(TeamComponent.RUNNING)) {
				return false;
			}
		}

		Log.info("GM: Teams running...");
		return true;
	}

	private boolean areTeamsOk(IGameEntity ent) {
		List<IGameComponent> teams = ent.getComponentsWithIdentifier(GameComps.COMP_TEAM);
		for (IGameComponent comp : teams) {
			TeamComponent team = (TeamComponent) comp;
			if (!team.isOnStage(TeamComponent.READY_TO_RUN)) {
				return false;
			}
		}
		if (teams.isEmpty()) {
			Log.error("No team defined.");
		}
		Log.info("GM: Teams good to go...");
		return true;
	}

	private void checkTerrain(MapComponent comp) {
		TerrainComponent terrain = ECS.getTerrainFrom(comp);
		if (terrain==null){
			Log.info("No terrain on this map.");
			comp.nextStage();
			return;
		}
		if (comp.previouslyLoaded) {
			if (!terrain.isFirstTick()){
				comp.nextStage();
			}
			return;
		}
	}

	private void loadSkyBox(IGameEntity ent, MapComponent comp) {
		if (comp.containsComponent(GameComps.COMP_SKYBOX)) {
			Log.info("Map already have skybox... skipping");
			return;
		}
		SkyBoxComponent skyBox = (SkyBoxComponent) entMan.addComponent(
				GameComps.COMP_SKYBOX, comp);
		if (comp.elSkybox == null) {
			Log.fatal("Map has no skyBox attached.");
		}
		skyBox.elSkyBox = comp.elSkybox;
	}

	private void loadMapOpponents(IGameEntity ent, MapComponent comp) {
		if (comp.elMapOpposition == null) {
			Log.error("No mapOpposition defined!!");
			return;
		}
		if (comp.containsComponent(GameComps.COMP_GAME_OPPOSITION)) {
			Log.info("Map already have gameOpposition... skipping");
			return;
		}
		IGameComponent obj = entMan.addComponent(
				GameComps.COMP_GAME_OPPOSITION, comp);
		obj.loadFromElement(comp.elMapOpposition);
	}

	private void loadMapObjectives(IGameEntity ent, MapComponent comp) {
		if (comp.containsComponent(GameComps.COMP_GAME_OBJECTIVE)) {
			Log.info("Map already have objectives... skipping");
			return;
		}
		
		for (IGameElement ge : comp.elMapObjectives) {
			IGameComponent obj = entMan.addComponent(
					GameComps.COMP_GAME_OBJECTIVE, comp);
			obj.loadFromElement(ge);
		}
	}

	

	private boolean checkBubblesSolved(MapComponent comp) {
		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_FLATTEN_TERRAIN);
		if (ents.size() > 0) {
			return false;
		}
		ents = entMan.getEntitiesWithComponent(GameComps.COMP_BUBBLE);
		if (ents.size() > 0) {
			return false;
		}
		return true;
	}

	private boolean checkRoomsSolved(MapComponent comp) {
		List<IGameComponent> rooms = comp.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		for (IGameComponent room : rooms) {
			if (room.isFirstTick()) {
				return false;
			}
		}
		return true;
	}

	private boolean checkRoadsSolved(MapComponent comp) {
		if (CRJavaUtils.isRelease()){
			return true;
		}
		List<IGameComponent> rooms = comp.mapSolver.getRoads();
		if (rooms.isEmpty()) {
			Log.warn("No Roads found!");
		}
		for (IGameComponent room : rooms) {
			if (room.isFirstTick()) {
				return false;
			}
		}
		return true;
	}

	private void stepGeneration(MapComponent comp, IGameEntity ent) {

		boolean finished = false;
		if (CRJavaUtils.isRelease()) {
			finished = true;
			;
		} else {
			finished =comp.mapSolver.hasCompleted();
		}
		if (finished) {
			comp.nextStage();
		}
	}

	private boolean startTerrain(MapComponent comp) {
		// there is no terrain...
		if (comp.elTerrain == null) {
			Log.warn("Map: No terrain to generate...");
			return false;
		}
		loadTerrainElement(comp);
		return true;
	}

	private void loadTerrainElement(MapComponent comp) {
		if (comp.containsComponent(GameComps.COMP_TERRAIN)) {
			Log.info("Map already have terrain... skipping");
			comp.previouslyLoaded = true;
			return;
		}
		TerrainComponent terrain = (TerrainComponent) entMan.addComponent(
				GameComps.COMP_TERRAIN, comp);
		terrain.loadFromElement(comp.elTerrain);
	}

	

}
