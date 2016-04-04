package com.cristiano.galactic.model.Entity.Logic.representation;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.cristiano.cyclone.utils.ClockWatch;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.controller.messenger.Coord;
import com.cristiano.galactic.model.Entity.Abstract.AbstractGameObject;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;


/*
 * Essa classe é responsável por armazenar todo o conhecimento que a entidade
 * possui sobre o mundo externo.  Tudo o que se refere a Inteligência deve
 * tirar suas informações daqui.
 */
public class Memory extends BTWrapper{
	
	public static final String FACT_SELFPOSITIONX="FactSelfPositionX";
	public static final String FACT_SELFPOSITIONY="FactSelfPositionY";
	public static final String FACT_SELFPOSITIONZ="FactSelfPositionZ";
	public static final String FACT_SELFVELOCITY="fact_SelfVelocity";
	public static final String FACT_SELFVELOCITYX="fact_SelfVelocityX";
	public static final String FACT_SELFVELOCITYY="fact_SelfVelocityY";
	public static final String FACT_SELFVELOCITYZ="fact_SelfVelocityZ";
	public static final String FACT_TARGETDISTANCE="fact_TargetDistance";
	public static final String FACT_TARGETDISTANCEX="fact_TargetDistanceX";
	public static final String FACT_TARGETDISTANCEY="fact_TargetDistanceY";
	public static final String FACT_TARGETDISTANCEZ="fact_TargetDistanceZ";
	public static final String FACT_TARGETPOSITIONX="fact_TargetPositionX";
	public static final String FACT_TARGETPOSITIONY="fact_TargetPositionY";
	public static final String FACT_TARGETPOSITIONZ="fact_TargetPositionZ";
	public static final String FACT_TARGETVELOCITY="fact_TargetVelocity";
	public static final String FACT_TARGETVELOCITYX="fact_TargetVelocityX";
	public static final String FACT_TARGETVELOCITYY="fact_TargetVelocityY";
	public static final String FACT_TARGETVELOCITYZ="fact_TargetVelocityZ";
	public static final String FACT_TARGETRELVELOCITY="fact_TargetRelVelocity";
	public static final String FACT_TARGETRELVELOCITYX="fact_TargetRelVelocityX";
	public static final String FACT_TARGETRELVELOCITYY="fact_TargetRelVelocityY";
	public static final String FACT_TARGETRELVELOCITYZ="fact_TargetRelVelocityZ";
	public static final String FACT_TARGETFRONTANGLE="fact_TargetFrontAngle";
	public static final String FACT_TARGETHORIZONTALANGLE="fact_TargetHorizontalAngle";
	public static final String FACT_TARGETVERTICALANGLE="fact_TargetVerticalAngle";
	public static final String FACT_SELFROCVERTICAL="fact_TargetROCVertical";
	public static final String FACT_SELFROCHORIZONTAL="fact_TargetROCHorizontal";
	public static final String FACT_SELFROTATIONMAGNITUDE="fact_SelfRotationMagnitude";


	//CMPT que retornam Vector3
	public static final String cmpt_rel_position_to_target="cmpt_rel_position_to_target";
	public static final String cmpt_rel_velocity_to_target="cmpt_rel_velocity_to_target";
	
	
	HashMap<AbstractGameObject, MemoryItem> entities=new HashMap<AbstractGameObject, MemoryItem>();
	private MemoryItem self;
	
	
	public Memory(ArtificialEntity entityOwner){
		super(entityOwner);
		setSelf(getEntity(entityOwner));
		//entityOwner.getDataManager().
 
	}
	
	public Vector3 computeVector3(String cmpt,MemoryDTO parms){
		if (cmpt.equals(cmpt_rel_position_to_target)){
			MemoryItem mItem=parms.getItem(0);
			Vector3 startPos=parms.getVector(1);
			return mItem.getDeltaPosition(startPos);
		}
		if (cmpt.equals(cmpt_rel_velocity_to_target)){
			
			MemoryItem mItem=parms.getItem(0);
			
			Vector3 startPos=parms.getVector(1);
			return mItem.getDeltaPosition(startPos);
		}
		return null;
	}
		
	
	public void registerItem(Item item){
		entities.put(item, new MemoryItem(entityOwner,item));
	}
	
	public void registerItem(Coord coord){
		entities.put(coord, new MemoryItem(entityOwner,coord.getVector()));
	}
	
	public void unregisterItem(AbstractGameObject item){
		entities.remove(item);
	}
	
	public MemoryItem getEntity(Vector3 item){
		if (!entities.containsKey(item)) registerItem(new Coord(item));
		return entities.get(item);
	}
	
