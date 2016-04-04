package com.cristiano.java.gm.ecs.comps;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.state.StageControl;
import com.cristiano.utils.Log;


public abstract class StagedComponent extends GameComponent {
	public StageControl stageControl = new StageControl();

	
	public StagedComponent(String tipo) {
		super(tipo);
	}
	
	@Override
	public String getInfo() {
		return super.getInfo()+"stage="+getStage()+":";
	}
	
	public boolean isOnStage(String stageName) {
		return stageControl.isOnStage(stageName);
	}
	@Override
	public void free() {
		super.free();
		stageControl.reset();
	}

	public String nextStage() {
		stageControl.next();
		String stage = getStage();
		Log.info(ident+": Changed stage to "+stage);
		return stage;
	}
	public String previousStage() {
		stageControl.previous();
		String stage = getStage();
		Log.info(ident+": Changed stage to "+stage);
		return stage;
	}
	public void setStage(String stage){
		Log.info(ident+": Stage set to "+stage);
		stageControl.setCurrentStage(stage);
	}

	public String getStage() {
		return stageControl.getCurrentStage();
	}	
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.INDEX,stageControl.getIndex());
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		stageControl.setIndex(CRJsonUtils.getInteger(obj,GameProperties.INDEX));
	}
}
