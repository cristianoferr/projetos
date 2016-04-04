package com.cristiano.java.gm.ecs.comps.mechanics;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.StagedComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class MapWorldComponent extends StagedComponent {
	
	//stages
	public static final String INITIAL = "INITIAL";
	public static final String WAITING = "WAITING";
	public static final String NEXT_MAP = "NEXT_MAP";
	public static final String LOAD_MAP = "LOAD_MAP";
	public static final String RUNNING = "RUNNING";
	public static final String RESET_MAP = "RESET_MAP";
	
	public static final String MAP_SUCCESS = "MAP_SUCCESS";
	public static final String WAIT_MAP_SUCCESS = "WAIT_MAP_SUCCESS";//WILL WAIT FOR THE PLAYER
	public static final String MAP_FAIL= "MAP_FAIL";
	public static final String WAIT_MAP_FAIL= "WAIT_MAP_FAIL";//WILL WAIT FOR THE PLAYER
	
	
	
	
	private IGameElement mapWorld;
	public int mapSequence;
	public boolean allowMapChoosing;
	private int currentMap;
	private List<IGameElement> maps;
	
	
	

	public MapWorldComponent() {
		super(GameComps.COMP_MAP_WORLD);
		//main flow
		stageControl.add(INITIAL);
		stageControl.add(WAITING);
		stageControl.add(NEXT_MAP);
		stageControl.add(LOAD_MAP); 
		stageControl.add(RUNNING); 
		
		//alternative flow
		stageControl.add(RESET_MAP); 
		stageControl.add(LOAD_MAP); 
		stageControl.add(RUNNING); 
		
		//SUCESS FLOW
		stageControl.add(MAP_FAIL); 
		stageControl.add(WAIT_MAP_FAIL); 
		
		stageControl.add(RESET_MAP); 
		stageControl.add(LOAD_MAP); 
		stageControl.add(RUNNING);
		
		//FAIL FLOW
		stageControl.add(MAP_SUCCESS); 
		stageControl.add(WAIT_MAP_SUCCESS); 
		stageControl.add(NEXT_MAP); 
		stageControl.add(LOAD_MAP); 
		stageControl.add(RUNNING);
		
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.mapWorld = ge.getPropertyAsGE(GameProperties.MAP_WORLD);
		loadFromMapWorld();
	}

	private void loadFromMapWorld() {
		this.mapSequence = mapWorld.getPropertyAsInt(GameProperties.MAP_SEQUENCE);
		this.allowMapChoosing = mapWorld.getPropertyAsBoolean(GameProperties.ALLOW_MAP_CHOOSING);
		this.currentMap = mapWorld.getPropertyAsInt(GameProperties.CURRENT_MAP);
		this.maps = mapWorld.getObjectList(GameProperties.MAPS);
		if (maps.size() < 1) {
			Log.error("MapWorld has no maps!");
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		MapWorldComponent ret = new MapWorldComponent();
		finishClone(ret);
		ret.mapWorld = mapWorld;
		ret.currentMap = currentMap;
		ret.maps = maps;
		ret.allowMapChoosing = allowMapChoosing;
		ret.mapSequence = mapSequence;
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		obj.put(GameProperties.MAP_WORLD, mapWorld.exportToJSON());
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		mapWorld=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.MAP_WORLD));
		loadFromMapWorld();
	}

	@Override
	public void resetComponent() {
		currentMap = -1;
		setStage(INITIAL);
	}

	public void nextMap() {
		getNextIndex();
	}
	
	public IGameElement getMap() {
		return maps.get(currentMap);
	}

	private int getNextIndex() {
		if (mapSequence == GameConsts.SEQUENCING_SERIAL) {
			currentMap++;
			if (currentMap >= maps.size()) {
				currentMap = 0;
			}
		} else if (mapSequence == GameConsts.SEQUENCING_SERIAL) {
			int c = 0;
			do {
				c = (int) (maps.size() * Math.random());
			} while (c == currentMap);
			currentMap = c;

		}
		return currentMap;
	}

	public MapComponent getMapComponent() {
		List<IGameComponent> maps = getComponentsWithIdentifier(GameComps.COMP_MAP);
		IGameElement mapElement = getMap();
		for (IGameComponent comp:maps){
			if (comp.getElement().getId()==mapElement.getId()){
				return (MapComponent) comp;
			}
		}
		Log.fatal("Map not found!");
		return null;
	}

}
