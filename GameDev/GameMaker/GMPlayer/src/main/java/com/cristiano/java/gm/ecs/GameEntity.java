package com.cristiano.java.gm.ecs;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class GameEntity implements IGameEntity {

	private List<IGameComponent> components = null;
	public List<Integer> componentsIDs = null; //used when importing

	protected IGameElement element;
	protected EntityManager entMan;
	protected IGameFactory factory;

	int id = -1;
	private boolean isActive = true;

	private boolean listed = false;

	public void setEntityManager(EntityManager entMan) {
		this.entMan = entMan;
		this.factory = entMan.getFactory();
		getId();
	}

	@Override
	public void free() {
		components = null;
		element = null;
		isActive = true;
		id = -1;
		removeAllComponents();
	}

	@Override
	public void attachComponent(IGameComponent comp) {
		if (comp == null) {
			return;
		}
		initComponents();
		if (components.contains(comp)) {
			return;
		}
		entMan.link(this, comp.getIdentifier());
		components.add(comp);
		// Log.debug("Attachando componente do tipo "+comp.getIdentifier()+" qtd:"+countsComponent(comp.getIdentifier()));

	}

	private void initComponents() {
		if (components == null) {
			components = new ArrayList<IGameComponent>();
		}
		linkComponents();
	}

	@Override
	public boolean containsComponent(String compClass) {
		if (components == null) {
			return false;
		}
		return (getComponentWithIdentifier(compClass) != null);
	}

	@Override
	public boolean containsComponentWithTag(String compClass) {
		if (components == null) {
			return false;
		}
		return (getComponentWithTag(compClass) != null);
	}

	@Override
	public IGameComponent getComponentWithIdentifier(String compClass) {
		if (components == null) {
			return null;
		}
		int size=components.size();
		for (int i = 0; i < size; i++) {
			IGameComponent iGameComponent = components.get(i);
			if (iGameComponent.getIdentifier().equals(compClass)) {
				return iGameComponent;
			}
		}
		return null;
	}

	@Override
	public boolean containsComponent(IGameComponent comp) {
		if (components == null) {
			return false;
		}
		for (int i = 0; i < components.size(); i++) {
			IGameComponent iGameComponent = components.get(i);
			if (iGameComponent == comp) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IGameComponent getComponentWithTag(String compIdent) {
		if (components == null) {
			return null;
		}
		for (int i = 0; i < components.size(); i++) {
			IGameComponent iGameComponent = components.get(i);
			if (iGameComponent.hasTag(compIdent)) {
				return iGameComponent;
			}
		}
		return null;
	}
	
	@Override
	public List<IGameComponent> getAllComponents() {
		return getAllComponents(new ArrayList<IGameComponent>());
	}
	
	@Override
	public List<IGameComponent> getAllComponents(List<IGameComponent> ret) {
		ret.clear();
		if (components == null) {
			return ret;
		}
		ret.addAll(components);
		return ret;
	}

	@Override
	public List<IGameComponent> getComponents(String compIdent, List<IGameComponent> ret) {
		ret.clear();
		if (components == null) {
			return ret;
		}
		if (compIdent.equals(Extras.TAG_ALL)) {
			ret.addAll(components);
			return ret;
		}

		for (IGameComponent comp : components) {
			if (comp.hasTag(compIdent)) {
				ret.add(comp);
			}
		}
		return ret;
	}

	@Override
	public List<IGameComponent> getComponentsWithIdentifier(String compIdent) {
		List<IGameComponent> ret = new ArrayList<IGameComponent>();
		return getComponentsWithIdentifier(compIdent, ret);
	}

	@Override
	public List<IGameComponent> getComponentsWithIdentifier(String compIdent, List<IGameComponent> ret) {
		ret.clear();
		if (components == null) {
			return ret;
		}

		for (IGameComponent comp : components) {
			if (comp.getIdentifier().equals(compIdent)) {
				ret.add(comp);
			}
		}
		return ret;
	}

	@Override
	public List<IGameComponent> getComponents(String compIdent) {
		List<IGameComponent> ret = new ArrayList<IGameComponent>();

		return getComponents(compIdent, ret);
	}

	@Override
	public void removeComponent(IGameComponent comp) {
		if (components == null) {
			return;
		}
		if (comp == null) {
			return;
		}
		// Log.debug("Removing component:"+comp);
		//TODO: remover isso
		if (comp.hasTag(GameComps.TAG_VICTORY_CHECKER_COMPONENTS)){
			Log.debug("Removendo check");
		}

		components.remove(comp);
		String identifier = comp.getIdentifier();
		if (!containsComponent(identifier)) {
			entMan.unlink(this, identifier);
		}
		entMan.checkRemoval(comp);
	}

	@Override
	public void removeComponent(String comp) {
		if (components == null) {
			return;
		}
		for (int i = components.size() - 1; i >= 0; i--) {
			IGameComponent iGameComponent = components.get(i);
			if (iGameComponent.hasTag(comp)) {
				removeComponent(iGameComponent);
			}
		}
	}

	@Override
	public IGameElement getElement() {
		return element;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		this.element = ge;
		if (ge == null) {
			Log.fatal("Entity element is null: " + this);
		}
		if (!ge.validate()) {
			Log.fatal("Entity element is invalid:" + ge);
		}
	}

	public void setElement(IGameElement element) {
		this.element = element;
	}

	@Override
	public int countsComponent(String ident) {
		if (components == null) {
			return 0;
		}
		int tot = 0;
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).getIdentifier().equals(ident)) {
				tot++;
			}
		}
		return tot;
	}

	public String toString() {
		String s = "GameEntity";
		String player = "";
		if (containsComponent(GameComps.COMP_PLAYER)) {
			player = ":PLAYER";
		}
		if (containsComponent(GameComps.COMP_WORLD)) {
			player = ":WORLD";
		}
		if (containsComponent(GameComps.COMP_AI)) {
			player = ":AI";
		}
		if (element != null) {
			s += ":" + element.getIdentifier();
		}
		return getId() + ":" + s + player;
	}

	@Override
	public int getId() {
		if (id < 0) {
			if (entMan != null) {
				id = entMan.generateID();
			} else {
				Log.fatal("No entitymanager defined!");
			}
		}
		return id;
	}

	@Override
	public void removeAllComponents() {
		if (components == null) {
			return;
		}
		for (int i = components.size() - 1; i >= 0; i--) {
			IGameComponent comp = components.get(i);
			entMan.unlink(this, comp.getIdentifier());
			components.remove(i);
			entMan.checkRemoval(comp);
		}

	}

	@Override
	public boolean hasTag(String compIdent) {
		if (compIdent.equals(Extras.TAG_ALL)) {
			return true;
		}
		if (element == null) {
			// Log.warn("Entity has no element attached.");
			return false;
		}
		return element.hasTag(compIdent);

	}

	@Override
	public void deactivate() {
		isActive = false;
	}

	@Override
	public void activate() {
		isActive = true;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void resetComponents() {
		if (components == null) {
			return;
		}
		for (IGameComponent comp : components) {
			comp.resetComponent();
		}

	}

	@Override
	public void attachComponents(List<IGameComponent> components) {
		for (IGameComponent comp : components) {
			attachComponent(comp);
		}

	}

	public int size() {
		linkComponents();
		if (components == null) {
			return 0;
		}
		return components.size();
	}

	@Override
	public void setProperty(String prop, String value) {
		element.setProperty(prop, value);
	}

	@Override
	public void setProperty(String prop, int value) {
		element.setProperty(prop, value);
	}

	@Override
	public void setProperty(String prop, float value) {
		element.setProperty(prop, value);
	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(GameProperties.OBJECT_TYPE, JavaConsts.OBJECT_GAME_ENTITY);
		obj.put(GameProperties.ID, getId());
		
		if (element != null) {
			JSONObject elJson = element.exportToJSON();
			obj.put(GameProperties.ELEMENT, element.getId());
			if (CRJavaUtils.ALLWAYS_EXPORT_ELEMENTS) {
				obj.put(GameProperties.ELEMENT_SOURCE, elJson);
			}
		}
		if (components != null) {
			for (IGameComponent comp : components) {
				comp.setEntityManager(entMan);
			}
		}
		JSONObject exportComps = exportComponents();// CRJsonUtils.exportList(components);
		obj.put(GameProperties.COMPONENTS, exportComps);
		// Log.debug(this+" was exported.");
		return obj;
	}

	private JSONObject exportComponents() {
		JSONObject obj = new JSONObject();
		if (components == null) {
			return obj;
		}
		for (int i = 0; i < components.size(); i++) {
			IGameEntity el = components.get(i);
			//JSONObject exportToJSON = el.exportToJSON();
			// exportToJSON = el.exportToJSON();
			//if (exportToJSON != null) {
				if (factory.okToExport(el)) {
					obj.put("v"+Integer.toString(i), el.getId());
					entMan.addEntityToExport(el);
				}
			//}
		}
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		Bench.start(BenchConsts.EV_ASSEMBLING + ":importFromJSON", BenchConsts.CAT_ATOMIC_INIT);
		importElement(json);

		id = CRJsonUtils.getInteger(json, GameProperties.ID);
		entMan.checkID(id);

		Bench.start(BenchConsts.EV_ASSEMBLING + ":importFromJSON:3", BenchConsts.CAT_ATOMIC_INIT);
		componentsIDs=CRJsonUtils.importIntegerList(json,GameProperties.COMPONENTS);
		//components = CRJsonUtils.importList((JSONObject) json.get(GameProperties.PROPERTIES), factory);
		
		Bench.end(BenchConsts.EV_ASSEMBLING + ":importFromJSON:3");

		Bench.end(BenchConsts.EV_ASSEMBLING + ":importFromJSON");
		// Log.debug(this+" was imported.");
	}

	private void importElement(JSONObject json) {
		Object objID = json.get(GameProperties.ELEMENT);
		if (objID != null) {
			String elID = objID.toString();
			IManageElements elementStore = factory.getElementStore();
			element = (IGameElement) elementStore.getElementWithID(Integer.parseInt(elID));
			if (element == null) {
				JSONObject elementSource = (JSONObject) json.get(GameProperties.ELEMENT_SOURCE);
				if (elementSource != null) {
					int ID = CRJsonUtils.getInteger(elementSource, GameProperties.ID);
					elementStore.removeElementWithID(ID);
					element = elementStore.importElementFromJSON(elementSource);
				}
				if (element == null) {
					Log.error("No element with ID:" + elID + " was found!");
				}
			}
		}
	}

	public void setId(int i) {
		this.id = i;

	}

	@Override
	public boolean isComponent() {
		return false;
	}

	@Override
	public void setListed() {
		listed = true;
		if (components != null) {
			int size = components.size();
			for (int i = 0; i < size; i++) {
				IGameComponent comp = components.get(i);
				entMan.link(this, comp.getIdentifier());
			}
		}
	}

	@Override
	public void setUnlisted() {
		if (components != null) {
			int size = components.size();
			for (int i = 0; i < size; i++) {
				IGameComponent comp = components.get(i);
				entMan.unlink(this, comp.getIdentifier());
			}
		}
		listed = false;
	}

	@Override
	public boolean isListed() {
		return listed;
	}
	
	@Override
	public void removeComponentSimple(IGameComponent comp) {
		if (components!=null){
			components.remove(comp);
		}
	}
	
	@Override
	public void report(List<IGameEntity> alreadyShown,String spaces) {
		if (alreadyShown.contains(this)){
			return;
		}
		alreadyShown.add(this);
		if (components==null){return;}
		Log.info(spaces+"+ "+this);
		int size = components.size();
		for (int i = 0; i < size; i++) {
			IGameComponent comp = components.get(i);
			comp.report(alreadyShown, spaces+"+--");
		}
		
	}

	public void linkComponents() {
		if (componentsIDs==null){
			return;
		}
		List<Integer> ids = componentsIDs;
		componentsIDs = null;
		for (Integer entId : ids) {
			IGameEntity comp = entMan.getEntityWithId(entId);
			if (comp == null) {
				Log.error("No entity with id " + entId + " found.");
			} else {
				attachComponent((IGameComponent) comp);
				entMan.link(this, ((IGameComponent) comp).getIdentifier());
			}

		}
	}
}
