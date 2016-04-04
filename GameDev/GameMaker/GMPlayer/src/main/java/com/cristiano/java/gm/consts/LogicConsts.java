package com.cristiano.java.gm.consts;

/*Focus in gameLogic stuff...*/
public class LogicConsts {
	public static final String BATTLE_NOTIF_CONTROL = "battleNotif";
	
	public static final int RELATION_ENEMY = 1;
	public static final int RELATION_NEUTRAL = 2;
	public static final int RELATION_FRIEND = 3;

	public static final String TARGETTING_NEAR = "near";
	public static final String TARGETTING_FAR = "far";
	public static final String TARGETTING_RANDOM = "random";
	public static final String TARGETTING_STRONGER = "stronger";
	public static final String TARGETTING_WEAK = "weak";



	public static final String OBJECT_BULLET = "BULLET";
	public static final int SCOPE_GAME = 1;
	public static final int SCOPE_TEAM = 2;
	public static final int SCOPE_UNIT = 3;

	// this will become entityHit and EnemyHit or FriendHit based on relation
	public static final String EVENT_DMG_RECEIVED = "EVENT_DMG_RECEIVED";

	public static final String EVENT_ENEMY_HIT = "enemyHit";
	public static final String EVENT_ENEMY_KILLED = "enemyKilled";
	public static final String EVENT_ENTITY_HIT = "entityHit";
	public static final String EVENT_ENTITY_KILLED = "entityKilled";
	public static final String EVENT_FRIEND_HIT = "friendHit";
	public static final String EVENT_FRIEND_KILLED = "friendKilled";

	public static final int BATTLE_NOTIF_DAMAGE = 1;
	public static final int BATTLE_NOTIF_SHOUT= 2;
	public static final int BATTLE_NOTIF_NONE= 0;

	public static final int DATASET_SCORE = 1;
	public static final int DATASET_LAP_POSITION= 2;
	public static final int DATASET_LAP = 3;

	public static final String STARTING_POSITION_GRID = "grid";
	public static final String STARTING_POSITION_RANDOM = "random";

	//public static final String TAG_ENTITY_GOAL_ARROW = "arrowEntity entity leaf";//GoalArrowEntity

	public static final String TAG_STATIC_ENTITY = "visible entity static leaf";
	public static final String TAG_DYNAMIC_ENTITY = "mobile entity gameData leaf";

	public static final String TAG_MESH_WALL = "mesh wall leaf";

	public static final int ENTITY_DYNAMIC = 1;
	public static final int ENTITY_STATIC = 2;

	


}
