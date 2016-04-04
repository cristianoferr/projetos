package com.cristiano.math.quickhull;
/**
 * 
Class controlling painting of animated 3D logo.
 */
import java.awt.*;

public class Paint3d extends Canvas implements Painter{ 
  Viewer viewer;
  Image offscreen; //offscreen bitmap to avoid flickering
  Object3d model;
  Graphics g; //we need this to work around a JDK bug

  
  /** Create a canvas for rendering model in.
   */

  public Paint3d(Viewer viewer){
    this.viewer = viewer;
    this.model=model;
  }
  
  public void setModel(Object3d model) {
    this.model = model;
    viewer.put();
  }

  public void paint(Graphics g) {
    g.drawImage(offscreen,0,0,null);
  }

  public void run() {
    for (;;) {
      View3d v = viewer.get();
      v.clear();
      model.render(v);
      Graphics g = getParent().getGraphics(); //bug workaround - ought to be getGraphics()
      if (g != null) {
	g.drawImage(offscreen,0,0,null);
      }
    }
  }

  public void resize(int w, int h) {
    offscreen = createImage(w,h);
    viewer.setGraphics(offscreen.getGraphics(),w,h);
  }

  public void reshape(int x, int y,int w, int h) {
    resize(w,h);
  }

}

