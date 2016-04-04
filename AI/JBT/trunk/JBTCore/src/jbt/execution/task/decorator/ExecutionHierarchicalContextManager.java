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

import jbt.execution.context.HierarchicalContext;
import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.core.ITaskState;
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.decorator.ModelHierarchicalContextManager;
import jbt.model.task.decorator.ModelDecorator;

/**
 * ExecutionHierarchicalContextManager is the ExecutionTask that knows how to run a
 * ModelHierarchicalContextManager.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionHierarchicalContextManager extends ExecutionDecorator {
	/** The child task. */
	private ExecutionTask child;

	/**
	 * Constructs an ExecutionHierarchicalContextManager that knows how to run a
	 * ModelHierarchicalContextManager.
	 * 
	 * @param modelTask
	 *            the ModelHierarchicalContextManager to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionHierarchicalContextManager.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionHierarchicalContextManager(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelHierarchicalContextManager)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelHierarchicalContextManager.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Spawns the child task. This method creates a new HierarchicalContext,
	 * sets its parent to the context of the ExecutionHierarchicalContextManager, and spawns
	 * the child task using this HierarchicalContext.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		HierarchicalContext newContext = new HierarchicalContext();
		newContext.setParent(this.getContext());
		this.child = ((ModelDecorator) this.getModelTask()).getChild().createExecutor(
				this.getExecutor(), this);
		this.child.addTaskListener(this);
		this.child.spawn(newContext);
	}

	/**
	 * Just terminates the child task.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		this.child.terminate();
	}

	/**
	 * Returns the current status of the child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		return this.child.getStatus();
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Just calls {@link #tick()} to make the tass evolve.
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
