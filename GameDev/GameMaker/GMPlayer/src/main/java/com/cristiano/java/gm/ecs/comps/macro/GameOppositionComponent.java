package com.cristiano.java.gm.ecs.comps.macro;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

/*
 * "Opposition" defines how the teams are formed...
 * */
public class GameOppositionComponent extends GameComponent {

	public static final String GAME_OPPOSITION_TAG = "GameOpposition";

	// for testing reference... this shouldnt be used for any decision...
	public static final String TYPE_OPPOSITION_FREEFORALL = "freeforall";
	public static final String TYPE_OPPOSITION_PLAYER_VERSUS = "playerVersus";
	public static final String TYPE_OPPOSITION_MULTI_TEAMS = "multiTeams";

	public int maxUnitsPerTeam;
	public int minUnitsPerTeam;
	public int initialUnitsPerTeam;
	public int maxTeams;
	public int minTeams;
	public float budgetMultiPlayer;
	public float budgetMultiNPC;
	public boolean playerOnlyTeam;
	public int sameTeamRelation;
	public int countDownTimer;

	public String[] scopeGame = null;
	public String[] scopeTeam;
	public String[] scopeUnit;
	public IGameElement startingPosition;
	public IGameElement playerSide;
	public IGameElement enemySide;

	@Override
	public void free() {
		super.free();
		maxUnitsPerTeam = 0;
		minUnitsPerTeam = 0;
		initialUnitsPerTeam = 0;
		maxTeams = 0;
		minTeams = 0;
		budgetMultiPlayer = 0;
		budgetMultiNPC = 0;
		playerOnlyTeam = false;
		sameTeamRelation = 0;
		countDownTimer = 0;
		playerSide = null;
		enemySide = null;

		scopeGame = null;
		scopeTeam = null;
		scopeUnit = null;
		startingPosition = null;

	}

	public GameOppositionComponent() {
		super(GameComps.COMP_GAME_OPPOSITION);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.maxUnitsPerTeam = ge
				.getPropertyAsInt(GameProperties.MAX_UNITS_PER_TEAM);
		this.minUnitsPerTeam = ge
				.getPropertyAsInt(GameProperties.MIN_UNITS_PER_TEAM);
		this.countDownTimer = ge
				.getPropertyAsInt(GameProperties.COUNTDOWN_TIMER);
		this.maxTeams = ge.getPropertyAsInt(GameProperties.MAX_TEAMS);
		this.minTeams = ge.getPropertyAsInt(GameProperties.MIN_TEAMS);
		this.budgetMultiPlayer = ge
				.getPropertyAsFloat(GameProperties.BUDGET_MULTI_PLAYER);
		this.budgetMultiNPC = ge
				.getPropertyAsFloat(GameProperties.BUDGET_MULTI_NPC);
		this.playerOnlyTeam = ge
				.getPropertyAsBoolean(GameProperties.PLAYER_ONLY_TEAM);
		this.sameTeamRelation = ge
				.getPropertyAsInt(GameProperties.SAME_TEAM_RELATION);
		this.initialUnitsPerTeam = ge
				.getPropertyAsInt(GameProperties.INITIAL_UNITS);

		playerSide = ge.getPropertyAsGE(GameProperties.PLAYER_SIDE);
		enemySide = ge.getPropertyAsGE(GameProperties.ENEMY_SIDE);

		if (initialUnitsPerTeam > maxUnitsPerTeam) {
			initialUnitsPerTeam = maxUnitsPerTeam;
		}
		this.startingPosition = ge
				.getPropertyAsGE(GameProperties.STARTING_POSITION);
		if (startingPosition == null) {
			Log.error("StartingPosition is null");
		}

		this.scopeGame = ge.getParamAsList(Extras.LIST_RESOURCE, GameProperties.SCOPE_GAME);
		this.scopeTeam = ge.getParamAsList(Extras.LIST_RESOURCE, GameProperties.SCOPE_TEAM);
		this.scopeUnit = ge.getParamAsList(Extras.LIST_RESOURCE, GameProperties.SCOPE_UNIT);
	}

	@Override
	public IGameComponent clonaComponent() {
		GameOppositionComponent ret = new GameOppositionComponent();
		ret.maxUnitsPerTeam = maxUnitsPerTeam;
		ret.scopeGame = scopeGame;
		ret.scopeTeam = scopeTeam;
		ret.scopeUnit = scopeUnit;
		ret.minUnitsPerTeam = minUnitsPerTeam;
		ret.maxTeams = maxTeams;
		ret.minTeams = minTeams;
		ret.budgetMultiPlayer = budgetMultiPlayer;
		ret.budgetMultiNPC = budgetMultiNPC;
		ret.playerOnlyTeam = playerOnlyTeam;
		ret.sameTeamRelation = sameTeamRelation;
		ret.initialUnitsPerTeam = initialUnitsPerTeam;
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;

	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());

	}

	@Override
	public void resetComponent() {
	}

	public boolean isResourceOnScope(String identifier, int scopeType) {
		if (scopeType == LogicConsts.SCOPE_GAME) {
			return StringHelper.listContainsItem(scopeGame, identifier);
		}
		if (scopeType == LogicConsts.SCOPE_TEAM) {
			return StringHelper.listContainsItem(scopeTeam, identifier);
		}
		if (scopeType == LogicConsts.SCOPE_UNIT) {
			return StringHelper.listContainsItem(scopeUnit, identifier);
		}
		return false;
	}

}
