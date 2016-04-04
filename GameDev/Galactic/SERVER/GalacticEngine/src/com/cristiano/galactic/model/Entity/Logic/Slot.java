package com.cristiano.galactic.model.Entity.Logic;

import com.cristiano.cyclone.forceGenerator.actuators.Actuator;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;


/*
 * Um "Slot" � um objeto que representa uma determinada funcionalidade da entidade
 * pode ser um motor, um thruster, um laser, etc e � criado um slot para cada ModelData 
 * (no sentido de que n�o � replicado o slot para cada entidade)
 * ItemSlot � criado em cada entidade, contendo dados referentes � entidade em quest�o
 * e aponta para c�.   
 */
public class Slot extends Actuator{
	WareGroup wareGroup;
	ModelData modelData;
	Ware defaultWare=null;
	public Slot(ModelData modelData,WareGroup wareGroup,Vector3 position,Vector3 normal,boolean addDefault){
		super(position,normal);
		setInternal(false);
		this.modelData=modelData;
		this.wareGroup=wareGroup;
		if (addDefault) defaultWare=wareGroup.getDefaultWare();

	}
	public Slot(ModelData modelData,WareGroup wareGroup,Vector3 position,Vector3 normal,Ware defaultWare){
		super(position,normal);
		this.modelData=modelData;
		this.wareGroup=wareGroup;
		this.defaultWare=defaultWare;

	}
	
	public String getType(){
		return getWareGroup().getName();
	}
	/**
	 * @return the wareGroup
	 */
	public WareGroup getWareGroup() {
		return wareGroup;
	}
	public Ware getDefaultWare() {
		return defaultWare;
	}
	
}
