package com.cristiano.java.gm.ecs.systems.ui;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyElementComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIControlComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.PointsResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.UnitPositionComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.nifty.NiftyPropertyApplier;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.gm.utils.GameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.elements.Element;

/*
 * Unit Test:TestUISystems
 * */

public class UIControlSystem extends AbstractUISystem {

	private final String futureName = "UIControlLoading";
	private boolean futureInUse=false;
	
	private UIControlComponent _compInUse=null;
	private IGameEntity _entInUse;
	boolean changedToLowPriority=false;

	public UIControlSystem() {
		super(GameComps.COMP_UI_CONTROL);
	}

	@Override
	protected void preTick(float tpf) {
		super.preTick(tpf);
		if (futureInUse) {
			checkControlCreation(_entInUse, _compInUse);
		}
		if (!changedToLowPriority){
			if (GameState.currentStage!=GameState.STG_INIT &&GameState.currentStage!=GameState.STG_START){
				changePriorityLow();
				changedToLowPriority=true;
			}
		}
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		UIControlComponent comp = (UIControlComponent) component;
		if (!niftyReady()) {
			return;
		}
		initInternational();
		if (component.isFirstTick()) {
			componentCreation(ent, comp);
		} else {
			Bench.start("UIControlSystem - updateControl", BenchConsts.CAT_SYSTEM_UPDATE);
			updateControl(ent, comp);
			Bench.end("UIControlSystem - updateControl");
		}

	}

	private void componentCreation(IGameEntity ent, UIControlComponent comp) {
		if (!futureInUse) {
			
			Bench.start("UIControlSystem - createControl", BenchConsts.CAT_SYSTEM_UPDATE);
			createControl(ent, comp);
			Bench.end("UIControlSystem - createControl");
		}
	}

	private void checkControlCreation(IGameEntity ent, UIControlComponent comp) {
		Bench.start("UIControlSystem - finishCreatingControl", BenchConsts.CAT_SYSTEM_UPDATE);
		try {
			checkFutureTask(ent, comp);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Log.error("Exception finishing Control: " + e.getMessage());
			futureInUse = false;
			_compInUse=null;
		}
		Bench.end("UIControlSystem - finishCreatingControl");
	}

	private void checkFutureTask(IGameEntity ent, UIControlComponent comp) throws InterruptedException, ExecutionException {
		Element control = null;
		if (FutureManager.isDone(futureName)){
			control=(Element) FutureManager.retrieveFuture(futureName);
		} else {
			return;
		}
		if (control == null) {
			Log.fatal("Nifty Control is null!");
		}
		futureInUse = false;

		finishControlCreation(comp, control);
	}

	private void finishControlCreation(UIControlComponent comp, Element control) {
		comp.niftyElement = control;

		NiftyElementComponent niftyElement = (NiftyElementComponent) entMan.addComponent(GameComps.COMP_UI_NIFTY_ELEMENT, comp);
		niftyElement.control = control;

		if (!comp.isVisible) {
			NiftyComponent.changeVisibility(comp, comp.isVisible);
		}
		comp.firstTick = false;
	}

	private void createControl(IGameEntity ent, UIControlComponent comp) {

		NiftyComponent niftyComp = (NiftyComponent) ent.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		if (comp.screen == null || comp.parent == null) {
			UIPanelComponent panel = (UIPanelComponent) ent;
			comp.screen = panel.screen;
			comp.parent = panel.niftyElement;
			if (panel.firstTick) {
				return;
			}
			if (comp.screen == null) {
				Log.error("Screen is null for control: " + comp);
				return;
			}
		}

		if (niftyComp == null) {
			return;
		}
		Nifty nifty = niftyComp.nifty;
		comp.elUI = comp.getElement().getPropertyAsGE(GameProperties.CONTROL);
		String classe = comp.elUI.getParamAsText(Extras.LIST_DOMAIN, Extras.DOMAIN_CLASS_NAME);
		if (!classe.equals("")) {
			Log.debug("Creating nifty control:" + comp.name);
			comp._controlBuilder = (ElementBuilder) CRJavaUtils.instanciaClasse(classe, comp.name);
			NiftyPropertyApplier.aplicaProperties(comp._controlBuilder, comp.getElement(), comp, entMan, inter);
			loadEvents(comp._controlBuilder, comp.events);
			comp._nifty = nifty;
			createFutureTask(ent,comp);
			// /Element control = comp._controlBuilder.build(comp._nifty,
			// comp.screen, comp.parent);
			// finishControlCreation(comp, control);

		} else {
			Log.error("Controle sem classe definida:" + comp.elUI);
		}
	}

