package cyclone;

import java.util.Date;
import java.util.Vector;

import cristiano.math.PhysicsConsts;
import cristiano.math.Vector3;
import cyclone.collide.fine.CollisionData;
import cyclone.collide.fine.CollisionDetector;
import cyclone.contact.ContactResolver;
import cyclone.contactGenerator.ContactGenerator;
import cyclone.contactGenerator.GroundContacts;
import cyclone.entities.RigidBody;
import cyclone.entities.geom.CollisionPlane;
import cyclone.entities.geom.Geom;
import cyclone.entities.geom.GeomBox;
import cyclone.entities.geom.GeomSphere;
import cyclone.forceGenerator.Gravity;
import cyclone.forceRegistry.ForceRegistry;

public class Cyclone {
	
EntityFactory entFac;
private Date lastUpdate=new Date();
public ForceRegistry registry;
boolean useGravity=false;
boolean useFloor=false;

public static final float MAX_UPDATE_TIME=0.05f;


Gravity gravity;
//RigidBodyBuoyancy buoyancy;
//RigidBodyDrag drag;

boolean calculateIterations;

int iterations=4;

/**
 * Holds the maximum number of contacts allowed (i.e. the
 * size of the contacts array).
 */
int maxContacts;

/**
 * Holds the resolver for contacts.
 */
ContactResolver resolver;

/** Holds the collision data structure for collision detection. */
CollisionData cData;

public EntityFactory getEntFac() {
	return entFac;
}

public CollisionData getcData() {
	return cData;
}


/**
 * Contact generators.
 */
private Vector<ContactGenerator> contactGenerators=new Vector<ContactGenerator>();


/**
 * Holds the list of contacts.
 */

GroundContacts groundContacts;

public Cyclone(){
	entFac=new EntityFactory();
	registry=new ForceRegistry();
	cData=new CollisionData();
	
	gravity=new Gravity(new Vector3(0.0f, -10f, 0.0f));
	//buoyancy=new Buoyancy(-30,40.0f, 3.0f, 0.6f);
	//drag=new Drag(0.05,0.05);
	
	resolver=new ContactResolver(iterations);
	maxContacts=500;
	
	calculateIterations = !(iterations == 0);
	//contacts = new RigidBodyContact[maxContacts];
	
	groundContacts=new GroundContacts(getItems());
	//getContactGenerators().add(groundContacts);
}

/*
public RigidBody createRigidBody(Class c){
	RigidBody p=entFac.create(c);
	registry.remove(p);
	
	registerBody(p);
	//registry.add(p, gravity);
	//registry.add(p, buoyancy);
	
	//pfr.add(p, drag);
	
	return p;
}*/


public void registerBody(RigidBody cp){
	if (useGravity)
		registry.add(cp, gravity);
	
	getItems().add(cp);
	//XithViewer.printLog("RegisterBody:"+cp,true);
}

public ForceRegistry getPfr() {
	return registry;
}


