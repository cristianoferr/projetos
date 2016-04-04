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
import jbt.execution.core.event.TaskEvent;
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelLeaf;

/**
 * Base class for all the ExecutionTask classes that are able to run leaf tasks,
 * that is, classes that inherit from ModelLeaf.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ExecutionLeaf extends ExecutionTask {
	/**
	 * Constructs an ExecutionLeaf to run a specific ModelLeaf.
	 * 
	 * @param modelTask
	 *            the ModelLeaf to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionLeaf.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionLeaf(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelLeaf)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelLeaf.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}

	/**
	 * Does nothing by default, since a leaf task has no children.
	 * 
	 * @see jbt.execution.core.ExecutionTask#statusChanged(jbt.execution.core.event.TaskEvent)
	 */
	public void statusChanged(TaskEvent e) {}
}
