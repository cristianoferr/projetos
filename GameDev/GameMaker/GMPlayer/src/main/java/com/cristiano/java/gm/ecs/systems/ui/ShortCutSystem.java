package com.cristiano.java.gm.ecs.systems.ui;

import java.util.HashMap;
import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.ShortCutComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

/*
 * This system is focused on environment shortcuts (save/load/toggle/etc), not entities shortcuts like movement*/
public class ShortCutSystem extends JMEAbstractSystem {

	private static HashMap<String, ShortCutComponent> mappings = new HashMap<String, ShortCutComponent>();// map:action/

	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			if (!isPressed){
				sendAction(name);
			}
		}
	};
	
	public ShortCutSystem() {
		super(GameComps.COMP_SHORTCUT);

	}


	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		ShortCutComponent comp = (ShortCutComponent) component;
		addTrigger(comp);
		comp.archive();
		//ent.removeComponent(component);
	}

	private void addTrigger(ShortCutComponent comp) {
		Trigger trigger = new KeyTrigger(comp.shortCut);
		String mapping = getMappingFor(comp);
		game.getInputManager().addMapping(mapping, trigger);
	}

	private String getMappingFor(ShortCutComponent comp) {
		ShortCutComponent compCheck = mappings.get(comp.action);
		if (compCheck == null) {
			mappings.put(comp.action, comp);
			// mapping=comp.action;
			if (comp.isAnalog) {
				this.game.getInputManager().addListener(analogListener,
						new String[] { comp.action });
			} else {
				this.game.getInputManager().addListener(actionListener,
						new String[] { comp.action });

			}
		}
		
		return comp.action;
	}

	private AnalogListener analogListener = new AnalogListener() {

		public void onAnalog(String name, float value, float tpf) {
			sendAction(name);
		}
	};

	public void sendAction(String name) {
		if (existsAction(name)){
			return;
		}
		ShortCutComponent comp = mappings.get(name);
		UIActionComponent actionComp = (UIActionComponent) entMan
				.addComponent(GameComps.COMP_UI_ACTION,
						game.getGameEntity());
		actionComp.action = comp.action;
		actionComp.originComponent = comp;
	}
	
	public boolean existsAction(String name) {
		List<IGameComponent> comps = game.getGameEntity().getComponentsWithIdentifier(GameComps.COMP_UI_ACTION);
		for (IGameComponent comp:comps){
			UIActionComponent action=(UIActionComponent) comp;
			if (action.action.equals(name)){
				return true;
			}
		}
		return false;
	}

	

}
