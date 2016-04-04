package com.cristiano.cyclone.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple collection used to store free objects which we need to work with
 * frequently. This is to solve some of the issues with garbage collection of
 * frequently used objects
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class ObjectPool< T >
{
    private List< T > objects;
    private int n;
    
    public int getSize()
    {
        return( n );
    }
    
    /**
     * Creates a new instance of a pooled object.
     * 
     * @return the new object instance
     */
    protected abstract T newInstance();
    
    public synchronized T alloc()
    {
        if ( n > 0 )
        {
            T o = objects.remove( --n );
            
            return( o );
        }
        else
        {
            return( newInstance() );
        }
    }
    
    public synchronized void free( T o )
    {
        if ( o == null )
            return;
        
        objects.add( o );
        n++;
    }
    
    public ObjectPool( int initialSize )
    {
        this.objects = new ArrayList<T>( initialSize );
        this.n = 0;
    }
}