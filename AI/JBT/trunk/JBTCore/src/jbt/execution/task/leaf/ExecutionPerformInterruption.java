/*******************************************************************************
 * Copyright (c) 2010 Ricardo Juan Palma Durán.
 * 
 * This source file is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, version 3 of
 * the License. The text of the GNU Lesser General Public License 
 * is included with this application in the file LICENSE.TXT.
 * 
 * This source file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 ******************************************************************************/
package jbt.execution.task.leaf;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.core.BTExecutor.BTExecutorList;
import jbt.execution.core.ITaskState;
import jbt.execution.task.decorator.ExecutionInterrupter;
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelPerformInterruption;

/**
 * ExecutionPerformInterruption is the ExecutionTask that knows how to run a
 * ModelPerformInterrupter.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionPerformInterruption extends ExecutionLeaf {
	/**
	 * Creates an ExecutionPerformInterruption that is able to run a
	 * ModelPerformInterruption task and that is managed by a BTExecutor.
	 * 
	 * @param modelTask
	 *            the ModelPerformInterruption that this
	 *            ExecutionPerformInterruption is going to run.
	 * @param executor
	 *            the BTExecutor in charge of running this
	 *            ExecutionPerformInterruption.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionPerformInterruption(ModelTask modelTask, BTExecutor executor,
			ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelPerformInterruption)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelPerformInterruption.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Calls {@link ExecutionInterrupter#interrupt(Status)} on the
	 * ExecutionInterrupter associated to this ExecutionPerformInterruption.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(BTExecutorList.TICKABLE, this);
		/*
		 * First, retrieve the ExecutionInterrupter that is going to be
		 * interrupted.
		 */
		ExecutionInterrupter interrupter = this.getExecutor().getExecutionInterrupter(
				((ModelPerformInterruption) this.getModelTask()).getInterrupter());

		/* If we could find the ExecutionInterrupter, interrupt it. */
		if (interrupter != null) {
			interrupter.interrupt(((ModelPerformInterruption) this.getModelTask())
					.getDesiredResult());
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {}

	/**
	 * Returns {@link Status#SUCCESS}.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		return Status.SUCCESS;
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeState()
	 */
	protected ITaskState storeState() {
		return null;
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeTerminationState()
	 */
	protected ITaskState storeTerminationState() {
		return null;
	}
}
