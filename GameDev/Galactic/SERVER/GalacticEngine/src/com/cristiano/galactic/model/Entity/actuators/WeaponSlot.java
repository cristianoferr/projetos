package com.cristiano.galactic.model.Entity.actuators;


import java.util.Date;

import com.cristiano.cyclone.math.HelperFunctions;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Entity.Bullet;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.Slot;
import com.cristiano.galactic.model.Entity.Logic.representation.Memory;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryDTO;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryItem;
import com.cristiano.galactic.model.enums.PropertyEnum;


public class WeaponSlot extends ItemSlot {
	private double delay=-1,speed=-1,range=-1,ttl;
	private Date lastShot=new Date();
	public WeaponSlot( ArtificialEntity item,Slot slot) {
		super(item,slot);

		
	}
	
	public boolean initialize(){
		super.initialize();
		delay=getValueDetail(PropertyEnum.DTL_COOLDOWN);
		if (delay<=0)
			Galactic.throwError("ROF Delay <=0");
		speed=getValueDetail(PropertyEnum.DTL_SPEED);
		if (speed<=0)
			Galactic.throwError("Speed <=0");
		range=getValueDetail(PropertyEnum.DTL_RANGE);
		ttl=range/speed*1000;
		return true;
	}
	
	public boolean isOkToShot(){
	Date now=new Date();
	int time=(int)(now.getTime()-lastShot.getTime());
	//System.out.println("time="+time+" delay="+delay);
	if (time>delay){
		lastShot=now;
		return true;
	}
	return false;
	
	}

	public double executeAction(float time){
		if (!isOkToShot()) return -1;
		Vector3 vel=new Vector3(1,0,0);
		
		double bulletSpeed=speed;
		
		MemoryItem target=getControl().getTarget();
		if (target==null){
			//TODO: melhorar esse método: tá muito feio
			target=getOwner().getTarget();
			
		}
		
		
		Vector3 relPos=getOwner().getTransformDirection(getAbsPos());
		relPos=getOwner().getBody().getPosition().getAddVector(relPos);
		
		if (target!=null){
			MemoryDTO parms=new MemoryDTO();
			parms.addItem(target);
			parms.addVector(relPos);
			vel=getOwner().getMemory().computeVector3(Memory.cmpt_rel_position_to_target, parms);
			Vector3 targetSpeed=target.getRelVelocity();
			
			//Vou limitar a velocidade baseado na distância (se for muito próximo então a velocidade do bullet é menor)
			double dist=target.getDistance();
			if (bulletSpeed>dist*10) bulletSpeed=dist*10;
			
			vel=HelperFunctions.intercept(vel, targetSpeed, bulletSpeed);
			if (vel==null){
				return 0;
			}
		}else{
			vel=getOwner().getTransformDirection(vel);
		}
		vel.normalise(bulletSpeed);
		//System.out.println("Shoot: target="+target+" vel:"+vel+" speed:"+speed );
		//System.out.println("mass:"+getItem().getBody().getMass()+"  relmass:"+getItem().getBody().getRelativisticMass()+" C:"+getItem().getBody().getVelocity().magnitude()/PhysicsConsts.C);
		
		
		
		Bullet s=Bullet.createBullet((int)ttl,getOwner(),vel,relPos);
		
		
		//s.init(10000,getItem().getTransformDirection(getSlot().getPos()),getItem().getTransformDirection(vel));
		//s.setVelocity(getItem().getVelocity().getAddVector(vel));
		
		return 0;
	}
}
