package com.cristiano.cyclone.contact;

import java.util.Vector;

import com.cristiano.cyclone.Cyclone;
import com.cristiano.cyclone.collide.fine.CollisionData;
import com.cristiano.cyclone.utils.Vector3;



public class ContactResolver {
	/**
     * The contact resolution routine. One resolver instance
     * can be shared for the whole simulation, as long as you need
     * roughly the same parameters each time (which is normal).
     *
     * @section algorithm Resolution Algorithm
     *
     * The resolver uses an iterative satisfaction algorithm; it loops
     * through each contact and tries to resolve it. Each contact is
     * resolved locally, which may in turn put other contacts in a worse
     * position. The algorithm then revisits other contacts and repeats
     * the process up to a specified iteration limit. It can be proved
     * that given enough iterations, the simulation will get to the
     * correct result. As with all approaches, numerical stability can
     * cause problems that make a correct resolution impossible.
     *
     * @subsection strengths Strengths
     *
     * This algorithm is very fast, much faster than other physics
     * approaches. Even using many more iterations than there are
     * contacts, it will be faster than global approaches.
     *
     * Many global algorithms are unstable under high friction, this
     * approach is very robust indeed for high friction and low
     * restitution values.
     *
     * The algorithm produces visually believable behaviour. Tradeoffs
     * have been made to err on the side of visual doubleism rather than
     * computational expense or numerical accuracy.
     *
     * @subsection weaknesses Weaknesses
     *
     * The algorithm does not cope well with situations with many
     * inter-related contacts: stacked boxes, for example. In this
     * case the simulation may appear to jiggle slightly, which often
     * dislodges a box from the stack, allowing it to collapse.
     *
     * Another issue with the resolution mechanism is that resolving
     * one contact may make another contact move sideways against
     * friction, because each contact is handled independently, this
     * friction is not taken into account. If one object is pushing
     * against another, the pushed object may move across its support
     * without friction, even though friction is set between those bodies.
     *
     * In general this resolver is not suitable for stacks of bodies,
     * but is perfect for handling impact, explosive, and flat resting
     * situations.
     */
	

            /**
             * Holds the number of iterations to perform when resolving
             * velocity.
             */
			private int velocityIterations;

            /**
             * Holds the number of iterations to perform when resolving
             * position.
             */
            private int positionIterations;

            /**
             * To avoid instability velocities smaller
             * than this value are considered to be zero. Too small and the
             * simulation may be unstable, too large and the bodies may
             * interpenetrate visually. A good starting point is the default
             * of 0.01.
             */
            private double velocityEpsilon;

            /**
             * To avoid instability penetrations
             * smaller than this value are considered to be not interpenetrating.
             * Too small and the simulation may be unstable, too large and the
             * bodies may interpenetrate visually. A good starting point is
             * the default of0.01.
             */
            private double positionEpsilon;

            /**
             * Stores the number of velocity iterations used in the
             * last call to resolve contacts.
             */
            private int velocityIterationsUsed;

            /**
             * Stores the number of position iterations used in the
             * last call to resolve contacts.
             */
            private int positionIterationsUsed;


            /**
             * Keeps track of whether the internal settings are valid.
             */
         //   private  boolean validSettings;

            /**
             * Creates a new contact resolver with the given number of iterations
             * per resolution call, and optional epsilon values.
             */
            public ContactResolver(int iterations,
                double velocityEpsilon,
                double positionEpsilon){
            	
            	    setIterations(iterations, iterations);
            	    setEpsilon(velocityEpsilon, positionEpsilon);
            	
            }
            
            public ContactResolver(int iterations){
            	this(iterations,(double)0.01,(double)0.01);
            }
            

            /**
             * Creates a new contact resolver with the given number of iterations
             * for each kind of resolution, and optional epsilon values.
             */
           public ContactResolver(int velocityIterations,
                int positionIterations,
                double velocityEpsilon,
                double positionEpsilon){
        	    setIterations(velocityIterations);
        	    setEpsilon(velocityEpsilon, positionEpsilon);
        	}

            
           public ContactResolver(int velocityIterations,
                    int positionIterations){
        	   this(velocityIterations,
                       positionIterations,(double)0.01,(double)0.01);
           }
            
            

            /**
             * Returns true if the resolver has valid settings and is ready to go.
             */
            boolean isValid()
            {
                return (velocityIterations > 0)&& 
                       (positionIterations > 0) &&
                       (positionEpsilon >= 0.0f) ;
            }

            /**
             * Sets the number of iterations for each resolution stage.
             */
            void setIterations(int velocityIterations,
                               int positionIterations){
                this.velocityIterations = velocityIterations;
                this.positionIterations = positionIterations;
            }

