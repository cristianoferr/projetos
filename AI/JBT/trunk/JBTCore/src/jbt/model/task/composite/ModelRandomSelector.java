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
package jbt.model.task.composite;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.composite.ExecutionRandomSelector;
import jbt.model.core.ModelTask;

/**
 * A ModelRandomSelector is a task that behaves just like a ModelSelector, but
 * which walk through its children in a random order. Instead of evaluating its
 * children from left to right, this task evaluate them in a random order.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelRandomSelector extends ModelComposite {
	/**
	 * Creates a ModelRandomSelector with a guard and several children. The list
	 * of children cannot be empty.
	 * 
	 * @param guard
	 *            the guard of the ModelRandomSelector, which may be null.
	 * @param children
	 *            the list of children, which cannot be empty.
	 */
	public ModelRandomSelector(ModelTask guard, ModelTask... children) {
		super(guard, children);
	}

	/**
	 * Returns an ExecutionRandomSelector that knows how to run this
	 * ModelRandomSelector.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionRandomSelector(this, executor, parent);
	}
}
