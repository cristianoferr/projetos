package com.cristiano.java.gm.ecs.comps.persists;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class GameConstsComponent extends GameComponent {
	private static final String ASSET_MATERIAL = "assetMaterial";
	private static final String UI_BATTLE_NOTIF_PANEL_ID = "uiBattleNotifPanelID";
	private static final String VISUAL_TARGET_TYPE = "visualTargetType";
	private static final String ASSET_ICON = "iconMaterial";
	
	public IGameElement gameConsts;
	public IGameElement uiConsts;
	public String battleNotifPanelID;
	private IGameElement theme;
	public String visualTargetType;
	
	//Builder only
	public IGameElement assetMaterial;
	public IGameElement iconMaterial;
	
	public GameConstsComponent(){
		super(GameComps.COMP_GAME_CONSTS);
	}
	@Override
	public void free() {
		super.free();
		gameConsts=null;
		uiConsts=null;
		battleNotifPanelID=null;
		assetMaterial=null;
		iconMaterial=null;
		theme=null;
		visualTargetType=null;
	}
	
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		gameConsts=ge.getPropertyAsGE(GameProperties.GAME_CONSTS);
		uiConsts=ge.getPropertyAsGE(GameProperties.UI_CONSTS);
		theme=ge.getPropertyAsGE(GameProperties.THEME);
		battleNotifPanelID=uiConsts.getProperty(UI_BATTLE_NOTIF_PANEL_ID);
		assetMaterial=theme.getPropertyAsGE(ASSET_MATERIAL);
		iconMaterial=theme.getPropertyAsGE(ASSET_ICON);
		visualTargetType=theme.getProperty(VISUAL_TARGET_TYPE);
	}


	@Override
	public IGameComponent clonaComponent() {
		GameConstsComponent ret=new GameConstsComponent();
		finishClone(ret);
		ret.battleNotifPanelID=battleNotifPanelID;
		ret.assetMaterial=assetMaterial;
		ret.gameConsts=gameConsts;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.GAME_CONSTS, gameConsts.exportToJSON());
		obj.put(GameProperties.UI_CONSTS, uiConsts.exportToJSON());
		obj.put(GameProperties.THEME, theme.exportToJSON());
		obj.put(UI_BATTLE_NOTIF_PANEL_ID, battleNotifPanelID);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		gameConsts=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.GAME_CONSTS));	
		uiConsts=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.UI_CONSTS));	
		theme=(IGameElement) factory.assembleJSON((JSONObject) obj.get(GameProperties.THEME));	
		battleNotifPanelID=(String) obj.get(UI_BATTLE_NOTIF_PANEL_ID);	
		visualTargetType=theme.getProperty(VISUAL_TARGET_TYPE);
	}
	
	

}
