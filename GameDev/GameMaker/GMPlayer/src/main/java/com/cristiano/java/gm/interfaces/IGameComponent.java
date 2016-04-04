package com.cristiano.java.gm.interfaces;

import com.cristiano.java.product.IManageElements;



public interface IGameComponent extends IGameEntity {
	

	
	
	
	void addInfo(String info);
	String getDebugInfo();
	
	public String getInfo();
	
	//retorna o identificador do componente, cada tipo de componente deve ter um identificador, 2 componentes com a mesma fun��o podem ter o mesmo identificador
	String getIdentifier(); 
	boolean isFirstTick();
	 void archive(); //store (or otherwise dont iterate everytime)
	 boolean isArchived();
	 void dearchive();
	IGameComponent clonaComponent();
	
	//get the info from the given component...
	void merge(IGameComponent comp);
	
	//this will load from the component element...
	void loadDefault(IManageElements em);
	
	//will reset the component... (useful for removing visible renders)
	void resetComponent();
	void setFirstTick();
	
	//when a entity is cloned...
	void setSourceID(int id);
	int getSourceID();
}
