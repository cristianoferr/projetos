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

import java.util.List;

import jbt.execution.context.SafeOutputContext;
import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionSafeOutputContextManager;
import jbt.model.core.ModelTask;

/**
 * A ModelSafeOutputContextManager is a decorator that creates a new context for
 * its child task. The context that it creates is a {@link SafeOutputContext},
 * and the input context that the SafeOutputContext receives is that of the
 * ModelSafeOutputContextManager.
 * <p>
 * The spawning and updating of the child task are carried out as usual.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSafeOutputContextManager extends ModelDecorator {
	/**
	 * The list of output variables of the SafeOutputContext.
	 */
	private List<String> outputVariables;

	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelSafeOutputContextManager, which may be
	 *            null.
	 * @param child
	 *            the child of the ModelSafeOutputContextManager.
	 * @param outputVariables
	 *            the list of output variables of the SafeOutputContext that is
	 *            created.
	 */
	public ModelSafeOutputContextManager(ModelTask guard, List<String> outputVariables,
			ModelTask child) {
		super(guard, child);
		this.outputVariables = outputVariables;
	}

	/**
	 * Returns an ExecutionSafeOutputContextManager that knows how to run this
	 * ModelSafeOutputContextManager.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSafeOutputContextManager(this, executor, parent);
	}

	/**
	 * Returns a list with the set of output variables of the SafeOutputContext.
	 * The list cannot be modified.
	 * 
	 * @return a list with the set of output variables of the SafeOutputContext.
	 */
	public List<String> getOutputVariables() {
		return this.outputVariables;
	}
}
