package com.cristiano.cyclone;

import java.util.HashMap;
import java.util.Vector;

import com.cristiano.cyclone.entities.RigidBody;


public class EntityFactory {
	private HashMap<Class,Vector> garbageCollector;
	

	private Vector<RigidBody> items;
	
	public EntityFactory(){
		items=new Vector<RigidBody>();
		garbageCollector=new HashMap<Class,Vector>();
	}
	
	/*public CollisionPrimitive create(Class c){
		CollisionPrimitive p=getFromGarbage(c);
		if (p==null){
			if (c==RigidBody.class){
				p=new RigidBody();	
			}
			else{
				System.out.println("Classe Invalida:"+c);
				return null;
			}
			
		}
		p.setAwake(true);
		//items.add(p);
		return p;
	}*/
	
	/*public static RigidBody createRigidBody(Geom){
		
		switch (type){
		case BOX:
			return new CollisionBox(body,new Vector3(1,1,1));
		case SPHERE:
			return new CollisionSphere(body,1);
		}
		return null;
	}*/
	/*
	public Geom createBox(Vector3 halfSize){
		Geom p=getFromGarbage(CollisionBox.class);
		if (p==null){
				p=new CollisionBox(new RigidBody(),halfSize);	
		}
		p.body.setAwake(true);
		//items.add(p);
		return p;
	}
	
	public Geom createSphere(float radius){
		Geom p=getFromGarbage(CollisionSphere.class);
		if (p==null){
				p=new CollisionSphere(new RigidBody(),radius);	
		}
		p.body.setAwake(true);
		//items.add(p);
		return p;
	}
	*/
/*
	public RigidBody getFromGarbage(Class c){
		RigidBody p=null;
		if (garbageCollector.containsKey(c)){
			Vector<RigidBody> v=new Vector<RigidBody>();
			if (v.size()>0){
				p=v.elementAt(0);
				removeFromGarbage(p);
			}
		}
		return p;
	}
	*//*
	public void add2Garbage(RigidBody RigidBody){
		if (!garbageCollector.containsKey(RigidBody.getClass())){
			Vector<RigidBody> v=new Vector<RigidBody>();
			v.add(RigidBody);
		}else {
			garbageCollector.get(RigidBody.getClass()).add(RigidBody);
		}
	}*/
	/*
	public void remove(RigidBody RigidBody){
		System.out.println("entFact.remove()"+RigidBody+" "+RigidBody.isAlive() );
		items.remove(RigidBody);
	//	add2Garbage(RigidBody);
	}
	*/
	
	/*
	public void removeFromGarbage(RigidBody cp){
		if (!garbageCollector.containsKey(cp.getClass())){
		}else {
			garbageCollector.get(cp.getClass()).remove(cp);
		}
	}*/
	public Vector<RigidBody> getItems() {
		return items;
	}
}
