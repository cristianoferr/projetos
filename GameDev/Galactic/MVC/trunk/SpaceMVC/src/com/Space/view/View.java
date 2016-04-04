/*
 * Copyright (c) 2005-2007 jME Physics 2
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of 'jME Physics 2' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.Space.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import com.Space.XMLparser.JmeBinaryReader;
import com.Space.controller.GameLogic;
import com.Space.model.Actor;
import com.Space.model.Body;
import com.Space.model.Item;
import com.Space.model.Platform;
import com.Space.model.Ship;
import com.Space.model.Star;
import com.Space.model.World;
import com.jme.bounding.BoundingSphere;
import com.jme.input.ChaseCamera;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.thirdperson.ThirdPersonMouseLook;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jmetest.physics.SimplePhysicsTest;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.physics.util.PhysicsPicker;

import cristiano.jmeutils.Consts;

/**
 * @author
 */
public class View extends SimplePhysicsTest {
    private World world;
    private final GameLogic gameLogic;
    protected ChaseCamera chaser;
    CameraNode camNode;
    
    private static final ColorRGBA AMBIENT_GRAY_COLOR = new ColorRGBA(0.7f, 0.7f, 0.7f, 1.0f);
    private static final ColorRGBA DIFFUSE_COLOR = new ColorRGBA(0.1f, 0.5f, 0.8f, 1.0f);
    private static final ColorRGBA NO_COLOR = ColorRGBA.black;
    private static final float NO_SHININESS = 0.0f;

    

    public View( World world, GameLogic gameLogic ) {
        this.world = world;
        this.gameLogic = gameLogic;
    }

    @Override
    protected void simpleUpdate() {
        // to ease things we call the controller update method from the view here
        // the server application (without view) should do it in an extra thread
    
        gameLogic.update( tpf );
        //chaser.update(tpf);
    //    updateCam();
        
    }
protected void updateCam(){
	Vector3f offset = new Vector3f( 0, 30, -200 );
	
	camNode.getLocalTranslation().set(offset);
	world.getPShip().getBs().getChassis().getLocalRotation().multLocal(camNode.getLocalTranslation());
	camNode.getLocalRotation().set( world.getPShip().getBs().getChassis().getLocalRotation() );
	camNode.getLocalTranslation().addLocal( world.getPShip().getBs().getChassis().getLocalTranslation() );
	world.getPShip().getBs().getChassis().attachChild(camNode);
	
}
    protected void simpleInitGame() {
    	JmeBinaryReader.setRenderer(DisplaySystem.getDisplaySystem().getRenderer());
    	
    	   Ship ship1=new Ship("ship1");
	        ship1.setMass(1000);
	        ship1.setRadius(1);
	        ship1.setPosition( 0, 8, 2 );
	        world.getItems().add( ship1);    	
	        world.setPShip(ship1);
	        
    	
        rootNode.attachChild( gameLogic.getScenegraph() );

        // like in the controller we need to know about any item that gets added to the world here:
        // have a look if there already is some stuff in the world
        for ( Item item : world.getItems() ) {
            registerItem( item );
        }
        configurePhysicsPicker();

        // finally subscribe a listener for stuff added to world (and removed from the world)
        world.getPropertyChangeSupport().addPropertyChangeListener( World.ITEMS_PROPERTY, new ItemsListener() );
        
        lightState.setGlobalAmbient(new ColorRGBA(5f,5f,5f,0));
        cam.setFrustumFar(Float.MAX_VALUE);
        
        new PhysicsPicker( input, rootNode, getPhysicsSpace(), true );
        fpsNode.setRenderQueueMode( Renderer.QUEUE_ORTHO );
        buildLighting();
        
       // buildChaseCamera();
        cam.update();
        display.getRenderer().setCamera(cam);
        camNode = new CameraNode("Camera Node", cam);
        camNode.setLocalTranslation(new Vector3f(0, 250, -20));
        camNode.updateWorldData(0);
        rootNode.attachChild(camNode);

        MaterialState materialState = display.getRenderer().createMaterialState();
        materialState.setAmbient(AMBIENT_GRAY_COLOR);
        materialState.setDiffuse(DIFFUSE_COLOR);
        materialState.setSpecular(NO_COLOR);
        materialState.setShininess(NO_SHININESS);
        materialState.setEmissive(NO_COLOR);
        materialState.setEnabled(true);
        
        rootNode.setRenderState(materialState);
        rootNode.updateRenderState();
   	 updateCam();
    }
    private void buildLighting() {
		/** Set up a basic, default light. */
    	//com.jme.light.PointLight
    	PointLight light = new PointLight();
	    light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
	    light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
	    //light.setDirection(new Vector3f(1,-1,0));
	    
	    light.setEnabled(true);
 
	      /** Attach the light to a lightState and the lightState to rootNode. */
	    LightState lightState = display.getRenderer().createLightState();
	    lightState.setEnabled(true);
	    lightState.attach(light);
	    rootNode.setRenderState(lightState);
}
    private void configurePhysicsPicker() {
        cameraInputHandler.setEnabled( !cameraInputHandler.isEnabled() );
        MouseInput.get().setCursorVisible( !MouseInput.get().isCursorVisible());
    }


    
    /**
     * Small class for listening to the items association of our world.
     */
    private class ItemsListener implements PropertyChangeListener {
        /**
         * called when an item has been added to / removed from the world
         *
         * @param evt evt.getNewValue() contains and added item, evt.getOldValue() a removed one
         */
        public void propertyChange( PropertyChangeEvent evt ) {
            if ( evt.getNewValue() instanceof Item ) {
                registerItem( (Item) evt.getNewValue() );
            }
            if ( evt.getOldValue() instanceof Item ) {
                unregisterItem( (Item) evt.getNewValue() );
            }
        }
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
    	 if ( item instanceof Ship ) {
//    		 input.removeFromAttachedHandlers( cameraInputHandler );
           //  cameraInputHandler = new ChaseCamera(cam,((Ship)item).getBs().getChassis() );
          //   input.addToAttachedHandlers( cameraInputHandler );     
    	 }else
        if ( item instanceof Actor ) {
        	//System.out.println("Child:"+item.getNode().getChildren().size());
        	Sphere sph=new Sphere( "actor", 20, 20, 2 );
            item.getNode().attachChild(sph );
            Consts.color( item.getNode().getChild(1), new ColorRGBA( 1.5f, 0.5f, 0.9f, 0.6f ) ,display);
            //item.getNode().getChild(1).setLocalScale(0.5f);
                   
        }
        else if ( item instanceof Platform ) {
            item.getNode().attachChild( new Box( null, new Vector3f(), 5, 0.25f, 5 ) );
        }
        else if ( item instanceof Star) {
        	Node n=new Node();
        	Sphere sph=new Sphere( ((Body)item).getName()+".sph",100,100,((Body)item).getRadius() );
//        	if (((Body)item).getName().equals("Terra")){
//        	color( sph, new ColorRGBA(25f, 25f, 25f, 0.6f ) );}
//        	else{
//        		color( sph, ColorRGBA.gray );}
//        	sph.getLocalTranslation().x=item.getPosition().getX();
//        	sph.getLocalTranslation().y=item.getPosition().getY();
//        	sph.getLocalTranslation().z=item.getPosition().getZ();
            
        	//applyTerrain(sph);
        	//n.attachChild(sph);
        	item.getNode().attachChild(Consts.insereLuzPontual(sph,((Body)item).getRadius(),lightState,display));
            item.getNode().attachChild( n );
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
        }
        else {
            throw new IllegalArgumentException( "unknown Item type: " + item.getClass() );
        }
        item.getNode().setModelBound( new BoundingSphere() );
        item.getNode().updateModelBound();
    }

