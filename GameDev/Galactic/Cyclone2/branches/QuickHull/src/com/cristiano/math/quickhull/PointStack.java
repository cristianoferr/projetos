package com.cristiano.math.quickhull;
import java.util.*;
/** This class stores the points that still need to be processed.
  It is not really a stack at all, but its use in 2d is analogous to the use
  of EdgeStack in 3d
  */
public class PointStack {
  private Vector starts; // unmatched start points
  private Vector ends; // unmatched end points
  	
  public PointStack() {
    starts = new Vector();
    ends = new Vector();
  }
  	
  public boolean isEmpty(){
    return starts.isEmpty();
  }

  public void put(Point3d start, Point3d end) {
    if (!ends.removeElement(start)){
      starts.addElement(start);
    }
    if (start==end || !starts.removeElement(end)){
      ends.addElement(end);
    }
  }

  public Point3d getStart(){
    Point3d p = (Point3d)starts.lastElement();
    starts.removeAllElements();
    return p;
  }

  public Point3d getEnd(){
    Point3d p = (Point3d)ends.lastElement();
    ends.removeAllElements();
    return p;
  }



}
  			
