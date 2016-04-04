package com.cristiano.java.gm.ecs.comps.unit.actuators;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.unit.actuators.WeaponSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.controls.GMBulletDefines;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IReceiveAction;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.jme3.noise.effects.GameEffects;
import com.cristiano.utils.Log;
import com.jme3.ai.navmesh.Plane;
import com.jme3.math.Vector3f;

public class WeaponComponent extends AbstractActuatorComponent implements IReceiveAction {

	public int angleLimit;
	public float currentAngle = 0;
	// degrees per second
	public float turnRate = 0;
	public float shotMargin;
	public IRigidBody physNode;
	public float mass;

	// internal (defined automatically for fast access)
	public float cooldown = 0;
	public UnitClassComponent unitClass;
	public DPSComponent dpsComponent;
	public GMBulletDefines bulletDefines;

	// used for math calc...
	public Plane _plane = new Plane();
	public final Vector3f _forwardVector = new Vector3f();
	public final Vector3f _rotatedRightVector = new Vector3f();
	public PositionComponent entityPositionC;
	public float height;
	public float length;
	public final Vector3f weaponBasePos = new Vector3f();
	public final Vector3f weaponFrontPos = new Vector3f();
	public PhysicsComponent physicsC;
	public int bulletEffectType = GameEffects.FX_SMOKE_FIRE;
	public Vector3f targetRelPosNormal = new Vector3f();

	// used to shoot bullets...
	public WeaponSystem weaponSystem;
	public IGameEntity owner;
	public boolean okToShoot = false;

	public WeaponComponent() {
		super(GameComps.COMP_ACTUATOR_WEAPON);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.angleLimit = ge.getPropertyAsInt(GameProperties.ANGLE_LIMIT);
		this.turnRate = ge.getPropertyAsFloat(GameProperties.TURN_RATE);
		this.shotMargin = ge.getPropertyAsFloat(GameProperties.SHOT_MARGIN);
		this.mass = ge.getPropertyAsFloat(GameProperties.MASS);
		bulletEffectType = ge.getPropertyAsInt(GameProperties.BULLET_EFFECT_TYPE);
	}

	@Override
	public IGameComponent clonaComponent() {
		WeaponComponent ret = new WeaponComponent();
		finishClone(ret);
		ret.angleLimit = angleLimit;
		ret.bulletEffectType = bulletEffectType;
		ret.turnRate = turnRate;
		ret.shotMargin = shotMargin;
		ret.bulletDefines = bulletDefines;
		ret.mass = mass;
		return ret;

	}

	

	public UnitClassComponent getRole() {
		if (unitClass == null) {
			Log.error("UnitRole is null");
			return null;
		}
		if (unitClass == null) {
			Log.error("unitRole.role is null");
		}
		return unitClass;
	}

	

	// if true the node mesh will be added to the parent mesh... wheels by
	// default arent.
	public boolean isAddingNode() {
		return false;
	}

	@Override
	public void resetComponent() {
		unitClass=null;
		dpsComponent=null;
		bulletDefines=null;
		physNode=null;
	}

	
	@Override
	public void processAction(String action, float mult, float tpf) {
		TargetComponent targetComponent = ECS.getTargetComponent(owner);
		weaponSystem.shootBullet(owner, this, targetComponent);
	}

	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}
	
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		loadFromElement(getElement());
	}
	
	public GMBulletDefines generateBulletDefines() {
		bulletDefines=unitClass.generateBulletDefines(dpsComponent);
			
		if (physicsC != null) {
			bulletDefines.owner = physicsC.controlBody;
		} else {
			Log.error("No controlbody defined for weapon");
		}

		return bulletDefines;
		
	}

	public float getRateOfFire() {
		return unitClass.rateOfFire;
	}

	public String getBulletID() {
		return unitClass.bulletID;
	}

	public float getBulletGravityDef() {
		return unitClass.bulletGravity;
	}

	public double getBulletSpeed() {
		return unitClass.bulletSpeed;
	}

}
