package com.cristiano.galactic.model.Entity.Logic;


import java.util.Iterator;
import java.util.Vector;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.representation.Memory;
import com.cristiano.galactic.model.bt.ItemExecutor;
import com.cristiano.galactic.model.enums.ItemGameProperties;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.gamelib.propriedades.Propriedades;


/*
 * This class is responsible for the systems insternals logic... 
 */
public class InternalLogic {
	
	ArtificialEntity item;
	private int maxCargo=500;
	Fitting fitting;
	static float timeToWait=1;
	float updateTime=0;
	
	ItemExecutor mainExec;
	
	Memory memory;
	
	public InternalLogic(ArtificialEntity item){
		this.item=item;
		
		fitting=new Fitting(item);
		resetProperties(true);
		mainExec=new ItemExecutor(item);
		memory=new Memory(item);
		
		initializeSlots();
	}
	
	//Inicializa os slots
	private void initializeSlots(){
		ModelData md=fitting.getModelData();
		for (int i=0;i<md.getSlots().size();i++){
			Slot slot=md.getSlots().get(i);
			if (fitting.getWareAtSlot(slot)!=null){
				fitting.getWareAtSlot(slot).initialize();
			}
		}
	}
	
	
	
	
	
	public void turn(float time){
		turnPassive(time);
		memory.turn();
		
		
		ModelData md=fitting.getModelData();
		for (int i=0;i<md.getSlots().size();i++){
			Slot slot=md.getSlots().get(i);
			if (fitting.getWareAtSlot(slot)!=null){
				fitting.getWareAtSlot(slot).turn(time);
			}
		}
		
		/*if ((mainExec.getCurrBT()==null) && (item.getNextCMD()!=null)){
				mainExec.setCurrBT(item.getCMD());
				item.getCMD().initExecutor();
				//item.removeCMDQueue();
		}*/
		mainExec.turn();
	}
	
	public void setBT(String bt){
		mainExec.setCurrBT(bt);
	}

	private void turnPassive(float time) {
		updateTime+=time;
		if (updateTime>timeToWait){
			
			ModelData md=fitting.getModelData();
			
			
			for (int i=0;i<md.getSlots().size();i++){
				Slot slot=md.getSlots().get(i);
				if (fitting.getWareAtSlot(slot)!=null){
					fitting.getWareAtSlot(slot).turnPassive(updateTime);
				}
			}
			
		//	System.out.println("IL: energy="+energy+" maxEnergy="+maxEnergy+" shield="+shield+" maxShield="+maxShield);
			updateTime=0;
		}
	}
	
	public Propriedades getCargoHold(){
		Propriedades cargo=item.getPropertyAsPropriedades(PropertyEnum.DTL_CARGO_HOLD.toString());
		if (cargo==null){
			cargo=new Propriedades();
			item.setProperty(PropertyEnum.DTL_CARGO_HOLD.toString(), cargo);
		}
		return cargo;
	}
	
	public float getCargoUsed(){
		float c=0;
		Propriedades cargo=getCargoHold();
		Iterator<String> iterator = cargo.getAllProps().keySet().iterator();
	    while (iterator.hasNext()) {
	    	String key = (String) iterator.next();
	    	Ware ware=item.getDataManager().getWare(key);
	    	c+=cargo.getPropertyAsInt(key)*ware.getVol();
	    }
		return c;
	}
	
	public float getCargoFree(){
		return maxCargo-getCargoUsed();
	}
	
	
	public int getFreeVolumeWare(Ware ware){
		return (int)(getCargoFree()/ware.getVol());
	}
	
	public int getWareCargoHold(Ware ware,boolean flagAdd){
		Propriedades cargo=getCargoHold();
		if (cargo.propertyExists(ware.getName())){
			return cargo.getPropertyAsInt(ware.getName());
		} else {
			if (flagAdd){
				cargo.setProperty(ware.getName(), 0);
			}
			return 0;
		}
	}
	
	
	
