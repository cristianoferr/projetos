package com.cristiano.java.gm.ecs.comps.map;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class BubbleDataComponent extends GameComponent {

	public List<BubbleComponent> enviros = new ArrayList<BubbleComponent>();
 
	public BubbleDataComponent() {
		super(GameComps.COMP_BUBBLE_DATA);
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		loadEnviros(ge.getObjectList(GameProperties.ENVIROS),enviros);
	}
	
	@Override
	public void free() {
		super.free();
		enviros.clear();
	}
	
	@Override
	public IGameComponent clonaComponent() {
		BubbleDataComponent ret=new BubbleDataComponent();
		ret.enviros.clear();
		ret.enviros.addAll(enviros);
		finishClone(ret);
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.ENVIROS,CRJsonUtils.exportList(enviros));
		return false;//This shouldnt be exported
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		enviros=CRJsonUtils.importList((JSONObject) obj.get(GameProperties.ENVIROS), factory);
	}
	
	
	public void loadEnviros(List<IGameElement> elementList, List<BubbleComponent> listEnviros) {
		for (IGameElement elEnviro : elementList) {
			if (!elEnviro.hasTag(Extras.TAG_NOTREADY)) {
				addEnviro(listEnviros,elEnviro);
			}
		}
	}

	private void addEnviro(List<BubbleComponent> listEnviros, IGameElement elEnviro) {
		BubbleComponent enviro = new BubbleComponent();
		enviro.setEntityManager(entMan);
		enviro.loadFromElement(elEnviro);
		listEnviros.add(enviro);
	}

	public BubbleComponent cloneBubbleFittingInto(Vector3f dimensions,
			String bubbleFilter) {
		if (bubbleFilter==null){
			Log.error("BubbleFilter is null.");
			return null;
		}
		List<BubbleComponent> ret=new ArrayList<BubbleComponent>();
		
		for (BubbleComponent enviro:enviros){
			if (enviro.getElement().hasAnyTag(bubbleFilter)){
				if (enviro.fitsInto(dimensions)){
					ret.add(enviro);
				}
			}
		}
		if (ret.size()==0){
			Log.warn("No Bubble found fitting in: "+dimensions+" and with filter:"+bubbleFilter);
			return null;
		}
		BubbleComponent bubbleComponent = ret.get((int)(ret.size()*CRJavaUtils.random()));
		return (BubbleComponent) bubbleComponent.clonaComponent();
	}

	@Override
	public void resetComponent() {
	}

	
}
