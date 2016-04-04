package com.cristiano.java.gm.ecs.systems.ui;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyElementComponent;
import com.cristiano.java.gm.ecs.comps.ui.UILayerComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.nifty.NiftyPropertyApplier;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.elements.Element;

public class UILayerSystem extends AbstractUISystem {

	public UILayerSystem() {
		super(GameComps.COMP_UI_LAYER);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (component.isFirstTick()) {
			UILayerComponent comp = (UILayerComponent) component;
			if (comp.screen == null) {
				return;
			}
			initInternational();

			comp.niftyElement = createLayer(ent, comp);
			if (comp.niftyElement == null) {
				return;
			}
			adicionaPanels(comp);
			comp.firstTick = false;
			comp.archive();
		}
	}

	private Element createLayer(IGameEntity ent, final UILayerComponent comp) {
		NiftyComponent niftyComp = (NiftyComponent) ent.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		if (niftyComp == null) {
			return null;
		}

		//Bench: desprezivel
		comp.attachComponent(niftyComp);
		Nifty nifty = niftyComp.nifty;
		LayerBuilder layerB = new LayerBuilder(comp.name);
		NiftyPropertyApplier.aplicaProperties(layerB, comp.getElement(), comp, entMan, inter);
		Log.debug("Criando layer com ID:" + comp.name);
		Element layer = layerB.build(nifty, comp.screen, comp.screen.getRootElement());

		NiftyElementComponent niftyElement = (NiftyElementComponent) entMan.addComponent(GameComps.COMP_UI_NIFTY_ELEMENT, comp);
		niftyElement.control = layer;
		
		return layer;
	}

	private void adicionaPanels(UILayerComponent comp) {
		Bench.start(BenchConsts.EV_UI_LAYER_ADD_PANELS,BenchConsts.CAT_ATOMIC_INIT);
		List<IGameComponent> panels = comp.getComponentsWithIdentifier(GameComps.COMP_UI_PANEL);
		if (panels.isEmpty()) {
			createPanelComponents(comp);
		} else {
			for (IGameComponent uicomp : panels) {
				UIPanelComponent panelC = (UIPanelComponent) uicomp;
				panelC.parent = comp.niftyElement;
				panelC.screen = comp.screen;
			}
		}
		Bench.end(BenchConsts.EV_UI_LAYER_ADD_PANELS);

	}

	private void createPanelComponents(UILayerComponent comp) {
		List<IGameElement> panels = comp.getElement().getParamAsGEList(Extras.LIST_PROPERTY, GameProperties.PANELS);
		comp.children = panels;
		for (IGameElement elPanel : panels) {
			createPanelComponent(comp, comp.niftyElement, comp.screen, elPanel);
		}
	}

}
