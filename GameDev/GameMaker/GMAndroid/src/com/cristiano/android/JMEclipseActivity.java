package com.cristiano.android;

import android.content.pm.ActivityInfo;

import com.cristiano.utils.Log;
import com.jme3.app.AndroidHarness;

public class JMEclipseActivity extends AndroidHarness {

	
	public JMEclipseActivity()
	{
		// Set the application class to run

		//precisa definir "/assets"
		//appClass = "com.cristiano.jme3.demos.rigidbody.VisualizaVehicleControl";
		//não funciona
		//appClass = "com.cristiano.jme3.demos.effects.HelloLightScattering";
		
		//funciona:
		//JmeAndroidSystem
		//appClass = "com.cristiano.jme3.demos.HelloEffects";
		//appClass = "com.cristiano.jme3.demos.HelloPicking";
		//appClass = "com.cristiano.jme3.demos.skycontrol.HelloSkyControl";
		//appClass = "com.cristiano.jme3.demos.vehicle.TestPhysicsCar";
		//appClass = "com.cristiano.jme3.demos.androidRelated.Test17";
		//appClass = "com.cristiano.jme3.demos.HelloTerrain";
		appClass = "com.cristiano.jme3.demos.AndroidDemo.MainAndroiddemo";
		//appClass = "com.cristiano.jme3.demos.ui.tonegod.HelloTonegod";
		//appClass = "com.cristiano.jme3.demos.HelloTest";
		//appClass = "com.cristiano.jme3.demos.livro.ch5.TexturesOpaqueTransparent";
	 
		// Try ConfigType.FASTEST; or ConfigType.LEGACY if you have problems
		//eglConfigType = ConfigType.LEGACY;
		Log.debug("teste log android");
		//eglConfigType = ConfigType.BEST;
	 
		// Exit Dialog title & message
		exitDialogTitle = "Quit game?";
		exitDialogMessage = "Do you really want to quit the game?";
	 
		// Choose screen orientation
		screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	 
		// Invert the MouseEvents X (default = true)
		mouseEventsInvertX = true;
	 
		// Invert the MouseEvents Y (default = true)
		mouseEventsInvertY = true;
	}
}
