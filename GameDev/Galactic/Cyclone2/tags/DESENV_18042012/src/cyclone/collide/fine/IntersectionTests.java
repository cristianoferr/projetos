package cyclone.collide.fine;

import cristiano.math.Vector3;
import cyclone.entities.RigidBody;
import cyclone.entities.GeomPoly.Face;
import cyclone.entities.geom.CollisionPlane;
import cyclone.entities.geom.GeomBox;
import cyclone.entities.geom.GeomSphere;

public abstract class IntersectionTests {
    

	public  static boolean sphereAndHalfSpace(
			RigidBody sphere,
             CollisionPlane plane){
	    // Find the distance from the origin
		 double r1=((GeomSphere)sphere.getGeom()).getRadius();
		double ballDistance =
	        plane.direction.scalarProduct(sphere.getAxis(3)) -
	        r1;

	    // Check for the intersection
	    return ballDistance <= plane.offset;
	}

	public static boolean sphereAndSphere(
			RigidBody one,
			RigidBody two){
	    // Find the vector between the objects
	    Vector3 midline = one.getAxis(3).getSubVector(two.getAxis(3));

	    // See if it is large enough.
	    double r1=((GeomSphere)one.getGeom()).getRadius();
	    double r2=((GeomSphere)two.getGeom()).getRadius();
	    
	    return midline.squareMagnitude() <
	        (r1+r2)*(r1+r2);
	}

	static double transformToAxis(
			RigidBody box,
		    Vector3 axis){
		    return
		        ((GeomBox)box.getGeom()).halfSize.x * Math.abs(axis.scalarProduct(box.getAxis(0))) +
		        ((GeomBox)box.getGeom()).halfSize.y * Math.abs(axis.scalarProduct(box.getAxis(1))) +
		        ((GeomBox)box.getGeom()).halfSize.z * Math.abs(axis.scalarProduct(box.getAxis(2)));
		}

	/**
	 * This function checks if the two boxes overlap
	 * along the given axis. The final parameter toCentre
	 * is used to pass in the vector between the boxes centre
	 * points, to avoid having to recalculate it each time.
	 */
	static boolean overlapOnAxis(
			RigidBody one,
			RigidBody two,
	     Vector3 axis,
	     Vector3 toCentre){
	    // Project the half-size of one onto axis
	    double oneProject = transformToAxis(one, axis);
	    double twoProject = transformToAxis(two, axis);

	    // Project this onto the axis
	    double distance = Math.abs(toCentre.scalarProduct(axis));

	    // Check for overlap
	    return (distance < oneProject + twoProject);
	}
	
	public static boolean boxAndBox(
			RigidBody one,
			RigidBody two){
	    // Find the vector between the two centres
	    Vector3 toCentre = two.getAxis(3).getSubVector(one.getAxis(3));

	 // Check on box one's axes first
	    boolean flagAxes1=overlapOnAxis(one, two, one.getAxis(0), toCentre) &&
			overlapOnAxis(one, two, one.getAxis(1), toCentre) &&
			overlapOnAxis(one, two, one.getAxis(2), toCentre) ;
	    if (!flagAxes1) {return false;}
	    
	 // And on two's
	    boolean flagAxes2=overlapOnAxis(one, two, two.getAxis(0), toCentre) &&
        overlapOnAxis(one, two, two.getAxis(1), toCentre) &&
        overlapOnAxis(one, two, two.getAxis(2), toCentre) ;
	    if (!flagAxes2) {return false;}
	    
	 // Now on the cross products
	    for (int i=0;i<3;i++){
		    for (int j=0;j<3;j++){
		    	boolean flag=overlapOnAxis(one, two, one.getAxis(i).getVectorProduct(two.getAxis(j)), toCentre);
		    	if (!flag) return false;
		    }
	    }
	    return true;
	    /*boolean flagCross=overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(0)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(1)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(0).getVectorProduct(two.getAxis(2)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(0)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(1)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(1).getVectorProduct(two.getAxis(2)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(0)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(1)), toCentre) &&
        overlapOnAxis(one, two, one.getAxis(2).getVectorProduct(two.getAxis(2)), toCentre);*/
//	    if (!flagCross) return false;
	    //return (flagAxes1 && flagAxes2 && flagCross);

	    
	}

