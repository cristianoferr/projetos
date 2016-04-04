package com.cristiano.math.quickhull;
import java.util.*;
import java.awt.*;

public class Object3dList extends Object3dAdaptor{
  
  protected double[] keys;
  protected Object3d[] elementData;
  protected int elementCount;
  protected long lasttime=0;
  protected int delay=0;
  protected int defaultColor=-1;
  
  static Timer sortTimer = new Timer("sort");
  static Timer renderTimer = new Timer("render");


  public Object3dList(int initial){
    elementData = new Object3d[initial];
    elementCount = 0;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public void setDefaultColor(int c){
    defaultColor = c;
  }
  
  public void addElement(Object3d e){
    if(elementData.length==elementCount) {
      Object3d[] newData = new Object3d[1+2*elementData.length];
      System.arraycopy(elementData,0,newData,0,elementCount);
      elementData = newData;
    }
    elementData[elementCount++] = e;
    this.centre = e.centre();
  }
  
  public void append(Object3dList l){
    for (int i = 0; i <l.size(); i++){
      addElement(l.elementAt(i));
    }
  }

  public Object3d elementAt(int i){
    return elementData[i];
  }
  
  public int size(){
    return elementCount;
  }

  void sort(){
    int hi,lo,end;
    boolean changed = true;
    for(end = elementCount; changed; end--){
      changed = false;
      for(hi = 1; hi < end; hi++){
	lo = hi - 1;
	if (keys[lo] < keys[hi]) {
	  Object3d T = elementData[lo];
	  elementData[lo] = elementData[hi];
	  elementData[hi] = T;
	  double d = keys[lo];
	  keys[lo] = keys[hi];
	  keys[hi] = d;
	  changed = true;
	}
      }
    }
  }

  /** ensure keys array exists */
  void initKeys(){
    if (keys == null || keys.length != elementCount) {
      keys = new double[elementCount];
    }
  }    
  
  /** sort by firstFrame */
  public void sortByFirstFrame(){
    initKeys();
    for (int i=0; i < elementCount; i++){
      keys[i] = elementData[i].getFirstFrame();
    }
    sort();
  }

  /** sort by lasstFrame */
  public void sortByLastFrame(){
    initKeys();
    for (int i=0; i < elementCount; i++){
      keys[i] = elementData[i].getLastFrame();
    }
    sort();
  }
    
  /** sort by selectFrame */
  public void sortBySelectFrame(){
    initKeys();
    for (int i=0; i < elementCount; i++){
      keys[i] = elementData[i].getSelectFrame();
    }
    sort();
  }
    
  public void sort(View3d v){
    initKeys();
    for (int i=0; i < elementCount; i++){
      Object3d ei = elementData[i];
      keys[i] = v.depth(ei.centre())+ei.depthBias(v);
      if (ei instanceof Object3dList) {
	((Object3dList)ei).sort(v);
      }
    }
    sort();
  }
  
  
  public void render(View3d v){
    int save=-1;
    sortTimer.start(); sort(v); sortTimer.stop();
    int frame = v.getFrameNo();
    if (defaultColor!=-1) {
      save = v.setDefaultColor(defaultColor);
    }
    renderTimer.start();
    for (int i = elementCount-1; i>= 0; i--){
      if (elementData[i].visible(frame)) { 
	(elementData[i]).render(v);
	if (delay > 0) {
	  try {
	    Thread.sleep(delay);
	  } catch (InterruptedException e) {;}
	}
      }
    }
    renderTimer.stop();
    if (defaultColor!=-1) {
      v.setDefaultColor(save);
    }
    
  }

  public void transform(Matrix3D T){
    for (int i = elementCount-1; i>= 0; i--){
      (elementData[i]).transform(T);
    }
  }

  public void toVRML(VRMLState v){
    int save=-1;
    if (defaultColor!=-1) {
      save = v.setDefaultColor(defaultColor);
    }
    for (int i = elementCount-1; i>= 0; i--){
      elementData[i].toVRML(v);
    }
    if (defaultColor!=-1) {
      v.setDefaultColor(save);
    }
  }
}



