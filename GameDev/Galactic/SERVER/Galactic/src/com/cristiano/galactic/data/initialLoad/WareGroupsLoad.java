package com.cristiano.galactic.data.initialLoad;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.cristiano.cyclone.utils.ObjetoBasico;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.containers.WareManager;
import com.cristiano.galactic.model.enums.ItemGameProperties;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.galactic.model.wares.WareGroup;


public class WareGroupsLoad extends ObjetoBasico{
WareManager wm;
public WareGroupsLoad(DataManager dm){
	//wWareGroup wg=new WareGroup("ORE");
	wm=dm.getWareManager();
	start();
}
public void start(){
	initializeGroups();
	initializeWares();
	initializeRefined();
	initializeEquip();
	
}



/*public static WareManager getInstance(){
	return DataManager.getWareManager();
}*/
public  Ware getWare(String ware){
	return getWares().get(ware);
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

private void initializeRefined(){
	getWare("Veldspar").setMinAmountRefine(333);
	getWare("Veldspar").setVol(0.1);
	getWare("Scordite").setMinAmountRefine(333);
	getWare("Scordite").setVol(0.15);
	getWare("Pyroxeres").setMinAmountRefine(333);
	getWare("Pyroxeres").setVol(0.3);
	getWare("Plagioclase").setMinAmountRefine(333);
	getWare("Plagioclase").setVol(0.35);
	getWare("Omber").setMinAmountRefine(500);
	getWare("Omber").setVol(0.6);
	getWare("Kernite").setMinAmountRefine(400);
	getWare("Kernite").setVol(1.2);
	getWare("Jaspet").setMinAmountRefine(500);
	getWare("Jaspet").setVol(2);
	getWare("Hemorphite").setMinAmountRefine(500);
	getWare("Hemorphite").setVol(3);
	getWare("Hedbergite").setMinAmountRefine(500);
	getWare("Hedbergite").setVol(3);
	getWare("Gneiss").setMinAmountRefine(400);
	getWare("Gneiss").setVol(5);
	getWare("Dark Ochre").setMinAmountRefine(400);
	getWare("Dark Ochre").setVol(8);
	getWare("Spodumain").setMinAmountRefine(250);
	getWare("Spodumain").setVol(16);
	getWare("Crokite").setMinAmountRefine(250);
	getWare("Crokite").setVol(16);
	getWare("Bistot").setMinAmountRefine(200);
	getWare("Bistot").setVol(16);
	getWare("Arkonor").setMinAmountRefine(200);
	getWare("Arkonor").setVol(16);
	getWare("Mercoxit").setMinAmountRefine(250);
	getWare("Mercoxit").setVol(40);
	
	
	getWare("Veldspar").addRefineTo(getWare("Tritanium"),1000);

	getWare("Scordite").addRefineTo(getWare("Tritanium"),833);
	getWare("Scordite").addRefineTo(getWare("Pyerite"),416);

	getWare("Pyroxeres").addRefineTo(getWare("Tritanium"),844);
	getWare("Pyroxeres").addRefineTo(getWare("Pyerite"),59);
	getWare("Pyroxeres").addRefineTo(getWare("Mexallon"),120);
	getWare("Pyroxeres").addRefineTo(getWare("Nocxium"),11);

	getWare("Plagioclase").addRefineTo(getWare("Tritanium"),256);
	getWare("Plagioclase").addRefineTo(getWare("Pyerite"),512);
	getWare("Plagioclase").addRefineTo(getWare("Mexallon"),256);

	getWare("Omber").addRefineTo(getWare("Tritanium"),307);
	getWare("Omber").addRefineTo(getWare("Pyerite"),123);
	getWare("Omber").addRefineTo(getWare("Isogen"),307);

	getWare("Kernite").addRefineTo(getWare("Tritanium"),386);
	getWare("Kernite").addRefineTo(getWare("Mexallon"),773);
	getWare("Kernite").addRefineTo(getWare("Isogen"),386);

	getWare("Jaspet").addRefineTo(getWare("Tritanium"),259);
	getWare("Jaspet").addRefineTo(getWare("Pyerite"),259);
	getWare("Jaspet").addRefineTo(getWare("Mexallon"),518);
	getWare("Jaspet").addRefineTo(getWare("Nocxium"),259);
	getWare("Jaspet").addRefineTo(getWare("Zydrine"),8);

	getWare("Hemorphite").addRefineTo(getWare("Tritanium"),212);
	getWare("Hemorphite").addRefineTo(getWare("Isogen"),212);
	getWare("Hemorphite").addRefineTo(getWare("Nocxium"),424);
	getWare("Hemorphite").addRefineTo(getWare("Zydrine"),28);

	getWare("Hedbergite").addRefineTo(getWare("Isogen"),708);
	getWare("Hedbergite").addRefineTo(getWare("Nocxium"),354);
	getWare("Hedbergite").addRefineTo(getWare("Zydrine"),32);

	getWare("Gneiss").addRefineTo(getWare("Tritanium"),171);
	getWare("Gneiss").addRefineTo(getWare("Mexallon"),171);
	getWare("Gneiss").addRefineTo(getWare("Isogen"),343);
	getWare("Gneiss").addRefineTo(getWare("Zydrine"),171);

	getWare("Dark Ochre").addRefineTo(getWare("Tritanium"),250);
	getWare("Dark Ochre").addRefineTo(getWare("Nocxium"),500);
	getWare("Dark Ochre").addRefineTo(getWare("Zydrine"),250);

	getWare("Crokite").addRefineTo(getWare("Tritanium"),331);
	getWare("Crokite").addRefineTo(getWare("Nocxium"),331);
	getWare("Crokite").addRefineTo(getWare("Zydrine"),663);

	getWare("Spodumain").addRefineTo(getWare("Tritanium"),700);
	getWare("Spodumain").addRefineTo(getWare("Pyerite"),140);
	getWare("Spodumain").addRefineTo(getWare("Megacyte"),140);

	getWare("Bistot").addRefineTo(getWare("Pyerite"),170);
	getWare("Bistot").addRefineTo(getWare("Zydrine"),341);
	getWare("Bistot").addRefineTo(getWare("Megacyte"),170);

	getWare("Arkonor").addRefineTo(getWare("Tritanium"),300);
	getWare("Arkonor").addRefineTo(getWare("Zydrine"),166);
	getWare("Arkonor").addRefineTo(getWare("Megacyte"),333);

	getWare("Mercoxit").addRefineTo(getWare("Morphite"),530);

}
private void initializeEquip(){
	
	

	

	//	getWare("Basic Weapon").addDetail(DetailManager.DTL_RANGE, 10000);
	/*getWare("Basic Ship").addDetail(DetailManager.DTL_SHIP_CARGOBAY, 0);
	getWare("Basic Ship").addDetail(DetailManager.DTL_SHIP_CURR_LASER_ENERGY, 10);
	getWare("Basic Ship").addDetail(DetailManager.DTL_RANGE, 10000);
	getWare("Basic Ship").addDetail(DetailManager.DTL_LASER_ENERGY_SHOT, 10);
	getWare("Basic Ship").addDetail(DetailManager.DTL_SPEED, 1000);
	
	
	getWare("Basic Weapon").addDetail(DetailManager.DTL_DMG_HULL, 10);
	getWare("Basic Weapon").addDetail(DetailManager.DTL_DMG_SHIELD, 10);
	getWare("Basic Weapon").addDetail(DetailManager.DTL_RANGE, 10000);
	getWare("Basic Weapon").addDetail(DetailManager.DTL_LASER_ENERGY_SHOT, 10);
	getWare("Basic Weapon").addDetail(DetailManager.DTL_SPEED, 1000);*/
	
}

private void initializeGroups(){
	
	addWareGroup("INTERNAL_FUNCTIONS");
		addWareGroup(Consts.GRP_CAMERA.toString(),"INTERNAL_FUNCTIONS");
		getWareGroup(Consts.GRP_CAMERA.toString()).setProperty(PropertyEnum.DTL_WARE_CLASS,0);
	
	addWareGroup("WARE");
	getWareGroup("WARE").setProperty(PropertyEnum.DTL_WARE_CLASS,0);
	getWareGroup("WARE").setProperty(PropertyEnum.DTL_WARE_MAX_PRICE,0);
	getWareGroup("WARE").setProperty(PropertyEnum.DTL_WARE_MIN_PRICE,0);
	getWareGroup("WARE").setProperty(PropertyEnum.DTL_MODEL_ID,0);
	getWareGroup("WARE").setProperty(PropertyEnum.getBase(ItemGameProperties.PRICE),0);
	

	addWareGroup("ORE","WARE");
		addWareGroup("Refined Ore","ORE");
		
	addWareGroup("Equipment","WARE");
		getWareGroup("Equipment").setProperty(PropertyEnum.DTL_CURR_VALUE,0);
		getWareGroup("Equipment").setProperty(PropertyEnum.DTL_MAX_VALUE,0);
		getWareGroup("Equipment").setProperty(PropertyEnum.DTL_AUTO_ACTIVATE,0);
		addWareGroup("EnergyEquipment","Equipment");
			getWareGroup("EnergyEquipment").setProperty(PropertyEnum.DTL_ENERGY_USE_SEC,0);
			addWareGroup("Weapon","EnergyEquipment");
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_LASER_ENERGY_SHOT,0);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_COOLDOWN,0);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_DMG_HULL,0);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_AUTO_ACTIVATE,1);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_DMG_SHIELD,0);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_RANGE,0);
				getWareGroup("Weapon").setProperty(PropertyEnum.DTL_SPEED,0);
				addWareGroup("Laser","Weapon");
				addWareGroup("Missil","Weapon");
					getWareGroup("Missil").setProperty(PropertyEnum.DTL_BLAST_RADIUS,0);
				addWareGroup("Torpedo","Missil");
			addWareGroup("Shield","EnergyEquipment");
				getWareGroup("Shield").setProperty(PropertyEnum.getMult(ItemGameProperties.SHIELD),1);
				getWareGroup("Shield").setProperty(PropertyEnum.DTL_AUTO_ACTIVATE,0);
			addWareGroup("Generator","EnergyEquipment");
				getWareGroup("Generator").setProperty(PropertyEnum.getMult(ItemGameProperties.CAPREGEN),1);
			addWareGroup("Movement","EnergyEquipment");
				getWareGroup("Movement").setProperty(PropertyEnum.DTL_AUTO_ACTIVATE,1);
				addWareGroup("Engine","Movement");
					getWareGroup("Engine").setProperty(PropertyEnum.getMult(ItemGameProperties.ACCEL),1);
				addWareGroup("Thruster","Movement");
		addWareGroup("PhysicalEquipment","Equipment");
			getWareGroup("PhysicalEquipment").setProperty(PropertyEnum.DTL_AUTO_ACTIVATE,0);

			addWareGroup("AMMO","PhysicalEquipment");
			addWareGroup("HULL","PhysicalEquipment");
			addWareGroup("Turret","PhysicalEquipment");
				getWareGroup("Turret").setProperty(PropertyEnum.DTL_MAX_VALUE,1);
				getWareGroup("Turret").setProperty(PropertyEnum.DTL_COOLDOWN,0);
				addWareGroup("Fixed Turret","Turret");
					getWareGroup("Fixed Turret").setProperty(PropertyEnum.DTL_MAX_VALUE,0);
				addWareGroup("Mobile Turret","Turret");
				addWareGroup("Missile Turret","Turret");
					getWareGroup("Missile Turret").setProperty(PropertyEnum.DTL_MAX_VALUE,0);
			addWareGroup("DockBay","PhysicalEquipment");
	
	addWareGroup("Artificial Entity","WARE");
		getWareGroup("Artificial Entity").setProperty(PropertyEnum.DTL_MODEL_ID,0);
		addWareGroup("Ship","Artificial Entity");
		Vector<String> vProps=ItemGameProperties.getAll();
		for (int i=0;i<vProps.size();i++){
			String prop=vProps.get(i);
			String curr=PropertyEnum.getCurr(prop);
			String multStr=PropertyEnum.getMult(prop);
			String maxStr=PropertyEnum.getMax(prop);
			String baseStr=PropertyEnum.getBase(prop);
			getWareGroup("Ship").setProperty(curr,0);
			getWareGroup("Ship").setProperty(multStr,0);
			getWareGroup("Ship").setProperty(maxStr,0);
			getWareGroup("Ship").setProperty(baseStr,0);
		}
		getWareGroup("Ship").setProperty(PropertyEnum.DTL_MODEL_ID,0);
		getWareGroup("Ship").setProperty(PropertyEnum.DTL_THRUST_POWER_TEMP,0);
		
		addWareGroup("Station","Artificial Entity");
		
}







