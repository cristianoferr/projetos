package com.cristiano.java.gm.ecs.comps.unit.resources;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public abstract class ResourceComponent extends GameComponent {
	// from unitResource:
	public float startingValue;
	public float weight; // peso define qual o peso da role no budget...
	public float budgetPrice;
	public float randomMulti;
	public int scope;
	public int notificationType;

	protected float maxValue = 0;
	public float currValue = 0;

	@Override
	public void free() {
		super.free();
		startingValue = 0;
		weight = 0;
		budgetPrice = 0;
		randomMulti = 0;
		scope = 0;
		notificationType = 0;
		maxValue = 0;
		currValue = 0;
	}

	public static List<ResourceComponent> loadResources(
			List<IGameElement> elResources, EntityManager entMan,
			IGameElement opposition) {
		
		List<ResourceComponent> unitResources = new ArrayList<ResourceComponent>();
		if (entMan == null) {
			Log.fatal("EntMan undefined!");
		}
		for (IGameElement elResource : elResources) {
			if (resourceIsListed(elResource.getIdentifier(), opposition)) {
				ResourceComponent resource = (ResourceComponent) entMan
						.spawnComponent(elResource.getIdentifier());
				resource.loadFromElement(elResource);
				unitResources.add(resource);
			}
		}
		return unitResources;

	}

	private static boolean resourceIsListed(String identifier,
			IGameElement opposition) {
		if (opposition == null) {
			Log.fatalIfRunning("Opposition undefined!");
			return true;
		}
		return StringHelper.checkList(identifier, opposition,Extras.LIST_RESOURCE,
				GameProperties.SCOPE_GAME)
				|| StringHelper.checkList(identifier, opposition,Extras.LIST_RESOURCE,
						GameProperties.SCOPE_TEAM)
				|| StringHelper.checkList(identifier, opposition,Extras.LIST_RESOURCE,
						GameProperties.SCOPE_UNIT);
	}

	@Override
	public String getInfo() {
		return super.getInfo() + "CurrValue=" + getCurrValue()
				+ ":startingValue=" + startingValue + ":weight=" + weight
				+ ":budgetPrice=" + budgetPrice;
	}

	public ResourceComponent(String tipo) {
		super(tipo);

	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		this.startingValue = ge
				.getPropertyAsFloat(GameProperties.STARTING_VALUE);
		this.weight = ge.getPropertyAsFloat(GameProperties.WEIGHT);
		this.budgetPrice = ge.getPropertyAsFloat(GameProperties.BUDGET_PRICE);
		this.randomMulti = ge
				.getPropertyAsFloat(GameProperties.RANDOM_MULTIPLIER);
		this.scope = ge.getPropertyAsInt(GameProperties.SCOPE);
		this.notificationType = ge
				.getPropertyAsInt(GameProperties.NOTIFICATION_TYPE);

		maxValue = ge.getPropertyAsFloat(GameProperties.VALUE);
		currValue = getMaxValue();
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.STARTING_VALUE, startingValue);
		obj.put(GameProperties.WEIGHT, weight);
		obj.put(GameProperties.BUDGET_PRICE, budgetPrice);
		obj.put(GameProperties.RANDOM_MULTIPLIER, randomMulti);
		obj.put(GameProperties.SCOPE, scope);
		obj.put(GameProperties.NOTIFICATION_TYPE, notificationType);
		obj.put(GameProperties.VALUE, maxValue);
		obj.put(GameProperties.CURRENT_VALUE, currValue);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		startingValue = CRJsonUtils
				.getFloat(obj, GameProperties.STARTING_VALUE);
		weight = CRJsonUtils.getFloat(obj, GameProperties.WEIGHT);
		budgetPrice = CRJsonUtils.getFloat(obj, GameProperties.BUDGET_PRICE);
		randomMulti = CRJsonUtils.getFloat(obj,
				GameProperties.RANDOM_MULTIPLIER);
		scope = CRJsonUtils.getInteger(obj, GameProperties.SCOPE);
		notificationType = CRJsonUtils.getInteger(obj,
				GameProperties.NOTIFICATION_TYPE);
		maxValue = CRJsonUtils.getFloat(obj, GameProperties.VALUE);
		currValue = CRJsonUtils.getFloat(obj, GameProperties.CURRENT_VALUE);
	}

	// called from unitRoles
	public void initComponent(float budget, float totalWeight) {
		budget = budget * weight / totalWeight * randomMulti;
		if (weight == 0) {
			return;
		}

		setMaxValue(startingValue + budget / budgetPrice);
		setCurrValue(getMaxValue());
	}

	@Override
	protected void finishClone(GameComponent ret) {
		super.finishClone(ret);
		ResourceComponent res = (ResourceComponent) ret;
		res.budgetPrice = budgetPrice;
		res.notificationType = notificationType;
		res.randomMulti = randomMulti;
		res.scope = scope;
		res.startingValue = startingValue;
		res.weight = weight;
		res.setCurrValue(getCurrValue());
		res.setMaxValue(getMaxValue());
	}

	public float getPercValue() {
		return getCurrValue() / getMaxValue();
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getCurrValue() {
		return currValue;
	}

	public void setCurrValue(float currValue) {
		this.currValue = currValue;
	}

	public void addValue(float value) {
		currValue += value;
	}

	public float getWeight() {
		return weight;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}

	public int getScope() {
		return scope;
	}

	public void incValue() {
		currValue = currValue + 1;
		/*
		 * if (currValue>maxValue){ currValue=maxValue; }
		 */
	}

}
