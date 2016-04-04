package com.cristiano.java.gm.ecs.comps.misc;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class TestComponent1 extends GameComponent {

	public String valString=null;
	public float valFloat=0;
	
	public TestComponent1(){
		super(GameComps.COMP_TEST);
		
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		TestComponent1 ret = new TestComponent1();
		ret.valFloat=valFloat;
		ret.valString=valString;
		return ret;
	}
	
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put("valString",valString);
		obj.put("valFloat",valFloat);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		valFloat=(float) obj.get("valFloat");
		valString=(String) obj.get("valString");
		
	}
	
}
