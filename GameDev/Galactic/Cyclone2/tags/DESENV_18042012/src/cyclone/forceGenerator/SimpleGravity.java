package cyclone.forceGenerator;

import cristiano.math.Vector3;
import cyclone.CycloneConfig;
import cyclone.entities.RigidBody;

public class SimpleGravity implements ForceGenerator {

	/**
     * A force generator that applies a gravitational force. One instance
     * can be used for multiple particles.
     */
    private Vector3 gravity;

        /** Creates the generator with the given acceleration. */

	public SimpleGravity(Vector3 gravity){
		this.gravity=gravity;
	}
        /**
         * Overload this in implementations of the interface to calculate
         * and update the force applied to the given particle.
         */
        public void updateForce(RigidBody particle, double duration){
            // Check that we do not have infinite mass
            if (!particle.hasFiniteMass()) {
            	return;
            }
            
            if (!CycloneConfig.isUsingGravity()){
            	return;
            }

            // Apply the mass-scaled force to the particle
            particle.addForce(gravity.getMultiVector(particle.getMass()));
           //Vector3 g=particle.getPosition().getMultiVector(-1);
          //  g.normalise(10);

           // particle.addForce(g.getMultiVector(particle.getMass()));
           // System.out.println("updateForce()");
        }
    

}
