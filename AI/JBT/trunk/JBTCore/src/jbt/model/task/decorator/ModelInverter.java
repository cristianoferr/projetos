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
import jbt.execution.task.decorator.ExecutionInverter;
import jbt.model.core.ModelTask;

/**
 * ModelInverter is a decorator used to invert the status code returned by its
 * child.
 * <p>
 * When the decorated task finishes, its status code gets inverted according to:
 * 
 * <ul>
 * <li><code>Status.SUCCESS</code> -> <code>Status.FAILURE</code>.
 * <li><code>Status.FAILURE</code> -> <code>Status.SUCCESS</code>.
 * <li><code>Status.TERMINATED</code> -> <code>Status.SUCCESS</code>.
 * </ul>
 * 
 * If the child task has not finished yet, the ModelInverter returns
 * <code>Status.RUNNING</code> (that is, <code>Status.RUNNING</code> is not
 * inverted).
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelInverter extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelInverter, which may be null.
	 * @param child
	 *            the child task to invert.
	 */
	public ModelInverter(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionInverter that is able to run this ModelInverter.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionInverter(this, executor, parent);
	}
}
