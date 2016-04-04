package cyclone.particle;

import cyclone.entities.Particle;
import cyclone.math.Vector3;

public class ParticleContact {

	
    /**
     * A Contact represents two objects in contact (in this case
     * ParticleContact representing two Particles). Resolving a
     * contact removes their interpenetration, and applies sufficient
     * impulse to keep them apart. Colliding bodies may also rebound.
     *
     * The contact has no callable functions, it just holds the
     * contact details. To resolve a set of contacts, use the particle
     * contact resolver class.
     */
        // ... Other ParticleContact code as before ...


        /**
         * The contact resolver object needs access into the contacts to
         * set and effect the contact.
         */
        //friend ParticleContactResolver;


    public Particle particle[];
        /**
         * Holds the particles that are involved in the contact. The
         * second of these can be NULL, for contacts with the scenery.
         */
        

        /**
         * Holds the normal restitution coefficient at the contact.
         */
        public double restitution;

        /**
         * Holds the direction of the contact in world coordinates.
         */
        public Vector3 contactNormal;

        /**
         * Holds the depth of penetration at the contact.
         */
        public double penetration;

        /**
         * Holds the amount each particle is moved by during interpenetration
         * resolution.
         */
        Vector3 particleMovement[];

        public ParticleContact(Particle p1,Particle p2){
        	particle=new Particle[2];
        	particle[0]=p1;
        	particle[1]=p2;
        	particleMovement=new Vector3[2];
        	particleMovement[0]=new Vector3(0,0,0);
        	particleMovement[1]=new Vector3(0,0,0);
        }
        
        public ParticleContact(Particle p1){
        	particle=new Particle[2];
        	particle[0]=p1;
        	particle[1]=null;
        }
        
        /**
         * Resolves this contact, for both velocity and interpenetration.
         */
        void resolve(double duration){
           resolveVelocity(duration);
            resolveInterpenetration(duration);

        }
        
        
        

        /**
         * Calculates the separating velocity at this contact.
         */
        double calculateSeparatingVelocity(){
            Vector3 relativeVelocity = new Vector3(particle[0].getVelocity());
            if (particle[1]!=null) relativeVelocity.subVector(particle[1].getVelocity());
            return relativeVelocity.scalarProduct(contactNormal);
        }

    
        /**
         * Handles the impulse calculations for this collision.
         */
        private void resolveVelocity(double duration){
        	
            // Find the velocity in the direction of the contact
            double separatingVelocity = calculateSeparatingVelocity();

            // Check if it needs to be resolved
            if (separatingVelocity >= 0)
            {
                // The contact is either separating, or stationary - there's
                // no impulse required.
                return;
            }

          /*  // Calculate the new separating velocity
            double newSepVelocity = -separatingVelocity * restitution;

            // Check the velocity build-up due to acceleration only
            Vector3 accCausedVelocity = particle[0].getAcceleration();
            if (particle[1]!=null) accCausedVelocity -= particle[1].getAcceleration();
            double accCausedSepVelocity = accCausedVelocity * contactNormal * duration;

            // If we've got a closing velocity due to acelleration build-up,
            // remove it from the new separating velocity
            if (accCausedSepVelocity < 0)
            {
                newSepVelocity += restitution * accCausedSepVelocity;

                // Make sure we haven't removed more than was
                // there to remove.
                if (newSepVelocity < 0) newSepVelocity = 0;
            }*/

           // double deltaVelocity = newSepVelocity - separatingVelocity;
            double deltaVelocity =  -separatingVelocity;

            // We apply the change in velocity to each object in proportion to
            // their inverse mass (i.e. those with lower inverse mass [higher
            // actual mass] get less change in velocity)..
            double totalInverseMass = particle[0].getInverseMass();
            if (particle[1]!=null) totalInverseMass += particle[1].getInverseMass();

            // If all particles have infinite mass, then impulses have no effect
            if (totalInverseMass <= 0) return;

            // Calculate the impulse to apply
            double impulse = deltaVelocity / totalInverseMass;

            // Find the amount of impulse per unit of inverse mass
            Vector3 impulsePerIMass = contactNormal.getMultiVector(impulse);

            // Apply impulses: they are applied in the direction of the contact,
            // and are proportional to the inverse mass.
            particle[0].getVelocity().addVector(
                impulsePerIMass.getMultiVector(particle[0].getInverseMass()));
            if (particle[1]!=null)
            {
                // Particle 1 goes in the opposite direction
                particle[1].getVelocity().addVector(
                		impulsePerIMass.getMultiVector(-particle[1].getInverseMass()));
            }      
            
        }

        /**
         * Handles the interpenetration resolution for this contact.
         */
        private void resolveInterpenetration(double duration){
        	
            // If we don't have any penetration, skip this step.
            if (penetration <= 0) return;

            // The movement of each object is based on their inverse mass, so
            // total that.
            double totalInverseMass = particle[0].getInverseMass();
            if (particle[1]!=null) totalInverseMass += particle[1].getInverseMass();

            // If all particles have infinite mass, then we do nothing
            if (totalInverseMass <= 0) return;

            // Find the amount of penetration resolution per unit of inverse mass
            Vector3 movePerIMass = contactNormal.getMultiVector(penetration / totalInverseMass);

            // Calculate the the movement amounts
            particleMovement[0]=movePerIMass.getMultiVector(particle[0].getInverseMass());
            
            if (particle[1]!=null) {
            	particleMovement[1] = movePerIMass.getMultiVector(-particle[1].getInverseMass());
            } else {
            	particleMovement[1].clear();
            }

            // Apply the penetration resolution
            particle[0].getPosition().addVector(particleMovement[0]);
            //particle[0].setPosition(particle[0].getPosition() + particleMovement[0]);
            if (particle[1]!=null) {
            	particle[1].getPosition().addVector(particleMovement[1]);
            }
        }

}
