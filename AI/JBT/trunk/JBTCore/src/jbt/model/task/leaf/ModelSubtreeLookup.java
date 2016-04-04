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
import jbt.execution.task.leaf.ExecutionSubtreeLookup;
import jbt.model.core.ModelTask;

/**
 * A ModelSubtreeLookup is a leaf node that emulates the behaviour of another
 * behaviour tree.
 * <p>
 * One of the key features of behaviour trees is that they can be reused in many
 * places. This reusability is implemented through the ModelSubreeLookup task.
 * When a tree <i>A</i> must be reused within another tree <i>B</i>, this task
 * is used to retrieve <i>A</i> and use it within <i>B</i>. Trees are indexed by
 * names, so this task needs the name of the tree that it will emulate.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSubtreeLookup extends ModelLeaf {
	/** The name of the tree that this task is going to emulate. */
	private String treeName;

	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the task, which may be null.
	 * @param treeName
	 *            the name of the tree that this task is going to emulate.
	 */
	public ModelSubtreeLookup(ModelTask guard, String treeName) {
		super(guard);
		this.treeName = treeName;
	}

	/**
	 * Returns an ExecutionSubtreeLookup that is able to run this
	 * ModelSubtreeLookup.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSubtreeLookup(this, executor, parent);
	}

	/**
	 * Returns the name of the tree that this task is going to emulate.
	 * 
	 * @return the name of the tree that this task is going to emulate.
	 */
	public String getTreeName() {
		return this.treeName;
	}
}
