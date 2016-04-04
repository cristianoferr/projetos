package com.cristiano.galactic;


import java.util.Vector;

import org.apache.log4j.Logger;

import com.cristiano.cyclone.Cyclone;
import com.cristiano.cyclone.utils.GenericDTO;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.model.Sistema;
import com.cristiano.galactic.model.containers.DataManager;



class ThreadGameLogic implements Runnable {
	  private String tName;
	  private Thread t;
	  private boolean running=true;
	  private GameLogic gameLogic;
	  
	  public boolean isRunning() {
		return running;
	}

	  ThreadGameLogic (String threadName,GameLogic gameLogic) {
	     tName = threadName;
	     t = new Thread (this, tName);
//	     escreveArquivo();
	     this.gameLogic=gameLogic;
	     t.start();
	  }
	  

	  
	  public void run() {
	     try {
	    	 running=true;
	     while (true){
	     //   System.out.println("Thread: " + tName +" c:"+c);
	    	 gameLogic.update();//moveAndDisplay();
	        Thread.sleep(1);
	       // escreveArquivo();
	      }
	     } catch (InterruptedException e ) {
	       System.out.println("Exception: Thread "
	               + tName + " interrupted");
	     }
	     System.out.println("Terminating thread: " + tName );
	     running=false;
	  }
	}



public class Galactic {
	private static Logger logger = Logger.getLogger(Galactic.class);

	private static Galactic instance=null;

	public static Vector<GenericDTO> debug_lines=null;
	private GameLogic gameLogic;
	private Sistema world;
	public GameLogic getGameLogic() {
		return gameLogic;
		
	}



	public Sistema getWorld() {
		return world;
	}

	public static void log(String msg){
		logger.info(msg);
	}
	
	public static void printWarn(String msg){
		logger.warn(msg);
	}
	
	public static void printDebug(String msg){
		logger.debug("(printDebug): "+msg);
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	private DataManager dataManager;
	
	public Galactic(){
		log("Galactic.constructor");
		instance=this;
		Consts.initPath();
		
		debug_lines=Cyclone.debug_lines;
	}
	
	public static Galactic getInstance() {
		log("getInstance:"+instance);
		if (instance==null){
			instance=new Galactic();
		}

		return instance;
	}
	
	
	 public static void printLog(String message,boolean debug){
			log(message);
		}

	
	 
	  public void start() {
		  dataManager=new DataManager();
		 
	        // create a controller
	        // we do this before creating the view - this helps us to mind not to use
	        // anything from the view in the controller 
	        
	        //ok
	        gameLogic = new GameLogic();
	      
	        dataManager.setModelContainer(gameLogic.getPhysicsSpace().getModelContainer());
	      
	        // create a model
	        world = new Sistema("Sistema 1",dataManager);
	        
	        gameLogic.initialize(world);
	        dataManager.initialize(world);
	        world.start();
	        
	        
	        if (!Consts.LOAD_XML_ENTITIES){
	        	dataManager.getXmlBuilder().createXMLEntities(world);
	        }

	        world.setReady(true);
	    }

	    public static void main( String[] args ) {
	        new Galactic().start();
	    }



		public static void throwError(String string) {
			throw new IllegalArgumentException( "CRITICAL ERROR:" + string );
			
		}



		/**
		 * @return the instance
		 */
	
		
}
