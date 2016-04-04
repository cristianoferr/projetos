package com.cristiano.cyclone.utils;

public interface TupleInterface< T extends TupleNd >
{
    /**
     * @return Is this tuple a read-only one?
     */
    public boolean isReadOnly();
    
    /**
     * Marks this tuple non-dirty.
     * Any value-manipulation will mark it dirty again.
     * 
     * @return the old value
     */
    public boolean setClean();
    
    /**
     * @return This tuple's dirty-flag
     */
    public boolean isDirty();
    
    /**
     * @return this Tuple's size().
     */
    public int getSize();
    
    /**
     * Sets the i-th value of this tuple.
     */
    public void set( int i, double v );
    
    /**
     * Sets the i-th value of this tuple.
     */
    public double get( int i );
    
    /**
     * Sets all values of this TupleNd to f.
     * 
     * @param f
     */
    public void fill( double f );
    
    /**
     * Adds v to this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     */
    public void add( int i, double v );
    
    /**
     * Subtracts v of this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     */
    public void sub( int i, double v );
    
    /**
     * Multiplies v to this tuple's i'th value.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     */
    public void mul( int i, double v );
    
    /**
     * Multiplies all components of this tuple with v.
     * 
     * @param v modification amount
     */
    public void mul( double v );
    
    /**
     * Divides this tuple's i'th value by v.
     * 
     * @param i the index of the value to modify
     * @param v modification amount
     */
    public void div( int i, double v );
    
    /**
     * Divides all components of this tuple by v.
     * 
     * @param v modification amount
     */
    public void div( double v );
    
    /**
     * Sets all values of this Tuple to the specified ones.
     * 
     * @param values the values array (must be at least size getSize())
     */
    public void set( double[] values );
    
    /**
     * Sets all three values of this Tuple to the specified ones.
     * 
     * @param tuple the tuple to be copied
     */
    public void set( TupleNd tuple );
    
    /**
     * Writes all values of this Tuple to the specified buffer.
     * 
     * @param buffer the buffer array to write the values to
     */
    public void get( double[] buffer );
    
    /**
     * Writes all values of this Tuple to the specified buffer Tuple.
     * 
     * @param buffer the buffer Tuple to write the values to
     */
    public void get( TupleNd buffer );
    
    /**
     * Sets all components to zero.
     */
    public void setZero();
    
    /**
     * Negates the value of this vector in place.
     */
    public void negate();
    
    /**
     * Sets the value of this tuple to the negation of tuple that.
     * 
     * @param tuple the source vector
     */
    public void negate( T tuple );
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     */
    public void absolute();
    
    /**
     * Sets each component of the tuple parameter to its absolute value and
     * places the modified values into this tuple.
     * 
     * @param tuple the source tuple, which will not be modified
     */
    public void absolute( T tuple );
    
    /**
     * Sets the value of this tuple to the vector sum of tuples t1 and t2.
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     */
    public void add( T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector sum of itself and tuple t1.
     * 
     * @param tuple2 the other tuple
     */
    public void add( T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector difference of tuple t1 and t2
     * (this = t1 - t2).
     * 
     * @param tuple1 the first tuple
     * @param tuple2 the second tuple
     */
    public void sub( T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the vector difference of itself and tuple
     * t1 (this = this - t1).
     * 
     * @param tuple2 the other tuple
     */
    public void sub( T tuple2 );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1.
     * 
     * @param factor the scalar value
     * @param tuple the source tuple
     */
    public void scale( double factor, T tuple );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself.
     * 
     * @param factor the scalar value
     */
    public void scale( double factor );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of tuple t1 and
     * then adds tuple t2 (this = s*t1 + t2).
     * 
     * @param factor the scalar value
     * @param tuple1 the tuple to be multipled
     * @param tuple2 the tuple to be added
     */
    public void scaleAdd( double factor, T tuple1, T tuple2 );
    
    /**
     * Sets the value of this tuple to the scalar multiplication of itself and
     * then adds tuple t1 (this = s*this + t1).
     * 
     * @param factor the scalar value
     * @param tuple2 the tuple to be added
     */
    public void scaleAdd( double factor, T tuple2 );
    
    /**
     * Clamps the minimum value of this tuple to the min parameter.
     * 
     * @param min the lowest value in this tuple after clamping
     */
    public void clampMin( double min );
    
    /**
     * Clamps the maximum value of this tuple to the max parameter.
     * 
     * @param max the highest value in the tuple after clamping
     */
    public void clampMax( double max );
    
    /**
     * Clamps this tuple to the range [min, max].
     * 
     * @param min the lowest value in this tuple after clamping
     * @param max the highest value in this tuple after clamping
     */
    public void clamp( double min, double max );
    
    /**
     * Clamps the tuple parameter to the range [min, max] and places the values
     * into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     */
    public void clamp( double min, double max, T tuple );
    
    /**
     * Clamps the minimum value of the tuple parameter to the min parameter and
     * places the values into this tuple.
     * 
     * @param min the lowest value in the tuple after clamping
     * @parm that the source tuple, which will not be modified
     */
    public void clampMin( double min, T tuple );
    
    /**
     * Clamps the maximum value of the tuple parameter to the max parameter and
     * places the values into this tuple.
     * 
     * @param max the highest value in the tuple after clamping
     * @param tuple the source tuple, which will not be modified
     */
    public void clampMax( double max, T tuple );
    
    /**
     * Rounds this tuple to the given number of decimal places.
     * 
     * @param decPlaces
     */
    public void round( T tuple, int decPlaces );
    
    /**
     * Rounds this tuple to the given number of decimal places.
     * 
     * @param decPlaces
     */
    public void round( int decPlaces );
    
    /**
     * Linearly interpolates between this tuple and tuple t2 and places the
     * result into this tuple: this = (1 - alpha) * this + alpha * t1.
     * 
     * @param t2 the first tuple
     * @param alpha the alpha interpolation parameter
     */
    public void interpolate( T t2, double alpha );
    
    /**
     * Linearly interpolates between tuples t1 and t2 and places the result into
     * this tuple: this = (1 - alpha) * t1 + alpha * t2.
     * 
     * @param t1 the first tuple
     * @param t2 the second tuple
     * @param alpha the alpha interpolation parameter
     */
    public void interpolate( T t1, T t2, double alpha );
    
    /**
     * Returns true if the L-infinite distance between this vector and vector v1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[abs(x1-x2), abs(y1-y2), . . . ].
     * 
     * @param v2 The vector to be compared to this vector
     * @param epsilon the threshold value
     */
    public boolean epsilonEquals( T v2, double epsilon );
}