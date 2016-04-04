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
import jbt.execution.task.decorator.ExecutionInterrupter;
import jbt.model.core.ModelTask;

/**
 * An ModelInterrupter is a decorator that controls the termination of a child
 * task. An ModelInterrupter simply lets its child task run normally. If the
 * child returns a result, the ModelInterrupter will return it. However, the
 * ModelInterrupter can be asked to terminate the child task and return an
 * specified status when done so.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelInterrupter extends ModelDecorator {
	/**
	 * Constructor.
	 * <p>
	 * Constructs a ModelInterrupter with one child.
	 * 
	 * @param guard
	 *            the guard of the ModelInterrupter, which may be null.
	 * @param child
	 *            the child of the ModelInterrupter.
	 */
	public ModelInterrupter(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionInterrupter that is able to run this
	 * ModelInterrupter.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionInterrupter(this, executor, parent);
	}
}
