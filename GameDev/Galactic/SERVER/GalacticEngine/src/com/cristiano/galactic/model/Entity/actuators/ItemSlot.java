package com.cristiano.galactic.model.Entity.actuators;


import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.ObjetoPersistente;
import com.cristiano.galactic.model.Entity.Logic.Slot;
import com.cristiano.galactic.model.Entity.control.Control;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.Ware;


/*
 * ItemSlots representam os atuadores que cada entidade pode ter.
 * São instanciados a partir dos slots que estào no ModelData e podem 
 * conter informações internas, como nível de energia atual do slot 
 */
public class ItemSlot extends ObjetoPersistente{
	private Slot slot;
//	private HashMap<String,PropertyValue> wareInfo=new HashMap<String,PropertyValue>(); 
	private Ware ware=null;
	private ArtificialEntity item;
	private double intensity=0;
	private Control control;
	
	public ItemSlot(ArtificialEntity item,Slot slot){
		this.item=item;
		this.slot=slot;
		
	}
	/**
	 * @return the ware
	 */
	public Ware getWare() {
		return ware;
	}
	
	public Vector3 getPos(){
		return slot.getPosition();
	}
	
	public Vector3 getAbsPos(){
		return item.getShipWare().getModelData().getAbsVector(getPos());
	}
	/**
	 * @param ware the ware to set	
	 */
	public void setWare(Ware ware) {
		//wareInfo.clear();
		if (ware==null) return;
		
		
		getProps().propagateFrom(ware.getProps());
	    
	    this.ware = ware;
	    
	}
	
	public boolean initialize(){
		return true;
	}
	
	public void turn(float time){
		if (getWare()==null)Galactic.throwError("Slot without Default Group defined:"+this);
		if (getIntensity()>0){
			if (getIntensity()>1)setIntensity(1);
			//setIntensity(time);
			
			intensity=executeAction(time);
			
		}
	}
	public double executeAction(float time){
		return intensity;
	}
	
	public void addEnergy(double d){
		item.getItemLogic().addEnergy(d);
	}
	
	public void turnPassive(float time){
		if (propertyExists(PropertyEnum.DTL_ENERGY_USE_SEC)){
			double energySec=getPropertyAsDouble(PropertyEnum.DTL_ENERGY_USE_SEC);
			energySec=energySec*time;
			addEnergy(item.getItemLogic().getEnergy()+energySec);
		}
		
		/*if (getDetails().get(PropertyManager.DTL_SHIELD_MAX)!=null){
			if (getDetails().get(PropertyManager.DTL_SHIELD_RECHARGE)!=null){
				double r=getDetails().get(PropertyManager.DTL_SHIELD_RECHARGE).getValor()*time;
				setValueDetail(PropertyManager.DTL_CURR_VALUE,getValueDetail(PropertyManager.DTL_CURR_VALUE)+r);
				if (getValueDetail(PropertyManager.DTL_CURR_VALUE)>getValueDetail(PropertyManager.DTL_SHIELD_MAX)){
					setValueDetail(PropertyManager.DTL_CURR_VALUE,getValueDetail(PropertyManager.DTL_SHIELD_MAX));
				}
				item.getItemLogic().setShield(item.getItemLogic().getShield()+getValueDetail(PropertyManager.DTL_CURR_VALUE));
			}
			double a=getDetails().get(PropertyManager.DTL_SHIELD_MAX).getValor();
			item.getItemLogic().setMaxShield(item.getItemLogic().getMaxShield()+a);
		}*/
		
		
		
		/*if (getDetails().get(PropertyManager.DTL_MAX_ENERGY_STORAGE)!=null){
			double a=getDetails().get(PropertyManager.DTL_MAX_ENERGY_STORAGE).getValor();
			
			item.getItemLogic().setMaxEnergy(item.getItemLogic().getMaxEnergy()+a);
		}	*/	
	}
	
	public boolean isPassive(){
		if (propertyExists(PropertyEnum.DTL_AUTO_ACTIVATE)){
			return (getPropertyAsDouble(PropertyEnum.DTL_AUTO_ACTIVATE)<=0);
		}
		return false;
	}
	public void setValueDetail(String name,double vlr){
		setProperty(name, vlr);
	}
	
	public double getValueDetail(Enum name){
		return getValueDetail(name.toString());
	}
	
	public double getValueDetail(String name){
		if (propertyExists(name)){
			return (getPropertyAsDouble(name));
		}
		return -1;
	}
	/**
	 * @return the slot
	 */
	public Slot getSlot() {
		return slot;
	}
	/**
	 * @param slot the slot to set
	 */
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	/**
	 * @return the item
	 */
	public ArtificialEntity getOwner() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(ArtificialEntity item) {
		this.item = item;
	}

	public void setIntensity(double intensity) {
		if (intensity>1)intensity=1;
		if (intensity<0)intensity=0;
		this.intensity = intensity;
	}
	public double getIntensity() {
		return intensity;
	}
	public void setControl(Control control) {
		this.control=control;
		
	}
	public Control getControl() {
		return control;
	}
	
	
}
