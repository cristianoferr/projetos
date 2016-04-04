package com.cristiano.java.jme.tests.mocks;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gameObjects.tests.AbstractTest;
import com.cristiano.java.gm.builder.factory.BuilderFactory;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.gm.units.UnitStorageBuilder;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.extras.ObjectTypes;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.google.common.reflect.ClassPath;
import com.jme3.asset.AssetManager;

public class MockFactory extends BuilderFactory {

	public MockFactory(ElementManager em, EntityManager entMan, AssetManager assetManager) {
		super(em, entMan,assetManager);
	}

	public static String[] getSystemsPackages() {
		String root = GameComps.SYSTEM_PACKAGE;
		;
		String[] packages = new String[] { root, root + ".art",
				root + ".macro", root + ".map", root + ".mechanics",
				root + ".persists", root + ".ui", root + ".unit",
				root + ".unit.actuators", root + ".unit.fx",
				root + ".unit.npcBehaviours", root + ".unit.npcStates",
				root + ".npcStates", root + ".unit.resources",
				root + ".unit.sensors", root + ".visual" };
		return packages;

	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();

		JSONObject objMap = new JSONObject();

		String[] componentPackages = getComponentPackages();

		for (String pack : componentPackages) {
			exportClasses(pack, objMap);
		}

		obj.put(GameProperties.CLASS_PROPERTY, objMap);
		return obj;
	}

	private void exportClasses(String pack, JSONObject objMap) {
		try {
			final ClassLoader loader = Thread.currentThread()
					.getContextClassLoader();
			for (final ClassPath.ClassInfo info : ClassPath.from(loader)
					.getTopLevelClasses()) {
				if (info.getName().startsWith(pack)) {
					final Class<?> clazz = info.load();
					String clazzpack = pack + "." + clazz.getSimpleName();
					if (CRJavaUtils.classExists(clazzpack, this)){
						objMap.put(clazz.getSimpleName(), clazzpack);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String[] getComponentPackages() {
		String root = GameComps.COMPONENT_PACKAGE;
		String[] packages = new String[] { root, root + ".art",
				root + ".effects", root + ".macro", root + ".lifeCycle",
				root + ".map", root + ".mechanics", root + ".misc",
				root + ".persists", root + ".ui", root + ".unit",
				root + ".materials", root + ".unit.actuators",
				root + ".unit.fx", root + ".unit.npcBehaviours",
				root + ".unit.npcStatus", root + ".unit.npcStates",
				root + ".unit.npcConditions", root + ".npcStates",
				root + ".unit.resources", root + ".unit.sensors",
				root + ".visual" };
		return packages;
	}

	public static IGameSystem instantiateSystem(String name) {
		String[] packages = getSystemsPackages();
		for (String pack : packages) {
			String fullClassPath = pack + "." + name;
			if (CRJavaUtils.classExists(fullClassPath, AbstractTest.game)) {
				return (IGameSystem) CRJavaUtils.instanciaClasse(fullClassPath);
			}
		}
		return null;
	}

	@Override
	public Object instantiateClass(String className, String objectType) {
		if (className.equals(JavaConsts.OBJECT_GAME_ENTITY)) {
			return entMan.createEntity();
		}
		Object obj = CRJavaUtils.instanciaClasse(className);
		return obj;
	}

	@Override
	public IGameComponent createComponentFrom(IGameElement compEl) {
		IGameComponent createComponentFromClass = createComponentFromClass(compEl
				.getIdentifier());
		createComponentFromClass.loadFromElement(compEl);
		return createComponentFromClass;
	}

	@Override
	public void addPackage(String typeEntity, String entityPackage) {

	}

	@Override
	public IGameComponent createComponentFromClass(String compIdent) {
		String classeComponent = getFullClassFor(compIdent);
		if (classeComponent == null) {
			Log.error("Componente " + compIdent + " desconhecido.");
		}
		// initClass =initClass.replace("class ","");
		IGameComponent comp = (IGameComponent) createEntityFromClass(
				classeComponent, ObjectTypes.TYPE_COMPONENT);
		// comp.loadFromElement(em.pickFinal(compIdent));
		return comp;
	}

	private String getFullClassFor(String name) {
		String[] packages = getComponentPackages();
		for (String pack : packages) {
			String fullClassPath = pack + "." + name;
			if (CRJavaUtils.classExists(fullClassPath, this)) {
				return fullClassPath;
			}
		}
		Log.fatal("Class not found:" + name);
		return null;
	}

	@Override
	public IGameEntity clonaEntidade(IGameEntity molde) {
		IGameEntity clone = super.clonaEntidade(molde);
		return clone;
	}

	@Override
	public void addClasse(String compHealth, String string) {
	}

	@Override
	public UnitStorage createUnitStorage(
			IManageElements em,EntityManager entMan) {
		return new UnitStorageBuilder( em, this,entMan);
	}

}
