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
import jbt.model.task.decorator.ModelDecorator;
import jbt.model.task.decorator.ModelUntilFail;

/**
 * ExecutionUntilFail is the ExecutionTask that knows how to run a
 * ModelUntilFail.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionUntilFail extends ExecutionDecorator {
	/** The task that is being decorated. */
	private ExecutionTask child;

	/**
	 * Constructs and ExecutionUntilFail that knows how to run a ModelUntilFail.
	 * 
	 * @param modelTask
	 *            the ModelUntilFail that this ExecutionUntilFail will run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionUntilFail.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionUntilFail(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelUntilFail)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelUntilFail.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Spawns the child task.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.child = ((ModelDecorator) this.getModelTask()).getChild().createExecutor(
				this.getExecutor(), this);
		this.child.addTaskListener(this);
		this.child.spawn(this.getContext());
	}

	/**
	 * Just terminates the child of this task.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		this.child.terminate();
	}

	/**
	 * If the child has finished in failure or been terminated, return
	 * {@link Status#SUCCESS}. Otherwise, {@link Status#RUNNING} is returned. If
	 * the child has finished successfully, it is spawned again.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		Status childStatus = this.child.getStatus();

		/*
		 * If the child has finished in failure or been terminated, return
		 * success.
		 */
		if (childStatus == Status.FAILURE || childStatus == Status.TERMINATED) {
			return Status.SUCCESS;
		}
		else {
			/* If the child has finished successfully, spawn it again. */
			if (childStatus == Status.SUCCESS) {
				this.child = ((ModelDecorator) this.getModelTask()).getChild().createExecutor(
						this.getExecutor(), this);
				this.child.addTaskListener(this);
				this.child.spawn(this.getContext());
			}

			/*
			 * In case the child has not finished in failure, return
			 * Status.RUNNING.
			 */
			return Status.RUNNING;
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Just calls {@link #tick()} to make the task evolve.
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
