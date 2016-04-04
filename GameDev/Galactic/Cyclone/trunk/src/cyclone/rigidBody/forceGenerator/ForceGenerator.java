package cyclone.rigidBody.forceGenerator;

import cyclone.entities.RigidBody2;

public interface ForceGenerator {

    /**
     * A force generator can be asked to add a force to one or more
     * particles.
     */

        /**
         * Overload this in implementations of the interface to calculate
         * and update the force applied to the given particle.
         */
        public void updateForce(RigidBody2 particle, double duration);
    

}
