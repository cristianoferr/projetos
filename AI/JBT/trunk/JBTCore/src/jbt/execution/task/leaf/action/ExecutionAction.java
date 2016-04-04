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
package jbt.execution.task.leaf.action;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.leaf.ExecutionLeaf;
import jbt.model.core.ModelTask;
import jbt.model.task.leaf.action.ModelAction;

/**
 * ExecutionAction is the base class of all of the class that are able to run
 * actions in the game (that is, subclasses of ModelAction).
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ExecutionAction extends ExecutionLeaf {
	/**
	 * Constructs an ExecutionAction that knows how to run a ModelAction.
	 * 
	 * @param modelTask
	 *            the ModelAction to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionAction.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionAction(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelAction)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelAction.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}
}
