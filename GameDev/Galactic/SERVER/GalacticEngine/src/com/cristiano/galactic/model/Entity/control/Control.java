package com.cristiano.galactic.model.Entity.control;

import com.cristiano.galactic.model.Entity.Logic.representation.MemoryItem;
import com.cristiano.galactic.model.enums.ControlEnum;



public class Control {
	
	
	
	
 private String slotGrouping;
 private  double intensity=0;
 private double step=0;
 private double time=-1; //time é o tempo em segundos que o controle deve ficar ativo...
 private Enum name;
 //Target is to add some "intelligence" on the controls, in a way that a weapon can aim 
 //at the target instead of firing dumb
 private MemoryItem target=null;
 
 
 public Control(Enum name,String sg,double intensity){
	 this.name=name;
	 this.slotGrouping=sg;
	 setIntensity(intensity);
 }
 public Control(Enum name,String sg,double intensity,double time){
	 this.name=name;
	 this.slotGrouping=sg;
	 setIntensity(intensity);
 }
 
 public Control(ControlEnum name,String sg,double intensity,MemoryItem target){
	 this.name=name;
	 this.slotGrouping=sg;
	 this.target=target;
	 setIntensity(intensity);
 }
 
 public Control(ControlEnum name,String sg,double intensity,double step){
	 this.name=name;
	 this.slotGrouping=sg;
	 setIntensity(intensity);
	 this.step=step;
 }
 
 public Control(Control control) {
	 this.name=control.name;
	 this.intensity=control.intensity;
	 this.slotGrouping=control.slotGrouping;
	 this.step=control.step;
	 this.target=control.target;
}

public static Control createControl(ControlEnum name,String sg,double intensity,MemoryItem target){
	 return new Control(name,sg,intensity,target);
	 
 }
 
public static Control createControl(ControlEnum name,String sg,double intensity){
	return new Control(name,sg,intensity);
 }
 
 /*public void addAction(String action){
	 actions.add(action);
 }*/
 public void setIntensity(double intensity){
	 if (intensity<0)intensity=0;
	 if (intensity>1)intensity=1;
	 this.intensity=intensity;
 }

/**
 * @return the slotGrouping
 */
public String getSlotGroupingName() {
	return slotGrouping;
}

/**
 * @return the intensity
 */
public double getIntensity() {
	return intensity;
}

/**
 * @return the step
 */
public double getStep() {
	return step;
}

/**
 * @return the name
 */
public Enum getName() {
	return name;
}

public MemoryItem getTarget() {
	return target;
}

public void setTarget(MemoryItem target2) {
	this.target=target2;
	
}
public double getTime() {
	return time;
}
public void setTime(double time) {
	this.time = time;
}
public void decTime(double t){
	time-=t;
}
}
