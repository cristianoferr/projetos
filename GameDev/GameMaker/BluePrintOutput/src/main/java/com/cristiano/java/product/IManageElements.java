package com.cristiano.java.product;

import org.json.simple.JSONObject;

import com.cristiano.data.ISerializeJSON;

public interface IManageElements extends ISerializeJSON {

	String getVar(String string);
	IGameElement createElement();
	IGameElement getElementWithID(int id);
	IGameElement getElementWithID(String id);

	IGameElement pickFinal(String compNifty);
	IGameElement pickFinal(String solverTag, IGameElement creator);

	void setVar(String var, String val);

	void addElement(IGameElement el);

	IGameElement importElementFromJSON(JSONObject elJson);
	
	//only used by the builder...
	void addToExportList(IGameElement element);
	boolean containsElement(IGameElement ge);
	void removeElementWithID(int id);
	void clear();

}
