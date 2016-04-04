package com.cristiano.galactic.model.Entity.Abstract;


import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

/*
 * Essa classe é a base para as demais classes, daqui elas se separam entre 
 * Objetos físicos e objetos de dados (como wares,grupos, detail, etc)
 */

public abstract class AbstractGameObject extends ObjetoPersistente {
	private DataManager dataManager;


	public AbstractGameObject(int id,DataManager dataManager,String name,String type){
		super(id,name);
		this.dataManager=dataManager;
		setType(type);
	}
	
	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	


	public DataManager getDataManager() {
		return dataManager;
	}
	
	public String getType() {
		return props.getPropertyAsString(PropriedadesObjeto.PROP_TYPE);
		
	}
	public void setType(String type) {
		if (type.toString() != getType())
			firePropertyChange2( MVCProperties.TYPE_PROPERTY, getType(), type );
		props.setProperty(PropriedadesObjeto.PROP_TYPE, type.toString());
	}
	
	
	


}
