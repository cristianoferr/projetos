package com.cristiano.cyclone.utils;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


/*
 * Created on 08/07/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class ClockData{
	long min=0;
	long max=0;
	long tot;
	int qtdCalls=0;
	public Date inicial=new Date();
	
	public ClockData(){
		startClock();
	}
	
	public void startClock(){
		inicial=new Date();
	}
	
	public void stopClock(){
		qtdCalls++;
		Date now=new Date();
		long i2=now.getTime();
		long i1=inicial.getTime();
		long dif=(i2-i1);
		if ((dif<min) || (min==0))min=dif;
		if (dif>max) max=dif;
		tot=tot+dif;
	}
	
	public String toString(){
		return "calls:"+qtdCalls+" max:"+max+" min:"+min+" tot:"+tot+" avg:"+(float)((float)tot/(float)qtdCalls);
	}
}

public class ClockWatch  {
static HashMap<String,ClockData> dados=new HashMap<String,ClockData>();

	/**
	 * 
	 */
	
	public static void startClock(String name){
		ClockData ck=dados.get(name);
		if (ck==null){
			ck=new ClockData();
			dados.put(name, ck);
		} else{
			ck.startClock();
		}
		
	}
	public static void stopClock(String name){
		ClockData ck=dados.get(name);
		ck.stopClock();
	}
	
	public static void report(){
		System.out.println("CLOCK WATCH REPORT");
		Iterator iterator = dados.keySet().iterator();
		double avg=0;
	    while (iterator.hasNext()) {
	      String key = (String) iterator.next();
	      ClockData ck=dados.get(key);
	      System.out.println(key+":"+ck);
	      avg+=(float)((float)ck.tot/(float)ck.qtdCalls);
	    }
	    avg=avg/dados.size();
	    System.out.println("REPORT END.  avg:"+avg);
	    
	}

}
