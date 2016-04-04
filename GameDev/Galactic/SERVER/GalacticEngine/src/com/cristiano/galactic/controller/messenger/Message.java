package com.cristiano.galactic.controller.messenger;

import com.cristiano.galactic.controller.handlers.CommandHandler;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.Entity.Logic.representation.MemoryDTO;

public class Message extends ObjUpdate {
	ArtificialEntity to;

int startCondition;
MemoryDTO cmd;

static int totMsg=0;
int idMsg=++totMsg;



public Message(ArtificialEntity to,Item from,MemoryDTO cmd,int Condition){
	super(from);
	this.to=to;
	this.cmd=cmd;
	
	this.startCondition=Condition;
}

public ArtificialEntity getTo() {
	return to;
}





public String getCmd() {
	return cmd.getString(0);
}

public int getIdMsg() {
	return idMsg;
}



public boolean hasReached(){
	return hasReached(to.getCoord());
}


public boolean isDelivered(){
	if (hasReached()){
		CommandHandler.startCMD(to,cmd);
		return true;
	}
	return false;
}

public String toString(){
	return "MSG to:"+to.getName()+" from:"+getFrom().getName()+" CMD:"+getCmd();
}

}
