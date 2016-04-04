package com.cristiano.java.bpM.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;

public class ParamListHolder {

	AbstractElement owner;
	private HashMap<String, ParamList> lists;

	public ParamListHolder(AbstractElement propertyObject) {
		this.owner = propertyObject;
		lists=new HashMap<String, ParamList>();
	}

	public boolean hasParam(String list, String property) {
		ParamList pl = getListWithKey(list);
		return (pl.hasParam(property));

	}

	public void replaceWith(String text) {
		getLists().clear();
		String[] lines = text.split("\n");
		for (String line : lines) {
			addParam(line.trim());
		}

	}

	public void addParam(String param) {
		param = param.trim();
		// String listName = getListName(param);

		ParamList pl = getListWithKey(param);

		pl.setParam(removeListName(param));
	}

	// entra ('@property name = "Worn Spear"' sai 'name = "Worn Spear"')
	private String removeListName(String param) {
		String list = "@" + getListName(param);
		param = param.replace(list, "");
		return param.trim();
	}

	public ParamList getListWithKey(String param) {
		String set = getListName(param);

		if (getLists().containsKey(set)) {
			return getLists().get(set);
		}
		ParamList pl = new ParamList(this);
		getLists().put(set, pl);
		return pl;
	}

	private String getListName(String param) {
		param = param.trim();
		if (param.startsWith("@"))
			param = param.substring(1).trim();
		if (param.contains(" "))
			param = param.substring(0, param.indexOf(" "));
		return param.trim();
	}

	public String toString() {
		String saida = "";
		Iterator it = getLists().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			saida += getLists().get(pairs.getKey()).toString("  @" + pairs.getKey() + " ");
		}

		return saida;
	}

	public Parametro getParamFrom(String list, String paramName) {
		ParamList pl = getListWithKey(list);
		return pl.getParamBlueprintFor(paramName);
	}

	public Parametro getParamFrom(String param) {
		String list = getListName(param);
		return getParamFrom(list, removeListName(param));
	}

	public String getParamHAsString(String list, String param, boolean solved) {
		Parametro pb = getParamFrom(list, param);
		if (pb == null) {
			if (getExtends() != null) {
				return getExtendsParams().getParamHAsString(list, param, solved);
			} else {
				return "";
			}
		}

		String value = pb.getValue();

		value = aplicaModificadores(list, param, solved, pb, value);

		if (solved){
			value = resolveFunctionOf(value);}
		return value;

	}

	public String aplicaModificadores(String list, String param, boolean solved, Parametro pb, String value) {
		if (pb.getModificador().equals("+=")) {
			String addTo = "";
			if (getExtends() != null) {
				addTo = getExtendsParams().getParamHAsString(list, param, solved);
			}
			if (addTo != "") {
				value = addTo + "+" + value;
			}
		}

		if (pb.getModificador().equals("*=")) {
			String addTo = "";
			if (getExtends() != null) {
				addTo = getExtendsParams().getParamHAsString(list, param, solved);
			}
			if (addTo != "") {
				value = addTo + "*" + value;
			}
		}

		if (pb.getModificador().equals("-=")) {
			String addTo = "";
			if (getExtends() != null) {
				addTo = getExtendsParams().getParamHAsString(list, param, solved);
			}
			if (addTo != "") {
				value = addTo + "-" + value;
			}
		}
		return value;
	}

	private String resolveFunctionOf(String value) {
		return getOwner().resolveFunctionOf(value);
	}

	private ParamListHolder getExtendsParams() {
		if (getExtends() == null)
			return null;
		return ((AbstractElement) getExtends()).getParams();
	}

	private IGameElement getExtends() {
		return owner.getEstende();
	}

	// concretizaParams= pega os parametros da blueprint hierarquicamente e
	// aplica a fun��o resolveFunction para cada propriedade
	public void serializaParams() {

		HashMap<String, ParamList> arrLists = getListsH();

		ArrayList<String> arrKeys = getKeys(arrLists);
		for (int i = 0; i < arrKeys.size(); i++) {
			ParamList pl = arrLists.get(arrKeys.get(i));
			pl.serializa(arrKeys.get(i), this);
		}
	}

	public HashMap<String, ParamList> getListsH() {
		HashMap<String, ParamList> listsResult;
		if (getExtends() != null) {
			listsResult = ((AbstractElement) getExtends()).getParams().getListsH();
		} else {
			listsResult = new HashMap<String, ParamList>();
		}

		ArrayList<String> arrKeys = getKeys();
		for (int i = 0; i < arrKeys.size(); i++) {
			listsResult.put(arrKeys.get(i), getLists().get(arrKeys.get(i)));
		}

		return listsResult;
	}

	public ArrayList<String> getKeys() {
		return getKeys(getLists());
	}

	public ArrayList<String> getKeysH() {
		return getKeys(getListsH());
	}

	public ArrayList<String> getKeys(HashMap<String, ParamList> arrLists) {
		ArrayList<String> ret = new ArrayList<String>();
		Iterator it = arrLists.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			// System.out.println(pairs.getKey() + " = " + pairs.getValue());
			// saida+=lists.get(pairs.getKey()).toString("@"+pairs.getKey()+" ");
			ret.add(pairs.getKey().toString());
		}

		// moving defines to last position...
		if (ret.contains(Extras.LIST_DEFINE)) {
			ret.remove(Extras.LIST_DEFINE);
			ret.add(Extras.LIST_DEFINE);
		}

		return ret;
	}

	public void removeList(String prefixReplicate) {
		getLists().remove(prefixReplicate);
	}

	public AbstractElement getOwner() {
		return owner;
	}

	public void removeParam(String prefixProperty, String identifier) {
		getListWithKey(prefixProperty).removeParam(identifier);
	}

	public void resolveFuncoes(boolean solveDependent) {
		ArrayList<String> keys = getKeys();
		ParamList list = getListWithKey(Extras.LIST_PROPERTY);
		list.resolveFuncoes(solveDependent);
		for (String key : keys) {
			list = getListWithKey(key);
			list.resolveFuncoes(solveDependent);
		}

	}

	public void addDefine(String ident, String saida) {
		getOwner().getElementManager().setVar(ident, resolveFunctionOf(saida));

	}

	public HashMap<String, ParamList> getLists() {
		return lists;
	}
}
