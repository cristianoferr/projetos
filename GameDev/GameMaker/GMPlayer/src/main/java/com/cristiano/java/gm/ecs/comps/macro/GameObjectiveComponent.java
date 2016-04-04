package com.cristiano.java.gm.ecs.comps.macro;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;



public class GameObjectiveComponent extends GameComponent {

	public final List<VictoryCheckerComponent> victoryConditions=new ArrayList<VictoryCheckerComponent>();
	public String name;
	private List<IGameElement> elVictoryConditions;


	public GameObjectiveComponent(){
		super(GameComps.COMP_GAME_OBJECTIVE);
		
	}
	
	@Override
	public void free() {
		super.free();
		victoryConditions.clear();
		elVictoryConditions=null;
		name=null;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elVictoryConditions = ge.getPropertyAsGEList(GameProperties.VICTORY_CONDITIONS);
		loadVictoryConditions();
		name=ge.getName();
	}
	

	private void loadVictoryConditions() {
		for (IGameElement el:elVictoryConditions){
			VictoryCheckerComponent vic=(VictoryCheckerComponent) entMan.spawnComponent(GameComps.COMP_VICTORY_CHECKER);
			vic.loadFromElement(el);
			victoryConditions.add(vic);
		}
		
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.VICTORY_CONDITIONS, CRJsonUtils.exportList(elVictoryConditions));
		obj.put(GameProperties.NAME,name);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		elVictoryConditions=CRJsonUtils.importList((JSONObject) obj.get(GameProperties.VICTORY_CONDITIONS),factory);
		loadVictoryConditions();
		name=(String) obj.get(GameProperties.NAME);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		GameObjectiveComponent ret = new GameObjectiveComponent();
		ret.victoryConditions.clear();
		ret.victoryConditions.addAll(victoryConditions);
		ret.elVictoryConditions=elVictoryConditions;
		ret.name=name;
		return ret;
	}
	
	@Override
	public void resetComponent() {
	}
	
	
}
