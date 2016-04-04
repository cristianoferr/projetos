package com.cristiano.java.gm.ecs.comps.unit.resources;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

/*
 * Bateria armazena qualquer tipo de energia que se esvai e recupera com tempo.
 * Exemplos: eletricidade e mana
 * */
public class BatteryComponent  extends ResourceComponent {

	public float rechargeSec=0;
	public BatteryComponent(){
		super(GameComps.COMP_BATTERY);
		
	}
	@Override
	public void free() {
		super.free();
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		loadData(ge);
	}
	private void loadData(IGameElement ge) {
		rechargeSec=ge.getPropertyAsFloat(GameProperties.RECHARGE_SEC);
	}
	
	
	@Override
	public IGameComponent clonaComponent() {
		BatteryComponent ret = (BatteryComponent) entMan.spawnComponent(GameComps.COMP_BATTERY);
		ret.setCurrValue(getCurrValue());
		ret.setMaxValue(getMaxValue());
		ret.rechargeSec=rechargeSec;
		return ret;
	}
	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		super.exportComponentToJSON(obj);
		obj.put(GameProperties.RECHARGE_SEC, rechargeSec);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		super.importComponentFromJSON(obj);
		rechargeSec=CRJsonUtils.getFloat(obj,GameProperties.RECHARGE_SEC);	
	}


}
