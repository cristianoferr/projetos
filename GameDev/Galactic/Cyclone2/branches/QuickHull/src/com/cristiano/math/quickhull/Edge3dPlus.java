package com.cristiano.math.quickhull;
/** A 3d edge with a list of associated points 
It's not a triangle but...
*/

public class Edge3dPlus extends Edge3d {
  /* the points associated with this. inside() is true for each point in here */
  Object3dList pts;
  int selectFrameNo=-1; //frame no this tri was selected 
  HalfSpace h;

  public Edge3dPlus(Point3d a, Point3d b, int frameNo){
    super	(a,b,frameNo);
    h = new HalfSpace(a,b);

    pts = new Object3dList(10);
  }
  
  /** try to add a point to association list.  Return true if succesful */
  public boolean add(Point3dObject3d p) {
    if (inside(p)) {
      pts.addElement(p);
      return true;
    } else {
      return false;
    }
  }
  
  /* set frameno that this tri wasselected for processing */
  public void select(int n){
    selectFrameNo=n;
    extreme().select(n);
  }
  
  /** return list of points associated with this triangle */
  public Object3dList getPoints() {
    return pts;
  }
  
  /** return point farthest from support plane of this triangl */
  public Point3dObject3d extreme() {
    Point3dObject3d res = null;
    double maxd = Double.MIN_VALUE;
    for (int i = 0; i < pts.size(); i++) {
      double d = h.normal.dot((Point3d)pts.elementAt(i));
      if ( d > maxd){
	res = (Point3dObject3d)pts.elementAt(i);
	maxd = d;
      }
    }
    return res;
  }
  
  
  /** Should we show the points associated with this triangle ? */
  boolean shouldShowPts(View3d v) {
    return firstFrame==v.getFrameNo() || selectFrameNo==v.getFrameNo();
	}
  
  /** render the triangle, given a 3D view */
  public void render(View3d v){
    if (shouldShowPts(v)) {
      super.render(v);
      pts.render(v);
    } else {
      super.render(v);
    }
  }
  
  public int getColorIndex(View3d v,int col) {
    return (selectFrameNo==v.getFrameNo()) ? selectColor : super.getColorIndex(v,col);
  }				
  
  
}
