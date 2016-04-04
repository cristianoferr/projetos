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
import jbt.execution.task.composite.ExecutionSequence;
import jbt.model.core.ModelTask;

/**
 * A ModeSequence is a task with one or more children which are evaluated
 * sequentially.
 * <p>
 * A ModeSequence has an active child, which is the child task currently being
 * evaluated. If the execution of the current child finishes successfully, the
 * next child of the sequence is spawned and evaluated. However, if the
 * execution of the currently active child ends in failure, the whole
 * ModeSequence also fails.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSequence extends ModelComposite {
	/**
	 * Constructor.
	 * <p>
	 * Constructs a ModeSequence with some children. A ModeSequence must have at
	 * least one child.
	 * 
	 * @param guard
	 *            the guard of the ModeSequence, which may be null.
	 * @param children
	 *            the list of children. Must have at least one element.
	 */
	public ModelSequence(ModelTask guard, ModelTask... children) {
		super(guard, children);
	}

	/**
	 * Returns an ExecutionSequence that can run this ModelSequence.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSequence(this, executor, parent);
	}
}