        /**
         * Does an intersection test on an arbitrarily aligned box and a
         * half-space.
         *
         * The box is given as a transform matrix, including
         * position, and a vector of half-sizes for the extend of the
         * box along each local axis.
         *
         * The half-space is given as a direction (i.e. unit) vector and the
         * offset of the limiting plane from the origin, along the given
         * direction.
         */
	public static boolean boxAndHalfSpace(
			RigidBody box,
             CollisionPlane plane){
	    // Work out the projected radius of the box onto the plane direction
	    double projectedRadius = transformToAxis(box, plane.direction);

	    // Work out how far the box is from the origin
	    double boxDistance =
	        plane.direction.scalarProduct(box.getAxis(3))-projectedRadius;

	    // Check for the intersection
	    return boxDistance <= plane.offset;
	}
	
	
	/*---------------------------
	 * 
	 * Código para interseção de triangulos convertido a partir de:
	 * http://jgt.akpeters.com/papers/GuigueDevillers03/triangle_triangle_intersection.html
	 * 
	 * ---------------------------
	 */
	
	
	/*
	*
	*  Three-dimensional Triangle-Triangle Overlap Test
	*
	*/


	public static Vector3 cross(Vector3 v1,Vector3 v2){
		Vector3 dest=new Vector3();
	    dest.x=v1.y*v2.z-v1.z*v2.y; 
	    dest.y=v1.z*v2.x-v1.x*v2.z; 
	    dest.z=v1.x*v2.y-v1.y*v2.x;
	    return dest;
	}

	public static double dot(Vector3 v1,Vector3 v2) {
		return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
	}



	public static Vector3 sub(Vector3 v1,Vector3 v2){
		Vector3 dest=new Vector3();
		dest.x=v1.x-v2.x; 
		dest.y=v1.y-v2.y;
		dest.z=v1.z-v2.z;
		return dest;
     } 


	public static Vector3 scalar(double alpha,Vector3 v) {
		Vector3 dest=new Vector3();
		dest.x = alpha * v.x; 
		dest.y = alpha * v.y; 
		dest.z = alpha * v.z;
		return dest;
	}
	
	
	
	
	
	public static double overlapTestFaces(Face f1,Face f2){
		return tri2triOverlapTest3D(f1.get(0),f1.get(1),f1.get(2),
				f2.get(0),f2.get(1),f2.get(2));
	}
	
