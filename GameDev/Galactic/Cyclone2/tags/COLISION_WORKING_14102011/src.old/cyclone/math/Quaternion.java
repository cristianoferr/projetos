package cyclone.math;

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

        public void rotateByVector(Vector3 vector)
        {
            Quaternion q=new Quaternion(0, vector.x, vector.y, vector.z);
            multiQuaternion(q);
            //(*this) *= q;
        }
}
