package com.cristiano.java.gm.ecs.comps.persists;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class TextKey implements ISerializeJSON{
	public HashMap<String,String> values=new HashMap<String,String>();//key:language, value:text

	public TextKey(JSONObject obj) {
		importFromJSON(obj);
	}
	
	public TextKey(IGameElement elString) {
		List<IGameElement> translations = elString.getObjectList(GameProperties.TRANSLATION);
		
		for( IGameElement el:translations){
			String value = el.getProperty(GameProperties.VALUE);
			String name = el.getProperty(GameProperties.TEXT);
			values.put(value,name);
		}
	}

	public TextKey(String lang, String valor) {
		values.put(lang,valor);
	}

	public String getText(String currentLanguage, String defaultLanguage) {
		String value=values.get(currentLanguage);
		if (value==null){
			value=values.get(defaultLanguage);
		}
		if (value==null){
			Log.error("No text defined for: "+defaultLanguage);
		}
		return value;
	}

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj=new JSONObject();
		obj.put(GameProperties.OBJECT_TYPE, JavaConsts.TEXT_KEY);
		JSONObject texts=new JSONObject();
		Iterator<Entry<String, String>> it = values.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			String key = pairs.getKey();
			texts.put(key, (String) values.get(key));
		}
		obj.put(GameProperties.VALUE, texts);
		
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject obj) {
		JSONObject json=(JSONObject) obj.get(GameProperties.VALUE);
		Iterator<String> it = json.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String item = (String) json.get(key);
			values.put(key,item);
		}
	}
}
