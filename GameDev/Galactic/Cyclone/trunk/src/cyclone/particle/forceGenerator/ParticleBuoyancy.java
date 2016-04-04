package cyclone.particle.forceGenerator;

import cyclone.entities.Particle;
import cyclone.math.Vector3;

public class ParticleBuoyancy implements ParticleForceGenerator{
	/**
     * The maximum submersion depth of the object before
     * it generates its maximum boyancy force.
     */
    double maxDepth;

    /**
     * The volume of the object.
     */
    double volume;

    /**
     * The height of the water plane above y=0. The plane will be
     * parrallel to the XZ plane.
     */
    double waterHeight;

    /**
     * The density of the liquid. Pure water has a density of
     * 1000kg per cubic meter.
     */
    double liquidDensity;
    
    
    public ParticleBuoyancy(double maxDepth,
            double volume,
            double waterHeight,
            double liquidDensity){
    	this.maxDepth=maxDepth;
    	this.volume=volume;
    	this.waterHeight=waterHeight;
    	this.liquidDensity=liquidDensity;
    }

public void updateForce(Particle particle, double duration){
	// Calculate the submersion depth
	double depth = particle.getPosition().y;
	
	// Check if we're out of the water
	if (depth >= waterHeight + maxDepth) return;
	Vector3 force=new Vector3(0,0,0);
	
	// Check if we're at maximum depth
	if (depth <= waterHeight - maxDepth){
		force.y = liquidDensity * volume;
		particle.addForce(force);
		return;
	}
	
	// Otherwise we are partly submerged
	force.y = liquidDensity * volume *
	(depth - maxDepth - waterHeight) / 2 * maxDepth;
	particle.addForce(force);
	}    
}
