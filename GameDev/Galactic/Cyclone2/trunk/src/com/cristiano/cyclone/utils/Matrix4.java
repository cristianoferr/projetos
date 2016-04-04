package com.cristiano.cyclone.utils;

import java.nio.FloatBuffer;

public class Matrix4 {
	/**
     * Holds a transform matrix, consisting of a rotation matrix and
     * a position. The matrix has 12 elements, it is assumed that the
     * remaining four are (0,0,0,1); producing a homogenous matrix.
     */
    
        /**
         * Holds the transform matrix data in array form.
         */
	public  double data[];

        // ... Other Matrix4 code as before ...


        /**
         * Creates an identity matrix.
         */
      public  Matrix4()
        {
    	  data=new double[12];
            data[1] = data[2] = data[3] = data[4] = data[6] =
                data[7] = data[8] = data[9] = data[11] = 0;
            data[0] = data[5] = data[10] = 1;
        }
      
      public Matrix4(Matrix4 m){
    	  this();
    	  data[1]=m.data[1];
    	  data[2]=m.data[2];
    	  data[3]=m.data[3];
    	  data[4]=m.data[4];
    	  data[5]=m.data[5];
    	  data[6]=m.data[6];
    	  data[7]=m.data[7];
    	  data[8]=m.data[8];
    	  data[9]=m.data[9];
    	  data[10]=m.data[10];
      }
      
      
      
  	public void set(int i, int j, double d) {
		int x=i*4+j;
		data[x]=d;
		
	}  
	
	public final void m00(double d)
	{
		data[0] =d;
	}
	
	public final void m01(double d)
	{
		data[1]=d;
	}
	public final void m02(double d)
	{
		data[2] =d;
	}
	
	public final void m10(double d)
	{
		 data[3] =d;
	}
	
	public final void m11(double d)
	{
		 data[4]=d;
	}
	public final void m12(double d)
	{
		 data[5] =d;
	}
	
	public final void m20(double d)
	{
		 data[6]=d;
	}
	
	public final void m21(double d)
	{
		 data[7] =d;
	}
	public final void m22(double d)
	{
		 data[8] =d;
	}
	
	
	
	public final double m00()
	{
		return ( data[0] );
	}
	
	public final double m01()
	{
		return ( data[1] );
	}
	public final double m02()
	{
		return ( data[2] );
	}
	
	public final double m10()
	{
		return ( data[3] );
	}
	
	public final double m11()
	{
		return ( data[4] );
	}
	public final double m12()
	{
		return ( data[5] );
	}
	
	public final double m20()
	{
		return ( data[6] );
	}
	
	public final double m21()
	{
		return ( data[7] );
	}
	public final double m22()
	{
		return ( data[8] );
	}
      

        /**
         * Sets the matrix to be a diagonal matrix with the given coefficients.
         */
        void setDiagonal(double a, double b, double c)
        {
            data[0] = a;
            data[5] = b;
            data[10] = c;
        }

        /**
         * Returns a matrix which is this matrix multiplied by the given
         * other matrix.
         */
        //ok
       public Matrix4 getMulti( Matrix4 o) 
        {
            Matrix4 result=new Matrix4();
            result.data[0] = (o.data[0]*data[0]) + (o.data[4]*data[1]) + (o.data[8]*data[2]);
            result.data[4] = (o.data[0]*data[4]) + (o.data[4]*data[5]) + (o.data[8]*data[6]);
            result.data[8] = (o.data[0]*data[8]) + (o.data[4]*data[9]) + (o.data[8]*data[10]);

            result.data[1] = (o.data[1]*data[0]) + (o.data[5]*data[1]) + (o.data[9]*data[2]);
            result.data[5] = (o.data[1]*data[4]) + (o.data[5]*data[5]) + (o.data[9]*data[6]);
            result.data[9] = (o.data[1]*data[8]) + (o.data[5]*data[9]) + (o.data[9]*data[10]);

            result.data[2] = (o.data[2]*data[0]) + (o.data[6]*data[1]) + (o.data[10]*data[2]);
            result.data[6] = (o.data[2]*data[4]) + (o.data[6]*data[5]) + (o.data[10]*data[6]);
            result.data[10] = (o.data[2]*data[8]) + (o.data[6]*data[9]) + (o.data[10]*data[10]);

            result.data[3] = (o.data[3]*data[0]) + (o.data[7]*data[1]) + (o.data[11]*data[2]) + data[3];
            result.data[7] = (o.data[3]*data[4]) + (o.data[7]*data[5]) + (o.data[11]*data[6]) + data[7];
            result.data[11] = (o.data[3]*data[8]) + (o.data[7]*data[9]) + (o.data[11]*data[10]) + data[11];

            return result;
        }

