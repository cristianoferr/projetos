package com.cristiano.math.quickhull;
import java.awt.*;
public interface Painter extends Runnable{  

  public void setModel(Object3d model);

  public void paint(Graphics g);

  public void run();
}
