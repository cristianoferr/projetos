package com.cristiano.galactic.controller.handlers;


import java.util.Vector;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryDTO;
import com.cristiano.galactic.model.containers.DataManager;

public class CommandHandler {
Vector<String> commands=new Vector<String>();

static public final String CMD_MOVE_TO_TARGET="CMD_MOVE_TO_TARGET";
static public final String CMD_DOCK_AT="CMD_DOCK_AT";
static public final String CMD_BUILD="CMD_BUILD";
static public final String CMD_ATTACK_TARGET="CMD_ATTACK_TARGET";
static public final String CMD_RETURN_FLEET="CMD_RETURN_FLEET";
static public final String CMD_RETURN_HOME="CMD_RETURN_HOME";
static public final String CMD_ORIENT_TO="CMD_ORIENT_TO";
DataManager dataManager;

public CommandHandler(DataManager dataManager){
	this.dataManager=dataManager;
	commands.add(CMD_MOVE_TO_TARGET);
	commands.add(CMD_DOCK_AT);
	commands.add(CMD_ATTACK_TARGET);
	commands.add(CMD_RETURN_FLEET);
	commands.add(CMD_RETURN_HOME);
	commands.add(CMD_ORIENT_TO);
}




public static void startCMD(String cmd,ArtificialEntity entity,Vector3 coord){
	entity.setBT(cmd);
	entity.setTarget(coord);
}

public static void startCMD(ArtificialEntity entity,MemoryDTO cmd){
	entity.setBT(cmd.getString(0));
	entity.setTarget(cmd.getItem(1));
}

public static void startCMD(String cmd,ArtificialEntity entity,Item target){
	entity.setBT(cmd);
	entity.setTarget(target);
}



}
