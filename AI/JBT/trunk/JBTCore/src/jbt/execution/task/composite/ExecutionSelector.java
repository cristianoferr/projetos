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
package jbt.execution.task.composite;

import java.util.List;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.core.ITaskState;
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.composite.ModelSelector;

/**
 * ExecutionSelector is the ExecutionTask that is able to run a ModelSelector
 * task.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionSelector extends ExecutionComposite {
	/** Index of the active child. */
	private int activeChildIndex;
	/** The currently active child. */
	private ExecutionTask activeChild;
	/** List of the ModelTask children of the selector. */
	private List<ModelTask> children;

	/**
	 * Creates an ExecutionSelector that is able to run a ModelSelector task and
	 * that is managed by a BTExecutor.
	 * 
	 * @param modelTask
	 *            the ModelSelector that this ExecutionSelector is going to run.
	 * @param executor
	 *            the BTExecutor in charge of running this ExecutionSelector.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionSelector(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelSelector)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelSelector.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Spawns the first child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		this.activeChildIndex = 0;
		this.children = this.getModelTask().getChildren();
		this.activeChild = this.children.get(this.activeChildIndex).createExecutor(
				this.getExecutor(), this);
		this.activeChild.addTaskListener(this);
		this.activeChild.spawn(this.getContext());
	}

	/**
	 * Terminates the current active child.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		this.activeChild.terminate();
	}

	/**
	 * Checks if the currently active child has finished. It it has not, it
	 * returns {@link Status#RUNNING}. If it has finished successfully, it
	 * returns {@link Status#SUCCESS}. If it has finished in failure, then:
	 * <ul>
	 * <li>If it was the last child of the selector, returns
	 * {@link Status#FAILURE}.
	 * <li>Otherwise, it spawns the next child of the selector and returns
	 * {@link Status#RUNNING}.
	 * </ul>
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		Status childStatus = this.activeChild.getStatus();

		if (childStatus == Status.RUNNING) {
			return Status.RUNNING;
		}
		else if (childStatus == Status.SUCCESS) {
			return Status.SUCCESS;
		}
		else {
			/*
			 * If the current child has failed, and it was the last one, return
			 * failure.
			 */
			if (this.activeChildIndex == this.children.size() - 1) {
				return Status.FAILURE;
			}
			else {
				/*
				 * Otherwise, if it was not the last child, spawn the next
				 * child.
				 */
				this.activeChildIndex++;
				this.activeChild = this.children.get(this.activeChildIndex).createExecutor(
						this.getExecutor(), this);
				this.activeChild.addTaskListener(this);
				this.activeChild.spawn(this.getContext());
				return Status.RUNNING;
			}
		}
	}

	/**
	 * Does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#restoreState(ITaskState)
	 */
	protected void restoreState(ITaskState state) {}

	/**
	 * Just calls {@link #tick()} to make the ExecutionSelector evolve.
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
