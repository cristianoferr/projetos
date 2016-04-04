package cyclone.entities;

import java.util.HashMap;
import java.util.Vector;

public class EntityFactory {
	HashMap<Class,Vector> garbageCollector;
	

	Vector<Particle> items;
	
	public EntityFactory(){
		items=new Vector<Particle>();
		garbageCollector=new HashMap<Class,Vector>();
	}
	
	public Particle createParticle(Class c){
		Particle p=getFromGarbage(c);
		if (p==null){
			if (c==Particle.class){
				p=new Particle();	
			}
			if (c==RigidBody.class){
				p=new RigidBody();	
			}else{
				System.out.println("Classe Invalida:"+c);
				return null;
			}
			
		}
		p.setAlive(true);
		items.add(p);
		return p;
	}

	public Particle getFromGarbage(Class c){
		Particle p=null;
		if (garbageCollector.containsKey(c)){
			Vector<Particle> v=new Vector<Particle>();
			if (v.size()>0){
				p=v.elementAt(0);
				removeFromGarbage(p);
			}
		}
		return p;
	}
	
	public void add2Garbage(Particle particle){
		if (!garbageCollector.containsKey(particle.getClass())){
			Vector<Particle> v=new Vector<Particle>();
			v.add(particle);
		}else {
			garbageCollector.get(particle.getClass()).add(particle);
		}
	}
	
	public void remove(Particle particle){
	//	System.out.println("entFact.remove()"+particle+" "+particle.isAlive() );
		items.remove(particle);
		add2Garbage(particle);
	}
	
	
	
	public void removeFromGarbage(Particle particle){
		if (!garbageCollector.containsKey(particle.getClass())){
		}else {
			garbageCollector.get(particle.getClass()).remove(particle);
		}
	}
	public Vector<Particle> getItems() {
		return items;
	}
}
