package Robot3D;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;

import java.applet.Applet;
import java.awt.event.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class HelloJava3Da extends Applet {
	public HelloJava3Da() {
		setLayout(new BorderLayout());
		 GraphicsConfiguration config =
		SimpleUniverse.getPreferredConfiguration();
		 Canvas3D canvas3D = new Canvas3D(config);
		 add("Center", canvas3D);
		
		 BranchGroup scene = createSceneGraph();
		 scene.compile();
		
		 // SimpleUniverse is a Convenience Utility class
		 SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		
		 // This moves the ViewPlatform back a bit so the
		 // objects in the scene can be viewed.
		 simpleU.getViewingPlatform().setNominalViewingTransform();
		
		 simpleU.addBranchGraph(scene);
	 } // end of HelloJava3Da (constructor)

	public BranchGroup createSceneGraph() {
		 // Create the root of the branch graph
		 BranchGroup objRoot = new BranchGroup();
		 // rotate object has composite transformation matrix
		 Transform3D rotate = new Transform3D();
		 Transform3D tempRotate = new Transform3D();
		 
		 rotate.rotX(Math.PI/4.0d);
		 tempRotate.rotY(Math.PI/5.0d);
		 
		 rotate.mul(tempRotate);
		tempRotate.rotX(Math.PI/4.0d);
		//rotate.mul(rotate);		
		rotate.mul(tempRotate);
		 // Create a simple shape leaf node, add it to the scene graph.
		 // ColorCube is a Convenience Utility class
		TransformGroup objRotate = new TransformGroup(rotate);
		 objRotate.addChild(new ColorCube(0.4));
		objRoot.addChild(objRotate);
	
	 return objRoot;
	 } // end of createSceneGraph method of HelloJava3Da
	 
	// The following allows this to be run as an application
	 // as well as an applet
	
	public static void main(String[] args) {
		Frame frame = new MainFrame(new HelloJava3Da(), 256, 256);
	} // end of main (method of HelloJava3Da)
	 
	} // end of class HelloJava3Da