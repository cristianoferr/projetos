package com.cristiano.math.quickhull;
import java.util.*;
import java.awt.*;
/** Constructs a convex hull via divide and conquer algorithm
*/

public class DivideAndConquer extends HullAlgorithm{

  int frameNo;
  static int leftColor; //colour of left subhull
  static int rightColor; //colour of right subhull
  public DivideAndConquer(Point3dObject3d[] pts) {
    super(pts);
  }

  int[] extraColors() {
    int[] extra = new int[2];
    extra[0] = leftColor;
    extra[1] = rightColor;
    return extra;
  }

  void sortpts(){
    /* First sort pts */
    int hi,lo,end;
    boolean changed = true;
    for(end = pts.length; changed; end--){
      changed = false;
      for(hi = 1; hi < end; hi++){
	lo = hi - 1;
	if (pts[lo].x() < pts[hi].x()) {
	  Point3dObject3d T = pts[lo];
	  pts[lo] = pts[hi];
	  pts[hi] = T;
	  changed = true;
	}
      }
    }
  }
	
  public Object3dList build () {
    sortpts(); 
    frameNo = 1;
    Object3dList faces = build(0,pts.length-1);
    faces.lastFrame = frameNo;
    Object3d o = faces.elementAt(0);
    if (o instanceof Object3dList) {
      ((Object3dList)o).setDefaultColor(rightColor);
    }
    o = faces.elementAt(1);
    if (o instanceof Object3dList) {
      ((Object3dList)o).setDefaultColor(leftColor);
    }
    return faces;
  }
	
  /** recursive build */
  protected Object3dList build(int first,int last) {
    if (last-first < 2) {
      return null;
    } else {
      int mid = (first + last)/2;
      Object3dList faces = new Object3dList(5);
      faces.firstFrame=frameNo;
      Object3dList left = build(first,mid);
      Object3dList right = build(mid+1,last);
      /* find the lower common tangent */
      int lctl = first; //candidate for left end of LCT
      int lctr = last; //candidate for right end of LCT
      HalfSpace lct = new HalfSpace(pts[lctl],pts[lctr]);
      boolean changed; // has the lct changed this time around the loop?
      do {
	changed = false;
	for(int i=first; i <= mid; i++) {
	  if (i != lctl && lct.inside(pts[i])) {
	    lctl = i;
	    lct = new HalfSpace(pts[lctr],pts[lctl]);
	    changed = true;
	  }
	}
	for(int i=mid+1; i <= last; i++) {
	  if (i != lctr && lct.inside(pts[i])) {
	    lctr = i;
	    lct = new HalfSpace(pts[lctr],pts[lctl]);
	    changed = true;
	  }
	}
      } while (changed);

      if (left != null) {
      	faces.addElement(left);
      }
      if (right != null) {
      	faces.addElement(right);
      }

      Edge3d e = new Edge3d(pts[lctl],pts[lctr],frameNo);
      e.lastFrame = frameNo++;
      faces.addElement(e);
			
      /* now the main loop -- keep finding faces that connect the two small hulls until we get back to the LCT */
      int l = lctl;
      int r = lctr;
      do {
	int cand = (l==first)?first+1:first;
	HalfSpace candh = new HalfSpace(pts[l],pts[r],pts[cand]);
	for(int i=cand+1; i <= last; i++) {
	  if (i != l && i != r && candh.inside(pts[i])) {
	    cand = i;
	    candh = new HalfSpace(pts[l],pts[r],pts[cand]);
	  }
	}
	faces.addElement(new Triangle3d(pts[l],pts[r],pts[cand],frameNo++));
	if (cand <= mid) {
	  l = cand;
	} else {
	  r = cand;
	}
      } while(l != lctl || r != lctr);
      faces.centre = new Point3d(pts[mid].x(),0,0);
      deleteFaces(left, mid+1,last);
      deleteFaces(right, first,mid);
      frameNo++;
      return faces;
    }
  }

  /**  mark as deleted faces that can see a point */
  protected void deleteFaces(Object3dList faces,
			     int first, int last){
    if (faces != null) {
      for (int i = 0; i < faces.size(); i++) {
      	Object3d f = faces.elementAt(i);
      	if (f instanceof Triangle3d){
      	  Triangle3d t = (Triangle3d) f;
	  if (t.lastFrame > frameNo){
	    for (int j = first; j <= last; j++){
	      if (t.inside(pts[j])) {
		t.lastFrame = frameNo;
		break;
	      }
	    }
	  }
	} else if (f instanceof Object3dList){
	  deleteFaces((Object3dList)f,first,last);
	}
      }
    }
  
  }

  public Object3dList build2D() {
    sortpts();

    frameNo = 1;
    Object3dList faces = build2D(0,pts.length-1);
    faces.lastFrame = frameNo;
    Object3d o = faces.elementAt(0);
    if (o instanceof Object3dList) {
      ((Object3dList)o).setDefaultColor(rightColor);
    }
    o = faces.elementAt(1);
    if (o instanceof Object3dList) {
      ((Object3dList)o).setDefaultColor(leftColor);
    }
    return faces;
  }
	
  /** recursive build */
  protected Object3dList build2D(int first,int last) {
    if (last-first < 1) {
      return null;
    } else {
      int mid = (first + last)/2;
      Object3dList faces = new Object3dList(5);
      faces.firstFrame=frameNo;
      Object3dList left = build2D(first,mid);
      Object3dList right = build2D(mid+1,last);

      if (left != null) {
      	faces.addElement(left);
      }

      if (right != null) {
      	faces.addElement(right);
      }
      frameNo++;

      faces.centre = new Point3d(pts[mid].x(),0,0);

      PointStack esleft = new PointStack();
      deleteFaces2D(left, mid+1,last,esleft);
      if (esleft.isEmpty()){
	esleft.put(pts[first],pts[first]);
      }

      PointStack esright = new PointStack();
      deleteFaces2D(right, first,mid,esright);
      if (esright.isEmpty()) {
	esright.put(pts[last],pts[last]);
      }

      faces.addElement(new Edge3d(esleft.getStart(),esright.getEnd(),frameNo));
      faces.addElement(new Edge3d(esright.getStart(),esleft.getEnd(),frameNo++));

      return faces;
    }
  }

  /**  mark as deleted faces that can see a point */
  protected void deleteFaces2D(Object3dList faces,
			       int first, int last, PointStack es){
    if (faces != null) {
      for (int i = 0; i < faces.size(); i++) {
      	Object3d f = faces.elementAt(i);
      	if (f instanceof Edge3d){
      	  Edge3d t = (Edge3d) f;
	  if (t.lastFrame > frameNo){
	    for (int j = first; j <= last; j++){
	      if (t.inside(pts[j])) {
		t.lastFrame = frameNo;
		/* update boundary of hole */
		es.put(t.start,t.end);
		break;
	      }
	    }
	  }
	} else if (f instanceof Object3dList){
	  deleteFaces2D((Object3dList)f,first,last,es);
	}
      }
    }
  
  }
}
