package com.cristiano.java.gm.ecs.comps.unit.npcBehaviours;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.systems.unit.npcBehaviours.BehaviourSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.jme3.math.Vector3f;

public class BehaviourWanderComp extends AbstractNPCBehaviours {

	public float wanderRadius=0;
	public float wanderDistance=0;
	public float jitterStrength=0;
	public float degreeLimit=180;
	public float accelerationMult=1;
	
	public final Vector3f circlePoint=new Vector3f();
	
	//public Vector3f wanderPoint
	
	public float currentDegree=CRJavaUtils.random(0, 360);

	public BehaviourWanderComp(){
		super(GameComps.COMP_BEHAVIOR_WANDER);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		wanderRadius=ge.getPropertyAsFloat(GameProperties.WANDER_RADIUS);
		wanderDistance=ge.getPropertyAsFloat(GameProperties.WANDER_DISTANCE);
		jitterStrength=ge.getPropertyAsFloat(GameProperties.JITTER_STRENGTH);
		degreeLimit=ge.getPropertyAsFloat(GameProperties.ANGLE_LIMIT);
		accelerationMult=ge.getPropertyAsFloat(GameProperties.ACCELERATION_MULTI);
	}	
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.WANDER_RADIUS, wanderRadius);
		obj.put(GameProperties.WANDER_DISTANCE, wanderDistance);
		obj.put(GameProperties.JITTER_STRENGTH, jitterStrength);
		obj.put(GameProperties.ANGLE_LIMIT, degreeLimit);
		obj.put(GameProperties.ACCELERATION_MULTI, accelerationMult);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		wanderRadius=CRJsonUtils.getFloat(obj,GameProperties.WANDER_RADIUS);
		wanderDistance=CRJsonUtils.getFloat(obj,GameProperties.WANDER_DISTANCE);
		jitterStrength=CRJsonUtils.getFloat(obj,GameProperties.JITTER_STRENGTH);
		degreeLimit=CRJsonUtils.getFloat(obj,GameProperties.ANGLE_LIMIT);
		accelerationMult=CRJsonUtils.getFloat(obj,GameProperties.ACCELERATION_MULTI);
	}

	@Override
	public IGameComponent clonaComponent() {
		BehaviourWanderComp ret = new BehaviourWanderComp();
		finishComponent(ret);
		ret.wanderDistance=wanderDistance;
		ret.wanderRadius=wanderRadius;
		ret.jitterStrength=jitterStrength;
		ret.degreeLimit=degreeLimit;
		ret.accelerationMult=accelerationMult;
		return ret;
	}
	


	@Override
	public void executeBehavior(BehaviourSystem system, IGameEntity entity,float tpf) {
		float mult=1;
		if (CRJavaUtils.random()<0.5f){
			mult=-1;
		}
		currentDegree+=tpf*jitterStrength*mult;
		if (currentDegree>degreeLimit/2){
			currentDegree=degreeLimit/2;
		}
		if (currentDegree<-degreeLimit/2){
			currentDegree=-degreeLimit/2;
		}
		
		//I generate a line in front of the entity
		wanderDistance=10;
		circlePoint.set(_forwardVector).multLocal(wanderDistance);
		circlePoint.addLocal(_entityPos);
		
		//system.debugTools.setBlueArrow(_entityPos,circlePoint.subtract(_entityPos));
		
		float finalDegree=currentDegree+90;
		//a line pointing from the center is generated, rotated to a given angle...
		float angleFront = CRMathUtils.calcDegreesXZ(_entityPos,circlePoint);
		CRMathUtils.rotateVector2D(circlePoint,wanderRadius,finalDegree-angleFront,_targetLocation);
		_targetLocation.y=_entityPos.y;
		_targetRelPos.set(_targetLocation);
		_targetRelPos.subtractLocal(circlePoint);
		//system.debugTools.setYellowArrow(circlePoint,_targetRelPos);
		system.followPosition(_targetRelPos,accelerationMult,0,this);
	}
	@Override
	public void resetComponent() {
	}
}

