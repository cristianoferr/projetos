package com.cristiano.galactic.model.Entity.Logic;


import java.util.Vector;

import com.cristiano.cyclone.forceGenerator.actuators.Actuator;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.actuators.ItemSlot;
import com.cristiano.galactic.model.Entity.control.Control;


/*
 * SlotGrouping é um grupamento de Slots, ele é inserido no ModelData 
 * e usado quando algum comando é passado para a entidade.
 */
public class SlotGrouping{
	Vector<Slot> slots=new Vector<Slot>();
	
public SlotGrouping(){
}



public void addSlot(Slot s){
	for (int i=0;i<slots.size();i++){
		if (slots.get(i)==s) return;
	}
	slots.add(s);
}


/**
 * @return the slots
 */
public Vector<Slot> getSlots() {
	return slots;
}

public Actuator getSlot(int i){
	return slots.get(i);
}


public void activate(ArtificialEntity item,Control control){
	InternalLogic il=item.getItemLogic();
	for (int i=0;i<getSlots().size();i++){
		Slot slot=(Slot)getSlot(i);
		ItemSlot is=il.getFitting().getWareAtSlot(slot);
		is.setControl(control);
		if (control.getStep()!=0)
			is.setIntensity(is.getIntensity()+control.getStep());
		else
			is.setIntensity(control.getIntensity());
	}
	
}


}
