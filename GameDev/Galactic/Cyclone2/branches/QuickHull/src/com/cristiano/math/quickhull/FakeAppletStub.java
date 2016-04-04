package com.cristiano.math.quickhull;
import java.applet.*;
import java.net.*;
/** Fake AppletStub so that an Applet can run as as application */
public class FakeAppletStub implements AppletStub {

  String docBase;

  public FakeAppletStub(String docBase){
    this.docBase = docBase;
  }

  public boolean isActive(){
    return false;
  }

  public URL getDocumentBase(){
    try {
      return new URL("file://"+docBase);
    } catch (MalformedURLException e) {
      return null;
    }
  }
  public URL getCodeBase(){
    return null;
  }

  public String getParameter(String name){
    return null;
  }
  
  public AppletContext getAppletContext(){
    return null;
  }
  
  public void appletResize(int width,
                                   int height){
  }
}
