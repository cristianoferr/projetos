/**
 * Applet allowing interactive rotation of spline patches

 */
package com.cristiano.math.quickhull;
import java.applet.*;
import java.awt.*;
import java.util.*;
public
class AppletPatch extends Applet3d{

  static double[][][] bezdata = { // Bez controls
    {{-3,0,-3},{-1,-1,-3},{1,1,-3}, {3,0,-3.4},{5,0,-3.4}},
    {{-3,0,-1},{-1,1,-1},{1,3,-1}, {3.5,1.2,-0.6},{5.5,1.2,-0.6}},
    {{-3,0,1},{-1,0.8,1},{1,1.5,1}, {2.8,0.6,1},{4.8,0.6,1}},
    {{-3,0,3},{-1,0,3},{1,0,3}, {3,2.5,3},{5,2.5,3}},
    {{-3,0,5},{-1,1,5},{1,-1,5}, {3,2.9,5},{5,2.9,5}}
  };

  private Object3dList bezpatch(double[][][] data) {
    Object3dList model = new Object3dList(1);
      Point3d[][] ctl = new Point3d[4][4];
    for (int i=0; i <= 3; i++){
      for (int j=0; j <= 3; j++){
	ctl[i][j] = new Point3d(data[i][j][0],data[i][j][1],data[i][j][2]);
      }
    }
    QuadMesh3d b = new Bezier3d(ctl,Color.red,Color.green,10);
    QuadMesh3d c = new QuadMesh3d(ctl);
    //b.toVRML(System.out);
    model.addElement(b);
    model.addElement(c);
    return model;
  }    

  private Object3dList bsplinepatch(double[][][] data) {
    Object3dList model = new Object3dList(1);
      Point3d[][] ctl = new Point3d[data.length][data[0].length];
    for (int i=0; i < data.length; i++){
      for (int j=0; j < data[0].length; j++){
	ctl[i][j] = new Point3d(data[i][j][0],data[i][j][1],data[i][j][2]);
      }
    }
    QuadMesh3d b = new Bspline3d(ctl,Color.red,Color.green,10);
    QuadMesh3d c = new QuadMesh3d(ctl);
    //b.toVRML(System.out);
    model.addElement(b);
    model.addElement(c);
    return model;
  }
  
  public boolean mouseEnter(Event e, int x, int y){
    showStatus("Hold the mouse button down and move the mouse to rotate the patch");
    return true;
  }

  public Choice createModelChoice() {
    Choice modelChoice = new Choice();
    modelChoice.addItem("Bezier");
    modelChoice.addItem("Bspline");
    return modelChoice;
  }

  /** override this method if we want a choice of models */
  public Object3dList selectModel(String choice) {
    if (choice.equals("Bezier")) {
      return bezpatch(bezdata);
    } else if (choice.equals("Bspline")) {
      return bsplinepatch(bezdata);
    }
    return null;
  }

  /** defaultModel to display */
  public Object3dList defaultModel() {
      return bezpatch(bezdata);
  }

    // Return information suitable for display in an About dialog box.
    public String getAppletInfo() {
        return "3D Spline Patch Applet.\nWritten by Tim Lambert.";
    }
 
}
