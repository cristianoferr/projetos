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
import jbt.execution.task.composite.ExecutionParallel;
import jbt.model.core.ModelTask;

/**
 * ModelParallel is a task that runs all its children simultaneously. A
 * ModelParallel is constantly checking the evolution of its children.
 * <p>
 * The parallel task has a policy that defines the way it behaves. There are to
 * policies for parallel:
 * <ul>
 * <li>{@link ParallelPolicy#SEQUENCE_POLICY}: meaning the parallel behaves like
 * a sequence task, that is, it fails as soon as one of its children fail, and
 * it only succeed if all of its children succeed. Otherwise it is running.
 * <li>{@link ParallelPolicy#SELECTOR_POLICY}: meaning the parallel behaves like
 * a selector task, that is, if succeeds as soon as one of its children succeed,
 * and it only fails of all of its children fail. Otherwise it is running.
 * </ul>
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelParallel extends ModelComposite {
	/** Policy of this ModelParallel task. */
	private ParallelPolicy policy;

	/**
	 * Enum defining the different policies for a parallel task (ModelParallel):
	 * <ul>
	 * <li>{@link ParallelPolicy#SEQUENCE_POLICY}: means the parallel behaves
	 * like a sequence task, that is, it fails as soon as one of its children
	 * fail, and it only succeed if all of its children succeed. Otherwise it is
	 * running.
	 * <li>{@link ParallelPolicy#SELECTOR_POLICY}: means the parallel behaves
	 * like a selector task, that is, if succeeds as soon as one of its children
	 * succeed, and it only fails of all of its children fail. Otherwise it is
	 * running.
	 * </ul>
	 * 
	 * @author Ricardo Juan Palma Durán
	 * 
	 */
	public static enum ParallelPolicy {
		/**
		 * Policy meaning that the parallel behaves like a sequence task, that
		 * is, it fails as soon as one of its children fail, and it only succeed
		 * if all of its children succeed.
		 */
		SEQUENCE_POLICY,
		/**
		 * Policy meaning the parallel behaves like a selector task, that is, if
		 * succeeds as soon as one of its children succeed, and it only fails of
		 * all of its children fail.
		 */
		SELECTOR_POLICY
	}

	/**
	 * Creates a ModelParallel task with a guard, a policy and a list of
	 * children to run. A ModelParallel must have at least one child.
	 * 
	 * @param guard
	 *            the guard, which may be null.
	 * @param policy
	 *            the policy for the ModelParallel.
	 * @param children
	 *            the list of children. Must have at least one element.
	 */
	public ModelParallel(ModelTask guard, ParallelPolicy policy, ModelTask... children) {
		super(guard, children);
		this.policy = policy;
	}

	/**
	 * Returns the policy of this ModelParallel.
	 * 
	 * @return the policy of this ModelParallel.
	 */
	public ParallelPolicy getPolicy() {
		return this.policy;
	}

	/**
	 * Returns an ExecutionParallel that can run this ModelParallel.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionParallel(this, executor, parent);
	}
}
