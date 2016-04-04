package com.cristiano.java.gm.ecs.comps.map;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.StagedComponent;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroEntity;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;

public class MapComponent extends StagedComponent {
	
	public static final String WAITING = "WAITING";
	public static final String STARTING = "STARTING";
	public static final String LOAD_SKYBOX = "LOAD_SKYBOX";
	public static final String LOAD_OPPOSITION = "LOAD_OPPOSITION";
	public static final String LOAD_OBJECTIVES = "LOAD_OBJECTIVES";
	public static final String TERRAIN_GENERATING = "TERRAIN_GENERATING";
	public static final String TERRAIN_GENERATED = "TERRAIN_GENERATED";
	public static final String ROOM_CHECK = "ROOM_CHECK";
	public static final String MAP_SOLVING = "MAP_SOLVING";
	public static final String ROOM_SOLVING = "ROOM_SOLVING";
	public static final String ROOM_SOLVED = "ROOM_SOLVED";
	public static final String ROAD_SOLVING = "ROAD_SOLVING";
	public static final String ROAD_SOLVED = "ROAD_SOLVED";
	public static final String TERRAIN_FINISHING = "TERRAIN_FINISHING";
	public static final String TERRAIN_FINISHED = "TERRAIN_FINISHED";
	public static final String LOADED = "LOADED";
	public static final String BUBBLE_SOLVING = "BUBBLE_SOLVING";
	public static final String BUBBLE_SOLVED = "BUBBLE_SOLVED";
	public final static String GENERATING_TEAMS = "GENERATING_TEAMS";
	public final static String WAITING_TEAMS = "WAITING_TEAMS";
	public final static String TEAMS_READY = "TEAMS_READY";
	public final static String COUNTDOWN_TIMER = "COUNTDOWN_TIMER";
	public static final String RUNNING = "RUNNING";


	

	public String populateMap;
	public IGameElement elTerrain = null;
	public int length;
	public IGameElement elSkybox = null;

	public float enviroSpacing;
	public ISolveMap mapSolver;
	
	public int maxRooms;
	
	public EnviroEntity roadEnviro;
	public int maxRoadsRoom;
	public String roadFunctionTag;
	public int roadPoints;
	public int roadWidth;
	
	public float maxRoomSize;
	public float minRoomSize;



	public float roomFloorHeight;

	public float roomHeight;

	public List<IGameElement> elMapObjectives;
	public IGameElement elMapOpposition;

	public boolean isCompleted = false; // set to true once a player completes a
										// level, dont save
	public boolean playerVictorious=false;
	
	/*if true then room size will based on relative size to map 
	 * (a map with 1000 and a room with relative size of 0.1 will result in a room with 100)
	 */
	public boolean isRoomSizeRelative=true;
	
	//defines if the map has been already loaded (reseting a map, for instance)
	public boolean previouslyLoaded=false;
	private IGameElement elAmbience;
	public int seed;
	//used to measure the lap... more like a goal
	public float lapDistance;
	

