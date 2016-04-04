package cyclone.collide;

import cyclone.math.Vector3;

/**
 * Represents a bounding sphere that can be tested for overlap.
 */
public class BoundingSphere{
    
    double radius;
    Vector3 centre;
	

    /**
     * Creates a new bounding sphere at the given centre and radius.
     */
    public BoundingSphere( Vector3 centre, double radius){
        this.centre = centre;
        this.radius = radius;

    }
    public BoundingSphere(){
    	
    }

    /**
     * Creates a bounding sphere to enclose the two given bounding
     * spheres.
     */
    public BoundingSphere( BoundingSphere one,  BoundingSphere two){
        Vector3 centreOffset = two.centre.getSubVector(one.centre);
        double distance = centreOffset.squareMagnitude();
        double radiusDiff = two.radius - one.radius;

        // Check if the larger sphere encloses the small one
        if (radiusDiff*radiusDiff >= distance)
        {
            if (one.radius > two.radius)
            {
                centre = one.centre;
                radius = one.radius;
            }
            else
            {
                centre = two.centre;
                radius = two.radius;
            }
        }
    // Otherwise we need to work with partially
    // overlapping spheres
    else
    {
        distance = Math.sqrt(distance);
        radius = (distance + one.radius + two.radius) * ((double)0.5);

        // The new centre is based on one's centre, moved towards
        // two's centre by an ammount proportional to the spheres'
        // radii.
        centre = one.centre;
        if (distance > 0)
        {
            centre.addVector(centreOffset.getMultiVector((radius - one.radius)/distance));
        }
    }
   }

     /**
     * Checks if the bounding sphere overlaps with the other given
     * bounding sphere.
     */
    public boolean overlaps( BoundingSphere other) {
		double distanceSquared = (centre.getSubVector(other.centre)).squareMagnitude();
		return distanceSquared < (radius+other.radius)*(radius+other.radius);
    }

    /**
     * Reports how much this bounding sphere would have to grow
     * by to incorporate the given bounding sphere. Note that this
     * calculation returns a value not in any particular units (i.e.
     * its not a volume growth). In fact the best implementation
     * takes into account the growth in surface area (after the
     * Goldsmith-Salmon algorithm for tree ruction).
     */
    public double getGrowth( BoundingSphere other) {
    	 BoundingSphere newSphere=new BoundingSphere(this, other);

    	    // We return a value proportional to the change in surface
    	    // area of the sphere.
    	    return newSphere.radius*newSphere.radius - radius*radius;
    }

    /**
     * Returns the volume of this bounding volume. This is used
     * to calculate how to recurse into the bounding volume tree.
     * For a bounding sphere it is a simple calculation.
     */
    public double getSize() 
    {
        return ((double)1.333333) * Math.PI * radius * radius * radius;
    }
}