            /**
             * Sets the number of iterations for both resolution stages.
             */
            void setIterations(int iterations){
                setIterations(iterations, iterations);
            }


            /**
             * Sets the tolerance value for both velocity and position.
             */
            void setEpsilon(double velocityEpsilon,
                            double positionEpsilon){
            	this.velocityEpsilon = velocityEpsilon;
            	this.positionEpsilon = positionEpsilon;
            }

            /**
             * Resolves a set of contacts for both penetration and velocity.
             *
             * Contacts that cannot interact with
             * each other should be passed to separate calls to resolveContacts,
             * as the resolution algorithm takes much longer for lots of
             * contacts than it does for the same number of contacts in small
             * sets.
             *
             * @param contactArray Pointer to an array of contact objects.
             *
             * @param numContacts The number of contacts in the array to resolve.
             *
             * @param numIterations The number of iterations through the
             * resolution algorithm. This should be at least the number of
             * contacts (otherwise some raints will not be resolved -
             * although sometimes this is not noticable). If the iterations are
             * not needed they will not be used, so adding more iterations may
             * not make any difference. In some cases you would need millions
             * of iterations. Think about the number of iterations as a bound:
             * if you specify a large number, sometimes the algorithm WILL use
             * it, and you may drop lots of frames.
             *
             * @param duration The duration of the previous integration step.
             * This is used to compensate for forces applied.
             */
            public void resolveContacts(CollisionData cData,
                double duration){
            	Vector<Contact> contacts=cData.getContacts();
                // Make sure we have something to do.
                if (contacts.size()== 0) {
                	return;
                }
                if (!isValid()) {
                	return;
                }

                // Prepare the contacts for processing
                prepareContacts(contacts,  duration);

                // Resolve the interpenetration problems with the contacts.
                adjustPositions(contacts, duration);

                // Resolve the velocity problems with the contacts.
                adjustVelocities(contacts,  duration);
                
                
                for (int i=0;i<contacts.size();i++){
                	Contact contact=contacts.get(i);
                	if (contact.getBody(0).isDestroyOnContact()){
                		contact.getBody(0).setAlive(false);
                	}
                	if (contact.getBody(1).isDestroyOnContact()){
                		contact.getBody(1).setAlive(false);
                	}
                		
                }
          //      System.out.println("Size: "+contacts.size());
              //  contacts.clear();
            }

            /**
             * Sets up contacts ready for processing. This makes sure their
             * internal data is configured correctly and the correct set of bodies
             * is made alive.
             */
            //ok
            void prepareContacts(Vector<Contact> contacts, 
                double duration){
                // Generate contact velocity and axis information.
            	//System.out.println("prepareContacts: size="+contacts.size());
                for (int i=0;i<contacts.size();i++)
                {
                	
                    // Calculate the internal contact data (inertia, basis, etc).
                    contacts.get(i).calculateInternals(duration);
                }
            }

            
            
            private void adjustVelocities(Vector<Contact> c,
                    double duration){
            
            	int numContacts=c.size();
            	 double max = velocityEpsilon;
                 Vector3 velocityChange[]=new Vector3[2];
                 velocityChange[0]=new Vector3();
                 velocityChange[1]=new Vector3();
                 Vector3  rotationChange[]=new Vector3[2];
                 rotationChange[0]=new Vector3();
                 rotationChange[1]=new Vector3();
            	for (int i = 0; i < numContacts; i++){
            		
            		if (Math.abs(c.get(i).getDesiredDeltaVelocity()) > max){
            			
	            		//Cyclone.logDebug("velocityIterationsUsed:"+velocityIterationsUsed+" "+c.get(i).getDesiredDeltaVelocity()+" b0:"+c.get(i).getBody(0).getVelocity().magnitude()+" b1:"+c.get(i).getBody(1).getVelocity().magnitude());
	                    // Match the awake state at the contact
	                    c.get(i).matchAwakeState();
	
	                    // Do the resolution on the contact that came out top.
	                    c.get(i).applyVelocityChange(velocityChange, rotationChange);
            		}
            	}
            }
            
