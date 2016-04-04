package com.cristiano.galactic.model.wares;


import java.util.Vector;

import com.cristiano.galactic.model.Entity.Abstract.AbstractGameObject;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;

public class WareGroup extends AbstractGameObject{
private WareGroup grupoPai=null;
private Vector<WareGroup> gruposFilhos=new Vector<WareGroup>();
private Vector<Ware> wares=new Vector<Ware>();
//private HashMap<String,PropertyValue> groupInfo=new HashMap<String,PropertyValue>();
private Ware defaultWare=null;
private int idDefaultWare=0;

public WareGroup(int id,DataManager dataManager,String name){
super(id,dataManager,name,EntityType.ET_WARE_GROUP);
//System.out.println("idWareGroup="+getId()+" name:"+name);
}




public WareGroup(int id,DataManager dataManager,String name,WareGroup pai){
	super(id,dataManager,name,EntityType.ET_WARE_GROUP);
	this.setGrupoPai(pai);
//	System.out.println("idWareGroup="+idWareGroup);
	//System.out.println("idWareGroup="+getId()+" name:"+name);
	if (pai==null) return;
	
	pai.addGrupoFilho(this);
	getProps().propagateFrom(pai.getProps());
	
	
}








public boolean isSubgroupOf(WareGroup group){
	if (group==this) return true;
	WareGroup g=group.getGrupoPai();
	while (g!=null){
		if (g==this) return true;
		g=group.getGrupoPai();
	}
	
	return false;
}
public void addGrupoFilho(WareGroup group){
	getGruposFilhos().add(group);
}

public void addWare(Ware ware){
	getWares().add(ware);
}


/*
 * There can be only one Param of each type, existent ones are overwritten.
 */

/**
 * @return the groupInfo
 */


/**
 * @return the defaultWare
 */
public Ware getDefaultWare() {
	if (defaultWare==null)defaultWare=getDataManager().getWareManager().getWareID(idDefaultWare);
	return defaultWare;
}

/**
 * @param defaultWare the defaultWare to set
 */
public void setDefaultWare(Ware defaultWare) {
	this.defaultWare = defaultWare;
}

public void setIdDefaultWare(int id) {
	idDefaultWare=id;
}

public void setGrupoPai(WareGroup grupoPai) {
	this.grupoPai = grupoPai;
}

public WareGroup getGrupoPai() {
	return grupoPai;
}

public void setGruposFilhos(Vector<WareGroup> gruposFilhos) {
	this.gruposFilhos = gruposFilhos;
}

public Vector<WareGroup> getGruposFilhos() {
	return gruposFilhos;
}

public void setWares(Vector<Ware> wares) {
	this.wares = wares;
}

public Vector<Ware> getWares() {
	return wares;
}


}
