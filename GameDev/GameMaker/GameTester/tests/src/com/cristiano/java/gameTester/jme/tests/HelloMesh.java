package com.cristiano.java.gameTester.jme.tests;

import java.util.ArrayList;

import com.cristiano.jme3.utils.JME3Utils;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;
 
/** Sample 1 - how to get started with the most simple JME 3 application.
 * Display a blue 3D cube and view from all sides by
 * moving the mouse and pressing the WASD keys. */
public class HelloMesh extends SimpleApplication {
 
    public static void main(String[] args){
    	HelloMesh app = new HelloMesh();
        app.start(); // start the game
    }
 
    @Override
    public void simpleInitApp() {
        Box b = new Box(Vector3f.ZERO, 1, 1, 1); // create cube shape at the origin
        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
        rootNode.attachChild(geom);              // make the cube appear in the scene
        
        
        ArrayList<Vector3f> points=new ArrayList<Vector3f>();
        points.add(new Vector3f(0,5,2));
        points.add(new Vector3f(5,0,0));
        points.add(new Vector3f(0,5,0));
        points.add(new Vector3f(2,0,6));
        points.add(new Vector3f(0,2,2));
        points.add(new Vector3f(3,0,2));
        
        Material mat2 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
              mat2.setColor("Color", ColorRGBA.Yellow);
        
        Geometry geo = JME3Utils.generatePolygon(points,mat2);
       	rootNode.attachChild(geo);
       
    }

}