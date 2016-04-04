package com.cristiano.math.quickhull;
import java.util.*;
import java.awt.*;
/** Constructs a convex hull via incremental algorithm
*/

public class Incremental extends HullAlgorithm{

  public Incremental(Point3dObject3d[] pts) {
    super(pts);
  }
	
  int[] extraColors() {
    int[] extra = new int[1];
    extra[0] = Object3dAdaptor.selectColor;
    return extra;
  }

  public Object3dList build () {
    int frameNo = 1;
    EdgeStack es = new EdgeStack(); //used to find boundary of hole
    Object3dList faces = new Object3dList(20);
    if (pts.length < 2){
      return faces;
    }
    faces.addElement(new Triangle3d(pts[0],pts[1],pts[2],frameNo++));
    faces.addElement(new Triangle3d(pts[0],pts[2],pts[1],frameNo++));
    /* now the main loop -- add vertices one at a time */
    for (int i = 3; i < pts.length; i++) {
      /* delete faces that this vertex can see*/
      boolean inside = true; //are we inside the hull?
      for (int j = 0; j < faces.size(); j++){
      	Object3d o = faces.elementAt(j);
      	if (o instanceof Triangle3d) {
	  Triangle3d t = (Triangle3d)o;
	  if (t.lastFrame>frameNo && t.inside(pts[i])) {
	    t.lastFrame = frameNo;
	    inside = false;
	    /* update boundary of hole */
	    es.putp(t.tri[0],t.tri[1]);
	    es.putp(t.tri[1],t.tri[2]);
	    es.putp(t.tri[2],t.tri[0]);
	  }
	}
      }
      if (inside) continue;
      pts[i].select(frameNo++);
      	
      while (!es.isEmpty()){
	Edge3d e = es.get();
	faces.addElement(new Triangle3d(e.start,e.end,pts[i],frameNo));
      }
      frameNo++;
    }
    faces.lastFrame = frameNo;
    return faces;
  }

  public Object3dList build2D() {
    int frameNo = 1;
    PointStack es = new PointStack(); //used to find boundary of hole
    Object3dList faces = new Object3dList(20);
    faces.addElement(new Edge3d(pts[0],pts[1],frameNo++));
    faces.addElement(new Edge3d(pts[1],pts[0],frameNo++));
    /* now the main loop -- add vertices one at a time */
    for (int i = 2; i < pts.length; i++) {
      /* delete faces that this vertex can see*/
      boolean inside = true; //are we inside the hull?
      for (int j = 0; j < faces.size(); j++){
      	Object3d o = faces.elementAt(j);
      	if (o instanceof Edge3d) {
	  Edge3d t = (Edge3d)o;
	  if (t.lastFrame>frameNo && t.inside(pts[i])) {
	    t.lastFrame = frameNo;
	    inside = false;
	    /* update boundary of hole */
	    es.put(t.start,t.end);
	  }
	}
      }
      if (inside) continue;
      faces.addElement(new Point3dObject3d(pts[i],frameNo++));
      
      faces.addElement(new Edge3d(es.getStart(),pts[i],frameNo));
      faces.addElement(new Edge3d(pts[i],es.getEnd(),frameNo++));
    }
    faces.lastFrame = frameNo;
    return faces;
  }

}




