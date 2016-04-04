package com.cristiano.galactic.model.Entity.Logic;



import java.util.HashMap;
import java.util.Vector;

import com.cristiano.cyclone.forceGenerator.actuators.Actuator;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.actuators.EngineSlot;
import com.cristiano.galactic.model.Entity.actuators.ItemSlot;
import com.cristiano.galactic.model.Entity.actuators.ThrusterSlot;
import com.cristiano.galactic.model.Entity.actuators.WeaponSlot;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;


/*
 * Class ItemSlot: Contains all the wares equipped at their slot.
 * wareInfo: Controls the various "Details" that each equipped ware can have, such as: current energy, range, etc 
 */



public class Fitting {
ModelData modelData;
HashMap<Slot,ItemSlot> itemSlots=new HashMap<Slot,ItemSlot>();
ArtificialEntity item;



public Fitting(ArtificialEntity item){
	this.modelData=item.getShipWare().getModelData();
	this.item=item;
	for (int i=0;i<modelData.getSlots().size();i++){
		Slot slot=modelData.getSlots().get(i);
		ItemSlot itemSlot=createItemSlot(slot);
		itemSlot.setSlot(slot);
		itemSlots.put(slot,itemSlot);
		 
		itemSlot.setWare(slot.getDefaultWare());
	}
	
}

public void initialize(){
	
}

//What a name...
public void fitAllSlotsFromWareGroupWithWare(WareGroup wg,Ware ware){
	for (int i=0;i<modelData.getSlots().size();i++){
		Slot slot=modelData.getSlots().get(i);
		if (slot.getWareGroup()==wg){
			ItemSlot itemSlot=itemSlots.get(slot);
			itemSlot.setWare(ware);
		}
	}
}

public ItemSlot createItemSlot(Slot slot){
	if (slot.getWareGroup().getName().equals("Thruster")) {
		return new ThrusterSlot(item,slot);
	}
	if (slot.getWareGroup().getName().equals("Engine")) {
		return new EngineSlot(item,slot);
	}
	if (slot.getWareGroup().getName().equals("Weapon")) {
		return new WeaponSlot(item,slot);
	}
	if (slot.getWareGroup().getName().equals("Laser")) {
		return new WeaponSlot(item,slot);
	}
	return new ItemSlot(item,slot);
}

public Vector<ItemSlot> getSlotsInGroup(WareGroup wg){
	Vector<ItemSlot> v=new Vector<ItemSlot>();
	
	for (int i=0;i<modelData.getSlots().size();i++){
		Slot slot=modelData.getSlots().get(i);
		if (slot.getWareGroup()==wg) v.add(getWareAtSlot(slot));
	}
	return v;
	
}

public ItemSlot getWareAtPosition(Vector3 pos){
	for (int i=0;i<modelData.getSlots().size();i++){
		Slot slot=modelData.getSlots().get(i);
		if (pos.equals(slot.getPosition())) return itemSlots.get(slot);
	}
	return null;
}


public ItemSlot getWareAtSlot(Slot slot){
	return itemSlots.get(slot);
}
public boolean isWareCompatible(Ware ware,Vector3 pos){
	
	Actuator slot=modelData.getSlotAtPosition(pos);
	return isWareCompatible(ware, slot);
}

public boolean isWareCompatible(Ware ware,Actuator slot){
	if (slot==null) return false;
	if (ware==null) return true;
	if (slot.isInternal())return false;
	if (((Slot)slot).getWareGroup()==ware.getGrupo()) return true;
	if (ware.getGrupo().isSubgroupOf(((Slot)slot).getWareGroup())) return true;
	
	return false;
}

public void setWareAtPos(Ware ware,Vector3 pos){
	setWareAtSlot(ware,modelData.getSlotAtPosition(pos));
	
}


public void setWareAtSlot(Ware ware,Actuator slot){
	if (!isWareCompatible(ware,slot)) {
		Galactic.printLog("Ware:"+ware+" is not compatible in the slot:"+slot.getPosition(), true);
		return;
	}
	ItemSlot equip=getWareAtSlot((Slot)slot);
	if (equip==null){
		Galactic.printLog("Trying to add a Ware to a empty Position: "+ware+" at "+slot.getPosition(), true);
		return;
	}
	if (equip.getWare()!=ware){
		equip.firePropertyChange2( MVCProperties.EQUIPPED_WARE_PROPERTY, null, ware );
	} 
	equip.setWare(ware);
	
}


/**
 * @return the modelData
 */
public ModelData getModelData() {
	return modelData;
}
}
