package galactic;


import galactic.controller.GameLogic;
import galactic.model.Sistema;
import galactic.model.containers.DataManager;
import galactic.view.View;
import cristiano.comum.ObjetoBasico;


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
			while (true){
				try {
					g.getGameLogic().update();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (view!=null){
			view.inicializa();
			System.out.println("inicializa fim");
		}
	}



	public boolean isRunning() {
		return running;
	}



	public void setRunning(boolean running) {
		this.running = running;
	}
}

public class GalacticViewStarterXith3D extends ObjetoBasico {

	
	public GalacticViewStarterXith3D(){
		
	}
	
	private void startMulti() {
		  Galactic g=Galactic.getInstance();
	        // create a view
	      View view=null;
	      g.start();  
		try {
			view = new View( "../GalacticEngine" );
			view.setRunGameLogic(false);
			view.setGameLogic(g.getGameLogic());
			
		} catch (Throwable e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		RunThread threadGalactic=new RunThread(g,null);
		RunThread threadView=new RunThread(null,view);
		
		new Thread(threadGalactic).start();
		new Thread(threadView).start();
        // wait till all the threads complete
        boolean more=true;
        while(more ) {
            try {
                Thread.sleep(500);
                System.out.println("Thread loop");                               
                
            } catch ( Exception e) {
 
            }
	}

	}
	
	  private void startSingle(boolean update) {
		  
		  Galactic g=Galactic.getInstance();
	        // create a view
	      View view;
	      g.start();  
		try {
			view = new View( "../GalacticEngine" );
			view.setRunGameLogic(update);
			view.setGameLogic(g.getGameLogic());
		    view.inicializa();					
		} catch (Throwable e) {
				// TODO Auto-generated catch block
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
	        new GalacticViewStarterXith3D().startSingle(true);
	      //  new GalacticViewStarterXith3D().startMulti();
	    }
		
}
