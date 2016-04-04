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

import jbt.execution.context.SafeContext;
import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionSafeContextManager;
import jbt.model.core.ModelTask;

/**
 * A ModelSafeContextManager is a decorator that creates a new context for its
 * child task. The context that it creates is a {@link SafeContext}, and the
 * input context that the SafeContext receives is the context of the
 * ModelSafeContextManager.
 * <p>
 * The spawning and updating of the child task are carried out as usual.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSafeContextManager extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelSafeContextManager, which may be null.
	 * @param child
	 *            the child of the ModelSafeContextManager.
	 */
	public ModelSafeContextManager(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionSafeContextManager that knows how to run this
	 * ModelSafeContextManager.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSafeContextManager(this, executor, parent);
	}
}
