package cyclone;

import java.util.Date;
import java.util.Vector;

import cyclone.contact.Contact;
import cyclone.contact.ContactResolver;
import cyclone.contactGenerator.ContactGenerator;
import cyclone.contactGenerator.GroundContacts;
import cyclone.entities.RigidBody;
import cyclone.forceGenerator.Gravity;
import cyclone.forceRegistry.ForceRegistry;
import cyclone.math.Vector3;

public class World {
	
EntityFactory entFac;
private Date lastUpdate=new Date();
public ForceRegistry registry;

Gravity gravity;
//RigidBodyBuoyancy buoyancy;
//RigidBodyDrag drag;

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
ContactResolver resolver;


/**
 * Contact generators.
 */
private Vector<ContactGenerator> contactGenerators=new Vector<ContactGenerator>();


/**
 * Holds the list of contacts.
 */
Vector<Contact> contacts=new Vector<Contact>();

GroundContacts groundContacts;

public World(){
	entFac=new EntityFactory();
	registry=new ForceRegistry();
	
	gravity=new Gravity(new Vector3(0.0f, -10f, 0.0f));
	//buoyancy=new Buoyancy(-30,40.0f, 3.0f, 0.6f);
	//drag=new Drag(0.05,0.05);
	
	resolver=new ContactResolver(iterations);
	maxContacts=500;
	
	calculateIterations = !(iterations == 0);
	//contacts = new RigidBodyContact[maxContacts];
	
	groundContacts=new GroundContacts(getItems());
	getContactGenerators().add(groundContacts);
}


public RigidBody createRigidBody(Class c){
	RigidBody p=entFac.create(c);
	registry.remove(p);
	
	registerBody(p);
	registry.add(p, gravity);
	//registry.add(p, buoyancy);
	
	//pfr.add(p, drag);
	
	return p;
}


public void registerBody(RigidBody body){
	registry.add(body, gravity);
	getItems().add(body);
}

public ForceRegistry getPfr() {
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

       // Update the physics of each RigidBody in turn
		float duration=(float)time/1000;
		update(duration);
	}
	
	
	public int generateContacts()	{
	    int limit = maxContacts;
	    //RigidBodyContact nextContact[] = contacts;

	    
	    for (int i=0;i<getContactGenerators().size();i++)
	    {
	    	ContactGenerator g=getContactGenerators().elementAt(i);
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
		
		//System.out.println("update:"+getItems().size());
       for (int i=0;i<getItems().size();i++){
    	   RigidBody p=getItem(i);
    	   p.integrate(duration);
    	//   System.out.println(i+"= pos:"+p.getPosition());
    	 //  if (!p.isAlive()){
    	//	   entFac.remove(p);
    	//   }
              
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
	
	public Vector<RigidBody> getItems(){
		return entFac.getItems();
	}

	public RigidBody getItem(int i){
		return getItems().elementAt(i);
	}


	public void setContactGenerators(Vector<ContactGenerator> contactGenerators) {
		this.contactGenerators = contactGenerators;
	}


	public Vector<ContactGenerator> getContactGenerators() {
		return contactGenerators;
	}
}
