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
import jbt.execution.core.ExecutionTask.Status;
import jbt.execution.task.decorator.ExecutionRepeat;
import jbt.model.core.ModelTask;

/**
 * ModelRepeat represents a decorator that runs its child task forever. When its
 * child task finishes, it runs it once more. This decorator always return
 * {@link Status#RUNNING}.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelRepeat extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelRepeat, which may be null.
	 * @param child
	 *            the child that will be run forever.
	 */
	public ModelRepeat(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionForever that knows how to run this ModelRepeat.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionRepeat(this, executor, parent);
	}
}
