package com.cristiano.java.gm.ecs.systems.macro;

import java.util.concurrent.ExecutionException;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.QueryBestiaryComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.UnitStorage;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

/*Unit Test:TestBestiarySystems*/
public class QueryBestiarySystem extends JMEAbstractSystem {

	public QueryBestiarySystem() {
		super(GameComps.COMP_BESTIARY_QUERY);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		QueryBestiaryComponent comp = (QueryBestiaryComponent) component;
		if (comp.firstTick) {
			Log.debug("Bestiary Query arrived... ");
			comp.firstTick = false;

			Bench.start("QueryBestiarySystem-iniciaLibComp",
					BenchConsts.CAT_SYSTEM_UPDATE);
			iniciaBestiaryLibrary();
			Bench.end("QueryBestiarySystem-iniciaLibComp");

			Bench.start("QueryBestiarySystem-recebePedido",
					BenchConsts.CAT_SYSTEM_UPDATE);
			atendePedidoUnidade(comp);
			Bench.end("QueryBestiarySystem-recebePedido");

			// comp.removeAllComponents();// cleaning up any added component...
			//
		} else {
			finishCloneCreation(comp);
			checkArchiving(comp);
		}

	}

	private void checkArchiving(QueryBestiaryComponent comp) {
		if (comp.unitsDelivered > 0) {
			if (comp.selfRemoval) {
				comp.archive();
				comp.deactivate();
			}
		}
	}

	public void atendePedidoUnidade(QueryBestiaryComponent comp) {
		Bench.start("QueryBestiarySystem-atendePedidoUnidade 1",
				BenchConsts.CAT_SYSTEM_UPDATE);
		IGameEntity molde = pegaEntidadeMolde(comp);
		Bench.end("QueryBestiarySystem-atendePedidoUnidade 1");
		if (molde.size() == 0) {
			Log.fatal("Molde has no components!");
		}

		comp.moldeUnit = molde;
		// comp.futureQuery="queryBestiary"+comp.getId();
		// FutureManager.requestFuture(comp.futureQuery, comp.cloneEntity);
		try {
			comp.clonedUnit = (IGameEntity) comp.cloneEntity.call();
		} catch (Exception e) {
			e.printStackTrace();
		}

		comp.firstTick = false;
	}

	private void finishCloneCreation(QueryBestiaryComponent comp) {
		if (comp.clonedUnit == null) {
			try {
				checkFutureCloneCreation(comp);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			if (comp.clonedUnit == null) {
				return;
			}
		}
		Log.debug("Generated clone:" + comp.clonedUnit);

		// #Bench: desprezivel
		deliverClone(comp.clonedUnit, comp, comp.budgetToSpend);
	}

	private void checkFutureCloneCreation(QueryBestiaryComponent comp)
			throws InterruptedException, ExecutionException {
		if (FutureManager.isDone(comp.futureQuery)) {
			comp.clonedUnit = (IGameEntity) FutureManager
					.retrieveFuture(comp.futureQuery);
			comp.futureQuery = null;
		}
	}

	private void deliverClone(IGameEntity clone, QueryBestiaryComponent comp,
			float budget) {
		comp.finalizeDeliveredEntity(clone);
		entMan.addIfNotExistsComponent(GameComps.COMP_TRANSIENT, clone);
		// a unidade vem com os resources zerados... vou distribuir o budget de
		// acordo com a role
		clone.removeComponent(GameComps.COMP_MASTER);
		comp.selectedClass.atualizaUnitRole(clone, budget);
		comp.unitsDelivered = 1;
		clone.activate();
	}

	public IGameEntity pegaEntidadeMolde(QueryBestiaryComponent comp) {

		if (libComp == null) {
			Log.error("libComp is null");
			return null;
		}

		UnitClassComponent unitClass = getUnitClass(comp);
		
		if (unitClass == null) {
			Log.fatal("No unitStorage generated!");
		}

		IGameEntity molde = existeEntityComRoleEBudget(unitClass, comp);
		Log.debug("Generated molde:" + molde + " by " + comp);
		return molde;
	}

	private UnitClassComponent getUnitClass(QueryBestiaryComponent comp) {
		UnitClassComponent unitClass;
		// used when the tag is defined (example: loading from a mesh element)
		if (comp.tagSource != null) {
			Log.debug("Creating a genericRole for:" + comp.tagSource);
			unitClass = libComp.getGenericClass(comp.tagSource, comp.entityType);
		} else {
			unitClass = libComp.getUnitClass(comp.roleIdentifier);
		}
		if (unitClass==null){
			Log.fatal("unitClass is null!! ");
		}
		return unitClass;
		
	}


	public IGameEntity existeEntityComRoleEBudget(UnitClassComponent unitClass,
			QueryBestiaryComponent comp) {
		comp.selectedClass = unitClass;
		IGameEntity entity = unitClass.requestMasterEntity(libComp.chanceNewEntity);
		return entity;
	}

}
