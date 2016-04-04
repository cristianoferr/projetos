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
import jbt.execution.task.leaf.ExecutionVariableRenamer;
import jbt.model.core.ModelTask;

/**
 * A ModelVariableRenamer is a task that renames a variable of the context. This
 * task just takes one variable of the context and changes its name.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelVariableRenamer extends ModelLeaf {
	/** The name of the variable that must be renamed. */
	private String variableName;
	/** The new name for the variable that must be renamed. */
	private String newVariableName;

	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the task, which may be null.
	 * @param variableName
	 *            the name of the variable to rename.
	 * @param newVariableName
	 *            the new name for the variable.
	 */
	public ModelVariableRenamer(ModelTask guard, String variableName, String newVariableName) {
		super(guard);
		this.variableName = variableName;
		this.newVariableName = newVariableName;
	}

	/**
	 * Returns a new {@link ExecutionVariableRenamer} that knows how to run this
	 * ModelVariableRenamer.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      jbt.execution.core.ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionVariableRenamer(this, executor, parent);
	}

	/**
	 * Returns the name of the variable to rename.
	 * 
	 * @return the name of the variable to rename.
	 */
	public String getVariableName() {
		return this.variableName;
	}

	/**
	 * Returns the new name for the variable that must be renamed.
	 * 
	 * @return the new name for the variable that must be renamed.
	 */
	public String getNewVariableName() {
		return this.newVariableName;
	}
}
