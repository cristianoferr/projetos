package cyclone.particle.contactGenerator;

import java.util.Vector;

import cyclone.entities.Particle;
import cyclone.particle.ParticleContact;

public interface ParticleContactGenerator {
	
	    
	        /**
	         * Fills the given contact structure with the generated
	         * contact. The contact pointer should point to the first
	         * available contact in a contact array, where limit is the
	         * maximum number of contacts in the array that can be written
	         * to. The method returns the number of contacts that have
	         * been written.
	         */
	
	
		public int addContact(Vector<ParticleContact> contact, int limit);

}
