package com.cristiano.java.gm.builder.utils;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.lifeCycle.MeshLoaderComponent;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroEntity;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.SpeedComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public abstract class BuilderUtils extends GMUtils {

	// element must have materialObj defined
	public static MaterialComponent createMaterial(MeshLoaderComponent meshLoaderComp, IGameElement elMesh, EntityManager entMan, IFinalGame game) {
		IGameElement elMaterial = null;
		if (meshLoaderComp != null) {
			elMaterial = elMesh.getPropertyAsGE(GameProperties.MATERIAL_TYPE);// ParamAsTag(Extras.LIST_MATERIAL,
			if (elMaterial == null) {
				Log.fatal("Material element is null!");
			}
			// Extras.PROPERTY_TYPE);
			/*
			 * elMaterial = meshLoaderComp.findMaterial(materialType,
			 * elMesh.getElementManager());
			 */
		} else {
			elMaterial = elMesh.getPropertyAsGE(GameProperties.MATERIAL_OBJ);
		}
		MaterialComponent comp = (MaterialComponent) entMan.spawnComponent(GameComps.COMP_MATERIAL);
		comp.loadFromElement(elMaterial);
		comp.initMaterialFromSnippets(game.getSnippets());
		comp.addInfo("createMaterial()");
		if (comp.mat() == null) {
			Log.fatal("No Material was generated for " + elMaterial);
		}
		return comp;
	}

	// calculate the point based on the position and/or orientation...
	public static Vector3f calculaPonto(IGameElement elMesh, IGameElement elPT, String objName, Vector3f rotation, Vector3f origem) {
		float radius = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".radius");
		float x = 0, y = 0, z = 0;

		// starting position...
		x = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".x");
		y = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".y");
		z = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".z");

		// x-=origem.x;
		// y-=origem.y;
		// z-=origem.z;

		// se tem radius ent�o eu adiciono o radius � posi��o levando em
		// considera��o o angulo
		if (radius > 0) {
			x += origem.x;
			y += origem.y;
			z += origem.z;

			float width = 0;
			float height = 0;
			float depth = 0;
			width = elPT.getPropertyAsFloat(GameProperties.WIDTH) / 2;
			height = elPT.getPropertyAsFloat(GameProperties.HEIGHT) / 2;
			depth = elPT.getPropertyAsFloat(GameProperties.DEPTH) / 2;
			if (width == 0)
				width = elMesh.getPropertyAsFloat(GameProperties.WIDTH) / 2;
			if (height == 0)
				height = elMesh.getPropertyAsFloat(GameProperties.HEIGHT) / 2;
			if (depth == 0)
				depth = elMesh.getPropertyAsFloat(GameProperties.DEPTH) / 2;
			if (width == 0)
				width = radius;
			if (height == 0)
				height = radius;
			if (depth == 0)
				depth = radius;

			float elRotX = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".x") + rotation.x;
			float rotX = elRotX * FastMath.DEG_TO_RAD;

			float elRotY = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".y") + rotation.y;
			float rotY = elRotY * FastMath.DEG_TO_RAD;
			x += (float) (radius * Math.cos(rotX) * Math.cos(rotY));
			y += (float) (radius * Math.sin(rotX));
			z += (float) (radius * Math.cos(rotX) * Math.sin(rotY));
			/*
			 * x += (float) (radius * Math.cos(rotX) * Math.cos(rotY)) * width /
			 * radius; y += (float) (radius * Math.sin(rotX)) * height / radius;
			 * z += (float) (radius * Math.cos(rotX) * Math.sin(rotY)) * depth /
			 * radius;
			 */
		}
		Vector3f pontoObj = new Vector3f(x, y, z);
		return pontoObj;
	}

	public static Vector3f addRotationFrom(String objName, IGameElement elMesh, Vector3f rotation) {
		rotation = new Vector3f(rotation);
		float elRotX = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".x");
		rotation.x += elRotX;

		float elRotY = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".y");
		rotation.y += elRotY;
		return rotation;
	}

	public static boolean isMerge(String objName, IGameElement elMesh) {
		String tipoNode = elMesh.getParamAsText(Extras.LIST_NODETYPE, objName);
		if (tipoNode == null)
			return true;
		if (tipoNode.equals(GameConsts.NODE_TYPE_NEW)) {
			return false;
		}
		return true;
	}

	public static void generateWall(EntityManager entMan, ElementManager em, MapComponent map, Vector3f pt1, Vector3f pt2) {
		EnviroEntity roadEnviro = map.getRoadEnviro();
		if (roadEnviro == null) {
			Log.error("roadEnviro is null, returning.");
			return;
		}
		String meshTag = roadEnviro.getMeshTag();
		if (meshTag == null) {
			Log.debug("No wall on enviro, returning.");
			return;
		}
		Vector3f posicao = pt1.add(pt2).mult(0.5f);
		float depth = pt1.distance(pt2);
		float angle = CRMathUtils.calcDegreesXZ2(pt1, pt2);
		posicao.y = 0;
		Vector3f dimension = new Vector3f(roadEnviro.getWallWidth(), roadEnviro.getWallHeight(), depth);
		BuilderUtils.addWallElement(entMan, em, map, roadEnviro, posicao, angle, dimension);
	}

	// will create an working unit for testing purposes...
	public static IGameEntity createExampleUnit(EntityManager entMan, ElementManager em, String meshTag) {
		IGameElement elRole = em.pickFinal(TestStrings.UNIT_ROLE);
		IGameEntity entity = entMan.getFactory().createEntityFromTag(TestStrings.UNIT_COMBAT_TAG, "meshTag={" + meshTag + "}");

		UnitClassComponent roleC = (UnitClassComponent) entMan.addComponent(GameComps.COMP_UNIT_CLASS, entity);
		if (elRole == null) {
			Log.fatal("No elRole defined (null)");
		}
		roleC.loadFromElement(elRole);
		
		SpeedComponent speedC = (SpeedComponent) entMan.addComponent(GameComps.COMP_SPEED, entity);
		speedC.setCurrValue(100);

		DPSComponent dps = (DPSComponent) entMan.addComponent(GameComps.COMP_RESOURCE_DPS, entity);
		dps.setCurrValue(101);
		initPhysicsRender(entMan, em, entity);
		return entity;
	}

	private static void initPhysicsRender(EntityManager entMan, ElementManager em, IGameEntity entity) {
		RenderComponent renderC = (RenderComponent) entMan.addIfNotExistsComponent(GameComps.COMP_RENDER, entity);
		renderC.loadController(em.pickFinal(TestStrings.PLAYER_CONTROLLER_TAG));
	}

	public static void addWall(EntityManager entMan, ElementManager em, IGameEntity ent, float w, float h, float d, float x, float y, float z) {
		addStaticElement(LogicConsts.TAG_MESH_WALL, entMan, em, ent, new Vector3f(w, h, d), new Vector3f(x, y, z));
	}

	public static void addStaticElement(String meshTag, EntityManager entMan, ElementManager em, IGameEntity ent, Vector3f dimension, Vector3f pos) {
		IGameElement templ = mountEntity(em, meshTag, LogicConsts.TAG_STATIC_ENTITY, dimension);
		ComponentRecipes.loadEntityFromElement(entMan, ent, templ, pos);
	}

	public static IGameElement mountEntity(ElementManager em, String entityTag) {
		return mountEntity(em, null, entityTag, null);
	}

	public static IGameElement mountEntity(ElementManager em, String entityTag, Vector3f dimensions) {
		return mountEntity(em, null, entityTag, dimensions);
	}

	// Inicia a entity a partir do BubbleComponent...
	public static IGameElement mountEntity(ElementManager em, String meshTag, String entityTag, Vector3f dimensions) {
		IGameElement entityElement = em.createElementFromTag(entityTag);
		if (entityElement == null) {
			Log.error("No Entity was generated with "+entityTag);
			return null;
		}
		// Log.info("EntityFinal:"+entityFinal.id());

		if (meshTag != null) {
			entityElement.setPropertyTag(GameProperties.MESH_TAG, meshTag);
		} else {
			Log.error("No Mesh was defined!");
		}
		if (dimensions != null) {
			entityElement.setProperty(GameProperties.WIDTH, dimensions.x);
			entityElement.setProperty(GameProperties.HEIGHT, dimensions.y);
			entityElement.setProperty(GameProperties.DEPTH, dimensions.z);
		}
		IGameElement templ = em.createFinalElement(entityElement);
		return templ;
	}

	// Used to define a default name for nodes...
	public static String getNodeName(IGameEntity ent, String suffix) {
		return "NODE" + ent.getId() + "-" + suffix;
	}

	public static IGameElement generateElementLine(String lineTag, Vector3f posIni, Vector3f posFim, int pontos, ElementManager em) {

		String pars = "";
		pars += "p0X=" + posIni.x + ",";
		pars += "p0Y=" + posIni.y + ",";
		pars += "p0Z=" + posIni.z + ",";
		pars += "p1X=" + posFim.x + ",";
		pars += "p1Y=" + posFim.y + ",";
		pars += "p1Z=" + posFim.z + ",";
		pars += "qtdPts=" + pontos;
		IGameElement line = em.pickFinal(lineTag, null, pars);
		return line;
	}

	// returns a list with vector3f from the object list
	public static ArrayList<Vector3f> extractPointsFromList(IGameElement line, String objList, Vector3f origin) {
		objList = StringHelper.clear(objList);
		List<IGameElement> pontos = line.getObjectList(objList);
		ArrayList<Vector3f> pts = new ArrayList<Vector3f>();
		for (int i = 0; i < pontos.size(); i++) {

			Vector3f ponto = calculaPonto(line, line, objList + "#" + i, Vector3f.ZERO).addLocal(origin);
			pts.add(ponto);
		}
		return pts;
	}

	// calculate the point based on the position and/or orientation...
	public static Vector3f calculaPonto(IGameElement elMesh, IGameElement elPT, String objName, Vector3f rotation) {
		float radius = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".radius");
		float x = 0, y = 0, z = 0;

		// starting position...
		x = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".x");
		y = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".y");
		z = elMesh.getParamAsFloat(Extras.LIST_POSITION, objName + ".z");

		// se tem radius ent�o eu adiciono o radius � posi��o levando em
		// considera��o o angulo
		if (radius > 0) {
			float width = 0;
			float height = 0;
			float depth = 0;
			width = elPT.getPropertyAsFloat(GameProperties.WIDTH) / 2;
			height = elPT.getPropertyAsFloat(GameProperties.HEIGHT) / 2;
			depth = elPT.getPropertyAsFloat(GameProperties.DEPTH) / 2;
			if (width == 0)
				width = elMesh.getPropertyAsFloat(GameProperties.WIDTH) / 2;
			if (height == 0)
				height = elMesh.getPropertyAsFloat(GameProperties.HEIGHT) / 2;
			if (depth == 0)
				depth = elMesh.getPropertyAsFloat(GameProperties.DEPTH) / 2;
			if (width == 0)
				width = radius;
			if (height == 0)
				height = radius;
			if (depth == 0)
				depth = radius;

			float elRotX = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".x") + rotation.x;
			float rotX = elRotX * FastMath.DEG_TO_RAD;

			float elRotY = elMesh.getParamAsFloat(Extras.LIST_ORIENTATION, objName + ".y") + rotation.y;
			float rotY = elRotY * FastMath.DEG_TO_RAD;
			x += (float) (radius * Math.cos(rotX) * Math.cos(rotY));
			y += (float) (radius * Math.sin(rotX));
			z += (float) (radius * Math.cos(rotX) * Math.sin(rotY));
			/*
			 * x += (float) (radius * Math.cos(rotX) * Math.cos(rotY)) * width /
			 * radius; y += (float) (radius * Math.sin(rotX)) * height / radius;
			 * z += (float) (radius * Math.cos(rotX) * Math.sin(rotY)) * depth /
			 * radius;
			 */
		}
		Vector3f pontoObj = new Vector3f(x, y, z);
		return pontoObj;
	}

	public static String generateDimensionsProps(Vector3f dimensions) {
		return "width=" + dimensions.x + ",height=" + dimensions.y + ",depth=" + dimensions.z;
	}

	public static ArrayList<Vector3f> extractPointsFrom(IGameElement element, Vector3f origin) {
		ArrayList<Vector3f> ret = extractPointsFromList(element, element.getProperty(GameProperties.POINT_LIST), origin);
		return ret;
	}

	public static void addWallElement(EntityManager entMan, IManageElements em, IGameEntity comp, EnviroEntity enviro, Vector3f posMedia, float angle,
			Vector3f dimension) {
		Log.debug("Creating wall at " + posMedia + " dimension:" + dimension + " angle:" + angle);
		String meshTag = enviro.getMeshTag();
		if (meshTag == null) {
			Log.error("Wall mesh is null... cant add.");
			return;
		}
		IGameElement templ = mountEntity((ElementManager) em, meshTag, enviro.getCarrierTag(), dimension);
		if (templ == null) {
			Log.debug("Cant addWall: IGameElement is null.");
			return;
		}

		ChildComponent wallComp = ComponentRecipes.loadEntityFromElement(entMan, comp, templ, posMedia);
		ComponentRecipes.defineEntityOrientation(wallComp, (float) Math.toRadians(-angle), Vector3f.UNIT_Y, entMan);
	}
}
