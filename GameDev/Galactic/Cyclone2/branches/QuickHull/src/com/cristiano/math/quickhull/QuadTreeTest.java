package com.cristiano.math.quickhull;
import java.io.*;
import java.awt.*;
import java.util.*;
/* Test if QuadTree works*/

public class QuadTreeTest {
  public static void main (String[] args) {
    Point3d[] pts = new Point3d[Integer.parseInt(args[0])];
    for(int i = 0; i<pts.length; i++) {
      pts[i] = (Point3d.randomInSphere());
    }
    QuadTree q = QuadTree.build(pts);
    q.dump();
  }
}