private void initializeWares(){




	addWare("CAMERA",0,Consts.GRP_CAMERA.toString(),true);
	
	initializeWeapons();
	initializeEngines();
		
		
	initializeShips();
	
		
		
	initializeAmmo();

	
	addWare("Basic Generator",10,"Generator",true);
		getWare("Basic Generator").setProperty(PropertyEnum.getMult(ItemGameProperties.CAPREGEN),1);
	
	
	

	
	addWare("Veldspar",0.1,"ORE");
		getWare("Veldspar").setProperty(PropertyEnum.DTL_MODEL_ID,10);
	addWare("Scordite",0.1,"ORE");
	addWare("Pyroxeres",0.1,"ORE");
	addWare("Plagioclase",0.1,"ORE");
	addWare("Omber",0.1,"ORE");
	addWare("Kernite",0.1,"ORE");
	addWare("Jaspet",0.1,"ORE");
	addWare("Hemorphite",0.1,"ORE");
	addWare("Hedbergite",0.1,"ORE");
	addWare("Gneiss",0.1,"ORE");
	addWare("Dark Ochre",0.1,"ORE");
	addWare("Crokite",0.1,"ORE");
	addWare("Spodumain",0.1,"ORE");
	addWare("Bistot",0.1,"ORE");
	addWare("Arkonor",0.1,"ORE");
	addWare("Mercoxit",0.1,"ORE");
	
	
	addWare("Tritanium",0.1,"Refined Ore");
	addWare("Pyerite",0.1,"Refined Ore");
	addWare("Mexallon",0.1,"Refined Ore");
	addWare("Isogen",0.1,"Refined Ore");
	addWare("Nocxium",0.1,"Refined Ore");
	addWare("Zydrine",0.1,"Refined Ore"); 	
	addWare("Megacyte",0.1,"Refined Ore");	
	addWare("Morphite",0.1,"Refined Ore");	
	
	
}
private void initializeEngines() {
	
	
	//getWareGroup("Ship").setProperty(PropertyEnum.DTL_ENGINE_POWER,1000000000);//Está sendo usado?

	
	/*getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_CARGO);
	getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_ARMOR);
	getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_SHIELD);
	getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_SHIELDREGEN);
	getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_CAPACITOR);
	getWareGroup("Ship").setProperty(PropertyEnum.DTL_CURR_MAX_CAPREGEN);*/
	
	
	addWare("Basic Engine",100,"Engine",true);
		getWare("Basic Engine").setProperty(PropertyEnum.DTL_ENERGY_USE_SEC,30);
		getWare("Basic Engine").setProperty(PropertyEnum.getMult(ItemGameProperties.ACCEL),1);
	addWare("Capital Engine",100,"Engine",false);
		getWare("Capital Engine").setProperty(PropertyEnum.DTL_ENERGY_USE_SEC,30);
		getWare("Capital Engine").setProperty(PropertyEnum.getMult(ItemGameProperties.ACCEL),1);
	
		
		/*
	addWare("Basic Thruster",10,"Thruster",true);
		getWare("Basic Thruster").addDetail(DetailManager.DTL_ENERGY_USE_SEC,20);
		getWare("Basic Thruster").addDetail(DetailManager.DTL_MAX_FORCE,shipThruster);
	addWare("Capital Thruster",10,"Thruster",false);
		getWare("Capital Thruster").addDetail(DetailManager.DTL_ENERGY_USE_SEC,20);
		getWare("Capital Thruster").addDetail(DetailManager.DTL_MAX_FORCE,shipThruster*relMass);
		*/
	addWare("Basic Shield",10,"Shield",true);
		getWare("Basic Shield").setProperty(PropertyEnum.getMult(ItemGameProperties.SHIELD),1);
}
private void initializeWeapons() {
	addWare("Basic Laser",10,"Laser",true);
		getWare("Basic Laser").setProperty(PropertyEnum.DTL_ENERGY_USE_SEC,30);
		getWare("Basic Laser").setProperty(PropertyEnum.DTL_COOLDOWN,300);
		getWare("Basic Laser").setProperty(PropertyEnum.DTL_SPEED,10000);
		getWare("Basic Laser").setProperty(PropertyEnum.DTL_RANGE,100000);
	
	addWare("Basic Weapon",10,"Weapon",true);
		getWare("Basic Weapon").setProperty(PropertyEnum.DTL_ENERGY_USE_SEC,10);
		getWare("Basic Weapon").setProperty(PropertyEnum.DTL_COOLDOWN,300);
		getWare("Basic Weapon").setProperty(PropertyEnum.DTL_SPEED,1000);
		getWare("Basic Weapon").setProperty(PropertyEnum.DTL_RANGE,100000);
}
private void initializeAmmo() {
	addWare(Consts.BASIC_AMMO,.1,"AMMO",true);
		getWare(Consts.BASIC_AMMO).setProperty(PropertyEnum.DTL_MODEL_ID,3);
		getWare(Consts.BASIC_AMMO).setProperty(PropertyEnum.getBase(ItemGameProperties.ARMOR),100);
		getWare(Consts.BASIC_AMMO).setProperty(PropertyEnum.getBase(ItemGameProperties.SHIELD),100);
}

