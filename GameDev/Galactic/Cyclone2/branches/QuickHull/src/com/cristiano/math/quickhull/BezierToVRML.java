package com.cristiano.math.quickhull;
import java.io.*;
import java.awt.*;
/* transform bezier patch data to a VRML indexed face set */

public class BezierToVRML {
  public static void main (String[] args) {
    int steps = 6;
    if(args.length > 0){
      try {
	steps = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
	System.err.println("Arguments must be an integer to use for no of steps.");
	System.exit(1);
      }
    } 
    NumberStream ns = new NumberStream(System.in);
    try {
      int nopts = (int) ns.next();
      Point3d[] pts = new Point3d[nopts];
      for (int i=0; i<nopts; i++){
	pts[i] = new Point3d(ns.next(),ns.next(),ns.next());
      }
      
      System.out.println("#VRML V2.0 utf8\nGroup {\nchildren [");
      Point3d[][] ctl = new Point3d[4][4];
      int nopatches = (int) ns.next();
      for (int i=0; i<nopatches; i++){
	for (int j=0;j<4;j++){
	  for (int k=0;k<4;k++){
	    ctl[j][k] = pts[(int) ns.next()];
	  }
	}
	Bezier3d b = new Bezier3d(ctl,Color.red,Color.green,steps);
	if (i!=0) {
	  System.out.println(",");
	}
	//b.toVRML(System.out);
      }
      System.out.println("]\n}");
    } catch (IOException e) {
      System.err.println("IOException " + e);
    }
  }
}
