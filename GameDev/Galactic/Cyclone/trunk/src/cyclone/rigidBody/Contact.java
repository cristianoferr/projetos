package cyclone.rigidBody;

import cyclone.entities.RigidBody;
import cyclone.math.Vector3;
import cyclone.particle.ParticleContact;

public class Contact extends ParticleContact {
	
	  /**
     * Holds the lateral friction coefficient at the contact.
     */
    double friction;
    
    /**
     * Holds the direction of the contact in world coordinates.
     */
    Vector3 contactNormal;
    
    /**
     * Sets the data that doesn't normally depend on the position
     * of the contact (i.e. the bodies, and their material properties).
     */
    void setBodyData(RigidBody one, RigidBody two,
                     double friction, double restitution){
    	particle[0] = one;
    	particle[1] = two;
        this.friction = friction;
        this.restitution = restitution;
    }

    
    
public Vector3 contactPoint=new Vector3();
	public Contact(RigidBody p1) {
		super(p1);
	}

	public Contact(RigidBody p1,RigidBody p2) {
		super(p1,p2);
	}
}
