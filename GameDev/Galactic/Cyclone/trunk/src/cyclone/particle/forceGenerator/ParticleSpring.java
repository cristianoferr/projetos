package cyclone.particle.forceGenerator;

import cyclone.entities.Particle;
import cyclone.math.Vector3;

public class ParticleSpring implements ParticleForceGenerator{

    /** The particle at the other end of the spring. */
    Particle other;

    /** Holds the sprint constant. */
    private double springConstant;

    /** Holds the rest length of the spring. */
    private double restLength;
    
	public ParticleSpring(Particle other, double sc, double rl){
		this.other=other;
		setSpringConstant(sc);
		setRestLength(rl);
	
	}

	public void updateForce(Particle particle, double duration)
	{
	    // Calculate the vector of the spring
	    Vector3 force;
	    force=particle.getPosition();
	    force.subVector(other.getPosition());

	    // Calculate the magnitude of the force
	    double magnitude = force.magnitude();
	    magnitude = Math.abs(magnitude - getRestLength());
	    magnitude *= getSpringConstant();

	    // Calculate the final force and apply it
	    force.normalise();
	    force.multiVectorScalar(-magnitude);
	    particle.addForce(force);
	}

	public void setSpringConstant(double springConstant) {
		this.springConstant = springConstant;
	}

	public double getSpringConstant() {
		return springConstant;
	}

	public void setRestLength(double restLength) {
		this.restLength = restLength;
	}

	public double getRestLength() {
		return restLength;
	}	
}
