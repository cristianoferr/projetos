package com.cristiano.math.quickhull;
import java.lang.*;
import java.util.*;

public class Timer {

int count =0;
long starttime;
String name;
long cumtime=0;

static Vector timers = new Vector();

public Timer(String s){
  name = s;
  timers.addElement(this);
}

public static void printall(){
  Enumeration e = timers.elements();
  while (e.hasMoreElements()) {
    System.out.println(e.nextElement());
  }
}

public void start() {
  starttime = System.currentTimeMillis();
}

public void stop() {
   cumtime += System.currentTimeMillis() - starttime;
   count++;
}

public String toString() {
  if (count > 0) {
    String ret = (name + " " + count + " " + cumtime / (double) count);
    count = 0;
    cumtime = 0;
    return ret;
  } else {
    return name;
  }
}
}
