package com.cristiano.java.gm.ecs.comps.macro;

import com.jme3.math.Vector3f;

/*This class sets how the screen shot has to be taken...*/
//TODO: proceduralize this
public class ScreenShotSituation{
	public String screenName;
	public Vector3f position;
	public ScreenShotSituation(String screen, Vector3f pos) {
		this.screenName=screen;
		this.position=pos;
	}
}