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
package jbt.model.task.leaf;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.leaf.ExecutionSuccess;
import jbt.model.core.ModelTask;

/**
 * A ModelSuccess represents a task that always succeeds.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSuccess extends ModelLeaf {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelSuccess, which may be null.
	 */
	public ModelSuccess(ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns an {@link ExecutionSuccess} that knows how to run this
	 * ModelSuccess.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      jbt.execution.core.ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSuccess(this, executor, parent);
	}
}
