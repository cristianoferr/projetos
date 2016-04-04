package com.cristiano.galactic.Utils;

import com.cristiano.cyclone.utils.ObjetoBasico;
import com.cristiano.galactic.controller.handlers.PhysicsItem;
import com.cristiano.galactic.model.Entity.Abstract.Item;


public class ItemFactory extends ObjetoBasico {

	/*public static Star createStar(String name,Sistema sistema){
		return Star.createRandom(name,sistema);
	}
	public static PlanetM createPlanet(String name,Star s){
		return PlanetM.createRandom(name,s);
	}*/
	
	
	public static PhysicsItem createPhysicsItem(Item item){
		PhysicsItem body = new PhysicsItem(item.getGeom());
		body.setPosition(item.getCoord().getX(), item.getCoord().getY(), item.getCoord().getZ());
		item.getGeom().calculateInternals(body);
		item.setBody(body);
		body.setName(item.getName());
		body.setMass(item.getMass());
		return body;
	}
	
	
}
