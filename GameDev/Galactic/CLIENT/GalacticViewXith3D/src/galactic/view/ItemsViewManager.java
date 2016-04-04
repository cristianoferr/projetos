package galactic.view;

import galactic.controller.ItemsController;
import galactic.model.AbstractModel;
import galactic.model.Entity.Bullet;
import galactic.model.Entity.Planet;
import galactic.model.Entity.Ship;
import galactic.model.Entity.SphereItem;
import galactic.model.Entity.Star;
import galactic.model.Entity.Station;
import galactic.model.Entity.Abstract.Item;
import galactic.view.models.ItemView;
import galactic.view.models.ShipView;
import galactic.view.models.SphereModel;

import java.beans.PropertyChangeEvent;


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
		//System.out.println("ItemsView.register2: "+obj);
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
    	
    	
    	//System.out.println("ItemsView.registerItem: "+item.getName()+" mass:"+item.getMass());
    	 if ( item instanceof Ship ) {
    		 ShipView shipView=new ShipView((View)getView());
    		 shipView.setTamX(item.getModelData().getTamX());
    		 shipView.setTamY(item.getModelData().getTamY());
    		 shipView.setTamZ(item.getModelData().getTamZ());
    		 shipView.setName(item.getName());
    		 linkItems(item, shipView);
    		 
    		 shipView.loadModel(item.getModelData());
//    		 input.removeFromAttachedHandlers( cameraInputHandler );
           //  cameraInputHandler = new ChaseCamera(cam,((Ship)item).getBs().getChassis() );
          //   input.addToAttachedHandlers( cameraInputHandler );     
    	 }else 
    		 if ( item instanceof Station ) {
        		 ShipView shipView=new ShipView((View)view);
        		 shipView.setTamX(item.getModelData().getTamX());
        		 shipView.setTamY(item.getModelData().getTamY());
        		 shipView.setTamZ(item.getModelData().getTamZ());
        		 shipView.setName(item.getName());
        		 linkItems(item, shipView);
        		 shipView.loadModel(item.getModelData());
//        		 input.removeFromAttachedHandlers( cameraInputHandler );
               //  cameraInputHandler = new ChaseCamera(cam,((Ship)item).getBs().getChassis() );
              //   input.addToAttachedHandlers( cameraInputHandler );     
        	 }else
    		 if ( item instanceof Planet) {
    			 ItemView vm=new SphereModel((View)view,item.getGeom().getRadius());
    			 linkItems(item, vm);
    			 vm.setName(item.getName());
    			 vm.setTexture(((SphereItem)item).getTexture());
    			 vm.createVisualNodeFromGeom(item.getGeom());
    	 }else 
    		 if ( item instanceof Star) {
    			 ItemView vm=new SphereModel((View)view,item.getGeom().getRadius());
    			 linkItems(item, vm);
    			 vm.setName(item.getName());
    			 vm.setTexture(((SphereItem)item).getTexture());
    			 vm.createVisualNodeFromGeom(item.getGeom());}
    		
    			 else
        			 if ( item instanceof Bullet) {
        				 ItemView itemView=new SphereModel((View)view,item.getGeom().getRadius());
            			 itemView.loadModel(item.getModelData());
            			 itemView.setName(item.getName());
            			 linkItems(item, itemView);
            			 
            			
            				
            	 } else
        			 if ( item instanceof SphereItem) {
            			 ItemView vm=new SphereModel((View)view,item.getGeom().getRadius());
            			 vm.setName(item.getName());
            			 vm.setTexture(((SphereItem)item).getTexture());

            			 linkItems(item, vm);
            			 vm.createVisualNodeFromGeom(item.getGeom());
            				
            	 }
    			 
        //	Sphere sph=new Sphere( ((Body)item).getName()+".sph",100,100,((Body)item).getRadius() );
//        	if (((Body)item).getName().equals("Terra")){
//        	color( sph, new ColorRGBA(25f, 25f, 25f, 0.6f ) );}
//        	else{
//        		color( sph, ColorRGBA.gray );}
//        	sph.getLocalTranslation().x=item.getPosition().getX();
//        	sph.getLocalTranslation().y=item.getPosition().getY();
//        	sph.getLocalTranslation().z=item.getPosition().getZ();
            
        	//applyTerrain(sph);
        	//n.attachChild(sph);
        	//item.getNode().attachChild(Consts.insereLuzPontual(sph,((Body)item).getRadius(),lightState,display));
           // item.getNode().attachChild( n );
           /*else
        if ( item instanceof Actor ) {
        	//System.out.println("Child:"+item.getNode().getChildren().size());
        	Sphere sph=new Sphere( "actor", 20, 20, 2 );
            item.getNode().attachChild(sph );
            Consts.color( item.getNode().getChild(1), new ColorRGBA( 1.5f, 0.5f, 0.9f, 0.6f ) ,display);
            //item.getNode().getChild(1).setLocalScale(0.5f);
                   
        }
        else if ( item instanceof Platform ) {
            item.getNode().attachChild( new Box( null, new Vector3d(), 5, 0.25f, 5 ) );
        }
             
        else if ( item instanceof Body ) {
        	//Node n=new Node();
        	Sphere sph=new Sphere( ((Body)item).getName()+".sph",100,100,((Body)item).getRadius() );
//        	if (((Body)item).getName().equals("Terra")){
//        	color( sph, new ColorRGBA(25f, 25f, 25f, 0.6f ) );}
//        	else{
//        		color( sph, ColorRGBA.gray );}
//        	sph.getLocalTranslation().x=item.getPosition().getX();
//        	sph.getLocalTranslation().y=item.getPosition().getY();
//        	sph.getLocalTranslation().z=item.getPosition().getZ();
            
        	CommonFunctions.applyTerrain(sph);
        	//n.attachChild(sph);
        	//item.getNode().attachChild(Consts.insereLuzPontual(sph,((Body)item).getRadius(),lightState,display));
            item.getNode().attachChild( sph );
        }*/
        else {
            throw new IllegalArgumentException( "unknown Item type: " + item.getClass() );
        }
    	 
      //  item.getNode().setModelBound( new BoundingSphere() );
      //  item.getNode().updateModelBound();
    }




	
	

	
	

	
	// From this point on: view -> controller -> model
	
	 /* public void changeShipWare(IItemView itemView,String newWare) {
	        itemsController.changeShipWare(viewItems.get(itemView),newWare);                 
	    }*/
	    


	
}
