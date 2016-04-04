package com.cristiano.galactic.model.Entity.Abstract;


import java.util.Vector;

import com.cristiano.cyclone.forceGenerator.ActuatorController;
import com.cristiano.cyclone.forceGenerator.actuators.ActuatorGroup;
import com.cristiano.cyclone.utils.ClockWatch;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Entity.Logic.InternalLogic;
import com.cristiano.galactic.model.Entity.Logic.SlotGrouping;
import com.cristiano.galactic.model.Entity.Logic.representation.Memory;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryItem;
import com.cristiano.galactic.model.Entity.actuators.ItemSlot;
import com.cristiano.galactic.model.Entity.control.Control;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.ControlEnum;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;



/*
 * Essa classe é básica para qualquer entidade que receba ordens e comandos
 */

public abstract class ArtificialEntity extends Item {
//HashMap<String, String> cmdAvail=new HashMap<String, String>();
//HashMap<String,Class<AbstractCommand>> cmdConnect=new HashMap<String,Class<AbstractCommand>>();

private Vector<Control> activeControls=new Vector<Control>();
private InternalLogic itemLogic;





	public ArtificialEntity(int id,DataManager dataManager,Ware shipWare, String name) {
		super(id,dataManager,dataManager.getGeomFromWare(shipWare), name,EntityType.ET_ARTIFICIAL_ENTITY);
		setShipWare(shipWare);
		 itemLogic=new InternalLogic(this);
	//	connectCMD(CommandManager.CMD_MOVETO,CommandMoveTo.class);
	//	connectCMD(CommandManager.CMD_ORIENTTO,CommandOrientTo.class);
	}
	
	
	

	public void activateKeyGroup(Enum ctl){
	//	if (activeControls.contains(control))return;
		//System.out.println("IC: control="+ctl);
		Control control=new Control(getDataManager().getControlManager().getControl(ctl));
		addActiveControl(control);
		
	}
	public void addActiveControl(Control control){
		for (int i=0;i<activeControls.size();i++) {
			if (activeControls.get(i)!=null){
		
				try {
					if (activeControls.get(i).getSlotGroupingName().equals(control.getSlotGroupingName())){
						return;
					}
				} catch (Exception e) {
					Galactic.throwError("ArtificialEntity():"+e.getMessage());
				}
				
				
			}
		}
		activeControls.add(control);
	}
	
	public void activateKeyGroup(ControlEnum ctl,MemoryItem target){
		//	if (activeControls.contains(control))return;
			//System.out.println("IC: control="+ctl);
			Control control=new Control(getDataManager().getControlManager().getControl(ctl));
			control.setTarget(target);
			control.setIntensity(1);
			addActiveControl(control);
			
		}
	
public void deactivateKeyGroup(Enum ctl) {
	Control control=new Control(getDataManager().getControlManager().getControl(ctl));
	String ctlName=control.getSlotGroupingName();
	for (int i=0;i<activeControls.size();i++) {
			try {
				if (activeControls.get(i).getSlotGroupingName().equals(ctlName)){
					activeControls.remove(i);
				}
			} catch (Exception e) {
				Galactic.throwError("ArtificialEntity():"+e.getMessage());
			}
		}
		
	}
	
	public void activateControl(Enum ctl,double intensity){
		//	if (activeControls.contains(control))return;
		//System.out.println("IC: control="+ctl);
		Control controlLib=getDataManager().getControlManager().getControl(ctl);
			Control control=new Control(ctl,controlLib.getSlotGroupingName(),intensity);
			addActiveControl(control);
			
		}
	
	/**
	 * ActivateGroup: Ativa um grupo de Slots (sg) usando o controle (control)
	 * Grupo de slots pode se referir a motores, armas ,etc e o controle dá mais detalhes
	 * sobre o mesmo (alvo, força, etc) 
	 * @param sg
	 * @param control
	 */
	public void activateGroup(SlotGrouping sg,Control control){
		//sg=nulo implica que não é um slot  
		if (sg==null){
			ActuatorGroup ag=getSteering().getGroup(control.getSlotGroupingName());
			double f=0;
			//Thrusters é tratado pela engine física
			if (ag.getType().equals(ActuatorController.ACT_THRUSTER)){
				f=getPropertyAsDouble(PropertyEnum.DTL_THRUST_POWER_TEMP);
				f*=6;
			}
			//System.out.println("SG:"+control.getSlotGroupingName()+" F:"+f);
			getSteering().activateGroup(ag,getBody(),f);
		}else{
			//Se não for Thruster então é ativado o slot da nave
			sg.activate(this,control);
		}
	}
	
	
	public void activateGroup(Control control){
		activateGroup(getSlotGrouping(control.getSlotGroupingName()),control);
	}
	
	public ActuatorController getSteering(){
		return getBody().getSteering();
	}
	

	
	public Vector<ItemSlot> getSlotsFromWareGroup(WareGroup wg){
		return itemLogic.getFitting().getSlotsInGroup(wg);
	}

	
	

	
	public void setBT(String bt){
		itemLogic.setBT(bt);
	}
	public void setTarget(Item item){
		getItemLogic().getMemory().setTarget(getItemLogic().getMemory().getEntity(item));
	}
	public void setTarget(MemoryItem item){
		getItemLogic().getMemory().setTarget(item);
	}
	
	public MemoryItem getTarget(){
		return getItemLogic().getMemory().getTarget();
	}
	public void setTarget(Vector3 item){
		getItemLogic().getMemory().setTarget(getItemLogic().getMemory().getEntity(item));
	}
	
	
	
	public void turn(float time){
		ClockWatch.startClock("ArtificialEntity.turn");
		super.turn(time);
		//if (cmdStack.size()>0)cmdStack.elementAt(0).turn();
		itemLogic.turn(time);
		
		if (Double.isNaN(getCoord().x)){
			Galactic.log("nan..."+getName());
		}
			
		for (int i=activeControls.size()-1;i>=0;i--){
		//	String ctl=activeControls.get(i);
			//Control control=getDataManager().getControlManager().getControl(ctl);
			Control control=activeControls.get(i);
			if (control.getTime()!=-1){
				control.decTime(time);
			}
			activateGroup(control);
			if ((control.getTime()<=0) && (control.getTime()!=-1)){
				activeControls.remove(i);
			}
		}
		//activeControls.clear();
		ClockWatch.stopClock("ArtificialEntity.turn");
	}
	 
	public void removeActiveControl(Control control ){
		for (int i=0;i<activeControls.size();i++) {
			if (activeControls.get(i)!=null){
		
				if (activeControls.get(i).getSlotGroupingName().equals(control.getSlotGroupingName())){
					activeControls.remove(i);
					return;
				}
			}
		}
	}
	
	
	public int addCargo(Ware ware,int qtd){
		return itemLogic.addCargo(ware, qtd);
	}
	
	public float getCargoFree(){
		return itemLogic.getCargoFree();
	}
	
	public int  getWareCargoHold(Ware ware){
		return itemLogic.getWareCargoHold(ware,false);
	}


	/**
	 * @return the itemLogic
	 */
	public InternalLogic getItemLogic() {
		return itemLogic;
	}


	public Memory getMemory() {
		return itemLogic.getMemory();
	}


	
}
