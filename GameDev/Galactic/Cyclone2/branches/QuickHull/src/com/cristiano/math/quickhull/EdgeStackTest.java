package com.cristiano.math.quickhull;
import java.io.*;
import java.awt.*;
import java.util.*;
/* Test if EdgeStack works*/

public class EdgeStackTest {
  public static void main (String[] args) {
  	Point3d[] pts = new Point3d[10];
  	for(int i = 0; i<pts.length; i++) {
  		pts[i] = (Point3d.randomOnSphere());
  	}
	/*GiftWrap g = new GiftWrap(pts);
	Vector v = g.build();
	Enumeration e = v.elements();
	while (e.hasMoreElements()) {
		System.out.println((Triangle3d)e.nextElement());
	}*/
}
}
