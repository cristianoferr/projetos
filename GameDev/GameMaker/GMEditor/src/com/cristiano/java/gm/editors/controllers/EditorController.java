package com.cristiano.java.gm.editors.controllers;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.bpM.entidade.blueprint.Factory;
import com.cristiano.java.bpM.entidade.blueprint.Mod;
import com.cristiano.java.bpM.params.ParamListHolder;
import com.cristiano.java.bpM.utils.EMLoader;
import com.cristiano.java.gm.builder.factory.BuilderFactory;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.editors.EditorPanel;
import com.cristiano.java.gm.editors.consts.EditorConsts;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.viewers.GMEditor;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.jme3.hull.BaseHull;
import com.cristiano.jme3.rigidBody.CharDefines;
import com.cristiano.jme3.rigidBody.GMPhysicalVehicle;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;
import com.jme3.system.JmeCanvasContext;

//Separando conceitos
public class EditorController {

	private static final String TEST_PARTE = "testParte";
	private GMEditor view;
	private JPanel graphPanel;
	public ElementManager em;

	private boolean elementChanging = false;
	private EntityManager entMan;
	Preferences prefs = Preferences.userNodeForPackage(this.getClass());
	private Canvas canvas;
	private boolean hasChanged = false;
	private IGameElement lastElement;
	private DefaultTreeModel treeModel;
	private int countNovo = 0;
	private int textIncr = 0;

	public DefaultListModel listModel; // tag search
	public boolean cleanView = true;// se true entÃ£o remove as meshs
	private ChildComponent compLoader;
	private IGameEntity clone;
	private int entSize = -1;
	private int emSize = -1;

	EntityManager entManRelease;
	ElementManager emRelease;
	private BuilderFactory factoryRelease;

	public EditorController(GMEditor editorSwing) {
		this.view = editorSwing;
		this.graphPanel = view.getGraphPanel();
		loadBasicPrefs();

	}

