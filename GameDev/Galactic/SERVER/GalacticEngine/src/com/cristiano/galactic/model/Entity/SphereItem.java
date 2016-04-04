package com.cristiano.galactic.model.Entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.MVCProperties;


public class SphereItem extends Item {
	private String texture=null;
	public SphereItem(int id,DataManager dataManager,Geom geom, String name,String texture) {
		super(id,dataManager,geom, name,EntityType.ET_SPHERE);
		setTexture(texture);
	}
	public String getTexture() {
		return texture;
	}
	

	
	
	public final void setTexture(String texture) {
		firePropertyChange2(
				MVCProperties.TEXTURE_PROPERTY,
	            this.texture, texture);
		this.texture = texture;
	}

	
	public Element buildXML(Document testDoc) {
			 Element element = super.buildXML(testDoc);
			 element.setAttribute("texture", texture);
			 return element;
	}
			 
}
