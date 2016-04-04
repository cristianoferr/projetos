package com.cristiano.java.gm.ecs.comps.macro;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.jme3.audio.AudioNode;



public class GameEventComponent extends GameComponent {
	public IGameEntity sender=null;
	public boolean showScrollingText=false;
	
	public String eventName;
	public float value;//multi-use: how much dmg?
	public float pointReward;
	public float hitPointReward;
	public float livesReward;
	public float timeReward;
	public float ctfReward;
	public boolean multiByValue;
	public boolean isCritical=false;
	public float finalValue;
	
	public int notificationType=0; //used to define the battle notification, loaded automatically from unitresource
	public IGameElement soundObj;
	public AudioNode audio=null;
 
	public GameEventComponent() {
		super(GameComps.COMP_GAME_EVENT);
	}
	
	@Override
	public String toString(){
		return super.toString()+":"+eventName;
	}
	
	@Override
	public void free() {
		super.free();
		eventName=null;
		value=0;
		pointReward=0;
		hitPointReward=0;
		livesReward=0;
		timeReward=0;
		ctfReward=0;
		multiByValue=false;
		isCritical=false;
		finalValue=0;
		notificationType=0;
		soundObj=null;
		audio=null;
				
	}
	
	@Override
	public String getInfo() {
		return super.getInfo()+"EventName="+eventName+":";
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.eventName=ge.getName();
		this.showScrollingText=ge.getPropertyAsBoolean(GameProperties.SHOW_SCROLLING_TEXT);
		this.pointReward=ge.getPropertyAsFloat(GameProperties.REWARD_POINT);
		this.hitPointReward=ge.getPropertyAsFloat(GameProperties.REWARD_HIT_POINT);
		this.livesReward=ge.getPropertyAsFloat(GameProperties.REWARD_LIVES);
		this.timeReward=ge.getPropertyAsFloat(GameProperties.REWARD_TIME);
		this.ctfReward=ge.getPropertyAsFloat(GameProperties.REWARD_FLAG);
		this.multiByValue=ge.getPropertyAsBoolean(GameProperties.REWARD_MULTI_BY_VALUE);
		this.soundObj=ge.getPropertyAsGE(GameProperties.SOUND_OBJ);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		GameEventComponent ret=new GameEventComponent();
		ret.eventName=eventName;
		ret.setElement(getElement());
		ret.showScrollingText=showScrollingText;
		ret.pointReward=pointReward;
		ret.hitPointReward=hitPointReward;
		ret.livesReward=livesReward;
		ret.timeReward=timeReward;
		ret.ctfReward=ctfReward;
		ret.multiByValue=multiByValue;
		ret.soundObj=soundObj;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

	

	
}
