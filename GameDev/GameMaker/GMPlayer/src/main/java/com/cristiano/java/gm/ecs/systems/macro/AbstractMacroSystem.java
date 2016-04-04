package com.cristiano.java.gm.ecs.systems.macro;

import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.macro.GameOppositionComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitResourcesComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public abstract class AbstractMacroSystem extends JMEAbstractSystem {

	public AbstractMacroSystem(String compRequired) {
		super(compRequired);
	}

	protected void applyResourcesFromScopeAbove(IGameEntity ent, IGameEntity entDest, int scopeType) {
		// Log.debug("Getting resources from '"+ent+" and applying to:"+entDest+" Scope:"+scopeType);
		UnitResourcesComponent res = getUnitResourcesComponent();
		List<ResourceComponent> gameResource = res.getResourcesWithScope(scopeType);
		for (ResourceComponent resource : gameResource) {
			
			applyResource(ent, entDest, scopeType,  resource);
		}
	}

	private void applyResource(IGameEntity ent, IGameEntity entDest, int scopeType,  IGameComponent resource) {
		IGameComponent compResource = resource.clonaComponent();
		if (compResource == null) {
			Log.error("Resource null (not present in the master entity: '" + ent + "' with scope:" + scopeType + "):" + compResource.getIdentifier());
		}
		compResource.addInfo("applyResource to "+entDest);
		if (!checkSpecificResources(ent,(ResourceComponent) compResource)){
			return;
		}
		
		if (!entDest.containsComponent(compResource.getIdentifier())) {
			compResource.addInfo("applyResource==> attaching component:"+ent);
			entDest.attachComponent(compResource);
		}
	}

	private boolean checkSpecificResources(IGameEntity mapEnt, ResourceComponent resource) {
		if (resource.getIdentifier().equals(GameComps.COMP_RESOURCE_LAP)){
			resource.setCurrValue(0);
			int lapGoal = mapEnt.getElement().getPropertyAsInt(GameProperties.LAP_GOAL);
			resource.setMaxValue(lapGoal);
		}
		
		return true;
	}

	protected void generateResources(IGameEntity mapEnt,IGameEntity ent, int scopeType) {
		Log.trace("Generating Resources for '" + ent + " scope:" + scopeType);
		
		GameOppositionComponent oppos=(GameOppositionComponent) mapEnt.getComponentWithIdentifier(GameComps.COMP_GAME_OPPOSITION);
		if (oppos==null){
			Log.fatal("GameOpposition not defined for map.");
			return;
		}
		
		UnitResourcesComponent res = getUnitResourcesComponent();
		List<ResourceComponent> gameResource = res.getResourcesWithScope(scopeType);
		for (ResourceComponent resource : gameResource) {
			if (!oppos.isResourceOnScope(resource.getIdentifier(),scopeType)){
				continue;
			}
			ResourceComponent resC = (ResourceComponent) resource.clonaComponent();
			if (!checkSpecificResources(mapEnt,(ResourceComponent) resC)){
				return;
			}
			resC.addInfo("generateResources... "+ent);
			ent.attachComponent(resC);
		}
	}
	
	protected void generateTeams(IGameEntity map) {
		Log.info("GM: Generating Teams...");
		if (map.containsComponent(GameComps.COMP_TEAM)) {
			Log.warn("world entity already has teams defined...");

			return;
		}
		List<IGameComponent> opponents = map
				.getComponentsWithIdentifier(GameComps.COMP_GAME_OPPOSITION);

		if (opponents.size() != 1) {
			Log.error("No oposition defined (or too many)... count:"
					+ opponents.size());
			return;
		}
		GameOppositionComponent opositionComp = (GameOppositionComponent) opponents
				.get(0);
		Log.debug("GM: Opposition name: "
				+ opositionComp.getElement().getName());

		updateResources(opositionComp.scopeGame, opositionComp.scopeTeam,
				opositionComp.scopeUnit);
		generateResources(map, map, LogicConsts.SCOPE_GAME);
		int numTeams = (int) CRJavaUtils.random(opositionComp.minTeams,
				opositionComp.maxTeams);

		for (int i = 0; i < numTeams; i++) {
			createTeam(map, opositionComp, i, i == 0);
		}

		if (numTeams <= 0) {
			Log.error("Error when generating teams... numTeams:" + numTeams);
		}

	}

	protected void updateResources(String[] scopeGame, String[] scopeTeam,
			String[] scopeUnit) {
		Log.debug("Updating scope of resources...");
		UnitResourcesComponent resources = getUnitResourcesComponent();
		for (ResourceComponent resource : resources.unitResources) {
			updateResource(scopeGame, resource, LogicConsts.SCOPE_GAME);
			updateResource(scopeTeam, resource, LogicConsts.SCOPE_TEAM);
			updateResource(scopeUnit, resource, LogicConsts.SCOPE_UNIT);
		}

	}

	protected void updateResource(String[] scopeGame, ResourceComponent resource,
			int scope) {
		for (String name : scopeGame) {
			if (resource.getIdentifier().equals(name)) {
				resource.setScope(scope);
			}
		}
	}

	protected void createTeam(IGameEntity ent,
			GameOppositionComponent opositionComp, int id,
			boolean teamOfThePlayer) {
		TeamComponent team = (TeamComponent) entMan.addComponent(
				GameComps.COMP_TEAM, ent);
		team.idTeam = id;
		team.isPlayerTeam = teamOfThePlayer;
		team.sameTeamRelation = opositionComp.sameTeamRelation;
		team.maxUnits = opositionComp.maxUnitsPerTeam;
		team.minUnits = opositionComp.minUnitsPerTeam;
		team.initialUnits = opositionComp.initialUnitsPerTeam;
		
		team.countDownTimer = opositionComp.countDownTimer;
		team.timeToStart = opositionComp.countDownTimer;
		team.startingPosition = opositionComp.startingPosition;

		applyResourcesFromScopeAbove(ent, team, LogicConsts.SCOPE_GAME);
		generateResources(ent, team, LogicConsts.SCOPE_TEAM);

		if (teamOfThePlayer) {
			if (opositionComp.playerOnlyTeam) {
				team.maxUnits = 1;
			}
		}
		
		retrieveSideInformation(team,teamOfThePlayer?opositionComp.playerSide:opositionComp.enemySide);
		
		team.maxLives = team.livesLeft;
	}

	protected void retrieveSideInformation(TeamComponent team,
			IGameElement elSide) {
		team.livesLeft = elSide.getPropertyAsInt(GameProperties.LIVES);
		team.hasRespawn = elSide.getPropertyAsBoolean(GameProperties.RESPAWN);
		IGameElement elUnitRole = elSide.getPropertyAsGE(GameProperties.UNIT_ROLE);
		team.roleIdentifier=elUnitRole.getIdentifier();
		
		int minVariations=elSide.getPropertyAsInt(GameProperties.MIN_VARIATIONS);
		iniciaBestiaryLibrary();
		libComp.addStorage(elUnitRole,minVariations);
	}
	
	protected void putTeamsOnCountdownStage(IGameEntity ent) {
		putTeamsOnStage(ent, TeamComponent.COUNTDOWN);
	}

	protected void putTeamsOnStage(IGameEntity ent, String stage) {
		Log.info("GM: Setting teams on " + stage + "...");
		List<IGameComponent> teams = ent.getComponentsWithIdentifier(GameComps.COMP_TEAM);
		if (teams.isEmpty()){
			Log.error("No Team Found!");
		}
		for (IGameComponent comp : teams) {
			TeamComponent team = (TeamComponent) comp;
			team.setStage(stage);
		}
	}
	
	protected void startMapSolver(MapComponent comp, IGameEntity ent) {
		if (comp.previouslyLoaded) {
			return;
		}
		if (CRJavaUtils.isRelease()) {
			return;
		}

		IGameElement solveMap = getSolveMap(ent, comp);
		String solverClass = solveMap.getProperty(Extras.PROPERTY_INIT);
		Log.debug("Starting map solver: " + solverClass);
		ISolveMap solver = (ISolveMap) CRJavaUtils.instanciaClasse(solverClass);
		solver.initVars(entMan);
		
		GameGenreComponent genre=getGameGenre();
		
		solver.startFrom(comp, getMapLocationComponent(), solveMap,genre);

		comp.mapSolver = solver;
	}

	protected IGameElement getSolveMap(IGameEntity ent, MapComponent comp) {
		MapLocationComponent mapLocal = getMapLocationComponent();

		String tag = comp.populateMap + " " + mapLocal.mapSolverFilter;
		String solverTag = StringHelper.removeChaves(tag);
		IGameElement ge = getElementManager().pickFinal(solverTag,
				comp.getElement());
		if (ge == null) {
			Log.fatal("No solver found for '" + tag + "'");
		}
		return ge;
	}

}
