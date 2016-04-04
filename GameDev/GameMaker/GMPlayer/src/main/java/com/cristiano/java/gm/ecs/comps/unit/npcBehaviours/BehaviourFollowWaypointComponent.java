package com.cristiano.java.gm.ecs.comps.unit.npcBehaviours;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.resources.RaceGoalComponent;
import com.cristiano.java.gm.ecs.systems.unit.npcBehaviours.BehaviourSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;

public class BehaviourFollowWaypointComponent extends AbstractNPCBehaviours {
	
	RaceGoalComponent goal=null;
	
	public BehaviourFollowWaypointComponent(){
		super(GameComps.COMP_BEHAVIOR_FOLLOW_WAYPOINT);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	@Override
	public IGameComponent clonaComponent() {
		BehaviourFollowWaypointComponent ret = new BehaviourFollowWaypointComponent();
		finishComponent(ret);
		
		return ret;
	}

	@Override
	public void executeBehavior(BehaviourSystem system, IGameEntity entity,float tpf) {
		if (!hasGoal(entity)){
			return;
		}
		
		if (goal.currDestination!=null){
			_targetRelPos.set(goal.currDestination).subtractLocal(_entityPos);
			system.followPosition(_targetRelPos,1,0,this);
		}
		
		/*
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
		system.followPosition(_targetRelPos,1,idealDistance,this);*/
	}

	
	private boolean hasGoal(IGameEntity entity) {
		goal=ECS.getRacegoalComponent(entity);
		return (goal!=null);
	}

	@Override
	public void resetComponent() {
		goal=null;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

	
}
