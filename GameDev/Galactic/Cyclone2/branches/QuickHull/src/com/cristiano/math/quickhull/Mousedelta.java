package com.cristiano.math.quickhull;
/**
 * 
This class is used for communication between the thread that responds to
 * user input and the one that does the drawing
 */
import java.awt.*;

public class Mousedelta {
  private int deltax=0;
  private int deltay=0;
  private int deltax2=0;
  private int deltay2=0;
  private int deltax3=0;
  private int deltay3=0;
  private int frameNo=0;
  private boolean repaint=false;
  private boolean animating=false;

  public synchronized void put (int deltax, int deltay, int modifiers){
  	if ((Event.ALT_MASK & modifiers) !=0) {
  		deltax2 += deltax;
  		deltay2 += deltay;
  	} else if ((Event.META_MASK & modifiers) !=0) {
   		deltax3 += deltax;
  		deltay3 += deltay;
    } else if (modifiers == 0) {
    	this.deltax += deltax;
        this.deltay += deltay;
    }
    repaint = true;
    notify();
  }

  public synchronized void put (){
    repaint = true;
    notify();
  }

  public synchronized void putFrameNo(int frameNo) {
    this.frameNo = frameNo;
    repaint = true;
    notify();
  }

  public synchronized void startAnimation (){
    animating = true;
    repaint = true;
    notify();
  }

  public synchronized void stopAnimation (){
    animating = false;
  }


  public synchronized MouseInfo get(){
    while (!repaint) {
      try {
	wait();
      } catch (InterruptedException e) {
      }
    }
    MouseInfo m = new MouseInfo(deltax,deltay,deltax2,deltay2,deltax3,deltay3,frameNo);
    deltax = 0;
    deltay = 0;
    deltax2 = 0;
    deltay2 = 0;
    deltax3 = 0;
    deltay3 = 0;
    repaint = animating; //only need to repaint again if we're animating
    return m;
  }

} 
