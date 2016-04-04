package cyclone.entities.geom;



public class GeomSphere extends Geom{
	
	
        public GeomSphere(double radius) {
		super(Geom.PrimitiveType.SPHERE);
		this.radius=radius;
	}

		/**
         * The radius of the sphere.
         */
	public double radius;
	
	
	public double getRadius() {
			return radius;
		}

	
	public double calculateMass(){
		return (double)(4/3*(Math.PI)*Math.pow(radius,3) * density);
	}
	
	
}
