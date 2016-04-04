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
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelWait;

/**
 * ExecutionWait is the ExecutionTask that knows how to run a ModelWait task.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionWait extends ExecutionLeaf {
	/** Duration of the wait task. */
	private long duration;
	/**
	 * Starting time, measured in nanoseconds. Note that this value is obtained
	 * by {@link System#nanoTime()}, so it is not related to any notion of
	 * system or wall-clock time. Therefore, it can only be used to measure time
	 * intervals.
	 */
	private long startTime;

	/**
	 * Creates an ExecutionWait that is able to run a ModelWait task and that is
	 * managed by a BTExecutor.
	 * 
	 * @param modelTask
	 *            the ModelWait that this ExecutionWait is going to run.
	 * @param executor
	 *            the BTExecutor in charge of running this ExecutionWait.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionWait(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelWait)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelWait.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}

		this.duration = ((ModelWait) modelTask).getDuration();
	}

	/**
	 * Starts measuring the time interval.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.getExecutor().requestInsertionIntoList(BTExecutorList.TICKABLE, this);
		this.startTime = System.nanoTime();
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {}

	/**
	 * Returns Status.SUCCESS or Status.RUNNING depending on whether the task
	 * has waited long enough or not.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		long estimatedTime = System.nanoTime() - this.startTime;

		if ((estimatedTime / 1000000.0) >= this.duration) {
			return Status.SUCCESS;
		}
		else {
			return Status.RUNNING;
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {

	}

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
