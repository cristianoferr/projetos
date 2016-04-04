package com.cristiano.java.gm.ecs.comps.ui;

import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;
import de.lessvoid.nifty.screen.Screen;

public class NiftyComponent extends GameComponent {
	public static final String TIME_COUNTDOWN_PANEL_TAG = "TimeCountdownPanel";
	
	public Nifty nifty=null;
	private List<IGameElement> effects;
	public String currentScreenName="";
	private int defaultWidth;
	private int defaultHeight;
	
	public boolean screensStarted=false;

	public boolean loaded=false;
	
	public NiftyComponent(){
		super(GameComps.COMP_NIFTY);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		effects=ge.getObjectList(GameProperties.EFFECTS);
		defaultWidth=ge.getPropertyAsInt(GameProperties.DEFAULT_WIDTH);
		defaultHeight=ge.getPropertyAsInt(GameProperties.DEFAULT_HEIGHT);
	}
	
	public void initNifty(Nifty nifty){
		this.nifty=nifty;
		for (IGameElement el:effects){
			nifty.registerEffect(new RegisterEffectType(el.getName(), el.getProperty(GameProperties.VALUE)));
		}
		nifty.enableAutoScaling(defaultWidth, defaultHeight);
	}
	
	@Override
	public IGameComponent clonaComponent() {
		NiftyComponent ret = new NiftyComponent();
		ret.nifty=nifty;
		ret.effects=effects;
		return ret;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		//obj.put(GameProperties.SCREEN_CURRENT,currentScreenName);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
		//currentScreenName=(String) obj.get(GameProperties.SCREEN_CURRENT);
	}
	@Override
	public void resetComponent() {
		
	}
	public void changeScreenTo(IGameElement elScreen) {
		String name=elScreen.getName();
		Log.debug("changeScreenTo: "+name);
		this.currentScreenName=name;
		nifty.gotoScreen(name);
		Log.debug(Bench.report());
	}
	public Screen getCurrentScreen() {
		return nifty.getCurrentScreen();
	}
	public Element getPanelWithID(Screen screen, String elementID) {
		return screen.findElementByName(elementID);
	}
	public boolean isOnScreen(String screenName) {
		return currentScreenName.equals(screenName);
	}
	public void changeScreenTo(String screenName) {
		nifty.gotoScreen(screenName);
		Log.debug(Bench.report());
	}
	
	
	
	public static void changeVisibility(AbstractUIComponent comp, boolean flag) {
		if (comp==null || comp.niftyElement==null){
			Log.error("No element found on element:"+comp);
			return;
		}
		comp.niftyElement.setVisible(flag);
	}
	public boolean isRunning() {
		return currentScreenName.equals(GameConsts.SCREEN_RUNNING) && screensStarted;// || currentScreenName.equals("");
	}
	public void gotoSplashScreen() {
		changeScreenTo(GameConsts.SCREEN_SPLASH);
		
	}
	public void endSplashScreen() {
		changeScreenTo(GameConsts.SCREEN_RUNNING);
		
	}
}
