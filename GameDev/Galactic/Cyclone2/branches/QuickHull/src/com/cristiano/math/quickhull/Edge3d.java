package com.cristiano.math.quickhull;
import java.awt.*;

public class Edge3d extends Object3dAdaptor {
  Point3d start,end; //end points
  
  public Edge3d(Point3d start, Point3d end) {
    this.start = start;
    this.end = end;
    centre = start;
  }

  public Edge3d(Point3d start, Point3d end, int frameNo) {
    this(start,end);
    this.firstFrame = frameNo;
  }
  
  /** render the edge, given a 3D view */
  public void render(View3d v){
    Point s, e;
    s = v.toPoint(start);
    e = v.toPoint(end);
    Color c = v.getColor(getColorIndex(v,-1));
    if (c != null){
      v.g.setColor(c);
      v.g.drawLine(s.x,s.y,e.x,e.y);
    }
  }
  
  public boolean equals(Object o) {
    if (o instanceof Edge3d) {
      Edge3d e = (Edge3d) o;
      return (start == e.end && end == e.start) ||
	(end == e.end && start == e.start);
    } else {
      return false;
    }
  }

  public boolean inside (Point3d x){
    HalfSpace h = new HalfSpace(start,end);
    return h.inside(x);
  }

  public String toString(){
    return"("+start+","+end+")";
  }
  public String vrmlPROTOName() {
    return "Edge3d";
  }

  public String vrmlPROTOExtraFields() {
    return 
      super.vrmlPROTOExtraFields()+
      "  field MFVec3f point [] \n"+
      "  field MFColor color 0 0 0 \n";
  }

  public String[] vrmlPROTOMaterials() {
    String[] result = super.vrmlPROTOMaterials();
    for (int i=0; i < result.length; i++){
      result[i] += "point IS point\n";
    }
    result[0] += "color IS color\n";
    result[1] += "color 1 0 0\n";
    result[2] += "color 1 1 0\n";
    return result;
  }

  public String vrmlPROTOINBody() {
    return
      "Shape {\n"+
      "	appearance Appearance {\n"+
      "	  material IS material\n"+
      "	}\n"+
      " geometry IndexedLineSet {\n"+
      "  coord Coordinate {\n"+
      "    point IS point\n"+
      "      }\n"+
      "  coordIndex [0,1,-1]\n"+
      "	  color Color{ color IS color}\n"+
      "	  colorPerVertex FALSE\n"+
      "	}\n"+
      "}\n";
  }


  public String toVRMLBody(VRMLState v) {
    StringBuffer s = new StringBuffer();
    String vcol = v.getVRMLColor(-1);
    if (vcol!=null) {
      s.append("color "+vcol+"\n");
    }
    s.append("point ["+start.toVRML()+","+end.toVRML()+"]\n");
    return s.toString();
  }
}
