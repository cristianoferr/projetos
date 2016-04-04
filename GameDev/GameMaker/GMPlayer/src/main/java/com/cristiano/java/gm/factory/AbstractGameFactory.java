package com.cristiano.java.gm.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.persists.CloneComponent;
import com.cristiano.java.gm.ecs.comps.persists.TextKey;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.extras.ObjectTypes;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.export.Savable;

public abstract class AbstractGameFactory implements IGameFactory {

	private static final long serialVersionUID = 6457096460733757208L;
	protected IManageElements em;
	protected EntityManager entMan;
	PackageCentral pkg = new PackageCentral();
	private HashMap<String, String> initClasses = new HashMap<String, String>();

	public AbstractGameFactory(IManageElements em, EntityManager entMan) {
		this.em = em;
		this.entMan = entMan;
		if (entMan == null) {
			Log.fatalIfRunning("EntMan is undefined!");
			return;
		}
		if (em == null) {
			Log.fatalIfRunning("ElementManager is undefined!");
			return;
		}
		entMan.setFactory(this);

		addPackage(ObjectTypes.TYPE_GAME_SYSTEM, GameConsts.SYSTEM_PACKAGE);
		addPackage(ObjectTypes.TYPE_ENTITY, GameConsts.ENTITY_PACKAGE);
		addPackage(ObjectTypes.TYPE_COMPONENT, GameConsts.COMPONENT_PACKAGE);
		addPackage(ObjectTypes.TYPE_GAME_STATE, GameConsts.STATE_PACKAGE);
	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		JSONObject objClasses = new JSONObject();

		Iterator<Entry<String, String>> it = initClasses.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String key = pairs.getKey();
			objClasses.put(key, initClasses.get(key));
		}

		obj.put(GameProperties.CLASS_PROPERTY, objClasses);

