package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.controls.GMBulletDefines;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IBombControl;
import com.jme3.math.Vector3f;

public class BulletComponent extends GameComponent {

	//shotSpeed is the speed (not the force) in which the bullet will be ejected... force is mass relevant
	public final Vector3f bulletDirection=new Vector3f();
	public String elementID;//bulletEntity

	public PositionComponent sourcePosition;
	
	//this will store the shooting position of the bullet relative to its body source (the cannon and angle may differ, so this is necessary_
	public final Vector3f relSourcePos=new Vector3f();
	public GMBulletDefines bulletDefines;
	
	public IBombControl bulletNode;
	public IGameEntity bulletEnt;
	
	
	public BulletComponent(){
		super(GameComps.COMP_BULLET);
	}
	
	@Override
	public void free() {
		super.free();
		bulletNode=null;
		bulletEnt=null;
		bulletDefines=null;
		sourcePosition=null;
		elementID=null;
		bulletDirection.zero();
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		//this shouldnt be cloned...
		return null;
	}
	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
		
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

}
