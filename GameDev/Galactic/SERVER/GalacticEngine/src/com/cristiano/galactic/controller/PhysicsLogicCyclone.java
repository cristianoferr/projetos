package com.cristiano.galactic.controller;


import com.cristiano.cyclone.Cyclone;
import com.cristiano.galactic.controller.handlers.PhysicsItem;
import com.cristiano.galactic.model.Entity.Abstract.Item;





	/**
	 * This class is instantiated to initialize the games logic (behaviour) and should take care of all model changes.
	 * E.g. physics is one of its domains. As physics already need the scenegraph it is also created in this class
	 * (see {@link #getScenegraph()}.
	 * <p/>
	 * An important thing for the GameLogic is that its {@link #update} method must be called by the application frequently.
	 *
	 * @author Irrisor
	 */
	public class PhysicsLogicCyclone extends Cyclone{
		// keep the collision shapes, for deletion/cleanup
	//	private ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
	  
		
	    public PhysicsLogicCyclone(  ) {
	    	super();
	    	
	        // first create the root Node of the scenegraph
/*	        scenegraph = new Node( "World" );

	        // and initialize the physics
	        physicsSpace = PhysicsSpace.create();
	        
	        // turn off directional gravity
	        physicsSpace.setDirectionalGravity(new Vector3d());*/

	        
	    }
	    
	    
	    
	    

		public void initialize() {
			Cyclone.log("GameLogic.initPhysics()");
			super.initialize();
		}	    


	  /*  public static void lineTo( Vector3d geometryPivot ) {
	        BufferUtils.setInBuffer( geometryPivot, line.getVertexBuffer( 0 ), 1 );
	    }

	    public static void lineFrom( Vector3d from ) {
	        BufferUtils.setInBuffer( from, line.getVertexBuffer( 0 ), 0 );
	    }*/
  

	   
		public interface ItemHandler {
	    	PhysicsItem createPhysicsFor( Item item );
	    }

	    




	}

	/*
	 * $Log$
	 */

