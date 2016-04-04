package com.cristiano.galactic.view.demos;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.curve.BezierCurve;
import com.jme.curve.CurveController;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.KeyExitAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;
import com.jme.scene.lod.AreaClodMesh;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;
 
/**
 * Started Date: Aug 16, 2004<br><br>
 *
 * This program teaches Continuous Level of Detail mesh objects. To use this program, move
 * the camera backwards and watch the model disappear.
 *
 * @author Jack Lindamood
 */
public class HelloLOD extends SimpleGame {
 
    CameraNode cn;
 
    public static void main(String[] args) {
        HelloLOD app = new HelloLOD();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }
 
    //If you don't use java 6 remove tag, but why don't you use java 6 anyway?
    @Override
    protected void simpleInitGame() {
        // Point to a URL of my model
        URL model =
                HelloLOD.class.getClassLoader().
                getResource("jmetest/data/model/maggie.obj");
        
        // Create something to convert .obj format to .jme
        FormatConverter converter = new ObjToJme();
        // Point the converter to where it will find the .mtl file from
        converter.setProperty("mtllib", model);
        // This byte array will hold my .jme file
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        // This will read the .jme format and convert it into a scene graph
        BinaryImporter jbr = new BinaryImporter();
 
        //// Use an exact BoundingSphere bounds
        //   BoundingSphere.useExactBounds = true; //Note: Deprecated??
 
        Node meshParent = null;
        try {
            // Use the format converter to convert .obj to .jme
            converter.convert(model.openStream(), BO);
            // Load the binary .jme format into a scene graph
            Node maggie = (Node) jbr.load(new ByteArrayInputStream(BO.toByteArray()));
 
//            meshParent = (Node) maggie.getChild(0); //Note: ¿¿¿??? that won't work... Deprecated?
            meshParent = maggie; //This seems ok
        } catch (IOException e) { // Just in case anything happens
 
            System.out.println("Damn exceptions!" + e);
            e.printStackTrace();
            System.exit(0);
        }
        // Create a clod duplicate of meshParent.
        Node clodNode = getClodNodeFromParent(meshParent);
        // Attach the clod mesh at the origin.
        clodNode.setLocalScale(.1f);
        rootNode.attachChild(clodNode);
        // Attach the original at -15,0,0
        meshParent.setLocalScale(.1f);
        meshParent.setLocalTranslation(new Vector3f(-15, 0, 0));
        rootNode.attachChild(meshParent);
        // Clear the keyboard commands that can move the camera.
        input = new InputHandler();
        // Insert a keyboard command that can exit the application.
        input.addAction(new KeyExitAction(this), "exit", KeyInput.KEY_ESCAPE, false);
        // The path the camera will take.
        Vector3f[] cameraPoints = new Vector3f[]{
            new Vector3f(0, 5, 20),
            new Vector3f(0, 20, 90),
            new Vector3f(0, 30, 200),
            new Vector3f(0, 100, 300),
            new Vector3f(0, 150, 400),
        };
        // Create a path for the camera.
        BezierCurve bc = new BezierCurve("camera path", cameraPoints);
        // Create a camera node to move along that path.
        cn = new CameraNode("camera node", cam);
        // Create a curve controller to move the CameraNode along the path
        CurveController cc = new CurveController(bc, cn);
        // Cycle the animation.
        cc.setRepeatType(Controller.RT_CYCLE);
        // Slow down the curve controller a bit
        cc.setSpeed(.25f);
        // Add the controller to the node.
        cn.addController(cc);
        // Attach the node to rootNode
        rootNode.attachChild(cn);
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
            acm.setTrisPerPixel(.5f);
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
 
    Vector3f up = new Vector3f(0, 1, 0);
    Vector3f left = new Vector3f(1, 0, 0);
    private static Vector3f tempVa = new Vector3f();
    private static Vector3f tempVb = new Vector3f();
    private static Vector3f tempVc = new Vector3f();
    private static Vector3f tempVd = new Vector3f();
    private static Matrix3f tempMa = new Matrix3f();
 
    @Override
    protected void simpleUpdate() {
        // Get the center of root's bound.
        Vector3f objectCenter = rootNode.getWorldBound().getCenter(tempVa);
        // My direction is the place I want to look minus the location
        // of the camera.
        Vector3f lookAtObject = tempVb.set(objectCenter).subtractLocal(cam.getLocation()).normalizeLocal();
        // Left vector
        tempMa.setColumn(0, up.cross(lookAtObject, tempVc).normalizeLocal());
        // Up vector
        tempMa.setColumn(1, left.cross(lookAtObject, tempVd).normalizeLocal());
        // Direction vector
        tempMa.setColumn(2, lookAtObject);
        cn.setLocalRotation(tempMa);
    }
}



