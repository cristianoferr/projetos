package cyclone.contact;

import java.util.Vector;

import cyclone.math.Vector3;

public class ContactResolver {
    /**
     * The contact resolution routine for particle contacts. One
     * resolver instance can be shared for the whole simulation.
     */
        /**
         * Holds the number of iterations allowed.
         */
        int iterations;

        /**
         * This is a performance tracking value - we keep a record
         * of the actual number of iterations used.
         */
        int iterationsUsed;

    public ContactResolver(int iterations){
    	this.iterations=iterations;
    }
    	
        /**
         * Creates a new contact resolver.
         */
        

        /**
         * Sets the number of iterations that can be used.
         */
       public void setIterations(int iterations){
    	   this.iterations=iterations;
       }

        /**
         * Resolves a set of particle contacts for both penetration
         * and velocity.
         *
         * Contacts that cannot interact with each other should be
         * passed to separate calls to resolveContacts, as the
         * resolution algorithm takes much longer for lots of contacts
         * than it does for the same number of contacts in small sets.
         *
         * @param contactArray Pointer to an array of particle contact
         * objects.
         *
         * @param numContacts The number of contacts in the array to
         * resolve.
         *
         * @param numIterations The number of iterations through the
         * resolution algorithm. This should be at least the number of
         * contacts (otherwise some constraints will not be resolved -
         * although sometimes this is not noticable). If the
         * iterations are not needed they will not be used, so adding
         * more iterations may not make any difference. But in some
         * cases you would need millions of iterations. Think about
         * the number of iterations as a bound: if you specify a large
         * number, sometimes the algorithm WILL use it, and you may
         * drop frames.
         *
         * @param duration The duration of the previous integration step.
         * This is used to compensate for forces applied.
        */
      public  void resolveContacts(Vector<Contact> contactArray,
            double duration){
    	   int i;

    	    iterationsUsed = 0;
    	    while(iterationsUsed < iterations)
    	    {
    	        // Find the contact with the largest closing velocity;
    	        double max = Double.MAX_VALUE;
    	        int maxIndex = contactArray.size();
    	        for (i = 0; i < contactArray.size(); i++)
    	        {
    	            double sepVel = contactArray.elementAt(i).calculateSeparatingVelocity();
    	            if (sepVel < max &&
    	                (sepVel < 0 || contactArray.elementAt(i).penetration > 0))
    	            {
    	                max = sepVel;
    	                maxIndex = i;
    	            }
    	        }

    	        // Do we have anything worth resolving?
    	        if (maxIndex >= contactArray.size()) break;

    	        // Resolve this contact
    	        contactArray.elementAt(maxIndex).resolve(duration);

    	        // Update the interpenetrations for all particles
    	        Vector3 move[] = contactArray.elementAt(maxIndex).particleMovement;
    	        for (i = 0; i < contactArray.size(); i++)
    	        {
    	            if (contactArray.elementAt(i).body[0] == contactArray.elementAt(maxIndex).body[0])
    	            {
    	                contactArray.elementAt(i).penetration -= move[0].scalarProduct(contactArray.elementAt(i).contactNormal);
    	            }
    	            else if (contactArray.elementAt(i).body[0] == contactArray.elementAt(maxIndex).body[1])
    	            {
    	                contactArray.elementAt(i).penetration -= move[1].scalarProduct(contactArray.elementAt(i).contactNormal);
    	            }
    	            if (contactArray.elementAt(i).body[1]!=null)
    	            {
    	                if (contactArray.elementAt(i).body[1] == contactArray.elementAt(maxIndex).body[0])
    	                {
    	                    contactArray.elementAt(i).penetration += move[0].scalarProduct(contactArray.elementAt(i).contactNormal);
    	                }
    	                else if (contactArray.elementAt(i).body[1] == contactArray.elementAt(maxIndex).body[1])
    	                {
    	                    contactArray.elementAt(i).penetration += move[1].scalarProduct(contactArray.elementAt(i).contactNormal);
    	                }
    	            }
    	        }

    	        iterationsUsed++;	  
    }
 }
		/**
		 * @return the iterationsUsed
		 */
		public int getIterationsUsed() {
			return iterationsUsed;
		}

		/**
		 * @param iterationsUsed the iterationsUsed to set
		 */
		public void setIterationsUsed(int iterationsUsed) {
			this.iterationsUsed = iterationsUsed;
		}

		/**
		 * @return the iterations
		 */
		public int getIterations() {
			return iterations;
		}
}