        /**
         * Transform the given vector by this matrix.
         *
         * @param vector The vector to transform.
         */
       public Vector3 getMulti( Vector3 vector) 
        {
            return new Vector3(
                vector.x * data[0] +
                vector.y * data[1] +
                vector.z * data[2] + data[3],

                vector.x * data[4] +
                vector.y * data[5] +
                vector.z * data[6] + data[7],

                vector.x * data[8] +
                vector.y * data[9] +
                vector.z * data[10] + data[11]
            );
        }

        /**
         * Transform the given vector by this matrix.
         *
         * @param vector The vector to transform.
         */
       public Vector3 transform( Vector3 vector) 
        {
            return this.getMulti(vector);
        }

        /**
         * Returns the determinant of the matrix.
         */
       public double getDeterminant() 
       {
           return data[8]*data[5]*data[2]+
               data[4]*data[9]*data[2]+
               data[8]*data[1]*data[6]-
               data[0]*data[9]*data[6]-
               data[4]*data[1]*data[10]+
               data[0]*data[5]*data[10];
       }

        /**
         * Sets the matrix to be the inverse of the given matrix.
         *
         * @param m The matrix to invert and use to set this.
         */
        
        public void setInverse(Matrix4 m)
        {
            // Make sure the determinant is non-zero.
            double det = getDeterminant();
            if (det == 0) return;
            det = ((double)1.0)/det;

            data[0] = (-m.data[9]*m.data[6]+m.data[5]*m.data[10])*det;
            data[4] = (m.data[8]*m.data[6]-m.data[4]*m.data[10])*det;
            data[8] = (-m.data[8]*m.data[5]+m.data[4]*m.data[9]* m.data[15])*det;

            data[1] = (m.data[9]*m.data[2]-m.data[1]*m.data[10])*det;
            data[5] = (-m.data[8]*m.data[2]+m.data[0]*m.data[10])*det;
            data[9] = (m.data[8]*m.data[1]-m.data[0]*m.data[9]* m.data[15])*det;

            data[2] = (-m.data[5]*m.data[2]+m.data[1]*m.data[6]* m.data[15])*det;
            data[6] = (+m.data[4]*m.data[2]-m.data[0]*m.data[6]* m.data[15])*det;
            data[10] = (-m.data[4]*m.data[1]+m.data[0]*m.data[5]* m.data[15])*det;

            data[3] = (m.data[9]*m.data[6]*m.data[3]
                       -m.data[5]*m.data[10]*m.data[3]
                       -m.data[9]*m.data[2]*m.data[7]
                       +m.data[1]*m.data[10]*m.data[7]
                       +m.data[5]*m.data[2]*m.data[11]
                       -m.data[1]*m.data[6]*m.data[11])*det;
            data[7] = (-m.data[8]*m.data[6]*m.data[3]
                       +m.data[4]*m.data[10]*m.data[3]
                       +m.data[8]*m.data[2]*m.data[7]
                       -m.data[0]*m.data[10]*m.data[7]
                       -m.data[4]*m.data[2]*m.data[11]
                       +m.data[0]*m.data[6]*m.data[11])*det;
            data[11] =(m.data[8]*m.data[5]*m.data[3]
                       -m.data[4]*m.data[9]*m.data[3]
                       -m.data[8]*m.data[1]*m.data[7]
                       +m.data[0]*m.data[9]*m.data[7]
                       +m.data[4]*m.data[1]*m.data[11]
                       -m.data[0]*m.data[5]*m.data[11])*det;
        }
        /** Returns a new matrix containing the inverse of this matrix. */
        public Matrix4 inverse() 
        {
            Matrix4 result=new Matrix4();
            result.setInverse(result);
            return result;
        }

        /**
         * Inverts the matrix.
         */
        void invert()
        {
        	Matrix4 m=new Matrix4(this); 
            setInverse(m);
        }

