package com.cristiano.galactic.view.demos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.lod.AreaClodMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

public class OBJLoader extends SimpleGame {
	private float xlocation=0f;
	private float ylocation=0f;
	private float zlocation=-20f;
	static String recursosPath="../Galactic/assets";

	public static void main(String[] args) {
		File f1 = new File (recursosPath);
		 try {
		       System.out.println ("Current directory : " + f1.getCanonicalPath());
		       recursosPath="file:/"+f1.getCanonicalPath().replace("\\", "/");
		       }
		     catch(Exception e) {
		       e.printStackTrace();
		       }
		OBJLoader app = new OBJLoader(); // Create Object
		// Signal to show properties dialog
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start(); // Start the program
	}
 

	public Node loadOBJ(String objPath,String objModel){
        // Point to a URL of my model
		URL model=null;
		URL path=null;
		try {
			model = new URL(recursosPath+objPath+objModel);
			path = new URL(recursosPath+objPath);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}/*
		OBJLoader.class.getClassLoader().
		getResource(recursosPath+"/models/EVE/Orca.obj");*/

        // Create something to convert .obj format to .jme
        FormatConverter converter=new ObjToJme();
        // Point the converter to where it will find the .mtl file from
       // converter.setProperty("mtllib",model);
        //converter.setProperty("texdir",model);
        converter.setProperty("mtllib",path);
        converter.setProperty("texdir",path);
        
        
        

        // This byte array will hold my .jme file
        ByteArrayOutputStream BO=new ByteArrayOutputStream();
        try {
            // Use the format converter to convert .obj to .jme
            converter.convert(model.openStream(), BO);
            
            
            TriMesh mesh = (TriMesh)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            
            Node node=new Node("MeshNode");
            node.attachChild(mesh);
            // shrink this baby down some
            //node.setLocalScale(.1f);
            node.setModelBound(new BoundingSphere());
            node.updateModelBound();
            return node;
        } catch (IOException e) {   // Just in case anything happens
//          logger.logp(Level.SEVERE, this.getClass().toString(),
//                  "simpleInitGame()", "Exception", e);
      	e.printStackTrace();
      }
            
            return null;
	}
	
	protected void simpleInitGame() {
		
		// ---- LIGHTS
		/** Set up a basic, default light. */
		
		
		DirectionalLight l=new DirectionalLight();
		l.setAmbient(ColorRGBA.white);
		l.setEnabled(true);
		l.setDiffuse(ColorRGBA.randomColor());
		l.setSpecular(ColorRGBA.randomColor());
		l.setShadowCaster(true);
		lightState.attach(l);
		
		
		/*PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 200f, 200f, 200f, 1.0f ) );
        light.setLocation( new Vector3f( 0, 0, 0 ) );
        light.setEnabled( true );
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );*/
          
		
		
		
		Box b=new Box("box",Vector3f.ZERO,new Vector3f(2,2,2));
		b.setLocalTranslation(new Vector3f(2,12,2));
		rootNode.attachChild(b);
		b.setRandomColors();
		
		/** Attach the light to a lightState and the lightState to rootNode. */
		/*lightState = display.getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);*/
		//rootNode.setRenderState(lightState);

		//lightState.setGlobalAmbient(ColorRGBA.white);
		//lightState.attach(light);
		rootNode.setRenderState(lightState);
		
		
		
		
		
		display.getRenderer().setBackgroundColor(ColorRGBA.white.clone());
		
		/*try {
			ResourceLocatorTool.addResourceLocator(
			        ResourceLocatorTool.TYPE_TEXTURE,
			        new SimpleResourceLocator(getClass().getResource(
			                "/assets")));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
            // Put her on the scene graph
			//Node node=loadOBJ("/models/EVE/Megathron/megathron.obj");
		//Node node=loadOBJ("/models/shipA_OBJ/","shipA_OBJ.obj");
		//Node node=loadOBJ("/models/EVE/Providence/","providence.obj");
		Node node=loadOBJ("/models/misc/","dressoirNoir.obj");
		//Node node=loadOBJ("/models/EVE/Ragnarok/ragnarok.obj");
			//Node node=loadOBJ("/models/argon_m1/argon_m4.obj");
			node.setLocalTranslation(new Vector3f(0, 0, 0));
            rootNode.attachChild(node);
            
        
        
        
        
    }
	
	
	
	private Node getClodNodeFromParent(Node meshParent) {
        // Create a node to hold my cLOD mesh objects
        Node clodNode = new Node("Clod node");
        // For each mesh in maggie
        for (int i = 0; i < meshParent.getQuantity(); i++) {
            // Create an AreaClodMesh for that mesh. Let it compute
            // records automatically
            AreaClodMesh acm = new AreaClodMesh("part" + i,
                    (TriMesh) meshParent.getChild(i), null);
            
            acm.setModelBound(new BoundingSphere());
            acm.updateModelBound();
            // Allow 1/2 of a triangle in every pixel on the screen in
            // the bounds.
            acm.setTrisPerPixel(5f);
            // Force a move of 2 units before updating the mesh geometry
            acm.setDistanceTolerance(2);
            // Give the clodMe sh node the material state that the
            // original had.
//acm.setRenderState(meshParent.getChild(i).getRenderStateList()[RenderState.RS_MATERIAL]); //Note: Deprecated
            acm.setRenderState(meshParent.getChild(i).getRenderState(RenderState.RS_MATERIAL));
            // Attach clod node.
            clodNode.attachChild(acm);
        }
        return clodNode;
    }
	
	
	
	
	public void simpleInitGame_(){
		// Make a box
		Box b = new Box("Mybox", new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
//		rootNode.attachChild(b); // Put it in the scene graph
		
		
		URL model =OBJLoader.class.getClassLoader().
		getResource("resources/argon_m4.obj");
		//getResource("images/ship.obj");
		FormatConverter converter = new ObjToJme();
		ByteArrayOutputStream BO = new ByteArrayOutputStream();
		BinaryImporter jbr = new BinaryImporter();
		TriMesh ship = null;
		TextureState ts1;
		ts1 = display.getRenderer().createTextureState();
		ts1.setEnabled(true);
		ts1.setTexture(TextureManager.loadTexture(OBJLoader.class.getClassLoader() .getResource("resources/maps/unique_argon_m4_diff.jpg")));
		for(int x=0;x<1;x++ ) { try { converter.convert(model.openStream(), BO);
		ship = (TriMesh) jbr.load(new ByteArrayInputStream(BO.toByteArray()));
		}
		catch (IOException e) { e.printStackTrace(); System.exit(0); } ship.setLocalScale(1f); ship.setRenderState(ts1);
		Quaternion rotQuat = new Quaternion();
		Vector3f axis = new Vector3f(1,0.5f, 1).normalizeLocal();
		float angle = 90;
		rotQuat.fromAngleNormalAxis(angle * FastMath.DEG_TO_RAD, axis);
		ship.setLocalTranslation(new Vector3f(this.xlocation,this.ylocation,this.zlocation));
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();
		rootNode.attachChild(ship);
		}
	}
}
