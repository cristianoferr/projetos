package com.cristiano.math.quickhull;
/** super class for hull constructing algorithms
*/

public abstract class HullAlgorithm{
  Point3dObject3d[] pts;
  int[] extraColors(){
    return new int[0];
  }
  public HullAlgorithm(Point3dObject3d[] pts) {
    this.pts = pts;
  }
  
  public abstract Object3dList build();
  public abstract Object3dList build2D();

}
