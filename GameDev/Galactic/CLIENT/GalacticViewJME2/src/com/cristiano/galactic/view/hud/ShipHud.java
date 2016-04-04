package com.cristiano.galactic.view.hud;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.KeyMapping;
import com.cristiano.galactic.view.View;
import com.cristiano.galactic.view.app.KeyMappingJME;
import com.cristiano.galactic.view.app.RotateCamera;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.jme.input.KeyInput;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;


public class ShipHud implements IShipHud{

  

View view;
private Nifty nifty;
HUDController hudController;
KeyMapping keyMap;
KeyMappingJME keyMapJME;
private RotateCamera rotateCam;
Node currPlayer=null;

	public ShipHud( View view){
		this.view=view;  
		keyMap=new KeyMapping(view.getPlayer());
		keyMapJME=new KeyMappingJME(keyMap);
		//HUDController.setShipHUD(this);
		nifty = new Nifty(new JmeRenderDevice(), new JmeSoundDevice(), new JmeInputSystem(), new TimeProvider());
		nifty.fromXml(Consts.niftyHUD, "start");
	    Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
	    Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
		hudController=(HUDController) nifty.findScreenController(HUDController.class.getName());
		hudController.setShipHUD(this);
		hudController.setJogo(view);

		rotateCam = new RotateCamera(hudController,view.getDisplay().getWidth(), view.getDisplay().getHeight(), view.getCamNode(),keyMapJME);

        view.setInput(rotateCam);

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
			  
			  
			  
			  for (int i=keyMap.getControls().size()-1;i>=0;i--){
					//System.out.println("KEYTurn= "+controls.get(i)+" i:"+i);
				  String key=keyMap.getControls().get(i);
				  if (!KeyInput.get().isKeyDown(KeyMappingJME.getKeyInput(key))){
					  keyMap.releaseKey(key);
				  }
					
				}
		  }
		  try {
			  nifty.render(false);
		      nifty.update();
		} catch (Exception e) {
			View.logDebug(e.getMessage());
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


	public void reLoad(Node objVisual, CameraNode camNode) {
		
		//TODO: remover? verificar?
		//rotateCam.reLoad(objVisual);
		
		if (objVisual!=currPlayer){
			
			  rotateCam.reLoad(objVisual,camNode);
			  currPlayer=objVisual;
			  rotateCam.rotate(0, 0, 0, 0);
		  }
		
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


	public boolean isLockedCamera() {
		return rotateCam.isLockedCamera();
	}


	public void setLockedCamera(boolean b) {
		rotateCam.setLockedCamera(b);
		
	}


	public void updateSelection(IObjeto objeto) {
		hudController.updateSelection(objeto);
		
	}
	  
	  /**
		 * Create our GUI.  FengGUI init code goes in here
		 *
		 */
	
	 
}
