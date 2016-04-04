package com.Space.controller.ships;

import jmetest.renderer.TestEnvMap;

import com.Space.XMLparser.JmeBinaryReader;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmetest.physics.vehicle.Util;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

/**
 * Car is a JME Node which contains physics and visual representation of our vehicle.
 * First we need to create the dynamic physics node for the chassis, in which we attach
 * a model to generate the collision geometries from.
 *
 * @author Erick B Passos
 */
public class BasicShip extends Node {

    private static final long serialVersionUID = 1L;
    Node smoke = null;

    // The node to represent the car chassis
    private DynamicPhysicsNode chassisNode;


    public BasicShip( final DynamicPhysicsNode chassisNode ) {
        super( "ship" );
        this.chassisNode=chassisNode;
        createChassi(  );
        loadFancySmoke();
    }
    public BasicShip( final PhysicsSpace pSpace ) {
        super( "ship" );
        createChassi( pSpace );
        loadFancySmoke();
    }

    
    
    public void setPosition( float x, float y, float z ) {
        chassisNode.clearDynamics();
        chassisNode.getLocalTranslation().set( x, y, z );
        chassisNode.getLocalRotation().loadIdentity();
    }

    public void setRotation( float x, float y, float z, float w ) {
        chassisNode.clearDynamics();
        chassisNode.getLocalRotation().set( x, y, z, w );
    }


