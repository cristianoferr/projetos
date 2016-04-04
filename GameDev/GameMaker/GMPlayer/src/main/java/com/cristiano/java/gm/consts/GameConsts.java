package com.cristiano.java.gm.consts;

import com.cristiano.jme3.assets.GMAssets;

/*Focus in technical stuff...*/
public class GameConsts {
	
	public static final int SCREENSHOT_SIZE=512;
	public static final int ICON_SIZE=128;//must be 512
	
	public static String ANDROID_PATH="../GMAndroid/";
	
	public static final int SHRINK_TIMER=3;
	
	public static final String ASSET_TERRAIN = "terrain";
	public static final String ASSET_MESH = "mesh";
	public static final String ASSET_MATERIAL = "material";
	public static final String ASSET_TEXTURE = "texture";
	public static final String ASSET_PHYSICS = "physics";
	
	
	public static final int RED_INDEX = 0;
	public static final int GREEN_INDEX = 1;
	public static final int BLUE_INDEX = 2;
	public static final int ALPHA_INDEX = 3;
	public static final int DATA_SIZE = 3;
	public static final int ROAD_INDEX = BLUE_INDEX;
	
	
	public static final GMAssets gmAssetsPath=new GMAssets();
	
	public static final int CURRENT_MILESTONE=1;
	
	public static final String NIFTY_EVENT_START_SCREEN="onStartScreen";
	public static final String NIFTY_EVENT_HOVER="onHover";
	public static final String NIFTY_EVENT_CLICK="onClick";
	public static final String NIFTY_EVENT_FOCUS="onFocus";
	

	public static final int ERROR_TRIES = 50;
	
	// O valor inicial default vai estar entre esses dois valores...
	public static final float DEFAULT_SYSTEM_DELAY_MAX = 0.5f;
	public static final float DEFAULT_SYSTEM_DELAY_MIN = 0.1f;

	public final static String DMG_TYPE_GENERIC = "generic";
	public final static String DMG_TYPE_FIRE = "fire";
	public final static String DMG_TYPE_ELECTRIC = "electric";

	public final static String IDENT_STATES = "states";
	public final static String IDENT_SYSTEM = "systems";
	public final static String IDENT_COMPONENT = "components";
	public final static String IDENT_COMPONENT_LIST = "componentList";
	public static final String SYSTEM_PACKAGE = "com.cristiano.java.gm.ecs.systems";

	public static final String ENTITY_PACKAGE = "com.cristiano.java.gm.ecs";
	public static final String COMPONENT_PACKAGE = "com.cristiano.java.gm.ecs.comps";
	public static final String STATE_PACKAGE = "com.cristiano.java.gm.states";


	public static final String NODE_TYPE_MERGE = "merge";
	public static final String NODE_TYPE_NEW = "newsubnode";
	public static final String LIGHT_DIRECTIONAL = "DirectionalLight";
	public static final String LIGHT_AMBIENT = "AmbientLight";
	public static final String LIGHT_POINT = "PointLight";
	public static final String LIGHT_SPOT = "SpotLight";
	
	
	public static final String TERRAIN_NOISE = "noise";
	public static final String TERRAIN_FLAT = "flat";
	public static final Object TERRAIN_PLACEHOLDER = "placeholder";

	public static final String TEXTURE_MAP_DIFFUSE = "diffuse";
	public static final String TEXTURE_MAP_GLOW = "glow";
	public static final String TEXTURE_MAP_NORMAL = "normal";
	public static final String TEXTURE_MAP_SHINE = "shine";
	public static final String TEXTURE_TYPE_3D = "3d";
	public static final String TEXTURE_TYPE_2D = "2d";

	public static final float DEFAULT_GRAVITY = 9.8f;

	public static final String ENTITY_TYPE = "entity";

	public static final int SEQUENCING_SERIAL = 1;
	public static final int SEQUENCING_RANDOM = 2;

	public static final String SCREEN_RUNNING = "running";
	public static final String SCREEN_PAUSE= "pause";
	public static final String SCREEN_DEBUG = "debug";
	public static final String SCREEN_CONFIG = "config";
	public static final String SCREEN_SPLASH = "splash";
	public static final String SCREEN_START = "start";

	public static final String MATERIAL_ROOT_VAR = "materialRoot";
	
	//USED BY THE SYSTEMS
	public static final String TARGET_RELEASE = "release";
	public static final String TARGET_DEV = "dev";
	
	
	public static final float PRIORITY_AVERAGE = 0.5f;
	public static final float PRIORITY_LOW = 1f;
	
	
}
