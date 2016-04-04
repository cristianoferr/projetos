package cyclone.math;

public class HelperFunctions {

	
	/**
	 * Internal function that checks the validity of an inverse inertia tensor.
	 */
	public static void _checkInverseInertiaTensor( Matrix3 iitWorld)
	{
	    // TODO: Perform a validity check in an assert.
	}

	/**
	 * Internal function to do an intertia tensor transform by a quaternion.
	 * Note that the implementation of this function was created by an
	 * automated code-generator and optimizer.
	 */
	public static void _transformInertiaTensor(Matrix3 iitWorld,
	                                            Quaternion q,
	                                            Matrix3 iitBody,
	                                            Matrix4 rotmat)
	{
	    double t4 = rotmat.data[0]*iitBody.data[0]+
	        rotmat.data[1]*iitBody.data[3]+
	        rotmat.data[2]*iitBody.data[6];
	    double t9 = rotmat.data[0]*iitBody.data[1]+
	        rotmat.data[1]*iitBody.data[4]+
	        rotmat.data[2]*iitBody.data[7];
	    double t14 = rotmat.data[0]*iitBody.data[2]+
	        rotmat.data[1]*iitBody.data[5]+
	        rotmat.data[2]*iitBody.data[8];
	    double t28 = rotmat.data[4]*iitBody.data[0]+
	        rotmat.data[5]*iitBody.data[3]+
	        rotmat.data[6]*iitBody.data[6];
	    double t33 = rotmat.data[4]*iitBody.data[1]+
	        rotmat.data[5]*iitBody.data[4]+
	        rotmat.data[6]*iitBody.data[7];
	    double t38 = rotmat.data[4]*iitBody.data[2]+
	        rotmat.data[5]*iitBody.data[5]+
	        rotmat.data[6]*iitBody.data[8];
	    double t52 = rotmat.data[8]*iitBody.data[0]+
	        rotmat.data[9]*iitBody.data[3]+
	        rotmat.data[10]*iitBody.data[6];
	    double t57 = rotmat.data[8]*iitBody.data[1]+
	        rotmat.data[9]*iitBody.data[4]+
	        rotmat.data[10]*iitBody.data[7];
	    double t62 = rotmat.data[8]*iitBody.data[2]+
	        rotmat.data[9]*iitBody.data[5]+
	        rotmat.data[10]*iitBody.data[8];

	    iitWorld.data[0] = t4*rotmat.data[0]+
	        t9*rotmat.data[1]+
	        t14*rotmat.data[2];
	    iitWorld.data[1] = t4*rotmat.data[4]+
	        t9*rotmat.data[5]+
	        t14*rotmat.data[6];
	    iitWorld.data[2] = t4*rotmat.data[8]+
	        t9*rotmat.data[9]+
	        t14*rotmat.data[10];
	    iitWorld.data[3] = t28*rotmat.data[0]+
	        t33*rotmat.data[1]+
	        t38*rotmat.data[2];
	    iitWorld.data[4] = t28*rotmat.data[4]+
	        t33*rotmat.data[5]+
	        t38*rotmat.data[6];
	    iitWorld.data[5] = t28*rotmat.data[8]+
	        t33*rotmat.data[9]+
	        t38*rotmat.data[10];
	    iitWorld.data[6] = t52*rotmat.data[0]+
	        t57*rotmat.data[1]+
	        t62*rotmat.data[2];
	    iitWorld.data[7] = t52*rotmat.data[4]+
	        t57*rotmat.data[5]+
	        t62*rotmat.data[6];
	    iitWorld.data[8] = t52*rotmat.data[8]+
	        t57*rotmat.data[9]+
	        t62*rotmat.data[10];
	}

	/**
	 * Inline function that creates a transform matrix from a
	 * position and orientation.
	 */
	public static void _calculateTransformMatrix(Matrix4 transformMatrix,
	                                              Vector3 position,
	                                              Quaternion orientation)
	{
	    transformMatrix.data[0] = 1-2*orientation.j*orientation.j-
	        2*orientation.k*orientation.k;
	    transformMatrix.data[1] = 2*orientation.i*orientation.j -
	        2*orientation.r*orientation.k;
	    transformMatrix.data[2] = 2*orientation.i*orientation.k +
	        2*orientation.r*orientation.j;
	    transformMatrix.data[3] = position.x;

	    transformMatrix.data[4] = 2*orientation.i*orientation.j +
	        2*orientation.r*orientation.k;
	    transformMatrix.data[5] = 1-2*orientation.i*orientation.i-
	        2*orientation.k*orientation.k;
	    transformMatrix.data[6] = 2*orientation.j*orientation.k -
	        2*orientation.r*orientation.i;
	    transformMatrix.data[7] = position.y;

	    transformMatrix.data[8] = 2*orientation.i*orientation.k -
	        2*orientation.r*orientation.j;
	    transformMatrix.data[9] = 2*orientation.j*orientation.k +
	        2*orientation.r*orientation.i;
	    transformMatrix.data[10] = 1-2*orientation.i*orientation.i-
	        2*orientation.j*orientation.j;
	    transformMatrix.data[11] = position.z;
	}
}
