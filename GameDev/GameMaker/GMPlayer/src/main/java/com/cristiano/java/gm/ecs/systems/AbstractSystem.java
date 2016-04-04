package com.cristiano.java.gm.ecs.systems;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.systems.map.MapWorldSystem;
import com.cristiano.java.gm.factory.AbstractGameFactory;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.Log;

public abstract class AbstractSystem extends GameEntity implements IGameSystem {

	public static boolean checkRunning = true;
	private static boolean lastCheckIsGameRunning = false;

	protected String compRequired = "";
	protected IRunGame game;
	private float delay = 1000;// delay=tempo entre iteracoes...
	private float currTimer = 0;

	protected ReuseManagerComponent reuseC = null;
	protected IManageElements em;
	private NiftyComponent niftyC;
	private PhysicsSpaceComponent physSpC = null;
	
	public int systemStageRequirements=0; //GameState

	protected static final List<IGameEntity> _reusableEnts = new ArrayList<IGameEntity>();
	protected static final List<IGameComponent> _reusableComps = new ArrayList<IGameComponent>();
	

	@Override
	public void update(float tpf) {
		iterateEntitiesWith(compRequired, tpf);
	}

	protected NiftyComponent getNiftyComponent() {
		if (niftyC != null) {
			return niftyC;
		}
		niftyC = (NiftyComponent) game.getGameEntity().getComponentWithTag(GameComps.COMP_NIFTY);
		if (niftyC == null) {
			Log.error("NiftyComponent undefined");
		}
		return niftyC;
	}

	protected boolean isGameRunning() {
		if (!checkRunning) {
			return lastCheckIsGameRunning;
		}
		boolean result = true;
		if (MapWorldSystem.currentMap == null) {
			return false;
		}
		if (!MapWorldSystem.currentMap.isOnStage(MapComponent.RUNNING)) {
			result = false;
		}
		if (result) {
			result = getNiftyComponent().isRunning();
		}
		if (lastCheckIsGameRunning != result) {
			if (result) {
				Log.info("Game is running...");
				getPhysicsSpace().activate();
			} else {
				Log.info("Game is paused...");
				getPhysicsSpace().deactivate();
			}
		}
		lastCheckIsGameRunning = result;
		checkRunning = false;
		return result;

	}

	protected PhysicsSpaceComponent getPhysicsSpace() {
		if (physSpC != null) {
			return physSpC;
		}
		physSpC = (PhysicsSpaceComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_PHYSICS_SPACE);
		return physSpC;
	}


	public MapComponent getMap() {
		if (game == null) {
			return null;
		}
		MapWorldComponent mapWorldComponent = ECS.getMapWorldComponent(game.getGameEntity());
		if (mapWorldComponent == null) {
			// Log.fatalIfRunning("mapWorld is null!");
			return null;
		}
		return ECS.getMap(mapWorldComponent);
	}

	public AbstractSystem(String compRequired) {
		this.compRequired = compRequired;
		if (compRequired == null) {
			Log.error("Sistema sendo carregado com componente base nulo:" + this);
		}
	}

	public UIActionComponent sendAction(IGameEntity gameEntity, String action) {
		UIActionComponent actionToggle = (UIActionComponent) entMan.addComponent(GameComps.COMP_UI_ACTION, gameEntity);
		actionToggle.originComponent = gameEntity;
		actionToggle.action = action;
		return actionToggle;
	}

	public IGameComponent getReusableComponent(String compType, boolean spawnNew) {
		initReuseComponent();
		IGameComponent component = reuseC.getReusableComponent(compType);
		if ((spawnNew) && (component == null)) {
			component = entMan.spawnComponent(compType);
		}
		return component;
	}

	public void removeReusableComponent(IGameComponent comp) {
		initReuseComponent();
		// I remove the component from ALL entities...
		entMan.removeComponent(comp);
		reuseC.addReusableComponent(comp);
	}

