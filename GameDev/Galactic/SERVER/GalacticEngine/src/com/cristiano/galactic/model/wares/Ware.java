package com.cristiano.galactic.model.wares;


import com.cristiano.galactic.model.Entity.Abstract.AbstractGameObject;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.gamelib.propriedades.Propriedades;

public class Ware extends AbstractGameObject {
WareGroup grupo;
double vol=0;



int minAmountRefine=1;
ModelData modelData=null;

	public Ware(int id,DataManager dataManager,String name,double vol,WareGroup grupo) {
		super(id,dataManager,name, EntityType.ET_WARE);
		
		this.grupo=grupo;
		this.vol=vol;
		if (grupo!=null){
			grupo.addWare(this);
			
			getProps().propagateFrom(grupo.getProps());
		}
	}
	
	public String toString(){
		return "Ware:" +getName();
	}
	/*
	 * There can be only one Param of each type, existent ones are overwritten.
	 * In addition, a ware can only have a property inherited from its group.
	 */
	

	

	public void addRefineTo(Ware ware,int qtd){
		Propriedades refineTo=getRefineTo();
		refineTo.setProperty(ware.getName(), qtd);
	}
	
	public void addRefineTo(String ware,int qtd){
		Propriedades refineTo=getRefineTo();
		refineTo.setProperty(ware, qtd);
	}
	

	public int getMinAmountRefine() {
	return minAmountRefine;
}

public void setMinAmountRefine(int minAmountRefine) {
	this.minAmountRefine = minAmountRefine;
}

public WareGroup getGrupo() {
	return grupo;
}

public double getVol() {
	return vol;
}
public void setVol(double vol) {
	this.vol = vol;
}



public Propriedades getRefineTo() {
	Propriedades refineTo=getPropertyAsPropriedades(PropertyEnum.DTL_REFINE_INTO.toString());
	if (refineTo==null){
		refineTo=new Propriedades();
		setProperty(PropertyEnum.DTL_REFINE_INTO.toString(), refineTo);
	}
	return refineTo;
}

/**
 * @return the modelData
 */
public ModelData getModelData() {
	if (modelData==null){
		if (propertyExists(PropertyEnum.DTL_MODEL_ID)){
			modelData=getDataManager().getModels3D().getModelByID((int)(getPropertyAsDouble(PropertyEnum.DTL_MODEL_ID)));
		}
	}
	return modelData;
}

/**
 * @param modelData the modelData to set
 */
public void setModelData(ModelData modelData) {
	this.modelData = modelData;
}
}
