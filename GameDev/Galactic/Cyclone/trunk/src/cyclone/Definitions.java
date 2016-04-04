package cyclone;

import cyclone.math.Vector3;

public class Definitions {
	static double sleepEpsilon=0.1;
	public static final Vector3 GRAVITY = new Vector3(0, -9.81, 0);
	
	public static final Vector3 HIGH_GRAVITY =  new Vector3(0, -19.62, 0);
	public static final Vector3 UP = new Vector3(0, 1, 0);
	public static final Vector3 RIGHT = new Vector3(1, 0, 0);
	public static final Vector3 OUT_OF_SCREEN = new Vector3(0, 0, 1);
	public static final Vector3 X = new Vector3(0, 1, 0);
	public static final Vector3 Y = new Vector3(1, 0, 0);
	public static final Vector3 Z = new Vector3(0, 0, 1);
	
	public static double getSleepEpsilon() {
		return sleepEpsilon;
	}

	public static void setSleepEpsilon(double sleepEpsilon) {
		Definitions.sleepEpsilon = sleepEpsilon;
	}
}
