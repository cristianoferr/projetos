package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;
/*
 * System: LoadEntitySystem
 * Test: TestGamePlayer
 * */

public class ChildComponent extends GameComponent {

	public ChildComponent(){
		super(GameComps.COMP_CHILD);
	}

	public String tag=null;
	public IGameElement elementSource=null;
	public IGameEntity madeBy=null;
	public boolean addRender=false;
	public boolean load=true;
	
	
	@Override
	public void free() {
		super.free();
		tag=null;
		load=true;
		elementSource=null;
		madeBy=null;
		addRender=false;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		tag=ge.getProperty(GameProperties.TAG_PARAM);
	}	
	
	
	@Override
	public IGameComponent clonaComponent() {
		ChildComponent ret = (ChildComponent) entMan.spawnComponent(ident);
		ret.tag=tag;
		ret.elementSource=elementSource;
		ret.load=load;
		ret.madeBy=madeBy;
		ret.addRender=addRender;
		return ret;
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
		firstTick=false;
		load=false;
	}
	
	public Vector3f getElementDimension() {
		Vector3f dim=new Vector3f(elementSource.getPropertyAsFloat(GameProperties.WIDTH),elementSource.getPropertyAsFloat(GameProperties.HEIGHT),elementSource.getPropertyAsFloat(GameProperties.DEPTH));
		return dim;
	}
	
	public Vector3f getElementPosition() {
		PositionComponent posC = ECS.getPositionComponent(this);
		Vector3f posc=posC.getPos();
		return posc;
	}
}
