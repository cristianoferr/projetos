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
import jbt.execution.core.ITaskState;
import jbt.execution.core.BTExecutor.BTExecutorList;
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelFailure;

/**
 * An ExecutionFailure is the ExecutionTask that knows how to run a
 * ModelFailure.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionFailure extends ExecutionLeaf {
	/**
	 * Constructs an ExecutionFailure that knows how to run a ModelFailure.
	 * 
	 * @param modelTask
	 *            the ModelFailure to run.
	 * @param executor
	 *            the BTExecutor managing this ExecutionFailure.
	 * @param parent
	 *            the parent ExecutionTask.
	 */
	public ExecutionFailure(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelFailure)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelFailure.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(BTExecutorList.TICKABLE, this);
	}

	/**
	 * Returns {@link Status#FAILURE}.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		return Status.FAILURE;
	}

	/**
	 * Returns null.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeState()
	 */
	protected ITaskState storeState() {
		return null;
	}

	/**
	 * Returns null.
	 * 
	 * @see jbt.execution.core.ExecutionTask#storeTerminationState()
	 */
	protected ITaskState storeTerminationState() {
		return null;
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(jbt.execution.core.ITaskState)
	 */
	protected void restoreState(ITaskState state) {
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
	}
}
