package com.cristiano.java.gm.ecs.systems.unit.npcBehaviours;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcBehaviours.AbstractNPCBehaviours;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.GenericComponentAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.jme3.rigidBody.CharActions;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.Log;
import com.jme3.ai.navmesh.Plane;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class BehaviourSystem extends GenericComponentAbstractSystem {
	
	
	final Vector3f _targetPos=new Vector3f();
	

	public BehaviourSystem() {
		super(GameComps.TAG_COMPS_NPC_BEHAVIOURS);
		
	}
	
	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		AbstractNPCBehaviours comp=(AbstractNPCBehaviours) component;
	
		//debugTools.show(game.getRenderManager(), game.getViewPort());
		retrieveCommonProperties(ent,comp);
		
		//components focus on what they do... action methods will stay here
		if (comp._vehicle!=null){
			comp.executeBehavior(this, ent, tpf);
			//comp.actionController.sendAction(CharActions.STOP_MOVEMENT,1);
		}
	}
	

	public void calculateInterceptVector(TargetComponent target,AbstractNPCBehaviours comp,Vector3f targetInceptionPoint) {
		if (JMEUtils.intercept(target.lastRelPos, comp._targetVelocity, comp._vehicle.getMaxSpeed(),0, targetInceptionPoint)){
			comp._targetRelPos.set(targetInceptionPoint);
		}
	}

	

	public void followPosition(Vector3f _targetRelPos,float maxAcceleration,float idealDistance, AbstractNPCBehaviours comp) {
	    
		_targetPos.set(_targetRelPos);
		_targetPos.normalizeLocal();
	    
		comp._rotatedForwardVector.set(comp._forwardVector);
		JMEConsts.ROTATE_RIGHT.multLocal(comp._rotatedForwardVector);
	    comp._plane.setOriginNormal(Vector3f.ZERO, comp._rotatedForwardVector);
	    
	    float dot = 1 - comp._forwardVector.dot(_targetPos);
	    float angle = comp._forwardVector.angleBetween(_targetPos);
	    float accelStr=maxAcceleration;
	    if (idealDistance>0){
	    	accelStr-=idealDistance/comp._distanceTarget;
	    }

	    float anglemult = 1;//FastMath.PI / 4.0f;
	    float speedmult = 0.3f;

	    if (angle > FastMath.QUARTER_PI) {
	        angle = FastMath.QUARTER_PI;
	    }
	    //left or right
	    if (comp._plane.whichSide(_targetRelPos) == Plane.Side.Negative) {
	        anglemult *= -1;
	    }
	    //backwards
	    if (dot > 1) {
	        speedmult *= -1;
	        anglemult *= -1;
	    }
	   if (anglemult>0){
		   comp.actionController.sendAction(CharActions.TURN_LEFT,angle);
	    } 
	   
	    if (anglemult<0){
	    	comp.actionController.sendAction(CharActions.TURN_RIGHT,angle);
	    }
	    if (speedmult>0){
	    	comp.actionController.sendAction(CharActions.MOVE_FORWARD,accelStr);
	    }
	    if (speedmult<0){
	    	comp.actionController.sendAction(CharActions.MOVE_BACKWARD,accelStr);
	    }
	}

	private void retrieveCommonProperties(IGameEntity ent, AbstractNPCBehaviours comp) {
		PositionComponent positionC = ECS.getPositionComponent(ent);
		PhysicsComponent physicsC = ECS.getPhysicsComponent(ent);
		if (physicsC==null){
			Log.fatal("No Physics Component!");
		}
		if (physicsC.controlBody==null){
			physicsC.firstTick=true;
			physicsC.dearchive();
			//Log.warn("ControlBody not found for entity: "+ent);
			return;
		}
		physicsC.controlBody.getForwardVector(comp._forwardVector).normalizeLocal();
		comp._entityPos.set(positionC.getPos());
		comp.initVehicle(physicsC.controlBody);
		comp._entityVelocity.set(comp._vehicle.getVelocity());
		
	}

	public TargetComponent retrieveTargetProperties(IGameEntity ent, AbstractNPCBehaviours comp) {
		TargetComponent targetC=ECS.getTargetComponent(ent);
		if (targetC!=null){
			comp._targetLocation.set(targetC.lastPosition);
			//comp._targetRelPos.set(targetC.lastPosition);
			//comp._targetRelPos.subtractLocal(comp._entityPos);
			
			comp._targetRelVelocity.set(targetC.lastVelocity);
			comp._targetVelocity.set(targetC.lastVelocity);
			comp._targetRelVelocity.subtractLocal(comp._entityVelocity);
			comp._targetRelPos.set(targetC.lastRelPos);
			
			comp._distanceTarget=targetC.lastRelPos.length();
			//debugTools.setRedArrow(comp._entityPos, targetC.lastRelPos);
			return targetC;
		}
		return targetC;
		
	}

	
}