private void addShip(String ware,int id,int cargo,int armor,int shieldPoints,int shieldRegen,int capacitor,int capacitorRegen,int speed,float baseprice,int turning ){
	Ware w;
	w=addWare(ware,10000,"Ship",true);
	w.setProperty(PropertyEnum.DTL_THRUST_POWER_TEMP,3*Math.pow(10,7));
		w.setProperty(PropertyEnum.DTL_MODEL_ID,id);
		
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.CARGO),cargo);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.ARMOR),armor);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.SHIELD),shieldPoints);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.SHIELDREGEN),shieldRegen);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.CAPACITOR),capacitor);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.CAPREGEN),capacitorRegen);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.ACCEL),speed);
		w.setProperty(PropertyEnum.getBase(ItemGameProperties.PRICE),baseprice);
		w.setProperty(PropertyEnum.DTL_BASE_TURN,turning);
}

private void initializeShips() {
	
	
	addShip("Buster",1,60, 100, 180, 380, 125, 80, 223, 100000f, 10 );
	addShip("Nova",2,80, 120, 200, 440, 125, 90, 190, 150000f, 10 );
	addShip("Discoverer",3,40, 80, 160, 320, 125, 60, 439, 50000f, 10 );
	addShip("DiscovererPirate",4,40, 80, 160, 320, 125, 60, 410, 50000f, 10 );
	addShip("Orca",5,30000, 6900, 10750, 800, 4200, 800, 60, 610000000f, 10 );
	addShip("Avatar",6,11250, 1400000, 580000, 37000, 112500, 6750, 105, 50383182600f, 10 );
	addShip("Erebus",7,16250, 1300000, 680000, 40615, 108000, 6480, 70, 48678402000f, 10 );
	addShip("Drake",8,345, 3906, 5469, 1400, 2812, 750, 140, 25000000f, 10 );
	addShip("Velator",9,120, 188, 235, 625, 125, 130, 296, 250000f, 10 );
	addShip("Megathron",10,675, 6641, 6211, 2500, 5625, 1155, 105, 105000000f, 10 );
	addShip("Ragnarok",11,15000, 700000, 1280000, 44000, 112500, 5940, 80, 48678402000f, 10 );
	addShip("Providence",12,750000, 24000, 5000, 1666, 3750, 231, 70, 776193970f, 10 );


	
}