	public MapComponent() {
		super(GameComps.COMP_MAP);

		stageControl.add(WAITING);
		stageControl.add(STARTING);
		stageControl.add(LOAD_SKYBOX);
		stageControl.add(LOAD_OPPOSITION);
		stageControl.add(LOAD_OBJECTIVES);
		stageControl.add(TERRAIN_GENERATING);
		stageControl.add(TERRAIN_GENERATED);
		stageControl.add(ROOM_CHECK);
		stageControl.add(MAP_SOLVING);
		stageControl.add(ROOM_SOLVING);
		stageControl.add(ROOM_SOLVED);
		stageControl.add(ROAD_SOLVING);
		stageControl.add(ROAD_SOLVED);
		stageControl.add(TERRAIN_FINISHING);
		stageControl.add(TERRAIN_FINISHED);
		stageControl.add(LOADED);
		stageControl.add(BUBBLE_SOLVING);
		stageControl.add(BUBBLE_SOLVED);
		stageControl.add(GENERATING_TEAMS);
		stageControl.add(WAITING_TEAMS);
		stageControl.add(TEAMS_READY);
		stageControl.add(RUNNING);
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.populateMap = ge.getProperty(GameProperties.MAP_POPULATE_ID);
		this.length = ge.getPropertyAsInt(GameProperties.LENGTH);
		maxRooms = ge.getPropertyAsInt(GameProperties.ROOM_MAX);
		this.enviroSpacing = ge.getPropertyAsFloat(GameProperties.ENVIRO_SPACING);
		lapDistance=ge.getPropertyAsFloat(GameProperties.LAP_DISTANCE);
		isRoomSizeRelative = ge.getPropertyAsBoolean(GameProperties.IS_ROOM_SIZE_RELATIVE);
		seed=ge.getPropertyAsInt(GameProperties.SEED);

		// mod terrain
		this.elTerrain = ge.getPropertyAsGE(GameProperties.TERRAIN);
		
		//mod skybox
		elAmbience = ge.getPropertyAsGE(GameProperties.AMBIENCE_THEME);
		if (elAmbience==null){
			Log.error("Map has no ambience!");
		}
		//,roadTexture=$this.roadTexture,lowHeightTexture=$this.lowHeightTexture.value,highHeightTexture=$this.highHeightTexture

		// mod road
		this.roadFunctionTag = ge.getProperty(GameProperties.ROAD_FUNCTION_TAG);
		this.roadPoints = ge.getPropertyAsInt(GameProperties.ROAD_POINTS);
		this.roadWidth = ge.getPropertyAsInt(GameProperties.ROAD_WIDTH);
		maxRoadsRoom = ge.getPropertyAsInt(GameProperties.ROAD_PER_ROOM);
		roadEnviro = new EnviroEntity();
		roadEnviro.loadFromElement(ge.getPropertyAsGE(GameProperties.ROAD_WALL_ENVIRO));

		// roomDimension
		this.roomFloorHeight = ge.getPropertyAsFloat(GameProperties.ROOM_FLOOR_HEIGHT);
		// the size is relavtive to map size
		this.maxRoomSize = ge.getPropertyAsFloat(GameProperties.ROOM_MAX_SIZE);
		this.minRoomSize = ge.getPropertyAsFloat(GameProperties.ROOM_MIN_SIZE);
		this.roomHeight = ge.getPropertyAsFloat(GameProperties.ROOM_HEIGHT);

		// mod opposition
		elMapObjectives = ge.getPropertyAsGEList(GameProperties.MAP_OBJECTIVES);
		elMapOpposition = ge.getPropertyAsGE(GameProperties.MAP_OPPOSITION);
		
	}
	
	

	@Override
	public IGameComponent clonaComponent() {
		MapComponent ret = new MapComponent();
		ret.populateMap = populateMap;
		ret.stageControl.setCurrentStage(stageControl);
		ret.elSkybox = elSkybox;
		ret.seed = seed;
		ret.elAmbience = elAmbience;
		ret.isRoomSizeRelative = isRoomSizeRelative;
		ret.lapDistance = lapDistance;
		finishClone(ret);
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		loadFromElement(getElement());
		deactivate();
	}

	public int getLength() {
		return length;
	}

	public RoomComponent getStartingRoom() {
		List<IGameComponent> rooms = getComponentsWithIdentifier(GameComps.COMP_ROOM);
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			if (room.startingRoom) {
				return room;
			}
		}
		Log.error("No Starting room found of " + rooms.size() + " rooms available.");
		return null;
	}
	public RoomComponent getEndingRoom() {
		List<IGameComponent> rooms = getComponentsWithIdentifier(GameComps.COMP_ROOM);
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			if (room.endingRoom) {
				return room;
			}
		}
		Log.error("No endingRoom room found of " + rooms.size() + " rooms available.");
		return null;
	}	
	
	
	public RoomComponent getNextRoom(RoomComponent fromRoom) {
		List<IGameComponent> rooms = getComponentsWithIdentifier(GameComps.COMP_ROOM);
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			if (room.roomId==fromRoom.roomId+1) {
				return room;
			}
		}
		Log.warn("No nextRoom for " + fromRoom + " found.");
		return null;
	}

	public EnviroEntity getRoadEnviro(){
		return roadEnviro;
	}
	
	@Override
	public void resetComponent() {
		setStage(WAITING);
	}
	@Override
	public void free() {
		super.free();
	}

	public String getRoadTexture() {
		return GMAssets.requestAsset(elAmbience.getProperty(GameProperties.AMBIENCE_ROAD_TEXTURE));
	}

	public String getHighHeightTexture() {
		return GMAssets.requestAsset(elAmbience.getProperty(GameProperties.AMBIENCE_HIGH_HEIGHT_TEXTURE));
	}

	public String getLowHeightTexture() {
		return GMAssets.requestAsset(elAmbience.getProperty(GameProperties.AMBIENCE_LOW_HEIGHT_TEXTURE));
	}

	
}
