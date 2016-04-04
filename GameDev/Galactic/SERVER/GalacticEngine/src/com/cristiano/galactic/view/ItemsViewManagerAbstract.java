/*
 * AbstractViewPanel.java
 *
 * Created on January 22, 2007, 9:06 AM
 *
 * To change this AbstractElement, choose Tools | AbstractElement Manager
 * and open the AbstractElement in the editor.
 */

package com.cristiano.galactic.view;


import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.controller.ItemsController;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.enums.ControlEnum;
import com.cristiano.galactic.model.enums.MVCProperties;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.cristiano.gamelib.propriedades.AbstractModel;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;


public abstract class ItemsViewManagerAbstract  {
	private Map<Item, AbstractItemView> items,itemsToAdd,itemsToRemove ;
	protected Map<AbstractItemView,Item > viewItems ;
	ItemsController itemsController;
	IView view;
	boolean flagSync=false;

	
	
	public ItemsViewManagerAbstract(ItemsController itemsController,IView view,boolean flagSync){
		items = new HashMap<Item, AbstractItemView>();
		itemsToAdd= new HashMap<Item, AbstractItemView>();
		itemsToRemove= new HashMap<Item, AbstractItemView>();
		viewItems=new HashMap<AbstractItemView,Item >();
		this.itemsController=itemsController;
		this.view=view;
		itemsController.addView(this);
		this.flagSync=flagSync;
	}
	
	protected void linkItems(Item item, AbstractItemView itemView) {
		//Galactic.printLog("flagSync:"+flagSync,true);
		if (flagSync){
			itemsToAdd.put(item, itemView);
			
		}else {
			items.put(item, itemView);
			
		}
		viewItems.put(itemView, item);
		 	
	}
	
