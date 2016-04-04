package com.cristiano.galactic.model.containers;


import java.util.HashMap;
import java.util.Iterator;

import com.cristiano.cyclone.utils.ObjetoBasico;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;


public class WareManager extends ObjetoBasico{
private HashMap<String,WareGroup> wareGroups =new HashMap<String,WareGroup>();
private HashMap<String,Ware> wares=new HashMap<String,Ware>();
private DataManager dataManager;

public WareManager(DataManager dataManager){
	//wWareGroup wg=new WareGroup("ORE");
	this.dataManager=dataManager;
	
}




/*public static WareManager getInstance(){
	return DataManager.getWareManager();
}*/
public  Ware getWare(String ware){
	return getWares().get(ware);
}


public Ware getWareID(int id){
	Iterator<String> iterator = getWares().keySet().iterator();
    while (iterator.hasNext()) {
    	String key = (String) iterator.next();
    	Ware wg=getWares().get(key);
    	 if (wg.getId()==id) return wg;
     }
    return null;
}


public WareGroup getWareGroup(String wg){
	return getWareGroups().get(wg);
}

public WareGroup getWareGroup(int id){
	Iterator<String> iterator = getWareGroups().keySet().iterator();
    while (iterator.hasNext()) {
    	String key = (String) iterator.next();
    	WareGroup wg=getWareGroups().get(key);
    	 if (wg.getId()==id) return wg;
     }
    return null;
}




public void addWare(Ware ware,boolean setGroupDefault){
	getWares().put(ware.getName(),ware);
	if (setGroupDefault)
		ware.getGrupo().setDefaultWare(ware);
}

public void addWare(String ware,double vol,String wg,boolean setGroupDefault){
	Ware w=new Ware(Ware.getNextID(),dataManager,ware,vol,getWareGroups().get(wg));
	addWare(w,setGroupDefault);
}

public void addWare(String ware,double vol,String wg){
	addWare(ware,vol,wg,false);
}
public void addWareGroup(String wg){
	addWareGroup(wg,null);
}
public void addWareGroup(String wg,String wgPai){
	if (wgPai==null){
		getWareGroups().put(wg,new WareGroup(WareGroup.getNextID(),dataManager,wg));
	}else {
		getWareGroups().put(wg,new WareGroup(WareGroup.getNextID(),dataManager,wg,getWareGroups().get(wgPai)));
		
	}
}

public void addWareGroup(WareGroup wg){
	getWareGroups().put(wg.getName(),wg);
}
public void setWareGroups(HashMap<String,WareGroup> wareGroups) {
	this.wareGroups = wareGroups;
}
public HashMap<String,WareGroup> getWareGroups() {
	return wareGroups;
}
public void setWares(HashMap<String,Ware> wares) {
	this.wares = wares;
}
public HashMap<String,Ware> getWares() {
	return wares;
}
public DataManager getDataManager() {
	return dataManager;
}
}
