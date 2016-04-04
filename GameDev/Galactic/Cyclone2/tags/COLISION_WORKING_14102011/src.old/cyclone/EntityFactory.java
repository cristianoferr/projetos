package cyclone;

import java.util.HashMap;
import java.util.Vector;

import cyclone.entities.RigidBody;

public class EntityFactory {
	HashMap<Class,Vector> garbageCollector;
	

	Vector<RigidBody> items;
	
	public EntityFactory(){
		items=new Vector<RigidBody>();
		garbageCollector=new HashMap<Class,Vector>();
	}
	
	public RigidBody create(Class c){
		RigidBody p=getFromGarbage(c);
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
	}

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
	
	public void add2Garbage(RigidBody RigidBody){
		if (!garbageCollector.containsKey(RigidBody.getClass())){
			Vector<RigidBody> v=new Vector<RigidBody>();
			v.add(RigidBody);
		}else {
			garbageCollector.get(RigidBody.getClass()).add(RigidBody);
		}
	}
	
	public void remove(RigidBody RigidBody){
	//	System.out.println("entFact.remove()"+RigidBody+" "+RigidBody.isAlive() );
		items.remove(RigidBody);
		add2Garbage(RigidBody);
	}
	
	
	
	public void removeFromGarbage(RigidBody RigidBody){
		if (!garbageCollector.containsKey(RigidBody.getClass())){
		}else {
			garbageCollector.get(RigidBody.getClass()).remove(RigidBody);
		}
	}
	public Vector<RigidBody> getItems() {
		return items;
	}
}
