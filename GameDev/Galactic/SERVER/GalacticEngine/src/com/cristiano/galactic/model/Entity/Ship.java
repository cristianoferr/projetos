package com.cristiano.galactic.model.Entity;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.Entity.Abstract.AbstractMovableEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.faction.AbstractFaction;
import com.cristiano.galactic.model.wares.Ware;
import com.cristiano.gamelib.propriedades.Propriedades;

public class Ship extends AbstractMovableEntity {

	
	public Ship(int id,DataManager dataManager,Ware shipWare, String name) {
		super(id,dataManager,shipWare, name);
		setType(EntityType.ET_SHIP);
		
		
	}

	public static Propriedades loadNodeIntoProp(Node n) {
		Propriedades props=Item.loadNodeIntoProp(n);
		Node node=n;
		
		NamedNodeMap atrs=node.getAttributes();
		node=node.getFirstChild();
		while (node!=null){
			atrs=node.getAttributes();
			if (node.getNodeName().equals("model")){
				int idShipWare=Integer.parseInt(atrs.getNamedItem("idShipWare").getNodeValue());
				//String shipWare=atrs.getNamedItem("shipWare").getNodeValue();
				props.setProperty("idShipWare",idShipWare);
				//props.setProperty("shipWare",shipWare);
			}
			
			node=node.getNextSibling();
		}

		return props;
	}
	
		public static Ship createFromXML(int id, String name, Node node,
			DataManager dataManager,Sistema sistema) {
		NamedNodeMap atrs=node.getAttributes();
		Propriedades props=loadNodeIntoProp(node);
		id=Integer.parseInt(atrs.getNamedItem("id").getNodeValue());
		AbstractFaction faction=null;
		if (props.propertyExists("owner")){
			faction=dataManager.getFactionManager().getFactionByID(Integer.parseInt(props.getPropertyAsString("owner")));
			
		}
//		type=atrs.getNamedItem("type").getNodeValue();
		name=atrs.getNamedItem("name").getNodeValue();

		Ship s=new Ship(id,dataManager,dataManager.getWareManager().getWareID(props.getPropertyAsInt("idShipWare")),name);
		s.setOwner(faction);
		if (faction!=null){
			faction.addOwnership(s);
		}
		
		sistema.addEntity(s);
		s.loadFromProps(props);
		//s.setTexture(props.getPropertyAsString("texture"));
		return s;
	}
	

}
