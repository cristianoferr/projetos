package com.cristiano.java.gm.units.controls;

import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.rigidBody.BulletDefines;

public class GMBulletDefines extends BulletDefines {
	public PositionComponent targetPosition=null;//points to the current position of the taget (used to detonate bombs by proximity)
	public IControlBody targetBody;
	
}