public void addWare(Ware ware,boolean setGroupDefault){
	getWares().put(ware.getName(),ware);
	if (setGroupDefault)
		ware.getGrupo().setDefaultWare(ware);
}

public Ware addWare(String ware,double vol,String wg,boolean setGroupDefault){
	Ware w=new Ware(Ware.getNextID(),wm.getDataManager(),ware,vol,getWareGroups().get(wg));
	addWare(w,setGroupDefault);
	return w;
}

public void addWare(String ware,double vol,String wg){
	addWare(ware,vol,wg,false);
}
public void addWareGroup(String wg){
	addWareGroup(wg,null);
}
public void addWareGroup(String wg,String wgPai){
	if (wgPai==null){
		getWareGroups().put(wg,new WareGroup(WareGroup.getNextID(),wm.getDataManager(),wg));
	}else {
		getWareGroups().put(wg,new WareGroup(WareGroup.getNextID(),wm.getDataManager(),wg,getWareGroups().get(wgPai)));
		
	}
}

public void addWareGroup(WareGroup wg){
	getWareGroups().put(wg.getName(),wg);
}
public void setWareGroups(HashMap<String,WareGroup> wareGroups) {
	wm.setWareGroups(wareGroups);
}
public HashMap<String,WareGroup> getWareGroups() {
	return wm.getWareGroups();
}
public void setWares(HashMap<String,Ware> wares) {
	wm.setWares(wares);
}
public HashMap<String,Ware> getWares() {
	return wm.getWares();
}
}