	public static double tri2triOverlapTest3D(Vector3 p1, Vector3 q1, Vector3 r1, 
			Vector3 p2, Vector3 q2, Vector3 r2)
	{
	  double dp1, dq1, dr1, dp2, dq2, dr2;
	  Vector3 v1, v2;
	  Vector3 n1, n2; 
	  
	  /* Compute distance signs  of p1, q1 and r1 to the plane of
	     triangle(p2,q2,r2) */


	  v1=sub(p2,r2);
	  v2=sub(q2,r2);
	  n2=cross(v1,v2);

	  v1=sub(p1,r2);
	  dp1 = dot(v1,n2);
	  v1=sub(q1,r2);
	  dq1 = dot(v1,n2);
	  v1=sub(r1,r2);
	  dr1 = dot(v1,n2);
	  
	  if (((dp1 * dq1) > 0.0f) && ((dp1 * dr1) > 0.0f))  {
		  return 0; 
	  }

	  /* Compute distance signs  of p2, q2 and r2 to the plane of
	     triangle(p1,q1,r1) */

	  
	  v1=sub(q1,p1);
	  v2=sub(r1,p1);
	  n1=cross(v1,v2);

	  v1=sub(p2,r1);
	  dp2 = dot(v1,n1);
	  v1=sub(q2,r1);
	  dq2 = dot(v1,n1);
	  v1=sub(r2,r1);
	  dr2 = dot(v1,n1);
	  
	  if (((dp2 * dq2) > 0.0f) && ((dp2 * dr2) > 0.0f)) {
		  return 0;
	  }

	  /* Permutation in a canonical form of T1's vertices */


	  if (dp1 > 0.0f) {
	    if (dq1 > 0.0f) {
	    	return tri2tri3D(r1,p1,q1,p2,r2,q2,dp2,dr2,dq2,n1,n2);
	    }
	    else if (dr1 > 0.0f) {
	    	return tri2tri3D(q1,r1,p1,p2,r2,q2,dp2,dr2,dq2,n1,n2)	;
	    }
	    else return tri2tri3D(p1,q1,r1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	  } else if (dp1 < 0.0f) {
	    if (dq1 < 0.0f) {
	    	return tri2tri3D(r1,p1,q1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	    }
	    else if (dr1 < 0.0f) {
	    	return tri2tri3D(q1,r1,p1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	    }
	    else return tri2tri3D(p1,q1,r1,p2,r2,q2,dp2,dr2,dq2,n1,n2);
	  } else {
	    if (dq1 < 0.0f) {
	      if (dr1 >= 0.0f) {
	    	  return tri2tri3D(q1,r1,p1,p2,r2,q2,dp2,dr2,dq2,n1,n2);
	      }
	      else {return 
	    	  tri2tri3D(p1,q1,r1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	      }
	    }
	    else if (dq1 > 0.0f) {
	      if (dr1 > 0.0f) return tri2tri3D(p1,q1,r1,p2,r2,q2,dp2,dr2,dq2,n1,n2);
	      else return tri2tri3D(q1,r1,p1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	    }
	    else  {
	      if (dr1 > 0.0f) {
	    	  return tri2tri3D(r1,p1,q1,p2,q2,r2,dp2,dq2,dr2,n1,n2);
	      }
	      else if (dr1 < 0.0f) {
	    	  return tri2tri3D(r1,p1,q1,p2,r2,q2,dp2,dr2,dq2,n1,n2);
	      }
	      else {return 
	    	  coplanarTri2Tri3D(p1,q1,r1,p2,q2,r2,n1,n2);
	      }
	    }
	  }
	};
	
	
	public static int checkMinMax(Vector3 p1,Vector3 q1,Vector3 r1,Vector3 p2,Vector3 q2,Vector3 r2) {
		Vector3 v1=sub(p2,q1);
		Vector3 v2=  sub(p1,q1);
		Vector3 n1=  cross(v1,v2);
		v1=sub(q2,q1);
		if (dot(v1,n1) > 0.0f) {
			return 0;
		}
		v1=sub(p2,p1);
		v2=sub(r1,p1);
		n1=cross(v1,v2);
		v1=sub(r2,p1) ;
		  if (dot(v1,n1) > 0.0f) {
			  return 0;
		  }
		  else {
			  return 1;
		  } 
		 }
	
	public static double tri2tri3D(Vector3 p1,Vector3 q1,Vector3 r1,Vector3 p2,Vector3 q2,
			Vector3 r2,double dp2,double dq2,double dr2,Vector3 N1,Vector3 N2) { 
		  if (dp2 > 0.0f) { 
		     if (dq2 > 0.0f) {
		    	 return checkMinMax(p1,r1,q1,r2,p2,q2); 
		     }
		     else if (dr2 > 0.0f) {
		    	 return checkMinMax(p1,r1,q1,q2,r2,p2);
		     }
		     else {
		    	 return checkMinMax(p1,q1,r1,p2,q2,r2); }
		     }
		  else if (dp2 < 0.0f) { 
		    if (dq2 < 0.0f) {
		    	return checkMinMax(p1,q1,r1,r2,p2,q2);
		    }
		    else if (dr2 < 0.0f) {
		    	return checkMinMax(p1,q1,r1,q2,r2,p2);
		    }
		    else {
		    	return checkMinMax(p1,r1,q1,p2,q2,r2);
		    }
		  } else { 
		    if (dq2 < 0.0f) { 
		      if (dr2 >= 0.0f)  {
		    	  return checkMinMax(p1,r1,q1,q2,r2,p2);
		      }
		      else {
		    	  return checkMinMax(p1,q1,r1,p2,q2,r2);
		      }
		    } 
		    else if (dq2 > 0.0f) { 
		      if (dr2 > 0.0f) {
		    	  return checkMinMax(p1,r1,q1,p2,q2,r2);
		      }
		      else  return checkMinMax(p1,q1,r1,q2,r2,p2);
		    } 
		    else  { 
		      if (dr2 > 0.0f) {
		    	  return checkMinMax(p1,q1,r1,r2,p2,q2);
		      }
		      else if (dr2 < 0.0f) {
		    	  return checkMinMax(p1,r1,q1,r2,p2,q2);
		      }
		      else return coplanarTri2Tri3D(p1,q1,r1,p2,q2,r2,N1,N2);
		     }}}
	
	
	public static int coplanarTri2Tri3D(Vector3 p1, Vector3 q1, Vector3 r1,
			Vector3 p2, Vector3 q2, Vector3 r2,
			Vector3 normal1, Vector3 normal2){

		Vector3 vP1=new Vector3();
		Vector3 vQ1=new Vector3();
		Vector3 vR1=new Vector3();
		
		Vector3 vP2=new Vector3();
		Vector3 vQ2=new Vector3();
		Vector3 vR2=new Vector3();
		

double nX, nY, nZ;

nX = ((normal1.x<0)?-normal1.x:normal1.x);
nY = ((normal1.y<0)?-normal1.y:normal1.y);
nZ = ((normal1.z<0)?-normal1.z:normal1.z);


/* Projection of the triangles in 3D onto 2D such that the area of
  the projection is maximized. */


if (( nX > nZ ) && ( nX >= nY )) {
 // Project onto plane YZ

   vP1.x = q1.z; vP1.y = q1.y;
   vQ1.x = p1.z; vQ1.y = p1.y;
   vR1.x = r1.z; vR1.y = r1.y; 
 
   vP2.x = q2.z; vP2.y = q2.y;
   vQ2.x = p2.z; vQ2.y = p2.y;
   vR2.x = r2.z; vR2.y = r2.y; 

} else if (( nY > nZ ) && ( nY >= nX )) {
 // Project onto plane XZ

 vP1.x = q1.x; vP1.y = q1.z;
 vQ1.x = p1.x; vQ1.y = p1.z;
 vR1.x = r1.x; vR1.y = r1.z; 

 vP2.x = q2.x; vP2.y = q2.z;
 vQ2.x = p2.x; vQ2.y = p2.z;
 vR2.x = r2.x; vR2.y = r2.z; 
 
} else {
 // Project onto plane XY

 vP1.x = p1.x; vP1.y = p1.y; 
 vQ1.x = q1.x; vQ1.y = q1.y; 
 vR1.x = r1.x; vR1.y = r1.y; 
 
 vP2.x = p2.x; vP2.y = p2.y; 
 vQ2.x = q2.x; vQ2.y = q2.y; 
 vR2.x = r2.x; vR2.y = r2.y; 
}

return tri2TriOverlapTest2D(vP1,vQ1,vR1,vP2,vQ2,vR2);
 
};


public static int tri2TriOverlapTest2D(Vector3 p1, Vector3 q1, Vector3 r1, 
		Vector3 p2, Vector3 q2, Vector3 r2) {
if ( orient2D(p1,q1,r1) < 0.0f ){
if ( orient2D(p2,q2,r2) < 0.0f ){
		return ccwTri2TriIntersection2D(p1,r1,q1,p2,r2,q2);
	}else{
		return ccwTri2TriIntersection2D(p1,r1,q1,p2,q2,r2);}
	}
else{
	if ( orient2D(p2,q2,r2) < 0.0f ){
		return ccwTri2TriIntersection2D(p1,q1,r1,p2,r2,q2);}
	else{
		return ccwTri2TriIntersection2D(p1,q1,r1,p2,q2,r2);
	}
}
};

public static int  ccwTri2TriIntersection2D(Vector3 p1, Vector3 q1, Vector3 r1, 
		Vector3 p2, Vector3 q2, Vector3 r2) {
	if ( orient2D(p2,q2,p1) >= 0.0f ) {
		if ( orient2D(q2,r2,p1) >= 0.0f ) {
			if ( orient2D(r2,p2,p1) >= 0.0f ) {
				return 1;
			}
			else {
				return intersectionTestEdge(p1,q1,r1,p2,q2,r2);
			}
		} else {  
			if ( orient2D(r2,p2,p1) >= 0.0f ){ 
				return intersectionTestEdge(p1,q1,r1,r2,p2,q2);
			}else {
				return intersectionTestVertex(p1,q1,r1,p2,q2,r2);}
			}
		}
		else {
			if ( orient2D(q2,r2,p1) >= 0.0f ) {
				if ( orient2D(r2,p2,p1) >= 0.0f ) {
					return intersectionTestEdge(p1,q1,r1,q2,r2,p2);
				}else  {
					return intersectionTestVertex(p1,q1,r1,q2,r2,p2);
				}
			}
			else {
				return intersectionTestVertex(p1,q1,r1,r2,p2,q2);
			}
	}
};

/* some 2D macros */

public static double orient2D(Vector3 a,Vector3 b,Vector3 c)  {
	return (a.x-c.x)*(b.y-c.y)-(a.y-c.y)*(b.x-c.x);
}

public static int  intersectionTestVertex(Vector3 p1, Vector3 q1, Vector3 r1, Vector3 p2, Vector3 q2, Vector3 r2) {
	  if (orient2D(r2,p2,q1) >= 0.0f)
	    if (orient2D(r2,q2,q1) <= 0.0f)
	      if (orient2D(p1,p2,q1) > 0.0f) {
		if (orient2D(p1,q2,q1) <= 0.0f) {
			return 1; 
		}
		else {
			return 0;
		}
		} else {
		if (orient2D(p1,p2,r1) >= 0.0f){
		  if (orient2D(q1,r1,p2) >= 0.0f) {
			  return 1; 
		  } else {
			  return 0;
		  }
		} else {
			return 0;
		}
		}
	    else 
	      if (orient2D(p1,q2,q1) <= 0.0f)
		if (orient2D(r2,q2,r1) <= 0.0f)
		  if (orient2D(q1,r1,q2) >= 0.0f) {
			  return 1; 
		  }
		  else {
			  return 0;
		  }
		else {
			return 0;
		}
	      else {
	    	  return 0;
	      }
	  else
	    if (orient2D(r2,p2,r1) >= 0.0f) 
	      if (orient2D(q1,r1,r2) >= 0.0f)
		if (orient2D(p1,p2,r1) >= 0.0f) {
			return 1;
		}
		else {
			return 0;
		}
	      else 
		if (orient2D(q1,r1,q2) >= 0.0f) {
		  if (orient2D(r2,r1,q2) >= 0.0f) {
			  return 1; 
		  }
		  else {
			  return 0; 
		  }
		}
		else {
			return 0; 
		}
	    else  {
	    	return 0; 
	    }
	 };



	 public static int  intersectionTestEdge(Vector3 p1, Vector3 q1, Vector3 r1, Vector3 p2, Vector3 Q2, Vector3 r2) {
	  if (orient2D(r2,p2,q1) >= 0.0f) {
	    if (orient2D(p1,p2,q1) >= 0.0f) { 
	        if (orient2D(p1,q1,r2) >= 0.0f) {
	        	return 1; 
	        }
	        else {
	        	return 0;}}
	    else { 
	        
	      if (orient2D(q1,r1,p2) >= 0.0f){ 
		if (orient2D(r1,p1,p2) >= 0.0f) {
			return 1;
		} else {
			return 0;
		}
		} 
	      else return 0; } 
	  } else {
	    if (orient2D(r2,p2,r1) >= 0.0f) {
	      if (orient2D(p1,p2,r1) >= 0.0f) {
		if (orient2D(p1,r1,r2) >= 0.0f) return 1;  
		else {
		  if (orient2D(q1,r1,r2) >= 0.0f) return 1; else return 0;}}
	      else  return 0; }
	    else return 0; }}
}
