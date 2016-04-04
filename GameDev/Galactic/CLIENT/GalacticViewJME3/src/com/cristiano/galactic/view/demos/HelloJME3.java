package com.cristiano.galactic.view.demos;

import javax.swing.Box;

import com.cristiano.cyclone.utils.Vector3;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
 
/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class HelloJME3 extends SimpleApplication {
	Geometry centro;
	Geometry ponto;
 Vector3 vCentro=new Vector3(0,0,0);
 Vector3 vPonto=new Vector3(1,0,0);
	
    public static void main(String[] args){
        HelloJME3 app = new HelloJME3();
        app.start(); // start the game
    }
 
    @Override
    public void simpleInitApp() {
    	Box b= new Box(Vector3f.ZERO, 0.1f, 0.1f, 0.1f); // create cube shape at the origin
    	centro = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        centro.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(centro);              // make the cube appear in the scene
        
        
        b = new Box(Vector3f.ZERO, 0.1f, 0.1f, 0.1f); // create cube shape at the origin
        ponto = new Geometry("Box", b);  // create cube geometry from the shape
         mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Yellow);   // set color of material to blue
        ponto.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(ponto);              // make the cube appear in the scene
        
        
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1,0,2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        
    }
    
    public void simpleUpdate(float tpf) {
    	ponto.setLocalTranslation(vPonto.getXf(), vPonto.getYf(), vPonto.getZf());
    	centro.setLocalTranslation(vCentro.getXf(), vCentro.getYf(), vCentro.getZf());
    	vPonto=vPonto.getRotateBy(vCentro, new Vector3(0,0,1), 1);
    	vPonto=vPonto.getRotateBy(vCentro, new Vector3(1,0,0), 1);

    	System.out.println(vPonto);
    }
}