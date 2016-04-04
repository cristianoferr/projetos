package com.cristiano.java.gm.ecs.comps.persists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

class ObjectBin {
	final List<Object>objects=new ArrayList<Object>();
	
	public void addObject(Object obj){
		objects.add(obj);
	}
	public Object removeObject(){
		if (objects.size()==0){
			return null;
		}
		Object obj=null;
		try {
			obj=objects.get(0);
		} catch (Exception e) {
			Log.error("Error removing object:"+e.getMessage());
			return null;
		}
		
		objects.remove(obj);
		return obj;
	}
	public int size() {
		return objects.size();
	}
}

public class ReuseManagerComponent extends GameComponent {

	final HashMap<String,Object> objects=new HashMap<String,Object>();
	final HashMap<String,ObjectBin> reusableObjects=new HashMap<String,ObjectBin>();
	public EntityManager entMan;
	public IManageElements em;
	
	public ReuseManagerComponent(){
		super(GameComps.COMP_REUSE_MANAGER);
		
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		ReuseManagerComponent ret = new ReuseManagerComponent();
		return ret;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}
	public Object getObjectWithKey(String key) {
		return objects.get(key);
	}
	public void setObjectForKey(String key, Object obj) {
		objects.put(key,obj);
	}
	
	public IGameComponent getReusableComponent(String compType) {
		IGameComponent comp=(IGameComponent) getFromBin(compType);
		if (comp==null){
			return null;
		}
		comp.activate();
		return comp;
	}
	
	public void addReusableComponent(IGameComponent comp) {
		comp.deactivate();
		addToBin(comp.getIdentifier(),comp);
	}
	
	public void addToBin(String compType, Object comp) {
		ObjectBin objectBin = reusableObjects.get(compType);
		if (objectBin==null){
			objectBin=new ObjectBin();
			reusableObjects.put(compType,objectBin);
		}
		objectBin.addObject(comp);
		//Log.debug("Adding Object to recycle bin:"+compType+" size:"+objectBin.size());
	}
	private Object getFromBin(String compType) {
		ObjectBin objectBin = reusableObjects.get(compType);
		if (objectBin==null){return null;}
		return objectBin.removeObject();
	}
	
	
	public IGameEntity requestEntityFromElement(String id) {
		IGameEntity ent=(IGameEntity) objects.get(id);
		return ent;
	}
	public Object requestEntityFromElement(IGameElement element) {
		return requestEntityFromElement(element.id());
	}

	
	public void addEntityFromElement(String id,IGameEntity ent) {
		objects.put(id,ent);
	}
	@Override
	public void resetComponent() {
	}
	public Object instantiateUniqueClass(String classPath) {
		Object obj=getFromBin(classPath);
		if (obj==null){
			obj=CRJavaUtils.instanciaClasse(classPath);
			addToBin(classPath, obj);
		}
		
		return obj;
	}
	
	//returns the path to the file
	public String getAsset(String imageTag) {
		String obj=(String) getFromBin(imageTag);
		
		return obj;
	}
	
	
	
}
