package com.cristiano.galactic.view.demos;


import java.io.File;

import javax.swing.Box;

import com.cristiano.galactic.Utils.Consts;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
 
/** Sample 3 - how to load an OBJ model, and OgreXML model, 
 * a material/texture, or text. */
public class HelloAssets extends SimpleApplication {
	static String recursosPath=Consts.rootPath+"assets";
	static String recursosPath2=Consts.rootPath+"assets";

    public static void main(String[] args) {
		File f1 = new File (recursosPath);
		 try {
		       System.out.println ("Current directory : " + f1.getCanonicalPath());
		       recursosPath="file:/"+f1.getCanonicalPath().replace("\\", "/");
		       recursosPath2=f1.getCanonicalPath();
		       }
		     catch(Exception e) {
		       e.printStackTrace();
		       }
		     
        HelloAssets app = new HelloAssets();
        app.start();
    }
 
    @Override
    public void simpleInitApp() {
 
    	assetManager.registerLocator(recursosPath,UrlLocator.class.getName());
    	//assetManager.registerLocator("../GalacticEngine/assets",UrlLocator.class.getName());
    	assetManager.registerLocator("../GalacticEngine/assets/", ClasspathLocator.class);
    	Spatial teapot1 = assetManager.loadModel("../GalacticEngine/assets/models/EVE/Megathron/megathron.obj");
    	//Spatial teapot2 = assetManager.loadModel(recursosPath+"/models/argon_m1/argon_m4.obj");
    	//Spatial teapot2 = assetManager.loadModel(recursosPath+"/models/EVE/Galente/Rookie/Rookie.obj");
/*            Material mat_default = new Material( 
            assetManager, recursosPath+"/models/argon_m1/argon_m4.mtl");
            teapot1.set
        teapot1.setMaterial(mat_default);*/
    	teapot1.setLocalTranslation(2.0f,-2.5f,0.0f);
    	teapot1.setLocalScale((float) 0.2);
    	//teapot2.setLocalTranslation(-2.0f,2.5f,0.0f);
        rootNode.attachChild(teapot1);
        //rootNode.attachChild(teapot2);
 
        // Create a wall with a simple texture from test_data
        Box box = new Box(Vector3f.ZERO, 2.5f,2.5f,1.0f);
        Spatial wall = new Geometry("Box", box );
        Material mat_brick = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap", 
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(mat_brick);
        wall.setLocalTranslation(2.0f,-2.5f,0.0f);
       //rootNode.attachChild(wall);
 
        // Display a line of text with a default font
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Hello World");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
 
        // Load a model from test_data (OgreXML + material + texture)
        Spatial ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.05f, 0.05f, 0.05f);
        ninja.rotate(0.0f, -3.0f, 0.0f);
        ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
       // rootNode.attachChild(ninja);
        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);
 
    }
}