		return obj;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		JSONObject jsonProps = (JSONObject) json.get(GameProperties.CLASS_PROPERTY);
		Iterator<String> it = jsonProps.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String item = (String) jsonProps.get(key);
			initClasses.put(key, item);
		}
	}

	@Override
	public Object instantiateClass(String className, String objectType) {
		return pkg.instantiateClass(className, objectType);
	}

	@Override
	public Object instantiateClass(IGameElement element) {
		return pkg.instantiateClass(element.getIdentifier(), element.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS));

	}

	@Override
	public IGameComponent createComponentFromClass(String compIdent) {
		String classeComponent = initClasses.get(compIdent);
		if (classeComponent == null) {
			Log.error("Componente " + compIdent + " desconhecido.");
		}
		IGameComponent comp = (IGameComponent) createEntityFromClass(classeComponent, ObjectTypes.TYPE_COMPONENT);
		if (comp!=null){
			comp.addInfo("AbstractGameFactory.SpawnComponent ("+compIdent+")");
		}
		return comp;
	}

	public void addPackage(String typeEntity, String entityPackage) {
		pkg.addPackage(typeEntity, entityPackage);
	}

	protected IGameEntity createEntityFromClass(String init, String classe) {
		IGameEntity entity;
		entity = (IGameEntity) instantiateClass(init, classe);

		entMan.addEntity(entity);
		return entity;
	}
	
	@Override
	public void loadComponents(IGameEntity ent, String tag){
		Log.fatal("Not available.");	
	}

	@Override
	public void loadComponents(IGameEntity entity, IGameElement element) {
		if (element == null) {
			Log.fatal("Element is null!!");
		}
		List<IGameElement> lista = element.getObjectList(GameConsts.IDENT_COMPONENT);
		for (IGameElement elComponent : lista) {
			IGameComponent comp = createComponentFrom(elComponent);
			if (comp==null){
				Log.error("Null component was created from "+elComponent.getIdentifier());
			}
			entMan.addComponent(comp, entity);
		}
	}

	protected String getClasseFromComponent(IGameElement comp) {
		String init = comp.getProperty(Extras.PROPERTY_INIT);
		if (!"".equals(init)) {
			return init;
		}
		return comp.getIdentifier();
	}

	@Override
	public IGameEntity clonaEntidade(IGameEntity molde) {
		GameEntity entity = new GameEntity();
		entity.deactivate();
		cloneComponents(molde, entity);
		checkClonedEntity(entity);

		CloneComponent cloneC = (CloneComponent) entMan.addComponent(GameComps.COMP_CLONE, entity);
		cloneC.idMolde = molde.getId();
		return entity;
	}

	private void cloneComponents(IGameEntity molde, IGameEntity entity) {
		List<IGameComponent> components = molde.getAllComponents();
		for (IGameComponent comp : components) {
			if (!comp.getIdentifier().equals(GameComps.COMP_MASTER)) {
				IGameComponent clonaComponent = comp.clonaComponent();
				if (clonaComponent != null) {
					clonaComponent.setSourceID(comp.getId());
					clonaComponent.addInfo("GameFactory.clonaEntidade()");
					entMan.addComponent(clonaComponent, entity);
				}
				cloneComponents(comp,clonaComponent);
			}
		}
	}

	// executa algum finetuning em algum componente...
	protected void checkClonedEntity(GameEntity entity) {

	}

	@Override
	public IGameState createStateFrom(IGameElement elState) {
		IGameState state = (IGameState) createEntityFrom(elState);
		state.initWithEntityManager(entMan);
		return state;
	}

	@Override
	public IGameSystem createSystemFrom(IGameElement elSystem) {
		if (elSystem == null) {
			Log.error("System element is null!");
			return null;
		}
		
		if (CRJavaUtils.isRelease()) {
			if (!elementCanRunOnRelease(elSystem)) {
				return null;
			}
		} else {
			if (!elementCanRunOnDev(elSystem)) {
				return null;
			}
		}
		
		IGameSystem system = (IGameSystem) createEntityFrom(elSystem);
		return system;

	}

	public static boolean elementCanRunOnDev(IGameElement el) {
		String[] targets=el.getParamAsList(Extras.LIST_DOMAIN, GameProperties.TARGET);
		for (String target:targets){
			if (target.equals(GameConsts.TARGET_DEV)){
				return true;
			}
		}
		return false;
	}
	public static boolean elementCanRunOnRelease(IGameElement el) {
		String[] targets=el.getParamAsList(Extras.LIST_DOMAIN, GameProperties.TARGET);
		for (String target:targets){
			if (target.equals(GameConsts.TARGET_RELEASE)){
				return true;
			}
		}
		return false;
	}

	
	@Override
	public IGameComponent createComponentFrom(IGameElement elComponent) {
		IGameComponent comp = (IGameComponent) createEntityFrom(elComponent);
		return comp;
	}

	public void addClasse(String comp, String classe) {
		initClasses.put(comp, classe);
	}

	@Override
	public IGameEntity restoreEntityFromID(String id) {
		// TODO Auto-generated method stub
		Log.todo("Resolver essa função");
		return null;
	}

	@Override
	public Object assembleJSON(Object o) {
		if (o == null) {
			return null;
		}
		JSONObject obj = (JSONObject) o;
		String objectType = (String) obj.get(GameProperties.OBJECT_TYPE);
		if (objectType != null) {
			if ((objectType.equals(JavaConsts.OBJECT_GAME_ENTITY)) || (objectType.equals(JavaConsts.OBJECT_GAME_COMPONENT))) {
				return preAssembleEntity(obj, objectType);
			}
			if (isElement(objectType)) {
				return assembleGenericElement(obj);
			}
			if (objectType.equals(JavaConsts.TEXT_KEY)) {
				TextKey us = new TextKey(obj);
				return us;
			}
			if (objectType.equals(JavaConsts.OBJECT_UNIT_STORAGE)) {
				UnitStorage us = new UnitStorage(obj, this,entMan);
				return us;
			}
		}
		Log.fatal("Unknown object type:" + obj);
		return null;
	}

	private boolean isElement(String objectType) {
		if (objectType.equals(JavaConsts.OBJECT_GENERIC_ELEMENT)) {
			return true;
		}
		if (objectType.equals(JavaConsts.OBJECT_TEMPLATE)) {
			return true;
		}
		return false;
	}

	private Object preAssembleEntity(JSONObject obj, String objectType) {
		int id = CRJsonUtils.getInteger(obj, GameProperties.ID);
		IGameEntity ent = entMan.getEntityWithId(id);
		if (ent != null) {
			//checkImportedComponent(obj, objectType, id, ent);
			return ent;
		}
		Bench.start(BenchConsts.EV_ASSEMBLING_ENTITY,BenchConsts.CAT_ATOMIC_INIT);
				ent = assembleEntity(obj, objectType, ent);
		Log.debug("Assembled entity:"+ent+" with type:"+objectType);

		Bench.end(BenchConsts.EV_ASSEMBLING_ENTITY);
		return ent;
	}

	private IGameEntity assembleEntity(JSONObject obj, String objectType,
			IGameEntity ent) {
		if (objectType.equals(JavaConsts.OBJECT_GAME_ENTITY)) {
			ent = assembleGameEntity(obj);
		} else if (objectType.equals(JavaConsts.OBJECT_GAME_COMPONENT)) {
			ent = assembleGameComponent(obj);
		} else {
			Log.fatal("Unknown object type:"+objectType);
		}
		return ent;
	}

	private IGameEntity assembleGameEntity(JSONObject obj) {
		IGameEntity ent;
		Bench.start(BenchConsts.EV_ASSEMBLING+":importJsonEnt",BenchConsts.CAT_ATOMIC_INIT);
		ent = entMan.createEntity();
		ent.importFromJSON(obj);
		Bench.end(BenchConsts.EV_ASSEMBLING+":importJsonEnt");
		return ent;
	}

	private IGameEntity assembleGameComponent(JSONObject obj) {
		IGameEntity ent;
		Bench.start(BenchConsts.EV_ASSEMBLING+":importJsonComp",BenchConsts.CAT_ATOMIC_INIT);
		
		String ident = (String) obj.get(GameProperties.IDENTIFIER);
		ent = entMan.spawnComponent(ident);
		((IGameComponent) ent).addInfo("Assembling Component from json...");
		
		Bench.start(BenchConsts.EV_ASSEMBLING+":importJsonComp:3",BenchConsts.CAT_ATOMIC_INIT);
		ent.importFromJSON(obj);
		Bench.end(BenchConsts.EV_ASSEMBLING+":importJsonComp:3");
		Bench.end(BenchConsts.EV_ASSEMBLING+":importJsonComp");
		return ent;
	}

	private Object assembleGenericElement(JSONObject obj) {
		int id = CRJsonUtils.getInteger(obj, GameProperties.ID);
		IGameElement el = em.getElementWithID(id);
		if (el != null) {
			return el;
		}
		Bench.start(BenchConsts.EV_ASSEMBLING+":gameElement",BenchConsts.CAT_ATOMIC_INIT);
		Log.debug("Assembling element with id:" + id);
		el = BPUtils.createGameElement();
		el.importFromJSON(obj);
		Bench.end(BenchConsts.EV_ASSEMBLING+":gameElement");
		return el;
	}

	@Override
	public IManageElements getElementStore() {
		return em;
	}

	@Override
	public Savable importSavable(int entId, String type) {
		Log.throwUnsupported("Not implemented.");
		return null;
	}

	@Override
	public String exportSavable(String entId, String type, Savable node) {
		Log.throwUnsupported("Not implemented.");
		return null;
	}
	@Override
	public String exportSavable(int entId, String type, Savable node) {
		Log.throwUnsupported("Not implemented.");
		return null;
	}

	
	public boolean okToExport(IGameEntity entity) {
		if (entity.containsComponent(GameComps.COMP_TRANSIENT)) {
			return false;
		}
		if (entity instanceof IGameState) {
			return false;
		}
		if (entity instanceof IGameSystem) {
			return false;
		}
		
		if (entity.containsComponent(GameComps.COMP_SPATIAL)){
			if (!entity.containsComponent(GameComps.COMP_RENDER)){
				Log.fatal("Entity has spatial but dont have render: "+entity);
			}
		}
		return true;
	}
}
