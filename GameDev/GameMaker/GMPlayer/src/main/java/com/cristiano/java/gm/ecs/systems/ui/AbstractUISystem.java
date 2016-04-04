package com.cristiano.java.gm.ecs.systems.ui;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIControlComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIPanelComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public abstract class AbstractUISystem extends JMEAbstractSystem {

	protected NiftyComponent niftyComp = null;

	public AbstractUISystem(String tipo) {
		super(tipo);
	}

	protected boolean niftyReady() {
		if (niftyComp == null) {
			niftyComp = (NiftyComponent) game.getGameEntity().getComponentWithIdentifier(GameComps.COMP_NIFTY);
		}
		if (niftyComp == null) {
			return false;
		}
		return (niftyComp.loaded);
	}

	protected void createPanelComponent(IGameEntity ent, Element parent, Screen screen, IGameElement elPanel) {
		UIPanelComponent panelC = (UIPanelComponent) entMan.addComponent(GameComps.COMP_UI_PANEL, ent);
		panelC.loadFromElement(elPanel);
		panelC.parent = parent;
		panelC.screen = screen;
		panelC.firstTick = true;
	}

	protected void createControlComponent(IGameEntity ent, Element parent, Screen screen, IGameElement elControl) {
		UIControlComponent controlC = (UIControlComponent) entMan.addComponent(GameComps.COMP_UI_CONTROL, ent);
		controlC.loadFromElement(elControl);
		controlC.parent = parent;
		controlC.screen = screen;
	}

	protected void changeVisibility(IGameEntity ent, AbstractUIComponent comp, boolean visibilityCurrent) {
		if (comp.niftyElement == null) {
			Log.errorIfRunning("UI Element is null!");
			return;
		}
		Log.info("Changing UI visibility of " + comp.name + "  to " + visibilityCurrent);
		comp.niftyElement.setVisible(visibilityCurrent);
	}
}
