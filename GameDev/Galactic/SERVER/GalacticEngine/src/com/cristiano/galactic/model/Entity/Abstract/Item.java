package com.cristiano.galactic.model.Entity.Abstract;


import java.util.Map;
import java.util.WeakHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.cyclone.entities.geom.GeomSphere;
import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.controller.handlers.PhysicsItem;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.Entity.Logic.SlotGrouping;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.galactic.model.faction.AbstractFaction;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.gamelib.propriedades.Propriedades;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;
import com.jme3.scene.Spatial;


public abstract class Item extends AbstractGameObject {
	//private Mass mass;
	//private Quaternion orientation;
	private Geom geom=null;
	private Ware shipWare;
	private boolean alive=true;
	//private Vector3 velocity,rotation;
	private AbstractFaction owner;
	
	
	//private Map<String,PropertyValue> modelProperty=new HashMap<String,PropertyValue>();
	
	//private Date ultimoUpdateCoord=new Date();
	


	

	public Item(int id,DataManager dataManager,Geom geom,String name,String type){
		super(id,dataManager,name,type);
		this.setDataManager(dataManager);
		
		setProperty(PropriedadesObjeto.PROP_ORIENTATION, new Quaternion());
		setProperty(PropriedadesObjeto.PROP_SPEED, new Vector3());
		setProperty(PropriedadesObjeto.PROP_ROTATION, new Vector3());
		setProperty(PropriedadesObjeto.PROP_POSITION, new Vector3());
		

		this.geom = geom;
		
		
	}
	
	public void initializeModelProperty(){
		if (shipWare==null) {return;}
		getProps().propagateFrom(shipWare.getProps());
/*		Iterator iterator = shipWare.getProps().getAllProps().keySet().iterator();
	    while (iterator.hasNext()) {
	    	String key = (String) iterator.next();
	    	if (!propertyExists(key)){
	    		setProperty(key, shipWare.getProperty(key));
	    	}
	    	
	    }*/
	   
	    
	}

	
	
	public void setVelocity(Vector3 vec){
		if (body!=null){
			body.setVelocity(vec.x,vec.y,vec.z);}
	}

	public void setVelocity(double x,double y,double z){
		if (body!=null){
			body.setVelocity(x,y,z);}
	}
	public void setRotation(Vector3 vec){
		if (body!=null)
			body.setRotation(vec.x,vec.y,vec.z);
	}



	//----------------------------------- world association -----------------------------------

	/**
	 * store reference to world.
	 */
	private Sistema world;

	/**
	 * @return world that contains this item
	 */
	public Sistema getWorld() {
	    return world;
	}

	/**
	 * Constant for firing property change events for the 'world' links.
	 */


	/**
	 * Only the {@link Sistema} class calls this when this item gets added to a world.
	 * If this can be called from somewhere else the implementation would be more complex to ensure having proper
	 * bidirectional links.
	 *
	 * @param value world that contains this item
	 */
	public void setWorld( Sistema value ) {
	    Sistema oldValue = this.world;
	    if (value==null){
	    	return;
	    }
	    if ( !value.equals(oldValue) ) {
	        if ( oldValue != null ) {
	            oldValue.removeItem(this);
	        }
	        this.world = value;
	        firePropertyChange2( MVCProperties.WORLD_PROPERTY, oldValue, value );
	    }
	}


	// ----------------------------------- spatial association -----------------------------------

	/**
	 * store reference to this Items associated scenegraph Spatial.
	 */
	private PhysicsItem body;

	/**
	 * As Spatial cannot have a backlink to this Item we store all the backlinks in a Map to allow navigating from
	 * Spatial to Item.
	 *
	 * @see #getItem(Spatial)
	 */
	private static final Map<PhysicsItem, Item> SPATIALBACKLINKS = new WeakHashMap<PhysicsItem, Item>();

	/**
	 * @return the scenegraph Node associated with this Item
	 */
	public PhysicsItem getBody() {
	    return body;
	}

	/**
	 * Change the associated scenegraph Node.
	 *
	 * @param node new Spatial
	 */
	public void setBody( PhysicsItem node ) {

		PhysicsItem oldValue = this.body;
	    if ( oldValue != node ) {
	        this.body = node;
	        if ( node != null ) {
	        	onBodyChanged();
	            // here we introduce the backlink
	        	Item oldItem = SPATIALBACKLINKS.put( node, this );
	            if ( oldItem != null ) {
	          //      Logger.getLogger( PhysicsSpace.LOGGER_NAME ).warning( "Spatial '" + node + "' was registered for '" + oldItem + "', now registered for '" + this + "'" );
	                oldItem.setBody( null );
	            }
	        }
	        if ( oldValue != null && SPATIALBACKLINKS.get( oldValue ) == this ) {
	            // here we remove it
	            SPATIALBACKLINKS.remove( oldValue );
	        }
	    }
	}

