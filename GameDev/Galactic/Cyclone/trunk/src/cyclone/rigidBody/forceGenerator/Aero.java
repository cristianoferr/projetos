package cyclone.rigidBody.forceGenerator;

import cyclone.entities.Particle;
import cyclone.entities.RigidBody;
import cyclone.math.Matrix3;
import cyclone.math.Vector3;
import cyclone.particle.forceGenerator.ParticleForceGenerator;


public class Aero implements ParticleForceGenerator {
    /**
     * A force generator that applies an aerodynamic force.
     */
    
        /**
         * Holds the aerodynamic tensor for the surface in body
         * space.
         */
        Matrix3 tensor;

        /**
         * Holds the relative position of the aerodynamic surface in
         * body coordinates.
         */
        Vector3 position;

        /**
         * Holds a pointer to a vector containing the windspeed of the
         * environment. This is easier than managing a separate
         * windspeed vector per generator and having to update it
         * manually as the wind changes.
         */
        Vector3 windspeed;

    
        /**
         * Creates a new aerodynamic force generator with the
         * given properties.
         */
        public Aero( Matrix3 tensor,  Vector3 position,
              Vector3 windspeed){
        	this.tensor=tensor;
        	this.position=position;
        	this.windspeed=windspeed;
        }

        /**
         * Applies the force to the given rigid body.
         */
        public void updateForce(RigidBody body, double duration){
        	   this.updateForceFromTensor(body, duration, tensor);
        }

    
        /**
         * Uses an explicit tensor matrix to update the force on
         * the given rigid body. This is exactly the same as for updateForce
         * only it takes an explicit tensor.
         */
        void updateForceFromTensor(RigidBody body, double duration,
                                   Matrix3 tensor){
        	   // Calculate total velocity (windspeed and body's velocity).
            Vector3 velocity = body.getVelocity();
            velocity.addVector(windspeed);

            // Calculate the velocity in body coordinates
            Vector3 bodyVel = body.getTransform().transformInverseDirection(velocity);

            // Calculate the force in body coordinates
            Vector3 bodyForce = tensor.transform(bodyVel);
            Vector3 force = body.getTransform().transformDirection(bodyForce);

       //    System.out.println("velocity:"+velocity +" bodyForce:"+bodyForce+" Force:"+force+" pos:"+position );
            
            // Apply the force
            body.addForceAtBodyPoint(force, position);
        }

		@Override
		public void updateForce(Particle particle, double duration) {
			updateForce((RigidBody)particle, duration);
			
		}
}
