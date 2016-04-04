package com.cristiano.java.bpM.entidade;

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
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.functions.Solver;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.bpM.params.ParamListHolder;
import com.cristiano.java.bpM.utils.FunctionHelper;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

/*
 * 
 * Objeto base para GameElement e outros objetos que usam listas de propriedades.
 * 
 */
public abstract class AbstractElement implements IGameElement {
	private ParamListHolder params;
	private Solver functionManager;
	private ArrayList<String> tags;
	protected ElementManager elementManager;
	protected int ID = 0;

	private String objectType = "undefined";
	public String debugInfo = "";

	public AbstractElement() {
		params = new ParamListHolder(this);
		functionManager = new Solver(this);
		tags = new ArrayList<String>();

	}

	public void refresh() {

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

	public void resolveFuncoes(boolean solveDependent) {
		getParams().resolveFuncoes(solveDependent);
	}

	public void serializa() {
		getParams().serializaParams();
	}

	public IGameElement getPropertyAsGE(String param) {
		return getParamAsGE(Extras.LIST_PROPERTY, param);
	}

	public IGameElement getParamAsGE(String lista, String param) {
		String id = resolveFunctionOf(getParamAsTag(lista, param));
		return elementManager.getElementWithID(id);
	}

	public boolean hasTag(String tags) {
		if (tags == null)
			return true;
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
	public boolean hasTag(String domainProperty, String tag) {
		String propertyTag = getParamAsTag(Extras.LIST_DOMAIN, domainProperty);
		String[] split = propertyTag.split(" ");
		for (String txt : split) {
			if (txt.equals(tag)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasAnyTag(String tags) {
		tags = StringHelper.removeChaves(tags);
		String[] split = tags.split(" ");
		for (String tag : split) {
			if (hasTag(tag))
				return true;
		}
		return false;
	}

	public boolean hasKeyword(String keyword) {
		String keywords = getParamAsTag(Extras.LIST_DOMAIN,
				Extras.DOMAIN_KEYWORD);
		String[] splitKeywords = keywords.split(" ");
		String[] splitCheck = keyword.split(" ");
		for (int j = 0; j < splitCheck.length; j++) {
			for (int i = 0; i < splitKeywords.length; i++) {
				if (splitCheck[j].equals(splitKeywords[i])) {
					return true;
				}
			}
		}

		return false;
	}

	public void removeTag(String tag) {
		tags.remove(tag);
		if (elementManager != null)
			elementManager.removeElementFromTag(this, tag);
	}

	public void setVar(String var, String val) {
		functionManager.setVar(var, val);
	}

	public void setVar(String var, int val) {
		functionManager.setVar(var, Integer.toString(val));
	}

	public void setVar(String var, float val) {
		functionManager.setVar(var, Float.toString(val));

	}

	public String getVar(String var) {
		return functionManager.getVar(var);
	}

	public void setParam(String param) {
		validaParametroELancaExcecao(param);
		params.addParam(param);
	}

	public void setParam(String list, Parametro value) {
		setParam(list, value.getIdentifier() + " " + value.getModificador()
				+ " " + value.getValue());
	}

	public String getTagsAsText() {
		String saida = "";
		for (int i = 0; i < tags.size(); i++) {
			saida += tags.get(i) + " ";
		}

		return saida.trim();
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public void setParam(String list, String param) {
		validaParametroELancaExcecao(param);
		setParam("@" + list + " " + param);
	}

	public boolean hasProperty(String property) {
		return (params.hasParam(Extras.LIST_PROPERTY, property));
	}

	public boolean hasParam(String list, String property) {
		return (params.hasParam(list, property));
	}

	public void setParam(String list, String param, String val) {
		setParam("@" + list + " " + param + " = " + val);
	}

	public void setProperty(String param) {
		setParam(Extras.LIST_PROPERTY, param);
	}

	public void setProperty(String param, float val) {
		setProperty(param, Float.toString(val));
	}

	@Override
	public void setProperty(String param, int val) {
		setProperty(param, Integer.toString(val));

	}

	public void setProperty(String param, String val) {
		setParam(Extras.LIST_PROPERTY, param + " = " + val);
	}

	public void unsetParam(String list, String identifier) {
		params.getListWithKey(list).removeParam(identifier);

	}

	public void setDomain(String param) {
		setParam(Extras.LIST_DOMAIN, param);
	}

	public void setDomain(String param, String val) {
		setParam(Extras.LIST_DOMAIN, param, val);
	}

	private void validaParametroELancaExcecao(String param) {
		if (!param.contains("=")) {
			Log.error("Parameter doesnt contains a valid modifier (=,+=,*=,etc):"
					+ param);
		}
	}

	public Parametro getParametro(String list, String param) {
		return params.getParamFrom(list, param);
	}

	public String getParamAsText(String list, String param) {
		Parametro pb = getParametro(list, param);
		if (pb == null) {
			return "";
		}

		return pb.getValue().replace("'", "").trim();
	}

	public int getPropertyAsInt(String prop) {
		return getParamAsInt(Extras.LIST_PROPERTY, prop);
	}

	public float getPropertyAsFloat(String prop) {
		return getParamAsFloat(Extras.LIST_PROPERTY, prop);
	}

	public float getParamAsFloat(String list, String chavePropriedade) {
		String par = resolveFunctionOf(getParamAsText(list, chavePropriedade));
		par = par.replace(" ", "");
		if ("".equals(par)) {
			return 0;
		}
		return new Float(Float.parseFloat(par));
	}

	public int getParamAsInt(String list, String chavePropriedade) {
		String par = resolveFunctionOf(getParamAsText(list, chavePropriedade));
		par = par.replace(" ", "").replace("'", "");
		if ("".equals(par)) {
			return 0;
		}
		return Integer.parseInt(par);
	}

	public String getParamAsTag(String list, String param) {
		return StringHelper.removeChaves(getParamAsText(list, param));
	}

	// retorn o parametro de forma hierarquica... por exemplo, se o atual define
	// prop+=1 e o pai tem prop=1 tem o retorno � 1+1
	public String getParamH(String list, String param, boolean solved) {
		return params.getParamHAsString(list, param, solved);
	}

	public String getPropertyH(String param, boolean solved) {
		return getParamH(Extras.LIST_PROPERTY, param, solved);
	}

	public String getPropertyHAsText(String param, boolean solved) {
		return getPropertyH(param, solved).replace("\"", "").replace("'", "");
	}

	public String getProperty(String prop) {
		return getParamAsText(Extras.LIST_PROPERTY, prop);
	}

	public String getProperty(String prop, boolean clean) {
		String val = getProperty(prop);
		if (clean) {
			val = StringHelper.clear(val);
		}
		return val;
	}

	// retorna uma lista com todos as chaves de parametros na paramList
	// informada
	public List<String> getAllParams(String listName, boolean hierarquico) {

		List<String> parms;
		if ((getEstende() != null) && (hierarquico)) {
			parms = ((AbstractElement) getEstende()).getAllParams(listName,
					true);
		} else {
			parms = new ArrayList<String>();
		}

		List<String> thisParms = params.getListWithKey(listName).getAllKeys();
		for (int i = 0; i < thisParms.size(); i++) {
			if (!parms.contains(thisParms.get(i))) {
				parms.add(thisParms.get(i));
			}
		}

		return parms;
	}

	public ParamListHolder getParams() {
		return params;
	}

	public IGameElement getCreator() {
		return getParamAsGE(Extras.LIST_PROPERTY, Extras.PROPERTY_CREATED_BY);
	}

	public void setCreator(IGameElement iGameElement) {
		setParam("@" + Extras.LIST_PROPERTY + " " + Extras.PROPERTY_CREATED_BY
				+ " = [" + iGameElement.id() + "]");
	}

	public String getName() {
		return getPropertyH(Extras.PROPERTY_NAME, true).replace("'", "");
	}

	public String getValue() {
		return getPropertyH(Extras.PROPERTY_VALUE, true).replace("'", "");
	}

	public String getIdentifier() {
		return getProperty(Extras.PROPERTY_IDENTIFIER);
	}

	public void setName(String name) {
		setProperty(Extras.PROPERTY_NAME + "='" + name + "'");
	}

	public void removeProperty(String propertyIdentifier) {
		ParamList list = getParams().getListWithKey(Extras.LIST_PROPERTY);
		list.removeParam(propertyIdentifier);
	}

	public String resolveFunctionOf(String value) {
		return functionManager.resolveFunctionOf(value);
	}

	public String resolveFunctionOf(String value, Object paramSource) {
		return functionManager
				.resolveFunctionOf(value, (Parametro) paramSource);
	}

	// metodos que devem ser implementados
	public abstract IGameElement getEstende();

	public int getId() {
		return ID;
	}

	public String id() {
		return "TEMPL" + ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public abstract boolean isMastered();

	public Solver getFunctionManager() {
		return functionManager;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<String> getIDFilhos() {
		ParamList plObject = getParams().getListWithKey(Extras.LIST_OBJECT);
		List<String> ret = new ArrayList<String>();
		List<String> allKeys = plObject.getAllKeys();
		for (int i = 0; i < allKeys.size(); i++) {
			// s� quero propriedades sem . (podem ser atributos)
			if (!allKeys.get(i).contains(".")) {
				ret.add(allKeys.get(i));
			}
		}
		return ret;
	}

	// Exemplo, se usar "obj1" e tiver definido "@object obj1=[TEMPL12]" ent�o o
	// retorno ser� o TEMPL12 devolvido como objeto
	public IGameElement getChildFromParam(String param) {
		return getParamAsGE(Extras.LIST_OBJECT, param);
	}

	public String toString() {
		try {
			String s = "@" + getObjectType() + " " + getIdentifier() + "("
					+ id() + ")";
			if (getEstende() != null) {
				s += " : " + getEstende().getIdentifier() + "("
						+ getEstende().id() + ")";
			}
			if (isMastered()) {
				s += " MASTERED";
			}
			s += "\n";

			s += getParams().toString();

			s += "@end";
			s += getTags();

			return s;
		} catch (Exception e) {
			Log.error("Error converting toString:" + e.getMessage());
			e.printStackTrace();
		}
		return "Erro";
	}

	public String export() {
		String s = "@" + getObjectType() + " " + getIdentifier();
		if (getEstende() != null) {
			s += ":" + getEstende().getIdentifier();
		}
		s += "\n";
		s += getParams().toString();
		s += "@end";
		return s;
	}

	public boolean checkFunctionSolvable(String param) {
		return getFunctionManager().checkFunctionSolvable(param);
	}

	public boolean isValid() {
		ParamList listValidations = getParams().getListWithKey(
				Extras.LIST_VALIDATE);
		for (String key : listValidations.getAllKeys()) {
			Parametro param = listValidations.getParamBlueprintFor(key);
			String[] elementosFuncao = StringHelper.getElementosFuncao(param
					.getValue());
			boolean b = FunctionHelper.validateFunctions(this, elementosFuncao);
			if (!b) {
				Log.error("Parameter didn't validate:" + param + " at:"
						+ this.getIdentifier());
				return false;
			}
		}
		return true;
	}

	public void setEntity() {
		setParam("@" + Extras.LIST_ENTITY + " " + Extras.ENTITY_ISENTITY
				+ " = [" + this.id() + "]");
	}

	public boolean isEntity() {
		String s = getParamAsText(Extras.LIST_ENTITY, Extras.ENTITY_ISENTITY);
		return (!s.equals(""));
	}

	public AbstractElement getEntity() {
		String s = getParamAsText(Extras.LIST_ENTITY, Extras.ENTITY_ISENTITY);
		String id = "";
		if (s.equals("")) {

			IGameElement creator = getCreator();
			if (creator != null) {
				return ((AbstractElement) creator).getEntity();
			}
		}
		return this;

	}

	// retorna uma lista de GE de uma lista de IDs: [BP1,BP2,BP3]
	public List<IGameElement> getParamAsGEList(String lista, String param) {
		List<IGameElement> ret = new ArrayList<IGameElement>();
		String items = resolveFunctionOf(getParamAsTag(lista, param));
		items = items.replace(" ", "").replace("'", "").replace("][", "],[");

		StringHelper.concatenaLista("", items);
		items = StringHelper.removeColchetes(items);
		String[] split = items.split(",");
		for (String id : split) {
			IGameElement genericElement = elementManager.getElementWithID(id);
			if (genericElement != null) {
				ret.add(genericElement);
			}
		}

		return ret;
	}

	public String[] getParamAsList(String list, String param) {
		String value = getParamAsTag(list, param);
		return value.split(" ");
	}

	public String[] getPropertyAsList(String param) {
		String value = getParamAsTag(Extras.LIST_PROPERTY, param);
		String[] split = value.split(" ");
		return split;
	}

	public List<IGameElement> getPropertyAsGEList(String param) {
		return getParamAsGEList(Extras.LIST_PROPERTY, param);
	}

	@Override
	public List<IGameElement> getObjectList(String objName,List<IGameElement> ret) {
		int c = 0;
		ret.clear();
		IGameElement ge = null;
		do {
			ge = getParamAsGE(Extras.LIST_OBJECT, objName + "#" + c);
			if (ge != null) {
				ret.add(ge);
			}
			c++;
		} while (ge != null);
		ge = getParamAsGE(Extras.LIST_OBJECT, objName);
		if (ge != null) {
			ret.add(ge);
		}
		return ret;
	}
	
	@Override
	public List<IGameElement> getObjectList(String properties) {
		List<IGameElement> list = new ArrayList<IGameElement>();
		return getObjectList(properties,list);
	}	

	@Override
	public void setObject(String objName, int i, IGameElement obj) {
		setParam(Extras.LIST_OBJECT, objName + "#" + i, obj);
	}

	public List<IGameElement> getObjectList(String sourceList, String withTag) {
		List<IGameElement> list = getObjectList(sourceList);
		for (int i = list.size() - 1; i >= 0; i--) {
			IGameElement ge = list.get(i);
			if (!ge.hasTag(withTag)) {
				list.remove(i);
			}
		}

		return list;
	}

	public ElementManager getElementManager() {
		return elementManager;
	}

	public ParamList getListWithKey(String param) {
		return getParams().getListWithKey(param);
	}

	public boolean getPropertyAsBoolean(String prop) {
		return "1".equals(getProperty(prop));
	}

	public void setPropertyTag(String prop, String val) {
		setProperty(prop, "{" + StringHelper.removeChaves(val) + "}");

	}

	public void applyInlineProperties(String pars) {

		String[] parms = StringHelper.getParams(pars);
		for (int i = 0; i < parms.length; i++) {
			try {
				String par = parms[i];
				String prop = par.substring(0, par.indexOf("="));
				String val = par.substring(par.indexOf("=") + 1);
				String list=Extras.LIST_PROPERTY;
				if (prop.contains("#")){
					list=prop.substring(0,prop.indexOf("#"));
					prop=prop.substring(prop.indexOf("#")+1);
				}
				setParam(list,prop, resolveFunctionOf(val));
			} catch (Exception e) {
				Log.error("Error applying properties:" + parms[i]);
				e.printStackTrace();
			}
		}

	}

	public String getDebugInfo() {
		return debugInfo;
	}

	public void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	public void setProperty(String param, IGameElement elParam) {
		setProperty(param, "[" + elParam.id() + "]");
	}

	public void setParam(String list, String param, IGameElement elParam) {
		setParam(list, param, "[" + elParam.id() + "]");

	}

	public void getPropertiesInto(IStoreProperties propTo) {
		List<String> allParams = getAllParams(Extras.LIST_PROPERTY, true);

		for (String prop : allParams) {
			propTo.setProperty(prop, getProperty(prop));
		}

	}

	public void setProperty(String param, Object val) {
		setProperty(param, val.toString());
	}

	@Override
	public JSONObject exportToJSON() {
		String objectType = getObjectType();
		if (!this.isMastered()) {
			// Log.warn("Trying to export an unsmastered element:"+this.getIdentifier()+" objectType:"+getObjectType());
			if (getObjectType() == Extras.OBJECT_TYPE_MOD) {
				return null;
			}
			objectType = Extras.OBJECT_TYPE_GENERIC_ELEMENT;
		}
		JSONObject obj = new JSONObject();
		obj.put(GameProperties.ID, ID);
		obj.put(GameProperties.STR_ID, id());
		obj.put(GameProperties.OBJECT_TYPE, objectType);
		JSONObject listProps = new JSONObject();

		HashMap<String, ParamList> listsH = getParams().getListsH();
		Iterator<Entry<String, ParamList>> it = listsH.entrySet().iterator();
		getElementManager().addToExportList(this);
		while (it.hasNext()) {
			Entry<String, ParamList> pairs = it.next();
			String key = pairs.getKey();
			ParamList paramList = listsH.get(key);
			if (paramList.size() > 0) {
				if (isListExportable(key)) {
					listProps.put(key, paramList.exportToJSON());
				}
			}

		}
		obj.put(GameProperties.PROPERTIES, listProps);
		obj.put(GameProperties.TAGS, tags);
		return obj;
	}

	private boolean isListExportable(String listName) {
		String[] notExportable = new String[] { Extras.LIST_CHILD_PROPERTY,
				Extras.LIST_COMMENT, Extras.LIST_POSITION,
				Extras.LIST_ORIENTATION, Extras.LIST_REPLICATE,
				Extras.LIST_MESH };
		for (String l : notExportable) {
			if (listName.equals(l)) {
				return false;
			}
		}

		return true;
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
			importParamsFromJSON(key, paramList);
		}
		tags = (ArrayList<String>) obj.get(GameProperties.TAGS);
	}

	private void importParamsFromJSON(String list, JSONObject paramList) {
		Iterator<String> it = paramList.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			setParam(list, key, (String) paramList.get(key));
		}

	}

	@Override
	public void setProperty(String prop, boolean b) {
		setParam(Extras.LIST_PROPERTY, prop + " = " + (b ? 1 : 0));

	}

	@Override
	public boolean getParamAsBoolean(String list, String param) {
		return "1".equals(getParamAsText(list, param));
	}

	@Override
	public void setParam(String list, String prop, boolean b) {
		setParam(list, prop + " = " + (b ? 1 : 0));

	}

	@Override
	public void setParam(String list, String prop, int v) {
		setParam(list, prop + " = " + v);
	}

	@Override
	public boolean validate() {
		HashMap<String, ParamList> listsH = getParams().getLists();
		Iterator<Entry<String, ParamList>> it = listsH.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ParamList> pairs = it.next();
			String key = pairs.getKey();
			ParamList paramList = listsH.get(key);
			List<String> allKeys = paramList.getAllKeys();
			for (String ident : allKeys) {
				Parametro param = paramList.getParamBlueprintFor(ident);
				String value = param.getValue();
				if (value.contains("$")) {
					value = resolveFunctionOf(value);
					param.setValue(value);
					if (value.contains("$")) {
						Log.error("Element contains invalid value:@" + key
								+ " " + ident + "=" + value);
						
						return false;
					}
				}
			}
		}
		return true;
	}
}
