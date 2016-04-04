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
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelSubtreeLookup;

/**
 * ExecutionSubtreeLookup is the ExecutionTask that knows how to run a
 * ModelExecutionSubtreeLookup.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ExecutionSubtreeLookup extends ExecutionLeaf {
	/**
	 * Behaviour tree that is retrieved from the context and that is going to be
	 * run.
	 */
	private ModelTask treeToRun;
	/**
	 * The tree that is actually being run (constructed from {@link #treeToRun}
	 * ).
	 */
	private ExecutionTask executionTree;
	/** Flag that tells if the tree could be retrieved from the context. */
	private boolean treeRetrieved;

	/**
	 * Constructs an ExecutionSubtreeLookup that knows how to run a
	 * ModelSubtreeLookup.
	 * 
	 * @param modelTask
	 *            the ModelSubtreeLookup to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionSubtreeLookup.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionSubtreeLookup(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelSubtreeLookup)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelSubtreeLookup.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * This method first retrieve from the context the tree (ModelTask) that is
	 * going to be emulated by this task. Then, it creates its corresponding
	 * executor and finally spawns it. If the tree cannot be found in the
	 * context, does nothing.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalSpawn()
	 */
	protected void internalSpawn() {
		/* Retrieve the tree to run from the context. */
		this.treeToRun = this.getContext().getBT(
				((ModelSubtreeLookup) this.getModelTask()).getTreeName());

		if (this.treeToRun == null) {
			this.treeRetrieved = false;
			/*
			 * Must request to be inserted into the list of tickable nodes,
			 * since no tree has been retrieved and as a result it must be the
			 * task the one continuin the work.
			 */
			this.getExecutor().requestInsertionIntoList(BTExecutorList.TICKABLE, this);
			System.err.println("Could not retrieve tree "
					+ ((ModelSubtreeLookup) this.getModelTask()).getTreeName()
					+ " from the context. Check if the context has been properly initialized.");
		}
		else {
			this.treeRetrieved = true;
			/* Compute positions for the retrieved tree. */
			this.treeToRun.computePositions();

			this.executionTree = this.treeToRun.createExecutor(this.getExecutor(), this);
			this.executionTree.addTaskListener(this);
			this.executionTree.spawn(this.getContext());
		}
	}

	/**
	 * Just terminates the tree that it is emulating, or does nothing if the
	 * tree could not be retrieved.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTerminate()
	 */
	protected void internalTerminate() {
		if (this.treeRetrieved) {
			this.executionTree.terminate();
		}
	}

	/**
	 * Returns the status of the tree it is running, or null if the tree could
	 * not be retrieved.
	 * 
	 * @see jbt.execution.core.ExecutionTask#internalTick()
	 */
	protected Status internalTick() {
		if (this.treeRetrieved) {
			return this.executionTree.getStatus();
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
	 * Just calls {@link #tick()} to make the task evolve.
	 * 
	 * @see jbt.execution.task.leaf.ExecutionLeaf#statusChanged(jbt.execution.core.event.TaskEvent)
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
