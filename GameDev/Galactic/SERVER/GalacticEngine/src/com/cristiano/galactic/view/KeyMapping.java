package com.cristiano.galactic.view;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jagatoo.input.devices.components.KeyID;

import com.cristiano.galactic.model.enums.ControlEnum;
import com.cristiano.gamelib.interfaces.IObjeto;

/**
 * Essa classe armazena e gerencia os controles que o jogador está enviando para a Engine.
 * Ela fica do lado do cliente e manda pelo Controller para a engine.
 * @author Cristiano
 *
 */
public class KeyMapping {
HashMap<String,ControlEnum> keyMap=new HashMap<String,ControlEnum>();

Vector<String> controls=new Vector<String>();

private IObjeto item;
//boolean removeOnTurn=false;

public KeyMapping(IObjeto item){
	this.item=item;
	//this.removeOnTurn=removeOnTurn;
	registerKey(KeyID.Q,ControlEnum.CTL_ROLL_RIGHT);
	registerKey(KeyID.E,ControlEnum.CTL_ROLL_LEFT);
	registerKey(KeyID.W,ControlEnum.CTL_TURN_DOWN);
	registerKey(KeyID.S,ControlEnum.CTL_TURN_UP);
	registerKey(KeyID.R,ControlEnum.CTL_MOVE_UP);
	registerKey(KeyID.F,ControlEnum.CTL_MOVE_DOWN);
	registerKey(KeyID.D,ControlEnum.CTL_TURN_RIGHT);
	registerKey(KeyID.A,ControlEnum.CTL_TURN_LEFT);
	registerKey(KeyID.BACK_SPACE,ControlEnum.CTL_ENGINE_STOP);
	registerKey(KeyID.TAB,ControlEnum.CTL_ENGINE_MAX);
	registerKey(KeyID.INSERT,ControlEnum.CTL_LASER);
	registerKey(KeyID.LEFT_CONTROL,ControlEnum.CTL_LASER);
	registerKey(KeyID.LEFT_SHIFT,ControlEnum.CTL_WEAPON);
}

public void registerKey(KeyID key,ControlEnum control){
	registerKey(key.toString(), control);
}

public void registerKey(String key,ControlEnum control){
	keyMap.put(key.toLowerCase(), control);
}

public void pressKeyOld(KeyID key){
	pressKey(key.toString());
}

public void pressKey(String key){
	if (controls.contains(key)) return;
	//System.out.println("KEYPRESS= "+key);
	
	ControlEnum group=keyMap.get(key);
	if (group!=null) item.activateControl(group);
	
	key=key.toLowerCase();
	if (keyMap.get(key)!=null){
		for (int i=0;i<controls.size();i++)
			if (controls.get(i).equals(key))return;
		controls.add(key);
	
	}
}



public void releaseKey(String key){
	key=key.toLowerCase();
	for (int i=0;i<controls.size();i++){
		
		if (controls.get(i).equals(key)){
			ControlEnum group=keyMap.get(controls.get(i));
			//ControlEnum group=keyMap.get(controls.get(i));
			if (group!=null) item.deactivateControl(group);
			controls.remove(i);

		}
	}
//	System.out.println("KEYRelease2= "+key+" size:"+controls.size());
}


public void turn(IObjeto item){
	this.item=item;
}

public Vector<String> getAllKeys() {
	Vector<String> v=new Vector<String>();
	Iterator iterator = keyMap.keySet().iterator();
    while (iterator.hasNext()) {
    	
    	String key = (String) iterator.next();
      	v.add(key.toLowerCase());
    }
    return v;
}

public Vector<String> getControls() {
	return controls;
}

public IObjeto getItem() {
	return item;
}

public void setItem(IObjeto item) {
	this.item = item;
}
}