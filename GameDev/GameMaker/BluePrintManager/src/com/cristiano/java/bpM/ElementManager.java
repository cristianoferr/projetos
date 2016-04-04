package com.cristiano.java.bpM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.KeyWordSet.Domains;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.bpM.entidade.blueprint.Factory;
import com.cristiano.java.bpM.entidade.blueprint.Mod;
import com.cristiano.java.bpM.entidade.blueprint.Template;
import com.cristiano.java.bpM.entidade.functions.FunctionSolver;
import com.cristiano.java.bpM.interfaces.IElementCallBack;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.bpM.utils.EMLoader;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public class ElementManager extends Domains implements IManageElements {

	private ParamList vars; // vari�veis globais... se define usando @define no
							// arquivo .txt

	private ArrayList<IGameElement> elements;
	private HashMap<Integer, IGameElement> elementsMap;// mapeia pelo ID
	private int elementCount = 0;

	private List<IGameElement> elementsToExport = new ArrayList<IGameElement>();

	private FunctionSolver functionSolver;

	private EMLoader emLoader;

	public ElementManager() {
		Log.setNivel(Extras.NIVEL_LOGGING);
		elements = new ArrayList<IGameElement>();
		elementsMap = new HashMap<Integer, IGameElement>();
		vars = new ParamList(null);
		functionSolver = new FunctionSolver();
		emLoader = new EMLoader(this);
	}

	private AbstractElement instanciaFinalObject(IGameElement pickOne) {
		AbstractElement objetoFinal = null;

		objetoFinal = new Template(this, pickOne);
		return objetoFinal;
	}

	public void loadBlueprintsFromFile() throws IOException {
		loadBlueprintsFromFile(Extras.HEADER_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_MAIN_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_ENUM_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_UI_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_ECS_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_MESH_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_LANDMARKS_PATH);
		loadBlueprintsFromFile(Extras.BLUEPRINTS_MODS_PATH);
		emLoader.markSaving();
		emLoader.markEnd();
	}

	public void loadTestBlueprintsFromFile() throws IOException {
		loadBlueprintsFromFile(Extras.HEADER_PATH);
		loadBlueprintsFromFile(Extras.TEST_PATH);
		emLoader.markSaving();
		emLoader.markEnd();
	}

	public void loadBlueprintsFromFile(String file) throws IOException {

		emLoader.loadBlueprintsFrom(file);
		verifyLeafs();
	}

	public void saveToFile() {
		Log.info("Saving elements to file...");
		try {
			emLoader.saveBlueprints();
			Log.info("done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void verifyLeafs() {
		iteraElementos(new IElementCallBack() {
			@Override
			public void updateElement(IGameElement element) {
				GenericElement el = (GenericElement) element;
				if (!el.hasLeafs()) {
					el.addTag(Extras.TAG_LEAF);
				}
			}
		});

	}

	private void iteraElementos(IElementCallBack callback) {
		for (int i = 0; i < elements.size(); i++) {
			IGameElement genericElement = elements.get(i);
			callback.updateElement(genericElement);
		}

	}

	public Blueprint createBlueprint(IGameElement estende) {
		return createBlueprint(estende, true);
	}

	public Blueprint createBlueprint(IGameElement estende, boolean add) {
		Blueprint bp = new Blueprint(this, estende, add);

		bp.addTag(Extras.TAG_BLUEPRINT);
		if (estende != null)
			bp.addTag(estende.getTagsAsText());
		return bp;
	}

	public AbstractElement createAbstractElement(IGameElement ge, boolean add) {
		AbstractElement t = new Template(this, ge, add);
		t.serializa();
		t.addTag(Extras.TAG_TEMPLATE);
		return t;
	}

	public AbstractElement createElement(IGameElement ge) {
		return createAbstractElement(ge, true);
	}

	public IGameElement createElementFromTag(String tags) {
		IGameElement ge = pickOne(tags);
		ge = createElement(ge);
		return ge;
	}

	public IGameElement createElement() {
		AbstractElement el = new GenericElement(this);
		return el;
	}

	public IGameElement createFinalElementFromTag(String tags, IGameElement creator, String props) {
		IGameElement pickOne = pickOne(tags, props);
		if (pickOne == null) {
			Log.trace("tags '" + tags + "' returned nothing...");
			return null;
		}
		return createFinalElement(pickOne, creator);
	}

	public AbstractElement createFinalElementFromTag(String tags) {
		IGameElement pickOne = pickOne(tags);
		if (pickOne == null) {
			Log.error("Nenhum elemento com tag '" + tags + "' encontrado.");
			return null;
		}
		return createFinalElement(pickOne, null);
	}

	public AbstractElement createFinalElement(IGameElement geLeaf) {
		if (geLeaf == null)
			return null;
		return createFinalElement(geLeaf, null);
	}

	public AbstractElement createFinalElement(IGameElement pickOne, IGameElement creator) {
		if (pickOne == null) {
			Log.error("Tentando criar um elemento final a partir de um elemento nulo!!");
		}
		// Log.debug("Criando elemento final de "+base.getIdentifier()+"... Creator:"+cr);
		int c = 0;
		AbstractElement ge = null;
		boolean valid = false;
		do {
			String info = "> " + pickOne.getIdentifier() + " " + pickOne.id();
			if (creator != null) {
				info = ((AbstractElement) creator).getDebugInfo() + info;
			}
			ge = createFinalElementSingleTry(pickOne, creator, info);
			c++;
			if (ge == null) {
				valid = false;
			} else {
				valid = ge.isValid();
			}
			if (!valid) {
				Log.warn("Elemento inválido... tentativa: " + c);
			}
		} while ((c < Extras.VALIDATE_MAX_TRIES) && (!valid));

		if (!ge.isValid()) {
			Log.error("ERRO de validação!" + ge);
			ge = null;
		}
		ge.addTag(Extras.TAG_LEAF);
		return ge;
	}

	private AbstractElement createFinalElementSingleTry(IGameElement pickOne, IGameElement iGameElement, String debugInfo) {
		try {
			AbstractElement objetoFinal;
			if (((AbstractElement) pickOne).getObjectType().equals(Extras.OBJECT_TYPE_FACTORY)) {
				Factory factory = (Factory) pickOne;
				objetoFinal = factory.fabricaItem();
			} else {
				objetoFinal = instanciaFinalObject(pickOne);
			}
			objetoFinal.setDebugInfo(debugInfo + " >>" + objetoFinal.id() + " ");

			// used to ascertain that some calculations are only done in the
			// final phase...
			objetoFinal.setVar(Extras.VAR_FINAL, "");

			String isEntity = ((AbstractElement) pickOne).getParamH(Extras.LIST_ENTITY, Extras.ENTITY_ISENTITY, true);
			if (isEntity.equals("1")) {
				objetoFinal.setEntity();
			}

			if (iGameElement != null) {
				objetoFinal.setCreator(iGameElement);
			}
			objetoFinal.serializa();
			((Template) objetoFinal).tornaObjetoFinal();
			objetoFinal.addTag(Extras.TAG_FINAL);
			objetoFinal.addTag(Extras.TAG_TEMPLATE);
			return objetoFinal;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Factory createFactory(IGameElement base) {
		Factory t = new Factory(this, base);
		// Log.debug("Criando Factory: "+base.getIdentifier());
		t.addTag(Extras.TAG_FACTORY);
		return t;
	}

	public Mod createMod(IGameElement base) {
		Mod t = new Mod(this, base);
		// Log.debug("Criando Mod: "+base.getIdentifier());
		t.addTag(Extras.TAG_MOD);
		return t;
	}

	public String toString() {
		String saida = "";

		for (int i = 0; i < elements.size(); i++) {
			saida += elements.get(i) + "\n";
		}
		return saida;
	}

	public void addElement(IGameElement ge) {

		if (!containsElement(ge)) {
			elements.add(ge);
			ge.setID(elementCount);
			// Log.debug("id:"+ge.id()+" "+(ge==null)+" ident:"+ge.getIdentifier());
			elementsMap.put(ge.getId(), ge);
			elementCount++;
			if (elementCount % 250 == 0) {
				Log.debug("ElementCount:" + elementCount);
			}
			ge.addTag(Extras.TAG_ALL);
			
		}
	}

	public void removeElement(IGameElement ge) {
		if (ge==null){
			return;
		}
		Log.debug("removendo elemento " + ge.id());
		elementsMap.remove(ge.id());
		elements.remove(ge);
		List<String> tags = ge.getTags();
		for (int i = 0; i < tags.size(); i++) {
			removeElementFromTag(ge, tags.get(i));
		}
	}

	public boolean containsElement(IGameElement ge) {
		return elements.contains(ge);
	}

	public int size() {
		return elements.size();
	}

	public IGameElement getElementAt(int i) {
		return elements.get(i);
	}

	// fun��es internas... usando essas fun��es para sincronizar com as tags de
	// forma a melhorar a performance
	public void addObjectToTag(GenericElement gameElement, String tag) {
		List<IGameElement> tags = getTags(tag);
		if (!tags.contains(gameElement)) {
			tags.add(gameElement);
		}
	}

	public void removeElementFromTag(IGameElement ge, String tag) {
		List<IGameElement> tags = getTags(tag);
		tags.remove(ge);
	}

	// only return non-final objects
	public IGameElement pickOne(String withTag, String pars) {
		withTag = StringHelper.removeChaves(withTag);
		IGameElement element = null;
		if (withTag.contains("[")) {
			element = getElementWithID(StringHelper.pickRandomItemFromList(withTag));
		} else {
			withTag = withTag + " !" + Extras.TAG_FINAL + " !" + Extras.TAG_TEMPLATE + " !" + Extras.TAG_NOTREADY;
			element = pickAnyOne(withTag);
		}
		if (element == null) {
			Log.trace("No element with tags '" + withTag + "' found!");
			return null;
		}
		if (pars == null) {
			return element;
		}
		AbstractElement ge = null;
		if (element.hasTag(Extras.TAG_FACTORY)) {
			ge = new Factory(this, element);
		} else {
			ge = new Template(this, element);
		}

		ge.applyInlineProperties(pars);
		ge.addTag(Extras.TAG_FINAL);
		return ge;
	}

	public IGameElement pickOne(String withTag) {
		return pickOne(withTag, null);
	}

	// pode retornar objetos finais
	public IGameElement pickAnyOne(String withTag) {
		String tagsLimpa = withTag;
		if (tagsLimpa.contains(Extras.KEYWORD_TAG)) {
			tagsLimpa = tagsLimpa.substring(0, tagsLimpa.lastIndexOf(Extras.KEYWORD_TAG)).trim();
		}
		List<IGameElement> result = getElementsWithTag(tagsLimpa);
		int size = result.size();
		if (size == 0)
			return null;
		result = filterKeyword(result, withTag);
		int pos = (int) (result.size() * Math.random());

		IGameElement ge = result.get(pos);
		// Log.debug("pickAnyOne:"+withTag+"    -->>"+ge.getIdentifier());
		return ge;
	}

	private List<IGameElement> filterKeyword(List<IGameElement> list, String tags) {
		if (!tags.contains(Extras.KEYWORD_TAG)) {
			return list;
		}
		String keyword = tags.substring(tags.lastIndexOf(Extras.KEYWORD_TAG) + Extras.KEYWORD_TAG.length()).trim();
		List<IGameElement> result = new ArrayList<IGameElement>();
		for (IGameElement el : list) {
			if (((AbstractElement) el).hasKeyword(keyword)) {
				result.add(el);
			}
		}

		// se n�o existem elementos com a keyword ent�o eu retorno a lista
		// inteira...
		if (result.size() == 0) {
			return list;
		}
		return result;
	}

	public IGameElement pickFinal(String tag) {
		return pickFinal(tag, null, null);
	}

	public IGameElement pickFinal(String tag, IGameElement creator, String par1) {
		IGameElement ge;
		ge = createFinalElementFromTag(tag, (AbstractElement) creator, par1);
		return ge;
	}

	@Override
	public IGameElement pickFinal(String solverTag, IGameElement creator) {

		return pickFinal(solverTag, creator, null);
	}

	public IGameElement pickSingle(String tag, IGameElement creator, String par1) {
		IGameElement ge;
		ge = pickAnyOne(Extras.TAG_FINAL + " " + tag);
		if (ge == null) {
			ge = createFinalElementFromTag(tag, creator, par1);
		} else {
			//Log.info("PickSingle returned for tag '"+tag+"': "+ge);
		}
		return ge;
	}

	public int countElementos(String withTag) {
		withTag = StringHelper.removeChaves(withTag);
		List<IGameElement> result = getElementsWithTag(withTag);
		int size = result.size();
		return size;
	}

	public IGameElement getElementWithID(int id) {
		return elementsMap.get(id);
	}

	public IGameElement getElementWithID(String strId) {
		strId = StringHelper.removeColchetes(strId);
		if (strId.equals("")) {
			return null;
		}
		int size=elements.size();
		for (int i=0;i<size;i++){
			IGameElement ge=elements.get(i);
			if (ge.id().equals(strId)) {
				return ge;
			}
		}
		// Log.warn("No element with ID: "+strId+" found!");
		return null;
	}

	public List<IGameElement> getElementsFromList(String idList) {
		List<IGameElement> list = new ArrayList<IGameElement>();
		idList = idList.replace("[", "");
		idList = idList.replace("]", "");
		String[] arr = idList.split(",");
		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].trim().equals(""))
				list.add(getElementWithID(arr[i]));
		}
		return list;
	}

	public IGameElement getElementByIdentifier(String identifier) {
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getIdentifier().equals(identifier)) {
				return elements.get(i);
			}
		}
		return null;
	}

	public String getVar(String var) {
		String s = vars.getVar(var);
		if (s == null) {
			return "";
		}
		return s;
	}

	public void setVar(String var, String val) {
		vars.setVar(var, val);

	}

	public List<IGameElement> getElements() {
		return elements;
	}

	// remove elementos da lista que tenham as tags informadas...
	public void removeElementsWithTag(String tags) {
		List<IGameElement> result = getElementsWithTag(tags);
		for (int i = 0; i < result.size(); i++) {
			IGameElement el = result.get(i);
			removeElement(el);
		}

	}

	public FunctionSolver getFunctionSolver() {
		return functionSolver;
	}

	public void addUserFunction(String function, String params, String val) {
		functionSolver.addUserFunction(function, params, val);

	}

	public void insertElementAt(int pos, GenericElement ge) {
		elements.add(pos, ge);
	}

	public void purgeNew() {
		;
		purge(emLoader.lastElement);
	}

	public void purge(int qtd) {
		while (size() > qtd) {
			removeElement(elements.get(size() - 1));
		}
	}

	public EMLoader getLoader() {
		return emLoader;

	}

	@Override
	public IGameElement importElementFromJSON(JSONObject elJson) {
		int ID = CRJsonUtils.getInteger(elJson,GameProperties.ID);

		IGameElement el = getElementWithID(ID);
		if (el != null) {
			return el;
		}
		el = createElement();
		el.importFromJSON(elJson);
		elements.add(el);
		return el;
	}

	@Override
	public void addToExportList(IGameElement element) {
		if (!elementsToExport.contains(element)) {
			elementsToExport.add(element);
		}

	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj=new JSONObject();
		obj.put(GameProperties.ELEMENT,CRJsonUtils.exportList(elementsToExport));
		Log.debug("Exported "+elementsToExport.size()+" of "+elements.size()+" elements.");
		return obj;
	}

	//this shouldnt be used...
	@Override
	public void importFromJSON(JSONObject json) {
		//elements=CRJsonUtils.importList(json.get(GameProperties.ELEMENT), factory);
		JSONObject object=(JSONObject) json.get(GameProperties.ELEMENT);
		Iterator<String> it = object.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			JSONObject item = (JSONObject) object.get(key);
			IGameElement el = createElement();
			el.importFromJSON(item);
		}
		
	}

	@Override
	public void removeElementWithID(int id) {
		removeElement(getElementWithID(id));
		
	}

	@Override
	public void clear() {
		elements.clear();
		elementsMap.clear();
		
	}
}
