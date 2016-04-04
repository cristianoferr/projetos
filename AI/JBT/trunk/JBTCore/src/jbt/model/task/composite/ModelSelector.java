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
import jbt.execution.task.composite.ExecutionSelector;
import jbt.model.core.ModelTask;

/**
 * This class represents a task with one or more children, which are run
 * sequentially.
 * <p>
 * A selector tries to run all its children sequentially. Therefore, there is an
 * active child task. However, when the current active task fails, the selector
 * does not fail, but goes on to the next child task, which is evaluated. A
 * selector succeeds if one of the tasks succeeds, and fails if all the child
 * tasks fail.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSelector extends ModelComposite {
	/**
	 * Constructor.
	 * <p>
	 * Constructs a ModelSelector with some children. A ModelSelector must have
	 * at least one child.
	 * 
	 * @param guard
	 *            the guard of the ModelSelector, which may be null.
	 * @param children
	 *            the list of children. Must have at least one element.
	 */
	public ModelSelector(ModelTask guard, ModelTask... children) {
		super(guard, children);
	}

	/**
	 * Returns an ExecutionSelector that is able to run this ModelSelector.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSelector(this, executor, parent);
	}
}
