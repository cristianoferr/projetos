package com.cristiano.java.gm.ecs.comps.visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.ui.billboard.IBillboardElement;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;

public class BillboardComponent extends GameComponent {
	BillboardControl billboardControl;
	public final List<IBillboardElement>listToAdd=new ArrayList<IBillboardElement>();//list to add... will be removed by the system
	public final HashMap<String,IBillboardElement>elements=new HashMap<String,IBillboardElement>();
	public Node billboard;

	public BillboardComponent() {
		super(GameComps.COMP_BILLBOARD);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	
	@Override
	public void free() {
		super.free();
		billboardControl=null;
		listToAdd.clear();
		elements.clear();billboard=null;
	}
	
	@Override
	public IGameComponent clonaComponent() {
		BillboardComponent ret = new BillboardComponent();
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void addBillboard(String name,IBillboardElement billboardElement) {
		listToAdd.add(billboardElement);
		elements.put(name,billboardElement);
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
		
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}


}
