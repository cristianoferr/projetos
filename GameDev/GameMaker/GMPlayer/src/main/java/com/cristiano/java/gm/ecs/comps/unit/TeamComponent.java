package com.cristiano.java.gm.ecs.comps.unit;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.StagedComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class TeamComponent extends StagedComponent {
	public final static String ON_HOLD="ON_HOLD";
	public final static String START="START";
	public final static String INITIALISING="INITIALISING";
	public final static String WAITING_4_PLACEMENT="WAITING_UNITIES_PLACEMENT";
	public final static String READY_TO_RUN="READY_TO_RUN";
	public static final String COUNTDOWN = "COUNTDOWN";
	public final static String RUNNING="RUNNING";
	
	
	public int idTeam=0;
	public boolean isPlayerTeam=true;
	public int sameTeamRelation=LogicConsts.RELATION_FRIEND;
	public int maxUnits=-1;
	public int minUnits=-1;
	public boolean hasRespawn;
	public int initialUnits;
	public int livesLeft=-1;
	public int maxLives=-1;
	public int countDownTimer=5;
	public float timeToStart=countDownTimer;
	public IGameElement startingPosition;//defines where to place the units
	
	//set automatically...
	public TeamMemberComponent teamMember=null;
	
	//units spawned will use this role as base
	public String roleIdentifier;
	
	public TeamComponent(){
		super(GameComps.COMP_TEAM);
		stageControl.add(ON_HOLD);
		stageControl.add(START);
		stageControl.add(INITIALISING);
		stageControl.add(WAITING_4_PLACEMENT);
		stageControl.add(READY_TO_RUN);
		stageControl.add(COUNTDOWN);
		stageControl.add(RUNNING);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		idTeam=ge.getPropertyAsInt(GameProperties.TEAM_ID);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		TeamComponent ret = new TeamComponent();
		ret.idTeam=idTeam;
		ret.isPlayerTeam=isPlayerTeam;
		ret.maxUnits=maxUnits;
		ret.minUnits=minUnits;
		ret.sameTeamRelation=sameTeamRelation;
		ret.hasRespawn=hasRespawn;
		ret.initialUnits=initialUnits;
		ret.livesLeft=livesLeft;
		return ret;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		obj.put(GameProperties.TEAM_ID,idTeam);
		obj.put(GameProperties.IS_TEAM_PLAYER,isPlayerTeam);
		obj.put(GameProperties.HAS_RESPAWN,hasRespawn);
		obj.put(GameProperties.MAX_UNITS,maxUnits);
		obj.put(GameProperties.MIN_UNITS,minUnits);
		obj.put(GameProperties.SAME_TEAM_RELATION,sameTeamRelation);
		obj.put(GameProperties.INITIAL_UNITS,initialUnits);
		obj.put(GameProperties.LIVES_LEFT,maxLives);
		obj.put(GameProperties.STARTING_POSITION,startingPosition.exportToJSON());
		obj.put(GameProperties.ROLE_IDENTIFIER,roleIdentifier);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		idTeam=CRJsonUtils.getInteger(obj, GameProperties.TEAM_ID);
		isPlayerTeam=(boolean) obj.get(GameProperties.IS_TEAM_PLAYER);
		hasRespawn=(boolean) obj.get(GameProperties.HAS_RESPAWN);
		roleIdentifier=(String) obj.get(GameProperties.ROLE_IDENTIFIER);
		maxUnits=CRJsonUtils.getInteger(obj, GameProperties.MAX_UNITS);
		minUnits=CRJsonUtils.getInteger(obj, GameProperties.MIN_UNITS);
		sameTeamRelation=CRJsonUtils.getInteger(obj, GameProperties.SAME_TEAM_RELATION);
		initialUnits=CRJsonUtils.getInteger(obj, GameProperties.INITIAL_UNITS);
		maxLives=CRJsonUtils.getInteger(obj, GameProperties.LIVES_LEFT);
		livesLeft=maxLives;
		startingPosition=(IGameElement) factory.assembleJSON(obj.get(GameProperties.STARTING_POSITION));
		setStage(ON_HOLD);
		if (roleIdentifier==null){
			Log.fatal("RoleIdentifier is null");
		}
	}
	

	public void unitSpawned() {
		if (livesLeft>0){
			livesLeft--;
		}
	}
	@Override
	public void resetComponent() {
		setStage(ON_HOLD);
	}
}