	//This method is called when the body is changed...
	public void onBodyChanged() {
		
	}





	/**
	 * Retrieve the Item that is associated with the given Spatial. If the given spatial itself is not associated with
	 * an Item this methods walks up the scenegraph hierarchy to find a Node that is associated with an Item.
	 *
	 * @param spatial a scenegraph Spatial
	 * @return the associated with the Spatial, null if no such Item was found
	 */

	public static Item getItem( PhysicsItem spatial ) {
		Item item = null;
	  //  while ( item == null && spatial != null ) {
	        item = SPATIALBACKLINKS.get( spatial );
	   //     spatial = spatial.getParent();
	  //  }
	    return item;
	}
	
	//Vector3d linVel=new Vector3d();

	public boolean oktoUpdate(){
		/*Date now=new Date();
		long i2=now.getTime();
		long i1=ultimoUpdateCoord.getTime();
		int dif=(int)(i2-i1);
		return (dif>5);*/
		return true;
	}
	
	
	/**
	 * @return the shipWare
	 */
	public Ware getShipWare() {
		return shipWare;
	}
	
	
	
	/*
	 * This method updates the model Item with the physics object position.
	 * Theres a small delay between updates to avoid too many events to be fired.
	 */
	public void propagate_physics_properties(){
	//	System.out.println("update_coord() ");
		if (getBody()==null){return;}
			if (oktoUpdate()){
				updateOrientation();
				updateVelocity();
				updateRotation();
				updateCoord();
				//ultimoUpdateCoord=new Date();
			}
		}



	private void updateCoord() {
		Vector3 pos=getBody().getPosition();
		
		//Updates from physical obj to here
		Vector3 coord=getCoord();
		//if (!coord.isEqual(pos)){
		//	System.out.println(getName()+" upd coord="+coord+" to:"+pos);
			firePropertyChange2( MVCProperties.COORD_PROPERTY, coord, pos);
			
			coord.setX(pos.x);
			coord.setY(pos.y);
			coord.setZ(pos.z);
			
			
			
		//}
	}
	
	
	
	public void setOrientation(Quaternion q){
		getBody().setOrientation(q);
		setProperty(PropriedadesObjeto.PROP_ORIENTATION, q);
	}
	
	private void updateOrientation(){
		//	System.out.println("update_coord() ");
			if (getBody()!=null){
					Quaternion pos=getBody().getOrientation();
					
					if (!pos.isEqual(getOrientation())){
						firePropertyChange2( MVCProperties.ORIENTATION_PROPERTY, getOrientation(), new Quaternion(pos) );
						Quaternion orientation=getOrientation();
						orientation.i=pos.i;
						orientation.j=pos.j;
						orientation.k=pos.k;
						orientation.r=pos.r;
					}
				}
		}
	private void updateVelocity(){
		//	System.out.println("update_coord() ");
			if (getBody()!=null){
					Vector3 pos=getBody().getVelocity();
					Vector3 velocity=getVelocity();
					if (!pos.isEqual(velocity)){
						firePropertyChange2( MVCProperties.VELOCITY_PROPERTY, velocity, new Vector3(pos) );
						velocity.x=pos.x;
						velocity.y=pos.y;
						velocity.z=pos.z;
					}
				}
		}
	
	private void updateRotation(){
		//	System.out.println("update_coord() ");
			if (getBody()!=null){
					Vector3 pos=getBody().getRotation();
					Vector3 rotation=getRotation();
					if (!pos.isEqual(rotation)){
						firePropertyChange2( MVCProperties.ROTATION_PROPERTY, rotation, new Vector3(pos) );
						rotation.x=pos.x;
						rotation.y=pos.y;
						rotation.z=pos.z;
					}
				}
		}
	
	
	public Geom getGeom(){
		return geom;
	}
	
	
	public void turn(float time){
	//System.out.println("item.turn() pos: "+getCoord());	
		propagate_physics_properties();
	}

	public double getRadius() {
		return getGeom().getRadius();
	}
	
	public void setRadius(double r) {
		((GeomSphere)getGeom()).setRadius(r);
		
		
	}
	
	public double getMass() {
		if (getBody()!=null) {return getBody().getMass();}
		return 0;
	}

	


	
	public Vector3 getCoord(){
//		update_coord();
		return getPropertyAsVector3(PropriedadesObjeto.PROP_POSITION);
	}
	
	public String toString(){
		return getType()+": "+getName();
	}


