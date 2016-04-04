package com.cristiano.cyclone.utils;

import org.openmali.FastMathd;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;



public class Vector3 {
public double x,y,z;

public static final Vector3 GRAVITY=new Vector3(0,-10,0);
public static  final Vector3 UP = new Vector3(0, 1, 0);
public static  final Vector3 DOWN = new Vector3(0, 1, 0);
public static  final Vector3 RIGHT = new Vector3(1, 0, 0);
public static  final Vector3 LEFT = new Vector3(-1, 0, 0);
public static  final Vector3 FRONT = new Vector3(0, 0, 1);
public static  final Vector3 BACK = new Vector3(0, 0, -1);
public static  final Vector3 OUT_OF_SCREEN = new Vector3(0, 0, 1);
public static  final Vector3 ZERO = new Vector3(0, 0, 0);
public static  final Vector3 X = new Vector3(1,0, 0);
public static  final Vector3 Y = new Vector3(0, 1, 0);
public static  final Vector3 Z = new Vector3(0, 0, 1);

private static final Vector3Pool POOL = new Vector3Pool( 128 );

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

public Vector3(double x, double y){
    this.x=x;
    this.y=y;
    this.z=0;
}


public Vector3 clone(){
	Vector3 c=new Vector3(x,y,z);
	return c;
}


public Vector3 getRotateBy(Vector3 center,Vector3 axis,double angle){
	Vector3 ret=clone();
	
	angle=Math.toRadians(angle);
	
	if (axis.x>0){
		ret.x = Math.cos(angle) * (x-center.x) - Math.sin(angle) * (y-center.y) + center.x;
		ret.y = Math.sin(angle) * (x-center.x) + Math.cos(angle) * (y-center.y) + center.y;
	}
	if (axis.y>0){
		ret.y = Math.cos(angle) * (y-center.y) - Math.sin(angle) * (z-center.z) + center.y;
		ret.z = Math.sin(angle) * (y-center.y) + Math.cos(angle) * (z-center.z) + center.z;
	}
	if (axis.z>0){
		ret.z = Math.cos(angle) * (z-center.z) - Math.sin(angle) * (x-center.x) + center.z;
		ret.x = Math.sin(angle) * (z-center.z) + Math.cos(angle) * (x-center.x) + center.x;
	}
	return ret;
}

public void rotateBy(Vector3 center,Vector3 axis,double angle){
	double nx=0,ny=0,nz=0;	
	angle=Math.toRadians(angle);
	
	if (axis.x>0){
		nx = Math.cos(angle) * (x-center.x) - Math.sin(angle) * (y-center.y) + center.x;
		ny = Math.sin(angle) * (x-center.x) + Math.cos(angle) * (y-center.y) + center.y;
	}
	if (axis.y>0){
		ny = Math.cos(angle) * (y-center.y) - Math.sin(angle) * (z-center.z) + center.y;
		nz = Math.sin(angle) * (y-center.y) + Math.cos(angle) * (z-center.z) + center.z;
	}
	if (axis.z>0){
		nz = Math.cos(angle) * (z-center.z) - Math.sin(angle) * (x-center.x) + center.z;
		nx = Math.sin(angle) * (z-center.z) + Math.cos(angle) * (x-center.x) + center.x;
	}
	
	x=nx;
	y=ny;
	z=nz;
}


/**
 * Computes the dot product of the this vector and vector v2.
 * 
 * @param v2 the other vector
 */
public final double dot( Vector3 v2 )
{
    return( this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ() );
}



public Vector3 subtract(Vector3 x){
	Vector3 a = new Vector3();
    for (int i=0;i<3;i++){
    a.set(i,get(i) - x.get(i));
    }
    return a;
  }



/**
 * Allocates an Vector3f instance from the pool.
 */
public static Vector3 fromPool()
{
    return( POOL.alloc() );
}

/**
 * Allocates an Vector3f instance from the pool.
 */
public static Vector3 fromPool( float x, float y, float z )
{
    return( POOL.alloc( x, y, z ) );
}

/**
 * Stores the given Vector3f instance in the pool.
 * 
 * @param o
 */
public static void toPool( Vector3 o )
{
    POOL.free( o );
}

public static Vector3 randomVector(Vector3 minPos, Vector3 maxPos){
	return new Vector3(minPos.x+(minPos.x-maxPos.x)*Math.random(),
			minPos.y+(minPos.y-maxPos.y)*Math.random(),
			minPos.z+(minPos.z-maxPos.z)*Math.random());
}




public Vector3 cross(Vector3 other) {
    return cross(other.x, other.y, other.z);
}


public Vector3 cross(double otherX, double otherY, double otherZ) {
	double resX = ((y * otherZ) - (z * otherY));
	double resY = ((z * otherX) - (x * otherZ));
	double resZ = ((x * otherY) - (y * otherX));
    return new Vector3(resX, resY, resZ);
}

/**
 * Returns the angle in radians between this vector and the vector
 * parameter; the return value is constrained to the range [0, PI].
 * 
 * @param v2 the other vector
 * @return the angle in radians in the range [0, PI]
 */
public final double angle( Vector3 v2 )
{
    // return (double)Math.acos(dot(v1)/v1.length()/v.length());
    // Numerically, near 0 and PI are very bad condition for acos.
    // In 3-space, |atan2(sin,cos)| is much stable.
    
    final double xx = this.getX() * v2.getZ() - this.getZ() * v2.getY();
    final double yy = this.getZ() * v2.getX() - this.getX() * v2.getZ();
    final double zz = this.getX() * v2.getY() - this.getY() * v2.getX();
    final double cross = FastMathd.sqrt( (xx * xx + yy * yy + zz * zz) );
    
    return( Math.abs( FastMathd.atan2( cross, dot( v2 ) ) ) );
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

public void set(double x,double y,double z){
	this.x=x;
	this.y=y;
	this.z=z;
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
public Vector3 addVector(Vector3 v){
	x+=v.x;	
	y+=v.y;
	z+=v.z;
	return this;
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



/** this = a cross b. NOTE: "this" must be a different vector than
both a and b. */
public static Vector3 cross(Vector3 a, Vector3 b) {
	return new Vector3(a.y * b.z - a.z * b.y,a.z * b.x - a.x * b.z,a.x * b.y - a.y * b.x);
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
public Vector3 getMultiVector(Vector3 vector) 
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
/*	if (vector==null) {
		return new Vector3(this);
	}*/
    return new Vector3(y*vector.z-z*vector.y,
                   z*vector.x-x*vector.z,
                   x*vector.y-y*vector.x);
}


/**
 * Calculates and returns the scalar product of this vector
 * with the given vector.
 * oper *
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
    return Math.sqrt(squareMagnitude());
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

public void normalise(double v)
{
    double l = magnitude();
    if (l > 0)
    {
    	multiVectorScalar(v/l);
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
	if (other==null)return false;
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
	return "x:"+MathUtils.Round(x, 3)+" y:"+MathUtils.Round(y, 3)+" z:"+MathUtils.Round(z, 3);
}

public double getX() {
	return x;
}

public float getXf() {
	return (float)x;
}
public float getYf() {
	return (float)y;
}
public float getZf() {
	return (float)z;
}

public float getXf(double scale) {
	return (float)(x*scale);
}
public float getYf(double scale) {
	return (float)(y*scale);
}
public float getZf(double scale) {
	return (float)(z*scale);
}


public void setX(double x) {
	this.x = x;
}

public double getY() {
	return y;
}

public void setY(double y) {
	this.y = y;
}

public double getZ() {
	return z;
}

public void setZ(double z) {
	this.z = z;
}

public boolean isEqual(Vector3 vec) {
	
	return equals(vec);
}

public void setZero() {
	set( 0, 0, 0 );
	
}

public Vector3 normalize() {
	normalise();
	return this;
}

public void copy(Vector3 position) {
	this.x=position.x;
	this.y=position.y;
	this.z=position.z;
	
}

}
