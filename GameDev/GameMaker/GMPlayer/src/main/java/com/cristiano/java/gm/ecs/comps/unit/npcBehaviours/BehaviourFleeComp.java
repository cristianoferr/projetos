package com.cristiano.java.gm.ecs.comps.unit.npcBehaviours;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.systems.unit.npcBehaviours.BehaviourSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class BehaviourFleeComp extends AbstractNPCBehaviours {

	public final Vector3f targetInceptionPoint=new Vector3f();
	public final Vector3f targetAntiPos=new Vector3f();

	public BehaviourFleeComp(){
		super(GameComps.COMP_BEHAVIOR_FLEE);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public void free() {
		super.free();
	}

	@Override
	public IGameComponent clonaComponent() {
		BehaviourFleeComp ret = new BehaviourFleeComp();
		finishComponent(ret);
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
		
		
		system.calculateInterceptVector(target,this,targetInceptionPoint);
		targetAntiPos.set(target.lastRelPos);
		targetAntiPos.negateLocal();
		_targetLocation.set(_entityPos).addLocal(targetAntiPos);
		system.followPosition(targetAntiPos,1,0,this);
		
	}
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
}
