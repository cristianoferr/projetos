package com.cristiano.cyclone.utils;

import org.openmali.pooling.ObjectPool;


/**
 * An instance pool for Vector3f instances.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class Vector3Pool extends ObjectPool< Vector3 >
{
	
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected Vector3 newInstance()
    {
        return( new Vector3() );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Vector3 alloc()
    {
        Vector3 o = super.alloc();
        
        o.setZero();
        
        return( o );
    }
    
    public Vector3 alloc( double x, double y, double z )
    {
        Vector3 o = super.alloc();
        
        o.set( x, y, z );
        
        return( o );
    }
    
    public Vector3Pool( int initialSize )
    {
        super( initialSize );
    }
}