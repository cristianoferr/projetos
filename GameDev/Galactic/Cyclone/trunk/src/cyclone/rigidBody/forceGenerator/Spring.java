package cyclone.rigidBody.forceGenerator;

import cyclone.entities.RigidBody;
import cyclone.math.Vector3;
import cyclone.particle.forceGenerator.ParticleSpring;

public class Spring extends ParticleSpring {

	   /**
     * The point of connection of the spring to the other object,
     * in that object's local coordinates.
     */
    Vector3 otherConnectionPoint;
    /**
     * The point of connection of the spring, in local
     * coordinates.
     */
    Vector3 connectionPoint;
    
    
    /** The particle at the other end of the spring. */
    RigidBody other;
	public Spring( Vector3 localConnectionPt,
            RigidBody other,
            Vector3 otherConnectionPt,
            double springConstant,
            double restLength) {
		super(other, springConstant, restLength);
		connectionPoint=localConnectionPt;
		this.otherConnectionPoint=otherConnectionPt;
		this.other=other;
	}
	
	
	void updateForce(RigidBody body, double duration)
	{
	    // Calculate the two ends in world space
	    Vector3 lws = body.getPointInWorldSpace(connectionPoint);
	    Vector3 ows = other.getPointInWorldSpace(otherConnectionPoint);

	    // Calculate the vector of the spring
	    Vector3 force = lws.getSubVector(ows);

	    // Calculate the magnitude of the force
	    double magnitude = force.magnitude();
	    magnitude = Math.abs(magnitude - getRestLength());
	    magnitude *= getSpringConstant();

	    // Calculate the final force and apply it
	    force.normalise();
	    force.multiVectorScalar(-magnitude);
	    body.addForceAtPoint(force, lws);
	}



}
