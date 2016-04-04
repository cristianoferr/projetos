package com.cristiano.java.gm.ecs.comps.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.macro.GameEventComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

/*
 * 
 * */


public class GameGenreComponent extends GameComponent {

	public int npcBudget;
	public int playerBudget;
	//private ArrayList<IGameElement> elEvents;
	private final List<GameEventComponent> events=new ArrayList<GameEventComponent>();
	public IGameElement elGameGenre;
	public IGameElement elGameAxis;

	public GameGenreComponent(){
		super(GameComps.COMP_GAME_GENRE);
	}
	
	@Override
	public void free() {
		super.free();
		npcBudget=0;
		playerBudget=0;
		events.clear();
		elGameGenre=null;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo()+"npcBudget="+npcBudget+":playerBudget="+playerBudget+":";
	}
	
	//ge comes from GameGenreData
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.elGameGenre=ge.getPropertyAsGE(GameProperties.GAME_GENRE);
		this.playerBudget=elGameGenre.getPropertyAsInt(GameProperties.BUDGET_PLAYER);
		this.npcBudget=elGameGenre.getPropertyAsInt(GameProperties.BUDGET_NPC);
		this.elGameAxis=elGameGenre.getPropertyAsGE(GameProperties.GAME_AXIS);
		List<IGameElement> elEvents = elGameGenre.getPropertyAsGEList(GameProperties.GAME_EVENTS);
		loadEvents(elEvents);
	}

	private void loadEvents(List<IGameElement> elEvents) {
		for (IGameElement el:elEvents){
			GameEventComponent event=(GameEventComponent) entMan.spawnComponent(GameComps.COMP_GAME_EVENT);
			event.setEntityManager(entMan);
			event.loadFromElement(el);
			events.add(event);
		}
		
	}

	@Override
	public IGameComponent clonaComponent() {
		GameGenreComponent ret = (GameGenreComponent) entMan.spawnComponent(GameComps.COMP_GAME_GENRE);
		ret.npcBudget=npcBudget;
		ret.playerBudget=playerBudget;
		ret.events.addAll(events);
		ret.elGameGenre=elGameGenre;
		ret.elGameAxis=elGameAxis;
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.BUDGET_PLAYER,playerBudget);
		obj.put(GameProperties.BUDGET_NPC,npcBudget);
		obj.put(GameProperties.GAME_GENRE,elGameGenre.exportToJSON());
		obj.put(GameProperties.GAME_EVENTS,CRJsonUtils.exportList(events));
		obj.put(GameProperties.GAME_AXIS,elGameAxis.exportToJSON());
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		playerBudget=CRJsonUtils.getInteger(obj,GameProperties.BUDGET_PLAYER);
		npcBudget=CRJsonUtils.getInteger(obj,GameProperties.BUDGET_NPC);
		elGameGenre=(IGameElement) factory.assembleJSON(obj.get(GameProperties.GAME_GENRE));
		elGameAxis=(IGameElement) factory.assembleJSON(obj.get(GameProperties.GAME_AXIS));
		
		List<IGameElement> elEvents = elGameGenre.getPropertyAsGEList(GameProperties.GAME_EVENTS);
		loadEvents(elEvents);
	}

	@Override
	public void resetComponent() {
	}

	public GameEventComponent getEventWithName(String eventName) {
		for (GameEventComponent event:events){
			if (event.eventName.equals(eventName)){
				return event;
			}
		}
		Log.warn("No event named '"+eventName+"' found.");
		return null;
	}
	
	public int eventsSize() {
		return events.size();
	}

	public List<GameEventComponent> getEvents() {
		return events;
	}


}
