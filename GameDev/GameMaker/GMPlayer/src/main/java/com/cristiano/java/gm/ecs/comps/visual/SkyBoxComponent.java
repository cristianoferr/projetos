package com.cristiano.java.gm.ecs.comps.visual;

import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMESnippets;
import com.jme3.scene.Spatial;

/*
 * Dados do skybox a ser mostrado
 * */
public class SkyBoxComponent extends GameComponent {

	public IGameElement elSkyBox=null;
	public int size=256;
	public String futureName=null;//create skybox
	
	
	public String _down=null;
	public JMESnippets _snippets;
	public String _east;
	public String _north;
	public String _south;
	public String _up;
	public String _west;
	
	public SkyBoxComponent(){
		super(GameComps.COMP_SKYBOX);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elSkyBox=ge.getPropertyAsGE(GameProperties.SKYBOX);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		SkyBoxComponent ret = new SkyBoxComponent();
		ret.elSkyBox=elSkyBox;
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.SKYBOX, elSkyBox.exportToJSON());
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		//loadFromElement(getElement());
		elSkyBox=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.SKYBOX));
	}
	
	// MUlti-thread
		public Callable<Object> createSkybox = new Callable<Object>() {
			public Object call() throws Exception {
				Spatial generateSkyBox = _snippets.generateSkyBox(_west, _east, _north, _south, _up, _down);
				return generateSkyBox;
			}
		};
			
}
