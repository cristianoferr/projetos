package com.cristiano.galactic.controller;



import java.util.Date;
import java.util.Vector;

import com.cristiano.cyclone.utils.ClockWatch;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;






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
		/**
	     * the world this game logic operates on.
	     */
	    private Sistema world;
	    
		ItemsController itemsController;
		
		private PhysicsLogicCyclone physicsSpace;
		
		Vector<Item> itemstoRemove=new Vector<Item>();
		Vector<Item> players=new Vector<Item>();
		private Date lastUpdate=new Date();
		
	    public GameLogic() {
	    	super();
	    	
	        // first create the root Node of the scenegraph
/*	        scenegraph = new Node( "World" );

	        
	        
	        // turn off directional gravity
	        physicsSpace.setDirectionalGravity(new Vector3d());*/
	    	
	    	// and initialize the physics
	        physicsSpace = new PhysicsLogicCyclone();
	    	
	        physicsSpace.initialize();
	    	
	        
	     
	        
	        
	    /*    itemHandlers.put( Actor.class, new ActorHandler( this ) );
	        itemHandlers.put( Body.class, new BodyHandler( this ) );
	        itemHandlers.put( Star.class, new StarHandler( this ) );
	       
	        itemHandlers.put( Platform.class, new PlatformHandler( this ) );*/

	       
	    }
	    
	    public Item getEntityByID(int id){
			return world.getEntityByID(id);
		}

		public void initialize(Sistema world) {
			this.world=world;
			itemsController=new ItemsController(this,world.getDataManager());
			
			// then we have a look if there already is some stuff in the world
			for (int i=0;i<world.size();i++){
				itemsController.registerItem( world.getItem(i) );
			}

	        // finally subscribe a listener for stuff added to world (and removed from the world)
	        world.addPropertyChangeListener( itemsController );
	        
	        
	        //world.start();
		}
	    
	    public void connectPlayer(Item player){
	    	players.add(player);
	    }
	    
	    public void disconnectPlayer(Item player){
	    	players.remove(player);
	    }
	    
	    
	    public void addView(ItemsViewManagerAbstract view) {
	        itemsController.addView(view);
	       
	    }
	    
	    
	   /* public void shootSph(Coord pos,Vector3 linVel){
	    	SphereItem item=new SphereItem(getWorld().getDataManager(),new GeomSphere(1),  "Shoot");
	    	item.getCoord().set(pos.getX()+linVel.x, pos.getY()+linVel.y, pos.getZ()+linVel.z);
	    	//item.setLinVel(linVel); 
	    	getWorld().addEntity(item);
	    }*/
	    
	    

	    public void update(){
	    	ClockWatch.startClock("GameLocic.Update");
	    	
	    	
	    	
			Date now=new Date();
			int time=(int)(now.getTime()-lastUpdate.getTime());
			if (time<=0) time=1;
			lastUpdate=now;

			//System.out.println("gameLogic.update()"+time);
	       // Find the duration of the last frame in seconds
	       //float duration = (float)TimingData::get().lastFrameDuration * 0.001f;
	       //if (duration <= 0.0f) return;

	       // Update the physics of each RigidBody in turn
			float duration=(float)time/1000;
			world.turn();
			update(duration);
			
			ClockWatch.stopClock("GameLocic.Update");
		}
	    
	 

	    public void update( float time ) {
	    	
	    	//System.out.println(time);
			
			try {
				itemstoRemove.clear();
			      
				for (int i=0;i<world.size();i++){
					Item item=world.getItem(i);
					item.turn(time);
					if (!item.isAlive()) itemstoRemove.add(item);
				}
				
				//if (!world.isReady()) return;
				getPhysicsSpace().update(time);
				  
				for ( Item item : itemstoRemove ) {
					world.removeEntity(item);
				}
			} catch (Exception e) {
				Galactic.log(e.getMessage());
				e.printStackTrace();
			}

    }

	  /*  public static void lineTo( Vector3d geometryPivot ) {
	        BufferUtils.setInBuffer( geometryPivot, line.getVertexBuffer( 0 ), 1 );
	    }

	    public static void lineFrom( Vector3d from ) {
	        BufferUtils.setInBuffer( from, line.getVertexBuffer( 0 ), 0 );
	    }*/
  
	  
	   

	    

	    

	    /**
	     * @return the world this game logic operates on
	     */
	    public Sistema getWorld() {
	        return world;
	    }

		public void setPhysicsSpace(PhysicsLogicCyclone physicsSpace) {
			this.physicsSpace = physicsSpace;
		}

		public PhysicsLogicCyclone getPhysicsSpace() {
			return physicsSpace;
		}

		public ItemsController getItemsController() {
		
			return itemsController;
		} 
	 
	    
 
	}	   
	/*
	 * $Log$
	 */

