package com.cristiano.galactic.view.models;



import java.awt.Color;

import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.cyclone.entities.geom.Geom.PrimitiveType;
import com.cristiano.cyclone.entities.geom.GeomBox;
import com.cristiano.cyclone.entities.geom.GeomSphere;
import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.enums.ControlEnum;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;
import com.cristiano.gamelib.propriedades.PropriedadesView;

public abstract class AbstractItemView implements IObjeto {
	

	
	
	//rotation=força angular
	//orientation=orientação do objeto(angulo)
	private IView view;
	private String texture=null;
	private String shipWare;
	private ItemsViewManagerAbstract itemsView;
	protected double radius;
	
	Color corAtmos=null;

	PropriedadesView props; 

	public AbstractItemView(IView view){
		this.view=view;
		props=new PropriedadesView(this);
		props.setProperty(PropriedadesObjeto.PROP_SPEED, new Vector3());
		props.setProperty(PropriedadesObjeto.PROP_POSITION, new Vector3());
		props.setProperty(PropriedadesObjeto.PROP_ABSOLUTE_POSITION, new Vector3());
		props.setProperty(PropriedadesObjeto.PROP_ROTATION, new Vector3());
		props.setProperty(PropriedadesObjeto.PROP_ORIENTATION, new Quaternion());
		
		
		itemsView=view.getItemsView();
		//System.out.println("basicmodel");

	}
	
	public IView getJogo(){
		return view;
	}

	public void setColor(Color color) {
		this.corAtmos=color;
		
	}
	
	public void clearDebugNode(){
		
	}
	
	
	public double getRelativeScreenSize(){
		double distancia=getCoord().magnitude()+1;
		return radius/distancia;
	}
	
	public IView getView(){return view;}

	/*
	 *  Referente a criacao do modelo
	 */
	
	public abstract void loadModel(ModelData md);
	public void createVisualNodeFromGeom(Item item) {
		
		Geom geom=item.getGeom();
		if (geom.getType()==PrimitiveType.SPHERE){
			attachSphere(((GeomSphere)geom).radius);
		} else 
		if (geom.getType()==PrimitiveType.BOX){
			attachBox(((GeomBox)geom).halfSize);
		}
	
		
		

	}

	public abstract void attachSphere(double radius);
	public abstract void attachBox(Vector3 halfSize);
	
	
	

	/*
	 *  Referente atualização do modelo visual  (controller->view)
	 */
	
public void update(){
		
	}
	

	/*
	 *  Referente desenho do modelo
	 */
	public abstract void updatePosition(boolean direct);
	
	public String getTexture() {
		if ((texture!=null) || ("".equals(texture))) {
				return null;
		}
		
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public Vector3 getCameraPosition(){
		return itemsView.getCameraPosition(this);
	}
	
	/*
	 *  Informacoes genericas do modelo
	 */
	public String getName() {
		return props.getPropertyAsString(PropriedadesObjeto.PROP_NAME);
	}
	public void setName(String name) {
		props.setProperty(PropriedadesObjeto.PROP_NAME,name);
	}
	public String getShipWare() {
		return shipWare;
	}
	public void setShipWare(String shipWare) {
		this.shipWare = shipWare;
	}
	public Vector3 getCoord() {
		return props.getPropertyAsVector3(PropriedadesObjeto.PROP_POSITION);
	}
	public void setCoord(Vector3 coord) {
		props.setProperty(PropriedadesObjeto.PROP_POSITION, coord);
		view.notifyPropertyChanged(this, PropriedadesObjeto.PROP_POSITION);

	}
	
	public void setAbsolutePosition(Vector3 vector) {
		props.setProperty(PropriedadesObjeto.PROP_ABSOLUTE_POSITION, vector);
	}
	public Quaternion getOrientation() {
		return props.getPropertyAsQuaternion(PropriedadesObjeto.PROP_ORIENTATION);
	}
	public void setOrientation(Quaternion orientation) {
		props.setProperty(PropriedadesObjeto.PROP_ORIENTATION, orientation);
	}


	public void activateControl(Enum ctl){
		itemsView.activateControl(this,ctl);
	}
	public void deactivateControl(Enum ctl){
		itemsView.deactivateControl(this,ctl);
	}
	public void activateControl(ControlEnum ctl,double intesity){
		itemsView.activateControl(this,ctl,intesity);
	}
	
	public Vector3 getPointInWorldSpace(Vector3 vec){
		return itemsView.getPointInWorldSpace(this, vec);
	}
	
	public Vector3 getTransformDirection(Vector3 vec){
		return itemsView.getTransformDirection(this, vec);
	}
	
	
	public void debugInternalPoints(){
		
	}
	


	public void setVelocity(Vector3 newValue) {
		Vector3 velocity=getVelocity();
		velocity.x=newValue.x;
		velocity.y=newValue.y;
		velocity.z=newValue.z;
		view.notifyPropertyChanged(this, PropriedadesObjeto.PROP_SPEED);
	}

	public void setRotation(Vector3 newValue) {
		Vector3 rotation=getRotation();
		rotation.x=newValue.x;
		rotation.y=newValue.y;
		rotation.z=newValue.z;
		
		view.notifyPropertyChanged(this, PropriedadesObjeto.PROP_ROTATION);
		
	}

	public Vector3 getVelocity() {
		return props.getPropertyAsVector3(PropriedadesObjeto.PROP_SPEED);
	}
	public Vector3 getRotation() {
		return props.getPropertyAsVector3(PropriedadesObjeto.PROP_ROTATION);
	}

	
	
	
	public String toString(){
		return getName();
	}
	
	@Override
	public void setCoord(double x, double y, double z) {
		getCoord().x=x;
		getCoord().y=y;
		getCoord().z=z;
		view.notifyPropertyChanged(this, PropriedadesObjeto.PROP_POSITION);
	}

	
	@Override
	public void setProperty(String property, Vector3 vlr) {
		props.setProperty(property, vlr);
		
	}

	

	@Override
	public void setProperty(String property, Object vlr) {
		props.setProperty(property, vlr);
		
	}
	@Override
	public void setProperty(String property, double vlr) {
		props.setProperty(property, vlr);
		
	}

	@Override
	public void setProperty(String property, String vlr) {
		props.setProperty(property, vlr);
		
	}
	
	
	@Override
	public String getPropertyAsText(String property) {
		if (property.equals(PropriedadesObjeto.PROP_NAME)){
			return getName();
		}
		
		return props.getPropertyAsString(property);
		
	}

	@Override
	public Vector3 getPropertyAsVector3(String property) {
		return props.getPropertyAsVector3(property);
	}

	@Override
	public double getPropertyAsDouble(String property) {
		if (property.equals(PropriedadesObjeto.PROP_POSITION)){
			return getCoord().magnitude();
		}else if (property.equals(PropriedadesObjeto.PROP_SPEED)){
			return getVelocity().magnitude();
		}else if (property.equals(PropriedadesObjeto.PROP_ROTATION)){
			return getRotation().magnitude();
		}
		return props.getPropertyAsDouble(property);
	}
	
	public Color getPropertyAsColor(String property) {
		return props.getPropertyAsColor(property);
	}

	
	public double getPropertyAsDouble(Enum property) {
		return getPropertyAsDouble(property.toString());
	}
	
	public Color getPropertyAsColor(Enum property) {
		return getPropertyAsColor(property.toString());
	}

	@Override
	public Vector3 getAbsolutePosition() {
		return props.getPropertyAsVector3(PropriedadesObjeto.PROP_ABSOLUTE_POSITION);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	
}