            /**
             * Resolves the velocity issues with the given array of raints,
             * using the given number of iterations.
             * 
             */
            private void adjustVelocitiesOld(Vector<Contact> c,
                double duration){
                Vector3 velocityChange[]=new Vector3[2];
                velocityChange[0]=new Vector3();
                velocityChange[1]=new Vector3();
                Vector3  rotationChange[]=new Vector3[2];
                rotationChange[0]=new Vector3();
                rotationChange[1]=new Vector3();
                Vector3 deltaVel=new Vector3();
                int numContacts=c.size();
                // iteratively handle impacts in order of severity.
                velocityIterationsUsed = 0;
                while (velocityIterationsUsed < c.size()*velocityIterations)
                {
                    // Find contact with maximum magnitude of probable velocity change.
                    double max = velocityEpsilon;
                    int index = numContacts;
                    for (int i = 0; i < numContacts; i++)
                    {
                        if (Math.abs(c.get(i).getDesiredDeltaVelocity()) > max)
                        {
                            max = Math.abs(c.get(i).getDesiredDeltaVelocity());
                            index = i;
                        }
                    }
                    if (index == numContacts) {
                    	break;
                    }
                    Cyclone.logDebug("velocityIterationsUsed:"+velocityIterationsUsed+" "+c.get(index).getDesiredDeltaVelocity()+" b0:"+c.get(index).getBody(0).getVelocity().magnitude()+" b1:"+c.get(index).getBody(1).getVelocity().magnitude());
                    // Match the awake state at the contact
                    c.get(index).matchAwakeState();

                    // Do the resolution on the contact that came out top.
                    c.get(index).applyVelocityChange(velocityChange, rotationChange);

                    // With the change in velocity of the two bodies, the update of
                    // contact velocities means that some of the relative closing
                    // velocities need recomputing.
                    for (int i = 0; i < numContacts; i++)
                    {
                        // Check each body in the contact
                        for (int b = 0; b < 2; b++) {if (c.get(i).getBody(b)!=null)
                        {
                            // Check for a match with each body in the newly
                            // resolved contact
                            for (int d = 0; d < 2; d++)
                            {
                                if (c.get(i).getBody(b) == c.get(index).getBody(d))
                                {
                                    deltaVel = velocityChange[d].getAddVector(rotationChange[d].getVectorProduct(
                                            c.get(i).getRelativeContactPosition(b)));

                                    // The sign of the change is negative if we're dealing
                                    // with the second body in a contact.
                                    c.get(i).getContactVelocity().addVector(
                                    		c.get(i).getContactToWorld().transformTranspose(deltaVel).getMultiVector(b==1?1:-1));
                                    c.get(i).calculateDesiredDeltaVelocity(duration);
                                }
                            }
                        }
                        }
                    }
                    velocityIterationsUsed++;
                }
            }

            /**
             * Resolves the positional issues with the given array of raints,
             * using the given number of iterations.
             */
            //não vi nada errado
            void adjustPositions(Vector<Contact> c,
                double duration){
                int i,index;
                Vector3[] linearChange=new Vector3[2];
                linearChange[0]=new Vector3();
                linearChange[1]=new Vector3();
                Vector3[] angularChange=new Vector3[2];
                angularChange[0]=new Vector3();
                angularChange[1]=new Vector3();
                double max;
                int numContacts=c.size();
                Vector3 deltaPosition;

                // iteratively resolve interpenetrations in order of severity.
                positionIterationsUsed = 0;
                while (positionIterationsUsed < c.size()*positionIterations)
                {
                    // Find biggest penetration
                    max = positionEpsilon;
                    index = numContacts;
                    for (i=0; i<numContacts; i++)
                    {
                        if (c.get(i).getPenetration() > max)
                        {
                            max = c.get(i).getPenetration();
                            index = i;
                        }
                    }
                    if (index == numContacts) {break;}

                    // Match the awake state at the contact
                    c.get(index).matchAwakeState();

                    // Resolve the penetration.
                    c.get(index).applyPositionChange(
                        linearChange,
                        angularChange,
                        max);

                    // Again this action may have changed the penetration of other
                    // bodies, so we update contacts.
                    for (i = 0; i < numContacts; i++)
                    {
                        // Check each body in the contact
                        for (int b = 0; b < 2; b++) if (c.get(i).getBody(b)!=null)
                        {
                            // Check for a match with each body in the newly
                            // resolved contact
                            for (int d = 0; d < 2; d++)
                            {
                                if (c.get(i).getBody(b) == c.get(index).getBody(d))
                                {
                                	//Cyclone.logDebug("c.get:"+c.get(i)+" || "+c.get(i).relativeContactPosition[b]+" || "+linearChange[d]);
                                    deltaPosition = linearChange[d].getAddVector(
                                    		angularChange[d].getVectorProduct(
                                    				c.get(i).getRelativeContactPosition(b)));

                                    // The sign of the change is positive if we're
                                    // dealing with the second body in a contact
                                    // and negative otherwise (because we're
                                    // subtracting the resolution)..
                                    c.get(i).setPenetration(
											c.get(i).getPenetration()
													+ (deltaPosition.scalarProduct(c.get(i).getContactNormal())
													* (b==1?1:-1)));
                                }
                            }
                        }
                    }
                    positionIterationsUsed++;
                }
            }
}
