package cyclone.particle.forceGenerator;

import cyclone.entities.Particle;
import cyclone.math.Vector3;

public class ParticleDrag implements ParticleForceGenerator{

	
    /**
     * A force generator that applies a drag force. One instance
     * can be used for multiple particles.
     */
        /** Holds the velocity drag coeffificent. */
        double k1;

        /** Holds the velocity squared drag coeffificent. */
        double k2;

        /** Creates the generator with the given coefficients. */
    	public ParticleDrag(double k1, double k2){
    		this.k1=k1;
    		this.k2=k2;
    	}

        /** Applies the drag force to the given particle. */
        public void updateForce(Particle particle, double duration)
        {
            Vector3 force;
            force=particle.getVelocity();

            // Calculate the total drag coefficient
            double dragCoeff = force.magnitude();
            dragCoeff = k1 * dragCoeff + k2 * dragCoeff * dragCoeff;

            // Calculate the final force and apply it
            force.normalise();
            force.multiVectorScalar(-dragCoeff);
            particle.addForce(force);
        }        


}
