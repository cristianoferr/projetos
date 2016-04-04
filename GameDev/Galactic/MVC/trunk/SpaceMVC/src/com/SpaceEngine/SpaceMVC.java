package com.SpaceEngine;



import com.Space.controller.GameLogic;
import com.Space.model.Body;
import com.Space.model.Platform;
import com.Space.model.Star;
import com.Space.model.World;
import com.Space.view.View;

import cristiano.space.Consts;

	/**
	 * This lesson shows an example of building an application with proper design. It makes use of the Model/View/Controller
	 * concept. This means the data model is separated from the vizualization (rendered stuff) and the controller (physics
	 * and user input). This will notice that this makes the task of creating an initial version of the application quite
	 * complex. But it is rewarded by maintainability, extensibility and readability. Additionally distribution or
	 * replication of data gets a lot easier and thus allows a client-server or peer-to-peer application.
	 * <p/>
	 * First we should create our data model. Have a short look into the {@link World} class. In the
	 * {@link #createSimpleWorld} method we create some model elements.
	 * <p/>
	 * The {@link #start} method calls that latter method. Afterwards it creates controller ({@link GameLogic}) and
	 * {@link View}.
	 *
	 * @author Irrisor
	 */
	public class SpaceMVC {

	    /**
	     * Create a world and some stuff in it - this could also load from file instead.
	     * @return the new world :)
	     */
	    private World createSimpleWorld() {
	        World world = new World();
	        
	        
	        Body actor1 = new Body("actor1");
	        
	        actor1.setMass(100);
	        actor1.setRadius(1);
	        actor1.setPosition( 10, 50, 2 );
	       world.getItems().add( actor1 );
	       
	        Platform platform = new Platform();
	        platform.setPosition( 0, -4, 0 );
	        //world.getItems().add( platform );
	        
	        Body lua=new Body("Lua");
	        lua.setMass(Consts.massaLUA);
	        lua.setRadius(Consts.raioLUA);
	        //1738500f
	        lua.setPosition(1738500f, 0, 0);
	        //world.getItems().add( lua );
	        
	        Star sol=new Star("Sol");
	        sol.setMass(Consts.massaTERRA);
	        sol.setRadius(Consts.raioTERRA);
	       // sol.setRadius(1000);
	        //1738500f
	        sol.setPosition(10, Consts.raioTERRA*5, Consts.raioTERRA*10);
	      	    


	    	
	        
	        Body terra=new Body("Terra");
	        terra.setMass(Consts.massaTERRA);
	        terra.setRadius(Consts.raioTERRA);
	        terra.setPosition(100, 0, Consts.raioTERRA*4);
	        
	    //Documento para Preparo de Ambiente (DPA)  world.getItems().add( sol );
	      // world.getItems().add( terra );
	        
	        
	        return world;
	    }

	    /**
	     * Start our application. Invoked by the main method.
	     */
	    private void start() {
	        // create a model
	        World world = createSimpleWorld();
	        // create a controller
	        // we do this before creating the view - this helps us to mind not to use
	        // anything from the view in the controller 
	        GameLogic gameLogic = new GameLogic( world );
	        // create a view
	        View view = new View( world, gameLogic );

	        
	        
	        
	        // start the view

	        view.start();


	    }

	    public static void main( String[] args ) {
	        new SpaceMVC().start();
	    }
	}

	/*
	 * $Log$
	 */

