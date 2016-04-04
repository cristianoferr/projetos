package com.cristiano.galactic.model.Entity;


import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.cristiano.cyclone.entities.geom.GeomSphere;
import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.gamelib.propriedades.Propriedades;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;


public class PlanetM extends SphereItem {
	private Item orbit;//The entity that this entity is orbting.
	Vector3 newCoord; //usado para calcular a nova posição do planeta
	 
	public PlanetM(int id,DataManager dataManager,String name,Item orbit,double orbitalRadius,double radius){
		super(id,dataManager,new GeomSphere(radius),name,null);
		setType(EntityType.ET_PLANET);
		setProperty(PlanetProperties.PP_ORBITAL_RADIUS, orbitalRadius);
		this.orbit=orbit;
		newCoord=new Vector3();
	}
	
	public Element buildXML(Document testDoc) {
		 Element element = super.buildXML(testDoc);
		 Element elPosition = testDoc.createElement("planetProperties");
		 element.appendChild(elPosition);
		 
		Vector<String> properties=PlanetProperties.getOrbitProps();

		for (String string : properties) {
			elPosition.setAttribute(string, Double.toString(getPropertyAsDouble(string)));
		}

			
		 elPosition.setAttribute("orbitando", Integer.toString(orbit.getId()));
		 
		 return element;
}
	
	


	public	void onBodyChanged() {
		super.onBodyChanged();
		getBody().setMass(getPropertyAsDouble(PlanetProperties.PP_MASS));
		
		//getBody().getOrientation().rotateByVector(new Vector3(0,0,orbitalOffset));
		//getBody().getOrientation().rotateByVector(new Vector3(orbitalInclination,0,0));
		
		
		
		}
	
	
	
public void turn(float time){
	super.turn(time);
	double orbitalRadius=getPropertyAsDouble(PlanetProperties.PP_ORBITAL_RADIUS);
	double orbitalPosition=getPropertyAsDouble(PlanetProperties.PP_ORBITAL_POSITION);
	
	double orbitalInclination=getPropertyAsDouble(PlanetProperties.PP_ORBITAL_INCLINATION);
	//double orbitalPosition=getPropertyAsDouble(PlanetProperties.PP_ORBITAL_POSITION);
	double mass=getPropertyAsDouble(PlanetProperties.PP_MASS);
	
	Vector3 coord=getBody().getPosition();
	
	
	double orbitalEccentricity=getPropertyAsDouble(PlanetProperties.PP_ORBITAL_ECCENTRICITY);
	
	double rAP=(1+orbitalEccentricity)*orbitalRadius;//no angulo 0 e 180
	double rPER=(1-orbitalEccentricity)*orbitalRadius;//no angulo 90 e 270
	
	double angle=orbitalPosition;
	angle=angle%180;
	if (angle>90){
		angle=180-angle;	
	}
	double difR=rAP-rPER;
	double currentOrbitalRadius=angle*difR/90+rPER; //distancia atual entre planeta e a estrela

	
	newCoord.x=currentOrbitalRadius;
	newCoord.y=0;
	newCoord.z=0;
	newCoord.rotateBy(Vector3.ZERO, Vector3.X, orbitalPosition);
	newCoord.rotateBy(Vector3.ZERO, Vector3.Z, orbitalInclination);
	
	Vector3 dif=coord.getSubVector(newCoord);
	dif.multiVectorScalar(time);
	
	coord.x=newCoord.x;
	coord.y=newCoord.y;
	coord.z=newCoord.z;
	coord.addVector(orbit.getCoord());//a posição é relativa ao objeto que a entidade está orbitando
	
	//System.out.println("name:"+ getName()+" vel:"+getBody().getVelocity());
	getBody().getVelocity().clear();
	//getBody().getVelocity().x=Math.random();
	
	/*
	rotationPosition += rotationVelocity * time;
	getBody().getRotation().x=0;
	getBody().getRotation().y=0;//rotationVelocity;
	getBody().getRotation().z=0;// * time;
	
	
	if( rotationPosition >= 360 ){
		rotationPosition =  rotationPosition%360 ;
	}*/

	// According to Kepler's laws of planetary motion, the velocity of an object
	// is determined by the masses and distance between the objects.
	//
	// http://en.wikipedia.org/wiki/Elliptic_orbit
	/*if (getName().equals("ikeass theta")){
		System.out.println(orbitalPosition+"  "+Formatacao.formatDistance(dif.magnitude()));
	}*/
	orbitalPosition += Math.sqrt( PhysicsConsts.Gforce* ( mass + orbit.getMass() ) *
			( 2 / (currentOrbitalRadius / 1000) - 1 / (orbitalRadius / 1000) ) )  // velocity of planet at this point
		/ (orbitalRadius * 2 * Math.PI)  // approx. circumference of the orbit
		* 360  // number of degrees in the circumference
		/500   // number of frames per second  TODO: rever esse número
		* time;
	if( orbitalPosition >= 360 ){
		orbitalPosition = orbitalPosition%360 ;
	}

	setProperty(PlanetProperties.PP_ORBITAL_POSITION,orbitalPosition);

}

	


public Item getOrbit() {
	return orbit;
}
/*
	public static PlanetM createRandom(String name,Item star){
		double x=(double)(-Star.MAX_DISTANCE/2+Math.random()*Star.MAX_DISTANCE);
		double y=(double)(-Star.MAX_DISTANCE/2+Math.random()*Star.MAX_DISTANCE);
		double z=(double)(-PhysicsConsts.AU/2+Math.random()*PhysicsConsts.AU);
		//double r=(double)(MIN_RADIUS+Math.random()*(MAX_RADIUS-MIN_RADIUS));
		return createPlanet(star.getDataManager(),name,star,new Vector3(x,y,z),-1,"moon1_grey_diff.jpg");

	}*/
	
