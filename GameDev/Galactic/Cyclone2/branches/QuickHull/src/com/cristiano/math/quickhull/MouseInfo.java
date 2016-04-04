package com.cristiano.math.quickhull;
/**
 * This class is used to store the information about a collection of mouse movements
 */

public class MouseInfo {
  public int deltax=0;
  public int deltay=0;
  public int deltax2=0;
  public int deltay2=0;
  public int deltax3=0;
  public int deltay3=0;
  public int frameNo=0;
  
  public MouseInfo(int x, int y, int x2, int y2, int x3, int y3, int frameNo) {
  	deltax = x;
  	deltay = y;
  	deltax2 = x2;
  	deltay2 = y2;
  	deltax3 = x3;
  	deltay3 = y3;
  	this.frameNo = frameNo;
  }

} 
