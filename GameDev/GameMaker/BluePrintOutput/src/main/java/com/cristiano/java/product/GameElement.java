package com.cristiano.java.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.data.IStoreProperties;
import com.cristiano.java.product.extras.ObjectTypes;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;



public class GameElement implements IGameElement {

	
	private int ID;
	private String objectType = "";
	private List<String> tags = new ArrayList<String>();
	HashMap<Integer, GameParams> paramMap = new HashMap<Integer, GameParams>();
	private IManageElements em;
	private String name;
	GameParams vars = new GameParams();
	private String _lastList;
	private GameParams _lastParamList;

	public GameElement(IManageElements em) {
		this.em = em;
	}

	private GameParams getGameParams(String list) {
		if (list.equals(_lastList)){
			return _lastParamList;
		}
		GameParams pars = paramMap.get(BPUtils.getIntForList(list));
		if (pars == null) {
			pars = new GameParams();
			paramMap.put(BPUtils.getIntForList(list), pars);
		}
		_lastList=list;
		_lastParamList=pars;
		return pars;

	}

	@Override
	public IGameElement getPropertyAsGE(String prop) {
		return getParamAsGE(Extras.LIST_PROPERTY, prop);
	}

	@Override
	public List<IGameElement> getPropertyAsGEList(String property) {
		return getParamAsGEList(Extras.LIST_PROPERTY, property);
	}

	@Override
	public String getParamAsText(String list, String param) {
		return getGameParams(list).getParamAsText(param);
	}

	@Override
	public float getParamAsFloat(String list, String param) {
		return getGameParams(list).getParamAsFloat(param);
	}

	@Override
	public String[] getParamAsList(String list, String param) {
		String value = getParamAsTag(list, param);
		if ("".equals(value)){
			return new String[]{};
		}
		return value.split(" ");
	}

	@Override
	public String getParamAsTag(String list, String param) {
		return StringHelper.removeChaves(getParamAsText(list, param));
	}

	@Override
	public int getParamAsInt(String list, String param) {
		return getGameParams(list).getParamAsInt(param);

	}

	@Override
	public String[] getPropertyAsList(String param) {
		return getParamAsList(Extras.LIST_PROPERTY, param);
	}

	@Override
	public IGameElement getParamAsGE(String list, String identifier) {
		String id = getGameParams(list).getParamAsText(identifier);
		return em.getElementWithID(id);
	}

	@Override
	public boolean hasTag(String tags) {
		if (tags == null)
			return true;
		if (!tags.contains(" ")){
			if (this.tags.contains(tags)){
				return true;
			}
		}
		if (tags.trim().equals("")) {
			return true;
		}
		
		String[] split = tags.split(" ");
		for (String tag : split) {
			if (!this.tags.contains(tag))
				return false;
		}
		return split.length > 0;
	}

