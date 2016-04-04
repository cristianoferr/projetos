package com.cristiano.galactic.view.app;

import java.awt.event.ActionEvent;
import java.util.Vector;

import com.cristiano.galactic.view.KeyMapping;
import com.cristiano.galactic.view.View;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;

/*
 * This class is responsible for translating JME keystrokes to the Galactic keystrokes...
 */
public class KeyMappingJME {
KeyMapping keyMap;
Vector<String> keys;
public KeyMappingJME(KeyMapping keyMap){
	this.keyMap=keyMap;
	keys=keyMap.getAllKeys();
}
public static int getKeyInput(String key) {
	key=key.toLowerCase();
	if (key.equals("a")){return KeyInput.KEY_A;}
	if (key.equals("b")){return KeyInput.KEY_B;}
	if (key.equals("c")){return KeyInput.KEY_C;}
	if (key.equals("d")){return KeyInput.KEY_D;}
	if (key.equals("e")){return KeyInput.KEY_E;}
	if (key.equals("f")){return KeyInput.KEY_F;}
	if (key.equals("g")){return KeyInput.KEY_G;}
	if (key.equals("h")){return KeyInput.KEY_H;}
	if (key.equals("i")){return KeyInput.KEY_I;}
	if (key.equals("j")){return KeyInput.KEY_J;}
	if (key.equals("k")){return KeyInput.KEY_K;}
	if (key.equals("l")){return KeyInput.KEY_L;}
	if (key.equals("m")){return KeyInput.KEY_M;}
	if (key.equals("n")){return KeyInput.KEY_N;}
	if (key.equals("o")){return KeyInput.KEY_O;}
	if (key.equals("p")){return KeyInput.KEY_P;}
	if (key.equals("q")){return KeyInput.KEY_Q;}
	if (key.equals("r")){return KeyInput.KEY_R;}
	if (key.equals("s")){return KeyInput.KEY_S;}
	if (key.equals("t")){return KeyInput.KEY_T;}
	if (key.equals("u")){return KeyInput.KEY_U;}
	if (key.equals("v")){return KeyInput.KEY_V;}
	if (key.equals("w")){return KeyInput.KEY_W;}
	if (key.equals("x")){return KeyInput.KEY_X;}
	if (key.equals("y")){return KeyInput.KEY_Y;}
	if (key.equals("z")){return KeyInput.KEY_Z;}
	if (key.equals("0")){return KeyInput.KEY_0;}
	if (key.equals("1")){return KeyInput.KEY_1;}
	if (key.equals("2")){return KeyInput.KEY_2;}
	if (key.equals("3")){return KeyInput.KEY_3;}
	if (key.equals("4")){return KeyInput.KEY_4;}
	if (key.equals("5")){return KeyInput.KEY_5;}
	if (key.equals("6")){return KeyInput.KEY_6;}
	if (key.equals("7")){return KeyInput.KEY_7;}
	if (key.equals("8")){return KeyInput.KEY_8;}
	if (key.equals("9")){return KeyInput.KEY_9;}
	if (key.equals("insert")){return KeyInput.KEY_INSERT;}
	if (key.equals("home")){return KeyInput.KEY_HOME;}
	if (key.equals("tab")){return KeyInput.KEY_TAB;}
	if (key.equals("end")){return KeyInput.KEY_END;}
	if (key.equals("back_space")){return KeyInput.KEY_BACK;}
	if (key.equals("left_control")){return KeyInput.KEY_LCONTROL;}
	if (key.equals("right_control")){return KeyInput.KEY_RCONTROL;}
	if (key.equals("left_shift")){return KeyInput.KEY_LSHIFT;}
	if (key.equals("right_shift")){return KeyInput.KEY_RSHIFT;}
return 0;
}
/**
 * @return the keys
 */
public Vector<String> getKeys() {
	return keys;
}
/**
 * @return the keyMap
 */
public KeyMapping getKeyMap() {
	return keyMap;
}

public void initKeys(View view){
	for (int i=0;i<keys.size();i++){
		view.getInputManager().addMapping(keys.get(i), new KeyTrigger(getKeyInput(keys.get(i))));
		view.getInputManager().addListener(actionListener, keys.get(i));
		view.getInputManager().addListener(analogListener, keys.get(i));
	}
}

private ActionListener actionListener = new ActionListener() {
	
    public void onAction(String name, boolean keyPressed, float tpf) {
    	System.out.println("name:"+name+" keyPressed:"+ keyPressed+" tpf:"+tpf);
    	if (keyPressed){
    		keyMap.pressKey(name);
    	} else{
    		keyMap.releaseKey(name);
    	}
    	
    }
  };

private AnalogListener analogListener = new AnalogListener() {
    public void onAnalog(String name, float value, float tpf) {
     //System.out.println("name:"+name+" value:"+ value+" tpf:"+tpf);
      }
  };
}
