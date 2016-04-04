package com.cristiano.java.gm.units;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

//UnitRoles contem todas as unidades que fazem parte daquela classe...
//Gerencia as entidades
public class UnitStorage implements ISerializeJSON {

	protected List<IGameEntity> entities = new ArrayList<IGameEntity>();

	private IGameFactory factory;
	private JSONObject json;
	protected EntityManager entMan;

	public UnitStorage( IGameFactory factory,
			EntityManager entMan) {
		this.factory = factory;
		this.entMan = entMan;
	}

	public UnitStorage(JSONObject obj, IGameFactory factory,
			EntityManager entMan) {
		this.factory = factory;
		this.entMan = entMan;
		importFromJSON(obj);
	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		obj.put(GameProperties.OBJECT_TYPE, JavaConsts.OBJECT_UNIT_STORAGE);
		obj.put(GameProperties.ENTITY_OBJ, CRJsonUtils.exportList(entities));
		if (entities.isEmpty()) {
			Log.fatal("Empty unitStorage!");
		}
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		this.json = json;
		JSONObject object = (JSONObject) json.get(GameProperties.UNIT_CLASSES);
		Object assembleJSON = factory.assembleJSON(object);
		entities = CRJsonUtils.importList(
				(JSONObject) json.get(GameProperties.ENTITY_OBJ), factory);
		for (IGameEntity ent : entities) {
			ent.deactivate();
		}

		if (entities.isEmpty()) {
			Log.fatal("Empty unitStorage!");
		}
	}

	public IGameEntity requestMasterEntity(UnitClassComponent unitClass,float chanceNewEntity) {
		IGameEntity ret = getExistingEntity();
		if (ret == null) {
			Log.fatal("No existing mould found...");
		}
		return ret;
	}

	public IGameEntity getExistingEntity() {

		if (entities.size() > 0) {
			int p = (int) (CRJavaUtils.random() * entities.size());
			return entities.get(p);
		}
		return null;
	}

	public void atualizaUnitRole(UnitClassComponent unitClass,IGameEntity entity, float budget,
			EntityManager entMan) {
		float totalWeight = 0;

		if (unitClass.unitResources == null) {
			return;
		}
		for (ResourceComponent resource : unitClass.unitResources) {
			totalWeight += resource.getWeight();
		}
		for (ResourceComponent resource : unitClass.unitResources) {
			String identifier = resource.getIdentifier();
			ResourceComponent entResource = (ResourceComponent) entity
					.getComponentWithIdentifier(identifier);
			if (entResource == null) {
				Log.warn("Entity dont have: " + identifier);
				entMan.addComponent(identifier, entity);
			} else {
				entResource.initComponent(budget, totalWeight);
			}
		}

	}

	public boolean isEmpty() {
		return entities.isEmpty();
	}

}
