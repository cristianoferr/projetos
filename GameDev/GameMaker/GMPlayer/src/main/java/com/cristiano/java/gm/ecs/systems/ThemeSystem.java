package com.cristiano.java.gm.ecs.systems;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.LightComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIScreenComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.comps.visual.ThemeComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class ThemeSystem extends JMEAbstractSystem {

	public ThemeSystem() {
		super(GameComps.COMP_THEME);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (component.isFirstTick()) {
			ThemeComponent comp = (ThemeComponent) component;
			comp.firstTick = false;

			if (comp.elGenre == null) {
				comp.elGenre = comp.elTheme.getPropertyAsGE(GameProperties.GAME_GENRE);
				loadGenre(ent, comp);
			} else {
				ent.getComponentWithIdentifier(GameComps.COMP_GAME_GENRE);
			}

			// if (!CRJavaUtils.isRelease()){
			addLights(ent, comp.elTheme);
			loadScreens(ent, comp);
			// }

		}
	}

	private void loadScreens(IGameEntity ent, ThemeComponent comp) {
		List<IGameComponent> screens = ent.getComponentsWithIdentifier(GameComps.COMP_UI_SCREEN,new ArrayList<IGameComponent>());
		if (screens.isEmpty()) {
			loadScreensFromElements(ent, comp);
		} else {
			String startName = comp.elStartScreen.getName();
			for (IGameComponent uicomp : screens) {
				UIScreenComponent screen = (UIScreenComponent) uicomp;
				String screenName = screen.getElement().getName();
				screen.isStartScreen= screenName.equals(startName);
			}
		}
	}

	private void loadScreensFromElements(IGameEntity ent, ThemeComponent comp) {
		for (IGameElement elScreen : comp.elScreens) {
			UIScreenComponent screenC = (UIScreenComponent) entMan.addComponent(GameComps.COMP_UI_SCREEN, ent);
			screenC.loadFromElement(elScreen);
			//screenC.current = elScreen == comp.elStartScreen;
			screenC.isStartScreen= elScreen == comp.elStartScreen;;
		}
	}

	private GameGenreComponent loadGenre(IGameEntity ent, ThemeComponent comp) {
		GameGenreComponent genre = (GameGenreComponent) entMan.addIfNotExistsComponent(GameComps.COMP_GAME_GENRE, ent);
		if (genre.getElement() == null) {
			if (comp.elGenre != null) {
				genre.loadFromElement(comp.elGenre);
			} else {
				Log.fatal("CRITICAL ERROR: gameGenre element null!!");

			}
		}
		return genre;

	}

	private void addLights(IGameEntity ent, IGameElement element) {
		boolean applyLight=element.getPropertyAsBoolean(GameProperties.APPLY_LIGHTS);
		if (!applyLight){
			return;
		}
		List<IGameElement> lights = element.getParamAsGEList(Extras.LIST_PROPERTY, GameProperties.LIGHTS);
		RenderComponent renderC = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
		if (renderC == null) {
			Log.error("GameWorld has no RenderComponent attached or couldnt be found (trying to add lights)");
		}
		for (IGameElement elLight : lights) {
			LightComponent light = (LightComponent) entMan.addComponent(GameComps.COMP_LIGHT, ent);
			light.loadFromElement(elLight);
		}
	}

}
