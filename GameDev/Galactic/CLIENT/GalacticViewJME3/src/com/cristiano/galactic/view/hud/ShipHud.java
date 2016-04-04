package com.cristiano.galactic.view.hud;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.KeyMapping;
import com.cristiano.galactic.view.View;
import com.cristiano.galactic.view.app.KeyMappingJME;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.jme3.scene.Spatial;


public class ShipHud implements IShipHud{

  

View view;
private Nifty nifty;
HUDController hudController;
KeyMapping keyMap;
KeyMappingJME keyMapJME;
Spatial currPlayer=null;

	public ShipHud( View view){
		this.view=view;   
		keyMap=new KeyMapping(view.getPlayer());
		keyMapJME=new KeyMappingJME(keyMap);
		keyMapJME.initKeys(view);
		//HUDController.setShipHUD(this);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(view.getAssetManager(),view.getInputManager(),  view.getAudioRenderer(),   view.getGuiViewPort());        
        nifty = niftyDisplay.getNifty();
		//nifty.fromXml(Consts.niftyHUD, "start");
		//nifty.fromXml("hud/NiftyHUD.xml", "start");
		nifty.fromXml("hud/NiftyHUD.xml", "start");
	    Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
	    Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
		hudController=(HUDController) nifty.findScreenController(HUDController.class.getName());
		hudController.setShipHUD(this);
		hudController.setJogo(view);
		view.getGuiViewPort().addProcessor(niftyDisplay);


        hudController.initialize();
	}
  
	 
	  public void update(IView view){
		  boolean ok=true;
		  if (hudController!=null){
			  if (hudController.isConsoleVisible()) {
				  ok=false;
			  }
			  hudController.update();
		  }
		  if (ok){
			  keyMap.turn(view.getPlayer());
			  
			  
			/*  
			  for (int i=keyMap.getControls().size()-1;i>=0;i--){
					//System.out.println("KEYTurn= "+controls.get(i)+" i:"+i);
				  String key=keyMap.getControls().get(i);
				  if (!KeyInput.get().isKeyDown(KeyMappingJME.getKeyInput(key))){
					  keyMap.releaseKey(key);
				  }
					
				}*/
			  
		  }
		  try {
			//  nifty.render(false);
		     // nifty.update();
		} catch (Exception e) {
			View.logDebug(e.getMessage());
			e.printStackTrace();
		}
		  
		  
		 
	      
	  }

	  public IView getView(){
		  return view;
	  }



	public HUDController getHudController() {
		return hudController;
	}


	public void setHudController(HUDController hudController) {
		this.hudController = hudController;
	}


	public void printConsole(String message) {
		hudController.printConsole(message);
		
	}


	public void sendConsoleCommand(ConsoleExecuteCommandEvent command) {
		String cmd=command.getCommand().toLowerCase();
		if (cmd.equals("debugpanel")){
			hudController.setDebugLayerVisibility(!hudController.isDebugLayerVisible());
//			debugLayer.setVisible(!hudController.debugLayer.isVisible());
			return;
		}
		
		view.sendConsoleCommand(cmd,command.getArguments());
		
	}



	public void notifyPropertyChanged(IObjeto objeto, String property) {
		hudController.propertyChanged(objeto, property);
		
	}
	public void registerObjetoJogo(IObjeto item) {
		hudController.registerObjetoJogo(item);
	}
	public void unregisterObjetoJogo(IObjeto item) {
		hudController.unregisterObjetoJogo(item);
	}




	public void updateSelection(IObjeto objeto) {
		hudController.updateSelection(objeto);
		
	}
	  
	  /**
		 * Create our GUI.  FengGUI init code goes in here
		 *
		 */
	
	 
}
