package com.cristiano.galactic.view.demos;

import com.cristiano.cyclone.utils.Quaternion;
import com.jme3.math.Vector3f;

public class QuatCompute implements Runnable {
	 
    public double accumall=0;
    final Vector3f dir;
    final Vector3f up;
    long runtime;
    final long count;
    int id;
    
    public QuatCompute(Vector3f dir, Vector3f up, long count,int id) {
        this.dir = dir;
        this.up = up;
        this.count = count;
        this.id=id;
    }
    
    public void run() {
        // replace with QuaternionT for ThreadLocal version
        Quaternion quat=new Quaternion();
        runtime=System.currentTimeMillis();
        double accum=0;
        for(long i=0;i<count;i++) {
            quat.lookAt(dir, up);
            accum+= quat.getW() + quat.getX() + quat.getY() + quat.getZ();
            System.out.println("id:"+id+" i:"+i);
        }
        runtime=System.currentTimeMillis()-runtime;
        accumall = accum;
    }
}
