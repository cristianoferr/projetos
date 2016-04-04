package com.cristiano.java.gm.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.systems.AbstractSystem;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.jme3.interfaces.IControlReuse;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

/*
 * Unit Test: TestEntitySystem
 * */
public class EntityManager implements IControlReuse,  AppState {
	private final List<IGameEntity> entities = new ArrayList<IGameEntity>();
	public final List<IGameEntity> entitiesToAdd = new ArrayList<IGameEntity>();
	public List<IGameEntity> entitiesToExport = null;

	private final List<IGameSystem> systems = new ArrayList<IGameSystem>();
	private final Map<String, List<IGameEntity>> componentMapping = new HashMap<String, List<IGameEntity>>();

	private IGameFactory factory;
	int entityCount = 0;

	float shrink_timer = 0f;
	int shrink_index = 0;

	ReuseManagerComponent reuseC = null;

	private boolean paused = false;

	private final List<IGameEntity> _reusableEnts = new ArrayList<IGameEntity>();
	private final List<IGameComponent> _reusableComps = new ArrayList<IGameComponent>();
	private Application app;

	public EntityManager() {
		Log.debug(">> Creating EntityManager...");
	}

	public IGameComponent addComponent(IGameComponent comp, IGameEntity toEntity) {
		if (comp == null) {
			return null;
		}
		if (toEntity == null) {
			Log.error("Trying to add a component to a null entity...");
			return null;
		}
		comp.setEntityManager(this);
		toEntity.setEntityManager(this);
		toEntity.attachComponent(comp);
		addEntity(toEntity);
		comp.addInfo(CRJavaUtils.getMethodDescriptionAt(1));
		return comp;
	}

	public IGameEntity getEntityWithId(int id) {
		int size = entities.size();

		for (int i = 0; i < size; i++) {
			IGameEntity ent = entities.get(i);
			if (ent.getId() == id) {
				return ent;
			}
		}

		size = entitiesToAdd.size();
		for (int i = 0; i < size; i++) {
			IGameEntity ent = entitiesToAdd.get(i);
			if (ent.getId() == id) {
				return ent;
			}
		}
		return null;
	}

	public boolean addEntity(IGameEntity ent) {
		if (ent == null) {
			Log.error("Tentando adicionar uma entidade nula!");
			return false;
		}
		if (containsEntity(ent)) {
			return false;
		}
		ent.setListed();
		ent.setEntityManager(this);
		entitiesToAdd.add(ent);
		return true;
	}

	public boolean containsEntity(IGameEntity ent) {
		return entities.contains(ent) || entitiesToAdd.contains(ent);
	}

	public List<IGameEntity> getEntitiesWithComponent(String compIdentifier,
			List<IGameEntity> ret) {
		ret.clear();
		List<IGameEntity> map = getMap(compIdentifier);

		// sanity check... (not necessary anymore...)
		/*
		 * int size = map.size(); if (size==0){ return ret; } for (int i = size
		 * - 1; i >= 0; i--) { IGameEntity ent = map.get(i); if
		 * (!ent.isListed()){ map.remove(i); } }
		 */

		ret.addAll(map);
		int size = ret.size();
		if (size == 0) {
			return ret;
		}
		for (int i = size - 1; i >= 0; i--) {
			IGameEntity ent = ret.get(i);
			if (!ent.isActive()) {
				ret.remove(i);
			}
		}
		return ret;
	}

