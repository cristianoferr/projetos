package com.cristiano.java.gm.ecs.systems.unit.actuators;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.BulletComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.WeaponComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.EffectMaker;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.unit.PhysicsSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.jme3.rigidBody.CharActions;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.ai.navmesh.Plane;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/*
 * Tested in: TestActuators
 * */
public class WeaponSystem extends AbstractActuatorSystem {

	public final Vector3f _rotatedRightVector = new Vector3f();
	private float lastRandomAngle=0;

	public WeaponSystem() {
		super(GameComps.COMP_ACTUATOR_WEAPON);
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
	}

	@Override
	// ent=unit
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		super.iterateEntity(ent, component, tpf);
		WeaponComponent comp = (WeaponComponent) component;
		comp.cooldown += tpf;
		if (!initializaWeapon(ent, comp)) {
			return;
		}
		TargetComponent target = ECS.getTargetComponent(ent);
		if (target != null) {
			targetBehaviour(ent, comp, target, tpf);
		} else {
			noTargetBehaviour(ent,comp,tpf);
		}

	}

	private void noTargetBehaviour(IGameEntity ent, WeaponComponent comp, float tpf) {
		if (rotateTurret(comp, lastRandomAngle, tpf)){
			lastRandomAngle=CRJavaUtils.random(-comp.angleLimit / 2f,comp.angleLimit / 2f);
		}
		
	}

	private boolean initializaWeapon(IGameEntity ent, WeaponComponent comp) {
		if (comp.isFirstTick()) {

			comp.physicsC = ECS.getPhysicsComponent(ent);
			if (comp.physicsC==null){
				Log.fatal("Entity has no physics component!");
			}
			if (comp.physicsC.controlBody == null) {
				return false;
			}
			defineReusableData(ent, comp);
			if (comp.getRole() == null) {
				Log.error("No unitRole defined for weapon...");
				return false;
			}
			addWeaponNode(ent, comp);
			linkAction(ent, comp.physicsC.controlBody.getActionController(), comp);
		}
		return true;
	}

	private void linkAction(IGameEntity ent, ActionController actionController, WeaponComponent comp) {
		if (comp.addToAction > 0) {
			actionController.enableAction(CharActions.getActionNameFor(comp.addToAction), comp);
			comp.weaponSystem = this;
			comp.owner = ent;
		}

	}

	private void addWeaponNode(IGameEntity ent, WeaponComponent comp) {
		RenderComponent render = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
		if (render == null) {
			Log.error("No RenderComponent for Weapon: " + comp);
			return;
		}
		Spatial node = comp.getNode();
		CollisionShape modelShape = PhysicsSystem.getModelShape(node);
		IRigidBody body = PhysicsSystem.createNonControllableControl(modelShape, 0);
		comp.physNode = body;
		render.node.attachChild(node);
		node.setLocalTranslation(comp.getPosition());
		node.addControl(body);

	}

	public void defineReusableData(IGameEntity ent, WeaponComponent comp) {
		comp.firstTick = false;
		comp.dpsComponent = ECS.getDPSComponent(ent);
		if (comp.dpsComponent == null) {
			Log.fatal("Entity has no DPSComponent!");
			return;
		}
		comp.unitClass = ECS.getUnitRoleComponent(ent);
		if (comp.unitClass == null) {
			Log.fatal("Entity has no UnitRole!");
			return;
		}
		
		comp.entityPositionC = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);

		getBoundingDimensions(comp);

		generateBulletDefines(comp);
	}

	private void getBoundingDimensions(WeaponComponent comp) {
		if (comp.getNode() != null) {
			comp.weaponBasePos.set(comp.getNode().getLocalTranslation()).addLocal(0, comp.height, 0);
			BoundingBox bounds = (BoundingBox) comp.getNode().getWorldBound();
			if (bounds == null) {
				Log.error("Bounds null");
				return;
			}
			comp.height = bounds.getYExtent();
			comp.length = bounds.getZExtent();
		}
	}

	private void targetBehaviour(IGameEntity ent, WeaponComponent comp, TargetComponent target, float tpf) {

		if (comp.entityPositionC == null) {
			Log.error("WeaponComponent has no position defined.");
			comp.entityPositionC = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
			return;
		}

		Vector3f entPos = comp.entityPositionC.getPos();
		float gravity = comp.getBulletGravityDef();

		JMEUtils.intercept(target.lastRelPos, target.lastRelVelocity, comp.getBulletSpeed(), gravity, comp.targetRelPosNormal);
		//if (isPlayer(ent)) {
			//debugTools.setRedArrow(entPos.add(comp.weaponBasePos), comp.targetRelPosNormal);
		//}
		// comp.targetRelPosNormal.y=0;
		comp.targetRelPosNormal.normalizeLocal();

		PhysicsComponent physicsC = comp.physicsC;
		if (physicsC.controlBody == null) {
			Log.warn("No ControlBody defined...");
			return;
		}
		physicsC.controlBody.getForwardVector(comp._forwardVector).normalizeLocal();

		comp._rotatedRightVector.set(comp._forwardVector);
		JMEConsts.ROTATE_RIGHT.multLocal(comp._rotatedRightVector);
		comp._plane.setOriginNormal(entPos, comp._rotatedRightVector);
		float angleRad = comp._forwardVector.angleBetween(comp.targetRelPosNormal);
		float angle = FastMath.RAD_TO_DEG * angleRad;
		if (comp._plane.whichSide(target.lastPosition) == Plane.Side.Negative) {
			angle = -angle;
		}
		//Log.debug("Angle:"+angle);

		// calcTurretPoints(ent,comp, entPos);

		turnTurret(ent, comp, target, angle, tpf);

	}

	private void calcTurretPoints(IGameEntity entity, WeaponComponent comp, Vector3f entPos) {
		comp.weaponBasePos.set(comp.getNode().getLocalTranslation()).addLocal(0, comp.height, 0);//.subtractLocal(entPos);

		float angle2 = CRMathUtils.calcDegreesXZ(Vector3f.ZERO, comp.targetRelPosNormal);
		CRMathUtils.rotateVector2D(Vector3f.ZERO, comp.length, -angle2 + 90, comp.weaponFrontPos);
		comp.weaponFrontPos.multLocal(3f);
		comp.weaponFrontPos.addLocal(comp.weaponBasePos);
		comp.weaponFrontPos.y = 0;

		/*if ((CRJavaUtils.IS_DEBUG) && (!CRJavaUtils.IS_TEST)) {
			if (!isPlayer(entity)) {
				return;
			}
			debugTools.setPinkArrow(entPos, comp.weaponBasePos);
			debugTools.setBlueArrow(entPos.add(comp.weaponBasePos), comp.weaponFrontPos);
		}*/
	}

	private void turnTurret(IGameEntity ent, WeaponComponent comp, TargetComponent target, float angle, float tpf) {

		boolean ok =rotateTurret(comp, angle, tpf);

		comp.okToShoot = ok;
		if (ok) {
			shootBullet(ent, comp,target);
		}
	}

	private boolean rotateTurret(WeaponComponent comp, float angle, float tpf) {
		boolean ok=true;
		float angleDest = angle;
		if (angleDest > comp.angleLimit / 2) {
			angleDest = comp.angleLimit / 2;
			ok = false;
		}
		if (angleDest < -comp.angleLimit / 2) {
			angleDest = -comp.angleLimit / 2;
			ok = false;
		}
		float difTarget = Math.abs(comp.currentAngle - angleDest);
		float dif = tpf * comp.turnRate;
		if (dif > difTarget) {
			dif = difTarget;
		}

		dif = angleDest > comp.currentAngle ? +dif : -dif;
		comp.currentAngle += dif;
		Quaternion rotQuat1 = comp.getNode().getLocalRotation();
		rotQuat1.fromAngleAxis(comp.currentAngle * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);

		difTarget = Math.abs(comp.currentAngle - angleDest);
		if (dif > comp.shotMargin) {
			ok = false;
		}
		return ok;
	}

	public BulletComponent shootBullet(IGameEntity entity, WeaponComponent weaponC, TargetComponent target) {
		if (weaponC.cooldown < weaponC.getRateOfFire()) {
			return null;
		}
		if (!weaponC.okToShoot) {
			return null;
		}
		if (entity.containsComponent(GameComps.COMP_BULLET)){
			return null;
		}
		if (target==null){
			return null;
		}

		calcTurretPoints(entity, weaponC, ECS.getPositionComponent(entity).getPos());

		weaponC.cooldown = 0;
		// Log.debug("Shooting bullet from "+entity);
		BulletComponent bullet = (BulletComponent) getReusableComponent(GameComps.COMP_BULLET, true);
		entity.attachComponent(bullet);
		bullet.bulletDefines = weaponC.bulletDefines;
		bullet.bulletDefines.targetPosition=target.directPosition;
		bullet.bulletDefines.targetBody=target.getTargetBody();
		bullet.sourcePosition = weaponC.entityPositionC;
		//Log.debug("Pos:"+weaponC.entityPositionC.getPos());
		bullet.bulletDirection.set(weaponC.targetRelPosNormal);

		bullet.relSourcePos.set(weaponC.weaponBasePos);
		bullet.relSourcePos.addLocal(weaponC.weaponFrontPos);
		
		//Log.debug("weaponFrontPos:"+weaponC.weaponFrontPos+" base:"+weaponC.weaponBasePos+"    "+bullet.relSourcePos);
		//game.addDebugBox(bullet.relSourcePos, "aaa");
		bullet.elementID = weaponC.getBulletID();
		return bullet;
	}

	private void generateBulletDefines(WeaponComponent comp) {
		comp.generateBulletDefines();
		comp.bulletDefines.eventHandler = getGameEventSystem();
		comp.bulletDefines.effectMaker = new EffectMaker(0, entMan, getFXLibrary(), game.getAssetManager());
		comp.bulletDefines.effectType = comp.bulletEffectType;
		

	}

}
