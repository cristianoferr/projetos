package com.cristiano.java.gm.ecs.comps.unit;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;

/*
 * Target: entidade que pode ser selecionada, nem todas podem (tiros, por ex)
 * */
public class TargettingComponent extends GameComponent {
	//contains the list of possible targets... its filled by the radarSystem
	public final ArrayList<IGameEntity> targetList = new ArrayList<IGameEntity>();

	
	//LogicConsts.TARGETTING_RANDOM
	public String selectionType;
	public float timeoutTarget;

	//when locked the targetList cant be changed...
	private boolean islocked;

	public TargettingComponent(){
		super(GameComps.COMP_TARGETTING);
	}
	
	@Override
	public void free() {
		super.free();
		targetList.clear();
		selectionType=null;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		IGameElement elTargetting = ge.getPropertyAsGE(GameProperties.TARGET_SELECTION);
		IGameElement elFixation=ge.getPropertyAsGE(GameProperties.TARGET_FIXATION);
		this.timeoutTarget=elFixation.getPropertyAsFloat(GameProperties.TIMEOUT_TARGET);
		selectionType=elTargetting.getProperty(GameProperties.SELECTION_TYPE);
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}
	
	@Override
	public IGameComponent clonaComponent() {
		TargettingComponent ret = new TargettingComponent();
		ret.selectionType=selectionType;
		ret.timeoutTarget=timeoutTarget;
		return ret;
	}

	

	public void lock() {
		islocked=true;
	}	
	public void unlock() {
		islocked=false;
	}	
	public boolean isLocked(){
		return islocked;
	}
	@Override
	public void resetComponent() {
	}

	
}