	// old method
	public List<IGameEntity> getEntitiesWithComponentDirect(
			String compIdentifier, List<IGameEntity> ret) {
		ret.clear();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			IGameEntity ent = entities.get(i);
			if (ent.isActive()) {
				if (ent.getComponentWithIdentifier(compIdentifier) != null) {
					ret.add(ent);
				}
			}
		}
		return ret;
	}

	public List<IGameEntity> getEntitiesWithComponentTag(String compTag,
			List<IGameEntity> ret) {
		ret.clear();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			IGameEntity ent = entities.get(i);
			if (ent.isActive()) {
				if (ent.getComponentWithTag(compTag) != null) {
					ret.add(ent);
				}
			}
		}
		return ret;
	}

	public List<IGameEntity> getEntitiesWithComponent(String compTag,
			boolean reuse) {
		if (reuse) {
			return getEntitiesWithComponent(compTag, _reusableEnts);
		} else {
			return getEntitiesWithComponent(compTag);
		}
	}

	public List<IGameEntity> getEntitiesWithComponent(String compTag) {
		List<IGameEntity> ret = new ArrayList<IGameEntity>();
		return getEntitiesWithComponent(compTag, ret);
	}

	public IGameEntity getEntityWithComponent(String compTag) {
		for (IGameEntity ent : entities) {
			if (ent.isActive()) {
				if (ent.containsComponent(compTag)) {
					return ent;
				}
			}
		}
		Log.warn("No entity with component " + compTag + " was found.");
		return null;
	}

	public IGameEntity getEntityWithComponent(IGameComponent child) {
		List<IGameEntity> ents = getEntitiesWithComponent(child, _reusableEnts);
		if (ents.size() > 0) {
			return ents.get(0);
		}
		return null;
	}

	public List<IGameEntity> getEntitiesWithComponent(IGameComponent compTag,
			boolean reuse) {
		if (reuse) {
			return getEntitiesWithComponent(compTag, _reusableEnts);
		} else {
			return getEntitiesWithComponent(compTag);
		}
	}

	public List<IGameEntity> getEntitiesWithComponent(IGameComponent comp) {
		List<IGameEntity> ret = new ArrayList<IGameEntity>();
		return getEntitiesWithComponent(comp, ret);
	}

	public List<IGameEntity> getEntitiesWithComponent(IGameComponent comp,
			List<IGameEntity> ret) {
		ret.clear();
		for (int i = 0; i < entities.size(); i++) {
			IGameEntity ent = entities.get(i);
			if (ent.containsComponent(comp)) {
				ret.add(ent);
			}
		}
		return ret;
	}

	public int size() {
		return entities.size();
	}

	public void removeEntity(IGameEntity ent) {
		removeComponentsFromEntity(ent);
		entities.remove(ent);
		ent.setUnlisted();
		entitiesToAdd.remove(ent);

		if (ent instanceof IGameComponent) {
			reuseObject(((IGameComponent) ent).getIdentifier(), ent);
		} else {
			reuseObject(GameConsts.ENTITY_TYPE, ent);
		}

	}

	private void removeComponentsFromEntity(IGameEntity ent) {
		List<IGameComponent> components = ent.getComponents(Extras.TAG_ALL,
				_reusableComps);
		for (IGameComponent comp : components) {
			reuseObject(comp.getIdentifier(), comp);
		}
		ent.removeAllComponents();
	}

	public void addSystem(IGameSystem system) {
		if (system == null) {
			return;
		}
		if (CRJavaUtils.isRelease()) {
			if (!system.canRunOnRelease()) {
				return;
			}
		} else {
			if (!system.canRunOnDev()) {
				return;
			}
		}
		for (int i = 0; i < systems.size(); i++) {
			if (systems.get(i) == system) {
				return;
			}
		}
		Log.debug("Adicionando system:" + system);
		systems.add(system);
		addEntity(system);

	}

	@Override
	public void update(float tpf) {
		if (paused) {
			return;
		}

		Bench.start(BenchConsts.EV_ENTMAN_UPDATE, BenchConsts.CAT_UPDATE);
		AbstractSystem.checkRunning = true;

		int size = systems.size();
		for (int i = 0; i < size; i++) {
			IGameSystem system = systems.get(i);
			system.update(tpf);
		}
		cleanup();
		shrink();
		Bench.end(BenchConsts.EV_ENTMAN_UPDATE);
	}

	private void shrink() {
		shrink_index++;
		int size = entities.size();
		if (size == 0) {
			return;
		}
		if (shrink_index >= size) {
			shrink_index = 0;
		}
		//checkEntityToShrink();
	}

	private void checkEntityToShrink() {
		IGameEntity ent = entities.get(shrink_index);
		if (ent.isComponent()) {
			List<IGameEntity> ents = getEntitiesWithComponent(
					(IGameComponent) ent, _reusableEnts);
			if (ents.size() == 0) {
				entities.remove(ent);
				ent.setUnlisted();
				Log.debug("shrinking comp:"+ent.getId()+" "+ent);
			}
		} else {
			if (ent.size() == 0) {
				entities.remove(ent);
				Log.debug("shrinking ent:"+ent.getId()+" "+ent);
				ent.setUnlisted();
			}
		}
	}

	// remove unwanted components from the recycle bin
	public void cleanup() {
		JMEAbstractSystem.playerEnt = null;

		if (entitiesToAdd.isEmpty()) {
			return;
		}
		try {
			int size = entitiesToAdd.size();
			for (int i = 0; i < size; i++) {
				IGameEntity ent = entitiesToAdd.get(i);
				if (!entities.contains(ent)) {
					entities.add(ent);
				}
			}
			entitiesToAdd.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<IGameSystem> getSystems() {
		return systems;
	}

	public IGameSystem getSystem(String identifier) {
		for (IGameSystem system : systems) {
			if (system.hasIdentifier(identifier)) {
				return system;
			}
		}
		Log.errorIfRunning("System with identifier " + identifier
				+ " not found.");
		return null;
	}

	public IGameFactory getFactory() {
		return factory;
	}

	public void setFactory(IGameFactory fact) {
		factory = fact;
	}

	public IGameComponent addComponent(String compIdent, IGameEntity entity) {
		IGameComponent comp;
		comp = spawnComponent(compIdent);
		IGameComponent addComponent = addComponent(comp, entity);
		addComponent.addInfo(CRJavaUtils.getMethodDescriptionAt(1));
		return addComponent;
	}

	public IGameComponent addComponent(IGameElement elComp, IGameEntity entity) {
		IGameComponent comp;
		comp = factory.createComponentFrom(elComp);

		IGameComponent addComponent = addComponent(comp, entity);
		addComponent.addInfo(CRJavaUtils.getMethodDescriptionAt(1));
		return addComponent;
	}

	public IGameComponent spawnComponent(String compIdent) {
		if (reuseC != null) {
			IGameComponent comp = reuseC.getReusableComponent(compIdent);
			if (comp != null) {
				// Log.debug("reusing component:" + comp);
				// comp.free();
				comp.addInfo("reusing component at entMan:" + compIdent);
				comp.setFirstTick();
				comp.setEntityManager(this);
				return comp;
			}
		}
		// Log.debug("spawnComponent:" + compIdent);

		return factory.createComponentFromClass(compIdent);
	}

	public int generateID() {
		return entityCount++;
	}

	public void checkID(int id) {
		if (entityCount <= id) {
			entityCount = id + 1;
		}
	}

	public IGameEntity getEntityWithTag(String tag) {
		tag = tag.replace("{", "").replace("}", "");
		for (IGameEntity ent : entities) {
			IGameElement element = ent.getElement();
			if (element != null) {
				if (element.hasTag(tag)) {
					return ent;
				}
			}
		}
		return null;
	}

	public IGameEntity clonaEntidade(IGameEntity molde) {
		return factory.clonaEntidade(molde);
	}

	public void removeComponentFromEntity(IGameComponent comp, IGameEntity ent) {
		ent.removeComponent(comp);
	}

	public void removeComponentsFromEntity(String comp, IGameEntity entity) {
		entity.removeComponent(comp);
	}

	public void removeComponent(IGameComponent comp) {
		List<IGameEntity> allEntitiesWithComponent = getEntitiesWithComponent(
				comp, true);
		for (IGameEntity ent : allEntitiesWithComponent) {
			removeComponentFromEntity(comp, ent);
		}
	}

	public void removeComponent(String compTag) {
		List<IGameEntity> allEntitiesWithComponent = getEntitiesWithComponent(
				compTag, true);
		for (IGameEntity ent : allEntitiesWithComponent) {
			ent.removeComponent(compTag);
		}

	}

	public IGameComponent addIfNotExistsComponent(String ident, IGameEntity ent) {
		IGameComponent comp = ent.getComponentWithIdentifier(ident);
		if (comp != null) {
			return comp;
		}
		return addComponent(ident, ent);
	}

	public void addIfNotExistsComponent(IGameComponent comp, IGameEntity ent) {
		if (ent.containsComponent(comp)) {
			return;
		}
		addComponent(comp, ent);

	}

	@Override
	public void reuseObject(String key, Object obj) {
		if (reuseC == null) {
			Log.warn("No ReuseManager defined");
			return;
		}
		IGameEntity ent = (IGameEntity) obj;

		ent.free();
		reuseC.addToBin(key, obj);
		// Log.debug("Reusing object with key:"+key);
	}

	public void setReuseManager(ReuseManagerComponent reuseC) {
		this.reuseC = reuseC;
	}

	public IGameComponent addDefaultComponent(String compIdent,
			IGameEntity entity, IManageElements em) {
		IGameComponent comp = addComponent(compIdent, entity);
		comp.loadDefault(em);
		return comp;
	}

	public IGameEntity createEntity() {
		IGameEntity ent = new GameEntity();
		addEntity(ent);
		return ent;
	}

	public void addEntityToExport(IGameEntity ent) {
		if (!entitiesToExport.contains(ent)) {
			entitiesToExport.add(ent);
		}
	}

	public JSONObject exportToJSON() {
		cleanup();

		JSONObject entMan = new JSONObject();
		JSONObject ents = new JSONObject();
		JSONObject components = new JSONObject();

		int totalExported = exportEntities(ents);

		Log.info(totalExported + " entities were exported to JSON (of "
				+ size() + ")...");
		entMan.put(GameProperties.ENTITY, ents);
		entMan.put(GameProperties.INITIAL_UNITS, entityCount);
		entMan.put(GameProperties.COMPONENT, components);
		return entMan;
	}

	public int exportEntities(JSONObject ents) {
		int totalExported = 0;
		entitiesToExport = new ArrayList<IGameEntity>();
		entitiesToExport.addAll(entities);
		int i = 0;
		while (i < entitiesToExport.size()) {
			IGameEntity entity = entitiesToExport.get(i);
			entity.setEntityManager(this);
			JSONObject ent = null;
			if (factory.okToExport(entity)) {
				ent = entity.exportToJSON();
			}

			//ent = validateExportComponent(entity, ent);
			if (ent != null) {
				String strID = ent.get(GameProperties.ID).toString();
				ents.put(strID, ent);
				totalExported++;
			}
			i++;
		}
		entitiesToExport.clear();
		return totalExported;
	}

	private JSONObject validateExportComponent(IGameEntity entity,
			JSONObject json) {
		if (entity instanceof IGameComponent) {
			List<IGameEntity> entitiesWithComponent = getEntitiesWithComponent((IGameComponent) entity);
			if (entitiesWithComponent.isEmpty()) {
				Log.warn("Component has no master:" + entity
						+ ", not exporting.");
				json = null;
			} else {
				boolean ok = false;
				for (IGameEntity ent : entitiesWithComponent) {
					if (factory.okToExport(ent)) {
						ok = true;
					}
				}
				if (!ok) {
					json = null;
				}
			}
		}
		return json;
	}

	public void importFromJSON(JSONObject jsonEnts) {
		entities.clear();
		JSONObject ents = (JSONObject) jsonEnts.get(GameProperties.ENTITY);
		Log.info("Importing " + ents.size() + " entities from JSON...");
		entityCount = CRJsonUtils.getInteger(jsonEnts,
				GameProperties.INITIAL_UNITS);
		CRJsonUtils.importList(ents, factory, entities);
		attachComponents();

		// importComponents(jsonEnts);
	}

	private void attachComponents() {
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			GameEntity ent = (GameEntity) entities.get(i);
			ent.linkComponents();
		}
	}

	public List<IGameEntity> getEntities() {
		return entities;
	}

	public void checkRemoval(IGameComponent comp) {
		for (IGameEntity ent : entities) {
			if (ent.containsComponent(comp)) {
				return;
			}
		}
		// Log.info("Killing component: " + comp);
		removeEntity(comp);
	}

	public void pause() {
		paused = true;

	}

	public void unpause() {
		paused = false;

	}

	public void purgeToSize(int size) {
		while (entities.size() > size) {
			removeEntity(entities.get(entities.size() - 1));
		}
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void setEnabled(boolean active) {

	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void stateAttached(AppStateManager stateManager) {

	}

	@Override
	public void stateDetached(AppStateManager stateManager) {

	}

	@Override
	public void render(RenderManager rm) {

	}

	@Override
	public void postRender() {

	}

	public List<IGameComponent> getComponentsWithIdentifier(String ident) {
		return getComponentsWithIdentifier(ident,
				new ArrayList<IGameComponent>());
	}

	public List<IGameComponent> getComponentsWithIdentifier(String ident,
			List<IGameComponent> list) {
		list.clear();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			IGameEntity ent = entities.get(i);
			if (ent.isActive()) {
				List<IGameComponent> comps = ent.getComponentsWithIdentifier(
						ident, _reusableComps);
				int sizeC = comps.size();
				for (int j = 0; j < sizeC; j++) {
					IGameComponent comp = comps.get(j);
					if (comp.getIdentifier().equals(ident)) {
						list.add(comp);
					}
				}
			}
		}
		return list;
	}

	public void destroy() {
		FutureManager.destroy();
	}

	public Application getApp() {
		return app;
	}

	public void link(IGameEntity gameEntity, String identifier) {
		List<IGameEntity> map = getMap(identifier);
		if (!map.contains(gameEntity)) {
			// Log.info("Adding '" + gameEntity + "' to " + identifier + " ::" +
			// map.size());
			map.add(gameEntity);
		}
	}

	public void unlink(IGameEntity gameEntity, String identifier) {
		List<IGameEntity> map = getMap(identifier);
		map.remove(gameEntity);
		// Log.info("Removing '" + gameEntity + "' from " + identifier + " ::" +
		// map.size());
	}

	private List<IGameEntity> getMap(String identifier) {
		List<IGameEntity> list = componentMapping.get(identifier);
		if (list == null) {
			list = new ArrayList<IGameEntity>();
			componentMapping.put(identifier, list);
		}
		return list;
	}

	public void reportEntities() {
		List<IGameEntity> alreadyShown = new ArrayList<IGameEntity>();
		Log.info("Reporting Entities");
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).report(alreadyShown, "--");
		}
		Log.info("End Report");

	}

}
