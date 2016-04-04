package com.cristiano.java.gm.ecs.comps.macro;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

/*
 * This gets the component from the entity and updates the element
 * Components must extend type ResourceComponent
 * */
class ConditionChecker {

	String[] components = null;
	private String check0, check1, oper;
	private IGameElement element;

	public ConditionChecker(IGameElement el) {
		components = el.getPropertyAsList(VictoryCheckerComponent.COMPONENTS);
		check0 = el.getProperty(VictoryCheckerComponent.CHECK0).replace("#",
				"$");
		check1 = el.getProperty(VictoryCheckerComponent.CHECK1).replace("#",
				"$");
		oper = el.getProperty(VictoryCheckerComponent.OPER).replace("#", "$");
		this.element = el;
	}

	public boolean checkCondition(IGameEntity ent) {
		String unsolved0 = updateProperties(ent, check0);
		String unsolved1 = updateProperties(ent, check1);
		float v0 = Float.parseFloat(unsolved0);
		float v1 = Float.parseFloat(unsolved1);
		if (oper.equals(">")) {
			return v0 > v1;
		}
		if (oper.equals(">=")) {
			return v0 >= v1;
		}
		if (oper.equals("<")) {
			return v0 < v1;
		}
		if (oper.equals("<=")) {
			return v0 <= v1;
		}
		if (oper.equals("==")) {
			return v0 == v1;
		}
		Log.fatal("Unknown type: " + oper);
		return false;
	}

	private String updateProperties(IGameEntity ent, String check) {
		String ret = check;
		for (int i = 0; i < components.length; i++) {
			String ident = components[i];
			ResourceComponent res = (ResourceComponent) ent
					.getComponentWithTag(ident);
			ret = ret.replace("$c" + i + "Value",
					Float.toString(res.getCurrValue()));
			ret = ret.replace("$c" + i + "MaxValue",
					Float.toString(res.getMaxValue()));
		}
		return ret;
	}

}

public class VictoryCheckerComponent extends GameComponent {
	public static final String VICTORY_CONDITIONS = "victoryConditions";
	public static final String FINISH_CONDITIONS = "finishConditions";
	public static final String CHECK0 = "check0";
	public static final String CHECK1 = "check1";
	public static final String OPER = "oper";
	public static final String COMPONENTS = "components";

	public final List<ConditionChecker> finishConditions = new ArrayList<ConditionChecker>();
	public final List<ConditionChecker> victoryConditions = new ArrayList<ConditionChecker>();

	public VictoryCheckerComponent() {
		super(GameComps.COMP_VICTORY_CHECKER);

	}

	@Override
	public void loadFromElement(IGameElement el) {
		super.loadFromElement(el);
		List<IGameElement> elFinishConditions = el
				.getPropertyAsGEList(FINISH_CONDITIONS);
		List<IGameElement> elVictoryConditions = el
				.getPropertyAsGEList(VICTORY_CONDITIONS);
		loadConditions(finishConditions, elFinishConditions);
		loadConditions(victoryConditions, elVictoryConditions);
		if (finishConditions.isEmpty()) {
			Log.fatal("FinishConditions for element " + el.getIdentifier()
					+ " is empty.");
		}
		if (victoryConditions.isEmpty()) {
			Log.fatal("VictoryConditions for element " + el.getIdentifier()
					+ " is empty.");
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

	public void loadConditions(List<ConditionChecker> ret,
			List<IGameElement> elConditions) {
		for (IGameElement el : elConditions) {
			ret.add(new ConditionChecker(el));
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		VictoryCheckerComponent ret = new VictoryCheckerComponent();
		ret.finishConditions.clear();
		ret.victoryConditions.clear();

		ret.finishConditions.addAll(finishConditions);
		ret.victoryConditions.addAll(victoryConditions);
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public boolean checkFinishConditions(IGameEntity unitEnt) {
		return checkConditions(finishConditions, unitEnt);
	}

	private boolean checkConditions(List<ConditionChecker> conditions,
			IGameEntity unitEnt) {
		for (ConditionChecker check : conditions) {
			if (check.checkCondition(unitEnt)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkVictoryConditions(IGameEntity unitEnt) {
		return checkConditions(victoryConditions, unitEnt);
	}

}
