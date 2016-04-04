package com.cristiano.galactic.controller.handlers;

import com.cristiano.cyclone.entities.RigidBody;
import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.galactic.model.Entity.Abstract.Item;


/*
 * This is a wrapper for the RigidBody class
 * */
public class PhysicsItem extends RigidBody {

/*	public PhysicsItem(Geom geom, double mass) {
		super(geom, mass);
	}*/

	public PhysicsItem(Geom geom) {
		super(geom);
	}
public Item getItem(){
	return Item.getItem(this);
}




}
