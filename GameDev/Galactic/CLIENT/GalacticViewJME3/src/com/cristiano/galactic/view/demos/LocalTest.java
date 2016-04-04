package com.cristiano.galactic.view.demos;

import com.cristiano.cyclone.utils.Quaternion;
import com.jme3.math.Vector3f;

public class LocalTest {
	 
    public static long runs = 1000000;
    
    public static long runSingle() {
        Vector3f dir=new Vector3f(2f, 0.1f, 1.5f );
        Vector3f up = new Vector3f(0.1f, 1f, 0.1f );
        Quaternion quat=new Quaternion();
        
        double accum=0;
        long runtime=System.currentTimeMillis();
        for(long i=0;i<runs;i++) {
            quat.lookAt(dir, up);
            accum+= quat.getW() + quat.getX() + quat.getY() + quat.getZ();
        }
        runtime=System.currentTimeMillis()-runtime;
        System.out.println("Checksum "+accum+" should be 1.345954418182373E7");
        return runtime;
    }
    
    public static long runSingleT() {
        Vector3f dir=new Vector3f(2f, 0.1f, 1.5f );
        Vector3f up = new Vector3f(0.1f, 1f, 0.1f );
        Quaternion quat=new Quaternion();
        
        double accum=0;
        long runtime=System.currentTimeMillis();
        for(long i=0;i<runs;i++) {
            quat.lookAt(dir, up);
            accum+= quat.getW() + quat.getX() + quat.getY() + quat.getZ();
        }
        runtime=System.currentTimeMillis()-runtime;
        System.out.println("Checksum "+accum+" should be 1.345954418182373E7");
        return runtime;
    }
    
    public static long runMultiple(int num) {
        System.out.println("Threads: "+num);
        Vector3f dir=new Vector3f(2f, 0.1f, 1.5f );
        Vector3f up = new Vector3f(0.1f, 1f, 0.1f );
        
        long allruntime=System.currentTimeMillis();
        
        // start all the threads
        QuatCompute computes[] = new QuatCompute[num];
        for(int i = 0; i < num ; i++) {
            computes[i]=new QuatCompute(dir, up, runs/num,i);
            new Thread(computes[i]).start();
        }
 
        // wait till all the threads complete
        boolean more=true;
        while(more ) {
            try {
                Thread.sleep(500);
                System.out.println("loop");
                more=false;
                for(int i=0; (i < num) && !more ; i++) {
                    if(computes[i].accumall == 0) {
                        more=true;
                    }
                }
            } catch ( Exception e) {
 
            }
        }
        
        // calculate total checksum and the biggest run time
        double accum=0;
        long runtime=0;
        for(int i=0;i<num;i++) {
            accum+=computes[i].accumall;
            if(computes[i].runtime>runtime) {
                runtime=computes[i].runtime;
            }
        }
        allruntime=System.currentTimeMillis()-allruntime;
        System.out.println("Checksum "+accum+" should be 1.345954418182373E7");
        System.out.println("Total runtime " + allruntime);
        return runtime;
    }
    
    public static void main(String[] args) {
        System.gc(); System.gc(); System.gc(); System.gc();
        System.gc(); System.gc(); System.gc(); System.gc();
        System.gc(); System.gc(); System.gc(); System.gc();
        System.gc(); System.gc(); System.gc(); System.gc();
        System.out.println(System.getProperty("java.version"));
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors());
        long usedmem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        long runtime = //runSingleT();
                //runSingle();
                runMultiple(4);
        
        System.out.println("Runtime: " + runtime);
        long usedmem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Consumed memory: " + (usedmem1 - usedmem)/1024);
    }
 
}