package com.cristiano.java.gm.ecs.comps.persists;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public class CloneComponent extends GameComponent {

	public int idMolde=0;
	public CloneComponent(){
		super(GameComps.COMP_CLONE);
		
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		idMolde=ge.getPropertyAsInt(GameProperties.ID_MASTER);
	}
	
	@Override
	public void free() {
		super.free();
		idMolde=0;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		CloneComponent ret = new CloneComponent();
		ret.idMolde=idMolde;
		return ret;
	}
	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.ID_MASTER,this.idMolde);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		idMolde = CRJsonUtils.getInteger(obj,GameProperties.ID_MASTER);
		
	}
}
