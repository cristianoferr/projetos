package com.cristiano.java.gm.visualizadores;

import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.macro.BuildManagerComponent;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

/*
 * Usage GMBuilder <headless> tag="tag1 tag2 etc"
 * */
public class GMBuilder extends GMPlayer {

	public GMBuilder(String roottag) {
		super(false);
		if (roottag != null) {
			rootTag = roottag;
		}

	}

	public GMBuilder() {
		super(false);
	}

	public static void main(String[] args) {
		Log.info("Starting Builder...");

		String roottag = checkArguments(args);
		
		roottag="gamemechanic macroDefinition raceTest leaf";
		BuildManagerComponent.writeAndQuit = true;
		try {

			GMBuilder app = new GMBuilder(roottag);
			if (BuildManagerComponent.writeAndQuit) {
				JMEConsts.RES_HEIGHT=GameConsts.SCREENSHOT_SIZE;
				JMEConsts.RES_WIDTH=GameConsts.SCREENSHOT_SIZE;
			} 
			
				app.start();
			//}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String checkArguments(String[] args) {
		boolean headless;
		String roottag = "";
		String par_tag = "tag";

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			Log.debug("Arg[" + i + "]:" + args[i]);

			if (arg.equals("headless")) {
				headless = true;
				Log.info("Starting Headless");
				BuildManagerComponent.writeAndQuit = true;
			}
			if (arg.startsWith(par_tag)) {
				roottag = arg.replace(par_tag, "");
				for (int j = i + 1; j < args.length; j++) {
					roottag = args[j] + " ";
				}
				roottag = roottag.trim();
				Log.info("Root Tag is:" + roottag);

			}
		}
		return roottag.replace("=", "");
	}

	public void simpleInitApp() {
		CRJavaUtils.context = this;
		super.simpleInitApp();
	}
	
	
	

	@Override
	public void initBlueprintIntegration() {
		this.integr = new BPBuilderState();
		stateManager.attach(integr);
	}
}