	public int addCargo(Ware ware,int qtd){
	if (qtd>0){ 
		int qAtual=getWareCargoHold(ware,true);
		int q=qtd+qAtual;
		if (q>getFreeVolumeWare(ware)) {q=getFreeVolumeWare(ware);}
		Propriedades cargo=getCargoHold();
		cargo.setProperty(ware.getName(), q);
		
		item.firePropertyChange2( PropertyEnum.DTL_CARGO_HOLD.toString(), null, cargo );
		
		return q;
		} else {return -removeCargo(ware,-qtd);	
	}
	}
	public int removeCargo(Ware ware,int qtd){
		int q=Math.abs(qtd);
		int qAtual=getWareCargoHold(ware,true);
		//int q=qtd;
		if (q>qAtual){q=qAtual;}
		Propriedades cargo=getCargoHold();
		int qNovo=qAtual-q;
		cargo.setProperty(ware.getName(), qNovo);
		
	
		if (qNovo<=0){cargo.remove(ware.getName());}
		item.firePropertyChange2( PropertyEnum.DTL_CARGO_HOLD.toString(), null, cargo );
		return q;
	}

	/**
	 * @return the Fitting
	 */
	public Fitting getFitting() {
		return fitting;
	}

	/**
	 * @param fitting the Fitting to set
	 */
	public void setFitting(Fitting fitting) {
		this.fitting = fitting;
	}

	

	

	public ItemExecutor getMainExec() {
		return mainExec;
	}

	public Memory getMemory() {
		return memory;
	}

	public double getEnergy() {
		
		return item.getPropertyAsDouble(PropertyEnum.getCurr(ItemGameProperties.CAPACITOR));
	}
	
	public void addEnergy(double v) {
		double pv=item.getPropertyAsDouble(PropertyEnum.getCurr(ItemGameProperties.CAPACITOR));
		double d=getEnergy()+v;
		double pm=item.getPropertyAsDouble(PropertyEnum.getMax(ItemGameProperties.CAPACITOR));
		if (d>pm){d=pm;}
		item.setProperty(PropertyEnum.getCurr(ItemGameProperties.CAPACITOR),d);
	}
	
	/*
	 * Inicializa o campo multiplicador
	 */
	private void initMulti(String par){
		//String curr="CURR_"+par;
		//String max="CURR_MAX_"+par;
		String multStr="DTL_MULT_"+par;
		//String baseStr="BASE_"+par;
		item.setProperty(multStr, 1);
	}
	
	/*
	 * Esse método deve ser chamado sempre que há alguma modificacao no equipamento da nave
	 */
	public void resetProperties(boolean resetCurr){
		Vector<String> props=ItemGameProperties.getAll();
		for (int i=0;i<props.size();i++){
			String prop=props.get(i);
			initMulti(prop);
		}
		
		
		
		ModelData md=fitting.getModelData();
		for (int i=0;i<md.getSlots().size();i++){
			Slot slot=md.getSlots().get(i);
			if (fitting.getWareAtSlot(slot)!=null){
				Ware ware=fitting.getWareAtSlot(slot).getWare();
				
				//Pega cada propriedade da nave, verifica se o slot altera a propriedade MULT e se sim, multiplica o MULT do ware com o MULT da nave.
				for (int j=0;j<props.size();j++){
					String mult=PropertyEnum.getMult(props.get(j));
					double pWare=ware.getPropertyAsDouble(mult);
					if (pWare!=-1){
						double pItem=item.getPropertyAsDouble(mult);
						//pItem.setValor(pItem*pWare);
						item.setProperty(mult,pItem*pWare);
					}
				}
			}
		}
		
		
		
		//Atualiza
		for (int j=0;j<props.size();j++){
			String prop=props.get(j);
			double pMulti=item.getPropertyAsDouble(PropertyEnum.getMult(prop));
			double pMax=item.getPropertyAsDouble(PropertyEnum.getMax(prop));
			double pBase=item.getPropertyAsDouble(PropertyEnum.getBase(prop));
			double pCurr=item.getPropertyAsDouble(PropertyEnum.getCurr(prop));
			pMax=(pBase*pMulti);
			if (resetCurr){
				item.setProperty(PropertyEnum.getCurr(prop),pMax);
			}

		}
		
		
		
		
	}
}