	private void createFutureTask(IGameEntity ent, UIControlComponent comp) {
		futureInUse=true;
		FutureManager.requestFuture(futureName, comp.createControl);
		_compInUse=comp;
		_entInUse=ent;
	}

	private void loadEvents(ElementBuilder controlBuilder, List<IGameElement> events) {
		if (events.size() == 0) {
			return;
		}
		Log.debug("Loading events..." + events.size());
		for (IGameElement event : events) {
			String eventType = event.getProperty(GameProperties.EVENT);

			loadEvent(controlBuilder, eventType, event);
		}
	}

	private void loadEvent(ElementBuilder controlBuilder, String eventType, IGameElement event) {
		Log.debug("Loading event:" + eventType);
		EffectBuilder effB = new EffectBuilder(event.getName());
		List<IGameElement> params = event.getObjectList(GameProperties.PARAMS);
		for (IGameElement param : params) {
			String name = param.getName();
			String value = param.getValue();
			if (!value.equals("")) {
				effB.effectParameter(name, value);
			}
		}

		if (eventType.equals(GameConsts.NIFTY_EVENT_CLICK)) {
			controlBuilder.onClickEffect(effB);
			return;
		}
		if (eventType.equals(GameConsts.NIFTY_EVENT_FOCUS)) {
			controlBuilder.onFocusEffect(effB);
			return;
		}
		if (eventType.equals(GameConsts.NIFTY_EVENT_HOVER)) {
			Log.error("onHover not implemented");
			return;
		}
		if (eventType.equals(GameConsts.NIFTY_EVENT_START_SCREEN)) {
			controlBuilder.onStartScreenEffect(effB);
			return;
		}
		Log.error("Unknown event type:" + eventType);
	}

	private void updateControl(IGameEntity ent, UIControlComponent comp) {
		if (comp.dataSet <= 0) {
			// comp.archive();
			return;
		}

		String value = getDataSetValue(comp.dataSet);

		// if I already know the element, then I update it here...
		updateElement(ent, comp, value);

	}

	private boolean updateElement(IGameEntity ent, UIControlComponent comp, String value) {
		return comp.setText(value);

	}

	private String getDataSetValue(int dataSet) {
		if (dataSet == LogicConsts.DATASET_SCORE) {
			return getDataSetScore();
		}
		if (dataSet == LogicConsts.DATASET_LAP_POSITION) {
			return getDataSetLapPosition();
		}

		if (dataSet == LogicConsts.DATASET_LAP) {
			return getDataSetLap();
		}

		Log.error("Unkwnown dataSet:" + dataSet);
		return "";
	}

	private String getDataSetLapPosition() {
		String value = "";
		IGameEntity playerEntity = getPlayerEntity();
		if (playerEntity != null) {
			UnitPositionComponent resC = (UnitPositionComponent) playerEntity.getComponentWithIdentifier(GameComps.COMP_RESOURCE_UNIT_POSITION);

			if (resC != null) {
				// TeamMemberComponent teamC = (TeamMemberComponent)
				// playerEntity.getComponentWithIdentifier(GameComps.COMP_TEAM_MEMBER);
				value = Integer.toString((int) resC.getCurrValue()) + "/" + Integer.toString((int) resC.getMaxValue());
			}
		}
		return value;
	}

	private String getDataSetLap() {
		String value = "";
		IGameEntity playerEntity = getPlayerEntity();
		if (playerEntity != null) {
			LapResourceComponent resC = (LapResourceComponent) playerEntity.getComponentWithIdentifier(GameComps.COMP_RESOURCE_LAP);

			if (resC != null) {
				// TeamMemberComponent teamC = (TeamMemberComponent)
				// playerEntity.getComponentWithIdentifier(GameComps.COMP_TEAM_MEMBER);
				value = Integer.toString((int) resC.getCurrValue()) + "/" + Integer.toString((int) resC.getMaxValue());
			}
		}
		return value;
	}

	private String getDataSetScore() {
		int value = 0;
		IGameEntity playerEntity = getPlayerEntity();
		if (playerEntity != null) {
			PointsResourceComponent pointC = (PointsResourceComponent) playerEntity.getComponentWithIdentifier(GameComps.COMP_RESOURCE_POINTS);
			if (pointC != null) {
				value = (int) pointC.getCurrValue();
			}
		}
		return Integer.toString(value);
	}
}