	@Override
	public boolean hasAnyTag(String tags) {
		if (tags == null){
			return true;}
		if (!tags.contains(" ")){
			if (this.tags.contains(tags)){
				return true;
			}
		}
		if (tags.trim().equals("")) {
			return true;
		}
		String[] split = tags.split(" ");
		for (String tag : split) {
			if (this.tags.contains(tag)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasTag(String domainProperty, String tag) {
		String propertyTag = getParamAsTag(Extras.LIST_DOMAIN, domainProperty);
		if (!tag.contains(" ")){
			if (this.tags.contains(tag)){
				return true;
			}
		}
		
		String[] split = propertyTag.split(" ");
		for (String txt : split) {
			if (txt.equals(tag)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getIdentifier() {
		return getProperty(Extras.PROPERTY_IDENTIFIER);
	}

	@Override
	public String getName() {
		if (name == null) {
			name = getProperty(GameProperties.NAME);
			if (name == null) {
				name = getIdentifier();
			}
		}
		return name;
	}

	@Override
	public List<IGameElement> getObjectList(String objList, List<IGameElement> list) {
		int i = 0;
		list.clear();
		// GameParams params = getGameParams(Extras.LIST_OBJECT);
		String name = objList + "#" + i;
		IGameElement ge = getParamAsGE(Extras.LIST_OBJECT, name);
		while (ge != null) {
			list.add(ge);
			i++;
			name = objList + "#" + i;
			ge = getParamAsGE(Extras.LIST_OBJECT, name);
		}
		return list;
	}
	
	@Override
	public List<IGameElement> getObjectList(String properties) {
		List<IGameElement> list = new ArrayList<IGameElement>();
		return getObjectList(properties,list);
	}	

	@Override
	public List<IGameElement> getParamAsGEList(String list, String identifier) {
		String value = StringHelper.clear(getGameParams(list).getParamAsText(identifier));
		String[] arr = value.split(",");
		List<IGameElement> ret = new ArrayList<IGameElement>();
		for (String id : arr) {
			IGameElement ge = em.getElementWithID(id);
			if (ge == null) {
				// Log.error("Element null with id:" + id);
			} else {
				ret.add(ge);
			}
		}
		return ret;
	}

	@Override
	public void setObject(String objName, int i, IGameElement obj) {
		setParam(Extras.LIST_OBJECT, objName + "#" + i, obj);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String id() {
		return ObjectTypes.ELEMENT_PREFIX + ID;
	}

	@Override
	public IManageElements getElementManager() {
		return em;
	}

	@Override
	public String getValue() {
		return getProperty(Extras.PROPERTY_VALUE);
	}

	@Override
	public String resolveFunctionOf(String unsolved) {
		return null;
	}

	@Override
	public boolean hasProperty(String property) {
		return getProps().hasProperty(property);
	}

	@Override
	public void setParam(String list, String prop, String value) {
		getGameParams(list).setParam(prop, value);

	}

	@Override
	public void setID(int id) {
		this.ID = id;

	}

	public void addTag(String tag) {
		tag = StringHelper.removeChaves(tag);
		String arrTags[] = tag.split(" ");
		for (int i = 0; i < arrTags.length; i++) {
			String t = arrTags[i].trim();
			if ((!t.equals("")) && (!hasTag(t))) {
				tags.add(t);
			}
		}
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

	@Override
	public String getTagsAsText() {
		String saida = "";
		for (int i = 0; i < tags.size(); i++) {
			saida += tags.get(i) + " ";
		}

		return saida.trim();
	}

	@Override
	public void setParam(String list, String prop, IGameElement el) {
		getGameParams(list).setParam(prop, el);
	}

	@Override
	public void setProperty(String prop, IGameElement val) {
		setParam(Extras.LIST_PROPERTY, prop, val);
	}

	@Override
	public void setName(String val) {
		name = val;
		setProperty(GameProperties.NAME, val);
	}

	@Override
	public Object getParametro(String listProperty, String param) {
		return getGameParams(listProperty).getParamAsText(param);
	}

	@Override
	public String resolveFunctionOf(String function, Object parametro) {
		return null;
	}

	@Override
	public void setVar(String var, int i) {
		vars.setParam(var, i);

	}

	@Override
	public void setVar(String var, float val) {
		vars.setParam(var, val);

	}

	@Override
	public void setVar(String var, String val) {
		vars.setParam(var, val);

	}

	@Override
	public void setProperty(String param, float val) {
		getProps().setParam(param, val);

	}

	@Override
	public float getPropertyAsFloat(String param) {
		return getProps().getParamAsFloat(param);
	}

	private GameParams getProps() {
		return getGameParams(Extras.LIST_PROPERTY);
	}

	@Override
	public void setProperty(String param, int val) {
		getProps().setParam(param, val);

	}

	@Override
	public int getPropertyAsInt(String param) {
		return getProps().getParamAsInt(param);
	}

	@Override
	public void setProperty(String param, String val) {
		getProps().setParam(param, val);

	}

	@Override
	public String getProperty(String param) {
		String paramAsText = getProps().getParamAsText(param);
		return (paramAsText);
	}

	

	@Override
	public void getPropertiesInto(IStoreProperties prop) {
		getProps().applyTo(prop);

	}

	@Override
	public void setProperty(String prop, Object object) {
		getProps().setParam(prop, object.toString());

	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(GameProperties.ID, ID);
		obj.put(GameProperties.OBJECT_TYPE, objectType);
		JSONObject listProps = new JSONObject();

		Iterator<Entry<Integer, GameParams>> it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, GameParams> pairs = it.next();
			Integer key = pairs.getKey();
			GameParams pars = paramMap.get(key);
			listProps.put(BPUtils.getListForInt(key), pars.exportToJSON());
		}
		obj.put(GameProperties.PROPERTIES, listProps);
		obj.put(GameProperties.TAGS, tags);
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject obj) {
		ID = CRJsonUtils.getInteger(obj,GameProperties.ID);
		objectType = (String) obj.get(GameProperties.OBJECT_TYPE);
		JSONObject listProps = (JSONObject) obj.get(GameProperties.PROPERTIES);

		Iterator<String> it = listProps.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			JSONObject paramList = (JSONObject) listProps.get(key);
			importGameParamsFromJSON(key, paramList);
		}
		tags = (ArrayList<String>) obj.get(GameProperties.TAGS);
	}

	private void importGameParamsFromJSON(String list, JSONObject paramList) {
		Iterator<String> it = paramList.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			setParam(list, key, BPUtils.clear(paramList.get(key).toString()));
		}

	}

	@Override
	public String getVar(String var1) {
		return vars.getParamAsText(var1);
	}

	public void setPropertyTag(String prop, String val) {
		setProperty(prop, "{" + StringHelper.removeChaves(val) + "}");

	}

	public String toString() {
		try {
			String s = "@" + Extras.OBJECT_TYPE_GENERIC_ELEMENT + " " + getIdentifier() + "(" + id() + ") MASTERED";
			s += "\n";

			s += paramsToString();

			s += "@end";
			s += getTags();

			return s;
		} catch (Exception e) {
			Log.error("Error converting toString:" + e.getMessage());
			e.printStackTrace();
		}
		return "Erro";
	}

	private String paramsToString() {
		String s = "";
		Iterator<Entry<Integer, GameParams>> it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, GameParams> pairs = it.next();
			Integer key = pairs.getKey();
			GameParams pars = paramMap.get(key);
			s += pars.toString(BPUtils.getListForInt(key));
		}
		return s;
	}

	@Override
	public void setProperty(String prop, boolean b) {
		setParam(Extras.LIST_PROPERTY, prop, b);

	}

	@Override
	public void setParam(String list, String prop, boolean b) {
		setParam(list, prop, b ? 1 : 0);

	}

	@Override
	public boolean getParamAsBoolean(String list, String param) {
		return getGameParams(list).getParamAsInt(param) > 0;
	}

	@Override
	public void setParam(String list, String prop, int v) {
		getGameParams(list).setParam(prop, v);
	}

	@Override
	public boolean getPropertyAsBoolean(String isTerminal) {
		return getParamAsBoolean(Extras.LIST_PROPERTY, isTerminal);
	}

	@Override
	public boolean validate() {
		Iterator<Entry<Integer, GameParams>> it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, GameParams> pairs = it.next();
			Integer key = pairs.getKey();
			GameParams paramList = paramMap.get(key);
			List<String> allKeys = paramList.getAllKeys();
			for (String ident:allKeys){
				
				String value=paramList.getParamAsText(ident);
				if (value.contains("$")){
					Log.error("Element contains invalid value:@"+BPUtils.getListForInt(key)+" "+ident+"="+value);
					return false;
				}
			}
		}
		return true;
	}

	

}
