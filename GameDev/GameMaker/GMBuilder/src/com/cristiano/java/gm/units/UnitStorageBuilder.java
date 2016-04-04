package com.cristiano.java.gm.units;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

//UnitRoles contem todas as unidades que fazem parte daquela classe...
//Gerencia as entidades
/*
 * Unit Test: TestUnitStorage
 * */
public class UnitStorageBuilder extends UnitStorage {


	private ElementManager em;


	public UnitStorageBuilder(IManageElements em,IGameFactory factory,EntityManager entMan) {
		super(factory,entMan);
		this.em=(ElementManager) em;
	}

	@Override
	public IGameEntity requestMasterEntity( UnitClassComponent unitClass,float chanceNewEntity) {
		//Check chance to return a new entity...
		if (CRJavaUtils.random()<chanceNewEntity){
			return addNewEntity(unitClass);
		}
		
		//checking if there is a loaded entity
		IGameEntity ret=getExistingEntity();
		if (ret!=null){
			return ret;
		}
		
		//which it may not exist, so a new one is created...
		return addNewEntity(unitClass);
	}


	private IGameEntity addNewEntity(UnitClassComponent unitClass) {
		Log.info("Adding new Entity for role: "+unitClass);
		String props="multiplier="+unitClass.dimensionMultiplier;
		IGameEntity entity;
		if (unitClass.isGeneric){
			Log.debug("New entity is generic, tag is:"+unitClass.unitRootTag);
			IGameElement mountEntity = BuilderUtils.mountEntity(em, unitClass.unitRootTag);
			entity=entMan.createEntity();
			ChildComponent loadEntityFromElement = ComponentRecipes.loadEntityFromElement(entMan, entity, mountEntity, Vector3f.ZERO);
			
		} else {
			entity = entMan.getFactory().createEntityFromTag(unitClass.unitRootTag,props);
		}
		entity.attachComponent(unitClass);
		//entity.deactivate();
		entMan.addIfNotExistsComponent(GameComps.COMP_MASTER, entity);
		entities.add(entity);
		
		return entity;
	}

	
}
