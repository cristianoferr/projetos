package com.cristiano.galactic.view.hud;


import java.io.Console;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.gamelib.interfaces.IControle;
import com.cristiano.gamelib.interfaces.IControleTela;
import com.cristiano.gamelib.interfaces.IJogo;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.cristiano.gamelib.nifty.console.ConsoleController;
import com.cristiano.gamelib.nifty.debug.DebugController;
import com.cristiano.gamelib.nifty.overlay.OverlayController;
import com.cristiano.gamelib.nifty.overview.OverviewController;
import com.cristiano.gamelib.nifty.targetInfo.TargetInfoController;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;





public class HUDController implements IControleTela,KeyInputHandler{
	private Nifty nifty;
	  private Screen screen;
	  
	  private DebugController debugController;
	  private OverviewController overviewController;
	  
	  private ConsoleController consoleController;
	  
	  private OverlayController overlayController;
	  private IShipHud shipHUD;
	  
	  
	  private TargetInfoController shipInfoController,targetInfoController;
	  IJogo jogo=null;
	  
	  Vector<IControle> controles;

	  
	  
	  
	  //ImageRenderer shipGraphEl;
	 // ProgressBarControl progressbar;
	  
	  


	  public void bind(final Nifty newNifty, final Screen newScreen) {
		    nifty = newNifty;
		    		
		    Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
		    Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
		    //Logger.setLevel(Level.OFF);
		    
		

			controles=new Vector<IControle>();
		    
		    screen = newScreen;
		    screen.addKeyboardInputHandler(new HUDInputMapping(), this);

		    
		    nifty.registerScreenController(this);
		    debugController=screen.findElementByName("debugLayer").getControl(DebugController.class);
		    overlayController=screen.findElementByName("overlayLayer").getControl(OverlayController.class);
		    consoleController=screen.findElementByName("consoleLayer").getControl(ConsoleController.class);
		    shipInfoController=screen.findElementByName("shipInfo").getControl(TargetInfoController.class);
		    targetInfoController=screen.findElementByName("targetInfo").getControl(TargetInfoController.class);
		    overviewController=screen.findElementByName("overview").getControl(OverviewController.class);
		    
		    
		    targetInfoController.setFlagPlayer(false);
		    targetInfoController.setObjectName("Target Ship");
		    
		    
		    targetInfoController.setHullValue(0, 150);
		    targetInfoController.setShieldValue(330, 1200);
		   // targetInfoController.setSpeedValue(900, 1100);
		    shipInfoController.setHullValue(1000, 1000);
		    shipInfoController.setShieldValue(5000, 5000);
		    
		    registerController(targetInfoController);
		    registerController(shipInfoController);
		    registerController(consoleController);
		    registerController(debugController);
		    registerController(overlayController);
		    registerController(overviewController);
		    
		    
		    if (shipHUD==null){
		    	return;
		    }
		    shipHUD.setHudController(this);
		    	


		  }
	  
	  public void onStartScreen() {
	  }
	 

	  public void onEndScreen() {
	  }

	  public void gotoScreen(final String screenId) {
	    nifty.gotoScreen(screenId);
	  }
	  
	  
	  
	  /*
	   * Esse método é chamado a cada frame para atualizar as informações da tela
	   */
	  public void update(){
		  if (jogo==null){
			  return;
		  }
		  
		  shipInfoController.setObjeto(getJogo().getPlayer());
		  
		  for (int i=0;i<controles.size();i++){
			  controles.get(i).update(jogo);
		  }
		  
		  //Necessário para ficar atualizando a tela
		  nifty.getCurrentScreen().layoutLayers();
		  
	  }
	  
	  
	  public Element getElement(final String id) {
			return nifty.getCurrentScreen().findElementByName(id);
		}

	  
	  public boolean keyEvent(final NiftyInputEvent inputEvent) {
		    if (inputEvent == NiftyInputEvent.ConsoleToggle) {
		    	consoleController.toggleConsole();
		        return true;
		      } else {
		        return false;
		      }
		    }
	  
	  public String getPlayerName(){
		    return System.getProperty("user.name");
		  }
	  
	  
			public  IShipHud getShipHUD() {
				return shipHUD;
			}

			public  void setShipHUD(IShipHud shipHUD) {
				this.shipHUD = shipHUD;
			}

			public void printConsole(String message) {
				 Console console = screen.findNiftyControl("console", Console.class);
				    console.output(message);
				
			}

			 /**
			   * Esse método é chamado quando o usuário clica no botão do Overview...
			   */
			  public void overviewAlternate(){
				  overviewController.alternateVisibility();
			  }
			  
			public boolean isConsoleVisible() {
				return consoleController.isConsoleVisible();
			}


			@Override
			public IJogo getJogo() {
				return jogo;
			}


			@Override
			public void registerObjetoJogo(IObjeto item) {
				  for (int i=0;i<controles.size();i++){
					  controles.get(i).registerObjetoJogo(item);
				  }
				  
			}


			@Override
			public boolean sendConsoleCommand(String command,String[] pars) {
				return jogo.sendConsoleCommand(command.toLowerCase(),pars);
			}

			public void setJogo(IJogo jogo) {
				this.jogo=jogo;		
				if (shipInfoController!=null){ 
					shipInfoController.setObjeto(jogo.getPlayer());
				}
			}


			@Override
			public void unregisterObjetoJogo(IObjeto item) {
				 for (int i=0;i<controles.size();i++){
					  controles.get(i).unregisterObjetoJogo(item);
				  }
				
			}


			@Override
			public void registerController(IControle controle) {
				if (controle==null){
					Galactic.printWarn("Controller nulo!");
					return;
				}
				controle.setControleTela(this);
				controles.add(controle);
				  
				
			}

			public boolean isDebugLayerVisible() {
				return debugController.isVisible();
			}

			public void setDebugLayerVisibility(boolean b) {
				debugController.setVisibility(b);
				
			}

		

			@Override
			public void addOverviewField(String titulo,String field) {
				if (overviewController!=null){
					overviewController.addOverviewField(titulo,field);
				}
				
			}

			@Override
			public void propertyChanged(IObjeto objeto, String property) {
				if (overviewController!=null){
					overviewController.propertyChanged(objeto,property);
				}
				
			}

			@Override
			public void initialize() {

				overviewController.initialize();
				
				 if (shipInfoController!=null){
					 	shipInfoController.setObjeto(jogo.getPlayer());
					    shipInfoController.setFlagPlayer(true);
					    //Valores "falsos" para testar...
					    shipInfoController.setHullValue(500, 1500);
					    shipInfoController.setShieldValue(1000, 2000);
					  //  shipInfoController.setSpeedValue(80, 100);
				    }
				    
				    if (targetInfoController!=null){
				    	targetInfoController.setObjeto(null);
					    targetInfoController.setObjectName("Targeted ship");
					    targetInfoController.setHullValue(0, 150);
					    targetInfoController.setShieldValue(330, 1200);
					  //  targetInfoController.setSpeedValue(900, 1100);
					    targetInfoController.setFlagPlayer(false);
					    targetInfoController.setDistance(100000000);
				    }
				
			}

			@Override
			public void updateSelection(IObjeto objeto) {
				targetInfoController.setObjeto(objeto);
				targetInfoController.setImagem(Consts.GAMELIB_PATH+"imagens/avatares/Avatar"+(int)(Math.random()*7)+".png");
				
			}


	}
