package com.cristiano.java.gm.ecs.comps.unit;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.ui.gameController.IGameJoystick;
import com.cristiano.utils.Log;

public class JoystickComponent extends GameComponent {

	public static int JOYSTICK_NONE = 0; // dont show
	public static int JOYSTICK_DEFAULT = 1; // default type
	public static int JOYSTICK_INVERTED = 2; // stick on right, buttons on left

	// current is the current joystick on screen, newType is the new one (if
	// changed by configuration)
	public int currentType = 0;
	public int newType = 0;
	
	public List<IGameJoystick> controllers= new ArrayList<IGameJoystick>();

	public IGameElement playerController;
	public  List<IGameElement> elControllers;

	public JoystickComponent() {
		super(GameComps.COMP_JOYSTICKS);
	}
	
	@Override
	public void free() {
		super.free();
		currentType=0;
		newType=0;
		controllers.clear();
		playerController=null;
		elControllers=null;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.newType = 1;//ge.getPropertyAsInt(NEW_TYPE);
		this.playerController=ge.getPropertyAsGE(GameProperties.PLAYER_CONTROLLER);
		if (playerController==null){
			Log.fatal("playerController is null!");
		}
		loadControllers();
	}

	private void loadControllers() {
		elControllers = playerController.getPropertyAsGEList(GameProperties.CONTROLLERS);
		
	}
	
	public void addJoystick(IGameJoystick control){
		if (control==null){
			Log.warn("Trying to add a null IGameJoystick... ");
			return;
		}
		controllers.add(control);
	}

	@Override
	public IGameComponent clonaComponent() {
		JoystickComponent ret = new JoystickComponent();
		finishClone(ret);
		ret.newType = newType;
		ret.playerController = playerController;
		ret.elControllers = elControllers;
		ret.currentType = 0;
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.NEW_TYPE,newType);
		obj.put(GameProperties.PLAYER_CONTROLLER,playerController.exportToJSON());
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		newType=CRJsonUtils.getInteger(obj,GameProperties.NEW_TYPE);
		playerController=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.PLAYER_CONTROLLER));
		currentType=0;
		loadControllers();
	}

	@Override
	public void resetComponent() {

	}

	public void hide() {
		for (IGameJoystick control:controllers){
			control.hide();
		}
	
	}

	public void show() {
		for (IGameJoystick control:controllers){
			control.show();
		}

	}

	

	private void removeJoystick(IGameJoystick control) {
		control.remove();
		
		
	}
	public void removeJoysticks() {
		for (IGameJoystick control:controllers){
			removeJoystick(control);
		}
		controllers.clear();
		
	}

	

	

}
