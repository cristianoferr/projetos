package com.cristiano.galactic.model.wares;


import java.util.HashMap;
import java.util.Vector;

import com.cristiano.cyclone.entities.GeomPoly.GeomOBJ;
import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.Entity.Logic.Slot;
import com.cristiano.galactic.model.Entity.Logic.SlotGrouping;
import com.cristiano.galactic.model.Entity.Logic.SlotRules;
import com.cristiano.galactic.model.containers.DataManager;


public class ModelData{

	String path;
	private Vector<Slot> slots;
//	int type=0;  //0=OBJ
	public int id=0;
	
	float scale=1;
	private DataManager dataManager;
	double mass;
	
	HashMap<String,SlotGrouping> slotGrouping=new HashMap<String,SlotGrouping>();
	
	//cyclone.entities.geom.Geom.PrimitiveType primType=PrimitiveType.BOX;
	Geom geom=null;

	public ModelData(DataManager dataManager,int id,String path,double mass){
		this.path=path;
		//this.type=type;
		this.id=id;
		this.mass=mass;
		
		this.setDataManager(dataManager);
		setSlots(new Vector<Slot>());
		
	//	System.out.println("ModelData:"+path);
		
	}
	
	public void addSlotGrouping(String group,SlotGrouping sg){
		//System.out.println("Group="+group+" size="+sg.getSlots().size());
		slotGrouping.put(group, sg);
	}
	
	public SlotGrouping getSlotGrouping(String group){
		return slotGrouping.get(group);
	}
	

/*
 * Esse método cria os grupos de slots para o modelo... está correto dessa forma.
 * */
	public void createSlotGrouping(){
		SlotRules sr=new SlotRules(this);
	}
	
	public Vector3 getAbsVector(Vector3 pos){
		
		return new Vector3(pos.x*getTamX(),pos.y*getTamY(),pos.z*getTamZ());
	}
	
	public Slot addWareGroupAtPos(WareGroup wg,Vector3 pos,String orientation,boolean addDefault){
		//
		return addWareGroupAtPos(wg,pos,PhysicsConsts.orientations.get(orientation),addDefault,null);	
	}
	public Slot addWareGroupAtPos(WareGroup wg,Vector3 pos,String orientation,Ware defaultWare){
		//
		return addWareGroupAtPos(wg,pos,PhysicsConsts.orientations.get(orientation),false,defaultWare);	
	}
	public Slot addWareGroupAtPos(WareGroup wg,Vector3 pos,Vector3 orientation,boolean addDefault){
		return addWareGroupAtPos(wg,pos,orientation,addDefault,null);
	}
	
	public Slot addWareGroupAtPos(WareGroup wg,Vector3 pos,Vector3 orientation,boolean addDefault,Ware defaultWare){
		//pos=getRelVector(pos);
		if (orientation==null) {
			orientation=new Vector3(0,0,0);
			Galactic.printLog("Orientation null: "+wg+" at "+pos, true);
		}
		Slot ret=null;
		if (getSlotAtPosition(pos)==null){
			if (addDefault){
				ret=new Slot(this,wg,pos,orientation,addDefault);
			} else ret=new Slot(this,wg,pos,orientation,defaultWare);
			
			getSlots().add(ret);
		}
		else Galactic.printLog("Position in AbstractElement used: "+wg+" at "+pos, true);
		return ret;	
	}

	public Slot getSlotAtPosition(Vector3 pos){
		//pos=getRelVector(pos);
		for (int i=0;i<getSlots().size();i++){
			Slot equip=getSlots().get(i);
			if (pos.equals(equip.getPosition())) return equip;
		}
		return null;
	}


	/**
	 * @return the modelData
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the type
	 */
/*	public int getType() {
		return type;
	}
*/
	


	/**
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}



	/**
	 * @param scale the scale to set
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}


	public void setSlots(Vector<Slot> slots) {
		this.slots = slots;
	}


	public Vector<Slot> getSlots() {
		return slots;
	}

	/**
	 * @return the tamX
	 */
	public double getTamX() {
		return ((GeomOBJ)geom).halfSize.x;
	}

	/**
	 * @param tamX the tamX to set
	 */
	/*public void setTamX(double tamX) {
		this.tamX = tamX;
	}*/

	/**
	 * @return the tamY
	 */
	public double getTamY() {
		return ((GeomOBJ)geom).halfSize.y;
	}

	/**
	 * @param tamY the tamY to set
	 */
	/*public void setTamY(double tamY) {
		this.tamY = tamY;
	}*/

	/**
	 * @return the tamZ
	 */
	public double getTamZ() {
		return ((GeomOBJ)geom).halfSize.z;
	}

	/**
	 * @param tamZ the tamZ to set
	 */
	/*public void setTamZ(double tamZ) {
		this.tamZ = tamZ;
	}*/

	/**
	 * @return the geom
	 */
	public Geom getGeom() {
		if (geom==null){
			geom=dataManager.getModelContainer().loadGLModel("MD"+id,Consts.rootPath+ Consts.recursosPath+"/"+getPath(),scale,mass);
			/*if (primType==PrimitiveType.BOX)
				geom=new GeomBox(new Vector3(tamX,tamY,tamZ));
			if (primType==PrimitiveType.SPHERE)
				geom=new GeomSphere(tamX);*/
		}
		return geom;
	}

	public int getId() {
		return id;
	}


	/**
	 * @return the slotGrouping
	 */
	public HashMap<String, SlotGrouping> getSlotGrouping() {
		return slotGrouping;
	}



	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public double getMass() {
		return mass;
	}
	}