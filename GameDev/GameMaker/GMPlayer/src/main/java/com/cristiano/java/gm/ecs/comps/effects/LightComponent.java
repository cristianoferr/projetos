package com.cristiano.java.gm.ecs.comps.effects;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;

public class LightComponent extends GameComponent {

	public Light light=null;
	public IGameElement elLightControl;
	public IGameElement elColorObj;
	public ColorRGBA color;
	public boolean castShadow;
	public String tipo;
//	public AbstractShadowRenderer shadowRenderer;

	public LightComponent() {
		super(GameComps.COMP_LIGHT);
		
	}
	
	@Override
	public void free() {
		super.free();
		light=null;
		elColorObj=null;
		elLightControl=null;
		color=null;
		castShadow=false;
		tipo=null;
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elLightControl=ge.getPropertyAsGE(GameProperties.LIGHT_OBJ);
		elColorObj=ge.getPropertyAsGE(GameProperties.COLOR_OBJ);
		castShadow=(ge.getProperty(GameProperties.CAST_SHADOW).equals("1"));
		color=new ColorRGBA(elColorObj.getPropertyAsFloat("r"),elColorObj.getPropertyAsFloat("g"),elColorObj.getPropertyAsFloat("b"),elColorObj.getPropertyAsFloat("a"));
	}
	
	@Override
	public IGameComponent clonaComponent() {
		LightComponent ret = new LightComponent();
		ret.light=light;
		ret.elLightControl=elLightControl;
		ret.elColorObj=elColorObj;
		ret.castShadow=castShadow;
		ret.color=color;
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
		loadFromElement(getElement());
	}
}