	private void loadBasicPrefs() {
		String position = prefs.get(EditorConsts.PREF_MAIN_WINDOW_POS, "");
		if (!position.equals("")) {
			String[] split = position.split(";");
			Dimension size = new Dimension(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			Point pos = new Point(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
			view.setPosition(size, pos);
		}

		updateBooleanValues();

	}

	private void updateBooleanValues() {
		String value = prefs.get(EditorConsts.PREF_HULL_DEBUG, "");
		if (!value.equals("")) {
			BaseHull.debug_draw = (value.equals("T"));
			view.chkHullDebugJoints.setSelected(BaseHull.debug_draw);
		}
		value = prefs.get(EditorConsts.PREF_CLEAN_VIEW, "");
		if (!value.equals("")) {
			cleanView = (value.equals("T"));
			;
		}

		value = prefs.get(EditorConsts.PREF_MODO_DISCRETO, "");
		if (!value.equals("")) {
			CRJavaUtils.IS_CASA = (!value.equals("T"));
			view.chkDiscreto.setSelected(!CRJavaUtils.IS_CASA);
		}
		value = prefs.get(EditorConsts.PREF_APPLY_MATERIAL, "");
		if (!value.equals("")) {
			MaterialComponent.debugApplyMaterial = (value.equals("T"));
			view.chkApplyMaterial.setSelected(MaterialComponent.debugApplyMaterial);
		}
		value = prefs.get(EditorConsts.PREF_ANDROID_MODE, "");
		if (!value.equals("")) {
			CRJavaUtils.IS_ANDROID = (value.equals("T"));
			view.chkAndroidMode.setSelected(CRJavaUtils.IS_ANDROID);
		}
	}

	public void loadingComplete() {
		this.em = view.getElementManager();
		entMan = view.getIntegrationState().getEntityManager();

		entManRelease = new EntityManager();
		emRelease = new ElementManager();
		factoryRelease = new BuilderFactory(em, entManRelease, view.getAssetManager());
		factoryRelease.setElementManager(emRelease);
		List<IGameElement> elementsWithTag = em.getElementsWithTag("blueprint gameSystem leaf");
		CRJavaUtils.IS_RELEASE_INTERNAL = true;
		for (IGameElement el : elementsWithTag) {
			IGameElement elSystem = em.createFinalElement(el);
			IGameSystem mySystem = factoryRelease.createSystemFrom(elSystem);
			if (mySystem != null) {
				entManRelease.addSystem(mySystem);
				mySystem.initSystem(entManRelease, view);
			}
		}
		CRJavaUtils.IS_RELEASE_INTERNAL = false;
		view.getStateManager().attach(entManRelease);
	}

	public void loadPrefs() {
		String selected = getSelectedElements();
		if (!selected.equals("")) {
			view.selectElements(selected);
		}

		String position = prefs.get(EditorConsts.PREF_EDITOR_WINDOW_POS, "");
		if (!position.equals("")) {
			String[] split = position.split(";");
			Dimension size = new Dimension(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			Point pos = new Point(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
			view.setPopOutPosition(size, pos);
		}

		String value = prefs.get(EditorConsts.PREF_TAGS, "");
		if (!value.equals("")) {

		}
	}

	public String getSelectedElements() {
		String selected = prefs.get(EditorConsts.PREF_SELECTED_ELEMENTS, "");
		return selected;
	}

	public void mainWindowChanged(Dimension size, Point location) {
		if (canvas == null) {
			return;
		}
		String val = size.width + ";" + size.height + ";" + location.x + ";" + location.y;
		prefs.put(EditorConsts.PREF_MAIN_WINDOW_POS, val);

		graphPanel.setLayout(new BorderLayout());
		if (Extras.OS.contains("Mac")) {
			Point p = getGraphLocation();
			canvas.setLocation(p);
		} else {
			canvas.setLocation(0, 0);

		}
		canvas.setPreferredSize(graphPanel.getSize());
	}

	private Point getGraphLocation() {
		Point p = new Point(graphPanel.getLocation());
		p.x += graphPanel.getParent().getLocation().x;
		p.y += graphPanel.getParent().getLocation().y;
		p.x += graphPanel.getParent().getParent().getLocation().x;
		p.y += graphPanel.getParent().getParent().getLocation().y;
		p.x += graphPanel.getParent().getParent().getParent().getLocation().x;
		p.y += graphPanel.getParent().getParent().getParent().getLocation().y;
		return p;
	}

	public void popoutWindowChanged(Dimension size, Point location) {
		String val = size.width + ";" + size.height + ";" + location.x + ";" + location.y;
		prefs.put(EditorConsts.PREF_EDITOR_WINDOW_POS, val);

	}

	public void updateSelected(String selected) {
		// view.selectElements(selected);

		prefs.put(EditorConsts.PREF_SELECTED_ELEMENTS, selected);
	}

	public void initContext(JPanel mainPanel) {
		JmeCanvasContext ctx = (JmeCanvasContext) view.getContext();
		view.createCanvas();
		canvas = ctx.getCanvas();

		graphPanel.setLayout(new BorderLayout());
		graphPanel.add(canvas);

		loadingComplete();

	}

	public void salvaElementos() {
		// em.purgeNew();
		em.saveToFile();
		hasChanged = false;
	}

	public void elementSelected(EditorPanel ep, String identifier) {
		elementChanging = true;
		IGameElement currentElement = em.getElementByIdentifier(identifier);
		if (currentElement == null) {
			Log.error("Identifier " + identifier + " nÃ£o encontrado.");
			return;
		}
		ep.setCurrentElement(currentElement);
		ep.setText(sortText(((AbstractElement) currentElement).getParams().toString()));
		elementChanging = false;
		lastElement = currentElement;
	}

	public void elementChanged(EditorPanel ep, String text) {
		IGameElement currentElement = ep.getCurrentElement();
		if (currentElement == null)
			return;
		ParamListHolder params = ((AbstractElement) currentElement).getParams();

		if (elementChanging) {
			return;
		}
		params.replaceWith(text);
		hasChanged = true;
		loadTreeElements();
	}

	private String sortText(String text) {
		String ret = "";
		String[] split = text.split("\n");
		for (int i = 0; i < split.length - 1; i++) {
			for (int j = i + 1; j < split.length; j++) {
				String si = split[i].trim();
				si = si.substring(si.indexOf(" ")).trim();
				String sj = split[j].trim();
				sj = sj.substring(sj.indexOf(" ")).trim();
				if (si.compareToIgnoreCase(sj) > 0) {
					String s = split[i];
					split[i] = split[j];
					split[j] = s;
				}
			}
		}
		for (int i = 0; i < split.length; i++) {
			ret += split[i] + "\n";
		}
		return ret;
	}

	public void loadTreeElements() {
		loadTreeElements(treeModel);
	}

	public void loadTreeElements(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
		DefaultMutableTreeNode treeRoot = (DefaultMutableTreeNode) treeModel.getRoot();
		treeRoot.removeAllChildren();
		List<IGameElement> elements = em.getElements();
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		nodes.add(treeRoot);

		for (int i = 0; i < EMLoader.lastElement; i++) {
			IGameElement el = elements.get(i);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(el.getIdentifier());
			DefaultMutableTreeNode nodeParent = treeRoot;
			if (((AbstractElement) el).getEstende() != null) {
				nodeParent = getParentNode(((AbstractElement) el).getEstende().getIdentifier(), nodes);
				nodeParent.add(node);
			}
			nodes.add(node);
		}
		treeModel.reload(treeRoot);
	}

	private DefaultMutableTreeNode getParentNode(String identifier, List<DefaultMutableTreeNode> nodes) {
		for (DefaultMutableTreeNode node : nodes) {
			if (node.getUserObject().toString().equals(identifier)) {
				return node;
			}
		}
		return null;
	}

	public void visualizaMesh() {

	}

	private void loadEntity(IGameElement currentElement) {
		AbstractElement finalElement = em.createFinalElement(currentElement);

		IGameEntity entity = view.getGameEntity();
		compLoader = (ChildComponent) entMan.addComponent(GameComps.COMP_CHILD, entity);
		compLoader.madeBy = entity;
		compLoader.addRender = true;
		compLoader.elementSource = finalElement;

		setCameraPosition(compLoader);
	}

	private void setCameraPosition(IGameEntity entity) {
		PositionComponent compPosition = (PositionComponent) entMan.addIfNotExistsComponent(GameComps.COMP_POSITION, entity);
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_CAM);

		if (compPosition.getNode() == null) {
			RenderComponent render = (RenderComponent) entity.getComponentWithIdentifier(GameComps.COMP_RENDER);
			if (render != null) {
				compPosition.setNode(render.node);
			}
		}

		CamComponent compCam = (CamComponent) ents.get(0).getComponentWithIdentifier(GameComps.COMP_CAM);
		if (compCam != null) {
			Vector3f location = compCam.cam.getLocation();
			compPosition.setPos(location.add(compCam.cam.getDirection().mult(10)));
		}
	}

	public void visualiza(IGameElement currentElement) {
		Log.debug("Visualizando elemento...");
		if (cleanView) {
			em.purgeNew();
		}
		if (currentElement.hasTag("textureLayer")) {
			loadTexture(currentElement);
		} else if (CRJavaUtils.IS_PHYSICS_ON) {
			loadPhysEntity(currentElement);
		} else if (!((AbstractElement) currentElement).getPropertyH(GameProperties.DEFAULT_WIDTH, true).equals("")) {
			loadMesh(currentElement);
		} else {
			loadEntity(currentElement);
		}
	}

	private void loadPhysEntity(IGameElement currentElement) {
		String tags = getTags(currentElement);
		IGameEntity entity = BuilderUtils.createExampleUnit(entMan, em, tags);

		ComponentRecipes.addDeathTimer(entMan, entity, 240);

		entity.removeComponent(GameComps.COMP_POSITION);
		setCameraPosition(entity);

		Log.debug("Loading physEntity:" + entity);
	}

	private void loadTexture(IGameElement currentElement) {
		IGameEntity ent = entMan.createEntity();
		MaterialComponent matcomp = (MaterialComponent) entMan.addComponent(GameComps.COMP_MATERIAL, ent);
		IGameElement pickOne = em.pickFinal(EditorConsts.MATERIAL_PREVIEW_TEXTURE);
		String tags = getTags(currentElement);
		IGameElement elTexture = em.pickFinal(tags);
		pickOne.setProperty("diffuseObj", elTexture);
		matcomp.loadFromElement(pickOne);
		matcomp.length = EditorConsts.EXPORT_TEXTURE_SIZE;
		TextureComponent textcomp = (TextureComponent) entMan.addComponent(GameComps.COMP_TEXTURE, ent);
		textIncr++;
		Log.debug("Generating Texture:" + currentElement.getIdentifier());
		textcomp.exportTexture = EditorConsts.EXPORT_TEXTURE_PATH + "-" + textIncr + "-" + currentElement.getIdentifier() + ".png";

	}

	private void loadMesh(IGameElement currentElement) {
		if (cleanView) {
			List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_RENDER);
			for (IGameEntity ent : ents) {
				if (ent.containsComponent(GameComps.COMP_SPATIAL)) {
					// entMan.removeEntity(ent);
					entMan.addComponent(GameComps.COMP_DEATH, ent);
				}
			}
		}

		IGameElement element = em.getElementByIdentifier(TEST_PARTE);
		Factory templ = new Factory(em, element);
		String tags = getTags(currentElement);
		templ.setProperty(GameProperties.MESH_TAG, "{" + tags.trim() + "}");
		float width = Float.parseFloat(((AbstractElement) currentElement).getPropertyH(GameProperties.DEFAULT_WIDTH, true));
		float height = Float.parseFloat(((AbstractElement) currentElement).getPropertyH(GameProperties.DEFAULT_HEIGHT, true));
		float depth = Float.parseFloat(((AbstractElement) currentElement).getPropertyH(GameProperties.DEFAULT_DEPTH, true));
		templ.setProperty(GameProperties.WIDTH, width);
		templ.setProperty(GameProperties.HEIGHT, height);
		templ.setProperty(GameProperties.DEPTH, depth);
		loadEntity(templ);
	}

	private String getTags(IGameElement currentElement) {
		String tags = " ";
		List<String> tagList = currentElement.getTags();
		for (String tag : tagList) {
			tags += " " + tag;
		}
		return tags;
	}

	public IGameElement getElementForIdentifier(String identifier) {
		return em.getElementByIdentifier(identifier);
	}

	public void showGraph(boolean b) {
		canvas.setVisible(b);
		// canvas.setPreferredSize(new Dimension(10,10));
		if (b) {
			canvas.setLocation(getGraphLocation());
		} else {
			canvas.setLocation(10000, 10000);
		}

	}

	public void saveIfChanged() {
		if (hasChanged) {
			salvaElementos();
		}

	}

	public void booleanPrefChanged(String pref, boolean value) {
		String val = "T";
		if (!value) {
			val = "F";
		}
		updateBooleanValues();
		prefs.put(pref, val);

	}

	private void stringPrefChanged(String pref, String text) {
		prefs.put(pref, text);
	}

	public void createNewElement(String tipo) {
		GenericElement ge;
		if (tipo.equals("blueprint")) {
			ge = new Blueprint(em, lastElement);
		} else if (tipo.equals("factory")) {
			ge = new Factory(em, lastElement);
		} else {
			ge = new Mod(em, lastElement);
		}
		countNovo++;
		ge.setProperty("identifier", "'NewElement" + countNovo + "'");
		em.insertElementAt(EMLoader.lastElement, ge);
		EMLoader.lastElement++;
		hasChanged = true;
		loadTreeElements();
	}

	public int elementCount() {
		return EMLoader.lastElement;
	}

	public void filterBy(String text) {
		if (em == null) {
			return;
		}

		listModel.clear();
		List<IGameElement> elems = em.getElementsWithTag(text + " !final");
		for (IGameElement elem : elems) {
			listModel.addElement(elem.getIdentifier());
		}
		stringPrefChanged(EditorConsts.PREF_TAGS, text);
	}

	public void clonaUltimaEntidade() {
		pause();
		Log.info("clonaUltimaEntidade...");
		if (compLoader != null) {
			Log.debug("Entity to clone:" + compLoader);
			IGameEntity ent = compLoader;
			JSONObject jsonRoot = new JSONObject();
			JSONObject json = ent.exportToJSON();
			jsonRoot.put(GameProperties.ELEMENT, em.exportToJSON());

			jsonRoot.put(GameProperties.ENTITY, json);

			// GMAssets.s
			//CRJsonUtils.writeJSON(GMAssets.getFullFilePath(GMAssets.getJSONPath()), jsonRoot);
			entMan.cleanup();
			// entMan.removeEntity(ent);
			// entMan.cleanUp();

		} else {
			Log.error("CompLoader null...");
		}
		unpause();

	}

	public void importaEntidade() {
		pause();
		Log.info("Importando Entidade...");
		if (clone != null) {
			entMan.removeEntity(clone);
		}
		if (entSize == -1) {
			entSize = entMan.size();
		}
		if (emSize == -1) {
			emSize = em.size();
		}
		emRelease.purge(0);
		entManRelease.purgeToSize(0);

		// em.removeElementsWithTag(Extras.TAG_ALL);

		//Comentado porque mudou a forma de exportação/importação
		/*
		clone = entManRelease.createEntity();
		JSONObject jsonRoot = CRJsonUtils.readJSON(GMAssets.getFullFilePath(GMAssets.getJSONPath()));
		JSONObject json = (JSONObject) jsonRoot.get(GameProperties.ENTITY);
		JSONObject jsonEM = (JSONObject) jsonRoot.get(GameProperties.ELEMENT);
		emRelease.importFromJSON(jsonEM);
		clone.importFromJSON(json);*/

		// PositionComponent pos=(PositionComponent)
		// clone.getComponentWithIdentifier(GameComps.COMP_POSITION);
		setCameraPosition(clone);
		unpause();
	}

	private void unpause() {
		entMan.unpause();
		entManRelease.unpause();

	}

	private void pause() {
		entMan.pause();
		entManRelease.pause();
	}

	public void testaVeiculo() {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_TARGETABLE);
		for (IGameEntity ent:ents){
			PhysicsComponent physics = ECS.getPhysicsComponent(ent);
			if (physics!=null){
				testaVeiculo(physics);
			}
		}
		
	}

	private void testaVeiculo(PhysicsComponent physics) {
		Log.debug("Testa veiculo:"+physics);
		GMPhysicalVehicle vehicle=(GMPhysicalVehicle) physics.physNode;
		CharDefines defines = vehicle.getDefines();
		
		//defines.stiffness=
		defines.suspensionForce=5f;
		defines.stiffness=50;
		defines.restRel=1.5f;
		defines.maxSuspensionTravelCm=500;
		vehicle.updateFromDefines();
	}

	public void console(String text) {
		
		IGameEntity entity = view.getGameEntity();
		UIActionComponent compAction = (UIActionComponent) entMan.addComponent(GameComps.COMP_UI_ACTION, entity);
		compAction.action=GameActions.ACTION_CONSOLE;
		compAction.auxInfo=text;
		
	}

}
