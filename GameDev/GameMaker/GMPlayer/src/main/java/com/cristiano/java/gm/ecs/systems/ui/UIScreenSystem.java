package com.cristiano.java.gm.ecs.systems.ui;

import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.comps.ui.UILayerComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIScreenComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class UIScreenSystem extends AbstractUISystem implements
		ScreenController {

	public UIScreenSystem() {
		super(GameComps.COMP_UI_SCREEN);
	}
	UIScreenComponent activeScreen = null;

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		if (!niftyReady()){
			return;
		}
		UIScreenComponent comp = (UIScreenComponent) component;
		//changeScreenTo(ent, comp);
		if (component.isFirstTick()) {
			comp.screen = createScreen(ent, comp);
			adicionaLayers(comp);
			comp.firstTick = false;
			//comp.archive();

			if (comp.isStartScreen) {
				changeScreenTo(ent, comp);
			}
		}

	}

	private void changeScreenTo(IGameEntity ent, UIScreenComponent comp) {
		niftyComp.screensStarted=true;
		niftyComp.changeScreenTo(comp.getElement());
	}

	private void adicionaLayers(UIScreenComponent comp) {
		List<IGameComponent> layers = comp
				.getComponentsWithIdentifier(GameComps.COMP_UI_LAYER);

		if (layers.isEmpty()) {
			createLayerComponents(comp);
		} else {
			for (IGameComponent layer : layers) {
				UILayerComponent layerC = (UILayerComponent) layer;
				layerC.screen = comp.screen;
			}
		}
	}

	private void createLayerComponents(UIScreenComponent comp) {
		List<IGameElement> layers = comp.getElement().getParamAsGEList(
				Extras.LIST_PROPERTY, GameProperties.LAYERS);
		comp.children = layers;
		for (IGameElement elLayer : layers) {
			UILayerComponent layerC = (UILayerComponent) entMan.addComponent(
					GameComps.COMP_UI_LAYER, comp);
			if (elLayer == null) {
				Log.error("Erro: Elemento layer vazio para a tela:" + comp.name);
			}
			layerC.loadFromElement(elLayer);
			layerC.screen = comp.screen;
		}
	}

	private Screen createScreen(IGameEntity ent, UIScreenComponent comp) {
		comp.attachComponent(niftyComp);
		if (niftyComp != null) {
			final UIScreenSystem ss = this;
			Nifty nifty = niftyComp.nifty;
			if (nifty == null) {
				Log.error("Nifty not found...");
				return null;
			}
			Screen sb = new ScreenBuilder(comp.name) {
				{
					controller(ss);
				}
			}.build(nifty);
			Log.debug("Criando tela com ID:" + comp.name);
			return sb;
		}
		return null;

	}

	@Override
	public void bind(Nifty nifty, Screen screen) {

	}

	public void action(String elId, String actionName) {

		List<IGameEntity> playerEnts = entMan
				.getEntitiesWithComponent(GameComps.COMP_PLAYER);
		IGameEntity player;
		if (playerEnts.size() > 0) {
			player = playerEnts.get(0);
		} else {
			player = game.getGameEntity();
		}
		// Log.debug("action:" +
		// actionName+" vindo de:"+elId+" hasPlayer:"+hasPlayer);
		UIActionComponent actionComp = (UIActionComponent) entMan.addComponent(
				GameComps.COMP_UI_ACTION, player);
		actionComp.originComponent = entMan.getEntityWithId(Integer
				.parseInt(elId));
		actionComp.action = actionName;

	}

	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {

	}
}
