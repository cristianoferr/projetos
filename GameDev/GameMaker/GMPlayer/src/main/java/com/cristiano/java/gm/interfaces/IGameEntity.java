package com.cristiano.java.gm.interfaces;

import java.util.List;

import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.product.IGameElement;

/*
 * GameElement ��� qualquer elemento que prov���m de uma Blueprint
 * */
public interface IGameEntity extends ISerializeJSON   {

	
	
	void loadFromElement(IGameElement ge);
	IGameElement getElement();
	boolean hasTag(String compIdent);
	
	//these change the element inside the entity
	void setProperty(String prop,String value);
	void setProperty(String prop,int value);
	void setProperty(String prop,float value);
	
	void setEntityManager(EntityManager entMan);
	
	int getId();//retorna um id unico
	
	
	boolean isComponent();
	
	void attachComponent(IGameComponent comp);//anexo o component no objeto
	
	boolean containsComponent(String compIdent);
	boolean containsComponentWithTag(String tag);
	boolean containsComponent(IGameComponent comp);
	
	//Search the component in the component list (componentList)
	IGameComponent getComponentWithTag(String ident);
	IGameComponent getComponentWithIdentifier(String compClass);
	
	//A diferen���a desse dois ��� que o String ��� generico (remove todos os componentes do tipo especificado)
	void removeComponent(IGameComponent comp);
	void removeComponent(String comp);
	int size();
	
	int countsComponent(String ident);
	List<IGameComponent> getAllComponents();
	List<IGameComponent> getAllComponents(List<IGameComponent> ret);
	List<IGameComponent> getComponents(String compClass);
	List<IGameComponent> getComponents(String tagAll, List<IGameComponent> ret);
	List<IGameComponent> getComponentsWithIdentifier(String compIdent, List<IGameComponent> ret);
	List<IGameComponent> getComponentsWithIdentifier(String compIdent);
	void removeAllComponents();
	
	void deactivate();
	void activate();
	boolean isActive();
	void resetComponents();
	void attachComponents(List<IGameComponent> components);
	
	//will free any object referenced
	void free();
	void setListed();
	void setUnlisted();
	boolean isListed();
	
	//dont check if its being used (it was reusing components already in use)
	void removeComponentSimple(IGameComponent comp);
	void report(List<IGameEntity> alreadyShown,String spaces);
	
}
