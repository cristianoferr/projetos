package com.cristiano.galactic;



import com.cristiano.comum.ObjetoBasico;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.view.View;
import com.jme.app.SimpleGame;


class RunThread implements Runnable {
	private Galactic g=null;
	private View view=null;
	private boolean running=true;
	private int tipo;
	
	public RunThread(Galactic g,View v,int tipo){
		this.g=g;
		this.view=v;
		this.tipo=tipo;
	}
	
	
	public void run() {
		//tipo:
		//1=galactic thread
		//2=view thread
		//3=view LOD thread
		
		if ((tipo==1)&& (g!=null)){
			try {
				Thread.sleep(15);
			} catch (InterruptedException e1) {
				
				View.log(e1.getMessage());
			}
			View.log("Galactic start Thread");
			while (true){
				try {
					g.getGameLogic().update();
					Thread.sleep(1);
				} catch (InterruptedException e) {
					View.log(e.getMessage());
				}
			}
		}
		
		if ((tipo==2) && (view!=null)){
			try {
				Thread.sleep(5000);
			
				view.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
				view.start();
				//view.inicializa();
				View.log("inicializa fim");
			} catch (InterruptedException e) {
				
				View.log(e.getMessage());
			}
		}
		
			if ((tipo==3)&& (view!=null)){
				try {
				Thread.sleep(10000);
				while (true){
					view.updateLOD();
					//era 200:
					Thread.sleep(200);
				}
				} catch (InterruptedException e) {
					View.log(e.getMessage());
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


public class GalacticViewStarterJME2 extends ObjetoBasico {
	

	
	public GalacticViewStarterJME2(){
	/*	File f1 = new File (Consts.recursosPath);
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
			view = new View( Consts.absolutePath+Consts.recursosPath );
			view.setRunGameLogic(false);
			view.setGameLogic(g.getGameLogic());
			 
		} catch (Exception e) {
			View.logDebug(e.getMessage());
			
		}
		
		RunThread threadGalactic=new RunThread(g,null,1);
		RunThread threadView=new RunThread(null,view,2);
		//RunThread threadLOD=new RunThread(null,view,3);
		
		new Thread(threadGalactic).start();
		new Thread(threadView).start();
		//new Thread(threadLOD).start();
      // wait till all the threads complete
     // boolean more=true;
     /* while(more ) {
          try {
              Thread.sleep(50000);
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
			view = new View( Consts.recursosPath );
			view.setGameLogic(g.getGameLogic());
			view.setRunGameLogic(true);
		    view.start();					
		} catch (Throwable e) {
			View.log(e.getMessage());
			}

	    }

	    public static void main( String[] args ) {
	        new GalacticViewStarterJME2().startMulti();
	   //    new GalacticViewStarterJME2().startSingle();
	    }
		
}
