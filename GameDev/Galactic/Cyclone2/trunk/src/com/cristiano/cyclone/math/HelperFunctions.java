package com.cristiano.cyclone.math;

import com.cristiano.cyclone.utils.Matrix3;
import com.cristiano.cyclone.utils.Matrix4;
import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;



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
	
	
	
	
	
	/**
	 * Return the firing solution for a projectile starting at 'src' with
	 * velocity 'v', to hit a target, 'dst'.
	 *
	 * @param Object src position of shooter
	 * @param Object dst position & velocity of target
	 * @param Number v   speed of projectile
	 * @return Object Coordinate at which to fire (and where intercept occurs)
	 *
	 * E.g.
	 * >>> intercept({x:2, y:4}, {x:5, y:7, vx: 2, vy:1}, 5)
	 * = {x: 8, y: 8.5}
	 * 
	 * http://stackoverflow.com/questions/2248876/2d-game-fire-at-a-moving-target-by-predicting-intersection-of-projectile-and-u
	 * A posição do atirador é sempre 0 (posição do alvo é relativa)
	 * Velocidade idem
	 */
	public static Vector3 intercept(Vector3 dstPos,Vector3 dstVel, double v) {
	  double tx = dstPos.x ;
	  double ty = dstPos.y ;
	  double tz = dstPos.z ;
	  double tvx = dstVel.x;
	  double tvy = dstVel.y;
	  double tvz = dstVel.z;

	  // Get quadratic equation components
	  double a = tvx*tvx + tvy*tvy + tvz*tvz - v*v;
	  double b = 2 * (tvx * tx + tvy * ty + tvz * tz);
	  double c = tx*tx + ty*ty + tz*tz;    

	  // Solve quadratic
	  Vector3 ts = quad(a, b, c); // See quad(), below

	  // Find smallest positive solution
	  Vector3 sol = null;
	  if (ts!=null) {
	    double t0 = ts.x;
	    double t1 = ts.y;
	    double t2 = ts.z;
	    double t = Math.min(t0, t1);
	    t = Math.min(t, t2);
	    
	    if (t < 0) {
	    	t = Math.max(t0, t1);
	    	t = Math.max(t, t2);
	    }
	    if (t > 0) {
	      sol = new Vector3(dstPos.x + dstVel.x*t,
	    		  dstPos.y + dstVel.y*t,
	    		  dstPos.z + dstVel.z*t);
	    }
	  }

	  return sol;
	}


	/**
	 * Return solutions for quadratic
	 */
	public static Vector3 quad(double a,double b,double c) {
		Vector3 sol = null;
	  if (Math.abs(a) < 1e-6) {
	    if (Math.abs(b) < 1e-6) {
	      sol = Math.abs(c) < 1e-6 ? new Vector3(0,0,0) : null;
	    } else {
	      sol = new Vector3(-c/b, -c/b, -c/b);
	    }
	  } else {
	    double disc = b*b - 4*a*c;
	    if (disc >= 0) {
	      disc = Math.sqrt(disc);
	      a = 2*a;
	      sol = new Vector3((-b-disc)/a, (-b+disc)/a,(-b-disc)/a);
	    }
	  }
	  return sol;
	}
	
}
