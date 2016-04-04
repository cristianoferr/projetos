package com.cristiano.java.gm.ecs.comps.visual;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class ThemeComponent extends GameComponent {

	public IGameElement elTheme=null;
	
	public IGameElement elMapLocation=null;
	public IGameElement elCurrentScreen=null;
	public IGameElement elGenre;
	public List<IGameElement> elScreens;
	public IGameElement elStartScreen;
	public IGameElement elRunningScreen;
	public IGameElement elConfigScreen;
	public IGameElement elDebugScreen;
	public IGameElement elPauseScreen;
	public ThemeComponent(){
		super(GameComps.COMP_THEME);
		
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		//world sets this parameter
		elTheme=ge.getPropertyAsGE(GameProperties.THEME);
		loadFromThemeElement(elTheme);
	}

	public void loadFromThemeElement(IGameElement elTheme) {
		this.elTheme=elTheme;
		this.elScreens=elTheme.getPropertyAsGEList(GameProperties.SCREENS);
		elCurrentScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_CURRENT);
		elStartScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_START);
		elRunningScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_RUNNING);
		elConfigScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_CONFIG);
		elPauseScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_PAUSE);
		elDebugScreen=elTheme.getPropertyAsGE(GameProperties.SCREEN_DEBUG);
		if (elScreens.size()==0){
			Log.fatalIfRunning("Screens empty!");
		}
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
		ThemeComponent ret = new ThemeComponent();
		ret.elTheme=elTheme;
		finishClone(ret);
		ret.elMapLocation=elMapLocation;
		ret.elCurrentScreen=elCurrentScreen;
		ret.elStartScreen=elStartScreen;
		ret.elConfigScreen=elConfigScreen;
		ret.elRunningScreen=elRunningScreen;
		ret.elPauseScreen=elPauseScreen;
		ret.elScreens=elScreens;
		
		
		
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	

}
