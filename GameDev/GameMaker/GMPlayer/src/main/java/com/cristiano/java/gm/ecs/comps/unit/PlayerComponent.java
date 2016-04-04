package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.utils.CRJavaUtils;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;

public class PlayerComponent extends GameComponent {

	public int idUser=0;
	private ActionController actionController; //automatically defined...
	
	public PlayerComponent(){
		super(GameComps.COMP_PLAYER);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		PlayerComponent ret = (PlayerComponent) entMan.spawnComponent(GameComps.COMP_PLAYER);
		ret.setElement(getElement());
		ret.idUser=idUser;
		return ret;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.PLAYER_ID, idUser);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		idUser=Integer.parseInt(obj.get(GameProperties.PLAYER_ID).toString());
	}
	
	
	////
	
	public ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			sendAction(name,isPressed);
		}
	};

	

	//public for testing purposes, otherwise shouldnt be acessed directly...
	public void sendAction(String name, boolean isPressed) {
		//Log.debug("Player.sendAction: "+name+" pressed:"+isPressed);
		actionController.sendAction(name, isPressed);
	}

	@Override
	public void resetComponent() {
	}

	public void addPlayerListeners(InputManager inputManager, String[] actions) {
		inputManager.addListener(actionListener,
				actions);
		if (CRJavaUtils.isAndroid()){
			
		}
		
	}

	public ActionController getActionController() {
		return actionController;
	}

	public void setActionController(ActionController actionController) {
		this.actionController=actionController;
		
	}
}
