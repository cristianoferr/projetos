package com.cristiano.cyclone.utils;

import org.openmali.vecmath2.Quaternion4f;


public class Quaternion {

	
    /**
     * Holds a three degree of freedom orientation.
     *
     * Quaternions have
     * several mathematical properties that make them useful for
     * representing orientations, but require four items of data to
     * hold the three degrees of freedom. These four items of data can
     * be viewed as the coefficients of a complex number with three
     * imaginary parts. The mathematics of the quaternion is then
     * defined and is roughly correspondent to the math of 3D
     * rotations. A quaternion is only a valid rotation if it is
     * normalised: i.e. it has a length of 1.
     *
     * @note Angular velocity and acceleration can be correctly
     * represented as vectors. Quaternions are only needed for
     * orientation.
     */
	
	public double r;

	public double i, j, k;

            /**
             * Holds the quaternion data in array form.
             */
	double data[];
        

        // ... other Quaternion code as before ...

        /**
         * The default constructor creates a quaternion representing
         * a zero rotation.
         */
        public Quaternion() {
        	this(1,0,0,0);
        }

        public String toString(){
        	return "r:"+r+" i:"+i+" j:"+j+" k:"+k;
        }
        
        public double getA(){
        	return i;
        }
        public double getB(){
        	return j;
        }
        public double getC(){
        	return k;
        }
        public double getD(){
        	return r;
        }
        
        public Vector3 multiplyVector3( Vector3 dest ) {
        	dest=new Vector3(dest);

    		double x    = dest.x;
    		double y    = dest.y;
    		double z    = dest.z;

    		double qx   = this.i;
    		double qy = this.j;
    		double qz = this.k;
    		double qw = this.r;

    		// calculate quat * vector

    		double ix =  qw * x + qy * z - qz * y;
    		double iy =  qw * y + qz * x - qx * z;
    		double iz =  qw * z + qx * y - qy * x;
    		double iw = -qx * x - qy * y - qz * z;

    		// calculate result * inverse quat

    		dest.x = ix * qw + iw * -qx + iy * -qz - iz * -qy;
    		dest.y = iy * qw + iw * -qy + iz * -qx - ix * -qz;
    		dest.z = iz * qw + iw * -qz + ix * -qy - iy * -qx;

    		return dest;

    	}
        
        /**
         * The explicit constructor creates a quaternion with the given
         * components.
         *
         * @param r The real component of the rigid body's orientation
         * quaternion.
         *
         * @param i The first complex component of the rigid body's
         * orientation quaternion.
         *
         * @param j The second complex component of the rigid body's
         * orientation quaternion.
         *
         * @param k The third complex component of the rigid body's
         * orientation quaternion.
         *
         * @note The given orientation does not need to be normalised,
         * and can be zero. This function will not alter the given
         * values, or normalise the quaternion. To normalise the
         * quaternion (and make a zero quaternion a legal rotation),
         * use the normalise function.
         *
         * @see normalise
         */
        public Quaternion(double r, double i, double j, double k){
	        this.r=r;
	        this.i=i;
	        this.j=j;
	        this.k=k;
        }
        
        
        public Quaternion(Quaternion q){
	        this.r=q.r;
	        this.i=q.i;
	        this.j=q.j;
	        this.k=q.k;
        }
        
        
        public static Quaternion randomQuaternion()
        {
            Quaternion q=new Quaternion(
                Math.random(),
                Math.random(),
                Math.random(),
                Math.random()
                );
            q.normalise();
            return q;
        }
        
        /**
         * Normalises the quaternion to unit length, making it a valid
         * orientation quaternion.
         */
        public void normalise()
        {
            double d = r*r+i*i+j*j+k*k;

            // Check for zero length quaternion, and use the no-rotation
            // quaternion in that case.
            if (d == 0) {
                r = 1;
                return;
            }

            d = ((double)1.0)/Math.sqrt(d);
            r *= d;
            i *= d;
            j *= d;
            k *= d;
        }      
        
        /**
         * Multiplies the quaternion by the given quaternion.
         *
         * @param multiplier The quaternion by which to multiply.
         */
        public void multiQuaternion(Quaternion multiplier)
        {
            Quaternion q = this;
            double ir,ii,ij,ik;
            ir = q.r*multiplier.r - q.i*multiplier.i -
                q.j*multiplier.j - q.k*multiplier.k;
            ii = q.r*multiplier.i + q.i*multiplier.r +
                q.j*multiplier.k - q.k*multiplier.j;
            ij = q.r*multiplier.j + q.j*multiplier.r +
                q.k*multiplier.i - q.i*multiplier.k;
            ik = q.r*multiplier.k + q.k*multiplier.r +
                q.i*multiplier.j - q.j*multiplier.i;
            r=ir;
            i=ii;
            j=ij;
            k=ik;
        }

        
        public Quaternion4f getQuaternion4f(){
        	return new Quaternion4f((float)i,(float)j,(float)k,(float)r);
        	
        }
        
        /**
         * Adds the given vector to this, scaled by the given amount.
         * This is used to update the orientation quaternion by a rotation
         * and time.
         *
         * @param vector The vector to add.
         *
         * @param scale The amount of the vector to add.
         */
        public void addScaledVector(Vector3 vector, double scale)
        {
            Quaternion q=new Quaternion(0,
                vector.x * scale,
                vector.y * scale,
                vector.z * scale);
            q.multiQuaternion(this);
            r += q.r * ((double)0.5);
            i += q.i * ((double)0.5);
            j += q.j * ((double)0.5);
            k += q.k * ((double)0.5);
        }

        
        public boolean isEqual(Quaternion obj){
        	return ((obj.i==i) && (obj.j==j) && (obj.k==k) && (obj.r==r));
        }
        public void rotateByVector(Vector3 vector)
        {
            Quaternion q=new Quaternion(0, vector.x, vector.y, vector.z);
            multiQuaternion(q);
            //(*this) *= q;
        }

		public void copy(Quaternion d) {
			this.i=d.i;
			this.j=d.j;
			this.k=d.k;
			this.r=d.r;
			
			
		}
}
