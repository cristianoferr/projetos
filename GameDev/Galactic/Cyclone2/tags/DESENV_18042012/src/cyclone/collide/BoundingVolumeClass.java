package cyclone.collide;

import cristiano.math.Vector3;

public abstract class BoundingVolumeClass {
	private Vector3 centre;
	
	public boolean overlaps( BoundingVolumeClass other){return false;}
	public double getSize(){return 0;}
	public Vector3 getCentre() {
		return centre;
	}
	public void setCentre(Vector3 centre) {
		this.centre = centre;
	}
}
