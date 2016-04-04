package com.cristiano.java.gm.consts;




/*
 * Vai conter constantes dos Componentes e sistemas do jogo
 * */
public abstract class GameComps {
	public static final String SYSTEM_PACKAGE = "com.cristiano.java.gm.ecs.systems";
	public static final String COMPONENT_PACKAGE = "com.cristiano.java.gm.ecs.comps";
	
	
	public static final String TAG_ALL_COMPONENTS = "leaf component entitysystem";
	public static final String TAG_COMPS_VISUAL_ENHANCE = "visualEnchanceComp";
	public static final String TAG_COMPS_NPC_STATES = "NPCState";
	public static final String TAG_VICTORY_CHECKER_COMPONENTS= "victoryCheckGroup";
	public static final String TAG_NIFTY_UI_COMPONENT = "UINiftyComponent";//the elements of the screen
	public static final String COMP_ABSTRACT_NPCSTATE = "AbstractNPCStateComponent";
	public static final String TAG_COMPS_NPC_BEHAVIOURS ="NPCBehaviour";
	public static final String TAG_NPC_CONDITION ="npcCondition";
	
	// public static final String SYSTEM_HEALTH = "healthSystem";

	public static final String COMP_BATTERY ="BatteryComponent";// ok
	public static final String COMP_DAMAGE_OVER_TIME ="DamageOverTimeComponent";// ok
	public static final String COMP_DAMAGE_RECEIVED ="DamageReceivedComponent";// ok
	public static final String COMP_DEATH ="DeathComponent";// quase
	public static final String COMP_RESOURCE_HEALTH ="HealthComponent";// ok
	//public static final String COMP_MOBILE ="MobileComponent";
	public static final String COMP_PLAYER ="PlayerComponent";
	public static final String COMP_RADAR ="RadarComponent";
	public static final String COMP_TARGETABLE ="TargetableComponent";
	public static final String COMP_TARGET="TargetComponent";
	public static final String COMP_TEAM ="TeamComponent";
	public static final String COMP_TIMER ="TimerComponent";// quase
	public static final String COMP_DMG_EFFECT ="DamageEffectComponent";
	public static final String COMP_TARGETTING ="TargettingComponent";
	public static final String COMP_THEME ="ThemeComponent";
	//public static final String COMP_TESTE ="TestComponent";
	//public static final String COMP_EXPLOSION ="ExplosionComponent";
	//public static final String COMP_CONTACT_TRIGGER ="ContactTriggerComponent";
	public static final String COMP_SHORTCUT ="ShortCutComponent";
	public static final String COMP_CAM ="CamComponent";
	public static final String COMP_RENDER ="RenderComponent";
	public static final String COMP_MESH_LOADER ="MeshLoaderComponent";
	public static final String COMP_SKYBOX ="SkyBoxComponent";
	public static final String COMP_PHYSICS ="PhysicsComponent";
	public static final String COMP_PHYSICS_SPACE ="PhysicsSpaceComponent";
	public static final String COMP_UI_SCREEN ="UIScreenComponent";
	public static final String COMP_UI_LAYER ="UILayerComponent";
	public static final String COMP_UI_PANEL ="UIPanelComponent";
	public static final String COMP_UI_CONTROL ="UIControlComponent";
	public static final String COMP_NIFTY ="NiftyComponent";
	public static final String COMP_UI_ACTION ="UIActionComponent";
	public static final String COMP_APPLY_FORCE ="ApplyForceComponent";
	public static final String COMP_UI_NIFTY_ELEMENT ="NiftyElementComponent";
	public static final String COMP_LIGHT ="LightComponent";
	public static final String COMP_ELEMENT_MANAGER ="ElementManagerComponent";
	public static final String COMP_TERRAIN ="TerrainComponent";
	public static final String COMP_POSITION ="PositionComponent";
	public static final String COMP_TEXTURE ="TextureComponent";
	public static final String COMP_MATERIAL ="MaterialComponent";
	public static final String COMP_SPATIAL ="SpatialComponent";
	//public static final String COMP_UNIT_CHOOSER ="UnitChooserComponent";
	public static final String COMP_BESTIARY_LIB ="BestiaryLibraryComponent";
	public static final String COMP_BESTIARY_QUERY ="QueryBestiaryComponent";
	public static final String COMP_UNIT_ROLES ="UnitRolesComponent";
	public static final String COMP_AI ="AIComponent";
	public static final String COMP_UNIT_RESOURCES ="UnitResourcesComponent";
	public static final String COMP_UNIT_CLASS ="UnitClassComponent";
	public static final String COMP_SPEED ="SpeedComponent";
	public static final String COMP_RESOURCE_DPS ="DPSComponent";
	//public static final String COMP_TEST ="TestComponent";
	public static final String COMP_CLONE ="CloneComponent";
	public static final String COMP_MAP ="MapComponent";
	public static final String COMP_BUBBLE ="BubbleComponent";
	public static final String COMP_BUBBLE_DATA ="BubbleDataComponent";
	public static final String COMP_ROOM ="RoomComponent";
	public static final String COMP_ROAD ="RoadComponent";
	//public static final String COMP_NPCSTATUS ="NPCStatusComponent";
	public static final String COMP_NPC_CONDITION_ARMED_ENEMY_NEAR ="ArmedAndEnemyNearCondition";
	public static final String COMP_NPC_CONDITION_HEALTH_PACK_NEAR_SAFE ="HealthPackNearSafeCondition";
	public static final String COMP_NPC_CONDITION_LOW_HP_ENEMY_NEAR ="LowHPAndEnemyNearCondition";
	public static final String COMP_NPC_CONDITION_NO_ENEMY_SIGHTED ="NoEnemySightedCondition";
	public static final String COMP_NPC_CONDITION_UNARMED_ENEMY_NEAR ="UnarmedAndEnemyNearCondition";
	public static final String COMP_NPC_CONDITION_CHECKER ="NPCConditionCheckerComponent";
	
