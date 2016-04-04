package com.cristiano.cyclone.collide;

import com.cristiano.cyclone.utils.Vector3;

/**
 * Represents a bounding sphere that can be tested for overlap.
 */
public class BoundingSphere extends BoundingVolumeClass{
    
    private double radius;
	

    /**
     * Creates a new bounding sphere at the given centre and radius.
     */
    public BoundingSphere( Vector3 centre, double radius){
        this.setCentre(centre);
        this.setRadius(radius);

    }
    public BoundingSphere(){
    	
    }
    public String toString(){
    	return "radius:"+radius+" centre:"+getCentre();
    }
    
    

    /**
     * Creates a bounding sphere to enclose the two given bounding
     * spheres.
     */
    public BoundingSphere( BoundingSphere one,  BoundingSphere two){
        Vector3 centreOffset = two.getCentre().getSubVector(one.getCentre());
        double distance = centreOffset.squareMagnitude();
        double radiusDiff = two.getRadius() - one.getRadius();

        // Check if the larger sphere encloses the small one
        if (radiusDiff*radiusDiff >= distance)
        {
            if (one.getRadius() > two.getRadius())
            {
                setCentre(one.getCentre());
                setRadius(one.getRadius());
            }
            else
            {
                setCentre(two.getCentre());
                setRadius(two.getRadius());
            }
        }
    // Otherwise we need to work with partially
    // overlapping spheres
    else
    {
        distance = Math.sqrt(distance);
        setRadius((distance + one.getRadius() + two.getRadius()) * ((double)0.5));

        // The new centre is based on one's centre, moved towards
        // two's centre by an ammount proportional to the spheres'
        // radii.
        setCentre(one.getCentre());
        if (distance > 0)
        {
        	setCentre(getCentre().getAddVector(centreOffset.getMultiVector((getRadius() - one.getRadius())/distance)));
        }
    }
   }

     /**
     * Checks if the bounding sphere overlaps with the other given
     * bounding sphere.
     */
    public boolean overlaps( BoundingSphere other) {
		double distanceSquared = (getCentre().getSubVector(other.getCentre())).squareMagnitude();
		return distanceSquared < (getRadius()+other.getRadius())*(getRadius()+other.getRadius());
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
    	    return newSphere.getRadius()*newSphere.getRadius() - getRadius()*getRadius();
    }

    /**
     * Returns the volume of this bounding volume. This is used
     * to calculate how to recurse into the bounding volume tree.
     * For a bounding sphere it is a simple calculation.
     */
    public double getSize() 
    {
        return ((double)1.333333) * Math.PI * getRadius() * getRadius() * getRadius();
    }
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getRadius() {
		return radius;
	}
	
}
