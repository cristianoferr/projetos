package com.cristiano.java.gm.ecs.comps.unit.sensors;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IControlBody;
import com.jme3.math.Vector3f;

public class TargetComponent extends GameComponent {

	public boolean isEnemy=false;
	public IGameEntity target;
	
	public float timeNotVisible; //time since last time the target was visible...
	
	public  Vector3f dimensions=new Vector3f();
	
	public final Vector3f lastPosition=new Vector3f();
	public final Vector3f lastRelPos=new Vector3f();
	public final Vector3f lastRelVelocity=new Vector3f();
	public final Vector3f lastVelocity=new Vector3f();
	
	//not to be used with cheating stuff... the idea is to get a smooth turret angle...
	public PositionComponent directPosition;
	public IControlBody targetBody;
	
	public TargetComponent(){
		super(GameComps.COMP_TARGET);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}	

	@Override
	public IGameComponent clonaComponent() {
		return null;
	}	
	@Override
	public void resetComponent() {
		target=null;
		isEnemy=false;
		lastPosition.set(Vector3f.ZERO);
		lastRelPos.set(Vector3f.ZERO);
		lastVelocity.set(Vector3f.ZERO);
		lastRelVelocity.set(Vector3f.ZERO);
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

	public IControlBody getTargetBody() {
		if (targetBody==null){
			targetBody=ECS.getPhysicsComponent(target).controlBody;
		}
		return targetBody;
	}
}
