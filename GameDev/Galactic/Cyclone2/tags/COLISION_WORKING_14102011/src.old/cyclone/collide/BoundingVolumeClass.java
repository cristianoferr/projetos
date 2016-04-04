package cyclone.collide;

import cyclone.math.Vector3;

public abstract class BoundingVolumeClass {
	Vector3 centre;
	public boolean overlaps( BoundingVolumeClass other){return false;}
	public double getSize(){return 0;}
}
