package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class TeamMemberComponent extends GameComponent {

	public int idTeam=0;
	
	
	//set automatically...
	public TeamComponent masterTeam=null;
	
	public TeamMemberComponent(){
		super(GameComps.COMP_TEAM_MEMBER);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		idTeam=ge.getPropertyAsInt(GameProperties.TEAM_ID);
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.TEAM_ID,idTeam);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		idTeam=((Long)  obj.get(GameProperties.TEAM_ID)).intValue();
	}
	
	@Override
	public IGameComponent clonaComponent() {
		
		return this;
	}

	@Override
	public void resetComponent() {
	}

	

	public int getSameTeamRelation() {
		return masterTeam.sameTeamRelation;
	}

	
}
