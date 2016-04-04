package com.cristiano.java.bpM.entidade.blueprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cristiano.consts.Extras;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.extras.FunctionConsts;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public class Template extends Blueprint {

	public Template(ElementManager elementManager, IGameElement estende) {
		this(elementManager, estende, true);
	}

	public Template(ElementManager elementManager, IGameElement estende,
			boolean add) {
		super(elementManager, estende, add);
		setObjectType(Extras.OBJECT_TYPE_TEMPLATE);
		if (estende == null) {
			Log.error("AbstractElement estende null");
		}
		this.addTag(((AbstractElement) estende).getTagsAsText());
		this.removeTag(Extras.TAG_BLUEPRINT);
	}

	public void tornaObjetoFinal() {
		String domainType = getParamAsText(Extras.LIST_DOMAIN, "type");
		applyMods();
		resolveFuncoes(true);
		addTag(domainType);
		replicarObjetos();

		resolveObjects();
		checkMirrorPosition();
		resolveFuncoes(true);

		finishDefines();

		removeDesignTags();
		removeDesignLists();
	}

	private void applyMods() {
		ParamList list = getListWithKey(Extras.LIST_MOD);
		List<String> allKeys = list.getAllKeys();
		for (String key : allKeys) {
			Parametro param = list.getParamBlueprintFor(key);
			String value = param.getValue();
			String solvedValue = resolveFunctionOf(value);
			param.setValue(solvedValue);
			IGameElement mod = elementManager.getElementWithID(solvedValue);
			if (mod == null) {
				String tag = resolveFunctionOf(value.replace("pickOne(", "")
						.replace(")", ""));
				Log.fatal("No Mod found for:" + tag + "->" + solvedValue);
			}
			((GenericElement) mod).aplicaParametrosEm(this);
		}
	}

	private void finishDefines() {
		ParamList list = getListWithKey(Extras.LIST_DEFINE);
		if (!list.isEmpty()) {
			List<String> allKeys = list.getAllKeys();
			for (String key : allKeys) {
				getElementManager().setVar(key, list.getVar(key));
			}
		}

	}

	public void refresh() {
		super.refresh();
		Blueprint bp = getBlueprint();
		if (bp == null) {
			return;
		}
		ParamList pl = getParams().getListWithKey(Extras.LIST_PROPERTY);
		List<String> arr = pl.getAllKeys();
		for (int i = 0; i < arr.size(); i++) {
			String param = arr.get(i);
			String valor = bp.getProperty(param);
			if (valor.contains("$")) {
				String solved = resolveFunctionOf(valor);
				setParam(Extras.LIST_PROPERTY, param, solved);
			}
		}
	}

	private void replicateFromList(String components, String listName) {
		String[] comps = StringHelper.splitList(components);
		if (comps == null) {
			return;
		}
		List<IGameElement> currentList = this.getObjectList(listName);
		if (currentList.size() == comps.length) {
			return;
		}
		Log.debug(this.getIdentifier() + "(" + this.getId() + ")"
				+ " replicando " + components + "  ::" + getDebugInfo());
		for (int i = 0; i < comps.length; i++) {
			String name = comps[i];
			if (listName != null) {
				name = listName + "#" + i;
			}
			replicaChildProperties(listName, comps.length);
			IGameElement finalElement = createElementFromReplicateList(
					listName, comps[i]);

			if (finalElement == null) {
				Log.error("Element not found to replicate:" + comps[i]);
			}
			aplicaChildProperties(name, finalElement);
			((AbstractElement) finalElement).refresh();
			setParam(Extras.LIST_OBJECT, name, "[" + finalElement.id() + "]");
			replicarObjeto(comps[i], "1");
		}
	}

	private void replicaChildProperties(String objectName, int qtd) {
		ParamList plChild = getParams().getListWithKey(
				Extras.LIST_CHILD_PROPERTY);
		List<String> arr = plChild.getAllKeys();
		for (int i = 0; i < arr.size(); i++) {
			String obj = arr.get(i);
			if (obj.startsWith(objectName + ".")) {
				String valor = plChild.getParamBlueprintFor(obj).getValue();
				String prop = obj.replace(objectName + ".", "");
				// aplicaChildProperty(plChild, obj, ge);
				for (int q = 0; q < qtd; q++) {
					plChild.setParam(objectName + "#" + q + "." + prop + " = "
							+ valor);
				}
				plChild.removeParam(obj);
			}
		}
	}

	// cria um elemento da lista
	private IGameElement createElementFromReplicateList(String lista, String tag) {
		String sourceList = getParamAsText(Extras.LIST_REPLICATE, lista + "."
				+ Extras.PARAM_SOURCE);

		List<IGameElement> objectList = this.getObjectList(sourceList, tag);
		if ((objectList.size() == 0) || ("".equals(sourceList))) {
			return elementManager.createFinalElementFromTag(tag, this, null);
		}
		int p = (int) (Math.random() * objectList.size());
		return elementManager.createFinalElement(
				(AbstractElement) objectList.get(p), this);

	}

	private void resolveObjects() {
		ParamList plObject = getParams().getListWithKey(Extras.LIST_OBJECT);
		List<String> arr = plObject.getAllKeys();
		for (int i = 0; i < arr.size(); i++) {
			String obj = arr.get(i);
			Parametro paramBlueprint = plObject.getParamBlueprintFor(obj);
			String idobj = paramBlueprint.getValue();
			AbstractElement ge = (AbstractElement) elementManager
					.getElementWithID(idobj);
			if (ge == null) {
				Log.warn("null object given! id:" + idobj + " obj:" + obj);
				return;
			}
			if (hasChildProperties(obj, ge)) {
				ge = elementManager.createBlueprint(ge, false);
				if (aplicaChildProperties(obj, ge)) {
					ge = elementManager.createFinalElement(ge, this);
				}
			}
			if (!ge.isMastered()) {
				ge = elementManager.createFinalElement(ge, this);
				ge.setCreator(this);
			}
			plObject.setParam(obj + "=[" + ge.id() + "]");
		}

	}

	private void replicarObjetos() {
		// vou replicar objetos que est�o na lista antes de qualquer coisa...
		ParamList plObject = getParams().getListWithKey(Extras.LIST_REPLICATE);
		List<String> arr = plObject.getAllKeys();
		for (int i = 0; i < arr.size(); i++) {
			String obj = arr.get(i);
			if (!obj.endsWith("." + Extras.PARAM_SOURCE)) {

				String qtd = getParamAsText(Extras.LIST_REPLICATE, obj);
				if (qtd.startsWith("{")) {
					replicateFromList(qtd, obj);
				} else {
					if ("".equals(qtd)) {
						qtd = "1";
					}
					Log.trace("Replicando: " + obj + ": " + qtd + " vezes.");
					replicarObjeto(obj, qtd);
				}
				getParams().getListWithKey(Extras.LIST_REPLICATE).removeParam(
						obj);
			}
		}

	}

	private void replicarObjeto(String obj, String funcaoQuantasVezes) {
		String[] arrParams = null;
		arrParams = StringHelper.getParamsFromFuncao(
				FunctionConsts.FUNCTION_ONEFOREACH, funcaoQuantasVezes);
		if (arrParams != null) {
			replicaObjetosOneEach(obj, arrParams);
			getParams().getListWithKey(Extras.LIST_REPLICATE).removeParam(obj);
			;
			return;
		}

		arrParams = StringHelper.getParamsFromFuncao(
				FunctionConsts.FUNCTION_TAGLIST, funcaoQuantasVezes);
		if (arrParams != null) {
			replicaObjetosTagList(obj, arrParams);
			getParams().getListWithKey(Extras.LIST_REPLICATE).removeParam(obj);
			;
			return;
		}

		replicaObjetosLista(obj, funcaoQuantasVezes);// EX: {tag1 tag2 tag3}

	}

	private void replicaObjetosTagList(String obj, String[] arrParams) {
		if (arrParams[0].equals("")) {
			return;
		}
		List<IGameElement> lista = createListFromTagList(arrParams);
		replicaChildProperties(obj, lista.size());
		replicaItemsDaLista(obj, lista.size(), true);
		for (int i = 0; i < lista.size(); i++) {
			IGameElement ge = lista.get(i);
			if (!((AbstractElement) ge).isMastered()) {
				ge = elementManager.createFinalElement(ge, this);
			}
			String name = obj + "#" + i;
			setParam("@" + Extras.LIST_OBJECT + " " + name + " = [" + ge.id()
					+ "]");
			aplicaChildProperties(obj, ge);
		}

	}

	List<IGameElement> createListFromTagList(String[] arrParams) {
		arrParams[0] = resolveFunctionOf(arrParams[0]);
		arrParams[1] = resolveFunctionOf(arrParams[1]);
		String tags = StringHelper.removeParenteses(arrParams[0]);
		String[] pars = tags.split(" ");
		String filtro = "";
		if (arrParams.length > 0) {
			filtro = " " + arrParams[1];
		}

		List<IGameElement> lista = new ArrayList<IGameElement>();
		for (int i = 0; i < pars.length; i++) {
			if (!pars[i].equals(" ")) {
				IGameElement ge = elementManager.pickOne(StringHelper
						.removeChaves(pars[i] + filtro));
				if (ge != null) {
					lista.add(ge);
				}
			}
		}
		return lista;
	}

	private void replicaObjetosOneEach(String obj, String[] arrParams) {
		if (StringHelper.removeChaves(arrParams[0]).equals("")) {
			return;
		}
		List<IGameElement> lista = elementManager
				.getElementsWithTag(arrParams[0] + " !final");

		replicaItemsDaLista(obj, lista.size(), true);
		Log.trace("replicaObjetosOneEach:" + arrParams[0]);
		for (int i = 0; i < lista.size(); i++) {
			IGameElement ge = lista.get(i);
			setParam("@" + Extras.LIST_OBJECT + " " + obj + "#" + i + " = ["
					+ ge.id() + "]");
		}
	}

	private void replicaObjetosLista(String obj, String funcaoQuantasVezes) {
		int qtdVezes = 1;
		try {
			qtdVezes = (int) Float.parseFloat(funcaoQuantasVezes);
		} catch (Exception e) {
			Log.error("Erro ao parsear:" + funcaoQuantasVezes + " " + e);
		}
		replicaItemsDaLista(obj, qtdVezes, false);

	}

	private boolean hasChildProperties(String objectName, AbstractElement ge) {
		ParamList plProperty = getParams().getListWithKey(
				Extras.LIST_CHILD_PROPERTY);
		List<String> arr = plProperty.getAllKeys();
		boolean aplicou = false;
		for (int i = 0; i < arr.size(); i++) {
			// padrao: nomeObjeto#0.radius
			String obj = arr.get(i);
			if (obj.startsWith(objectName)) {
				if (hasChildProperty(plProperty, obj, ge)) {
					return true;
				}
			}

		}
		return aplicou;
	}

	private boolean aplicaChildProperties(String objectName, IGameElement ge) {
		ParamList plProperty = getParams().getListWithKey(
				Extras.LIST_CHILD_PROPERTY);
		List<String> arr = plProperty.getAllKeys();
		boolean aplicou = false;
		for (int i = 0; i < arr.size(); i++) {
			// padrao: nomeObjeto#0.radius
			String obj = arr.get(i);
			if (obj.startsWith(objectName)) {
				if (aplicaChildProperty(plProperty, obj, ge)) {
					aplicou = true;
				}
			}

		}
		return aplicou;
	}

	private boolean hasChildProperty(ParamList plProperty, String ident,
			IGameElement ge) {
		String filtro = null;
		if (ident.contains("[")) {
			filtro = ident
					.substring(ident.indexOf("[") + 1, ident.indexOf("]"));
		}
		if (filtro != null) {
			if (!ge.hasTag(filtro.trim())) {
				return false;
			}
		}
		return true;
	}

	private boolean aplicaChildProperty(ParamList plProperty, String ident,
			IGameElement ge) {
		if (!hasChildProperty(plProperty, ident, ge)) {
			return false;
		}
		String param = ident.substring(ident.indexOf(".") + 1);
		if (param.contains("[")) {
			param = param.substring(0, param.indexOf("["));
		}
		ge.setProperty(param, plProperty.getParamBlueprintFor(ident).getValue());
		return true;
	}

	private void replicaItemsDaLista(String obj, int qty,
			boolean listadeParametros) {
		ArrayList<String> keys = getParams().getKeys();
		keys = priorizaLista(keys);

		// replica as listas no objeto final..
		for (int i = 0; i < keys.size(); i++) {
			boolean flagOK = true;
			String lista = keys.get(i);
			if (lista.equals(Extras.LIST_REPLICATE)) {
				flagOK = false;
			}
			if (lista.equals(Extras.LIST_ITERATOR)) {
				flagOK = false;
			}
			if (lista.equals(Extras.LIST_PROPERTY)) {
				flagOK = false;
			}

			if ((listadeParametros) && (lista.equals(Extras.LIST_OBJECT))) {
				flagOK = false;
			}

			if (flagOK) {
				replicaLista(obj, qty, lista);
			}
		}
	}

	private void checkMirrorPosition() {
		float mirrorX = getPropertyAsFloat(Extras.PROPERTY_MIRROR_X);
		float mirrorY = getPropertyAsFloat(Extras.PROPERTY_MIRROR_Y);
		float mirrorZ = getPropertyAsFloat(Extras.PROPERTY_MIRROR_Z);
		if (mirrorX == 1) {
			mirrorPosition(this, "x");
		}
		if (mirrorY == 1) {
			mirrorPosition(this, "y");
		}
		if (mirrorZ == 1) {
			mirrorPosition(this, "z");
		}

	}

	public static void mirrorPosition(AbstractElement el, String eixo) {
		String list = StringHelper.clear(el
				.getProperty(Extras.PROPERTY_POINTLIST));
		String[] pointList = list.split(" ");
		String posName = "." + eixo;
		for (int i = 0; i < pointList.length; i++) {
			String objBase = pointList[i];

			List<IGameElement> objectList = el.getObjectList(objBase);
			for (int j = 0; j < objectList.size(); j++) {
				AbstractElement subObj = (AbstractElement) objectList.get(j);
				mirrorPosition(subObj, eixo);
				String objName = pointList[i] + "#" + j + posName;

				float pos = el.getParamAsFloat(Extras.LIST_POSITION, objName);
				pos = -pos;
				el.setParam(Extras.LIST_POSITION, objName, Float.toString(pos));
			}
		}
	}

	private ArrayList<String> priorizaLista(ArrayList<String> keys) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(Extras.LIST_REPLICATE);
		ret.add(Extras.LIST_OBJECT);
		ret.add(Extras.LIST_CHILD_PROPERTY);

		for (int i = 0; i < keys.size(); i++) {
			if ((!keys.get(i).equals(Extras.LIST_OBJECT))
					&& (!keys.get(i).equals(Extras.LIST_REPLICATE))
					&& (!keys.get(i).equals(Extras.LIST_CHILD_PROPERTY))) {
				ret.add(keys.get(i));
			}
		}
		return ret;
	}

	public void replicaLista(String obj, int qtdReplicacao, String lista) {
		ParamList pl = getParams().getListWithKey(lista);
		List<String> arrChaves = pl.getKeysWithParam(obj);

		HashMap<String, IteratorVar> vars = new HashMap<String, IteratorVar>();
		carregaVariaveis(vars, obj);

		resolveFuncoesObjetoFilho(obj, qtdReplicacao, pl, arrChaves, vars);

		limpaVariaveisTemporarias();

	}

	private void carregaVariaveis(HashMap<String, IteratorVar> vars,
			String objectName) {
		ParamList plProperty = getParams().getListWithKey(Extras.LIST_ITERATOR);
		List<String> arr = plProperty.getAllKeys();

		for (int i = 0; i < arr.size(); i++) {
			String key = arr.get(i);
			String aux = key;

			if (key.startsWith(objectName + ".")) {
				aux = aux.replace(objectName + ".", "");
				String varname = aux.substring(0, aux.indexOf("."));
				IteratorVar iter = vars.get(varname);
				if (iter == null) {
					iter = new IteratorVar();
					vars.put(varname, iter);
				}
				aux = aux.replace(varname + ".", "");
				String valor = plProperty.getParamBlueprintFor(key).getValue();
				iter.data(aux, valor, this);

			}
		}

	}

	private void resolveFuncoesObjetoFilho(String obj, int qty, ParamList pl,
			List<String> arrChaves, HashMap<String, IteratorVar> vars) {
		for (int index = 0; index < arrChaves.size(); index++) {
			Parametro pbOriginal = pl
					.getParamBlueprintFor(arrChaves.get(index));
			for (int i = 0; i < qty; i++) {
				setIteratorVars(i, qty, vars);
				setVar("index", Integer.toString(i));
				String nomeParam = pegaNomeDoObjeto(obj, pbOriginal, i);
				pl.setParam(nomeParam + "="
						+ resolveFunctionOf(pbOriginal.getValue()));
			}
			clearIteratorVars(obj, vars);
			pl.removeParam(pbOriginal.getIdentifier());
		}
	}

	private void clearIteratorVars(String objectName,
			HashMap<String, IteratorVar> vars) {
		Iterator it = vars.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			IteratorVar iter = vars.get(pairs.getKey());
			iter.clearVars((String) pairs.getKey(), this);
		}
	}

	private void setIteratorVars(int index, int qty,
			HashMap<String, IteratorVar> vars) {
		Iterator it = vars.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			IteratorVar iter = vars.get(pairs.getKey());
			iter.startVars(index, qty, (String) pairs.getKey(), this);
		}

	}

	private String pegaNomeDoObjeto(String obj, Parametro pbOriginal, int i) {
		String nomeParam = obj + "#" + i;
		if (pbOriginal.getIdentifier().contains(".")) {
			nomeParam = obj + "#" + i
					+ pbOriginal.getIdentifier().substring(obj.length());
		}
		return nomeParam;
	}

	private void limpaVariaveisTemporarias() {
		setVar("index", null);
	}

	public String id() {
		return "TEMPL" + super.ID;
	}

	public boolean isMastered() {
		return true;
	}

	
}
