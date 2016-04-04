package com.cristiano.java.bpM.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public class ParamList implements ISerializeJSON{
	ParamListHolder listOwner;
	HashMap<String, Parametro> params;

	public ParamList(ParamListHolder listOwner) {
		this.listOwner = listOwner;
		params = new HashMap<String, Parametro>();
	}

	public String getVar(String var) {
		Parametro pb = getParamBlueprintFor(var);
		if (pb != null) {
			return pb.getValue();
		}
		return null;
	}

	public void setVar(String varName, String value) {
		varName=varName.replace("$","");
		if (value == null)
			removeParam(varName);
		else
			setParam(varName + "=" + value);

	}

	public void setParam(String param) {

	/*	if (param.contains("camType")){
			Log.info("camType:"+param);
		}*/
		Parametro pb = getParamBlueprintFor(param);
		String comentario = null;
		if (param.contains("//")) {
			comentario = param.substring(param.indexOf("//") + 2);
			param = param.substring(0, param.indexOf("//"));
		}

		if (pb == null) {
			String ident = getIdentifier(param);
			pb = new Parametro(param);
			params.put(ident, pb);
		} else {
			pb.changeParam(param);
		}
		if (comentario != null) {
			pb.setComentario(comentario);
		}

	}

	public boolean hasParam(String property) {
		Parametro pb = getParamBlueprintFor(property);
		if (pb == null) {
			return false;
		}
		if (pb.getValue().trim().equals(""))
			return false;
		return true;
	}

	public boolean hasEmptyParam(String property) {
		Parametro pb = getParamBlueprintFor(property);
		if (pb == null) {
			return false;
		}
		return true;
	}

	public List<String> getAllKeys() {
		List<String> parms = new ArrayList<String>();
		Iterator<Entry<String, Parametro>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Parametro> pairs = (Map.Entry<String, Parametro>) it.next();
			parms.add(pairs.getKey().toString());
		}

		StringHelper.sortStringArray(parms);

		return parms;
	}

	public Parametro getParamBlueprintFor(String param) {
		String ident = getIdentifier(param);
		return params.get(ident);
	}

	public String getIdentifier(String param) {
		String s = "";
		for (int i = 0; i < param.length(); i++) {
			if ((param.charAt(i) == '+') || (param.charAt(i) == '-') || (param.charAt(i) == '=') || (param.charAt(i) == '*')) {
				return s.trim();
			}
			s += param.charAt(i);
		}

		return s.trim();
	}

	public String toString(String prefix) {
		String saida = "";

		List<String> parms = getAllKeys();
		for (int i = 0; i < parms.size(); i++) {
			String key = parms.get(i);
			Parametro parametro = params.get(key);
			saida += prefix + parametro.toString() + "\n";
		}

		return saida;
	}

	public void resolveFuncoes(boolean solveDependent) {
		List<String> keys = getAllKeys();

		// essa lista vai conter os params que possuem alguma refer�ncia a
		// propriedade $this do objeto, resolvo primeiro as demais propriedades
		List<String> propriedadesDependentes = new ArrayList<String>();

		for (String key : keys) {
			Parametro param = getParamBlueprintFor(key);
			if (checkFunctionSolvable(param)) {
				if (param.getValue().contains(Extras.REF_THIS + ".")) {
					// System.out.println("adicionando "+key+" � wait list...");
					propriedadesDependentes.add(key);
				} else {
					param.setValue(resolveFunctionOf(param.getValue(),param));
				}
			}
		}

		if (solveDependent) {
			// resolvo as propriedades que estavam na list de espera...
			for (String key : propriedadesDependentes) {
				Parametro param = getParamBlueprintFor(key);
				String resolveFunctionOf = resolveFunctionOf(param.getValue(),param);
				param.setValue(resolveFunctionOf);
			}
		}
	}

	private boolean checkFunctionSolvable(Parametro param) {
		return listOwner.owner.checkFunctionSolvable(param.getValue());
	}

	
	private String resolveFunctionOf(String property, Parametro paramSource) {
		return listOwner.owner.resolveFunctionOf(property,paramSource);
	}
	

	/*
	 * public void resolve(String listName,boolean flagHierarquico,
	 * ParamListHolder paramManager ) { List<String>
	 * allParams=listOwner.getOwner().getAllParams(listName,flagHierarquico);
	 * for (int i=0;i<allParams.size();i++){ String ident=allParams.get(i);
	 * String saida=listOwner.getOwner().getParamH(listName, ident, false);
	 * boolean
	 * exists=(!paramManager.getOwner().getParamAsText(listName,ident).equals
	 * ("")); if (!exists){
	 * saida=paramManager.getOwner().resolveFunctionOf(saida);
	 * paramManager.addParam("@"+listName+" "+ident+" = "+ saida); } } }
	 */

	public void serializa(String listName, ParamListHolder paramManager) {
		concretiza(listName, true, paramManager, false);
	}

	public void concretiza(String listName, boolean flagHierarquico, ParamListHolder paramManager, boolean solveParams) {
		// essa lista vai conter os params que possuem alguma refer�ncia a
		// propriedade $this do objeto, resolvo primeiro as demais propriedades
		List<String> propriedadesDependentes = new ArrayList<String>();
		List<String> identsDependentes = new ArrayList<String>();

		List<String> allParams = listOwner.getOwner().getAllParams(listName, flagHierarquico);
		for (int i = 0; i < allParams.size(); i++) {
			String ident = allParams.get(i);
			String saida = listOwner.getOwner().getParamH(listName, ident, false);
			boolean exists = (!paramManager.getOwner().getParamAsText(listName, ident).equals(""));
			if (!exists) {

				if ((solveParams) && (checkFunctionSolvable(saida))) {
					if (saida.contains("$this")) {
						propriedadesDependentes.add(saida);
						identsDependentes.add(ident);
					} else {
						saida = paramManager.getOwner().resolveFunctionOf(saida);
					}
				}
				paramManager.addParam("@" + listName + " " + ident + " = " + saida);
				if (listName.equals(Extras.LIST_DEFINE)) {
					// paramManager.addDefine(ident,saida);
				}
			}
		}

		// finalizo as propriedades que ficaram na wait list...
		for (int i = 0; i < propriedadesDependentes.size(); i++) {
			String saida = propriedadesDependentes.get(i);
			String ident = identsDependentes.get(i);
			paramManager.addParam("@" + listName + " " + ident + " = " + saida);
		}
	}

	private boolean checkFunctionSolvable(String property) {
		return listOwner.owner.checkFunctionSolvable(property);
	}

	public List<String> getKeysWithParam(String obj) {
		List<String> arr = getAllKeys();
		for (int i = arr.size() - 1; i >= 0; i--) {
			if (!arr.get(i).equals(obj) && (!arr.get(i).startsWith(obj + ".")))
				arr.remove(i);
		}
		return arr;
	}

	public void removeParam(String identifier) {
		params.remove(identifier);

	}

	public int size() {
		return params.size();
	}

	public boolean isEmpty() {
		return size()==0;
	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		List<String> keys = getAllKeys();
		for (String key:keys){
			String value = getParamBlueprintFor(key).getValue();
			obj.put(key, value);
			if (value.contains("[")){
				value=StringHelper.clear(value);
				String[] split = value.split(",");
				for (String id:split){
					ElementManager elementManager = listOwner.getOwner().getElementManager();
					IGameElement ge = elementManager.getElementWithID(id);
					if (ge!=null){
						elementManager.addToExportList(ge);
					}
				}
			}
		}
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

	
}
