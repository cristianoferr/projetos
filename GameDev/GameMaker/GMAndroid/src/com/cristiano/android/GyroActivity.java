package com.cristiano.android;

import com.jme3.app.AndroidHarness;

public class GyroActivity extends AndroidHarness {
/*
	AndroidGyro juego;
	
	private OrientationEventListener myOrientationEventListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public GyroActivity() {

		// Set the application class to run
		//appClass = "com.cristiano.jme3.demos.AndroidDemo.AndroidGyro"; // clickable
																		// hud
																		// navigation
																		// buttons
																		// &
																		// accelarometer
		//appClass = "com.cristiano.java.gm.visualizadores.GMPlayer";
		appClass =GMPlayer.class.getCanonicalName();
		screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		//eglConfigType = ConfigType.BEST;
		audioRendererType = AppSettings.ANDROID_OPENAL_SOFT;
		exitDialogTitle = "Exit?";
		exitDialogMessage = "Press Yes";
		//eglConfigVerboseLogging = false;
		
	}
	
	
	private AndroidGyro getGame(){
	if (juego == null) {
		juego = (AndroidGyro) getJmeApplication();
	}
	return juego;
	}
	
	 @Override
	    protected void onStart() {
	        super.onStart();
	        getGame();
	        myOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {

				@Override
				public void onOrientationChanged(int arg0) {
					Log.debug("Orientation: " + String.valueOf(arg0));
					AndroidUtils.orientation=arg0;
					if (juego!=null){
						juego.rotateCamera(0,arg0,0);
					}
					
				}
				
			};
			
			if (myOrientationEventListener.canDetectOrientation()) {
				Toast.makeText(this, "Can DetectOrientation", Toast.LENGTH_LONG).show();
				myOrientationEventListener.enable();
			} else {
				Toast.makeText(this, "Can't DetectOrientation", Toast.LENGTH_LONG).show();
				finish();
			}
	    }


	*/
}