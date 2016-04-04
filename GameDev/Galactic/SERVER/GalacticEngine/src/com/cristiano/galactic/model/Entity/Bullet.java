package com.cristiano.galactic.model.Entity;

import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.cyclone.entities.geom.GeomBox;
import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.EntityType;


public class Bullet extends SphereItem {
	private Item owner;
	
	
	//static Geom geom=new GeomBox(new Vector3(.1,.1,.1));
	private static Geom geom=new GeomBox(new Vector3(2,2,50));
	
	Bullet(DataManager dataManager, Geom geom, String name,
			String texture,Item owner) {
		super(getNextID(),dataManager, geom, name, texture);
		setShipWare(dataManager.getWareManager().getWare(Consts.BASIC_AMMO));
		this.owner=owner;
		
	}

	
	public static Bullet createBullet(int ttl,Item owner,Vector3 relVel,Vector3 relPos){
		Bullet b=new Bullet(owner.getDataManager(),geom,"bullet",null,owner);
		b.setType(EntityType.ET_BULLET);
		owner.getWorld().addEntity(b);
		//b.getBody().setOwner(owner.getBody());
		b.getBody().setMass(Math.pow(10,4));
		//b.getBody().setMass(10000);
		b.getBody().setDestroyOnContact(true);
		b.getBody().setPosition(relPos);
		
		//System.out.println("relPos:"+relPos+" POsition:"+b.getBody().getPosition());
		//System.out.println("relVel:"+relVel+" "+owner.getVelocity().getAddVector(relVel));
		b.setVelocity(owner.getVelocity().getAddVector(relVel));
		b.getBody().setTTL(ttl);
		b.getBody().setOwner(owner.getBody());
		Quaternion q=new Quaternion(owner.getBody().getOrientation());
		q.rotateByVector(new Vector3(-90,0,90));
		b.getBody().setOrientation(q);
		b.getBody().setDestroyOnContact(true);
		return b;
	}
/*
	public void init(int TTL,Vector3 pos,Vector3 velocity) {
		getBody().setMass(Math.pow(10, 3));
		getBody().setTTL(TTL);
		setVelocity(owner.getVelocity().getAddVector(velocity));
		getBody().setPosition(owner.getBody().getPosition().getAddVector(pos));

		
	}*/
}
