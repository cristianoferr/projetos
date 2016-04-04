package com.cristiano.math.quickhull;

import java.util.*;
/** This class stores state information needed to convert an Object3d
 to VRML. */
public class VRMLState {
  private View3d view;
  private Viewer viewer;
  //Stores all prototypes required for all object3d
  private Hashtable vrmlPROTOs = new Hashtable();
  private StringBuffer sb = new StringBuffer();
  private Object3dList appear = new Object3dList(10);
  private Object3dList disappear = new Object3dList(10);
  private Object3dList select = new Object3dList(10);
  private StringBuffer suffix = new StringBuffer(); //stuff to add to end

  public VRMLState(Viewer viewer){
    this.viewer = viewer;
    view = viewer.getView();
  }

  public int getLastFrame() {
    return view.getLastFrame();
  }

  public Viewer getViewer() {
    return viewer;
  }

  //VRML identifier for a colour
  public String idVRML(int col){
    return viewer.idVRML(col);
  }

  public String getPROTO(String name){
    return (String) vrmlPROTOs.get(name);
  }

  public void putPROTO(String name, String body){
    vrmlPROTOs.put(name,body);
  }

  public void append(String s){
    sb.append(s);
  }

  public void appendToSuffix(String s){
    suffix.append(s);
  }

  /* add to list of objects that appear */
  public void addAppear(Object3d o){
    appear.addElement(o);
  }

  public Object3dList getAppear() {
    return appear;
  }

  public Object3dList getDisappear() {
    return disappear;
  }

  public Object3dList getSelect() {
    return select;
  }

  public void addDisappear(Object3d o){
    disappear.addElement(o);
  }

  public void addSelect(Object3d o){
    select.addElement(o);
  }

  public int setDefaultColor(int col) {
    return view.setDefaultColor(col);
  }

  public String getVRMLColor(int col){
    return view.getVRMLColor(col);
  }

  //Protypes of all objects defined in this State
  public String vrmlAllPROTOs(){
    StringBuffer s = new StringBuffer();
    for (Enumeration e = vrmlPROTOs.elements() ; e.hasMoreElements() ;) {
      s.append((String)e.nextElement());
    }
    return s.toString();
  }

  public String toString() {
    return vrmlAllPROTOs() + sb.toString() + suffix.toString();
  }

}
