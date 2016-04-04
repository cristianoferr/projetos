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

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionLimit;
import jbt.model.core.ModelTask;

/**
 * Limit is a decorator that limits the number of times a task can be executed.
 * This decorator is used when a task (the child of the decorator) must be run a
 * maximum number of times. When the maximum number of times is exceeded, the
 * decorator will fail forever on.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelLimit extends ModelDecorator {
	/** Maximum number of times that the decorated task can be run. */
	private int maxNumTimes;

	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelLimit, which may be null.
	 * @param maxNumTimes
	 *            the maximum number of times that <code>child</code> will be
	 *            run.
	 * @param child
	 *            the child of this task.
	 */
	public ModelLimit(ModelTask guard, int maxNumTimes, ModelTask child) {
		super(guard, child);
		this.maxNumTimes = maxNumTimes;
	}

	/**
	 * Returns an ExecutionLimit that knows how to run this ModelLimit.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionLimit(this, executor, parent);
	}

	/**
	 * Returns the maximum number of times that the decorated task can be run.
	 * 
	 * @return the maximum number of times that the decorated task can be run.
	 */
	public int getMaxNumTimes() {
		return this.maxNumTimes;
	}
}
