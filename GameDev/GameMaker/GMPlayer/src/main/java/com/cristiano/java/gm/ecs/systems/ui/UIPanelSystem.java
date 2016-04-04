package com.cristiano.java.gm.ecs.systems.ui;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyElementComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.nifty.NiftyPropertyApplier;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;

public class UIPanelSystem extends AbstractUISystem {

	public UIPanelSystem() {
		super(GameComps.COMP_UI_PANEL);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		UIPanelComponent comp = (UIPanelComponent) component;
		if (!niftyReady()){
			return;
		}
		if (comp.firstTick) {
			if (comp.screen==null){
				AbstractUIComponent parent = (AbstractUIComponent) ent;
				if (parent.firstTick) {
					return;
				}
				comp.screen=parent.screen;
				comp.parent=parent.niftyElement;
				if (comp.screen==null){
					Log.error("Panel Screen is null!");
				}
				return;
			}
			
			initInternational();
			String eventPanel = "UIPanelSystem - CreatePanel";
			Bench.start(eventPanel,BenchConsts.CAT_SYSTEM_UPDATE);
			comp.niftyElement = createPanel(ent, comp);
			Bench.end(eventPanel);
			
			String eventControls = "UIPanelSystem - adicionaControls";
			Bench.start(eventControls,BenchConsts.CAT_SYSTEM_UPDATE);
			adicionaControls(comp);
			Bench.end(eventControls);
		}
		
		String eventVis = "UIPanelSystem - visibility";
		Bench.start(eventVis,BenchConsts.CAT_SYSTEM_UPDATE);
		checkVisibility(getPlayerEntity(),comp);
		// component.archive();
		Bench.end(eventVis);
		
	}

	public void checkVisibility(IGameEntity ent, UIPanelComponent comp) {
		if (ent==null){
			return;
		}
		if (!comp.hasVisibilityConditions()){
			//comp.archive();
			return;
		}
		boolean visibilityCurrent=comp.isVisible(ent);
		if (comp.isVisible){
			if (!visibilityCurrent){
				changeVisibility(ent,comp,visibilityCurrent);
			}
		} else {
			if (visibilityCurrent){
				changeVisibility(ent,comp,visibilityCurrent);
			}
		}
		comp.isVisible=visibilityCurrent;
		
	}

	

	private Element createPanel(IGameEntity ent, UIPanelComponent comp) {
		NiftyComponent niftyComp = (NiftyComponent) ent.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		if (niftyComp == null) {
			return null;
		}
		comp.firstTick = false;
		comp.attachComponent(niftyComp);
		Nifty nifty = niftyComp.nifty;
		PanelBuilder panelB = new PanelBuilder(comp.name);
		Log.debug("Criando painel com ID:" + comp.name);
		NiftyPropertyApplier.aplicaProperties(panelB, comp.getElement(), comp, entMan, inter);
		Element panel = panelB.build(nifty, comp.screen, comp.parent);

		NiftyElementComponent niftyElement = (NiftyElementComponent) entMan.addComponent(GameComps.COMP_UI_NIFTY_ELEMENT, comp);
		niftyElement.control = panel;

		return panel;
	}

	private void adicionaControls(UIPanelComponent comp) {
		List<IGameComponent> panels=comp.getComponents(GameComps.TAG_NIFTY_UI_COMPONENT);
		if (panels.isEmpty()){
			createControls(comp);
		} 

	}

	private void createControls(UIPanelComponent comp) {
		List<IGameElement> controls = comp.getElement().getParamAsGEList(Extras.LIST_PROPERTY, GameProperties.CONTROLS);
		comp.children = controls;
		for (IGameElement elControl : controls) {
			// selectively add control or panel...
			if (elControl.getIdentifier().equals(GameComps.COMP_UI_CONTROL)) {
				createControlComponent(comp, comp.niftyElement, comp.screen, elControl);
			}
			if (elControl.getIdentifier().equals(GameComps.COMP_UI_PANEL)) {
				addPanel(comp, elControl);
			}
		}
	}

	private void addPanel(UIPanelComponent comp, IGameElement elControl) {
		createPanelComponent(comp, comp.niftyElement, comp.screen, elControl);
	}

}
