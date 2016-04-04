package com.cristiano.galactic.model.Entity.actuators;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.Slot;
import com.cristiano.galactic.model.enums.ItemGameProperties;
import com.cristiano.galactic.model.enums.PropertyEnum;


public class EngineSlot extends ItemSlot {
	double power=-1;
	double currPower=0;
	double targetPower=0;
	boolean initialized=false;
	
	public EngineSlot( ArtificialEntity item,Slot slot) {
		super(item,slot);
	}

	public double getStepPower(){
		return power/5;
	}
	
	public boolean initialize(){
		super.initialize();
		ArtificialEntity item=getOwner();
		if (item.getMass()==0)return false;
		
		//Qtd retorna quantas engines estão sendo usadas... 
		int qtd=item.getItemLogic().getFitting().getSlotsInGroup(item.getDataManager().getWareManager().getWareGroup(Consts.WARE_GROUP_ENGINE)).size();
		power=item.getMass()*item.getPropertyAsDouble(PropertyEnum.getCurr(ItemGameProperties.ACCEL))/qtd;
		//power*=100;
		
//		power=getValueDetail(PropertyManager.DTL_b)*PhysicsConsts.massaKG;
		if (power<=0){
			Galactic.throwError("Power <=0");
		}
		return true;
	}
	
public double executeAction(float time){
		if (!initialized){
			initialized=initialize();
		}
		if (!initialized){
			Galactic.throwError("Error initializing Slot");
		}
		targetPower=power*getIntensity();
		if (currPower<targetPower){
			currPower+=getStepPower()*time;
		}
		if (currPower>targetPower){
			currPower-=getStepPower()*time;
		}
		//System.out.println("currpower="+currPower+" stepPower="+getStepPower()+" time*stepPower="+(getStepPower()*time)+" vel:"+Formatacao.formatSpeed(getItem().getVelocity().magnitude()));
		//Vector3 force=getSlot().getOrientation().getMultiVector(getIntensity()*-currPower*time);
		Vector3 force=getSlot().getOrientation().getMultiVector(getIntensity()*-currPower*1000);
		force=getOwner().getBody().getTransformMatrix().transformDirection(force);

	//	System.out.println("Time:"+time+" Engine.executeAction="+getIntensity()+" x "+getIntensity()*power+" ="+force);
		
		getOwner().getBody().addForceAtBodyPoint(force,getAbsPos() );
		//getItem().getBody().add
		
		return getIntensity();
		
	}
public void turn(float time){
	super.turn(time);
	if (getIntensity()==0){
		currPower=0;
	}
}
}
