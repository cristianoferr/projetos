package com.cristiano.java.product.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.cristiano.consts.Extras;
import com.cristiano.java.product.GameElement;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.Log;

public abstract class BPUtils {

	public static IManageElements em;
	
	static Map<String,Integer> hashList=new HashMap<String,Integer>();
	static Map<Integer,String> reverseList=new HashMap<Integer,String>();
	static int nextInt=0;
	
	static{
		getIntForList(Extras.LIST_DOMAIN);
		getIntForList(Extras.LIST_PROPERTY);
		getIntForList(Extras.LIST_OBJECT);
	}

	public static IGameElement createGameElement(){
		IGameElement el=new GameElement(em);
		addElement(el);
		return el;
	}
	
	public static String clear(String paramAsText) {
		
		if (paramAsText.contains("'")){
			paramAsText=paramAsText.replace("'", "");
		}
		if (paramAsText.contains("\"")){
			paramAsText=paramAsText.replace("\"", "");
		}
		if (paramAsText.contains("[")){
			paramAsText=paramAsText.replace("[", "");
		}
		if (paramAsText.contains("]")){
			paramAsText=paramAsText.replace("]", "");
		}
		if (paramAsText.contains("{")){
			paramAsText=paramAsText.replace("{", "");
		}
		if (paramAsText.contains("}")){
			paramAsText=paramAsText.replace("}", "");
		}
		return paramAsText;
	}

	private static void addElement(IGameElement el) {
		if (em!=null){
			em.addElement(el);
		}
	}

	public static Integer getIntForList(String list) {
		Integer obj=hashList.get(list);
		if (obj==null){
			hashList.put(list,++nextInt);
			reverseList.put(nextInt,list);
			return nextInt;
		}
		return obj;
	}
	
	public static String getListForInt(int val) {
		return reverseList.get(val);
	}
}


