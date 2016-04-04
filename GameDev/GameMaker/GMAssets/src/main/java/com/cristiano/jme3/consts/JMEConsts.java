package com.cristiano.jme3.consts;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class JMEConsts {
	//How many parallel threads the game can have...
	public static final int THREAD_QUANTITY = 4;
	
	public static int RES_WIDTH=-1;
	public static int RES_HEIGHT=-1;
	
	public static String assetsRoot = "assets";
	public static String NAME_GEN_ASSET = "nameGen";//inside assets on GMAssets
	public static final float MIN_DIST_POINTS_MESH=0.01F;
	public static final Quaternion ROTATE_RIGHT = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
	public static final float MAX_DIST_POINTS_MESH = 10000;
	
	public static final String JOYSTICK_VIRTUAL_PAD = "virtualPad";
	public static final String JOYSTICK_MOCK_PAD = "mockPad";
	public static final String JOYSTICK_GYRO_WHEEL = "gyroWheel";
	public static final String JOYSTICK_ACTION_TOUCH = "singleTouch";
	public static final Object JOYSTICK_ALLWAYS_PRESSED = "allwaysPressed";
	public static final Object JOYSTICK_SINGLE_TOUCH = "singleTouch";

	public static final Object JOYSTICK_GYRO_AXIS = "gyroAxis";

	public static final Object JOYSTICK_MULTI_GYRO = "multiGyro";
}