	public static PlanetM createPlanet(DataManager dataManager,String name,Item star,Vector3 pos,double r,String texture){
		PlanetM sis=new PlanetM(getNextID(),dataManager,name,star,pos.magnitude(),r);
		sis.getCoord().setX(pos.x);
		sis.setTexture(texture);
		sis.getCoord().setY(pos.y);
		sis.getCoord().setZ(pos.z);
		return sis;
	}

	
	public static Propriedades loadNodeIntoProp(Node n) {
		Propriedades props=Item.loadNodeIntoProp(n);
		Node node=n;
		NamedNodeMap atrs;
		node=node.getFirstChild();
		while (node!=null){
			atrs=node.getAttributes();
			if (node.getNodeName().equals("orbitProperties")){
				
				Vector<String> properties=PlanetProperties.getOrbitProps();
				for (String string : properties) {
					props.setPropertyFromXML(string, atrs.getNamedItem(string).getNodeValue());
				}
			}
			if (node.getNodeName().equals("planetProperties")){
				
				Vector<String> properties=PlanetProperties.getPlanetProps();
				for (String string : properties) {
					props.setPropertyFromXML(string, atrs.getNamedItem(string).getNodeValue());
				}
			}

			node=node.getNextSibling();
		}
		return props;
	}
	
	public static PlanetM createFromXML(int id, String name, Node node,
			DataManager dataManager,SphereItem orbitando,Sistema sistema) {
		NamedNodeMap atrs=node.getAttributes();
		
		Propriedades props=loadNodeIntoProp(node);
		
		
		
		PlanetM planet=new PlanetM(id,dataManager,name,orbitando,props.getPropertyAsDouble(PlanetProperties.PP_ORBITAL_RADIUS),props.getPropertyAsDouble(PlanetProperties.PP_RADIUS));
		planet.getGeom().setDensity(props.getPropertyAsDouble(PlanetProperties.PP_DENSITY));
		
		Vector<String> properties=PlanetProperties.getOrbitProps();
		for (String string : properties) {
			planet.setProperty(string, props.getPropertyAsObject(string));
		}
		
		properties=PlanetProperties.getPlanetProps();
		for (String string : properties) {
			planet.setProperty(string, props.getPropertyAsObject(string));
		}
		
		
		parseMoons(node, dataManager,planet,sistema);
		
		sistema.addEntity(planet);
		planet.turn(1);
		planet.turn(1);
		planet.loadFromProps(props);
		
		//s.setTexture(props.getPropertyAsString("texture"));
		return planet;
	}

	private static void parseMoons(Node node, DataManager dataManager,PlanetM pai,Sistema world) {
		NamedNodeMap atrs;
		Node nodeFilho=node.getFirstChild();
		while (nodeFilho!=null){
			if (nodeFilho.getNodeName().equals("entities")){
				Node nodeP=nodeFilho.getFirstChild();
		        while (nodeP!=null){
		        	atrs=nodeP.getAttributes();
		        	int idP=Integer.parseInt(atrs.getNamedItem(PropriedadesObjeto.PROP_ID.toString()).getNodeValue());
					String typeP=atrs.getNamedItem(PropriedadesObjeto.PROP_TYPE.toString()).getNodeValue();
					String nameP=atrs.getNamedItem(PropriedadesObjeto.PROP_NAME.toString()).getNodeValue();
					if (typeP.equals(EntityType.ET_PLANET)){
		        		PlanetM.createFromXML(idP, nameP, nodeP, dataManager, pai,world);
		        	}
		        	nodeP=nodeP.getNextSibling();
		        	
		        }
				
			}
			nodeFilho=nodeFilho.getNextSibling();
		}
	}
}
