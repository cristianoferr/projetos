package com.cristiano.galactic.controller;


import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.PhysicsLogicCyclone.ItemHandler;
import com.cristiano.galactic.controller.handlers.BodyHandler;
import com.cristiano.galactic.controller.handlers.PhysicsItem;
import com.cristiano.galactic.controller.handlers.ShipHandler;
import com.cristiano.galactic.controller.handlers.StationHandler;
import com.cristiano.galactic.model.Entity.Bullet;
import com.cristiano.galactic.model.Entity.PlanetM;
import com.cristiano.galactic.model.Entity.Ship;
import com.cristiano.galactic.model.Entity.SphereItem;
import com.cristiano.galactic.model.Entity.Star;
import com.cristiano.galactic.model.Entity.Station;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryItem;
import com.cristiano.galactic.model.Entity.actuators.ItemSlot;
import com.cristiano.galactic.model.Entity.control.Control;
import com.cristiano.galactic.model.bt.queries.QueryCentral;
import com.cristiano.galactic.model.containers.DataManager;
import com.cristiano.galactic.model.enums.ControlEnum;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;


/**
 * Small class for listening to the items association of our world.
 */
public class ItemsController extends AbstractController{

	
	private Map<Class, ItemHandler> itemHandlers = new HashMap<Class, ItemHandler>();
	
	

    
    /*
     * JBT will use this to send/receive commands/infos/etc
     */
    private static ItemsController itemsController;
    
	GameLogic gameLogic;


	private DataManager dataManager;
	
	public ItemsController(GameLogic gameLogic,DataManager dataManager){
		this.gameLogic=gameLogic;
		this.dataManager=dataManager;
		// initalize handlers for different types of items
        itemHandlers.put( Ship.class, new ShipHandler( gameLogic ) );
        itemHandlers.put( Station.class, new StationHandler( gameLogic ) );
        itemHandlers.put( Star.class, new BodyHandler( gameLogic ) );
        itemHandlers.put( PlanetM.class, new BodyHandler( gameLogic ) );
        itemHandlers.put( SphereItem.class, new BodyHandler( gameLogic ) );
        itemHandlers.put( Bullet.class, new BodyHandler( gameLogic ) );
        itemsController=this;
	}
	
	
	
	
	
