package com.cristiano.java.gm.ecs.comps.macro;

import java.util.List;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

/**
 * This Component is responsible for generating new entities. After generated,
 * the entities are attached a TeamMember Component.
 * 
 * @author CMM4
 * 
 */
public class QueryBestiaryComponent extends GameComponent {
	//TODO: get this from the team
	public float budgetToSpend=100;
	
	public int unitsDelivered = 0;
	public boolean isSolo = false;

	public int entityType = LogicConsts.ENTITY_DYNAMIC;

	public UnitClassComponent selectedClass;
	public String tagSource = null;
	
	//the component will be discarded when consumed...
	public boolean selfRemoval=true;
	public IGameEntity clonedUnit;
	public IGameEntity moldeUnit;
	public String futureQuery=null;
	
	//this contains the roleIdentifier, informed by the teamSystem and which must be already loaded on the libraryCompenent
	public String roleIdentifier=null;

	public QueryBestiaryComponent() {
		super(GameComps.COMP_BESTIARY_QUERY);

	}

	@Override
	public void free() {
		super.free();
		unitsDelivered = 0;
		clonedUnit=null;
		futureQuery=null;
		roleIdentifier=null;
		moldeUnit=null;
		isSolo = false;
		selfRemoval = true;
		selectedClass = null;

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		isSolo = ge.getPropertyAsBoolean(GameProperties.IS_SOLO);
	}

	@Override
	public IGameComponent clonaComponent() {
		return null;
	}

	public void finalizeDeliveredEntity(IGameEntity clone) {

		unitsDelivered++;
		List<IGameComponent> compsToClone = getAllComponents();
		for (IGameComponent comp : compsToClone) {
			if (clone.containsComponent(comp.getIdentifier())) {
				IGameComponent existingComp = clone.getComponentWithIdentifier(comp
						.getIdentifier());
				existingComp.merge(comp);
			} else {
				clone.attachComponent(comp.clonaComponent());
			}
		}
		Log.debug("Delivering clone:" + clone + " by " + this + " ("
				+ tagSource + ")");
	}

	public boolean readyToDeliver() {
		return clonedUnit!=null;
	}

	@Override
	public void resetComponent() {
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
	}

	public IGameEntity retrieveEntity() {
		return clonedUnit;
	}

	
	//Multi-threading:
	public Callable<Object> cloneEntity = new Callable<Object>() {
		public Object call() throws Exception {
			IGameEntity clone =entMan.clonaEntidade(moldeUnit);
			return clone;
		}
	};
	
	
	
}
