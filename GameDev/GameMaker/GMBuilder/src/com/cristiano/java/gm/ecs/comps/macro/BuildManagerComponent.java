package com.cristiano.java.gm.ecs.comps.macro;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.StagedComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.math.Vector3f;

public class BuildManagerComponent extends StagedComponent {

	public static final String STARTING = "STARTING";
	public static final String INIT_TO_RUN = "INIT_TO_RUN";
	public static final String RUNNING = "RUNNING";
	public static final String GAME_GENERATED = "GENERATED";

	public static final String SETTING_SCREENSHOT = "SETTING_SCREENSHOT";
	public static final String WAITING_SCREENSHOT = "WAITING_SCREENSHOT";
	public static final String SETTING_ICONSHOT = "SETTING_ICONSHOT";
	public static final String WAITING_ICONSHOT = "WAITING_ICONSHOT";
	public static final String REQUEST_ART = "REQUEST_ART";
	public static final String WAITING_ART = "WAITING_ART";
	public static final String SAVING_STATE = "SAVING_STATE";
	public static boolean writeAndQuit = false;

	public ScreenShotOrganizer screenShotOrganizer = new ScreenShotOrganizer();

	public int countFails = 0;
	public int maxFails = 300;
	public ScreenshotAppState screenShotState;
	public String screenShotFilePath = "screenShot";

	public BuildManagerComponent() {
		super(GameComps.COMP_BUILD_MANAGER);
		stageControl.add(STARTING);
		stageControl.add(INIT_TO_RUN);
		stageControl.add(RUNNING);
		stageControl.add(GAME_GENERATED);
		stageControl.add(SETTING_SCREENSHOT);
		stageControl.add(WAITING_SCREENSHOT);
		stageControl.add(SETTING_ICONSHOT);
		stageControl.add(WAITING_ICONSHOT);
		stageControl.add(REQUEST_ART);
		stageControl.add(WAITING_ART);
		stageControl.add(SAVING_STATE);

		screenShotOrganizer.addResolution("Phone4", 1280, 768);
		screenShotOrganizer.addResolution("Tablet7", 1920, 1200);
		screenShotOrganizer.addResolution("Phone5", 2560, 1600);

		// Visao do jogador
		screenShotOrganizer.addSituation(
				GameConsts.SCREEN_RUNNING,
				new Vector3f(CRJavaUtils.random(-5, -3), CRJavaUtils.random(1,
						3), CRJavaUtils.random(5, 3)));
		screenShotOrganizer.addSituation(GameConsts.SCREEN_START, new Vector3f(
				0, 1000, 0));
		screenShotOrganizer.addSituation(GameConsts.SCREEN_CONFIG,
				new Vector3f(0, 1000, 0));
	}

	@Override
	public void free() {
		super.free();
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		return null;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		Log.fatal("Cant import!!");
	}

	@Override
	public void resetComponent() {
	}

	public String getScreenShotAssetPath() {
		return "gen/" + screenShotFilePath + ".png";
	}

	public String getIconSourcePath() {
		return "gen/" + screenShotFilePath + "-icon.png";
	}

	public String getDestinationIcon() {
		return "gen/gameIcon.png";
	}
}
