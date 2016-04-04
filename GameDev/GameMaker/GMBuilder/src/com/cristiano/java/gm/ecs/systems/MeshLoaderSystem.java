package com.cristiano.java.gm.ecs.systems;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.lifeCycle.MeshLoaderComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.AbstractActuatorComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.jme3.interfaces.IMakeHull;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class MeshLoaderSystem extends JMEAbstractSystem {
	public boolean isDebugMode = false;
	private RenderComponent compRender;

	public MeshLoaderSystem() {
		super(GameComps.COMP_MESH_LOADER);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		MeshLoaderComponent comp = (MeshLoaderComponent) component;
		IGameElement elMesh = comp.meshElement;

		Log.info("MeshLoading entity..." + elMesh.getIdentifier() + " (" + comp.getId() + ")");
		compRender = addRenderComponent(ent);
		addPositionComponent(ent, compRender);
		debugPoint(ColorRGBA.Yellow, Vector3f.ZERO, 0.2f);

		// lÃª a lista de pontos e adiciona...
		loadFromElement(comp, elMesh, ent, Vector3f.ZERO, Vector3f.ZERO);

		ECS.addRotationComponent(ent, compRender, entMan);
		ent.removeComponent(comp);

	}

	private RenderComponent addRenderComponent(IGameEntity ent) {
		RenderComponent compRender;
		compRender = ECS.getRenderComponent(ent);
		if (compRender == null) {
			compRender = (RenderComponent) entMan.addComponent(GameComps.COMP_RENDER, ent);
			compRender.firstTick = false;
			if (compRender.node == null) {
				compRender.node = new Node(GMUtils.getNodeName(ent, "meshLoad"));
			}
		}
		return compRender;
	}

	private void addPositionComponent(IGameEntity ent, RenderComponent compRender) {
		PositionComponent posComp;
		posComp = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
		if (posComp == null) {
			posComp = (PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, ent);
		} else {
			compRender.node.setLocalTranslation(posComp.getPos());
		}
		posComp.setNode(compRender.node);
	}

	private SpatialComponent generateComplementaryComponents(MeshLoaderComponent comp, IGameElement elMesh, IGameEntity entity, IMakeHull hullMaker) {
		Geometry spatial;
		TextureComponent textComp = createTextureComponent();
		MaterialComponent matComp = BuilderUtils.createMaterial(comp, elMesh,entMan,game);
		spatial = hullMaker.createSpatial(matComp.mat(), snippets, GMUtils.getNodeName(entity, elMesh.getIdentifier()), CRJavaUtils.IS_DEBUG);
		SpatialComponent spatialComp = createSpatialComponentForEntity(entity, spatial, hullMaker);
		finishTextureComponent(textComp, spatialComp, elMesh);
		spatialComp.attachComponent(matComp);
		spatialComp.attachComponent(textComp);
		if (matComp.castShadows) {
			spatial.setShadowMode(ShadowMode.CastAndReceive);
		} else {
			spatial.setShadowMode(ShadowMode.Off);
		}
		return spatialComp;
	}

	private void finishTextureComponent(TextureComponent textComp, SpatialComponent spatialComp, IGameElement elMesh) {
		textComp.applyToSpatial = spatialComp;
	}

	private TextureComponent createTextureComponent() {
		TextureComponent texture = (TextureComponent) entMan.spawnComponent(GameComps.COMP_TEXTURE);
		return texture;
	}

	private void loadListFromElement(MeshLoaderComponent comp, IGameElement elMesh, IGameEntity entity, List<Vector3f> pontos, Vector3f objRot, Vector3f origem) {
		String objectList = elMesh.getProperty(GameProperties.POINT_LIST);
		String[] lists = StringHelper.splitList(objectList);
		if (lists == null) {
			return;
		}

		for (String list : lists) {
			if (!"".equals(list)) {
				//Log.debug("Loading from " + elMesh.getIdentifier() + ": " + list + " origem:" + origem + " rotation:" + objRot);
				loadFromList(comp, entity, list, elMesh, origem.clone(), objRot.clone(), pontos);
			}
		}
	}

	private SpatialComponent createSpatialComponentForEntity(IGameEntity entity, Geometry spatial, IMakeHull hullMaker) {
		SpatialComponent compSpat = (SpatialComponent) entMan.addComponent(GameComps.COMP_SPATIAL, entity);
		compSpat.faces2D = hullMaker.getFaces2D();
		compSpat.spatial(spatial);

		return compSpat;
	}

	private void loadFromList(MeshLoaderComponent comp, IGameEntity ent, String list, IGameElement elMesh, Vector3f origem, Vector3f rotation,
			List<Vector3f> pontos) {
		List<IGameElement> objectList = elMesh.getObjectList(list);
		debugPoint(ColorRGBA.Green, origem, 0.02f);
		for (int i = 0; i < objectList.size(); i++) {
			String objName = list + "#" + i;
			carregaPonto(comp, ent, elMesh, origem, rotation, pontos, objectList.get(i), objName);
		}
	}

	private void carregaPonto(MeshLoaderComponent comp, IGameEntity ent, IGameElement elMesh, Vector3f origem, Vector3f rotation, List<Vector3f> pontos,
			IGameElement elPT, String objName) {

		Vector3f pontoObj = BuilderUtils.calculaPonto(elMesh, elMesh, objName, rotation, origem);

		// pontoObj.addLocal(origem);
		pontos.add(pontoObj);
		if (pontoObj.length() > JMEConsts.MAX_DIST_POINTS_MESH) {
			Log.error("Point is too far apart:" + pontoObj);
		}
		if (BuilderUtils.isMerge(objName, elMesh)) {
			Vector3f objRot = BuilderUtils.addRotationFrom(objName, elMesh, rotation);
			Log.trace(objName + ":: rot:" + objRot + " pos:" + pontoObj + " origem:" + origem);
			loadListFromElement(comp, elPT, ent, pontos, objRot, pontoObj);
			debugPoint(ColorRGBA.Blue, pontoObj, 0.05f);
		} else {
			Vector3f rot = GMUtils.addRotation(objName, elMesh, rotation);
			Log.info(objName + " pos:" + pontoObj + " origem:" + origem);
			SpatialComponent spatialComp = loadFromElement(comp, elPT, ent, Vector3f.ZERO, pontoObj.add(origem));

			// quando não há pontos (o elemento adiciona meshes sem gerar
			// pontos)...
			if (spatialComp == null) {
				return;
			}
			spatialComp.setElement(elPT);
			debugPoint(ColorRGBA.Red, pontoObj, 0.2f);
			centraliza(spatialComp.spatial());
			// spatialComp.position=Vector3f.ZERO;
			if (rot.length() > 0) {
				JMESnippets.rotaciona(spatialComp.spatial(), rot.x, rot.y, rot.z);
				// spatialComp.position=pontoObj.clone();
			}
			spatialComp.addToRender = false;
			spatialComp.objName = objName;
			if ((verifyActuators(ent, elPT, spatialComp, pontoObj)) || (!CRJavaUtils.IS_PHYSICS_ON)) {
				spatialComp.addToRender = true;

				// Geometry clone = spatialComp.spatial.clone();
				// compRender.node.attachChild(clone);
			}
			spatialComp.addInfo("MeshLoader:addToRender?" + spatialComp.addToRender);
		}
	}

	private void centraliza(Geometry spatial) {
		spatial.center();
		BoundingVolume worldBound = spatial.getWorldBound();

	}

	private SpatialComponent loadFromElement(MeshLoaderComponent comp, IGameElement elMesh, IGameEntity entity, Vector3f objRot, Vector3f pos) {
		SpatialComponent spatialComp = null;
		debugPoint(ColorRGBA.Gray, pos, 0.025f);
		ArrayList<Vector3f> pontos = new ArrayList<Vector3f>();
		loadListFromElement(comp, elMesh, entity, pontos, objRot, pos);

		// debugLog("finalizando");
		if (pontos.size() > 3) {
			IGameElement elHullMaker = elMesh.getPropertyAsGE(GameProperties.HULL_MAKER);
			IMakeHull hullMaker = getHullMakerFor(elHullMaker);
			Vector3f[] points = JMEUtils.extractPointsFromList(pontos);// getPointsListFrom(elMesh);
			try {
				hullMaker.build(points);
				spatialComp = generateComplementaryComponents(comp, elMesh, entity, hullMaker);
				spatialComp.position = pos.clone();
				spatialComp.sourcePoints = pontos;
			} catch (Exception e) {
				try {
					Log.error("Erro ao criar a mesh a partir dos pontos, erro:" + e.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		return spatialComp;
	}

	private void debugPoint(ColorRGBA cor, Vector3f pontoObj, float f) {
		// compRender.node.attachChild(snippets.generateBox(cor, new
		// Vector3f(f,f,f), pontoObj));
	}

	// actuators: anything that acts on the enviroment, for instance: wheels,
	// weapons, etc, this method must be agnostic.
	public boolean verifyActuators(IGameEntity entity, IGameElement elTurret, SpatialComponent spatial, Vector3f pontoObj) {
		ParamList actuatorList = ((AbstractElement) elTurret).getListWithKey(Extras.LIST_ACTUATOR);
		List<String> allKeys = actuatorList.getAllKeys();
		boolean addNode = true;
		for (String key : allKeys) {
			Parametro identifier = actuatorList.getParamBlueprintFor(key);
			String solved = elTurret.resolveFunctionOf(identifier.getValue());
			IGameElement actuator = elTurret.getParamAsGE(Extras.LIST_ACTUATOR, identifier.getIdentifier());
			boolean b = addActuator(identifier, actuator, entity, spatial, pontoObj);
			if (!b) {
				addNode = false;
			}
			/*
			 * if (identifier.getIdentifier().contains("weapon")){
			 * entity.removeComponent(spatial); }
			 */
			Log.debug("Actuator " + identifier + " will be added? " + addNode);

		}

		return addNode;

	}

	private boolean addActuator(Parametro identifier, IGameElement actuator, IGameEntity entity, SpatialComponent spatial, Vector3f pontoObj) {
		Log.debug("Adding actuator " + identifier + " to entity:" + entity);
		// actuator = getIManageElements().createFinalElement(actuator);
		AbstractActuatorComponent actComp = (AbstractActuatorComponent) entMan.addComponent(actuator, entity);
		// actComp.loadFromElement(elPT);

		actComp.setPosition(pontoObj);
		actComp.addSpatial(spatial, pontoObj);

		boolean addingNode = actComp.isAddingNode();

		linkSubSpatials(entity, actComp, spatial, addingNode);

		return addingNode;
	}

	private void linkSubSpatials(IGameEntity entity, AbstractActuatorComponent actComp, SpatialComponent spatialRoot, boolean addingNode) {
		List<IGameComponent> spatials = entity.getComponentsWithIdentifier(GameComps.COMP_SPATIAL);

		for (IGameComponent comp : spatials) {
			IGameElement elCreator = comp.getElement().getPropertyAsGE(Extras.PROPERTY_CREATED_BY);
			if (elCreator == spatialRoot.getElement()) {
				SpatialComponent spatial = (SpatialComponent) comp;
				spatial.addToRender = addingNode;
				actComp.addSpatial(spatial, spatial.position);
				linkSubSpatials(entity, actComp, spatial, addingNode);
			}
		}
	}

	private IMakeHull getHullMakerFor(IGameElement elHullMaker) {
		if (elHullMaker == null) {
			Log.fatal("HullMaker is null!");
		}
		String pack = elHullMaker.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS_NAME);
		initReuseComponent();
		IMakeHull hullMaker = (IMakeHull) reuseC.instantiateUniqueClass(pack);
		return hullMaker;
	}

}
