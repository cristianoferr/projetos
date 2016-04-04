package cyclone.particle.contactGenerator;

import java.util.Vector;

import cyclone.entities.Particle;
import cyclone.math.Vector3;
import cyclone.particle.ParticleContact;

public class ParticleCable extends ParticleLink {
        /**
         * Holds the maximum length of the cable.
         */
    public double maxLength;

        /**
         * Holds the restitution (bounciness) of the cable.
         */
    public double restitution;	
	
    public ParticleCable(Particle p1,Particle p2,double maxLength,double restitution){
    	super(p1,p2);
    	this.maxLength=maxLength;
    	this.restitution=restitution;
    }
    
	public int addContact(Vector<ParticleContact> contact, int limit){
	    // Find the length of the cable
	    double length = currentLength();

	    // Check if we're over-extended
	    if (length < maxLength)
	    {
	        return 0;
	    }

	    ParticleContact c=new ParticleContact(particle[0],particle[1]);
	//	System.out.println("ParticleCable: 0:");//+particle[0].getVelocity()+" 1:"+particle[1].getVelocity());
	    
	    // Otherwise return the contact
		//c.particle[0] = particle[0];
		//c.particle[1] = particle[1];

	    // Calculate the normal
	    Vector3 normal = particle[1].getPosition().getSubVector(particle[0].getPosition());
	    normal.normalise();
	    c.contactNormal = normal;

	    c.penetration = (length-maxLength);
	    c.restitution = restitution;

		contact.add(c);

	    
	    return 1; 
	 }
}
