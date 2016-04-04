package com.cristiano.java.gm.ecs.systems.unit;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.mechanics.ApplyForceComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.WheelComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.EffectMaker;
import com.cristiano.java.gm.ecs.comps.unit.resources.SpeedComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.ApplyForceSystem;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.ecs.systems.unit.actuators.AbstractActuatorSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IControlBody;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.jme3.rigidBody.CharDefines;
import com.cristiano.jme3.rigidBody.GMRigidBody;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/*
 * Unit Test: TestPhysicsSystem
 * */
public class PhysicsSystem extends JMEAbstractSystem {

	public PhysicsSystem() {
		super(GameComps.COMP_PHYSICS);
	}
	
	@Override
	public void preTick(float tpf){
		super.preTick(tpf);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		PhysicsComponent comp = (PhysicsComponent) component;
		if (component.isFirstTick()) {
			RenderComponent renderC = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
			if (renderC != null) {
				if (renderC.node.getParent() == null) {
					return;
				}

				// for clones...
				if (comp.originalPhysNode != null && comp.physNode == null) {
					try {
						Bench.start("PhysicsSystem - cloneForSpatial",BenchConsts.CAT_SYSTEM_UPDATE);
						comp.physNode = (IRigidBody) comp.originalPhysNode.cloneForSpatial(renderC.node);
						Bench.end("PhysicsSystem - cloneForSpatial");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				Bench.start("PhysicsSystem - decideControleFisico",BenchConsts.CAT_SYSTEM_UPDATE);
				decideControleFisico(ent, comp, renderC);
				Bench.end("PhysicsSystem - decideControleFisico");
			}
		} else {
			Bench.start("PhysicsSystem - registerActions",BenchConsts.CAT_SYSTEM_UPDATE);
			registerActions(ent, comp);
			Bench.end("PhysicsSystem - registerActions");
			comp.archive();
		}

	}

	private void initPosition(IGameEntity ent, PhysicsComponent comp) {
		PositionComponent pos=ECS.getPositionComponent(ent);
		if (pos!=null){
			pos.physics=comp;
		}
		if (comp.relSourcePos != null) {
			comp._finalPosition.set(comp.relSourcePos).addLocal(comp.sourcePosition.getPos());
			 //Log.info("shooting from: "+comp._finalPosition+"  "+comp);
			//game.addDebugBox(addLocal.add(0, 20, 0), "goal");
			comp.physNode.setPhysicsLocation(comp._finalPosition);
		}

	}

	private void registerActions(IGameEntity ent, PhysicsComponent physC) {
		if (physC.actionsRegistered) {
			return;
		}

		List<IGameElement> actionGroups = physC.actionGroups;
		if (actionGroups == null) {
			return;
		}

		if (physC.controlBody == null) {
			Log.warn("Entity " + ent + " has no controlBody attached... aborting.");
			return;
		}

		physC.actionsRegistered = true;

		PlayerComponent playerC = (PlayerComponent) ent.getComponentWithIdentifier(GameComps.COMP_PLAYER);

		String[] actions = enableActions(physC, actionGroups, playerC);

		if (playerC != null) {
			playerC.addPlayerListeners(this.game.getInputManager(), actions);

		}

	}

	private String[] enableActions(PhysicsComponent physC, List<IGameElement> actionGroups, PlayerComponent playerC) {
		String actions = "";
		for (int j = 0; j < actionGroups.size(); j++) {
			IGameElement elActionGroup = actionGroups.get(j);
			List<IGameElement> elActions = elActionGroup.getObjectList(GameProperties.ACTIONS);

			for (int i = 0; i < elActions.size(); i++) {
				IGameElement elAction = elActions.get(i);
				String actionName = elAction.getName();
				physC.controlBody.getActionController().enableAction(actionName, physC.controlBody);
				if (playerC != null) {
					createTriggers(actionName, playerC, elAction);
				}
				actions += "," + actionName;
			}
		}
		actions = actions.substring(1);
		return actions.split(",");
	}

	private void createTriggers(String actionName, PlayerComponent comp, IGameElement elAction) {
		Trigger trigger;
		List<IGameElement> keys = elAction.getObjectList(GameProperties.KEYS);
		for (IGameElement key : keys) {
			int keyCode = key.getPropertyAsInt(GameProperties.VALUE);
			trigger = new KeyTrigger(keyCode);
			game.getInputManager().addMapping(actionName, trigger);
		}
		if (keys.isEmpty()) {
			Log.warn("Action " + elAction.getIdentifier() + " has no keys assigned.");
		}
	}

	private void decideControleFisico(IGameEntity ent, PhysicsComponent comp, RenderComponent renderC) {
		// deixa carregar a mesh primeiro...
		if (comp.physNode == null) {
			if (renderC.firstTick) {
				return;
			}
			if (ent.containsComponent(GameComps.COMP_MESH_LOADER)) {
				return;
			}
			if (renderC.node.getChildren().size() == 0) {
				//
				if (!ent.containsComponent(GameComps.COMP_SPATIAL)) {
					Log.info("Node without children and without spatial... " + ent);
					comp.firstTick = false;
				}
				return;
			}
			criaControleFisico(ent, comp, renderC);
		} else {
			anexaControleFisico(ent, renderC.node, comp.physNode, comp);
			initPosition(ent, comp);
			comp.firstTick = false;
		}
	}

	private void criaControleFisico(IGameEntity ent, PhysicsComponent comp, RenderComponent renderC) {
		IRigidBody body;
		PhysicsSpaceComponent physSpc = getPhysicsSpace();
		CollisionShape modelShape = getModelShape(renderC.node);

		if (comp.isControllable) {
			IControlBody controlBody = initControllableControl(ent, modelShape, comp);
			if (controlBody == null) {
				Log.warn("No controlBody was generated...");
				return;
			}
			CharDefines defines = createCharDefineFor(ent, physSpc);

			controlBody.setAssetManager(game.getAssetManager());
			addEffectMaker(controlBody, ent);
			controlBody.initControl(renderC.node, defines, physSpc.gravity, renderC.isGround);
			checkWheels(ent, controlBody, defines);
			linkViewDirections(ent, controlBody);
			linkVelocityToSpeedComponent(ent, controlBody);
			body = controlBody;
		} else {
			body = createNonControllableControl(modelShape, comp.mass);
		}
		comp.physNode = body;
	}

	private void linkVelocityToSpeedComponent(IGameEntity ent, IControlBody controlBody) {
		SpeedComponent speedC = (SpeedComponent) ent.getComponentWithIdentifier(GameComps.COMP_SPEED);
		speedC.velocity = controlBody.getVelocity();

	}

	private void linkViewDirections(IGameEntity ent, IControlBody controlBody) {
		OrientationComponent orientC = (OrientationComponent) ent.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);

		orientC.rotatedDownDirection = controlBody.getRotatedDownDirection();
		orientC.rotatedUPDirection = controlBody.getRotatedUPDirection();
		orientC.rotatedViewDirection = controlBody.getRotatedViewDirection();
	}

	private void addEffectMaker(IControlBody controlBody, IGameEntity ent) {
		EffectMaker eff = new EffectMaker(0, entMan, getFXLibrary(), game.getAssetManager());
		controlBody.setEffectMaker(eff);
	}

	private void checkWheels(IGameEntity ent, IControlBody body, CharDefines defines) {
		List<IGameComponent> wheels = ent.getComponentsWithIdentifier(GameComps.COMP_ACTUATOR_WHEEL);

		for (int i = 0; i < wheels.size(); i++) {
			WheelComponent wheel = (WheelComponent) wheels.get(i);
			AbstractActuatorSystem.linkSpatial(ent, wheel);
			body.addWheel(wheel.getNode(), wheel.isFrontWheel, wheel.getPosition(), wheel.radius);
		}
		if (wheels.size() > 0) {
			body.createWheels();
		}
	}

	public static CollisionShape getModelShape(Spatial collisionModel) {
		CollisionShape modelShape = CollisionShapeFactory.createDynamicMeshShape(collisionModel);
		return modelShape;
	}

	private IControlBody initControllableControl(IGameEntity ent, CollisionShape modelShape, PhysicsComponent comp) {
		Log.trace("Creating controllable control for entity:" + ent.getId());
		IControlBody body = queryRenderData(ent, modelShape, comp);
		if (body == null) {
			Log.error("Body is null...");
			return null;
		}

		if (ent.containsComponent(GameComps.COMP_PLAYER)) {
			comp.controlBody.setPlayerVehicle();
		}

		PositionComponent posC = ECS.getPositionComponent(ent);
		posC.physics = comp;

		return body;
	}

	private IControlBody queryRenderData(IGameEntity ent, CollisionShape modelShape, PhysicsComponent comp) {
		RenderComponent renderComp = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
		updateBounds(ent, renderComp);
		DimensionComponent dimComp = (DimensionComponent) ent.getComponentWithIdentifier(GameComps.COMP_DIMENSION);
		if (dimComp == null) {
			Log.warn("Dimension is null..");
			return null;
		}
		IControlBody body = (IControlBody) CRJavaUtils.instanciaClasse(renderComp.controlInit, (CollisionShape) modelShape, comp.mass);
		int x;
		if (renderComp.controlInit == null) {
			Log.error("renderComp.controlInit is null...");
			return null;
		}
		if (body == null) {
			Log.fatal("No body defined for entity...");
		}

		comp.controlBody = (IControlBody) body;
		body.setDimensions(dimComp.dimension);
		return body;
	}

	public static IRigidBody createNonControllableControl(CollisionShape modelShape, float mass) {
		IRigidBody body;
		body = new GMRigidBody(modelShape, mass);
		return body;
	}

	private CharDefines createCharDefineFor(IGameEntity ent, PhysicsSpaceComponent physSpc) {
		SpeedComponent speed =ECS.getSpeedComponent(ent);
		if (speed == null) {
			Log.fatal("Entity has no speed component:" + ent);
			return null;
		}
		int x;
		CharDefines defines = speed.createCharDefines();
		defines.gravityMult = physSpc.gravity.length() / GameConsts.DEFAULT_GRAVITY;
		if (defines.gravityMult < 1) {
			defines.gravityMult = 1;
		}
		defines.owner = ent;

		return defines;
	}

	private void anexaControleFisico(IGameEntity ent, Node node, IRigidBody physNode, PhysicsComponent comp) {
		IGameEntity gameEntity = game.getGameEntity();
		PhysicsSpaceComponent physpC = (PhysicsSpaceComponent) gameEntity.getComponentWithIdentifier(GameComps.COMP_PHYSICS_SPACE);
		if (physpC == null) {
			Log.error("No PhysicsSpace defined.");
			return;
		}
		node.addControl(physNode);
		physpC.addControl(physNode);

		checkApplyForce(ent, physNode, comp);

	}

	private void checkApplyForce(IGameEntity ent, IRigidBody physNode, PhysicsComponent comp) {
		ApplyForceComponent force = (ApplyForceComponent) ent.getComponentWithIdentifier(GameComps.COMP_APPLY_FORCE);
		if (force == null) {
			return;
		}
		ApplyForceSystem.applyForce(force, comp);
		ent.removeComponent(force);
	}

}
