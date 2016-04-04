package com.cristiano.math.quickhull;
import java.util.*;
/** Constructs a convex hull via giftwrapping algorithm
*/

public class GiftWrap extends HullAlgorithm{
  public GiftWrap(Point3dObject3d[] pts) {
    super(pts);
  }
	
  int index(Point3d p) {
    for(int i=0; i<pts.length; i++){
      if (p==pts[i]) {
	return i;
      }
    }
    return -1;
  }
  
  protected Point3d search(Edge3d e) {
      int i;
      for(i = 0; pts[i] == e.start || pts[i] == e.end; i++) {
	/* nothing */
      }
      Point3d cand = pts[i];
      HalfSpace candh = new HalfSpace(e.start,e.end,cand);
      for(i=i+1; i < pts.length; i++) {
	if (pts[i] != e.start && pts[i] != e.end && candh.inside(pts[i])) {
	  cand = pts[i];
	  candh = new HalfSpace(e.start,e.end,cand);
	}
      }
      return cand;
}

  protected Point3d search2d(Point3d p) {
      int i;
      i = pts[0] == p?1:0;
      Point3d cand = pts[i];
      HalfSpace candh = new HalfSpace(p,cand);
      for(i=i+1; i < pts.length; i++) {
	if (pts[i] != p && candh.inside(pts[i])) {
	  cand = pts[i];
	  candh = new HalfSpace(p,cand);
	}
      }
      return cand;
}

  /* bottom point */
  protected Point3d bottom(){
    Point3d bot = pts[0];
    for (int i = 1; i < pts.length; i++) {
      if (pts[i].y() < bot.y()) {
	bot = pts[i];
      }
    }
    return bot;
  }
	
  public Object3dList build () {
    /* First find a hull edge -- just connect bottommost to second from bottom */
    Point3d bot, bot2; /* bottom point and adjacent point*/
    bot = bottom();
    bot2 = search2d(bot);
		
    /* intialize the edge stack */
    EdgeStack es = new EdgeStack();
    es.put(bot,bot2);	
    es.put(bot2,bot);
    Object3dList faces = new Object3dList(20);
    int tcount = 1;
    Edge3d e = new Edge3d(bot,bot2,tcount);
    e.lastFrame = tcount++;
    faces.addElement(e);
		
    /* now the main loop -- keep finding faces till there are no more to be found */
    int i=0;
    while (! es.isEmpty() ) {
      e = es.get();
      Point3d cand = search(e);
      faces.addElement(new Triangle3d(e.start,e.end,cand,tcount++));
      es.putp(e.start,cand);
      es.putp(cand,e.end);
      //System.out.println("I:"+i);
      i++;
    }
    faces.lastFrame = tcount;
    return faces;
  }


  public Object3dList build2D() {
    /* First find a hull vertex -- just bottommost*/
    Point3d p; /* current hull vertex */
    Point3d bot = bottom(); /* bottom point */
		
    Object3dList faces = new Object3dList(20);
    int tcount = 1;
    faces.addElement(new Point3dObject3d(bot,tcount++));
		
    /* now the main loop -- keep finding edges till we get back */

    p = bot;
    do {
      Point3d cand = search2d(p);
      faces.addElement(new Edge3d(p,cand,tcount++));
      p = cand;
    } while (p!=bot);
    faces.lastFrame = tcount;
    return faces;
  }
	
}


