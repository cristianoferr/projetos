package com.cristiano.java.gm.ecs.comps.unit;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.gm.units.controls.GMBulletDefines;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;


public class UnitClassComponent extends GameComponent {
	//Store all MASTER entities from this class...
	public UnitStorage storage;
	
	
	//public elRole elRole;
	public float spawnChance = 0;
	public float maxCombatRange;
	public float minCombatRange;
	public float budgetMultiplier;
	public String roleIdentifier;
	public String unitRootTag;
	public List<ResourceComponent> unitResources = null;
	public float idealCombatRange;
	public float rateOfFire;
	public float dimensionMultiplier;
	public float blastRadius;
	public float bulletSpeed;
	//public IGameElement elBullet;
	public float bulletMass;
	public float bulletRadius;
	public float bulletGravity;
	private Vector3f vectorGravity;
	public boolean isGeneric = false;
	private String name;
	public String bulletID;
	public String bulletController;
	public float proximityDetonation;
	
	public IGameElement bulletElement;
	
	
	public int minVariations=1;
	
	
	public UnitClassComponent(){
		super(GameComps.COMP_UNIT_CLASS);
		
	}
	@Override
	public void loadFromElement(IGameElement elRole) {
		super.loadFromElement(elRole);
		this.name = elRole.getName();
		this.dimensionMultiplier = elRole.getPropertyAsFloat(GameProperties.DIMENSION_MULTIPLIER);
		this.spawnChance = elRole.getPropertyAsFloat(GameProperties.SPAWN_CHANCE);
		this.maxCombatRange = elRole.getPropertyAsFloat(GameProperties.COMBAT_RANGE_MAX);
		this.minCombatRange = elRole.getPropertyAsFloat(GameProperties.COMBAT_RANGE_MIN);
		this.idealCombatRange = elRole.getPropertyAsFloat(GameProperties.COMBAT_RANGE_IDEAL);
		this.budgetMultiplier = elRole.getPropertyAsFloat(GameProperties.BUDGET_MULTIPLIER);
		this.blastRadius = elRole.getPropertyAsFloat(GameProperties.BLAST_RADIUS);
		this.bulletElement = elRole.getPropertyAsGE(GameProperties.BULLET_ENTITY);
		if (bulletElement!=null){
			this.bulletID=bulletElement.id();
		} else {
			Log.warn("Role has no bullet associated.");
		}
		this.rateOfFire = elRole.getPropertyAsFloat(GameProperties.COMBAT_RATEOFFIRE);
		
		loadBulletProperties();
		
		this.roleIdentifier = BPUtils.clear(elRole.getIdentifier());
		this.unitRootTag = elRole.getProperty(GameProperties.UNIT_ROLE_TAG);
		List<IGameElement> elResources = elRole.getObjectList(GameProperties.RESOURCES);
		unitResources = ResourceComponent.loadResources(elResources, entMan,elRole.getPropertyAsGE(GameProperties.GAME_OPPOSITION));
	}
	private void loadBulletProperties() {
		if (bulletElement==null){
			return;
		}
		this.bulletSpeed = bulletElement.getPropertyAsFloat(GameProperties.BULLET_SPEED);
		this.bulletGravity = bulletElement.getPropertyAsFloat(GameProperties.BULLET_GRAVITY);
		this.bulletMass = bulletElement.getPropertyAsFloat(GameProperties.BULLET_MASS);
		this.bulletRadius = bulletElement.getPropertyAsFloat(GameProperties.BULLET_RADIUS);
		this.bulletController=bulletElement.getProperty(GameProperties.BULLET_CONTROLLER);
		this.blastRadius = bulletElement.getPropertyAsFloat(GameProperties.BLAST_RADIUS);
		this.proximityDetonation=bulletElement.getPropertyAsFloat(GameProperties.PROXIMITY_DETONATION);
		if (bulletController.trim().equals("")){
			Log.fatal("BulletController not defined!!");
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		UnitClassComponent ret = new UnitClassComponent();
		ret.setEntityManager(entMan);
		if (getElement()!=null){
			
			ret.loadFromElement(getElement());
		}
		ret.isGeneric=isGeneric;
		ret.unitRootTag=unitRootTag;
		ret.minVariations=minVariations;
		
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.BULLET_ID, bulletID);
		obj.put(GameProperties.GENERICS, isGeneric);
		obj.put(GameProperties.NAME, name);
		obj.put(GameProperties.DIMENSION_MULTIPLIER, dimensionMultiplier);
		obj.put(GameProperties.SPAWN_CHANCE, spawnChance);
		obj.put(GameProperties.COMBAT_RANGE_MAX, maxCombatRange);
		obj.put(GameProperties.MIN_VARIATIONS, minVariations);
		obj.put(GameProperties.COMBAT_RANGE_MIN, minCombatRange);
		obj.put(GameProperties.COMBAT_RANGE_IDEAL, idealCombatRange);
		obj.put(GameProperties.BUDGET_MULTIPLIER, budgetMultiplier);
		obj.put(GameProperties.BLAST_RADIUS, blastRadius);
		obj.put(GameProperties.BULLET_SPEED, bulletSpeed);
		obj.put(GameProperties.COMBAT_RATEOFFIRE, rateOfFire);
		obj.put(GameProperties.BULLET_GRAVITY, bulletGravity);
		obj.put(GameProperties.BULLET_MASS, bulletMass);
		obj.put(GameProperties.PROXIMITY_DETONATION, proximityDetonation);
		obj.put(GameProperties.BULLET_RADIUS, bulletRadius);
		obj.put(GameProperties.ID_MASTER, BPUtils.clear(roleIdentifier));
		obj.put(GameProperties.UNIT_ROLE_TAG, unitRootTag);
		obj.put(GameProperties.BULLET_CONTROLLER, bulletController);
		if (bulletElement!=null){
			obj.put(GameProperties.BULLET_ELEMENT, bulletElement.exportToJSON());
		}
		obj.put(GameProperties.RESOURCES, CRJsonUtils.exportList(unitResources));
		
		obj.put(GameProperties.UNIT_STORAGE, storage.exportToJSON());
		
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		name=(String)obj.get(GameProperties.NAME);
		bulletID=(String)obj.get(GameProperties.BULLET_ID);
		isGeneric=(boolean) obj.get(GameProperties.GENERICS);
		dimensionMultiplier=CRJsonUtils.getFloat(obj,GameProperties.DIMENSION_MULTIPLIER);
		spawnChance=CRJsonUtils.getFloat(obj,GameProperties.SPAWN_CHANCE);
		maxCombatRange=CRJsonUtils.getFloat(obj,GameProperties.COMBAT_RANGE_MAX);
		minCombatRange=CRJsonUtils.getFloat(obj,GameProperties.COMBAT_RANGE_MIN);
		idealCombatRange=CRJsonUtils.getFloat(obj,GameProperties.COMBAT_RANGE_IDEAL);
		minVariations=CRJsonUtils.getInteger(obj,GameProperties.MIN_VARIATIONS);
		budgetMultiplier=CRJsonUtils.getFloat(obj,GameProperties.BUDGET_MULTIPLIER);
		blastRadius=CRJsonUtils.getFloat(obj,GameProperties.BLAST_RADIUS);
		Object bulletObj = obj.get(GameProperties.BULLET_ELEMENT);
		if (bulletObj!=null){
			bulletElement=(IGameElement) factory.assembleJSON(bulletObj);
		}
		
		Bench.start("IMPORT UNIT_STORAGE",BenchConsts.CAT_COMP_INIT);
		storage=(UnitStorage) factory.assembleJSON(obj.get(GameProperties.UNIT_STORAGE));
		Bench.end("IMPORT UNIT_STORAGE");

		bulletSpeed=CRJsonUtils.getFloat(obj,GameProperties.BULLET_SPEED);
		rateOfFire=CRJsonUtils.getFloat(obj,GameProperties.COMBAT_RATEOFFIRE);
		
		proximityDetonation=CRJsonUtils.getFloat(obj,GameProperties.PROXIMITY_DETONATION);
		bulletGravity=CRJsonUtils.getFloat(obj,GameProperties.BULLET_GRAVITY);
		bulletMass=CRJsonUtils.getFloat(obj,GameProperties.BULLET_MASS);
		bulletRadius=CRJsonUtils.getFloat(obj,GameProperties.BULLET_RADIUS);
		roleIdentifier=BPUtils.clear((String)obj.get(GameProperties.ID_MASTER));
		unitRootTag=(String)obj.get(GameProperties.UNIT_ROLE_TAG);
		bulletController=(String)obj.get(GameProperties.BULLET_CONTROLLER);
		unitResources=CRJsonUtils.importList((JSONObject)obj.get(GameProperties.RESOURCES),factory);
		
	}
	
	
	public Vector3f getBulletGravityVector() {
		if (vectorGravity == null) {
			vectorGravity = new Vector3f(0, bulletGravity, 0);
		}
		return vectorGravity;
	}
	
	//new each time because different units may be using this class
	public GMBulletDefines generateBulletDefines(DPSComponent dpsComponent) {
		GMBulletDefines bulletDefines = new GMBulletDefines();
		bulletDefines.mass = bulletMass;
		bulletDefines.radius = bulletRadius;
		bulletDefines.bulletDmg = dpsComponent.calcBulletDmg(rateOfFire);
		bulletDefines.shotSpeed = bulletSpeed;
		bulletDefines.explosionRadius = blastRadius;
		bulletDefines.radius = bulletRadius;
		bulletDefines.proximityDetonation = proximityDetonation;
		bulletDefines.controller = bulletController;
		bulletDefines.gravity = getBulletGravityVector();
		
		return bulletDefines;
	}
	public IGameEntity requestMasterEntity(float chanceNewEntity) {
		
		return storage.requestMasterEntity(this,chanceNewEntity);
	}
	public void atualizaUnitRole(IGameEntity clone, float budget) {
		storage.atualizaUnitRole(this,clone, budget, entMan);
		
	}

	
}
