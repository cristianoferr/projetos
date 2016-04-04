package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.mechanics.ApplyForceComponent;
import com.cristiano.java.gm.ecs.comps.unit.BulletComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.SpeedComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.interfaces.IBombControl;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;

/*
 * Tested in: TestActuators
 * */
public class BulletSystem extends JMEAbstractSystem {

	public BulletSystem() {
		super(GameComps.COMP_BULLET);
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
		// debugTools=new DebugTools(this.game.getAssetManager());
		initReuseComponent();

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		BulletComponent comp = (BulletComponent) component;
		if (component.isFirstTick()) {
			comp.firstTick = false;
			/*if (!ent.containsComponent(GameComps.COMP_TRANSIENT)) {
				return;
			}*/
			

			IGameEntity bulletEnt = reuseC.requestEntityFromElement(comp.elementID);
			if (bulletEnt == null) {
				Log.warn("Bullet mould hasnt been spawned...");
				return;
			}
			//Log.info("Launch bullet:"+component);
			launchBullet(ent, comp, bulletEnt);
			return;
		}

		/*if (comp.bulletNode != null) {
			if (comp.bulletNode.isDead()) {
				//entMan.removeEntity(comp.bulletEnt);
				//removeReusableComponent(comp);
			}
		}*/
		ent.removeComponent(component);

	}

	private void launchBullet(IGameEntity ent, BulletComponent comp, IGameEntity masterBullet) {
		RenderComponent compRenderMaster = (RenderComponent) masterBullet.getComponentWithIdentifier(GameComps.COMP_RENDER);

		// PhysicsComponent physEntC=(PhysicsComponent)
		// ent.getComponentWithIdentifier(GameComps.COMP_PHYSICS);

		IGameEntity entBullet = entMan.createEntity();
		entMan.addComponent(GameComps.COMP_TRANSIENT, entBullet);
		RenderComponent compRender = generateRenderComponent(compRenderMaster, entBullet);
		PhysicsComponent compPhysics = generatePhysicsComponent(ent, comp, entBullet);

		IRigidBody bulletNode = generatePhysicsShape(comp, compPhysics);
		bulletNode.setReuseManager(entMan, LogicConsts.OBJECT_BULLET, entBullet);
		comp.bulletEnt=entBullet;
		
		comp.addInfo("launchBullet");

		initBulletPosition(comp, compPhysics);
	}

	private void initBulletPosition(BulletComponent comp, PhysicsComponent compPhysics) {
		compPhysics.relSourcePos = comp.relSourcePos;
		compPhysics.sourcePosition = comp.sourcePosition;
		//game.addDebugBox(comp.sourcePosition.getPos().add(0, 20, 0), "goal");

		// compPhysics.node.setLocalTranslation(comp.relSourcePos.addLocal(comp.sourcePosition.getPos()));
		// Log.debug("Shooting bullet from: "+comp.relSourcePos+" Speed:"+comp.bulletDefines.shotSpeed+" Direction:"+comp.bulletDirection);
	}

	private IRigidBody generatePhysicsShape(BulletComponent comp, PhysicsComponent compPhysics) {
		SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(comp.bulletDefines.radius);
		// SphereCollisionShape bulletCollisionShape = new
		// SphereCollisionShape(0.1mp.radius);
		IBombControl bulletNode = (IBombControl) CRJavaUtils.instanciaClasse(comp.bulletDefines.controller, (AssetManager) game.getAssetManager(),
				(CollisionShape) bulletCollisionShape, comp.bulletDefines);
		bulletNode.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
		compPhysics.physNode = bulletNode;
		comp.bulletNode = bulletNode;
		return bulletNode;
	}

	private PhysicsComponent generatePhysicsComponent(IGameEntity ent, BulletComponent comp, IGameEntity entBullet) {
		SpeedComponent speed = ECS.getSpeedComponent(ent);

		PhysicsComponent compPhysics = (PhysicsComponent) entMan.addComponent(GameComps.COMP_PHYSICS, entBullet);
		ApplyForceComponent compApplyForce = (ApplyForceComponent) entMan.addComponent(GameComps.COMP_APPLY_FORCE, entBullet);
		compApplyForce.force.set(comp.bulletDirection);
		compApplyForce.force.multLocal(comp.bulletDefines.shotSpeed).addLocal(speed.velocity);
		// compApplyForce.force.y=0;
		compApplyForce.applyVelocity = true;
		compApplyForce.applyForce = false;
		return compPhysics;
	}

	private RenderComponent generateRenderComponent(RenderComponent compRenderMaster, IGameEntity entBullet) {
		RenderComponent compRender = (RenderComponent) entMan.addComponent(GameComps.COMP_RENDER, entBullet);
		if (compRenderMaster == null) {
			Log.error("Master Node is null... ");
			return compRender;
		}
		compRender.node = compRenderMaster.node.clone(false);
		// compRender.node.removeFromParent();
		Log.trace("generateRenderComponent:" + compRender.node);
		return compRender;
	}

}
