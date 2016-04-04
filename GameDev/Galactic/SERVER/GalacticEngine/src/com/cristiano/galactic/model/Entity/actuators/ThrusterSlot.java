package com.cristiano.galactic.model.Entity.actuators;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.Slot;


public class ThrusterSlot extends ItemSlot {
private double power=-1;
private double currPower=0;
	public ThrusterSlot( ArtificialEntity item,Slot slot) {
		super(item,slot);
	}

	
	public boolean initialize(){
		super.initialize();
		//power=getValueDetail(PropertyManager.DTL_MAX_FORCE)*PhysicsConsts.massaKG;
		
		if (power<=0){
			Galactic.throwError("Power <=0");
		}
		//Galactic.printLog("thruster power:"+power, true);
		return true;
	}
	public void turn(float time){
		
		currPower=currPower*0.999;
		if (currPower<0.1)currPower=0.1;
		super.turn(time);
	}
	
	
	public double executeAction(float time){
		currPower=currPower+currPower*time;
		if (currPower>1){
			currPower=1;
		}
		
	//	Galactic.printLog("Currpower:"+currPower+" "+getItem()+" time:"+time, true);
		
		Vector3 force=getSlot().getOrientation().getMultiVector(getIntensity()*currPower*power*time*-1);
		force=getOwner().getBody().getTransformMatrix().transformDirection(force);

		//System.out.println("Thruster.executeAction="+getIntensity()+"  "+getIntensity()*power+" force="+force+" pos="+getSlot().getPos()+" abs="+getAbsPos());
		
		getOwner().getBody().addForceAtBodyPoint(force,getAbsPos() );
		//getItem().getBody().add
		
		return 0;
		
	}
}
