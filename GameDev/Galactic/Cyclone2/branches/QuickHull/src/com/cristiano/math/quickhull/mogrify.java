package com.cristiano.math.quickhull;
import java.io.*;
/* transform bezier patch data to a more convenient form */

class mogrify {
  public static void main (String[] args) {
    NumberStream ns = new NumberStream(System.in);
    PrintStream o = System.out;
    double t;
    try {
      int nopts = (int) ns.next();
      o.println(nopts);
      for (int i=0; i<nopts; i++){
	ns.next();
	o.println(ns.next()+" "+ns.next()+" "+ns.next());
      }
      int nopatches = (int) ns.next();
      o.println(nopatches);
      for (int i=0; i<nopatches; i++){
	for (int j=0;j<16;j++){
	  int k = Math.abs((int) ns.next());
	  o.print(k-1+" ");
	}
	o.println();
      }
    } catch (IOException e) {
      System.err.println("IOException " + e);
    }
  }
}
