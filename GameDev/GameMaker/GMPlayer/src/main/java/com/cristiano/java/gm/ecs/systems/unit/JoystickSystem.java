package com.cristiano.java.gm.ecs.systems.unit;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.data.IStoreProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.JoystickComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.jme3.ui.gameController.IGameJoystick;
import com.cristiano.jme3.ui.tonegod.TonegodUtils;
import com.cristiano.jme3.utils.JMEFactory;
import com.cristiano.utils.Log;

/*
 * Unit test: TestJoystickSystem
 * */

public class JoystickSystem extends JMEAbstractSystem {

	boolean wasGameRunning = false;
	private JoystickComponent joystickComp;

	public JoystickSystem() {
		super(GameComps.COMP_JOYSTICKS);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		JoystickComponent comp = (JoystickComponent) component;
		if (comp.firstTick) {
			Bench.start(BenchConsts.EV_JOYSTICK_INIT,BenchConsts.CAT_ATOMIC_INIT);
			showJoystick(ent, comp);
			hideJoystick(component, comp);
			comp.firstTick = false;
			joystickComp = comp;
			Bench.end(BenchConsts.EV_JOYSTICK_INIT);
		}
		
		//Bench:negligivel
		boolean running = isGameRunning();
		checkStatus(ent, comp, running);

	}


	private void checkStatus(IGameEntity ent, JoystickComponent comp, boolean running) {
		if (wasGameRunning != running) {
			if (running) {
				showJoystick(ent, comp);
			} else {
				hideJoystick(ent, comp);
			}
			wasGameRunning = running;
		}
	}


	private void showJoystick(IGameEntity ent, JoystickComponent comp) {
		if (comp.currentType != comp.newType) {
			changeJoystick(ent, comp);
		} else {

			comp.show();
		}
	}

	private void changeJoystick(IGameEntity ent, JoystickComponent comp) {
		comp.removeJoysticks();
		initJoysticks(ent, comp, comp.newType);
		comp.currentType = comp.newType;
		comp.show();
	}

	private boolean initJoysticks(IGameEntity ent, JoystickComponent comp, int type) {
		if (type == 0) {
			return false;
		}
		PlayerComponent playerComponent = ECS.getPlayerComponent(ent);
		if (playerComponent == null) {
			Log.error("Entity has JoystickComponent and isnt a player, removing component");
			ent.removeComponent(comp);
			return false;
		}

		ActionController controller = playerComponent.getActionController();
		if (controller == null) {
			Log.error("Entity has no controller attached..");
			return false;
		}
		
		//Tonegod desativado (screen não é adicionada)...
		//Bench:
		Bench.start(BenchConsts.EV_JOYSTICK_INIT+"_1_2_2",BenchConsts.CAT_ATOMIC_INIT);
		TonegodUtils tonegod = game.getSnippets().getTonegod();
		Bench.end(BenchConsts.EV_JOYSTICK_INIT+"_1_2_2");
		

		Log.debug("Attaching virtual joysticks...");
		for (IStoreProperties elJoystick : comp.elControllers) {
			initJoystick(elJoystick, tonegod, controller, comp);
		}
		return true;
	}

	private void initJoystick(IStoreProperties elJoystick, TonegodUtils tonegod, ActionController controller, JoystickComponent comp) {
		//Bench: desprezivel
		IGameJoystick control = JMEFactory.createGameJoystick(elJoystick.getProperty(GameProperties.CLASS_PROPERTY), controller, tonegod, elJoystick,game.getInputManager());
		addJoystick(controller, comp, control);
	}

	private void addJoystick(ActionController controller,
			JoystickComponent comp, IGameJoystick control) {
		comp.addJoystick(control);
		controller.addJoystick(control);
	}

	private void hideJoystick(IGameEntity ent, JoystickComponent comp) {
		comp.hide();
	}

}