	protected ReuseManagerComponent initReuseComponent() {
		if (reuseC == null) {
			IGameEntity gameEntity = game.getGameEntity();
			reuseC = (ReuseManagerComponent) gameEntity.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER);
			if (reuseC == null) {
				Log.error("No reuseComponent found, adding one...");
				reuseC = (ReuseManagerComponent) entMan.addComponent(GameComps.COMP_REUSE_MANAGER, gameEntity);
			}
			reuseC.entMan = entMan;
			reuseC.em = getElementManager();
		}
		return reuseC;
	}

	protected boolean existsComponents(String comp) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(comp, true);
		return !ents.isEmpty();
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		this.entMan = entMan;
		this.game = game;

		entMan.addSystem(this);
	}

	public IManageElements getElementManager() {
		if (em == null) {
			ElementManagerComponent comp = (ElementManagerComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_ELEMENT_MANAGER);
			if (comp == null) {
				Log.error("ElementManagerComponent is null");
				return null;
			}

			em = comp.em;
		}
		return em;
	}

	protected void iterateEntitiesWith(String compClass, float tpf) {
		if (!checkGameStage()){
			return;
		}
		

		// realtimeTick(tpf);

		tpf = timerCheck(tpf);
		if (tpf < 0) {
			return;
		}

		Bench.start(BenchConsts.EV_SYSTEM_ITERATE, BenchConsts.CAT_UPDATE);
		Bench.start(getClass().getSimpleName(), BenchConsts.CAT_SYSTEM_UPDATE);

		preTick(tpf);

		// Bench: desprezivel
		List<IGameEntity> ents = retrieveEntities(compClass);

		int size = ents.size();
		for (int i = 0; i < size; i++) {
			IGameEntity ent = ents.get(i);
			iterateComponents(compClass, tpf, ent);
		}

		posTick(tpf);
		Bench.end(getClass().getSimpleName());
		Bench.end(BenchConsts.EV_SYSTEM_ITERATE);
	}

	public boolean checkGameStage() {
		int currState=GameState.currentStage;
		boolean b = (systemStageRequirements & currState)>0;
		/*if (!b){
			Log.debug(getClass().getSimpleName());
		}*/
		return b;
		//return true;
	}

	protected List<IGameEntity> retrieveEntities(String compClass) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(compClass, _reusableEnts);
		/*
		 * List<IGameEntity> entsDirect =
		 * entMan.getEntitiesWithComponentDirect(compClass,new
		 * ArrayList<IGameEntity>()); if (ents.size()!=entsDirect.size()){
		 * Log.error("Ent Size differ: "+compClass); }
		 */
		return ents;
	}

	protected void preTick(float tpf) {

	}

	protected void posTick(float tpf) {
	}

	private void iterateComponents(String compClass, float tpf, IGameEntity ent) {
		List<IGameComponent> comps = retrieveComponents(compClass, ent);
		for (IGameComponent comp : comps) {
			if ((!comp.isArchived()) && (comp.isActive())) {
				iterateEntity(ent, comp, tpf);
			}
		}
	}

	protected boolean existsComponent(String ident) {
		return entMan.getEntitiesWithComponent(ident).size() > 0;
	}

	protected boolean existsComponentActive(String ident) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(ident);
		for (IGameEntity ent : ents) {
			List<IGameComponent> comps = ent.getComponentsWithIdentifier(ident);
			for (IGameComponent comp : comps) {
				if (!comp.isArchived()) {
					return true;
				}
			}
		}
		return false;
	}

	protected List<IGameComponent> retrieveComponents(String compClass, IGameEntity ent) {
		List<IGameComponent> comps = ent.getComponentsWithIdentifier(compClass, _reusableComps);
		return comps;
	}

	private float timerCheck(float tpf) {
		currTimer += tpf;
		if (currTimer >= delay) {
			float newtpf = currTimer;
			currTimer = 0;
			return newtpf;
		}
		return -1;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		resetPriority();
		loadStageRequiremente(ge);
		//this.requiresRunning = ge.getPropertyAsBoolean(GameProperties.REQUIRES_RUNNING);
	}

	private void loadStageRequiremente(IGameElement ge) {
		String[] stages=ge.getParamAsList(Extras.LIST_DOMAIN, GameProperties.REQUIRES);
		int size=stages.length;
		systemStageRequirements=0;
		for (int i=0;i<size;i++){
			String stg=stages[i];
			if (stg.equals(GameState.STG_INIT_STR)){
				systemStageRequirements+=GameState.STG_INIT;
			}
			if (stg.equals(GameState.STG_LOADING_STR)){
				systemStageRequirements+=GameState.STG_LOADING;
			}
			if (stg.equals(GameState.STG_PAUSED_STR)){
				systemStageRequirements+=GameState.STG_PAUSED;
			}
			if (stg.equals(GameState.STG_RUNNING_STR)){
				systemStageRequirements+=GameState.STG_RUNNING;
			}
			if (stg.equals(GameState.STG_START_STR)){
				systemStageRequirements+=GameState.STG_START;
			}
		}
	}

	protected void changePriorityRealtime() {
		delay = 0;
	}

	protected void changePriorityAverage() {
		changePriority(GameConsts.PRIORITY_AVERAGE);
	}
	protected void changePriorityLow() {
		changePriority(GameConsts.PRIORITY_LOW);
	}

	protected void changePriority(float delay) {
		this.delay = delay;
	}

	protected void resetPriority() {
		this.delay = getElement().getPropertyAsFloat(GameProperties.PRIORITY);
	}

	public String toString() {
		return getId() + ":" + getClass().getSimpleName() + ":" + compRequired;
	}

	// This adds or removes a status components to the entity (can also be used
	// with other types of components...
	protected void addStatus(IGameEntity ent, String ident) {
		if (ent.containsComponent(ident)) {
			return;
		}
		entMan.addComponent(ident, ent);
	}

	protected void removeStatus(IGameEntity ent, String ident) {
		ent.removeComponent(ident);
	}

	// --- checks... maybe find a better place...
	protected boolean aiOnlySystem(IGameEntity ent, IGameComponent component) {
		if (!ent.containsComponent(GameComps.COMP_AI)) {
			Log.warn("Entity (" + ent + ") has " + component.getIdentifier() + "  and dont have an AI Component... removing");
			ent.removeComponent(component);
			return true;
		}
		return false;
	}

	public void spawnComponentsFromList(List<IGameElement> list, List<IGameComponent> intoList) {
		for (IGameElement elCondition : list) {
			IGameComponent stateComp = entMan.spawnComponent(elCondition.getIdentifier());
			stateComp.loadFromElement(elCondition);
			intoList.add(stateComp);
		}
	}

	@Override
	public boolean hasIdentifier(String identifier) {
		if (element != null) {
			if (element.getIdentifier().equals(identifier)) {
				return true;
			}
		} else {
			String name = getClass().getSimpleName();
			return (identifier.equals(name));
		}
		return false;
	}

	// ---------


	@Override
	public boolean canRunOnRelease() {
		if (getElement() == null) {
			return true;
		}
		return AbstractGameFactory.elementCanRunOnRelease(getElement());
	}

	@Override
	public boolean canRunOnDev() {
		if (getElement() == null) {
			return true;
		}
		return AbstractGameFactory.elementCanRunOnDev(getElement());
	}

	@Override
	public void free() {
		super.free();
	}
}
