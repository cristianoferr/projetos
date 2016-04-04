package com.cristiano.java.gm.ecs.comps.ui;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public abstract class AbstractUIComponent extends GameComponent {

	public IGameElement elUI=null;
	public String name;
	public Element parent=null;
	public Screen screen;
	public Element niftyElement;
	public List<IGameElement> elProperties;
	public List<IGameElement> events;
	public List<IGameElement> children;
	
	//if the entity being checked contains ANY of the given components, then its visible, otherwise not
	public String[] visibleConditions;
	public boolean isVisible=true;
	public String imageArt = null;
	
	public AbstractUIComponent(String tipo){
		super(tipo);
	}
	
	@Override
	public String toString(){
		return super.toString()+":"+name;
	}
	
	@Override
	public void free() {
		super.free();
		elUI=null;
		elProperties=null;
		events=null;
		children=null;
		visibleConditions=null;
		imageArt=null;
		parent=null;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elUI=ge.getPropertyAsGE(GameProperties.CONTROL);
		name=ge.getProperty(GameProperties.NAME);
		events=ge.getPropertyAsGEList(GameProperties.EVENTS);
		visibleConditions=ge.getPropertyAsList(GameProperties.VISIBLE_CONDITIONS);
		
	}
	
	protected void finishClone(GameComponent ret) {
		super.finishClone(ret);
		AbstractUIComponent comp=(AbstractUIComponent) ret;
		comp.elUI=elUI;
		comp.name=name;
		comp.parent=parent;
		comp.visibleConditions=visibleConditions;
		comp.screen=screen;
		comp.children=children;
		comp.niftyElement=niftyElement;
		comp.elProperties=elProperties;
		comp.events=events;
		comp.imageArt=imageArt;
	}
	
	public boolean isVisible(IGameEntity ent){
		if (visibleConditions.length==1){
			if ("".equals(visibleConditions[0])){
				visibleConditions=new String[0];
			}
		}
		for (String comp:visibleConditions){
			if (ent.containsComponent(comp)){
				return true;
			}
		}
		return visibleConditions.length==0;
	}
	
	public boolean hasVisibilityConditions(){
		return visibleConditions.length>0;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.IMAGE, imageArt);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		imageArt=(String) obj.get(GameProperties.IMAGE);
	}
	
	@Override
	public void resetComponent() {
	}
}
