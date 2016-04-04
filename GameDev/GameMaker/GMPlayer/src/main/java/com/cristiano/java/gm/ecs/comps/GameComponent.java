package com.cristiano.java.gm.ecs.comps;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.consts.JavaConsts;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.utils.Log;

public abstract class GameComponent extends GameEntity implements IGameComponent {

	protected String ident;

	public boolean firstTick=true;

	private String info="";

	private boolean isArchived=false;

	private int sourceID;
	
	@Override
	public boolean hasTag(String compIdent) {
		//sometimes there is no element attached...
		if (ident.equals(compIdent)){
			return true;
		}
		return super.hasTag(compIdent);
		
	}
	
	@Override
	public void free() {
		super.free();
		isArchived=false;
		addInfo("free()");
		firstTick=true;
		sourceID=0;
	}
	
	@Override
	public void setSourceID(int id) {
		this.sourceID=id;
	}
	
	@Override
	public int getSourceID(){
		return sourceID;
	}
	
	public GameComponent(){
		this(null);
	}
	
	public void setFirstTick(){
		firstTick=true;
	}
	protected GameComponent(String ident){
		this.ident=ident;
	}
	
	@Override
	public String getIdentifier() {
		return ident;
	}
	

	@Override
	public void addInfo(String info) {
		this.info+=info+" \n";
		if (info.length()>4000){
			Log.warn("Exceeding info size: "+info.length()+" for: "+this);
			info="removing excess log \n";
			
		}
		
	}
	
	@Override
	public String getDebugInfo() {
		return info;
	}

	@Override
	public void loadDefault(IManageElements em) {
		loadFromElement(em.pickFinal(ident));
	}
	
	protected void finishClone(GameComponent ret) {
		ret.setElement(getElement());
		ret.setEntityManager(entMan);
		ret.ident=ident;
		ret.addInfo("Clone of '"+ret.getDebugInfo()+"'");
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		if (this.ident==null){
			this.ident=ge.getProperty(Extras.PROPERTY_INIT).replace("'", "");
		}
		if (this.ident.equals("")){
			this.ident=ge.getProperty(Extras.PROPERTY_IDENTIFIER).replace("'", "");
		}
	}
	
	public String toString(){
		String elName="";
		if (getElement()!=null){
			elName=getElement().getIdentifier();
		}
		return getId()+":component:"+ident +":"+elName;
	}

	@Override
	public boolean isFirstTick() {
		return firstTick;
	}
	

	@Override
	public void merge(IGameComponent comp) {
		
	}
	
	@Override
	public void archive() {
		isArchived=true;
	}
	
	@Override
	public void dearchive() {
		isArchived=false;
	}
	
	@Override
	public boolean isArchived(){
		return isArchived;
	}
	
	@Override
	public String getInfo() {
		return ident+":C"+size()+":";
	}
	
	
	@Override
	public JSONObject exportToJSON() {
		
		JSONObject obj=super.exportToJSON();
		obj.put(GameProperties.OBJECT_TYPE,JavaConsts.OBJECT_GAME_COMPONENT);
		obj.put(GameProperties.IDENTIFIER,ident);
		if (!exportComponentToJSON(obj)){
			return null;
		}
		return obj;
	}

	protected abstract boolean exportComponentToJSON(JSONObject obj);

	@Override
	public void importFromJSON(JSONObject obj) {
		super.importFromJSON(obj);
		String genericEvent = BenchConsts.EV_ASSEMBLING+":importFromJSON::Component:";
		String event = genericEvent + getClass().getSimpleName();
		Bench.start(genericEvent,BenchConsts.CAT_ATOMIC_INIT);
		Bench.start(event,BenchConsts.CAT_COMP_INIT);
		importComponentFromJSON(obj);
		resetComponent();	
		Bench.end(event);
		Bench.end(genericEvent);
	}

	protected abstract void importComponentFromJSON(JSONObject obj);
	
	
	@Override
	public boolean isComponent() {
		return true;
	}
}
