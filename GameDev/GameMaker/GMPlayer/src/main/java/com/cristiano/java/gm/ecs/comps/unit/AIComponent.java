package com.cristiano.java.gm.ecs.comps.unit;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class AIComponent extends GameComponent {
	private static final String STARTABLE_STATES = "startableStates";
	public String[] startableStates;
	
	public List<IGameElement> elStates=null;
	public List<IGameElement> elBehaviours;
	
	//NPCConditionChecker
	public List<IGameElement> elConditions=null;
	public List<IGameComponent> conditionComponents=null;


	public final List<IGameComponent> npcStates=new ArrayList<IGameComponent>();;//on loading, the state element (elStates) is transformed into a component for reuse...
	public final List<IGameComponent> npcBehaviours=new ArrayList<IGameComponent>();;
	
	private final List<IGameComponent> _reuseList=new ArrayList<IGameComponent>();

	
	public AIComponent(){
		super(GameComps.COMP_AI);
		
	}
	
	@Override
	public void free() {
		startableStates=null;
		elStates=null;
		elBehaviours=null;
		elConditions=null;
		npcStates.clear();
		npcBehaviours.clear();
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		startableStates=ge.getPropertyAsList(STARTABLE_STATES);
		elStates = ge.getObjectList(GameProperties.STATES);
		elBehaviours= ge.getObjectList(GameProperties.BEHAVIOURS);
		elConditions = ge.getObjectList(GameProperties.CONDITIONS);
		npcStates.clear();
		npcBehaviours.clear();
		_reuseList.clear();		
	}
	@Override
	public IGameComponent clonaComponent() {
		AIComponent aiComponent = (AIComponent) entMan.spawnComponent(GameComps.COMP_AI);
		aiComponent.setElement(element);
		aiComponent.startableStates=startableStates;
		aiComponent.elStates=elStates;
		aiComponent.elConditions=elConditions;
		aiComponent.elBehaviours=elBehaviours;
		aiComponent.npcBehaviours.addAll(npcBehaviours);
		aiComponent.npcStates.addAll(npcStates);
		aiComponent.conditionComponents=conditionComponents;
		return aiComponent;
	}
	@Override
	public void resetComponent() {
	}
	public String getRandomStartableState() {
		if (startableStates.length==0){
			Log.error("No startable state defined.");
			return null;
		}
		String tag=startableStates[(int) (CRJavaUtils.random()*startableStates.length)];
		return tag;
	}
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		//CRJsonUtils.exportVector(obj,STARTABLE_STATES,startableStates);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
		//startableStates=CRJsonUtils.importVector(obj,STARTABLE_STATES);
	}
	
	
	
	
	public IGameComponent getTemplateStateWithIdentifier(String stateName) {
		return getComponentWithIdentifier(npcStates,stateName);
	}
	public IGameComponent getTemplateBehaviourWithIdentifier(String stateName) {
		return getComponentWithIdentifier(npcBehaviours,stateName);
	}
	private IGameComponent getComponentWithIdentifier(List<IGameComponent> list, String stateName) {
		_reuseList.clear();
		if (list==null){
			Log.warn("NPCStates is not started yet...");
			return null;
		}
		for (IGameComponent state:list){
			if (state.getIdentifier().equals(stateName)){
				_reuseList.add(state);
			}
		}
		if (_reuseList.size()>0){
			return _reuseList.get((int) (_reuseList.size()*CRJavaUtils.random()));
		}
		Log.warn("No component was found with identifier: "+stateName);
		return null;
	}
	
	public IGameComponent getTemplateConditionWithIdentifier(String stateName) {
		for (IGameComponent state:conditionComponents){
			if (state.getIdentifier().equals(stateName)){
				return state;
			}
		}
		Log.error("No condition was found with identifier: "+stateName);
		return null;
	}

}