        /**
         * Transform the given direction vector by this matrix.
         *
         * @note When a direction is converted between frames of
         * reference, there is no translation required.
         *
         * @param vector The vector to transform.
         */
        public Vector3 transformDirection( Vector3 vector) 
        {
            return new Vector3(
                vector.x * data[0] +
                vector.y * data[1] +
                vector.z * data[2],

                vector.x * data[4] +
                vector.y * data[5] +
                vector.z * data[6],

                vector.x * data[8] +
                vector.y * data[9] +
                vector.z * data[10]
            );
        }

        /**
         * Transform the given direction vector by the
         * transformational inverse of this matrix.
         *
         * @note This function relies on the fact that the inverse of
         * a pure rotation matrix is its transpose. It separates the
         * translational and rotation components, transposes the
         * rotation, and multiplies out. If the matrix is not a
         * scale and shear free transform matrix, then this function
         * will not give correct results.
         *
         * @note When a direction is converted between frames of
         * reference, there is no translation required.
         *
         * @param vector The vector to transform.
         */
       public Vector3 transformInverseDirection( Vector3 vector) 
        {
            return new Vector3(
                vector.x * data[0] +
                vector.y * data[4] +
                vector.z * data[8],

                vector.x * data[1] +
                vector.y * data[5] +
                vector.z * data[9],

                vector.x * data[2] +
                vector.y * data[6] +
                vector.z * data[10]
            );
        }

        /**
         * Transform the given vector by the transformational inverse
         * of this matrix.
         *
         * @note This function relies on the fact that the inverse of
         * a pure rotation matrix is its transpose. It separates the
         * translational and rotation components, transposes the
         * rotation, and multiplies out. If the matrix is not a
         * scale and shear free transform matrix, then this function
         * will not give correct results.
         *
         * @param vector The vector to transform.
         */
       //ok
       public Vector3 transformInverse( Vector3 vector) 
        {
            Vector3 tmp = new Vector3(vector);
            tmp.x -= data[3];
            tmp.y -= data[7];
            tmp.z -= data[11];
            return new Vector3(
                tmp.x * data[0] +
                tmp.y * data[4] +
                tmp.z * data[8],

                tmp.x * data[1] +
                tmp.y * data[5] +
                tmp.z * data[9],

                tmp.x * data[2] +
                tmp.y * data[6] +
                tmp.z * data[10]
            );
        }

        /**
         * Gets a vector representing one axis (i.e. one column) in the matrix.
         *
         * @param i The row to return. Row 3 corresponds to the position
         * of the transform matrix.
         *
         * @return The vector.
         */
       public Vector3 getAxisVector(int i) 
        {
            return new Vector3(data[i], data[i+4], data[i+8]);
        }

        /**
         * Sets this matrix to be the rotation matrix corresponding to
         * the given quaternion.
         */
       public void setOrientationAndPos( Quaternion q,  Vector3 pos)
        {
            data[0] = 1 - (2*q.j*q.j + 2*q.k*q.k);
            data[1] = 2*q.i*q.j + 2*q.k*q.r;
            data[2] = 2*q.i*q.k - 2*q.j*q.r;
            data[3] = pos.x;

            data[4] = 2*q.i*q.j - 2*q.k*q.r;
            data[5] = 1 - (2*q.i*q.i  + 2*q.k*q.k);
            data[6] = 2*q.j*q.k + 2*q.i*q.r;
            data[7] = pos.y;

            data[8] = 2*q.i*q.k + 2*q.j*q.r;
            data[9] = 2*q.j*q.k - 2*q.i*q.r;
            data[10] = 1 - (2*q.i*q.i  + 2*q.j*q.j);
            data[11] = pos.z;
        }

        /**
         * Fills the given array with this transform matrix, so it is
         * usable as an open-gl transform matrix. OpenGL uses a column
         * major format, so that the values are transposed as they are
         * written.
         */
       public void fillGLArray(FloatBuffer fb) 
        {
    	   
    	   fb.put(0,(float)data[0]);
    	   fb.put(1,(float)data[4]);
    	   fb.put(2,(float)data[8]);;
    	   fb.put(3,(float)0);

    	   fb.put(4,(float)data[1]);
    	   fb.put(5,(float)data[5]);
    	   fb.put(6,(float)data[9]);
    	   fb.put(7,(float)0);

    	   fb.put(8,(float)data[2]);
    	   fb.put(9,(float)data[6]);
    	   fb.put(10,(float)data[10]);
    	   fb.put(11, (float)0);

    	   fb.put(12, (float)data[3]);
    	   fb.put(13,(float)data[7]);
    	   fb.put(14,(float)data[11]);
    	   fb.put(15,(float)1);
        }
    }

