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
import jbt.execution.task.composite.ExecutionStaticPriorityList;
import jbt.model.core.ModelTask;

/**
 * This class represents a task with one or more children, only one being
 * evaluated.
 * <p>
 * A ModelStaticPriorityList has a current active child, which is the task that
 * is being evaluated. The very first time the ModelStaticPriorityList is
 * spawned, the active child is set to the left most task whose guard is
 * evaluated to true. From then on, that child will run as normal, and the
 * ModelStaticPriorityList will finish as soon as its child finishes.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelStaticPriorityList extends ModelComposite {
	/**
	 * Creates a ModelStaticPriorityList task with a guard, and a list of
	 * children to run. A ModelStaticPriorityList must have at least one child.
	 * 
	 * @param guard
	 *            the guard, which may be null.
	 * @param children
	 *            the list of children. Must have at least one element.
	 */
	public ModelStaticPriorityList(ModelTask guard, ModelTask... children) {
		super(guard, children);
	}

	/**
	 * Returns an ExecutionStaticPriorityList that is able to run this
	 * ModelStaticPriorityList.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionStaticPriorityList(this, executor, parent);
	}
}
