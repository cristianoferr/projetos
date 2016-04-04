package com.cristiano.math.quickhull;

/**
 * Class controlling animation (play stop etc)
 */
import java.awt.*;

public class AnimationWidget extends Panel implements Runnable{ // extends thread so it is runnable
  Viewer viewer; // use to communicate with object that paints image
  protected int frameNo = 0;
  protected int lastFrame = 9999;
  protected Button start,stop,next,prev,first,last;
  protected TextField text;
  protected Thread engine;
  protected Scrollbar scroll;
  protected int delay = BASEDELAY;
  static final int BASEDELAY = 600;
  protected boolean auto = true;  //automatic play?

  public AnimationWidget(Viewer viewer){
    this.viewer = viewer;
    add(start =new Button("Start"));
    add(stop = new Button("Stop"));
    add(next = new Button("+1"));
    add(prev = new Button("-1"));
    add(first = new Button("<<"));
    add(last = new Button(">>"));
    add(new Label("Frame:"));
    add(text = new TextField("0",3));
    add(new Label("Speed:"));
    add(scroll = new Scrollbar(Scrollbar.HORIZONTAL,0,1,-8,12));
  }

  void setFrameNo(int frameNo){
    this.frameNo = frameNo;
    text.setText(Integer.toString(frameNo));
    viewer.putFrameNo(frameNo);
  }
  
  public void setLastFrame(int lastFrame){
    this.lastFrame = lastFrame;
    if (frameNo > lastFrame){
      frameNo = lastFrame;
    }
  }

  public void start(){
    if(engine!=null || frameNo >= lastFrame) {
      setFrameNo(0);
    }
    stop(); //just in case user clicks on start twice
    engine = new Thread(this);
    engine.start();
  }
  
  public void stop(){
    if (engine != null) {
      engine.stop();
      engine = null; // so it can be garbage collected
    }
  }

  /** Determine if animation plays automatically */
  public void setAutoPlay(boolean auto){
    this.auto = auto;
    if (auto) {
      start();
    }
  }


  public void run() {
    while (auto) {
      try {
	Thread.sleep(4000);
	for (int i = 0; i<= lastFrame; i++){
	  setFrameNo(i);
	  Thread.sleep(delay);
	}
      } catch (InterruptedException e) {;}
    } 
	
    while (frameNo < lastFrame) {
      setFrameNo(frameNo+1);
      try {
	Thread.sleep(delay);
      } catch (InterruptedException e) {;}
    }
  }

  public void next() {
    stop();
    if (frameNo < lastFrame) {
      setFrameNo(frameNo+1);
    }
  }
  	
  public void prev() {
    stop();
    if (frameNo > 0){
      setFrameNo(frameNo-1);
    }
  }
  	
  public void first() {
    if (auto) {
      stop();
      start();
    } else {
      stop();
    }
    setFrameNo(0);
  }
  	
  public void last() {
    stop();
    setFrameNo(lastFrame);
  }
  	

  public boolean handleEvent(Event evt){
    if (evt.id == Event.ACTION_EVENT){
      if (evt.target == start) {
	start();
	return true;
      } else if (evt.target == stop) {
	stop();
	return true;
      } else if (evt.target == next) {
	next();
	return true;
      } else if (evt.target == prev) {
	prev();
	return true;
      } else if (evt.target == first) {
	first();
	return true;
      } else if (evt.target == last) {
	last();
	return true;
      } else if (evt.target == text) {
	try {
	  setFrameNo(Integer.parseInt(text.getText()));
	          System.out.println("theframe "+frameNo);
	} catch (NumberFormatException e) {
	  setFrameNo(0);


	}
	return true;
      }
    } else if (evt.id==Event.KEY_PRESS) {
      return keyDown(evt,evt.key);
    } else if (evt.target == scroll) {
      delay = (int)(BASEDELAY*Math.pow(1.4,-scroll.getValue()));
      return true;
    }
    return super.handleEvent(evt);
  }
  
  public boolean keyDown(Event evt, int key) {
    if (key == '.') {
      next();
      return true;
    } else if (key == ',') {
      prev();
      return true;
    } else if (key == '>') {
      first();
      return true;
    } else if (key == '<') {
      last();
      return true;
    }
    return false;
  }

}

