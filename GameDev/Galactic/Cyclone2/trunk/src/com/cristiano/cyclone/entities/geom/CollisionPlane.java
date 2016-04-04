package com.cristiano.cyclone.entities.geom;

import com.cristiano.cyclone.utils.Vector3;

public class CollisionPlane {
	/**
     * The plane is not a primitive: it doesn't represent another
     * rigid body. It is used for contacts with the immovable
     * world geometry.
     */
    
    
        /**
         * The plane normal
         */
	public Vector3 direction;

        /**
         * The distance of the plane from the origin.
         */
	public double offset;
}
