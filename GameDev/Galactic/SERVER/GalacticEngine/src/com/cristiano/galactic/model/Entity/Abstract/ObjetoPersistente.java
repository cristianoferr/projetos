package com.cristiano.galactic.model.Entity.Abstract;


import java.awt.Color;

import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.gamelib.propriedades.AbstractModel;
import com.cristiano.gamelib.propriedades.Propriedades;
import com.cristiano.gamelib.propriedades.PropriedadesModel;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

public abstract class ObjetoPersistente extends AbstractModel{
	private static int count=0;
	PropriedadesModel props;

	
	public ObjetoPersistente(int id,String name){
		props=new PropriedadesModel(this);
		setProperty(PropriedadesObjeto.PROP_NAME, name);

		if (id>count){count=id;}
		setProperty(PropriedadesObjeto.PROP_ID, id);
		
	}
	public ObjetoPersistente(){
		props=new PropriedadesModel(this);
	}
	
	public String getName() {
		return getPropertyAsText(PropriedadesObjeto.PROP_NAME);
	}
	public void setName(String name) {
		setProperty(PropriedadesObjeto.PROP_NAME, name);
		
	}
	
	
public static final int getNextID(){
		count++;
		return count;
	}
	
	public int getId(){
		return getPropertyAsInt(PropriedadesObjeto.PROP_ID);
	}
	
	public void setId(int id) {
		if (id>count){count=id;}
		setProperty(PropriedadesObjeto.PROP_ID, id);
	}
	
	public Color getPropertyAsColor(String prop) {
		return props.getPropertyAsColor(prop);
	}
	
	
	public Color getPropertyAsColor(Enum prop) {
		return props.getPropertyAsColor(prop.toString());
	}
	
	public String getPropertyAsText(String property){
		return props.getPropertyAsString(property).toString();
	}
	public Vector3 getPropertyAsVector3(String property){
		return props.getPropertyAsVector3(property);
	}
	public Quaternion getPropertyAsQuaternion(String property){
		return props.getPropertyAsQuaternion(property);
	}
	public double getPropertyAsDouble(String property){
		return props.getPropertyAsDouble(property);
	}
	public Propriedades getPropertyAsPropriedades(String property){
		return props.getPropertyAsPropriedades(property);
	}
	public void setProperty(String property, Vector3 vlr){
		props.setProperty(property, vlr);
	}
	
	public void setProperty(String property, Object vlr){
		props.setProperty(property, vlr);
	}
	
	public void setProperty(String property, Propriedades vlr){
		props.setProperty(property, vlr);
	}
	
	public void setProperty(String property, double vlr){
		props.setProperty(property, vlr);
	}
	public void setProperty(String property, String vlr){
		props.setProperty(property, vlr);
	}
	public void setProperty(String property, Quaternion vlr){
		props.setProperty(property, vlr);
	}

	public Object getProperty(Enum property){
		return props.getPropertyAsObject(property);
	}
	public Object getProperty(String property){
		return props.getPropertyAsObject(property);
	}
	
	public String getPropertyAsText(Enum property){
		return props.getPropertyAsString(property).toString();
	}
	public Vector3 getPropertyAsVector3(Enum property){
		return props.getPropertyAsVector3(property);
	}
	public Quaternion getPropertyAsQuaternion(Enum property){
		return props.getPropertyAsQuaternion(property);
	}
	public double getPropertyAsDouble(Enum property){
		return props.getPropertyAsDouble(property);
	}
	public int getPropertyAsInt(Enum property){
		return props.getPropertyAsInt(property);
	}
	public int getPropertyAsInt(String property){
		return props.getPropertyAsInt(property);
	}
	public void setProperty(Enum property, Vector3 vlr){
		props.setProperty(property, vlr);
	}
	public void setProperty(Enum property, double vlr){
		props.setProperty(property, vlr);
	}
	public void setProperty(Enum property, String vlr){
		props.setProperty(property, vlr);
	}
	public void setProperty(Enum property, Quaternion vlr){
		props.setProperty(property, vlr);
	}

	public boolean propertyExists(Enum property){
		return propertyExists(property.toString());
	}
	public boolean propertyExists(String property){
		return props.propertyExists(property);
	}
	
	public Propriedades getProps() {
		return props;
	}
}
