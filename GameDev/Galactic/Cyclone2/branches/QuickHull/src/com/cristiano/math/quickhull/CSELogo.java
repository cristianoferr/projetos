package com.cristiano.math.quickhull;
public class CSELogo extends Object3dList{
    static double[][] logodata = { // initial positions of cubes
    {2,2,2},{1,2,2},{2,2,1},{2,0,2},{0,2,2},{2,2,0},{1,0,2},{-1,2,2},{2,-1,2},
    {2,0,1},{2,2,-1},{2,-2,2},{0,0,2},{-2,2,2},{2,0,0},{2,2,-2},{1,2,-2},
    {1,-2,2},{-1,0,2},{-2,1,2},{2,-2,1},{2,0,-1},{-2,2,1},{0,-2,2},{-2,0,2},
    {2,-2,0},{2,0,-2},{-2,2,0},{2,-2,-1},{-2,2,-1},{-1,2,-2},{-1,-2,2},
    {-2,-2,2},{2,-2,-2},{-2,2,-2}};

  static double cuberadius = 0.35; // radius of cubes in logo
  public CSELogo() {
    super(logodata.length);
    for (int i=0; i < logodata.length; i++){
      Cube c = new Cube(new Point3d(logodata[i][0],
					    logodata[i][1],
					    logodata[i][2]),cuberadius);
      addElement(c);      
    }
  }    
}
