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

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.model.core.ModelTask;
import jbt.model.task.composite.ModelComposite;

/**
 * Base class for all the ExecutionTask subclasses that are able to run
 * composite tasks (that is, classes that inherit from ModelComposite).
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ExecutionComposite extends ExecutionTask {
	/**
	 * Creates an ExecutionComposite that is able to run a particular
	 * ModelComposite task.
	 * 
	 * @param modelTask
	 *            the ModelComposite task to run.
	 * @param executor
	 *            the BTExecutor that will manage this ExecutionComposite.
	 * @param parent
	 *            the parent ExecutionTask of this task.
	 */
	public ExecutionComposite(ModelTask modelTask, BTExecutor executor, ExecutionTask parent) {
		super(modelTask, executor, parent);
		if (!(modelTask instanceof ModelComposite)) {
			throw new IllegalArgumentException("The ModelTask must subclass "
					+ ModelComposite.class.getCanonicalName() + " but it inherits from "
					+ modelTask.getClass().getCanonicalName());
		}
	}
}
