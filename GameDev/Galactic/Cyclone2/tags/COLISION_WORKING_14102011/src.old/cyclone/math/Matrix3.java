package cyclone.math;

public class Matrix3 {
	  /**
     * Holds an inertia tensor, consisting of a 3x3 row-major matrix.
     * This matrix is not padding to produce an aligned structure, since
     * it is most commonly used with a mass (single real) and two
     * damping coefficients to make the 12-element characteristics array
     * of a rigid body.
     */
    
    
        /**
         * Holds the tensor matrix data in array form.
         */
	public double data[];

        // ... Other Matrix3 code as before ...

        /**
         * Creates a new matrix.
         */
	public Matrix3()
        {
		data=new double[9];
            data[0] = data[1] = data[2] = data[3] = data[4] = data[5] =
                data[6] = data[7] = data[8] = 0;
        }

        /**
         * Creates a new matrix with the given three vectors making
         * up its columns.
         */
	public  Matrix3(Vector3 compOne,  Vector3 compTwo,
             Vector3 compThree)
        {
		this();
            setComponents(compOne, compTwo, compThree);
        }

        /**
         * Creates a new matrix with explicit coefficients.
         */
        public Matrix3(double c0, double c1, double c2, double c3, double c4, double c5,
            double c6, double c7, double c8)
        {
        	this();
            data[0] = c0; data[1] = c1; data[2] = c2;
            data[3] = c3; data[4] = c4; data[5] = c5;
            data[6] = c6; data[7] = c7; data[8] = c8;
        }
        
        public Matrix3(Matrix3 m){
        	this(m.data[0],m.data[1],m.data[2],m.data[3],m.data[4],m.data[5],m.data[6],m.data[7],m.data[8]);
        	
        }

        /**
         * Sets the matrix to be a diagonal matrix with the given
         * values along the leading diagonal.
         */
        void setDiagonal(double a, double b, double c)
        {
            setInertiaTensorCoeffs(a, b, c);
        }

        
        /**
         * Sets the value of the matrix from inertia tensor values.
         */
        public void setInertiaTensorCoeffs(double ix, double iy, double iz){
        	setInertiaTensorCoeffs(ix,iy,iz,0,0,0);
        }
        
        
        /**
         * Sets the value of the matrix from inertia tensor values.
         */
        void setInertiaTensorCoeffs(double ix, double iy, double iz,
            double ixy, double ixz, double iyz)
        {
            data[0] = ix;
            data[1] = data[3] = -ixy;
            data[2] = data[6] = -ixz;
            data[4] = iy;
            data[5] = data[7] = -iyz;
            data[8] = iz;
        }

        /**
         * Sets the value of the matrix as an inertia tensor of
         * a rectangular block aligned with the body's coordinate
         * system with the given axis half-sizes and mass.
         */
        public void setBlockInertiaTensor( Vector3 halfSizes, double mass)
        {
            Vector3 squares = halfSizes.componentProduct(halfSizes);
            setInertiaTensorCoeffs(0.3f*mass*(squares.y + squares.z),
                0.3f*mass*(squares.x + squares.z),
                0.3f*mass*(squares.x + squares.y));
        }

        /**
         * Sets the matrix to be a skew symmetric matrix based on
         * the given vector. The skew symmetric matrix is the equivalent
         * of the vector product. So if a,b are vectors. a x b = A_s b
         * where A_s is the skew symmetric form of a.
         */
        public void setSkewSymmetric( Vector3 vector)
        {
            data[0] = data[4] = data[8] = 0;
            data[1] = -vector.z;
            data[2] = vector.y;
            data[3] = vector.z;
            data[5] = -vector.x;
            data[6] = -vector.y;
            data[7] = vector.x;
        }

        /**
         * Sets the matrix values from the given three vector components.
         * These are arranged as the three columns of the vector.
         */
        public void setComponents( Vector3 compOne,  Vector3 compTwo,
             Vector3 compThree)
        {
            data[0] = compOne.x;
            data[1] = compTwo.x;
            data[2] = compThree.x;
            data[3] = compOne.y;
            data[4] = compTwo.y;
            data[5] = compThree.y;
            data[6] = compOne.z;
            data[7] = compTwo.z;
            data[8] = compThree.z;

        }

