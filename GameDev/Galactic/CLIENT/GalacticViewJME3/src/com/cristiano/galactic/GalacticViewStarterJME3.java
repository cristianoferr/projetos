package com.cristiano.galactic;



import com.cristiano.cyclone.utils.ObjetoBasico;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.view.View;


class RunThread implements Runnable {
	Galactic g=null;
	View view=null;
	boolean running=true;
	public RunThread(Galactic g,View v){
		this.g=g;
		this.view=v;
	}
	
	
	
	public void run() {
		if (g!=null){
			try {
				Thread.sleep(150);
			} catch (InterruptedException e1) {
				
				e1.printStackTrace();
			}
			System.out.println("Galactic start Thread");
			GameLogic gameLogic=g.getGameLogic();
			while (GalacticViewStarterJME3.running){
				try {
					gameLogic.update();
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (view!=null){
			try {
				Thread.sleep(1000);
				System.out.println("view Start()");
				view.start();
				
				System.out.println("view fim");
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}



	public boolean isRunning() {
		return running;
	}



	public void setRunning(boolean running) {
		this.running = running;
	}
}


public class GalacticViewStarterJME3 extends ObjetoBasico {
	//static String recursosPath=Consts.rootPath+"assets";
	public static boolean running=true;
	
	public GalacticViewStarterJME3(){
	/*	File f1 = new File (recursosPath);
		 try {
		       System.out.println ("Current directory : " + f1.getCanonicalPath());
		       recursosPath="file:/"+f1.getCanonicalPath().replace("\\", "/");
		       }
		     catch(Exception e) {
		       e.printStackTrace();
		       }*/
	}
	
	private void startMulti() {
		  Galactic g=Galactic.getInstance();
	        // create a view
	      View view=null;
	      g.start();  
		try {
			view = new View(  );
			view.setRunGameLogic(false);
			view.setGameLogic(g.getGameLogic());
			 
		} catch (Throwable e) {
			View.logDebug(e.getMessage());
		}
		
		RunThread threadGalactic=new RunThread(g,null);
		RunThread threadView=new RunThread(null,view);
		
		new Thread(threadGalactic).start();
		new Thread(threadView).start();
		/*
      // wait till all the threads complete
      boolean more=true;
      while(more ) {
          try {
              Thread.sleep(500);
           //   System.out.println("Thread loop");                               
              
          } catch ( Exception e) {

          }
	}*/
	}
	
	
	@SuppressWarnings("unused")
	  private void startSingle() {
		  
		  Galactic g=Galactic.getInstance();
	        // create a view
	      View view;
	      g.start();  
		try {
			view = new View(  );
			view.setGameLogic(g.getGameLogic());
			view.setRunGameLogic(true);
		    view.start();					
		} catch (Throwable e) {
				
			e.printStackTrace();
			}
			

	        
	        
	        
	        // start the view

	       

/*
	   //    ThreadGameLogic threadGL=new ThreadGameLogic("Thread GameLogic", gameLogic);
        	boolean running=true;
        	   
            try {
          	  while (running){
          		running=true;	      		   
          		Thread.sleep (1);
      		//    System.out.println("running...");
      		//    if (!threadGL.isRunning())running=false;
      		    
      		  gameLogic.clientMoveAndDisplay();
      		  if (view.renderView()) running=false;
      		   }
	      	  
	      	  System.out.println("Fim da execução");
	           Thread.sleep (100);
	        } catch (InterruptedException e) {
	          System.out.println(
	                   "Exception: Thread main interrupted.");
	        }*/
	        
	    }

	    public static void main( String[] args ) {
	        new GalacticViewStarterJME3().startMulti();
	       //new GalacticViewStarterJME3().startSingle();
	    }
		
}
