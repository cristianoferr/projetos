package com.cristiano.java.gm.ecs.comps.macro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class BestiaryLibraryComponent extends GameComponent {

	public float chanceNewEntity;
	
	public BestiaryLibraryComponent() {
		super(GameComps.COMP_BESTIARY_LIB);

	}
	
	@Override
	public void free() {
		super.free();
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		chanceNewEntity = ge.getPropertyAsFloat(GameProperties.CHANCE_NEW_ENTITY) / 100;
	}

	

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.CHANCE_NEW_ENTITY, chanceNewEntity);
		return true;
	}
	

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		chanceNewEntity = CRJsonUtils.getFloat(obj,GameProperties.CHANCE_NEW_ENTITY);
		//future = entMan.getExecutor().submit(loadBestiary);
	}
	
	
	
	@Override
	public IGameComponent clonaComponent() {
		BestiaryLibraryComponent ret = new BestiaryLibraryComponent();
		ret.setEntityManager(entMan);
		ret.chanceNewEntity = chanceNewEntity;
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public boolean addStorage(IGameElement elRole, int minVariations) {
		if (getClassWithIdentifier(elRole.getIdentifier())!=null){
			return false;
		}
		
			UnitClassComponent role=(UnitClassComponent) entMan.addComponent(GameComps.COMP_UNIT_CLASS,this);
			role.loadFromElement(elRole);
			role.minVariations=minVariations;
			UnitStorage beasts = factory.createUnitStorage(getElement().getElementManager(),entMan);
			
			role.storage=beasts;
			return true;
	}

	private UnitClassComponent getClassWithIdentifier(String identifier) {
		List<IGameComponent> comps = getComponents(GameComps.COMP_UNIT_CLASS);
		for (IGameComponent comp:comps){
			UnitClassComponent role = (UnitClassComponent) comp;
			if (role.roleIdentifier.equals(identifier)){
				return role;
			}
		}
		return null;
	}
	
	
	public UnitClassComponent getUnitClass(String roleIdentifier) {
		roleIdentifier=BPUtils.clear(roleIdentifier);
		UnitClassComponent role = getClassWithIdentifier(roleIdentifier);
		if (role==null){
			Log.fatal("No role with ident '"+roleIdentifier+"' found!");
		}
		return role;
	}
	
	public UnitClassComponent getGenericClass(String entitySource, int entityType) {
		UnitClassComponent ret=getClassWithIdentifier(entitySource);
		if (ret==null){
			ret=(UnitClassComponent) entMan.addComponent(GameComps.COMP_UNIT_CLASS,this);
			ret.addInfo("Generic Class:"+entitySource);
			ret.unitRootTag=entitySource;
			ret.roleIdentifier=entitySource;
			ret.isGeneric=true;
			ret.storage=factory.createUnitStorage(getElement().getElementManager(),entMan);
		}
		return ret;
	}
	
	//
	//
	
}