        /**
         * Transform the given vector by this matrix.
         *
         * @param vector The vector to transform.
         */
       public Vector3 getMulti( Vector3 vector) 
        {
            return new Vector3(
                vector.x * data[0] + vector.y * data[1] + vector.z * data[2],
                vector.x * data[3] + vector.y * data[4] + vector.z * data[5],
                vector.x * data[6] + vector.y * data[7] + vector.z * data[8]
            );
        }

        /**
         * Transform the given vector by this matrix.
         *
         * @param vector The vector to transform.
         */
       public  Vector3 transform( Vector3 vector) 
        {
            return (this.getMulti(vector));
        }

        /**
         * Transform the given vector by the transpose of this matrix.
         *
         * @param vector The vector to transform.
         */
       public Vector3 transformTranspose( Vector3 vector) 
        {
            return new Vector3(
                vector.x * data[0] + vector.y * data[3] + vector.z * data[6],
                vector.x * data[1] + vector.y * data[4] + vector.z * data[7],
                vector.x * data[2] + vector.y * data[5] + vector.z * data[8]
            );
        }

        /**
         * Gets a vector representing one row in the matrix.
         *
         * @param i The row to return.
         */
       public  Vector3 getRowVector(int i) 
        {
            return new Vector3(data[i*3], data[i*3+1], data[i*3+2]);
        }

        /**
         * Gets a vector representing one axis (i.e. one column) in the matrix.
         *
         * @param i The row to return.
         *
         * @return The vector.
         */
       public  Vector3 getAxisVector(int i) 
        {
            return new Vector3(data[i], data[i+3], data[i+6]);
        }

        /**
         * Sets the matrix to be the inverse of the given matrix.
         *
         * @param m The matrix to invert and use to set this.
         */
       public  void setInverse( Matrix3 m)
        {
            double t4 = m.data[0]*m.data[4];
            double t6 = m.data[0]*m.data[5];
            double t8 = m.data[1]*m.data[3];
            double t10 = m.data[2]*m.data[3];
            double t12 = m.data[1]*m.data[6];
            double t14 = m.data[2]*m.data[6];

            // Calculate the determinant
            double t16 = (t4*m.data[8] - t6*m.data[7] - t8*m.data[8]+
                        t10*m.data[7] + t12*m.data[5] - t14*m.data[4]);

            // Make sure the determinant is non-zero.
            if (t16 == (double)0.0f) return;
            double t17 = 1/t16;

            data[0] = (m.data[4]*m.data[8]-m.data[5]*m.data[7])*t17;
            data[1] = -(m.data[1]*m.data[8]-m.data[2]*m.data[7])*t17;
            data[2] = (m.data[1]*m.data[5]-m.data[2]*m.data[4])*t17;
            data[3] = -(m.data[3]*m.data[8]-m.data[5]*m.data[6])*t17;
            data[4] = (m.data[0]*m.data[8]-t14)*t17;
            data[5] = -(t6-t10)*t17;
            data[6] = (m.data[3]*m.data[7]-m.data[4]*m.data[6])*t17;
            data[7] = -(m.data[0]*m.data[7]-t12)*t17;
            data[8] = (t4-t8)*t17;
        }

        /** Returns a new matrix containing the inverse of this matrix. */
       public Matrix3 inverse() 
        {
            Matrix3 result=new Matrix3();
            result.setInverse(this);
            return result;
        }

        /**
         * Inverts the matrix.
         */
        void invert()
        {
        	Matrix3 result=new Matrix3(this);
            setInverse(result);
        }

        /**
         * Sets the matrix to be the transpose of the given matrix.
         *
         * @param m The matrix to transpose and use to set this.
         */
        void setTranspose( Matrix3 m)
        {
            data[0] = m.data[0];
            data[1] = m.data[3];
            data[2] = m.data[6];
            data[3] = m.data[1];
            data[4] = m.data[4];
            data[5] = m.data[7];
            data[6] = m.data[2];
            data[7] = m.data[5];
            data[8] = m.data[8];
        }

        /** Returns a new matrix containing the transpose of this matrix. */
        public Matrix3 transpose() 
        {
            Matrix3 result=new Matrix3();
            result.setTranspose(this);
            return result;
        }