	//Items podem ser removidos durante o loop e causar problema, ao invés disso eu adiciono em vetores para remoção/inclusão ao fim do loop
	public void sync(){
		Iterator iterator = itemsToAdd.keySet().iterator();
        while (iterator.hasNext()) {
          Item item = (Item) iterator.next();
          AbstractItemView itemView=itemsToAdd.get(item);

          	items.put(item, itemView);
			//viewItems.put(itemView, item);
        }
        itemsToAdd.clear();
        
        iterator = itemsToRemove.keySet().iterator();
        try {
			
        while (iterator.hasNext()) {
          Item item = (Item) iterator.next();
          AbstractItemView itemView=itemsToRemove.get(item);

          	items.remove(item);
			//viewItems.remove(itemView);
        }
        
        itemsToRemove.clear();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	public AbstractItemView getItemViewFromItem(Item item){
		if (items.get(item)!=null) return items.get(item);
		return itemsToAdd.get(item);

	}
	
	public Item getItemFromItemView(AbstractItemView itemView){
		return viewItems.get(itemView);
		
		
	}

	
	
	protected void unlinkItems(Item item) {
		AbstractItemView itemView=items.get(item);
		
		if (flagSync){
			itemsToRemove.put(item, itemView);
			
		}else {
			
			items.remove(item);
		}
		viewItems.remove(itemView);
		 	
	}
	
    /**
     * Called by the controller when it needs to pass along a property change 
     * from a model.
     *
     * @param evt The property change event from the model
     */
    
    public void modelPropertyChange(PropertyChangeEvent evt) {
		//if ((!evt.getPropertyName().equals("Orientation")) &&(!evt.getPropertyName().equals("Coord")))
		//		System.out.println("ItemsView.modelPropertyChange: PROP="+evt.getPropertyName()+" Source="+evt.getSource());
		if (evt.getPropertyName().equals(MVCProperties.ITEMS_PROPERTY)){
			if (evt.getNewValue()!=null){
				registerItem((Item)evt.getNewValue());
			}
			if (evt.getOldValue()!=null){
				unregisterItem((Item)evt.getOldValue());
			}
			
			return;
		}
		
		AbstractItemView vm=getItemViewFromItem((Item)evt.getSource());
		if (vm==null) return;
		if (evt.getPropertyName().equals(MVCProperties.COORD_PROPERTY)) {

			//Update the view layer with the relative position of the object... player is allways at 0,0,0 .
			vm.setAbsolutePosition(((Vector3)evt.getNewValue()));
			vm.setCoord(((Vector3)evt.getNewValue()).getSubVector(view.getPlayerAbsolutePosition()));
        }
		if (evt.getPropertyName().equals(MVCProperties.TEXTURE_PROPERTY)) {
			vm.setTexture(((String)evt.getNewValue()));
        }
		if (evt.getPropertyName().equals(MVCProperties.ORIENTATION_PROPERTY)) {
			vm.setOrientation((Quaternion)evt.getNewValue());
        }
		
		if (evt.getPropertyName().equals(MVCProperties.ROTATION_PROPERTY)) {
			vm.setRotation((Vector3)evt.getNewValue());
        }
		if (evt.getPropertyName().equals(MVCProperties.VELOCITY_PROPERTY)) {
			vm.setVelocity((Vector3)evt.getNewValue());
			if (view.getPlayer()!=null){
				Vector3 playerVel=view.getPlayer().getVelocity();
				Vector3 relVel=(Vector3)evt.getNewValue();
				relVel.subVector(playerVel);
				vm.setProperty(PropriedadesObjeto.PROP_RELATIVE_SPEED.toString(), relVel);
			}
        }
		if (evt.getPropertyName().equals(MVCProperties.NAME_PROPERTY)) {
			vm.setName((String)evt.getNewValue());
        }
		if (evt.getPropertyName().equals(MVCProperties.SHIPWARE_PROPERTY)) {
			vm.setShipWare((String)evt.getNewValue());
        }

	}
    
    public void registerItem(AbstractModel obj){Galactic.printLog("ERRO: ItemsViewManager.RegisterItem não implementado!",true);}   
    public void unregisterItem(AbstractModel obj){Galactic.printLog("ERRO: ItemsViewManager.UnRegisterItem não implementado!",true);}
    

	
    public Map<Item, AbstractItemView> getViewModels(){
		return items;
	}
    
    
	public void start(){
//    	System.out.println("view.start()");
    	Galactic.printLog("ItemsView Started",true);
    	
    	for (int i=0;i<itemsController.itemsSize();i++){
    		Item item=itemsController.getItem(i);
    		registerItem(item);
    	}
    	
    	
	}
    
    /*
     * Metodos que atualizam o mundo
     */
	
	public AbstractItemView createArtificialEntity(String shipWare,String name, double px,double py,double pz){
		Item item=itemsController.createArtificialEntity(shipWare,name,  px, py, pz);
		return getItemViewFromItem(item);
	}
	
	public AbstractItemView getEntityByID(int id){
		Item item=itemsController.getEntityByID(id);
		if (item==null){
			return null; 
		}
		return getItemViewFromItem(item);
	}
    
	
	
	/*
	 * Metodos que pegam dados do mundo
	 */
	public PlayerFaction getPlayerFactionID(int id){
		return itemsController.getPlayerFaction(id);
	}
	
	
	public Vector3 getPointInWorldSpace(AbstractItemView vm,Vector3 vec){
		return itemsController.getPointInWorldSpace(viewItems.get(vm),vec);
	}
	
	public Vector3 getTransformDirection(AbstractItemView vm,Vector3 vec){
		return itemsController.getTransformDirection(viewItems.get(vm),vec);
	}

	
	public Vector3 getCameraPosition(AbstractItemView vm){
		return itemsController.getCameraPosition((ArtificialEntity)viewItems.get(vm));
	}

		
		
    /*
     * Sends a control (command) to the server
     */
	public void activateControl(AbstractItemView vm,Enum ctl){
		itemsController.activateControl((ArtificialEntity)viewItems.get(vm),ctl);
	}
	
	public void deactivateControl(AbstractItemView vm,Enum ctl){
		itemsController.deactivateControl((ArtificialEntity)viewItems.get(vm),ctl);
	}
	
	public void activateControl(AbstractItemView vm,ControlEnum ctl,double intensity){
		itemsController.activateControl((ArtificialEntity)viewItems.get(vm),ctl,intensity,-1);
	}

	public IView getView() {
		return view;
	}

	public boolean sendConsoleCommand(String cmd,String[] pars) {
		//String cmd=command.getCommand().toLowerCase();
		if (cmd.equals("vis_ents")){
			view.print("There are "+viewItems.size()+" entities visible", false);
			return true;
		}
		if (cmd.equals("cleardebug")){
			
			Iterator iterator = viewItems.keySet().iterator();
	        while (iterator.hasNext()) {
	        	AbstractItemView item = (AbstractItemView) iterator.next();
	        	//System.out.println("cleardebug on:"+item.getName());
	        	item.clearDebugNode();
	        }
	        return true;
		}
		if (cmd.equals("drawip")){
			
			Iterator iterator = viewItems.keySet().iterator();
	        while (iterator.hasNext()) {
	        	AbstractItemView item = (AbstractItemView) iterator.next();
	        	System.out.println("drawip on:"+item.getName());
	        	item.debugInternalPoints();
	        	
	        }
	        return true;
		}
		return itemsController.parseConsoleCommand(cmd,pars,this);
		
	}

	public void printLog(String string, boolean b) {
		view.print(string, b);
		
	}

	public void setSelection(IObjeto objeto) {
		itemsController.setSelection(getItemFromItemView((AbstractItemView) view.getPlayer()), getItemFromItemView((AbstractItemView)objeto));
		
	}

}