	public MemoryItem getEntity(Item item){
		if (!entities.containsKey(item)) registerItem(item);
		return entities.get(item);
	}
	
	
	
	
	
	
	public double getFact(String name){
		
		MemoryItem target=getTarget();
		
		
		if (name.equals(FACT_SELFPOSITIONX)){return entityOwner.getCoord().getX();}
		if (name.equals(FACT_SELFPOSITIONY)){return entityOwner.getCoord().getY();}
		if (name.equals(FACT_SELFPOSITIONZ)){return entityOwner.getCoord().getZ();}
		if (name.equals(FACT_SELFVELOCITY)){return entityOwner.getVelocity().magnitude();}
		if (name.equals(FACT_SELFVELOCITYX)){return entityOwner.getVelocity().getX();}
		if (name.equals(FACT_SELFVELOCITYY)){return entityOwner.getVelocity().getY();}
		if (name.equals(FACT_SELFVELOCITYZ)){return entityOwner.getVelocity().getZ();}
		
		if (name.equals(FACT_SELFROCVERTICAL)){return getSelf().getROCVertical();}
		if (name.equals(FACT_SELFROCHORIZONTAL)){return getSelf().getROCHorizontal();}
		if (name.equals(FACT_SELFROTATIONMAGNITUDE)){return entityOwner.getRotation().magnitude();}		
		
		if (target==null) {return 0;}
		if (name.equals(FACT_TARGETFRONTANGLE)) {return target.getFrontAngle();}
		if (name.equals(FACT_TARGETHORIZONTALANGLE)){return target.getHorizontalAngle();}
		if (name.equals(FACT_TARGETVERTICALANGLE)){return target.getVerticalAngle();}
		if (name.equals(FACT_TARGETDISTANCE)){return target.getDistance();}
		if (name.equals(FACT_TARGETDISTANCEX)){return target.getDistanceX();}
		if (name.equals(FACT_TARGETDISTANCEY)){return target.getDistanceY();}
		if (name.equals(FACT_TARGETDISTANCEZ)){return target.getDistanceZ();}
		if (name.equals(FACT_TARGETPOSITIONX)){return target.getPositionX();}
		if (name.equals(FACT_TARGETPOSITIONY)){return target.getPositionY();}
		if (name.equals(FACT_TARGETPOSITIONZ)){return target.getPositionZ();}

		
		if (name.equals(FACT_TARGETVELOCITY)){return target.getVelocity().magnitude();}
		if (name.equals(FACT_TARGETVELOCITYX)){return target.getVelocity().x;}
		if (name.equals(FACT_TARGETVELOCITYY)){return target.getVelocity().y;}
		if (name.equals(FACT_TARGETVELOCITYZ)){return target.getVelocity().z;}
		if (name.equals(FACT_TARGETRELVELOCITY)){return target.getRelVelocityMag();}
		if (name.equals(FACT_TARGETRELVELOCITYX)){return target.getRelVelocityX();}
		if (name.equals(FACT_TARGETRELVELOCITYY)){return target.getRelVelocityY();}
		if (name.equals(FACT_TARGETRELVELOCITYZ)){return target.getRelVelocityZ();}
		
		

		return 0;
	}
	
	

	//Função temporária... idealmente quem deverá atualizar as posições são os sensores da entidade
	public void turn() {
		Vector<MemoryItem> remove=new Vector<MemoryItem>();
		ClockWatch.startClock("Memory.turn");
		Iterator iterator = entities.keySet().iterator();
	    while (iterator.hasNext()) {
	    	AbstractGameObject key = (AbstractGameObject) iterator.next();
	    	MemoryItem item=entities.get(key);
	    	
	    	//Stub para futura implementação... self deve estar sempre atualizado.
	    	
	    	if (item.getRefersTo()==entityOwner) {item.update();}
	    	else {
	    		if (!item.getRefersTo().isAlive()){
	    			remove.add(item);
	    		} else{
	    			item.update();
	    		}
	    	}
	    	
		}
	    
	    for (int i=0;i<remove.size();i++){
	    	entities.remove(remove.get(i));
	    }
	    ClockWatch.stopClock("Memory.turn");
		
	}

	
	
	
	public void setTarget(MemoryItem param) {
		getContext().setVariable("CurrentTarget",param);
	}
	public MemoryItem getTarget() {
		return (MemoryItem)getContext().getVariable("CurrentTarget");
	}

	public String getVariableS(String name){
		return getContext().getVariable("CurrentTarget").toString();
	}

	public void setVariable(String name,Object param) {
		getContext().setVariable(name,param);
	}

	public void setSelf(MemoryItem self) {
		this.self = self;
	}

	public MemoryItem getSelf() {
		return self;
	}
	
}
