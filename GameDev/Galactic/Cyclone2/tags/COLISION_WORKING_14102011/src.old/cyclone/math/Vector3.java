package cyclone.math;

import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;

public class Vector3 {
public double x,y,z;

public static Vector3 GRAVITY=new Vector3(0,-10,0);
public static  Vector3 UP = new Vector3(0, 1, 0);
public static  Vector3 RIGHT = new Vector3(1, 0, 0);
public static  Vector3 OUT_OF_SCREEN = new Vector3(0, 0, 1);
public static  Vector3 X = new Vector3(0, 1, 0);
public static  Vector3 Y = new Vector3(1, 0, 0);
public static  Vector3 Z = new Vector3(0, 0, 1);

public Vector3(Vector3 v){
	x=v.x;
	y=v.y;
	z=v.z;
}

public Vector3(){
	x=0;
	y=0;
	z=0;
}

/**
 * The explicit constructor creates a vector with the given
 * components.
 */
public Vector3(double x, double y, double z){
    this.x=x;
    this.y=y;
    this.z=z;
}

public double get(int i){
	if (i==0) return x;
	if (i==1) return y;
	return z;
}

public void set(int i,double v){
	if (i==0) x=v;
	if (i==1) y=v;
	if (i==2) z=v;
}

public Point3f getPoint3f(){
	return new Point3f((float)x,(float)y,(float)z);
}

public Vector3f getVector3f(){
	return new Vector3f((float)x,(float)y,(float)z);
}

public static Vector3 localToWorld(Vector3 local, Matrix4 transform){
	return transform.transform(local);
}

public static Vector3 worldToLocal(Vector3 world, Matrix4 transform){
	/*Matrix4 inverseTransform=new Matrix4();
	inverseTransform.setInverse(transform);
	return inverseTransform.transform(world);*/
	return transform.transformInverse(world);
}

public static Vector3 localToWorldDirn(Vector3 local,Matrix4 transform){
	return transform.transformDirection(local);
}

public static Vector3 worldToLocalDirn(Vector3 world,Matrix4 transform){
	return transform.transformInverseDirection(world);
}


/*
 * Add a Vector to this Vector
 */
public void addVector(Vector3 v){
	x+=v.x;	
	y+=v.y;
	z+=v.z;
}

public Vector3 getAddVector(Vector3 v){
	return new Vector3(x+v.x,y+v.y,z+v.z);
}


/*
 * Subtracts the given vector from this.
 */
public void subVector(Vector3 v){
	x-=v.x;	
	y-=v.y;
	z-=v.z;
}

/**
 * Returns the value of the given vector subtracted from this.
 */

public Vector3 getSubVector(Vector3 v){
	return new Vector3(x-v.x,y-v.y,z-v.z);
}


/** Multiplies this vector by the given scalar. */
public void multiVectorScalar(double value){
    x *= value;
    y *= value;
    z *= value;
}

/** Returns a copy of this vector scaled the given value. */
public Vector3 getMultiVector(double value){
    return new Vector3(x*value, y*value, z*value);
}


/**
 * Calculates and returns a component-wise product of this
 * vector with the given vector.
 */
public Vector3 componentProduct(Vector3 vector) 
{
    return new Vector3(x * vector.x, y * vector.y, z * vector.z);
}

/**
 * Performs a component-wise product with the given vector and
 * sets this vector to its result.
 */
public void componentProductUpdate(Vector3 vector)
{
    x *= vector.x;
    y *= vector.y;
    z *= vector.z;
}

/**
 * Calculates and returns the vector product of this vector
 * with the given vector.
 * oper %
 */
public Vector3 getVectorProduct(Vector3 vector) 
{
    return new Vector3(y*vector.z-z*vector.y,
                   z*vector.x-x*vector.z,
                   x*vector.y-y*vector.x);
}


/**
 * Calculates and returns the scalar product of this vector
 * with the given vector.
 */
public double scalarProduct(Vector3 vector) 
{
    return x*vector.x + y*vector.y + z*vector.z;
}


/**
 * Adds the given vector to this, scaled by the given amount.
 */
public void addScaledVector(Vector3 vector, double scale)
{
    x += vector.x * scale;
    y += vector.y * scale;
    z += vector.z * scale;
}

/** Gets the magnitude of this vector. */
public double magnitude() {
    return Math.sqrt(x*x+y*y+z*z);
}

/** Gets the squared magnitude of this vector. */
public double squareMagnitude() {
    return x*x+y*y+z*z;
}

/** Limits the size of the vector to the given maximum. */
public void trim(double size)
{
    if (squareMagnitude() > size*size)
    {
        normalise();
        x *= size;
        y *= size;
        z *= size;
    }
}


/** Turns a non-zero vector into a vector of unit length. */
public void normalise()
{
    double l = magnitude();
    if (l > 0)
    {
    	multiVectorScalar(1/l);
        //(*this) *= ((real)1)/l;
    }
}

/** Returns the normalised version of a vector. */
public Vector3 unit() 
{
    Vector3 result = new Vector3(x,y,z);
    result.normalise();
    return result;
}

/** Checks if the two vectors have identical components. */
public boolean equals (Vector3 other)
{
    return x == other.x &&
        y == other.y &&
        z == other.z;
}


/**
 * Checks if this vector is component-by-component less than
 * the other.
 *
 * @note This does not behave like a single-value comparison:
 * !(a < b) does not imply (b >= a).
 */
public boolean lessThan(Vector3 other) 
{
    return x < other.x && y < other.y && z < other.z;
}

/**
 * Checks if this vector is component-by-component less than
 * the other.
 *
 * @note This does not behave like a single-value comparison:
 * !(a < b) does not imply (b >= a).
 */
public boolean highThan(Vector3 other) 
{
    return x > other.x && y > other.y && z > other.z;
}

/**
 * Checks if this vector is component-by-component less than
 * the other.
 *
 * @note This does not behave like a single-value comparison:
 * !(a <= b) does not imply (b > a).
 */
public boolean equalOrLessThan(Vector3 other) 
{
    return x <= other.x && y <= other.y && z <= other.z;
}

/**
 * Checks if this vector is component-by-component less than
 * the other.
 *
 * @note This does not behave like a single-value comparison:
 * !(a <= b) does not imply (b > a).
 */
public boolean equarlOrHigherThan(Vector3 other) 
{
    return x >= other.x && y >= other.y && z >= other.z;
}

/** Zero all the components of the vector. */
public void clear()
{
    x = y = z = 0;
}

/** Flips all the components of the vector. */
public void invert()
{
    x = -x;
    y = -y;
    x = -z;
}


public String toString(){
	return "x:"+x+" y:"+y+" z:"+z;
}

}
