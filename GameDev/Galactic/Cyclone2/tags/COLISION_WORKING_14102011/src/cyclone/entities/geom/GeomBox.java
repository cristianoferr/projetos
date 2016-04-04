package cyclone.entities.geom;

import cristiano.math.Vector3;


public class GeomBox extends Geom{
	
        public GeomBox(Vector3 halfSize) {
		super(Geom.PrimitiveType.BOX);
		this.halfSize=halfSize;
		
	}

		/**
         * Holds the half-sizes of the box along each of its local axes.
         */
	public Vector3 halfSize;
	
	
	
	
	public double getRadius(){
		return halfSize.x;
	}
	
	public double calculateMass(){
		return halfSize.x*2 * halfSize.y*2 * halfSize.z*2 * density;
	}
	
}

