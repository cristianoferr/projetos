package com.cristiano.galactic.model.faction;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Abstract.ObjetoPersistente;
import com.cristiano.galactic.model.enums.FactionType;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

/**
 * Classe generica usada para determinar a qual facção os objetos pertencem
 * bem como determinar a relação entre as facções em si.
 * @author cmm4
 *
 */
public abstract class AbstractFaction extends ObjetoPersistente{
	Vector<Item> ownerShip;
	
	public AbstractFaction(int id,String name,FactionType type){
		super(id,name);
		setProperty(PropriedadesObjeto.PROP_TYPE, type.toString());
		ownerShip=new Vector<Item>();
	}

	public String getType(){
		return getPropertyAsText(PropriedadesObjeto.PROP_TYPE);
	}

	
	public Element buildXML(Document testDoc) {
		
		 Element element = testDoc.createElement("entity");
		 element.setAttribute("id", Integer.toString(getId()));
		 element.setAttribute("name", getName());
		 element.setAttribute("type", getType());
		 
		return element;
	
}

	public void addOwnership(Item s) {
		ownerShip.add(s);
		
	}
}
