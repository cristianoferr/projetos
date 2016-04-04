package com.cristiano.math.quickhull;
import java.awt.*;
import java.util.*;


public class Points3d extends Object3dAdaptor{
  
  public static int color;
  protected Point3dObject3d[] pts;
  protected Vector labels;
  private Object3dList model; //display this one at frame zero
  
  public Points3d() {
    centre = Point3d.o;
    pts = null;
    labels = null;
  }
  
  public double depthBias(View3d v) {
    return 4*FRONTBIAS;
  }
  
  public Points3d set(Point3dObject3d[] pts){
    this.pts = pts;
    labels = null;
    model = new Object3dList(pts.length);
    for (int i=0; i <pts.length; i++) {
      model.addElement(pts[i]);
    }
    model.lastFrame = 0;
    return this;
  }

  public void setLabels(Vector labels){
    this.labels = labels;
  }
  
  public void render (View3d v) {
    if (v.getFrameNo()==0 && pts != null) {
      model.render(v);
    } else {
      Color c = v.getColor(color);
      if (c != null && pts != null) {
	v.g.setColor(c);
	for (int i = 0; i < pts.length; i++) {
	  Point s = v.toPoint(pts[i]);
	  v.g.drawRect(s.x-1,s.y-1,3,3);
	}
      }
      if (labels != null){
	v.g.setColor(Color.black);
	StringBuffer sb = new StringBuffer(" ");
	for (int j = 0; j < labels.size(); j++) {
	  int i = ((Integer) labels.elementAt(j)).intValue();
	  if (i>=0&&i<pts.length){
	    sb.setCharAt(0,(char)('A'+j));
	    v.drawStringBelow(sb.toString(),pts[i]);
	  }
	}
      }
    }
  }

  public void toVRML(VRMLState v){
    int save=-1;
    if (color!=-1) {
      save = v.setDefaultColor(color);
    }
    for (int i = pts.length-1; i>= 0; i--){
      pts[i].toVRML(v);
    }
    if (color!=-1) {
      v.setDefaultColor(save);
    }
  }
}
