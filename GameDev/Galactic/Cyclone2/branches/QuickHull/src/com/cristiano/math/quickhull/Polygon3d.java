package com.cristiano.math.quickhull;
import java.awt.*;

public class Polygon3d extends Object3dAdaptor {

  Point3d[] pol ;
  Color col;
  
  /** Create a polygon with given colour
   */
  public Polygon3d(Point3d[] pol,Color col){
    centre = pol[0].add(pol[1]).add(pol[2]).scale(1.0/3.0);
    this.pol = pol;
    this.col = col;
  }


  /** render the poly, given a 3D view */
  public void render(View3d v){
    int[] ix = new int[pol.length]; // pol in
    int[] iy = new int[pol.length]; // screen space
      
    // first transform corners to screen space
    Point p;
    for (int i = 0; i < pol.length; i++){
      p = v.toPoint(pol[i]);
      ix[i] = p.x;
      iy[i] = p.y;
    }
    v.g.setColor(col);
    v.g.fillPolygon(ix,iy,pol.length);
    v.g.setColor(Color.black);
    v.g.drawPolygon(ix,iy,pol.length);
  }

  /** compute new position of pol
   */
  public void transform(Matrix3D T){
      T.transform(pol);
      centre = pol[0].add(pol[1]).add(pol[2]).scale(1.0/3.0);
  }

}
