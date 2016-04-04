package com.cristiano.math.quickhull;
/** interface for 3d objects that can be rendered on screen */
public interface Object3d{
  public abstract Point3d centre();
  public abstract void setCentre(Point3d c);
  public abstract void setFirstFrame(int f);
  public abstract  int getLastFrame();
  public abstract void setLastFrame(int f);
  public abstract  int getFirstFrame();
  public abstract void select(int f);
  public abstract  int getSelectFrame();
  /** Depth bias - used to adjust depth in depth sort so that front facing  faces
            appear in front of back faces */ 
  public abstract double depthBias(View3d v);
  public static final double FRONTBIAS = 1000.;//bias for front faces
  public static final double BACKBIAS = -1000.;//bias for back faces
  /** Is this object visible in specified frame ? */
  public abstract  boolean visible(int frame);
  /** render this object3d */
  public abstract void render(View3d v);

  public abstract void transform(Matrix3D T);
  /** turn into VRML */
  public abstract void toVRML(VRMLState v);
  public abstract String id();

}
