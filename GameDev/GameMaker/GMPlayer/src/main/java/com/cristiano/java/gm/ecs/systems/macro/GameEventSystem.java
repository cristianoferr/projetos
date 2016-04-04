package com.cristiano.java.gm.ecs.systems.macro;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.macro.GameEventComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitResourcesComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.interfaces.IReceiveGameEvent;
import com.cristiano.jme3.ui.battleNotif.BattleNotifControl;
import com.cristiano.utils.Log;
import com.jme3.audio.AudioNode;

import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

/*
 * Unit Test: TestGameEvents*/
public class GameEventSystem extends JMEAbstractSystem implements
		IReceiveGameEvent {

	// quick use... set automatically...
	GameGenreComponent genreC = null;
	private UnitResourcesComponent unitResources = null;
	private int increment = 0;
	private String battleNotifPanel = null;

	public GameEventSystem() {
		super(GameComps.COMP_GAME_EVENT);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {

		GameEventComponent comp = (GameEventComponent) component;

		// single use component...

		if (resolveDmgReceived(ent, comp)) {
			ent.removeComponent(comp);
			return;
		}

		GameEventComponent eventData = getEventDataFromGameGenre(comp.eventName);
		if (eventData == null) {
			Log.warn(comp.eventName
					+ " is not treated by the current gameGenre...");
		} else {
			// Log.debug("Event:"+comp.eventName);
			eventRewardize(ent, comp, eventData);
			if (eventData.showScrollingText) {
				showScrollingText(ent, comp);
			}
		}
		ent.removeComponent(comp);

	}

	private GameEventComponent getEventDataFromGameGenre(String eventName) {
		if (genreC == null) {
			genreC = getGameGenre();
			unitResources = getUnitResourcesComponent();
		}
		return genreC.getEventWithName(eventName);
	}

	private void eventRewardize(IGameEntity ent, GameEventComponent comp,
			GameEventComponent eventData) {
		if (eventData.hitPointReward != 0) {
			applyRewardToResource(ent, GameComps.COMP_RESOURCE_HEALTH, comp,
					eventData.hitPointReward, eventData.multiByValue, eventData);
		}
		if (eventData.ctfReward != 0) {
			applyRewardToResource(ent, GameComps.COMP_RESOURCE_FLAG, comp,
					eventData.ctfReward, eventData.multiByValue, eventData);
		}
		if (eventData.livesReward != 0) {
			applyRewardToResource(ent, GameComps.COMP_RESOURCE_LIVES, comp,
					eventData.livesReward, eventData.multiByValue, eventData);
		}
		if (eventData.pointReward != 0) {
			applyRewardToResource(ent, GameComps.COMP_RESOURCE_POINTS, comp,
					eventData.pointReward, eventData.multiByValue, eventData);
		}
		if (eventData.timeReward != 0) {
			applyRewardToResource(ent, GameComps.COMP_RESOURCE_TIME, comp,
					eventData.timeReward, eventData.multiByValue, eventData);
		}

	}

	private void applyRewardToResource(IGameEntity ent, String gameResource,
			GameEventComponent comp, float reward, boolean multiByValue,
			GameEventComponent eventData) {
		if (multiByValue) {
			reward *= comp.value;
		}
		// TODO: tentar simplificar isso...
		// Log.debug(ent + ":: Applying reward: " + reward + " to resource:" +
		// gameResource);
		ResourceComponent resource = unitResources.getResource(gameResource);
		if (resource == null) {
			Log.error("No resource found for: " + gameResource);
			return;
		}
		comp.notificationType = resource.notificationType;
		ResourceComponent resourceC = (ResourceComponent) ent
				.getComponentWithIdentifier(resource.getIdentifier());
		if (resourceC == null) {
			Log.error("No resource conponent found: "
					+ resource.getIdentifier());
			return;
		}

		if (eventData.soundObj != null) {
			playSound(eventData, eventData.soundObj.getValue());
		}

		comp.finalValue = reward;
		resourceC.addValue(reward);
	}

	private void playSound(GameEventComponent eventData, String value) {
		Log.debug("Playing event Sound:" + value);
		if (eventData.audio == null) {
			eventData.audio = new AudioNode(game.getAssetManager(), value,
					false);
		}
		eventData.audio.setVolume(5);
		eventData.audio.setPositional(false);
		eventData.audio.play();
	}

	private boolean resolveDmgReceived(IGameEntity ent, GameEventComponent comp) {
		if (comp.eventName.equals(LogicConsts.EVENT_DMG_RECEIVED)) {
			int relationBetween = getRelationBetween(ent, comp.sender);
			createEvent(LogicConsts.EVENT_ENTITY_HIT, comp.sender, ent,
					comp.value);
			if (relationBetween == LogicConsts.RELATION_FRIEND) {
				createEvent(LogicConsts.EVENT_FRIEND_HIT, ent, comp.sender,
						comp.value);
			} else {
				createEvent(LogicConsts.EVENT_ENEMY_HIT, ent, comp.sender,
						comp.value);
			}
			return true;
		}
		return false;
	}

	private GameEventComponent createEvent(String event, IGameEntity sender,
			IGameEntity receiver, float value) {
		GameEventComponent dmgSender = (GameEventComponent) entMan
				.addComponent(GameComps.COMP_GAME_EVENT, receiver);
		dmgSender.sender = sender;
		dmgSender.value = value;
		dmgSender.eventName = event;
		return dmgSender;
	}

	private void showScrollingText(IGameEntity ent, GameEventComponent comp) {
		if (comp.notificationType == LogicConsts.BATTLE_NOTIF_NONE) {
			return;
		}
		BattleNotifControl control = buildNotif();
		if (control == null) {
			return;
		}
		PhysicsComponent physC = (PhysicsComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		if (comp.notificationType == LogicConsts.BATTLE_NOTIF_DAMAGE) {
			if (comp.finalValue == 0) {
				control.createMissNotif(physC.controlBody);
				return;
			}
			control.createDamageNotif(physC.controlBody, (int) comp.finalValue,
					comp.isCritical);
		}

		if (comp.notificationType == LogicConsts.BATTLE_NOTIF_SHOUT) {

			control.createShoutNotif(physC.controlBody,
					Integer.toString((int) comp.finalValue));
		}
	}

	private synchronized BattleNotifControl buildNotif() {
		String id = "battleInfo-battleNotif_" + increment;
		increment++;

		NiftyComponent niftyC = getNiftyComponent();
		Screen screen = niftyC.getCurrentScreen();
		ControlBuilder builder = new ControlBuilder(id,
				LogicConsts.BATTLE_NOTIF_CONTROL);
		String battleNotifPanel = getBattleNotifPanel();
		Element panel = niftyC.getPanelWithID(screen, battleNotifPanel);
		if (panel == null) {
			Log.error("No Battle Notification panel found!");
			return null;
		}
		builder.build(niftyC.nifty, screen, panel);
		Element notif = screen.findElementByName(id);
		notif.layoutElements();
		return notif.getControl(BattleNotifControl.class);
	}

	private String getBattleNotifPanel() {
		if (battleNotifPanel != null) {
			return battleNotifPanel;
		}
		GameConstsComponent consts = getGameConstsComponent();
		battleNotifPanel = consts.battleNotifPanelID;
		return battleNotifPanel;
	}

	@Override
	public void dispatchEvent(String event, float value, boolean critical,
			IControlBody sender, IControlBody receiver) {
		IGameEntity senderE = (IGameEntity) sender.getDefines().owner;
		IGameEntity receiverE = (IGameEntity) receiver.getDefines().owner;
		Log.debug("GameEventSystem: dispatching event::" + event+" sender:"+senderE);
		// GameEventComponent comp=(GameEventComponent)
		// entMan.addComponent(GameComps.COMP_GAME_EVENT, getGameEntity());
		GameEventComponent createEvent = createEvent(event, senderE, receiverE,
				value);
		createEvent.isCritical = critical;

	}

}
