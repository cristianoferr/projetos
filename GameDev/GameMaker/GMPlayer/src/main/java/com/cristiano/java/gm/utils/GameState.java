package com.cristiano.java.gm.utils;

public abstract class GameState {
	public static final int STG_INIT=1;
	public static final int STG_START=2;
	public static final int STG_RUNNING=4;
	public static final int STG_PAUSED=8;
	public static final int STG_LOADING = 16;
	
	public static final String STG_INIT_STR = "init";
	public static final String STG_START_STR = "start";
	public static final String STG_RUNNING_STR = "running";
	public static final String STG_PAUSED_STR = "paused";
	public static final String STG_LOADING_STR = "loading";
	
	public static int currentStage=STG_INIT;
	
	public static boolean hasRunBegun=false; //if true then the game has already started (useful to come from a pause stage)
	
	public static boolean IS_PAUSED=false;
}