	public static final String COMP_NPC_STATUS_LOW_HEALTH ="LowHealthStatusComponent";
	//public static final String COMP_NPC_STATUS_ARMED ="ArmedStatusComponent";
	public static final String COMP_NPC_STATUS_ENEMY_NEAR ="EnemyNearStatusComponent";
	//public static final String COMP_NPC_STATUS_ENEMY_SIGHTED ="EnemySightedStatusComponent";
	public static final String COMP_NPC_STATUS_HEALTH_PACK_NEAR ="HealthPackNearStatusComponent";
	public static final String COMP_NPC_STATE_ATTACK ="NPCStateAttack";
	public static final String COMP_NPC_STATE_FLEE ="NPCStateFlee";
	public static final String COMP_NPC_STATE_GUARD ="NPCStateGuard";
	public static final String COMP_NPC_STATE_HITRUN ="NPCStateHitRun";
	public static final String COMP_NPC_STATE_IDLE ="NPCStateIdle";
	public static final String COMP_NPC_STATE_RECOVER_HP ="NPCStateRecoverHP";
	public static final String COMP_NPC_STATE_WANDER ="NPCStateWander";
	public static final String COMP_NPC_STATE_MOVE_TO ="NPCStateMoveTo";
	public static final String COMP_NPC_STATE_CHECKER ="NPCStateCheckerComponent";
	public static final String COMP_DIMENSION ="DimensionComponent";
	public static final String COMP_GAME_GENRE ="GameGenreComponent";
	public static final String COMP_ORIENTATION ="OrientationComponent";
	public static final String COMP_FLATTEN_TERRAIN ="FlattenTerrainComponent";
	public static final String COMP_ACTUATOR_WHEEL ="WheelComponent";
	public static final String COMP_ACTUATOR_WEAPON ="WeaponComponent";
	public static final String COMP_FX_LIB ="FXLibraryComponent";
	public static final String COMP_REUSE_MANAGER ="ReuseManagerComponent";
	public static final String COMP_ASSET_LOAD_REQUEST ="AssetLoadRequestComponent";
	public static final String COMP_BLOOM_FILTER ="BloomFilterComponent";
	public static final String COMP_DEPTH_OF_FIELD ="DepthOfFieldComponent";
	public static final String COMP_LIGHT_SCATTERING ="LightScatteringComponent";
	public static final String COMP_FOG_FILTER ="FogFilterComponent";
	public static final String COMP_CARTOON_EDGE_FILTER ="CartoonEdgeFilterComponent";
	public static final String COMP_BEHAVIOR_FLEE ="BehaviourFleeComp";
	public static final String COMP_BEHAVIOR_WANDER ="BehaviourWanderComp";
	public static final String COMP_BEHAVIOR_CHASE ="BehaviourChaseComp";
	public static final String COMP_BEHAVIOR_FOLLOW ="BehaviourFollowComp";
	
