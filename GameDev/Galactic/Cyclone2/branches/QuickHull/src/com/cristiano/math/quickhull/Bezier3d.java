package com.cristiano.math.quickhull;
import java.awt.*;

public class Bezier3d extends QuadMesh3d{

  protected Point3d[][] ctl;

  // the basis function for a Bezier spline
  static float b(int i, float t) {
    switch (i) {
    case 0:
      return (1-t)*(1-t)*(1-t);
    case 1:
      return 3*t*(1-t)*(1-t);
    case 2:
      return 3*t*t*(1-t);
    case 3:
      return t*t*t;
    }
    return 0; //we only get here if an invalid i is specified
  }
  
  //evaluate a point on the B spline
  static Point3d p(Point3d[][] ctl, float s, float t) {
    Point3d result = new Point3d(0,0,0);
    for (int i = 0; i<=3; i++){
      for (int j = 0; j<=3; j++){
	result = result.add(ctl[i][j].scale(b(i,s)).scale(b(j,t)));
      }
    }
    return result;
  }

  private int steps = 10;
  
  /** set step size for Bezier
   */
  public void setSteps(int steps){
    this.steps = steps;
    setPoints();
  }


  /** Create a Bezier patch given 4x4 grid of controls
      Colours are used for wireframe in s direction and t direction
   */
  public void setPoints(){
    Point3d[][] poly = new Point3d[steps+1][steps+1];
    for (int i = 0; i<=steps; i++){
      for (int j = 0; j<=steps; j++){
	poly[i][j] = p(ctl,((float) i)/steps,((float) j)/steps);
      }
    }
    setPoints(poly);
  }

  /** Create a Bezier patch given 4x4 grid of controls
      Colours are used for wireframe in s direction and t direction
      Bezier is approximated by a steps x steps quad mesh
   */
  public Bezier3d(Point3d[][] ctl, Color ci, Color cj, int steps){
    this.ctl = ctl;
    setSteps(steps);
    setColors(ci,cj);
  }



}
