package com.cristiano.java.gm.ecs.systems;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.macro.BestiaryLibraryComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamMemberComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitResourcesComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.FXLibraryComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.comps.visual.ThemeComponent;
import com.cristiano.java.gm.ecs.systems.macro.GameEventSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public abstract class JMEAbstractSystem extends AbstractSystem {
	protected IFinalGame game;
	protected JMESnippets snippets;
	protected static InternationalComponent inter = null;
	private IGameEntity gameEnt;
	private GameEventSystem gameEventSystem;
	private static MapWorldComponent mapWorld = null;
	protected static BestiaryLibraryComponent libComp = null;
	protected GameConstsComponent consts;
	private IGameComponent fxLib;

	// public DebugTools debugTools = null;
	public static IGameEntity playerEnt;

	public JMEAbstractSystem(String compRequired) {
		super(compRequired);
	}

	protected void updateBounds(IGameEntity ent, RenderComponent compRender) {
		/*if (ent.containsComponent(GameComps.COMP_DIMENSION)){
			return;
		}*/
		
		BoundingBox bounds = null;
		try {
			bounds=(BoundingBox) compRender.node.getWorldBound();
		} catch (Exception e) {
			Log.error("Error getting World Bounds... "+e.getMessage());
		}
		
		if (bounds == null) {
			Log.error("No bounds for node... something is wrong when loading mesh.");
			compRender.firstTick=true;
			return;
		}

		DimensionComponent comp = (DimensionComponent) entMan
				.addIfNotExistsComponent(GameComps.COMP_DIMENSION, ent);
		comp.dimension.set(bounds.getXExtent(), bounds.getYExtent(),
				bounds.getZExtent());

		if (CRJavaUtils.BOUNDS_DEBUG) {
			if (getSnippets() != null) {
				getSnippets().generateBox(ColorRGBA.Green, comp.dimension,
						Vector3f.ZERO);
			}
		}
	}

	protected boolean isPhysicsLoaded() {
		PhysicsSpaceComponent physSpC = getPhysicsSpace();
		if (physSpC == null) {
			return false;
		}
		if (physSpC.physics == null) {
			return false;
		}
		if (physSpC.physics.getPhysicsSpace() == null) {
			return false;
		}
		return true;
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
		this.game = (IFinalGame) game;
		if (this.game != null) {
			this.snippets = this.game.getSnippets();
		}
	}

	// general
	protected GameEventSystem getGameEventSystem() {
		if (gameEventSystem == null) {
			gameEventSystem = (GameEventSystem) entMan
					.getSystem(GameEventSystem.class.getSimpleName());
		}

		return gameEventSystem;
	}

	protected void initInternational() {
		if (inter == null) {
			inter = (InternationalComponent) getGameEntity()
					.getComponentWithIdentifier(GameComps.COMP_INTERNATIONAL);
		}
	}

	public BestiaryLibraryComponent iniciaBestiaryLibrary() {
		if (libComp == null) {
			IGameEntity gameEntity = game.getGameEntity();
			libComp = (BestiaryLibraryComponent) gameEntity
					.getComponentWithIdentifier(GameComps.COMP_BESTIARY_LIB);
			if (libComp == null) {
				Log.fatal("No Bestiary Library Component found!");
				return libComp;
			}

		}
		return libComp;
	}

	protected MapWorldComponent getMapWorldComponent() {
		if (mapWorld == null) {
			mapWorld = (MapWorldComponent) game.getGameEntity()
					.getComponentWithIdentifier(GameComps.COMP_MAP_WORLD);
		}
		return mapWorld;
	}

	public MapLocationComponent getMapLocationComponent() {
		MapLocationComponent genreC = (MapLocationComponent) game
				.getGameEntity().getComponentWithTag(
						GameComps.COMP_MAP_LOCATION);
		return genreC;
	}

	protected PlayerComponent getPlayerComponent() {
		return ECS.getPlayerComponent(getPlayerEntity());
	}

	protected IGameEntity getPlayerEntity() {
		if (playerEnt != null) {
			return playerEnt;
		}
		List<IGameEntity> players = entMan
				.getEntitiesWithComponent(GameComps.COMP_PLAYER);
		if (players.size() == 0) {
			return null;
		}
		playerEnt = players.get(0);
		return playerEnt;
	}

	protected boolean isPlayer(IGameEntity ent) {
		return ent.containsComponent(GameComps.COMP_PLAYER);
	}

	protected IGameEntity getGameEntity() {
		if (gameEnt == null) {
			gameEnt = game.getGameEntity();
		}
		return gameEnt;
	}

	// this will return the camcomponent, removing from the entity it currently
	// is...
	protected CamComponent extractCamComponent(IGameEntity entTo) {
		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_CAM);
		IGameEntity ent = ents.get(0);
		CamComponent camC = (CamComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_CAM);
		entTo.attachComponent(camC);
		ent.removeComponent(camC);
		return camC;
	}

	protected CamComponent getCamComponent() {
		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_CAM);
		if (ents.isEmpty()) {
			Log.error("Cam Component is null...");
			return null;
		}
		IGameEntity ent = ents.get(0);
		return (CamComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_CAM);
	}

	protected JMESnippets getSnippets() {
		return game.getSnippets();
	}

	// specific

	public int getRelationBetween(IGameEntity ent, IGameEntity target) {
		TeamMemberComponent teamEnt = (TeamMemberComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_TEAM_MEMBER);
		TeamMemberComponent teamTarget = (TeamMemberComponent) target
				.getComponentWithIdentifier(GameComps.COMP_TEAM_MEMBER);

		if ((teamEnt == null) || (teamTarget == null)) {
			return LogicConsts.RELATION_ENEMY;
		}
		if (teamEnt.idTeam == teamTarget.idTeam) {
			return teamTarget.getSameTeamRelation();
		}
		return LogicConsts.RELATION_ENEMY;
	}

	public static void defineTarget(EntityManager entMan, IGameEntity ent,
			IGameEntity target) {
		Log.debug(ent + " has defined target to:" + target);
		TargetComponent targetC = (TargetComponent) entMan
				.addIfNotExistsComponent(GameComps.COMP_TARGET, ent);
		targetC.target = target;
		targetC.directPosition = (PositionComponent) target
				.getComponentWithIdentifier(GameComps.COMP_POSITION);
		targetC.targetBody = null;
		ECS.updateTargetPosition(ent, targetC);
	}

	protected GameConstsComponent getGameConstsComponent() {
		if (consts == null) {
			consts = (GameConstsComponent) game.getGameEntity()
					.getComponentWithIdentifier(GameComps.COMP_GAME_CONSTS);
		}
		if (consts == null) {
			Log.error("GameConsts undefined");
		}
		return (GameConstsComponent) consts;
	}

	protected GameGenreComponent getGameGenre() {
		GameGenreComponent genreC = (GameGenreComponent) game.getGameEntity()
				.getComponentWithIdentifier(GameComps.COMP_GAME_GENRE);
		if (genreC == null) {
			Log.error("GameGenre Component not defined.");
		}
		return genreC;
	}

	protected ThemeComponent getTheme() {
		ThemeComponent genreC = (ThemeComponent) getGameEntity()
				.getComponentWithIdentifier(GameComps.COMP_THEME);
		if (genreC == null) {
			Log.error("GameGenre Component not defined.");
		}
		return genreC;
	}

	protected void changeScreen(IGameElement elScreen) {
		if (elScreen == null) {
			Log.error("Trying to change to a non-existant screen");
			return;
		}
		IGameEntity gameEnt = getGameEntity();
		NiftyComponent niftyC = (NiftyComponent) gameEnt
				.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		niftyC.changeScreenTo(elScreen);
	}

	protected FXLibraryComponent getFXLibrary() {
		if (fxLib == null) {
			fxLib = game.getGameEntity().getComponentWithIdentifier(
					GameComps.COMP_FX_LIB);
		}
		if (fxLib == null) {
			Log.warn("FXLibrary is null, adding one...");
			fxLib = entMan.addDefaultComponent(GameComps.COMP_FX_LIB,
					game.getGameEntity(), getElementManager());
		}
		return (FXLibraryComponent) fxLib;
	}

	protected UnitResourcesComponent getUnitResourcesComponent() {
		return (UnitResourcesComponent) game.getGameEntity()
				.getComponentWithIdentifier(GameComps.COMP_UNIT_RESOURCES);
	}
}
