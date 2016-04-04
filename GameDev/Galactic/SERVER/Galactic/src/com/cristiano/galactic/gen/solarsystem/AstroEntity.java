package com.cristiano.galactic.gen.solarsystem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.gamelib.propriedades.Propriedades;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

public abstract class AstroEntity {
	Propriedades props;

	public AstroEntity(String name){
		props=new Propriedades();
		props.setProperty(PropriedadesObjeto.PROP_NAME, name);
		props.setProperty(PropriedadesObjeto.PROP_ID, SolarSystemGenerator.getNextID());
	}
	
	public String getName(){
		return props.getPropertyAsString(PropriedadesObjeto.PROP_NAME);
	}
	
	public boolean isStar(){
		return false;
	}
	public Element buildXML(Document testDoc) {
		
		 Element element = testDoc.createElement("entity");
		 element.setAttribute(PropriedadesObjeto.PROP_NAME.toString(), props.getPropertyAsString(PropriedadesObjeto.PROP_NAME));
		 element.setAttribute(PropriedadesObjeto.PROP_ID.toString(), Integer.toString(props.getPropertyAsInt(PropriedadesObjeto.PROP_ID)));
		 element.setAttribute(PropriedadesObjeto.PROP_TYPE.toString(), props.getPropertyAsString(PropriedadesObjeto.PROP_TYPE));
		 
		 return element;
	}

	public double getMass() {
		
		return props.getPropertyAsDouble(PlanetProperties.PP_MASS);
	}

	public double getRadius() {
		
		return props.getPropertyAsDouble(PlanetProperties.PP_RADIUS);
	}

		 
}
