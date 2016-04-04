/**
 * Applet allowing interactive investigation of depth sort

 */
package com.cristiano.math.quickhull;
import java.applet.*;
import java.awt.*;
import java.util.*;
public
class AppletDepth extends Applet3d{
    static double[][][] tridata = { // initial positions of tris
    {{-2,-2,-1},{2,1,1},{-2,-1,-1}},
    {{1,-2,-1},{2,-1,-1},{-2,2,1}},
    {{-1,2,-1},{0,2,-1},{-1,-2,1}}
  };
  static Color[] tricol = {Color.red,Color.green,Color.blue};

  static double[][][] cutdata = { // initial positions of tris
    {{0,-0.5,0},{2,1,1},{0,0,0}},
    {{-2,-2,-1},{0,-0.5,0},{0,0,0},{-2,-1,-1}},
    {{-0.5,0,0},{0,0.5,0},{-2,2,1}},
    {{1,-2,-1},{2,-1,-1},{0,0.5,0},{-0.5,0,0}},
    {{-1,0,0},{-0.5,0,0},{-1,-2,1}},
    {{-1,2,-1},{0,2,-1},{-0.5,0,0},{-1,0,0}}
  };
  static Color[] cutcol = {Color.red,Color.red,Color.green,Color.green,Color.blue,Color.blue};


  private Object3dList polygons(double[][][] data, Color[] col) {
    Object3dList model = new Object3dList(data.length);
    for (int i=0; i < data.length; i++){
      Point3d[] tri = new Point3d[data[i].length];
      for (int j=0; j < data[i].length; j++){
	tri[j] = new Point3d(data[i][j][0],data[i][j][1],data[i][j][2]);
      }
      Polygon3d t = new Polygon3d(tri,col[i]);
      model.addElement(t);
    }
    return model;
  }    

  public boolean mouseEnter(Event e, int x, int y){
    showStatus("Hold the mouse button down and move the mouse to rotate shapes");
    return true;
  }

  public Choice createModelChoice() {
    Choice modelChoice = new Choice();
    modelChoice.addItem("Logo");
    modelChoice.addItem("Triangles");
    modelChoice.addItem("Cut Triangles");
    return modelChoice;
  }

  /** override this method if we want a choice of models */
  public Object3dList selectModel(String choice) {
    if (choice.equals("Logo")) {
      return new CSELogo();
    } else if (choice.equals("Triangles")) {
      return polygons(tridata,tricol);
    } else if (choice.equals("Cut Triangles")) {
      return polygons(cutdata,cutcol);
    }
    return null;
  }

  /** defaultModel to display */
  public Object3dList defaultModel() {
      return new CSELogo();
  }

    // Return information suitable for display in an About dialog box.
    public String getAppletInfo() {
        return "3D Spline Patch Applet.\nWritten by Tim Lambert.";
    }
 
}
