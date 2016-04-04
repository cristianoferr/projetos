package com.cristiano.java.gm.ecs.comps.unit.npcStates;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public abstract class AbstractNPCStateComponent extends GameComponent {
	public static final String STARTABLE_TAG = "startable";
	
	public IGameComponent previousState = null;
	public String[] entryConditions = null;
	public String[] keepConditions = null;
	public String[] destinationStates = null;

	//quick access to behaviours component... set automatically...
	public AIComponent states;

	public AbstractNPCStateComponent(String comp) {
		super(comp);
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
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		String conds = StringHelper.removeChaves(ge
				.getProperty(GameProperties.ENTRY_CONDITIONS));
		if (!conds.equals("")) {
			entryConditions = conds.split(" ");
		}
		conds = StringHelper.removeChaves(ge
				.getProperty(GameProperties.KEEP_CONDITIONS));
		if (!conds.equals("")) {
			keepConditions = conds.split(" ");
		}
		String states = StringHelper.removeChaves(ge
				.getProperty(GameProperties.DESTINATION_STATES));
		if (!states.equals("")) {
			destinationStates = states.split(" ");
		}
	}

	protected void completeStatus(AbstractNPCStateComponent status) {
		status.entryConditions=entryConditions;
		status.keepConditions=keepConditions;
		status.destinationStates=destinationStates;
	}

	public abstract void stateEnter(IGameEntity ent,NPCStateManagerSystem npcStateManagerSystem) ;
	public abstract void stateTick(IGameEntity ent,NPCStateManagerSystem npcStateManagerSystem) ;
	public abstract void stateExit(IGameEntity ent,NPCStateManagerSystem npcStateManagerSystem) ;

	
	
}
