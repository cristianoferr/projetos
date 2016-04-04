package com.cristiano.java.gm.ecs.comps.unit.npcBehaviours;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.systems.unit.npcBehaviours.BehaviourSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.rigidBody.CharActions;
import com.cristiano.utils.Log;

public class BehaviourFollowComp extends AbstractNPCBehaviours {

	

	public float idealDistance;

	public BehaviourFollowComp(){
		super(GameComps.COMP_BEHAVIOR_FOLLOW);
	}
	@Override
	public void free() {
		super.free();
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.idealDistance=ge.getPropertyAsFloat(GameProperties.IDEAL_DISTANCE);
	}	

	@Override
	public IGameComponent clonaComponent() {
		BehaviourFollowComp ret = new BehaviourFollowComp();
		finishComponent(ret);
		ret.idealDistance=idealDistance;
		return ret;
	}

	@Override
	public void executeBehavior(BehaviourSystem system, IGameEntity entity,float tpf) {
		TargetComponent target = system.retrieveTargetProperties(entity,this);
		if (target==null){
			Log.info("Entity has no target set... exiting behaviour");
			entity.removeComponent(this);
			return;
		}
		
		if (_distanceTarget<idealDistance){
			actionController.sendAction(CharActions.STOP_MOVEMENT);
			return;
		}
		system.followPosition(_targetRelPos,1,idealDistance,this);
	}
	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.IDEAL_DISTANCE, idealDistance);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		idealDistance= CRJsonUtils.getFloat(obj, GameProperties.IDEAL_DISTANCE);	
	}
	
}
