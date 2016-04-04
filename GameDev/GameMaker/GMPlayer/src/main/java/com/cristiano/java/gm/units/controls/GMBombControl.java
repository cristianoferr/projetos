package com.cristiano.java.gm.units.controls;

import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.jme3.interfaces.IBombControl;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.interfaces.IControlReuse;
import com.cristiano.jme3.interfaces.IMakeEffects;
import com.cristiano.jme3.rigidBody.BombControl;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

//referenced by: defaultBulletController
public class GMBombControl extends BombControl implements IBombControl {

	private IControlReuse reuse;
	private Object definingObject;
	private String key;
	private IMakeEffects smokeFX;
	private GMBulletDefines gmdefines;


	public GMBombControl(AssetManager manager, CollisionShape shape, GMBulletDefines bulletDefines) {
		super(manager, shape, bulletDefines);
		this.gmdefines = bulletDefines;
	}

	protected void removeThis() {
		super.removeThis();
		if (reuse != null) {
			// reuse.reuseObject(key,definingObject);
		}
	}

	protected void addGhostEffect() {
		super.addGhostEffect();
		if (smokeFX == null) {
			Log.error("No SmokeFX for bomb...");
			return;
		}
		smokeFX.attachToNode(spatial.getParent(), getPhysicsLocation());
		smokeFX.emmit(false);
		smokeFX.dieIn(defines.timeToEffectDie);

		IMakeEffects explosionFX = defines.effectMaker.requestFX(defines.effectExplosion);
		explosionFX.emmit(true);
		smokeFX = null;
	}

	protected void collidedWith(PhysicsCollisionObject object, PhysicsCollisionEvent event) {
		super.collidedWith(object, event);
		if (object instanceof IControlBody) {
			Log.debug(this + ":: CollidedWith:" + object);
			// TODO: definir dano levando em conta a distancia...
			dispatchEvent(object);
		}
	}

	private void dispatchEvent(Object object) {
		boolean critical = CRJavaUtils.random() > defines.chanceToCrit;
		if (defines.eventHandler == null) {
			Log.error("No eventHandler on defines...");
			return;
		}
		
		defines.eventHandler.dispatchEvent(LogicConsts.EVENT_DMG_RECEIVED, defines.getBulletDmg(critical), critical, defines.owner, (IControlBody) object);
		killBullet();
	}

	public void setSpatial(Spatial spatial) {
		super.setSpatial(spatial);
		if (defines.effectMaker == null) {
			Log.error("No effectMaker defined for the bomb...");
			return;
		}
		smokeFX = defines.effectMaker.requestFX(defines.effectType);
		smokeFX.attachToNode((Node) spatial, Vector3f.ZERO);
		smokeFX.emmit(true);
	}

	public void setPhysicsSpace(PhysicsSpace space) {
		super.setPhysicsSpace(space);

		if (defines.gravity == null) {
			Log.error("No gravity defined for bullet...");
			return;
		}
		setGravity(defines.gravity);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		checkProximityDetonation();
	}

	private void checkProximityDetonation() {
		if (gmdefines.targetPosition != null) {
			float distance = getBombPosition().distance(gmdefines.targetPosition.getPos());
			if (distance < gmdefines.proximityDetonation) {
				//Log.debug("Detonating bomb by proximity: "+distance);
				dispatchEvent(gmdefines.targetBody);
			}
		}
	}

	@Override
	public void setReuseManager(IControlReuse reuse, String key, Object definingObject) {
		this.reuse = reuse;
		this.key = key;
		this.definingObject = definingObject;
	}

	

}
