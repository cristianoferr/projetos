package cyclone.particle.forceGenerator;

import cyclone.entities.Particle;
import cyclone.math.Vector3;

public class ParticleGravity implements ParticleForceGenerator {

	/**
     * A force generator that applies a gravitational force. One instance
     * can be used for multiple particles.
     */
    Vector3 gravity;

        /** Creates the generator with the given acceleration. */

	public ParticleGravity(Vector3 gravity){
		this.gravity=gravity;
	}
        /**
         * Overload this in implementations of the interface to calculate
         * and update the force applied to the given particle.
         */
        public void updateForce(Particle particle, double duration){
            // Check that we do not have infinite mass
            if (!particle.hasFiniteMass()) return;

            // Apply the mass-scaled force to the particle
            particle.addForce(gravity.getMultiVector(particle.getMass()));
           // System.out.println("updateForce()");
        }
    

}
