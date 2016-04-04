package com.cristiano.java.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.data.ISerializeJSON;
import com.cristiano.data.IStoreProperties;
import com.cristiano.java.product.utils.BPUtils;

public class GameParams implements ISerializeJSON {
	HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();

	@Override
	public JSONObject exportToJSON() {
		JSONObject obj = new JSONObject();
		Iterator<Integer> it = values.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			String value = BPUtils.getListForInt(values.get(key));
			obj.put(BPUtils.getListForInt(key), value);
		}
		return obj;
	}

	@Override
	public void importFromJSON(JSONObject json) {
		// TODO Auto-generated method stub

	}

	public String getParamAsText(String param) {
		Integer intForList = BPUtils.getIntForList(param);
		Integer val = values.get(intForList);
		if (val == null) {
			return "";
		}
		return BPUtils.clear(BPUtils.getListForInt(val));
	}

	public void setParam(String prop, String value) {
		Integer intForList = BPUtils.getIntForList(value);
		values.put(BPUtils.getIntForList(prop), intForList);

	}

	public void setParam(String prop, IGameElement el) {
		Integer intForList = BPUtils.getIntForList(el.id());
		values.put(BPUtils.getIntForList(prop), intForList);

	}

	public float getParamAsFloat(String param) {
		Integer obj = values.get(BPUtils.getIntForList(param));
		if (obj == null) {
			return 0;
		}
		
		return Float.parseFloat(BPUtils.getListForInt(obj));
	}

	public void setParam(String param, float val) {
		Integer intForList = BPUtils.getIntForList(Float.toString(val));
		values.put(BPUtils.getIntForList(param), intForList);
	}

	public void setParam(String param, int val) {
		Integer intForList = BPUtils.getIntForList(Integer.toString(val));
		values.put(BPUtils.getIntForList(param), intForList);
	}

	public int getParamAsInt(String param) {
		Integer obj = values.get(BPUtils.getIntForList(param));
		if (obj == null) {
			return 0;
		}
		String listForInt = BPUtils.getListForInt(obj);
		if ("".equals(listForInt)){
			return 0;
		}
		if (listForInt.contains(".")){
			listForInt=listForInt.substring(0,listForInt.indexOf("."));
		}
		return Integer.parseInt(BPUtils.clear(listForInt));

	}

	

	public boolean hasProperty(String property) {
		return values.containsKey(BPUtils.getIntForList(property));
	}

	public void applyTo(IStoreProperties prop) {
		Iterator<Integer> it = values.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			String listForInt = BPUtils.getListForInt(key);
			Integer value = values.get(key);
			prop.setProperty(listForInt, BPUtils.getListForInt(value));
		}

	}

	public String toString(String list) {
		Iterator<Integer> it = values.keySet().iterator();
		String s = "";
		while (it.hasNext()) {
			Integer key = it.next();
			String listForInt = BPUtils.getListForInt(key);
			s += "  @" + list + " " + listForInt + "=" + getParamAsText(listForInt) + "\n";
			// obj.put(key, values.get(BPUtils.getIntForList(key).toString());
		}
		return s;
	}

	public List<String> getAllKeys() {
		List<String> ret=new ArrayList<String>();
		Iterator<Integer> it = values.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			ret.add(BPUtils.getListForInt(key));
		}
		return ret;
	}
}
