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
package jbt.model.task.decorator;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionUntilFail;
import jbt.model.core.ModelTask;

/**
 * The ModelUntilFail class represents a decorator used to run a task as long as
 * it does not fail.
 * <p>
 * ModelUntilFail just keeps executing its child task as long as it does not
 * fail. When the child task fails, ModelUntilFail returns
 * {@link Status#SUCCESS}. Otherwise it returns {@link Status#RUNNING}.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelUntilFail extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelUntilFail, which may be null.
	 * @param child
	 *            the task that will be run until it fails.
	 */
	public ModelUntilFail(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionUntilFail that knows how to run this ModelUntilFail.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionUntilFail(this, executor, parent);
	}
}