	public static void main(String[] args) {
//		System.out.println("t:"+ShotType.ARTILLERY.ordinal());
		Cyclone c=new Cyclone();
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
	    	int used =g.addContact(cData.getContacts(), limit);
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
	//	System.out.println("cyclone.update() duration:"+duration);
		if (duration<=0) return;
		if (duration>MAX_UPDATE_TIME) duration=MAX_UPDATE_TIME;
		
		//temp
		duration=0.05f;
		
		// First apply the force generators
		registry.updateForces(duration);
		
		CollisionPlane plane=null;
		if (useFloor){
			plane=new CollisionPlane();
		    plane.direction = new Vector3(0,1,0);
		    plane.offset = 0;
		}
	    
	    // Set up the collision data structure
	    cData.reset(maxContacts);
	    cData.setFriction(0.9);
	    cData.setRestitution(0.6);
	    cData.setTolerance(0.1);
	   
	    for (int i=0;i<getItems().size();i++)
	    	   if (!getItem(i).isAlive()) getItems().remove(i);
        
		//System.out.println("update:"+getItems().size());
       for (int i=0;i<getItems().size();i++){
    	   Geom p=getItem(i).getGeom();
    	 
    	   
    	   fixRotation(i);
    	   
    	   getItem(i).integrate(duration);
    	   p.calculateInternals();
    	   
    	 // System.out.println(getItem(i)+" pos:"+getItem(i).getPosition());
    	   
    	   if (useFloor){
    		   if (p.type==Geom.PrimitiveType.BOX)
    		     CollisionDetector.boxAndHalfSpace((GeomBox)p, plane, cData);
    		   if (p.type==Geom.PrimitiveType.SPHERE)
      		     CollisionDetector.sphereAndHalfSpace((GeomSphere)p, plane, cData);
    	   }
    	
    	//   System.out.println("i:"+i+" pos:"+getItem(i).getPosition()+" contacts:"+cData.getContacts().size());
       }
       
       for (int i=0;i<getItems().size();i++){
    	   verifyCollision(getItem(i),i);
       }  
    	   
    	//   System.out.println(i+"= pos:"+p.body.getPosition());
    	 //  if (!p.isAlive()){
    	//	   entFac.remove(p);
    	//   }
              
      
    // Generate contacts
       
     generateContacts();
     //  System.out.println("contacts:"+contacts.size()+" usedC:"+usedContacts);

       // Resolve detected contacts
       resolver.resolveContacts(
           cData.getContacts(),
           duration
           );
    
    
   }

	private void fixRotation(int i) {
		if (getItem(i).getRotation().magnitude()>.5) {
		//	System.out.println("fixRotation:"+getItem(i)+" "+getItem(i).getRotation().magnitude());
    		   getItem(i).getRotation().multiVectorScalar(0.99);
    	   }
	}
	
	
	private void applyGravity(RigidBody b1,RigidBody b2){
		Vector3 dif=b1.getPosition().getSubVector(b2.getPosition());
		Vector3 dif2=b2.getPosition().getSubVector(b1.getPosition());
		//PhysicsConsts.Gforce
		double f=PhysicsConsts.Gforce* (b1.getMass()*b2.getMass())/Math.pow(dif.magnitude(),2);
		dif.normalise();
		dif2.normalise();
		b2.addForce(dif.getMultiVector(f));
		b1.addForce(dif2.getMultiVector(f));
		
	}


	/*
	 * Essa função verifica a colisão entre o objeto atual e os demais objetos.
	 * Também aplica a gravidade entre os objetos.
	 */
	public void verifyCollision(RigidBody b1,int ini){
		// Check for collisions with each shot
		Geom g1=b1.getGeom();
		 for (int i=ini+1;i<getItems().size();i++){
               // if (!cData.hasMoreContacts()) return;
			 Geom g2=getItem(i).getGeom();
			 
			 if ((b1.checkIfEmitsGravity()) && (getItem(i).checkIfEmitsGravity()))
				 applyGravity(b1,getItem(i));
			 
			 
                // When we get a collision, remove the shot
			 if (g1.type==Geom.PrimitiveType.BOX){
				 if (g2.type==Geom.PrimitiveType.BOX){
					 CollisionDetector.boxAndBox((GeomBox)g1,(GeomBox)g2, cData);
				 } else if (g2.type==Geom.PrimitiveType.SPHERE){
					 CollisionDetector.boxAndSphere((GeomBox)g1,(GeomSphere)g2, cData);
					 
				 }
			 } else if (g1.type==Geom.PrimitiveType.SPHERE){
				 if (g2.type==Geom.PrimitiveType.BOX){
					 CollisionDetector.boxAndSphere((GeomBox)g2,(GeomSphere)g1, cData);
				 } else if (g2.type==Geom.PrimitiveType.SPHERE){
					 CollisionDetector.sphereAndSphere((GeomSphere)g2,(GeomSphere)g1, cData);
				 }
			 }
			 
           
        }
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
	
	public boolean isUseGravity() {
		return useGravity;
	}

	public void setUseGravity(boolean useGravity) {
		this.useGravity = useGravity;
	}

	public boolean isUseFloor() {
		return useFloor;
	}

	public void setUseFloor(boolean useFloor) {
		this.useFloor = useFloor;
	}
}
