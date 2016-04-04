package com.cristiano.cyclone.utils;


public class Point3Pool extends ObjectPool< Point3d >
	{
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected Point3d newInstance()
	    {
	        return( new Point3d() );
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public Point3d alloc()
	    {
	        Point3d o = super.alloc();
	        
	        o.setZero();
	        
	        return( o );
	    }
	    
	    public Point3d alloc( double x, double y, double z )
	    {
	        Point3d o = super.alloc();
	        
	        o.set( x, y, z );
	        
	        return( o );
	    }
	    
	    public Point3Pool( int initialSize )
	    {
	        super( initialSize );
	    }
	}
