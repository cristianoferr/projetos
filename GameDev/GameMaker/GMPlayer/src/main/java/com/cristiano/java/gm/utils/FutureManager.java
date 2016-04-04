package com.cristiano.java.gm.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.utils.Log;

public class FutureManager {
	static Map<String, Future<Object>> mapping = new HashMap<String, Future<Object>>();
	static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			JMEConsts.THREAD_QUANTITY);//quantas threads paralelas?
	
	public static Future<Object> requestFuture(String name,Callable<Object> method){
		Future<Object> future=executor.submit(method);
		mapping.put(name,future);
		Log.info("Future '"+name+"' requested, there are "+mapping.size()+" futures.");
		return future;
	}
	
	public static boolean isDone(String name){
		if (name==null){
			return false;
		}
		Future future=getFuture(name);
		if (future==null){
			return false;
		}
		return future.isDone();
	}

	public static Future getFuture(String name) {
		Future<Object> future = mapping.get(name);
		if (future==null){
			Log.warn("Future with name '"+name+"' is null");
		}
		return future;
	}
	
	public static void removeFuture(String name){
		mapping.remove(name);
	}

	public static Object retrieveFuture(String name) {
		try {
			Future future = getFuture(name);
			if (future==null){
				Log.error("Future is null for: "+name);
				return null;
			}
			Object o= future.get();
			mapping.remove(name);
			Log.info("Future removed: '"+name+"'.  There are "+mapping.size()+" futures remaining.");
			return o;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void destroy() {
		executor.shutdown();
	}
}