        /**
         * Returns a matrix which is this matrix multiplied by the given
         * other matrix.
         */
        public Matrix3 getMulti( Matrix3 o) 
        {
            return new Matrix3(
                data[0]*o.data[0] + data[1]*o.data[3] + data[2]*o.data[6],
                data[0]*o.data[1] + data[1]*o.data[4] + data[2]*o.data[7],
                data[0]*o.data[2] + data[1]*o.data[5] + data[2]*o.data[8],

                data[3]*o.data[0] + data[4]*o.data[3] + data[5]*o.data[6],
                data[3]*o.data[1] + data[4]*o.data[4] + data[5]*o.data[7],
                data[3]*o.data[2] + data[4]*o.data[5] + data[5]*o.data[8],

                data[6]*o.data[0] + data[7]*o.data[3] + data[8]*o.data[6],
                data[6]*o.data[1] + data[7]*o.data[4] + data[8]*o.data[7],
                data[6]*o.data[2] + data[7]*o.data[5] + data[8]*o.data[8]
                );
        }

        /**
         * Multiplies this matrix in place by the given other matrix.
         */
        public void multi( Matrix3 o)
        {
            double t1;
            double t2;
            double t3;

            t1 = data[0]*o.data[0] + data[1]*o.data[3] + data[2]*o.data[6];
            t2 = data[0]*o.data[1] + data[1]*o.data[4] + data[2]*o.data[7];
            t3 = data[0]*o.data[2] + data[1]*o.data[5] + data[2]*o.data[8];
            data[0] = t1;
            data[1] = t2;
            data[2] = t3;

            t1 = data[3]*o.data[0] + data[4]*o.data[3] + data[5]*o.data[6];
            t2 = data[3]*o.data[1] + data[4]*o.data[4] + data[5]*o.data[7];
            t3 = data[3]*o.data[2] + data[4]*o.data[5] + data[5]*o.data[8];
            data[3] = t1;
            data[4] = t2;
            data[5] = t3;

            t1 = data[6]*o.data[0] + data[7]*o.data[3] + data[8]*o.data[6];
            t2 = data[6]*o.data[1] + data[7]*o.data[4] + data[8]*o.data[7];
            t3 = data[6]*o.data[2] + data[7]*o.data[5] + data[8]*o.data[8];
            data[6] = t1;
            data[7] = t2;
            data[8] = t3;
        }

        /**
         * Multiplies this matrix in place by the given scalar.
         */
        public void multiScalar( double scalar)
        {
            data[0] *= scalar; data[1] *= scalar; data[2] *= scalar;
            data[3] *= scalar; data[4] *= scalar; data[5] *= scalar;
            data[6] *= scalar; data[7] *= scalar; data[8] *= scalar;
        }

        /**
         * Does a component-wise addition of this matrix and the given
         * matrix.
         */
       public void addMatrix( Matrix3 o)
        {
            data[0] += o.data[0]; data[1] += o.data[1]; data[2] += o.data[2];
            data[3] += o.data[3]; data[4] += o.data[4]; data[5] += o.data[5];
            data[6] += o.data[6]; data[7] += o.data[7]; data[8] += o.data[8];
        }

        /**
         * Sets this matrix to be the rotation matrix corresponding to
         * the given quaternion.
         */
        void setOrientation( Quaternion q)
        {
            data[0] = 1 - (2*q.j*q.j + 2*q.k*q.k);
            data[1] = 2*q.i*q.j + 2*q.k*q.r;
            data[2] = 2*q.i*q.k - 2*q.j*q.r;
            data[3] = 2*q.i*q.j - 2*q.k*q.r;
            data[4] = 1 - (2*q.i*q.i  + 2*q.k*q.k);
            data[5] = 2*q.j*q.k + 2*q.i*q.r;
            data[6] = 2*q.i*q.k + 2*q.j*q.r;
            data[7] = 2*q.j*q.k - 2*q.i*q.r;
            data[8] = 1 - (2*q.i*q.i  + 2*q.j*q.j);
        }

        /**
         * Interpolates a couple of matrices.
         */
        public static Matrix3 linearInterpolate( Matrix3 a,  Matrix3 b, double prop){
            Matrix3 result=new Matrix3();
            for (int i = 0; i < 9; i++) {
                result.data[i] = a.data[i] * (1-prop) + b.data[i] * prop;
            }
            return result;
        }        
    }
