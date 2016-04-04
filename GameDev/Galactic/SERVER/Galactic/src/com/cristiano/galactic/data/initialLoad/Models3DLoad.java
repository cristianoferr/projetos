package com.cristiano.galactic.data.initialLoad;


import java.util.Iterator;
import java.util.Vector;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.containers.Model3DManager;
import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;




public class Models3DLoad {
DataManager dataManager;
Model3DManager m3D;
public Models3DLoad(DataManager dataManager){
	this.dataManager=dataManager;
	m3D=dataManager.getModels3D();
	
	
	ModelData md;
	
	
	
	
	md=addModelData(1, "models/egosoft/argon_m4/argon_m4.obj",448000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(2, "models/egosoft/argon_m3/argon_m3.obj",648000f*PhysicsConsts.massaKG); md.setScale(0.1f);
	md=addModelData(3, "models/egosoft/argon_m5/argon_m5.obj",248000f*PhysicsConsts.massaKG); md.setScale(0.08f);
	md=addModelData(4,"models/egosoft/argon_m5-pirate/argon_m5-pirate.obj",248000f*PhysicsConsts.massaKG); md.setScale(0.08f);
	md=addModelData(5,"models/EVE/Orca/Orca.obj",250000000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(6, "models/EVE/Avatar/Avatar.obj",2278125000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(7, "models/EVE/Erebus/Erebus.obj",2379370000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(8, "models/EVE/Drake/Drake.obj",14010000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(9, "models/EVE/Galente/Rookie/Rookie.obj",1148000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(10, "models/EVE/Megathron/megathron.obj",98400000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(11, "models/EVE/Ragnarok/ragnarok.obj",2075625000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(12, "models/EVE/Providence/providence.obj",1125000000f*PhysicsConsts.massaKG); md.setScale(1f);
	md=addModelData(13, "models/shot/shot.obj",10*PhysicsConsts.massaKG);md.setScale(0.2f);


	/*md.setTamZ(1);
	md.setTamY(1);
	md.setTamX(1);*/
	
	
	//x=diametro
	//y=altura
	//z==largura
	
	//3ds  -> 
	//x	   -> z
	//y	   -> Y
	//z	   -> x

	
	initLayouts();
	initWares();
	
}

public void initLayouts(){
	for (int i=0;i<m3D.getModels().size();i++){
		ModelData md=m3D.getModels().get(i);
//		if (md.id==24) createCapitalLayout(md); else
		createDefaultLayout(md);
	}
}


public void createDefaultLayout(ModelData md){
	
	
	//Meio direita
/*	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0,0,-1),PhysicsConsts.ORIENT_EAST,true);//centro/centro/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0,0,1),PhysicsConsts.ORIENT_WEST,true);//centro/centro/direita
	
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0.9,0.9,-0.9),PhysicsConsts.ORIENT_UP,true);//frente/alto/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0.9,0.9,0.9),PhysicsConsts.ORIENT_UP,true);//frente/alto/direita
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0.9,-0.9,-0.9),PhysicsConsts.ORIENT_DOWN,true);//frente/baixo/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(0.9,-0.9,0.9),PhysicsConsts.ORIENT_DOWN,true);//frente/baixo/direita
	
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-0.9,0.9,-0.9),PhysicsConsts.ORIENT_UP,true);//atras/alto/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-0.9,0.9,0.9),PhysicsConsts.ORIENT_UP,true);//atras/alto/direita
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-0.9,-0.9,-0.9),PhysicsConsts.ORIENT_DOWN,true);//atras/baixo/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-0.9,-0.9,0.9),PhysicsConsts.ORIENT_DOWN,true);//atras/baixo/direita
	
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(1,1,0),PhysicsConsts.ORIENT_UP,true);//frente/alto/meio
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(1,-1,0),PhysicsConsts.ORIENT_DOWN,true);//frente/baixo/meio
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-1,1,0),PhysicsConsts.ORIENT_UP,true);//atras/alto/meio
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-1,-1,0),PhysicsConsts.ORIENT_DOWN,true);//atras/baixo/meio
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(1,0,-1),PhysicsConsts.ORIENT_WEST,true);//frente/meio/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(1,0,1),PhysicsConsts.ORIENT_EAST,true);//frente/meio/direita
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-1,0,-1),PhysicsConsts.ORIENT_WEST,true);//atras/meio/esquerda
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Thruster"), new Vector3(-1,0,1),PhysicsConsts.ORIENT_EAST,true);//atras/meio/direita
*/
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Shield"), new Vector3(0,.1,0),PhysicsConsts.ORIENT_UP,true);
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Generator"), new Vector3(0,0,-0.1),PhysicsConsts.ORIENT_UP,true);
//	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Power Cell"), new Vector3(0,0,0.1),PhysicsConsts.ORIENT_UP,true);
	
	//Weapons
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Laser"), new Vector3(1,0,-.01),PhysicsConsts.ORIENT_NORTH,true);
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Weapon"), new Vector3(1,0,.01),PhysicsConsts.ORIENT_NORTH,true);
	
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Engine"), new Vector3(-1,-0.1,-0.1),PhysicsConsts.ORIENT_SOUTH,true);
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Engine"), new Vector3(-1,0.1,0.1),PhysicsConsts.ORIENT_SOUTH,true);
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Engine"), new Vector3(-1,-0.1,0.1),PhysicsConsts.ORIENT_SOUTH,true);
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup("Engine"), new Vector3(-1,0.1,-0.1),PhysicsConsts.ORIENT_SOUTH,true);
	
	
	md.addWareGroupAtPos(getDataManager().getWareManager().getWareGroup(Consts.GRP_CAMERA.toString()),new Vector3(-2.4,2.4,0),PhysicsConsts.ORIENT_NORTH,true);
	//slot.
	
	
}


public  Ware getWare(String ware){
	return dataManager.getWareManager().getWare(ware);
}


public void initWares(){
	
	Iterator<String> iterator= dataManager.getWareManager().getWares().keySet().iterator();
		while (iterator.hasNext()) {
			Ware p=dataManager.getWareManager().getWares().get((String)iterator.next());
			if (p.propertyExists(PropertyEnum.DTL_MODEL_ID)){
				p.setModelData(getModelID(p.getPropertyAsInt(PropertyEnum.DTL_MODEL_ID)));
			}
		}
}

public ModelData getModelID(int id){
	return m3D.getModelByID(id);
}

public ModelData addModelData(int id,String modelData,double mass ){
	ModelData md=new ModelData(dataManager,id,modelData,mass );
	m3D.addModelData(md);
	return md;
}

public ModelData addModelData(ModelData md){
	m3D.addModelData(md);
	return md;
}


public Vector<ModelData> getModels() {
	return m3D.getModels();
}

public DataManager getDataManager() {
	return dataManager;
}
}
