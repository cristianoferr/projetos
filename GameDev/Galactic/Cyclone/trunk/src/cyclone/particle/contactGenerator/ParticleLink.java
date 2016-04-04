package cyclone.particle.contactGenerator;

import java.util.Vector;

import cyclone.entities.Particle;
import cyclone.math.Vector3;
import cyclone.particle.ParticleContact;

public class ParticleLink implements ParticleContactGenerator{
	
	 
    /**
     * Holds the pair of particles that are connected by this link.
     */
	public Particle particle[];

	    
	        /**
	         * Returns the current length of the link.
	         */
	 double currentLength(){
	  Vector3 relativePos = particle[0].getPosition().getSubVector(particle[1].getPosition());
	   	return relativePos.magnitude();
 
	 }

	 public ParticleLink(Particle p1,Particle p2){
		 particle=new Particle[2];
		 particle[0]=p1;
		 particle[1]=p2;
	 }
	        /**
	         * Geneates the contacts to keep this link from being
	         * violated. This class can only ever generate a single
	         * contact, so the pointer can be a pointer to a single
	         * element, the limit parameter is assumed to be at least one
	         * (zero isn't valid) and the return value is either 0, if the
	         * cable wasn't over-extended, or one if a contact was needed.
	         *
	         * NB: This method is declared in the same way (as pure
	         * virtual) in the parent class, but is replicated here for
	         * documentation purposes.
	         */
	 public int addContact(Vector<ParticleContact> contact, int limit){
		return 0; 
	 }

}
