package cyclone;

import java.util.Date;
import java.util.Vector;

import cyclone.entities.EntityFactory;
import cyclone.entities.Particle;
import cyclone.entities.RigidBody;
import cyclone.entities.RigidBody2;
import cyclone.math.Vector3;
import cyclone.particle.ParticleContact;
import cyclone.particle.ParticleContactResolver;
import cyclone.particle.ParticleForceRegistry;
import cyclone.particle.contactGenerator.GroundContacts;
import cyclone.particle.contactGenerator.ParticleContactGenerator;
import cyclone.particle.forceGenerator.ParticleBuoyancy;
import cyclone.particle.forceGenerator.ParticleDrag;
import cyclone.particle.forceGenerator.ParticleGravity;
import cyclone.rigidBody.ForceRegistry;

public class World {
	
EntityFactory entFac;
private Date lastUpdate=new Date();
public ParticleForceRegistry registry;
public ForceRegistry registry2;

ParticleGravity gravity;
ParticleBuoyancy buoyancy;
ParticleDrag drag;

boolean calculateIterations;

int iterations=1;

/**
 * Holds the maximum number of contacts allowed (i.e. the
 * size of the contacts array).
 */
int maxContacts;

/**
 * Holds the resolver for contacts.
 */
ParticleContactResolver resolver;


/**
 * Contact generators.
 */
private Vector<ParticleContactGenerator> contactGenerators=new Vector<ParticleContactGenerator>();


/**
 * Holds the list of contacts.
 */
Vector<ParticleContact> contacts=new Vector<ParticleContact>();

GroundContacts groundContacts;

public World(){
	entFac=new EntityFactory();
	registry=new ParticleForceRegistry();
	registry2=new ForceRegistry();
	
	gravity=new ParticleGravity(new Vector3(0.0f, -10f, 0.0f));
	buoyancy=new ParticleBuoyancy(-30,40.0f, 3.0f, 0.6f);
	drag=new ParticleDrag(0.05,0.05);
	
	resolver=new ParticleContactResolver(iterations);
	maxContacts=500;
	
	calculateIterations = !(iterations == 0);
	//contacts = new ParticleContact[maxContacts];
	
	groundContacts=new GroundContacts(getItems());
	getContactGenerators().add(groundContacts);
}


public Particle createParticle(Class c){
	Particle p=entFac.createParticle(c);
	registry.remove(p);
	
	registerBody(p);
	registry.add(p, gravity);
	//registry.add(p, buoyancy);
	
	//pfr.add(p, drag);
	
	return p;
}

public void registerBody2(RigidBody2 body){
	//registry2.add(body, gravity);
}

public void registerBody(RigidBody body){
	registry.add(body, gravity);
}
public void registerBody(Particle body){
	registry.add(body, gravity);
}

public ParticleForceRegistry getPfr() {
	return registry;
}


	public static void main(String[] args) {
		Vector3 v=new Vector3(10,20,30);
		v.normalise();
		
		System.out.println("V:"+v+" mag:"+v.magnitude());
//		System.out.println("t:"+ShotType.ARTILLERY.ordinal());
		World c=new World();
	}
	


	
	public void update(){
		Date now=new Date();
		int time=(int)(now.getTime()-lastUpdate.getTime());
		if (time<=0) time=1;
		lastUpdate=now;

       // Find the duration of the last frame in seconds
       //float duration = (float)TimingData::get().lastFrameDuration * 0.001f;
       //if (duration <= 0.0f) return;

       // Update the physics of each particle in turn
		float duration=(float)time/1000;
		update(duration);
	}
	
	
	public int generateContacts()	{
	    int limit = maxContacts;
	    //ParticleContact nextContact[] = contacts;

	    
	    for (int i=0;i<getContactGenerators().size();i++)
	    {
	    	ParticleContactGenerator g=getContactGenerators().elementAt(i);
	    	int used =g.addContact(contacts, limit);
	        limit -= used;
	        
	        //nextContact += used;

	        // We've run out of contacts to fill. This means we're missing
	        // contacts.
	        if (limit <= 0) break;
	    }

	    // Return the number of contacts used.
	    return maxContacts - limit;
	}
	
	public void update(float duration){
		if (duration>10) duration=10;
		// First apply the force generators
		registry.updateForces(duration);
		registry2.updateForces(duration);
		
		//System.out.println("update:"+getItems().size());
       for (int i=0;i<getItems().size();i++){
    	   Particle p=getItem(i);
    	   p.integrate(duration);
    	   if (!p.isAlive()){
    		   entFac.remove(p);
    	   }
              
       }
       
    // Generate contacts
       
       int usedContacts = generateContacts();
     //  System.out.println("contacts:"+contacts.size()+" usedC:"+usedContacts);

       // And process them
       if (contacts.size()>0)  {
           if (calculateIterations) resolver.setIterations(usedContacts * 2);
          resolver.resolveContacts(contacts, duration);
       } 
       
       contacts.clear();
   }
	
	public Vector<Particle> getItems(){
		return entFac.getItems();
	}

	public Particle getItem(int i){
		return getItems().elementAt(i);
	}


	public void setContactGenerators(Vector<ParticleContactGenerator> contactGenerators) {
		this.contactGenerators = contactGenerators;
	}


	public Vector<ParticleContactGenerator> getContactGenerators() {
		return contactGenerators;
	}
}
