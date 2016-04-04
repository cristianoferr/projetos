package com.cristiano.galactic.model.Entity;


import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.cristiano.cyclone.entities.geom.GeomSphere;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.gamelib.propriedades.Propriedades;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;


public class Star extends SphereItem  {
/*public static final float MIN_RADIUS=(float)Math.pow(10,7);
public static final float MAX_RADIUS=(float)Math.pow(10,15);*/


//public static final float MAX_SIZE=(float)(250*Consts.AU);

public static final int MAX_PLANETS=5;

public Star(int id,DataManager dataManager,String name,String texture){
	super(id,dataManager,new GeomSphere(1),name,texture);
	setType(EntityType.ET_STAR);
}


/*public static Star createRandom(String name,Sistema sistema){
	
	Star sis=new Star(getNextID(),sistema.getDataManager(),r,name,"sun_diff.jpg");
	//sis.getCoord().setX(1000000000000f);
	float pos=(float)0;
	sistema.addEntity(sis);
	
	PlanetM p1=PlanetM.createPlanet(sistema.getDataManager(),"Terra", sis,new Vector3((PhysicsConsts.AU),0,pos),(float)PhysicsConsts.raioTERRA,"Planets/Terran07.png");
	sistema.addEntity(p1);
	
	
	PlanetM p2=PlanetM.createPlanet(sistema.getDataManager(),"Lua", sis,new Vector3((PhysicsConsts.AU+PhysicsConsts.C),0,pos),(float)PhysicsConsts.raioLUA,"Planets/Barren02.png");
	sistema.addEntity(p2);
	
	r=(float)(Math.random()*(MAX_PLANETS));
	for (int i = 0; i < r; i++) {
		PlanetM p=ItemFactory.createPlanet("Planet "+i+" @ "+sis.getName(), sis);
		sistema.addEntity(p);
		//Galactic.log("Star:"+p);
	}
	return sis;
}*/

public void loadFromProps(Propriedades props){
	super.loadFromProps(props);
	Propriedades thisProps=getProps();
	
	
	Vector<String> properties=PlanetProperties.getStarProps();
	for (String string : properties) {
		thisProps.setProperty(string, props.getPropertyAsObject(string) );
	}
}

public static Star createFromXML(int id, String name, Node node, DataManager dataManager, Sistema world) {
	NamedNodeMap atrs=node.getAttributes();
	Propriedades props=loadNodeIntoProp(node);

	Vector<String> properties=PlanetProperties.getStarProps();

	Star star=new Star(id,dataManager,name,"");

	
	Node nodeFilho=node.getFirstChild();
	while (nodeFilho!=null){
		atrs=nodeFilho.getAttributes();
		if (nodeFilho.getNodeName().equals("starProperties")){
			for (String string : properties) {
				props.setProperty(string, Double.parseDouble(atrs.getNamedItem(string).getNodeValue()));
			}
			
		}
		if (nodeFilho.getNodeName().equals("entities")){
			
			Node nodeP=nodeFilho.getFirstChild();
	        while (nodeP!=null){
	        	atrs=nodeP.getAttributes();
	        	int idP=Integer.parseInt(atrs.getNamedItem(PropriedadesObjeto.PROP_ID.toString()).getNodeValue());
				String typeP=atrs.getNamedItem(PropriedadesObjeto.PROP_TYPE.toString()).getNodeValue();
				String nameP=atrs.getNamedItem(PropriedadesObjeto.PROP_NAME.toString()).getNodeValue();
				if (typeP.equals(EntityType.ET_PLANET)){
	        		PlanetM planet=PlanetM.createFromXML(idP, nameP, nodeP, dataManager, star,world);
	        	}
				
	        	nodeP=nodeP.getNextSibling();
	        	
	        }
			
		}
		nodeFilho=nodeFilho.getNextSibling();
	}
	
	star.setRadius(props.getPropertyAsDouble(PlanetProperties.PP_RADIUS));
	
	world.addEntity(star);
	star.loadFromProps(props);
	star.turn(1);
	star.turn(1);
	return star;
}
}
