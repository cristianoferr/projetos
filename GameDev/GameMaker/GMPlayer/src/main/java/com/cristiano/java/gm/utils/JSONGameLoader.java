package com.cristiano.java.gm.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.simple.JSONObject;

import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
/*
 * This call will make use of concurrent loadings (multi-threading) to load game properties in a fast way)
 * */
class Jsonloader{
	public Callable<JSONObject> load = new Callable<JSONObject>() {
		public JSONObject call() throws Exception {
			JSONObject obj = GMAssets.readJSON(GMAssets.getJSONPath(type));
			return obj;
		}
	};
	private String type;

	Jsonloader(String type){
		this.type=type;
	}
}

public abstract class JSONGameLoader {
	static Map<String, Future<JSONObject>> mapping = new HashMap<String, Future<JSONObject>>();

	public static void loadJSON(String type) {
		Future<JSONObject> future=FutureManager.executor.submit(new Jsonloader(type).load);
		mapping.put(type,future);
	}

	public static JSONObject getJSON(String type) {
		Future<JSONObject> future = mapping.get(type);
		if (future == null) {
			Log.fatal("Unknown json type:" + type);
		}
		while (!future.isDone()) {
			Log.debug("json "+type+" not done...");
			CRJavaUtils.sleep(50);
		}
		JSONObject obj=null;
		try {
			obj = (JSONObject) future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		mapping.remove(type);
		return obj;
	}
}
