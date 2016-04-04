package com.Space.controller;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.Space.controller.handlers.ActorHandler;
import com.Space.controller.handlers.BodyHandler;
import com.Space.controller.handlers.PlatformHandler;
import com.Space.controller.handlers.ShipHandler;
import com.Space.controller.handlers.StarHandler;
import com.Space.model.Actor;
import com.Space.model.Body;
import com.Space.model.Item;
import com.Space.model.Platform;
import com.Space.model.Ship;
import com.Space.model.Star;
import com.Space.model.World;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.util.geom.BufferUtils;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;


	/**
	 * This class is instantiated to initialize the games logic (behaviour) and should take care of all model changes.
	 * E.g. physics is one of its domains. As physics already need the scenegraph it is also created in this class
	 * (see {@link #getScenegraph()}.
	 * <p/>
	 * An important thing for the GameLogic is that its {@link #update} method must be called by the application frequently.
	 *
	 * @author Irrisor
	 */
	public class GameLogic {
	    public static Line line;
	    public static final ColorRGBA COLOR_ACTIVE = new ColorRGBA( 0.0f, 0.0f, 1.0f, 1 );
	    
	    static {
	        Vector3f[] points = {new Vector3f(), new Vector3f()};
	    	line = new Line( "line", points, null, null, null );
	        line.setRenderQueueMode( Renderer.QUEUE_SKIP );
	        line.setColorBuffer( 0, null );
	        line.setLightCombineMode( LightState.OFF);
	    }
	    
	    public GameLogic( World world ) {
	        this.world = world;
	        
	        // first create the root Node of the scenegraph
	        scenegraph = new Node( "World" );

	        // and initialize the physics
	        physicsSpace = PhysicsSpace.create();
	        
	        // turn off directional gravity
	        physicsSpace.setDirectionalGravity(new Vector3f());

	        
	     
	        // initalize handlers for different types of items
	        itemHandlers.put( Actor.class, new ActorHandler( this ) );
	        itemHandlers.put( Body.class, new BodyHandler( this ) );
	        itemHandlers.put( Star.class, new StarHandler( this ) );
	        itemHandlers.put( Ship.class, new ShipHandler( this ) );
	        itemHandlers.put( Platform.class, new PlatformHandler( this ) );

	        // then we have a look if there already is some stuff in the world
	        for ( Item item : world.getItems() ) {
	            registerItem( item );
	        }

	        // finally subscribe a listener for stuff added to world (and removed from the world)
	        world.getPropertyChangeSupport().addPropertyChangeListener( World.ITEMS_PROPERTY, new ItemsListener() );
	        
	        

	    }

	    public void update( float time ) {
	        getPhysicsSpace().update( time );
	        
	        for (int i=0;i<world.getItems().size();i++) {
//	    		if (world.getItems().get(i)!=null){
	    			Item item=world.getItems().get(i);
	    	

//	        //System.out.println("clasS:"+item.getClass()+" node:"+item.getNode().getClass());
	        	if (item.getNode() instanceof DynamicPhysicsNode) {
		        	DynamicPhysicsNode phys=(DynamicPhysicsNode)item.getNode();
		        	//System.out.println("item:"+item+" loc:"+phys.getLocalTranslation()+" rot:"+phys.getLocalRotation());
	        	}
	        }
    }

	    public static void lineTo( Vector3f geometryPivot ) {
	        BufferUtils.setInBuffer( geometryPivot, line.getVertexBuffer( 0 ), 1 );
	    }

	    public static void lineFrom( Vector3f from ) {
	        BufferUtils.setInBuffer( from, line.getVertexBuffer( 0 ), 0 );
	    }
  
	    
	    /**
	     * the world this game logic operates on.
	     */
	    private final World world;

	    /**
	     * @return the world this game logic operates on
	     */
	    public World getWorld() {
	        return world;
	    }

	    /**
	     * the scenegraph.
	     */
	    private final Node scenegraph;

	    /**
	     * @return the scenegraph
	     */
	    public Node getScenegraph() {
	        return scenegraph;
	    }

	    /**
	     * physics space.
	     */
	    private final PhysicsSpace physicsSpace;

	    /**
	     * @return physics space
	     */
	    public PhysicsSpace getPhysicsSpace() {
	    	
	        return physicsSpace;
	    }

	    /**
	     * Small class for listening to the items association of our world.
	     */
	    private class ItemsListener implements PropertyChangeListener {
	        /**
	         * called when an item has been added to / removed from the world
	         *
	         * @param evt evt.getNewValue() contains and added item, evt.getOldValue() a removed one
	         */
	        public void propertyChange( PropertyChangeEvent evt ) {
	            if ( evt.getNewValue() instanceof Item ) {
	                registerItem( (Item) evt.getNewValue() );
	            }
	            if ( evt.getOldValue() instanceof Item ) {
	                unregisterItem( (Item) evt.getNewValue() );
	            }
	        }
	    }

	    /**
	     * We call this method for all items we find in the world.
	     * <br> (it is package local to allow direct access from the listener)
	     *
	     * @param item new item
	     */
	    void registerItem( Item item ) {
	        // here defer to a seperate class for each kind of Item we have
	        ItemHandler handler = itemHandlers.get( item.getClass() );
	        if ( handler != null ) {
	            PhysicsNode itemPhysics = handler.createPhysicsFor( item );
	            // add it to the scenegraph
	            getScenegraph().attachChild( itemPhysics );

	            
	            // and associate it with the item
	            item.setNode( itemPhysics );
	            // apply current position
	            item.getPosition().copyTo( itemPhysics.getLocalTranslation() );
	            // and listen to position changes - the listener for this can
	            item.getPropertyChangeSupport().addPropertyChangeListener( Item.POSITION_PROPERTY, positionUpdater );
	        } else {
	            throw new IllegalArgumentException( "Unknown Item class: " + item.getClass() );
	        }
	    }

	    public interface ItemHandler {
	        PhysicsNode createPhysicsFor( Item item );
	    }

	    private Map<Class, ItemHandler> itemHandlers = new HashMap<Class, ItemHandler>();

	    private final PositionUpdater positionUpdater = new PositionUpdater();

	    private static class PositionUpdater implements PropertyChangeListener {
	        public void propertyChange( PropertyChangeEvent evt ) {
	            Item item = (Item) evt.getSource();
		    	//System.out.println("PositionUpdater:"+item);

	            item.getPosition().copyTo( item.getNode().getLocalTranslation() );
	        }
	    }

	    /**
	     * We call this method for all items that get removed from the world.
	     * <br> (it is package local to allow direct access from the listener)
	     *
	     * @param item removed item
	     */
	    void unregisterItem( Item item ) {
	        // undo the things we have done in registerItem
	        Spatial spatial = item.getNode();
	        if ( spatial instanceof PhysicsNode ) {
	            PhysicsNode node = (PhysicsNode) spatial;
	            node.setActive( false );
	        }
	        item.setNode( null );
	        item.getPropertyChangeSupport().removePropertyChangeListener( Item.POSITION_PROPERTY, positionUpdater );
	    }

	}

	/*
	 * $Log$
	 */