	public static final String COMP_NPC_CONDITION_ARMED_ENEMY_SIGHTED ="ArmedAndEnemySightedCondition";
	public static final String COMP_TARGET_VISIBLE ="TargetVisibleComponent";
	public static final String COMP_NPC_CONDITION_NO_TARGET ="NoTargetCondition";
	public static final String COMP_GAME_OBJECTIVE ="GameObjectiveComponent";
	public static final String COMP_GAME_OPPOSITION ="GameOppositionComponent";
	public static final String COMP_VICTORY_CHECKER ="VictoryCheckerComponent";
	public static final String COMP_TEAM_MEMBER ="TeamMemberComponent";
	public static final String COMP_BULLET ="BulletComponent";
	public static final String COMP_GAME_EVENT ="GameEventComponent";
	public static final String COMP_RESOURCE_TIME ="TimeResourceComponent";
	public static final String COMP_RESOURCE_FLAG ="FlagResourceComponent";
	public static final String COMP_RESOURCE_LIVES ="LiveResourceComponent";
	public static final String COMP_RESOURCE_POINTS ="PointsResourceComponent";
	public static final String COMP_INTERNATIONAL ="InternationalComponent";
	public static final String COMP_JOYSTICKS ="JoystickComponent";
	public static final String COMP_GAME_CONSTS ="GameConstsComponent";
	public static final String COMP_REQUEST_IMAGE ="ImageRequestComponent";
	public static final String COMP_MAP_WORLD ="MapWorldComponent";
	public static final String COMP_MAP_LOCATION ="MapLocationComponent";
	public static final String COMP_GAME_STATE ="GameStateComponent";
	//public static final String COMP_DATA_SET_UPDATER ="DataSetUpdaterComponent";
	public static final String COMP_RESOURCE_UNIT_POSITION ="UnitPositionComponent";
	public static final String COMP_WAYPOINT ="WaypointComponent";
	public static final String COMP_BEHAVIOR_FOLLOW_WAYPOINT ="BehaviourFollowWaypointComponent";
	public static final String COMP_NPC_RACE_DRIVER ="NPCStateRaceDriver";
	public static final String COMP_RESOURCE_RACE_GOAL ="RaceGoalComponent";
	public static final String COMP_RESOURCE_LAP ="LapResourceComponent";
	public static final String COMP_VICTORY_CHECKER_INIT ="VictoryCheckInitComponent";
	public static final String COMP_GUI ="GUIComponent";
	public static final String COMP_BILLBOARD ="BillboardComponent";
	public static final String COMP_NPC_STATE_PATROL ="NPCStatePatrol";
	public static final String COMP_TEST ="TestComponent1";
	public static final String COMP_COMPETITORS = "CompetititorsComponent";
	public static final String COMP_WORLD = "WorldComponent";
	public static final String COMP_TRANSIENT = "TransientComponent";
	public static final String COMP_MASTER = "MasterComponent";
	public static final String COMP_BUILD_MANAGER = "BuildManagerComponent";
	public static final String COMP_CHILD = "ChildComponent";
	public static final String COMP_DIRECTOR = "DirectorComponent";
	public static final String COMP_DIRECTOR_SHOOTER = "ShooterDirectorComponent";
	public static final String COMP_LANDMARKS = "LandmarksComponent";


	
	
	

}