    private void createChassi(final PhysicsSpace pSpace) {
        chassisNode =  pSpace.createDynamicNode();
        createChassi();    	
    }
    private void createChassi(  ) {
    	try {

    		
    	
        chassisNode.setName( "chassiPhysicsNode" );

        // The model of the chassis can be changed at the CarData interface
        JmeBinaryReader jbr = new JmeBinaryReader();
        //Node chassisModel =// Util.loadModel( BasicShipData.CHASSIS_MODEL );
        
        Node chassisModel = jbr.loadBinaryFormat(BasicShip.class.getClassLoader()
				.getResourceAsStream(BasicShipData.CHASSIS_MODEL));
        chassisModel.setLocalScale( BasicShipData.CHASSIS_SCALE );

        chassisModel.updateGeometricState(0, true);
		
		Texture bodyTexture = TextureManager.loadTexture(BasicShip.class
				.getClassLoader().getResource("resources/sky_env.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		bodyTexture.setMipmapState(Texture.MM_NONE);

		Texture wingTexture = TextureManager.loadTexture(BasicShip.class
				.getClassLoader().getResource("resources/sky_env.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		wingTexture.setMipmapState(Texture.MM_NONE);
        
		Texture envTexture = TextureManager.loadTexture(TestEnvMap.class
				.getClassLoader()
				.getResource("resources/sky_env.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		envTexture.setEnvironmentalMapMode(Texture.EM_SPHERE);
		envTexture.setApply(Texture.AM_ADD);

		for (Object o : chassisModel.getChildren()) {
			System.out.println(o);
			if (o instanceof Node) {
				Node n = (Node) o;

				TextureState ts = (TextureState) n
						.getRenderState(RenderState.RS_TEXTURE);
				if (ts == null) {
					ts = DisplaySystem.getDisplaySystem().getRenderer()
							.createTextureState();
				}

				// Initialize the texture state
				if (n.getName().indexOf("body_") >= 0) {
					ts.setTexture(bodyTexture, 0);
				} else {
					ts.setTexture(wingTexture, 0);
				}

				// Add shiny environment to shield, cowling and engines
				if (n.getName().indexOf("env") >= 0) {
				      ts.setTexture( envTexture, 1 );					
				}
				
				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

			}
		}
		
		
        chassisNode.attachChild( chassisModel );

        // use false if you're going to use many cars
        chassisNode.generatePhysicsGeometry( true );
        chassisNode.setMaterial( Material.IRON );
        chassisNode.setMass( BasicShipData.CHASSIS_MASS );
        this.attachChild( chassisNode );
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void loadFancySmoke() {
        // Smoke node was made with Ren's particle editor
        smoke = Util.loadModel( BasicShipData.SMOKE_MODEL );
        smoke.setLocalTranslation( BasicShipData.SMOKE_OFFSET );
        smoke.setLocalScale( 0.02f );
        Util.applyZBuffer( smoke );
        chassisNode.attachChild( smoke );
    }

    /**
     * Accelerates the car forward or backwards
     * Does it by accelerating both suspensions (4WD). If you want a front wheel drive, comment out
     * the rearSuspension.accelerate(direction) line. If you want a rear wheel drive car comment out the other one.
     *
     * @param direction 1 for ahead and -1 for backwards
     */
    public void accelerate(  ) {
    	thrust(1000);
    }
    public void rearThrust(  ) {
    	thrust(-1000);
    }
    
    public void fullStop( ) {
    	 chassisNode.clearDynamics();
    }
    public void thrust(int i  ) {
    //	chassisNode.getMaterial().setSurfaceMotion( BasicShipData.SMOKE_OFFSET.mult(1000) );
    	// System.out.println("X:"+smoke.getWorldTranslation().getX()+" Y:"+smoke.getWorldTranslation().getY()+" Z:"+smoke.getWorldTranslation().getZ());
    	//chassisNode.addForce(BasicShipData.SMOKE_OFFSET.mult(1000));
    	
//		Vector3f direction = new Vector3f();
//		Quaternion pitchY = new Quaternion();
//		pitchY.fromAngleAxis(FastMath.PI/2, new Vector3f(0,1,0));
//		pitchY.multLocal(chassisNode.getLocalRotation());
//		pitchY.getRotationColumn(0, direction);
//		pitchY.getRotationColumn(1, direction);
//		pitchY.getRotationColumn(2, direction);    	
//		chassisNode.addForce(direction.mult(100000*i));

    	System.out.println("MAss:"+chassisNode.getMass());
		//Vector3f offset = new Vector3f( 0, 10, -80 );
		
		
		Vector3f position=chassisNode.getPhysicsNode().getLocalTranslation();
        // found a dynamic physics node, apply gravity
        //float distance = smoke.getWorldTranslation().distance(position);
        // check in which direction we need to apply the force
        // subtract the location of the dynamic node from the 
        // black holes location and normalize it
        
        Vector3f direction = position.subtract(smoke.getWorldTranslation()).normalize();
        chassisNode.addForce(direction.mult(100000*i));
        
        
//		camNode.getLocalTranslation().set(offset);
//		world.getPShip().getBs().getChassis().getLocalRotation().multLocal(camNode.getLocalTranslation());
//		camNode.getLocalRotation().set( world.getPShip().getBs().getChassis().getLocalRotation() );
//		camNode.getLocalTranslation().addLocal( world.getPShip().getBs().getChassis().getLocalTranslation() );
		
    }

    /**
     * Stops accelerating both suspensions
     */
    public void releaseAccel() {
    }

    /**
     * Steers the front wheels.
     *
     * @param direction 1 for right and -1 for left
     */
    public void steer( final int direction ) {
    	chassisNode.addTorque(new Vector3f(0,200*direction,0));
    }

    public void roll( final int direction ) {
    	chassisNode.addTorque(new Vector3f(0,0,200*direction));
    }
    public void manche( final int direction ) {
    	chassisNode.addTorque(new Vector3f(200*direction,0,0));
    }
    
    /**
     * Unsteer the front wheels
     */
    public void unsteer() {
    }

    private final Vector3f tmpVelocity = new Vector3f();
    
    /**
     * To get the car speed for using in a HUD
     *
     * @return velocity of the car
     */
    public float getLinearSpeed() {
        return chassisNode.getLinearVelocity( tmpVelocity ).length();
    }

    /**
     * Needed e.g. by the ChaseCamera.
     *
     * @return node which represents the chassis
     */
	public DynamicPhysicsNode getChassis() {
		return chassisNode;
	}

}