    /**
     * We call this method for all items that get removed from the world.
     * <br> (it is package local to allow direct access from the listener)
     *
     * @param item removed item
     */
    void unregisterItem( Item item ) {
        // nothing to be done as the items node is deleted by the controller
    }

    @Override
    protected void simpleRender() {
    	if (world.getPShip()!=null){
    		
    		System.out.println("Speed: "+world.getPShip().getBs().getLinearSpeed());	
    	
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_UP ) ) {
	    		world.getPShip().getBs().accelerate();
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_DOWN ) ) {
	    		world.getPShip().getBs().rearThrust();
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_A ) ) {
	    		world.getPShip().getBs().steer(1);
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_D ) ) {
	    		world.getPShip().getBs().steer(-1);
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_BACK) ) {
	    		world.getPShip().getBs().fullStop();
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_W ) ) {
	    		world.getPShip().getBs().manche(1);
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_S ) ) {
	    		world.getPShip().getBs().manche(-1);
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_Q ) ) {
	    		world.getPShip().getBs().roll(1);
	    	}
	    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_E ) ) {
	    		world.getPShip().getBs().roll(-1);
	    	}
    	}
    	
    	
    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_U ) ) {
    		Item item=world.getItems().get(1);
    		System.out.println("U:"+item.getPosition().getX()+" "+item.toString());
    		item.setPosition(item.getPosition().getX(), item.getPosition().getY()+.02f,item.getPosition().getZ());
    		System.out.println("Class:"+item.getNode().getClass());
    	}
    	if ( KeyInput.get().isKeyDown( KeyInput.KEY_J ) ) {
    		Item item=world.getItems().get(1);
    		System.out.println("J:"+item.getPosition().getX()+" "+item.toString());
    		item.setPosition(item.getPosition().getX(), item.getPosition().getY()-.02f,item.getPosition().getZ());
    	}    	
        if ( KeyInput.get().isKeyDown( KeyInput.KEY_V ) ) {
            PhysicsDebugger.drawPhysics( gameLogic.getPhysicsSpace(), DisplaySystem.getDisplaySystem().getRenderer() );
            GameLogic.line.draw(DisplaySystem.getDisplaySystem().getRenderer());
        }
        if ( KeyInput.get().isKeyDown( KeyInput.KEY_BACK ) ) {
            configurePhysicsPicker();
        }
    }
}

/*
 * $Log$
 */

