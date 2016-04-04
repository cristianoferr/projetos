package com.cristiano.java.product;

import java.util.List;

import com.cristiano.data.IStoreProperties;



public interface IGameElement  extends IStoreProperties{

	IGameElement getPropertyAsGE(String prop);
	List<IGameElement> getPropertyAsGEList(String property);
	void setPropertyTag(String prop, String val);
	void setProperty(String prop, IGameElement val);
	void setProperty(String prop, boolean b);
	boolean getPropertyAsBoolean(String isTerminal);
	boolean hasProperty(String propertyCreatedBy);

	boolean getParamAsBoolean(String list, String param);
	String getParamAsText(String list, String param);
	float getParamAsFloat(String list, String param);
	String[] getParamAsList(String listResource, String scopeGame);
	String getParamAsTag(String listMaterial, String propertyType);
	List<IGameElement> getParamAsGEList(String listProperty, String actionGroup);
	int getParamAsInt(String listProperty, String value);
	String[] getPropertyAsList(String startableStates);
	IGameElement getParamAsGE(String list, String identifier);
	
	void setParam(String list, String property, String value);
	void setParam(String list, String property, IGameElement elValue);
	void setParam(String list, String prop, boolean b);
	void setParam(String list, String prop, int v);
	Object getParametro(String listProperty, String string);
	


	String getIdentifier();
	void setName(String string);
	String getName();
	String getValue();
	
	List<IGameElement> getObjectList(String objList);
	List<IGameElement> getObjectList(String properties, List<IGameElement> reuseprops);

	int getId();
	String id();
	void setID(int id);
	
	IManageElements getElementManager();
	
	String resolveFunctionOf(String unsolved);
	String resolveFunctionOf(String function, Object parametro);
	
	
	void addTag(String tagAll);
	List<String> getTags();
	String getTagsAsText();
	boolean hasTag(String compIdent);
	boolean hasAnyTag(String bubbleFilter);
	boolean hasTag(String domainProperty, String tag);//@domain <domainProperty>={tag} 
	
	void setVar(String string, int i);
	void setVar(String string, float height);
	void setVar(String string, String string2);
	String getVar(String var1);
	void setObject(String objName, int i, IGameElement obj);
	boolean validate();

	
}
