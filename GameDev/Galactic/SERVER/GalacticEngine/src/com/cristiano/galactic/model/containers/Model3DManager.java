package com.cristiano.galactic.model.containers;


import java.util.Iterator;
import java.util.Vector;

import com.cristiano.galactic.model.enums.PropertyEnum;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;


public class Model3DManager {
Vector <ModelData> models=new Vector<ModelData>();
DataManager dataManager;

public Model3DManager(DataManager dataManager){
	this.dataManager=dataManager;
}

private DataManager getDataManager() {
	return dataManager;
}

public void initSlotGrouping(){
	for (int i=0;i<models.size();i++){
		models.get(i).createSlotGrouping();
	}
	initWares();
}


private void initWares(){
	
	Iterator<String> iterator= dataManager.getWareManager().getWares().keySet().iterator();
		while (iterator.hasNext()) {
			Ware ware=dataManager.getWareManager().getWares().get((String)iterator.next());
			if (ware.propertyExists(PropertyEnum.DTL_MODEL_ID)){
				ware.setModelData(getModelByID(ware.getPropertyAsInt(PropertyEnum.DTL_MODEL_ID)));
			}
		}
}

/**
 * Retorna o objeto modelo com base no parametro. 
 * @param id
 * @return
 */
public ModelData getModelByID(int id){
	for (int i=0;i<models.size();i++){
		if (models.get(i).id==id) return models.get(i);
	}
	return null;
}
/*
public ModelData addModelData(int id,String modelData,float scale){
	ModelData md=new ModelData(dataManager,id,modelData,scale);
	addModelData(md);
	return md;
}*/

public ModelData addModelData(ModelData md){
	models.add(md);
	return md;
}


public Vector<ModelData> getModels() {
	return models;
}
}
