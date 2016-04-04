package com.cristiano.galactic.model.containers;


import java.util.HashMap;

import com.cristiano.cyclone.forceGenerator.ActuatorController;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.Entity.control.Control;
import com.cristiano.galactic.model.enums.ControlEnum;



/*
 * Essa classe possui todos os comandos que podem ser repassados para os objetos.
 * cada controle aponta para um grupo (e o grupo pode ter mais que um controle)
 */
public class ControlManager {
	
	
	private HashMap<ControlEnum,Control> controlMap=new HashMap<ControlEnum,Control>();
	
	public ControlManager(){
		addControl(ControlEnum.CTL_TURN_LEFT,ActuatorController.STEER_TURN_LEFT,1);
		addControl(ControlEnum.CTL_TURN_RIGHT,ActuatorController.STEER_TURN_RIGHT,1);
		addControl(ControlEnum.CTL_MOVE_LEFT,ActuatorController.STEER_THRUST_LEFT,1);
		addControl(ControlEnum.CTL_MOVE_RIGHT,ActuatorController.STEER_THRUST_RIGHT,1);
		addControl(ControlEnum.CTL_MOVE_UP,ActuatorController.STEER_THRUST_UP,1);
		addControl(ControlEnum.CTL_MOVE_DOWN,ActuatorController.STEER_THRUST_DOWN,1);
		addControl(ControlEnum.CTL_TURN_UP,ActuatorController.STEER_TURN_UP,1);
		addControl(ControlEnum.CTL_TURN_DOWN,ActuatorController.STEER_TURN_DOWN,1);
		addControl(ControlEnum.CTL_ROLL_LEFT,ActuatorController.STEER_ROTATE_LEFT,1);
		addControl(ControlEnum.CTL_ROLL_RIGHT,ActuatorController.STEER_ROTATE_RIGHT,1);
		addControl(ControlEnum.CTL_ENGINE_INC,Consts.CTL_GRP_ENGINE,1,0.1);
		addControl(ControlEnum.CTL_ENGINE_DEC,Consts.CTL_GRP_ENGINE,1,-0.1);
		addControl(ControlEnum.CTL_ENGINE_MAX,Consts.CTL_GRP_ENGINE,1);
		addControl(ControlEnum.CTL_ENGINE_STOP,Consts.CTL_GRP_ENGINE,0);
		addControl(ControlEnum.CTL_LASER,Consts.CTL_GRP_LASER,1);
		addControl(ControlEnum.CTL_WEAPON,Consts.CTL_GRP_WEAPON,1);
		
		
	}
	public Control getControl(Enum name){
		return controlMap.get(name);
	}
	
	
	public final void addControl(ControlEnum name,String sg,double intensity){
		controlMap.put(name, new Control(name,sg,intensity));
	}
	
	public void addControl(ControlEnum name,String sg,double intensity,double step){
		controlMap.put(name, new Control(name,sg,intensity,step));
	}
}
