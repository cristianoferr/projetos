package cyclone.collide.fine;

import cyclone.math.Vector3;

public abstract class IntersectionTests {
    

	public  static boolean sphereAndHalfSpace(
             CollisionSphere sphere,
             CollisionPlane plane){
	    // Find the distance from the origin
		double ballDistance =
	        plane.direction.scalarProduct(sphere.getAxis(3)) -
	        sphere.radius;

	    // Check for the intersection
	    return ballDistance <= plane.offset;
	}

	public static boolean sphereAndSphere(
             CollisionSphere one,
             CollisionSphere two){
	    // Find the vector between the objects
	    Vector3 midline = one.getAxis(3).getSubVector(two.getAxis(3));

	    // See if it is large enough.
	    return midline.squareMagnitude() <
	        (one.radius+two.radius)*(one.radius+two.radius);
	}

	static double transformToAxis(
		     CollisionBox box,
		    Vector3 axis){
		    return
		        box.halfSize.x * Math.abs(axis.scalarProduct(box.getAxis(0))) +
		        box.halfSize.y * Math.abs(axis.scalarProduct(box.getAxis(1))) +
		        box.halfSize.z * Math.abs(axis.scalarProduct(box.getAxis(2)));
		}

	/**
	 * This function checks if the two boxes overlap
	 * along the given axis. The final parameter toCentre
	 * is used to pass in the vector between the boxes centre
	 * points, to avoid having to recalculate it each time.
	 */
	static boolean overlapOnAxis(
	     CollisionBox one,
	     CollisionBox two,
	     Vector3 axis,
	     Vector3 toCentre){
	    // Project the half-size of one onto axis
	    double oneProject = transformToAxis(one, axis);
	    double twoProject = transformToAxis(two, axis);

	    // Project this onto the axis
	    double distance = Math.abs(toCentre.scalarProduct(axis));

	    // Check for overlap
	    return (distance < oneProject + twoProject);
	}
	
	public static boolean boxAndBox(
            CollisionBox one,
             CollisionBox two){
	    // Find the vector between the two centres
	    Vector3 toCentre = two.getAxis(3).getSubVector(one.getAxis(3));

	    return (
	        // Check on box one's axes first
	    		overlapOnAxis(one, two, one.getAxis(0), toCentre) &&
	    		overlapOnAxis(one, two, one.getAxis(1), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(2), toCentre) &&

	        // And on two's
	        overlapOnAxis(one, two, two.getAxis(0), toCentre) &&
	        overlapOnAxis(one, two, two.getAxis(1), toCentre) &&
	        overlapOnAxis(one, two, two.getAxis(2), toCentre) &&

	        // Now on the cross products
	        overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(0)), toCentre)) &&
	        overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(1)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(2)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(0)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(1)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(2)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(0)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(1)), toCentre) &&
	        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(2)), toCentre);
	    
	}

        /**
         * Does an intersection test on an arbitrarily aligned box and a
         * half-space.
         *
         * The box is given as a transform matrix, including
         * position, and a vector of half-sizes for the extend of the
         * box along each local axis.
         *
         * The half-space is given as a direction (i.e. unit) vector and the
         * offset of the limiting plane from the origin, along the given
         * direction.
         */
	public static boolean boxAndHalfSpace(
             CollisionBox box,
             CollisionPlane plane){
	    // Work out the projected radius of the box onto the plane direction
	    double projectedRadius = transformToAxis(box, plane.direction);

	    // Work out how far the box is from the origin
	    double boxDistance =
	        plane.direction.scalarProduct(box.getAxis(3))-projectedRadius;

	    // Check for the intersection
	    return boxDistance <= plane.offset;
	}
}