	 public void propertyChange( PropertyChangeEvent evt ) {
	        	//System.out.println("ItemsController.propertyChange: "+evt.getPropertyName()+" S:"+evt.getSource()+" nv:"+evt.getNewValue());
		 super.propertyChange(evt);
		 	//Item item = (Item) evt.getSource();
	        	/*if (evt.getPropertyName().equals("coord")){
	        		
	        		//EvtUpdate evtU=new EvtUpdate((Item)evt.getSource(),evt.getPropertyName(),evt.getNewValue());
	        		//getWorld().addMessage(evtU);
	        	}*/
	        	if (evt.getPropertyName().equals(MVCProperties.ITEMS_PROPERTY)){
		            if ( evt.getNewValue() instanceof Item ) {
		            	registerItem( (Item) evt.getNewValue() );
		            }
		            if ( evt.getOldValue() instanceof Item ) {
		            	unregisterItem( (Item) evt.getOldValue() );
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
//	    	System.out.println("ItemsController.registerItem: "+item);
	        // here defer to a seperate class for each kind of Item we have
	        ItemHandler handler = itemHandlers.get( item.getClass() );
	        if ( handler != null ) {
	        	PhysicsItem itemPhysics = handler.createPhysicsFor( item );
	        	gameLogic.getPhysicsSpace().registerBody(itemPhysics);
	        	// and associate it with the item
	            item.setBody( itemPhysics );
	            // apply current position
	            //item.getPosition().copyTo( itemPhysics.getLocalTranslation() );
	            // and listen to position changes - the listener for this can
	         
	        /*    for (AbstractView view: getRegisteredViews()) {
	            	view.registerItem(item);
	            }*/
	            item.addPropertyChangeListener(  this );
	        } else {
	            throw new IllegalArgumentException( "Unknown Item class: " + item.getClass() );
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
	     /*   Spatial spatial = item.getNode();
	        if ( spatial instanceof RigidBody ) {
	            RigidBody node = (RigidBody) spatial;
	            node.setActive( false );
	        }*/
	    //	Galactic.printLog("ItemsController.UnregisterItem:"+item,true);
	    	item.setAlive(false);
	    	item.getBody().setAlive(false); 
	        item.setBody( null );
	        item.removePropertyChangeListener( this );
	    }


	 
	    
	  /*  private final PositionUpdater positionUpdater = new PositionUpdater();

	    private static class PositionUpdater implements PropertyChangeListener {
	        public void propertyChange( PropertyChangeEvent evt ) {
	            Item item = (Item) evt.getSource();

	      //      System.out.println("PositionUpdater:"+item);

	      //      item.getCoord().copyTo( item.getNode() );
	        }
	    }	*/   	    
	 
	 /**From this point on: View->Controller->Model*/
	 
		public Vector3 getPointInWorldSpace(Item item,Vector3 vec){
			return item.getBody().getPointInWorldSpace(vec);
		}
	    
		public Vector3 getTransformDirection(Item item,Vector3 vec){
			return item.getTransformDirection(vec);
		}

		public void activateControl(ArtificialEntity item,Enum control,double intensity,double time){
			//System.out.println("IC: control="+control+" int:"+intensity);
			//Galactic.printDebug("IC: control="+control+" int:"+intensity);
			Control controlLib=getDataManager().getControlManager().getControl(control);
			Control ctl=new Control(control,controlLib.getSlotGroupingName(),intensity);
			ctl.setTime(time);
			item.addActiveControl(ctl);
		}
		
		
		
		public void activateControl(Item item,Enum control){
			item.activateKeyGroup(control);
//			Galactic.printDebug("IC: control="+control);
		}
		
		public void deactivateControl(Item item,Enum control){
			item.deactivateKeyGroup(control);
			//Galactic.printDebug("IC: control="+control);
		}
		
		public void activateControl(ArtificialEntity item,ControlEnum ctl,MemoryItem target){
			Control control=new Control(getDataManager().getControlManager().getControl(ctl));
			control.setTarget(target);
			control.setIntensity(1);
			
			item.addActiveControl(control);

		}
		
		
		public Vector3 getCameraPosition(ArtificialEntity item){
    		Vector<ItemSlot> v=item.getSlotsFromWareGroup(item.getDataManager().getWareManager().getWareGroup(Consts.GRP_CAMERA));
    		if (v.size()>0){
    			return new Vector3(v.get(0).getAbsPos());
    		}
			return null;
		}
		
	    public Item createArtificialEntity(String shipWare,String name, double px,double py,double pz){
	    	Item item=gameLogic.getWorld().createEntityFromWare(Item.getNextID(),shipWare, name,new Vector3(px,py,pz));
	    	return item;
	    	
	    }
	    

	    
	 /**
	     * Change the text element string in the model
	     * @param newText The new text element string
	     */
	    public void changeShipWare(Item item,String newWare) {
	        setModelProperty(item,MVCProperties.SHIPWARE_PROPERTY, newWare);                 
	    }
	    
	    public void changeShipPosition(Item item,Vector3 newPos) {
	        setModelProperty(item,MVCProperties.COORD_PROPERTY, newPos);                 
	    }
	    
	    public void changeShipRotation(Item item,Vector3 newRot) {
	        setModelProperty(item,MVCProperties.ROTATION_PROPERTY, newRot);                 
	    }
	    
	    public void changeShipVelocity(Item item,Vector3 newRot) {
	        setModelProperty(item,MVCProperties.VELOCITY_PROPERTY, newRot);                 
	    }


	public int itemsSize() {
		return gameLogic.getWorld().size();
	}


	public Item getItem(int i) {
		return gameLogic.getWorld().getItem(i);
	}

	public Item getEntityByID(int id) {
		return gameLogic.getWorld().getEntityByID(id);
	}
	public static ItemsController getItemsController() {
		return itemsController;
	}

	
//Métodos que IA usa

	public double executeQuery(ArtificialEntity currentEntityID,
			String query) {
		//System.out.println("query:"+query);
		return QueryCentral.checkQuery(currentEntityID, query);
	}



	public boolean parseConsoleCommand(String cmd,String[] pars,ItemsViewManagerAbstract itemView) {
		//String cmd=command.getCommand().toLowerCase();
		if (cmd.equals("hello")){
			itemView.printLog("Hello!", false);
			return true;
		}
		if (cmd.equals("printmass")){
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				itemView.printLog(item+"=>"+Formatacao.formatMass(item.getMass()), false);
			}
			return true;
		}
		
		if (cmd.equals("killvel")){
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				item.setVelocity(0, 0, 0);
			}
			return true;
		}
		
		if (cmd.equals("invertevel")){
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				item.setVelocity(-item.getVelocity().x, -item.getVelocity().y, -item.getVelocity().z);
			}
			return true;
		}
		
		if (cmd.equals("startengines")){
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				
				activateControl(item,ControlEnum.CTL_ENGINE_MAX);
			}
			return true;
			}
		if (cmd.equals("stopengines")){
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				
				activateControl(item,ControlEnum.CTL_ENGINE_STOP);
			}
			return true;
		}
		
		
		if (cmd.equals("moveto")){
			
			Item itemMove2=null;
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				if (item.getName().toLowerCase().equals("avatar")){
					itemMove2=item;
					break;
				}
			}
			
			
			int idMoveObj=Integer.parseInt(pars[0]);
			
			
			
			Item itemToMove=getEntityByID(idMoveObj);
			String nmMoveObj=pars[1];
			for (int i=0;i<itemsSize();i++){
				Item item=getItem(i);
				if (item.getName().toLowerCase().equals(nmMoveObj.toLowerCase())){
					itemToMove.getBody().setPosition(item.getCoord().x, item.getCoord().y, item.getCoord().z+item.getRadius()*1.01);
					itemToMove.getBody().getVelocity().clear();
					
					itemMove2.getBody().setPosition(item.getCoord().x, item.getCoord().y+5000, item.getCoord().z+item.getRadius()*1.01);
					itemMove2.getBody().getVelocity().clear();
					break;
				}
			}
			
			
			
			return true;
		}
		return false;
	}





	public DataManager getDataManager() {
		return dataManager;
	}





	public PlayerFaction getPlayerFaction(int id) {
		return dataManager.getFactionManager().getPlayerID(id);
	}





	public void setSelection(Item item, Item selection) {
		item.setTarget(selection);
	}
}