	/**
	 * @param shipWare the shipWare to set
	 */
	public void setShipWare(Ware shipWare) {
		firePropertyChange2(
	            MVCProperties.SHIPWARE_PROPERTY,
	            this.shipWare, shipWare);
		
		this.shipWare = shipWare;
		initializeModelProperty();
	}
	/*
	public void setShipWare(String shipWare) {
		firePropertyChange(
	            ItemsController.SHIPWARE_PROPERTY,
	            this.shipWare, shipWare);
		this.shipWare = getDataManager().getWareManager().getWare(shipWare);
	}*/

	public ModelData getModelData(){
		return getShipWare().getModelData();
	}
	
	public SlotGrouping getSlotGrouping(String s){
		return getModelData().getSlotGrouping().get(s);
	}


	public boolean isAlive() {
		if (body==null){
			return false;
		} 
		if (!body.isAlive()){return false;}
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	public Quaternion getOrientation() {
		return getPropertyAsQuaternion(PropriedadesObjeto.PROP_ORIENTATION);
	}


	public Vector3 getVelocity() {
		return getPropertyAsVector3(PropriedadesObjeto.PROP_SPEED);
	}


	public Vector3 getRotation() {
		return getPropertyAsVector3(PropriedadesObjeto.PROP_ROTATION);
	}
	
	public Vector3 getTransformDirection(Vector3 vec){

		
		return getBody().getTransformMatrix().transformDirection(vec);
	}





	public void setGeom(Geom geom) {
		this.geom = geom;
	}




	public Vector3 getGalacticPos() {
		return world.getGalacticPos();
	}





	public Element buildXML(Document testDoc) {
		
			 Element element = testDoc.createElement("entity");
			 element.setAttribute("id", Integer.toString(getId()));
			 element.setAttribute("name", getName());
			 if (owner!=null){
				 element.setAttribute("owner", Integer.toString(owner.getId()));
			 }
			 element.setAttribute("type", getType());
			 if (shipWare!=null){
				 Element elPosition = testDoc.createElement("model");
				 element.appendChild(elPosition);
				 elPosition.setAttribute("shipWare", shipWare.getName());
				 elPosition.setAttribute("idShipWare", Integer.toString(shipWare.getId()));
			 } else {
				 element.appendChild(geom.buildXML(testDoc));
			 }
			 Element elPosition = testDoc.createElement("coord");
			 element.appendChild(elPosition);
			 Vector3 rotation=getRotation();
			 Vector3 velocity=getVelocity();
			 Vector3 coord=getCoord();
			 Quaternion orientation=getOrientation();
			 elPosition.setAttribute("X", Double.toString(coord.getX()));
			 elPosition.setAttribute("Y", Double.toString(coord.getY()));
			 elPosition.setAttribute("Z", Double.toString(coord.getZ()));
			 elPosition = testDoc.createElement("rotation");
			 element.appendChild(elPosition);
			 elPosition.setAttribute("X", Double.toString(rotation.getX()));
			 elPosition.setAttribute("Y", Double.toString(rotation.getY()));
			 elPosition.setAttribute("Z", Double.toString(rotation.getZ()));
			 elPosition = testDoc.createElement("velocity");
			 element.appendChild(elPosition);
			 elPosition.setAttribute("X", Double.toString(velocity.getX()));
			 elPosition.setAttribute("Y", Double.toString(velocity.getY()));
			 elPosition.setAttribute("Z", Double.toString(velocity.getZ()));
			 elPosition = testDoc.createElement("orientation");
			 element.appendChild(elPosition);
			 elPosition.setAttribute("I", Double.toString(orientation.i));
			 elPosition.setAttribute("J", Double.toString(orientation.j));
			 elPosition.setAttribute("K", Double.toString(orientation.k));
			 elPosition.setAttribute("R", Double.toString(orientation.r));

			 
			return element;
		
	}






	public void activateKeyGroup(Enum control) {
		
		
	}
	public void deactivateKeyGroup(Enum control) {
		
		
	}
	
	

	/**
	 * Esse método carrega as informações úteis do Node XML em um objeto de propriedades 
	 * permitindo criar a entidade facilmente
	 * @param node
	 * @return
	 */
	public static Propriedades loadNodeIntoProp(Node n) {
		Propriedades props=new Propriedades();
		Node node=n;
		
		NamedNodeMap atrs=node.getAttributes();
		if (atrs.getNamedItem("texture")!=null){
			props.setProperty("texture", atrs.getNamedItem("texture").getNodeValue());
		}
		if (atrs.getNamedItem("owner")!=null){
			props.setProperty("owner", atrs.getNamedItem("owner").getNodeValue());
		}
		node=node.getFirstChild();
		while (node!=null){
			atrs=node.getAttributes();
			if (node.getNodeName().equals("geom")){
				if (atrs.getNamedItem("type").getNodeValue().equals("SPHERE")){
					props.setProperty("density", Double.parseDouble(atrs.getNamedItem("density").getNodeValue()));
					props.setProperty("radius", Double.parseDouble(atrs.getNamedItem("radius").getNodeValue()));
				}
			}
			if (node.getNodeName().equals("coord")){
				Vector3 coord=new Vector3(Double.parseDouble(atrs.getNamedItem("X").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Y").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Z").getNodeValue()));
				props.setProperty(PropriedadesObjeto.PROP_POSITION,coord);
			}
			if (node.getNodeName().equals("rotation")){
				Vector3 coord=new Vector3(Double.parseDouble(atrs.getNamedItem("X").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Y").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Z").getNodeValue()));
				props.setProperty(PropriedadesObjeto.PROP_ROTATION,coord);
			}
			if (node.getNodeName().equals("velocity")){
				Vector3 coord=new Vector3(Double.parseDouble(atrs.getNamedItem("X").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Y").getNodeValue()),Double.parseDouble(atrs.getNamedItem("Z").getNodeValue()));
				props.setProperty(PropriedadesObjeto.PROP_SPEED,coord);
			}
			if (node.getNodeName().equals("orientation")){
				Quaternion coord=new Quaternion(Double.parseDouble(atrs.getNamedItem("R").getNodeValue()),
						Double.parseDouble(atrs.getNamedItem("I").getNodeValue()),
						Double.parseDouble(atrs.getNamedItem("J").getNodeValue()),
						Double.parseDouble(atrs.getNamedItem("K").getNodeValue()));
				props.setProperty(PropriedadesObjeto.PROP_ORIENTATION,coord);
			}
			
			
			node=node.getNextSibling();
		}

		return props;
	}
	
	/**
	 * Usado para carregar as propriedades que foram carregadas do XML
	 * @param props
	 */
	public void loadFromProps(Propriedades props){
		Propriedades thisProps=getProps();
		
		if (props.propertyExists(PropriedadesObjeto.PROP_POSITION)){
			thisProps.setProperty(PropriedadesObjeto.PROP_POSITION, 
					props.getPropertyAsVector3(PropriedadesObjeto.PROP_POSITION));
			getBody().setPosition(new Vector3(props.getPropertyAsVector3(PropriedadesObjeto.PROP_POSITION)));
		} else {
			thisProps.setProperty(PropriedadesObjeto.PROP_POSITION,new Vector3());
			getBody().setPosition(new Vector3());
		}
		
		if (props.propertyExists(PropriedadesObjeto.PROP_ORIENTATION)){
			thisProps.setProperty(PropriedadesObjeto.PROP_ORIENTATION, 
					props.getPropertyAsQuaternion(PropriedadesObjeto.PROP_ORIENTATION));
			getBody().setOrientation(new Quaternion(props.getPropertyAsQuaternion(PropriedadesObjeto.PROP_ORIENTATION)));
		}else {
			thisProps.setProperty(PropriedadesObjeto.PROP_ORIENTATION,new Quaternion());
			getBody().setOrientation(new Quaternion());
		}
		
		if (props.propertyExists(PropriedadesObjeto.PROP_SPEED)){
			thisProps.setProperty(PropriedadesObjeto.PROP_SPEED, 
					props.getPropertyAsVector3(PropriedadesObjeto.PROP_SPEED));
			getBody().setVelocity(new Vector3(props.getPropertyAsVector3(PropriedadesObjeto.PROP_SPEED)));
		}else {
				thisProps.setProperty(PropriedadesObjeto.PROP_SPEED,new Vector3());
				getBody().setVelocity(new Vector3());
			}
		
		if (props.propertyExists(PropriedadesObjeto.PROP_SPEED)){
			thisProps.setProperty(PropriedadesObjeto.PROP_ROTATION, 
					props.getPropertyAsVector3(PropriedadesObjeto.PROP_ROTATION));
			getBody().setRotation(new Vector3(props.getPropertyAsVector3(PropriedadesObjeto.PROP_ROTATION)));
		}else {
			thisProps.setProperty(PropriedadesObjeto.PROP_ROTATION,new Vector3());
			getBody().setRotation(new Vector3());
		}
		
		 /*	if (getGeom().getType().equals(PrimitiveType.SPHERE)){
				getGeom().setDensity(props.getPropertyAsDouble("density"));
				((GeomSphere)getGeom()).setRadius(props.getPropertyAsDouble("radius"));
				getBody().setMass(getGeom().calculateMass());
			}
		*/
		
		

	}

	public AbstractFaction getOwner() {
		return owner;
	}

	public void setOwner(AbstractFaction owner) {
		this.owner = owner;
	}
	
	public void setOwner2(AbstractFaction owner) {
		this.owner = owner;
		owner.addOwnership(this);
	}

	public void setTarget(Item selection) {
		
	}

	

	

}
