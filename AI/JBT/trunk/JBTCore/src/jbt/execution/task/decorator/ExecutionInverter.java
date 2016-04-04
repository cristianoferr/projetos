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
package jbt.execution.task.decorator;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.core.ITaskState;
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.decorator.ModelInverter;

/**
 * ExecutionInverter is the ExecutionTask that knows how to run a ModelInverter.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionInverter extends ExecutionDecorator {
	/** The child. */
	private ExecutionTask child;

	/**
	 * Creates an ExecutionInverter that is able to run a ModelInverter task and
	 * that is managed by a BTExecutor.
	 * 
	 * @param modelTask
	 *            the ModelInverter that this ExecutionInverter is going to run.
	 * @param executor
	 *            the BTExecutor in charge of running this ExecutionInverter.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionInverter(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelInverter)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelInverter.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Spawns the only child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		/* Just spawn the only child. */
		this.child = ((ModelInverter) this.getModelTask()).getChild().createExecutor(
				this.getExecutor(), this);
		this.child.addTaskListener(this);
		this.child.spawn(this.getContext());
	}

	/**
	 * Terminates the only child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		/* Just terminates the only child. */
		this.child.terminate();
	}

	/**
	 * Checks if the only child has already finished. If so, it inverts its
	 * status code. Otherwise, it returns {@link Status#RUNNING}.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		/* Just inverts the status code. */
		Status childStatus = this.child.getStatus();
		if (childStatus == Status.RUNNING) {
			return Status.RUNNING;
		}
		else if (childStatus == Status.FAILURE || childStatus == Status.TERMINATED) {
			return Status.SUCCESS;
		}
		else {
			return Status.FAILURE;
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Just calls {@link #tick()} so that the ExecutionInverter can evolve
	 * according to the termination of its child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#statusChanged(jbt.execution.core.event.TaskEvent)
	 */
	public void statusChanged(TaskEvent e) {
		this.tick();
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
