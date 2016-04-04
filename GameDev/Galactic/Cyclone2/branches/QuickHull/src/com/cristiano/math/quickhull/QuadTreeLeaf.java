package com.cristiano.math.quickhull;
/** leaf node of a quadtree
*/
class QuadTreeLeaf extends QuadTree {
  static final int BUCKETSIZE = 10;
  Point3d[] pts;
  int no;
	
  QuadTreeLeaf(Point3d ne, Point3d se, Point3d nw, Point3d sw){
    super(ne,se,nw,sw);
    no = 0;
    pts = new Point3d[BUCKETSIZE];
  }
	
  QuadTree insert(Point3d p){
    QuadTree result;
    super.insert(p);
    if (no < BUCKETSIZE) {
      pts[no++] = p;
      result = this;
    } else {
      //split
      Point3d n = mid(pne,pnw);
      Point3d s = mid(pse,psw);
      Point3d e = mid(pne,pse);
      Point3d w = mid(pnw,psw);
      Point3d c = mid(n,s);
      QuadTree ne = new QuadTreeLeaf(pne, e, n, c); 
      QuadTree nw = new QuadTreeLeaf(n, c, pnw, w); 
      QuadTree se = new QuadTreeLeaf(e, pse, c, s); 
      QuadTree sw = new QuadTreeLeaf(c, s, w, psw);
      result = new QuadTreeInternal(pne, pse, pnw, psw, ne, se, nw, sw);
      for (int i = 0; i < no; i++){
	result=result.insert(pts[i]);
      }
      result=result.insert(p);
    }
    return result;
  }
	
  /* mid point of a line */
  Point3d mid (Point3d a, Point3d b) {
    return a.add(b).scale(0.5);
  }

  /** check all points in this quadtree to see if they're inside candh
            case where we're looking for points above (greater z) */
  protected void findup(double zint){
  	for(int i=1; i < no; i++) {
  		if (pts[i] != start && pts[i] != end && candh.inside(pts[i])) {
  			cand = pts[i];
  			candh = new HalfSpace(start,end,cand);
  		}
  	}
	}

  /** check all points in this quadtree to see if they're inside candh
            case where we're looking for points above (greater z) */
  protected void finddown(double zint) {
  	findup(zint);
  }

  void dump(int indent){
    out(indent,this);
    super.dump(indent);
    for (int i = 0; i < no; i++){
      out(indent,pts[i]);
    }
  }

}
