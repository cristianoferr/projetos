package com.cristiano.android;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.widget.Toast;

import com.cristiano.benchmark.Bench;
import com.cristiano.game.@CONFIG.APP_NAME@.R;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.visualizadores.GMPlayer;
import com.cristiano.jme3.utils.AndroidUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.renderer.android.TextureUtil;
import com.jme3.system.AppSettings;

public class GameActivity extends AbstractHarness implements SensorEventListener {

	SensorManager sm = null;
	GMPlayer game;
	
	private OrientationEventListener myOrientationEventListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		super.onCreate(savedInstanceState);
	}

	public GameActivity() {
		
		Log.info("Initializing Android Game...");
		Bench.start(BenchConsts.EV_INITIALIZATION,BenchConsts.CAT_INITIALIZING);
		
		CRJavaUtils.context=this;
		
		// Set the application class to run
		appClass =GMPlayer.class.getCanonicalName(); 
		//JmeAndroidSystem
		audioRendererType = AppSettings.ANDROID_OPENAL_SOFT;
		screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		//eglConfigType = ConfigType.BEST;

		exitDialogTitle = "Exit?";
		exitDialogMessage = "Press Yes"; 
		TextureUtil.ENABLE_COMPRESSION = false;
		
		splashPicID = R.drawable.startsplash;
	}


	private void checkGame() {
		if (game == null) {
			game = (GMPlayer) getJmeApplication();
			return;
		}
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		 SensorManager mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sm.registerListener(this,mAccelerometer , android.hardware.SensorManager.SENSOR_DELAY_GAME);//registerListener(this, SensorManager.SENSOR_ORIENTATION, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		sm.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*checkGame();
		if (event.sensor.getType()==android.hardware.Sensor.TYPE_ORIENTATION){
			Log.debug(CRMathUtils.round(event.values[0], 3)+":"+CRMathUtils.round(event.values[1], 3)+":"+CRMathUtils.round(event.values[2], 3));
			//AndroidUtils.orientX=event.values[0];
			//AndroidUtils.orientY=event.values[1];
			//AndroidUtils.orientZ=event.values[2];
		}*/
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        checkGame();
        myOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {

			@Override
			public void onOrientationChanged(int arg0) {
				AndroidUtils.orientation=arg0;
			}
			
		};
		
		if (myOrientationEventListener.canDetectOrientation()) {
			//Toast.makeText(this, "Can DetectOrientation", Toast.LENGTH_LONG).show();
			myOrientationEventListener.enable();
		} else {
			Toast.makeText(this, "Can't DetectOrientation", Toast.LENGTH_LONG).show();
			finish();
		}
    }
/*
	@Override
	public void onSensorChanged(SensorEvent event) {
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
*/

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}
}