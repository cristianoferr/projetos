package com.cristiano.java.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.product.extras.ObjectTypes;
import com.cristiano.utils.Log;
/*
 * This is a "product" element store, used by the final product to mostly retrieve generated elements.
 * 
 * */
public class ElementStore implements IManageElements {

	Map<String, String> vars = new HashMap<String, String>();
	List<IGameElement> elements = new ArrayList<IGameElement>();
	Map<String, IGameElement> elementMap = new HashMap<String, IGameElement>();

	@Override
	public String getVar(String var) {
		return vars.get(var);
	}

	@Override
	public void setVar(String var, String val) {
		vars.put(var, val);
	}

	public boolean containsElement(IGameElement ge) {
		return elements.contains(ge);
	}

	@Override
	public IGameElement getElementWithID(int id) {
		return getElementWithID(ObjectTypes.ELEMENT_PREFIX+id);
	}

	@Override
	public IGameElement getElementWithID(String id) {
		if ("".equals(id)) {
			return null;
		}
		IGameElement el = elementMap.get(id);
		return el;
	}

	@Override
	public IGameElement pickFinal(String tag) {
		//Bench: quase desprezivel: 9.75 ms por chamada
		for (IGameElement el : elements) {
			if (el.hasTag(tag)) {
				return el;
			}
		}
		Log.error("No PickFinal found for: " + tag);
		return null;
	}

	@Override
	public IGameElement pickFinal(String tag, IGameElement creator) {
		return pickFinal(tag);
	}

	@Override
	public void addElement(IGameElement el) {
		if (elements.contains(el)){
			return;
		}
		elements.add(el);
		el.setID(elements.size());
		elementMap.put(el.id(), el);
	}

	public IGameElement createElement() {
		IGameElement el = new GameElement(this);
		// elements.add(el);
		addElement(el);
		return el;
	}

	@Override
	public IGameElement importElementFromJSON(JSONObject elJson) {
		//long ID = CRJsonUtils.getInteger(elJson, GameProperties.ID);
		String strID=(String) elJson.get(GameProperties.STR_ID);

		IGameElement el = getElementWithID(strID);
		if (el != null) {
			return el;
		}
		el = new GameElement(this);
		el.importFromJSON(elJson);
		// Log.debug("Importing Element:"+el.getId());
		elements.add(el);
		elementMap.put(el.id(), el);
		return el;
	}

	@Override
	public void addToExportList(IGameElement element) {
		Log.throwUnsupported("ElementStore dont export...");
	}

	@Override
	public void importFromJSON(JSONObject json) {
		// elements=CRJsonUtils.importList(json.get(GameProperties.ELEMENT),
		// factory);
		if (json==null){
			Log.fatal("ELementStore Json is null!");
		}

		JSONObject object = (JSONObject) json.get(GameProperties.ELEMENT);
		Iterator<String> it = object.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			JSONObject item = (JSONObject) object.get(key);
			importElementFromJSON(item);
			// IGameElement el = createElement();
			// el.importFromJSON(item);
		}

	}

	@Override
	public JSONObject exportToJSON() {
		Log.throwUnsupported("ElementStore dont export...");
		return null;
	}

	@Override
	public void removeElementWithID(int id) {
		removeElement(getElementWithID(id));

	}

	public void removeElement(IGameElement ge) {
		if (ge == null) {
			return;
		}
		Log.debug("removendo elemento " + ge.id());
		elements.remove(ge);
		elementMap.remove(ge.id());
	}

	@Override
	public void clear() {
		elements.clear();
	}
}
