package com.cristiano.java.gm.ecs.systems.macro;

import java.util.List;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.macro.QueryBestiaryComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIControlComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamMemberComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.UnitPositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class TeamSystem extends AbstractMacroSystem {
	int gridPos = 0;

	private UIPanelComponent countDownPanel;

	private UIControlComponent labelGO;

	public TeamSystem() {
		super(GameComps.COMP_TEAM);
	}

	@Override
	// ent is the mapComponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		TeamComponent teamC = (TeamComponent) component;
		if (teamC.isFirstTick()) {
			teamC.firstTick = false;
			if (teamC.teamMember == null) {
				initTeamMember(ent, teamC);
			}
		}
		
		String stage=teamC.getStage();
		

		if (stage.equals(TeamComponent.START)) {
			getElementManager();
			JMEAbstractSystem.playerEnt = null;
			stage=teamC.nextStage();
		}
		if (stage.equals(TeamComponent.INITIALISING)) {
			initializeTeam(ent, teamC);
			stage=teamC.nextStage();
		}
		if (stage.equals(TeamComponent.WAITING_4_PLACEMENT)) {
			// initializeTeam(ent, teamC);

			updateEntitiesHeight(ent, teamC);
			JMEAbstractSystem.playerEnt = null;
			teamC.nextStage();
			teamC.timeToStart = teamC.countDownTimer;
			countDownPanel = (UIPanelComponent) entMan
					.getEntityWithTag(NiftyComponent.TIME_COUNTDOWN_PANEL_TAG);
			NiftyComponent.changeVisibility(countDownPanel, true);
			labelGO = (UIControlComponent) countDownPanel
					.getComponentWithIdentifier(GameComps.COMP_UI_CONTROL);
			initInternational();
			changePriorityRealtime();
			
			NiftyComponent nifty = ECS.getNifty(game.getGameEntity());
			nifty.endSplashScreen();
			
			GameState.currentStage=GameState.STG_RUNNING;
			GameState.hasRunBegun=true;
			return;
		}

		if (stage.equals(TeamComponent.COUNTDOWN)) {
			teamC.timeToStart -= tpf;
			if (teamC.timeToStart > 0) {
				labelGO.setText(Integer.toString(((int) teamC.timeToStart)));
				return;
			}
			if (teamC.timeToStart < 0) {
				stage=teamC.nextStage();
				labelGO.setText(inter.translate(labelGO.getElement().getProperty(GameProperties.LABEL_ACTIVE)));

				resetPriority();
				Log.info("TEAM:Countdown finished!");
				updateEntitiesHeight(ent, teamC);
				teamC.timeToStart = 0;
			} 
		}

		if (stage.equals(TeamComponent.RUNNING)) {
			showGo(tpf, teamC);
		}
	}

	private void updateEntitiesHeight(IGameEntity entity, TeamComponent teamC) {
		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(teamC.teamMember);
		ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_PLAYER);
		for (IGameEntity ent : ents) {
			PositionComponent pos = (PositionComponent) ent
					.getComponentWithIdentifier(GameComps.COMP_POSITION);
			if (pos != null) {
				DimensionComponent dim = (DimensionComponent) ent
						.getComponentWithIdentifier(GameComps.COMP_DIMENSION);
				if (dim != null) {
					pos.setPos(pos.getPos().add(0, dim.dimension.y / 2, 0));
				}
			}
		}

	}

	private void showGo(float tpf, TeamComponent teamC) {
		if (teamC.timeToStart == -100) {
			return;
		}
		if (teamC.timeToStart < -2) {
			NiftyComponent.changeVisibility(countDownPanel, false);
			
			changePriorityAverage();
			teamC.timeToStart=-100;
		} else {
			teamC.timeToStart -= tpf;
		}

	}

	private void initializeTeam(IGameEntity ent, TeamComponent teamC) {
		Log.info("InitializeTeam...");
		for (int i = 0; i < teamC.initialUnits; i++) {
			boolean isPlayer = i == 0 && teamC.isPlayerTeam;
			orderEntity(ent, teamC, isPlayer, teamC.initialUnits);
		}
	}

	private void orderEntity(IGameEntity ent, TeamComponent teamC,
			boolean isPlayer, int initialUnits) {
		if (!teamCanSpawn(teamC)) {
			return;
		}
		@SuppressWarnings("unused")
		QueryBestiaryComponent query;
		if (isPlayer) {
			query=spawnPlayer(ent, teamC, initialUnits);
		} else {
			query=spawnNPC(ent, teamC, initialUnits);
		}
		
		
		teamC.unitSpawned();

	}

	private boolean teamCanSpawn(TeamComponent teamC) {
		// if its set as -1 then lives are infinite
		if (teamC.livesLeft < 0) {
			return true;
		}
		return (teamC.livesLeft > 0);
	}

	

	private void initTeamMember(IGameEntity ent, TeamComponent teamC) {
		teamC.teamMember = (TeamMemberComponent) entMan
				.spawnComponent(GameComps.COMP_TEAM_MEMBER);
		teamC.addInfo("initTeamMember: TeamSystem");
		teamC.teamMember.masterTeam = teamC;
		teamC.teamMember.idTeam = teamC.idTeam;
	}

	private QueryBestiaryComponent spawnNPC(IGameEntity ent, TeamComponent teamC, int initialUnits) {
		MapComponent mapC = getMap();
		RoomComponent roomC = mapC.getStartingRoom();
		GameGenreComponent genreC = getGameGenre();
		float budget = genreC.npcBudget;

		Vector3f pt = getInitialPosition(teamC, roomC, mapC.getNextRoom(roomC),
				initialUnits);

		QueryBestiaryComponent queryNPC = orderNewEntity(ent, budget, pt, teamC);
		entMan.addDefaultComponent(GameComps.COMP_AI, queryNPC, em);
		return queryNPC;

	}

	public Vector3f getInitialPosition(TeamComponent teamC,
			RoomComponent roomC, RoomComponent nextRoom, int initialUnits) {
		Vector3f pt = null;
		if (teamC.startingPosition.getName().equals(
				LogicConsts.STARTING_POSITION_GRID)) {
			float angle = CRMathUtils.calcDegreesXZ(roomC.getPosition(),
					nextRoom.getPosition()) - 90;
			int cols = teamC.startingPosition
					.getPropertyAsInt(GameProperties.COLS);
			int depth = teamC.startingPosition
					.getPropertyAsInt(GameProperties.DEPTH);
			int width = teamC.startingPosition
					.getPropertyAsInt(GameProperties.WIDTH);
			pt = roomC.getPosition().subtract(
					width * cols / 2f - width / 2,
					0,
					+depth * (initialUnits - (gridPos - (gridPos % cols)))
							/ cols);
			// depth=depth/cols;
			float col = gridPos % cols * width;
			float row = gridPos / cols * depth;
			pt.addLocal(col, 0, row);
			pt = JMEUtils.rotatePointXZ(roomC.getPosition(), pt, angle,
					pt.distance(roomC.getPosition()));

			// ComponentRecipes.debugPoint(snippets, pt, ColorRGBA.Green, 1,1);
			gridPos++;

		} else {
			pt = roomC.getRandomPointInsideRoom();
		}
		pt.y = roomC.getPosition().y+2;
		
		return pt;
	}

	public QueryBestiaryComponent orderNewEntity(IGameEntity ent, float budget,
			Vector3f pt, TeamComponent teamC) {
		QueryBestiaryComponent unitQuery = (QueryBestiaryComponent) entMan
				.addComponent(GameComps.COMP_BESTIARY_QUERY, ent);
		unitQuery.entityType = LogicConsts.ENTITY_DYNAMIC;

		initUnitResources(unitQuery, ent, teamC);
		initEntityPosition(pt, unitQuery);
		unitQuery.attachComponent(teamC.teamMember);
		unitQuery.roleIdentifier=teamC.roleIdentifier;
		return unitQuery;
	}

	private void initEntityPosition(Vector3f pt,
			QueryBestiaryComponent unitQuery) {
		PositionComponent posC = (PositionComponent) entMan.addComponent(
				GameComps.COMP_POSITION, unitQuery);
		Log.trace("Placing Entity at " + pt);
		posC.setPos(pt);
	}

	private void initUnitResources(QueryBestiaryComponent queryPlayer,
			IGameEntity ent, TeamComponent teamC) {
		applyResourcesFromScopeAbove(ent, queryPlayer, LogicConsts.SCOPE_GAME);
		applyResourcesFromScopeAbove(teamC, queryPlayer, LogicConsts.SCOPE_TEAM);
		generateResources(ent, queryPlayer, LogicConsts.SCOPE_UNIT);

		UnitPositionComponent posC = (UnitPositionComponent) queryPlayer
				.getComponentWithIdentifier(GameComps.COMP_RESOURCE_UNIT_POSITION);
		if (posC != null) {
			posC.setMaxValue(teamC.initialUnits);
		}
	}

	private QueryBestiaryComponent spawnPlayer(IGameEntity ent, TeamComponent teamC,
			int initialUnits) {
		Log.info("Spawning player..." + teamC);
		MapComponent mapC = getMap();
		RoomComponent roomC = mapC.getStartingRoom();
		GameGenreComponent genreC = getGameGenre();

		float budget = genreC.playerBudget;
		Vector3f pt = getInitialPosition(teamC, roomC, mapC.getNextRoom(roomC),
				initialUnits);
		QueryBestiaryComponent queryPlayer = orderNewEntity(ent, budget, pt,
				teamC);
		addPlayerComponent(queryPlayer);
		return queryPlayer;
	}

	private void addPlayerComponent(QueryBestiaryComponent queryPlayer) {
		entMan.removeComponent(GameComps.COMP_PLAYER);
		PlayerComponent playerComp = (PlayerComponent) entMan.addComponent(
				GameComps.COMP_PLAYER, queryPlayer);
		IGameElement elPlayer = getElementManager().pickFinal(
				GameComps.COMP_PLAYER);
		playerComp.loadFromElement(elPlayer);

	}

}
