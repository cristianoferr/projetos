package com.cristiano.galactic.view;


import java.beans.PropertyChangeEvent;
import java.util.Vector;

import com.cristiano.galactic.controller.ItemsController;
import com.cristiano.galactic.model.Entity.SphereItem;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.galactic.view.models.ItemView;
import com.cristiano.galactic.view.models.PlanetView;
import com.cristiano.galactic.view.models.ShipView;
import com.cristiano.galactic.view.models.SphereView;
import com.cristiano.galactic.view.models.StarView;
import com.cristiano.gamelib.propriedades.AbstractModel;

public class ItemsViewManager extends ItemsViewManagerAbstract {
	
	
	public ItemsViewManager(ItemsController itemsController,IView view,boolean flagSync){
		super(itemsController,view,flagSync);

	}
	
	
	public void modelPropertyChange(PropertyChangeEvent evt) {
		super.modelPropertyChange(evt);
/*		if (evt.getPropertyName().equals(ItemsController.SHIPWARE_PROPERTY)) {
			vm.setShipWare((String)evt.getNewValue());
        }*/

	}
	@Override
	public void registerItem(AbstractModel obj) {
	//	System.out.println("ItemsView.register2: "+obj);
		registerItem((Item)obj);
		

	}
	
	public void unregisterItem(AbstractModel obj) {
		//System.out.println("ItemsView.unregister2: "+obj);
		getView().removeVisualObject((Item)obj);
		unlinkItems((Item)obj);
		//registerItem((Item)obj);
	}
	
	
	 /**
     * We call this method for all items we find in the world.
     * <br> (it is package local to allow direct access from the listener)
     *
     * @param item new item
     */
    void registerItem( Item item ) {
        // we could load some model here depending on e.g. some attribute of the Item
        // for simplicity in this tut we simply create some geom depending on type

        // these should somewhat match the physical representation
        // though I do not recommend to derive physics from visual as you can't have
        // correct behaviour without loading the models (e.g. on a server)
    	System.out.println("ItemsView.registerItem: "+item.getName());
    	String itemType=item.getType();
    	 if ( itemType.equals(EntityType.ET_SHIP) ) {
    		 ShipView itemView=new ShipView((View)view);
    		 itemView.setRadius(Math.max(item.getModelData().getTamZ(),Math.max(item.getModelData().getTamY(),item.getModelData().getTamX())));
    		 itemView.setName(item.getName());
    		 linkItems(item, itemView);
    		 itemView.loadModel(item.getModelData());
//    		 input.removeFromAttachedHandlers( cameraInputHandler );
           //  cameraInputHandler = new ChaseCamera(cam,((Ship)item).getBs().getChassis() );
          //   input.addToAttachedHandlers( cameraInputHandler );     
    	 }else 
    		 if ( itemType.equals(EntityType.ET_STATION) ) {
        		 ShipView itemView=new ShipView((View)view);
        		 itemView.setRadius(Math.max(item.getModelData().getTamZ(),Math.max(item.getModelData().getTamY(),item.getModelData().getTamX())));
        		 itemView.setName(item.getName());
        		 linkItems(item, itemView);
        		 itemView.loadModel(item.getModelData());
//        		 input.removeFromAttachedHandlers( cameraInputHandler );
               //  cameraInputHandler = new ChaseCamera(cam,((Ship)item).getBs().getChassis() );
              //   input.addToAttachedHandlers( cameraInputHandler );     
        	 }else
    		 if ( itemType.equals(EntityType.ET_PLANET)) {
    			 PlanetView itemView=new PlanetView((View)view,item.getGeom().getRadius());
    			 
    			 itemView.setName(item.getName());
    			 itemView.setTexture(((SphereItem)item).getTexture());
    			 
    			 Vector<String> properties=PlanetProperties.getOrbitProps();
    			 for (int i = 0; i < properties.size(); i++) {
    				 itemView.setProperty(properties.get(i), item.getProperty(properties.get(i)));
				}
    			 properties=PlanetProperties.getPlanetProps();
    			 for (int i = 0; i < properties.size(); i++) {
    				 itemView.setProperty(properties.get(i), item.getProperty(properties.get(i)));
				}
    			 
    			 linkItems(item, itemView);
    			 itemView.createVisualNodeFromGeom(item);
    	 }else 
    		 if ( itemType.equals(EntityType.ET_STAR)) {
    			 StarView itemView=new StarView((View)view,item.getGeom().getRadius());
    			 itemView.setName(item.getName());
    			 itemView.setTexture(((SphereItem)item).getTexture());
    			 linkItems(item, itemView);
    			 itemView.createVisualNodeFromGeom(item);}
    		 else
    			 if ( itemType.equals(EntityType.ET_BULLET)) {
        			 ItemView itemView=new SphereView((View)view,item.getGeom().getRadius());
        			 itemView.setName(item.getName());
        			 itemView.createVisualNodeFromGeom(item);
        			 //itemView.loadModel(item.getModelData());
        			 linkItems(item, itemView);
        			
        				
        	 }
			 else
    			 if ( itemType.equals(EntityType.ET_SPHERE)) {
        			 ItemView itemView=new SphereView((View)view,item.getGeom().getRadius());
        			 itemView.setName(item.getName());
        			 itemView.setTexture(((SphereItem)item).getTexture());

        			 linkItems(item, itemView);
        			 itemView.createVisualNodeFromGeom(item);
        				
        	 }
    			

        else {
            throw new IllegalArgumentException( "unknown Item type: " + item.getClass() );
        }
    	 

    }



}
