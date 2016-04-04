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
import jbt.execution.task.leaf.ExecutionWait;
import jbt.model.core.ModelTask;

/**
 * A ModelWait task is a task that keeps running for a period of time, and then
 * succeeds. The user can specify for how long the ModelWait task should be
 * running. For that period of time, the task will be evaluated to
 * Status.RUNNING. Then, the task will return Status.SUCCESS.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelWait extends ModelLeaf {
	/**
	 * Duration, measured in milliseconds, of the period of time the task will
	 * be running.
	 */
	private long duration;

	/**
	 * Constructor. Constructs a ModelWait task that will keep running for
	 * <code>duration</code> milliseconds.
	 * 
	 * @param guard
	 *            the guard of the ModelWait task, which may be null.
	 * @param duration
	 *            the ModelWait of the Wait task, in milliseconds.
	 */
	public ModelWait(ModelTask guard, long duration) {
		super(guard);
		this.duration = duration;
	}

	/**
	 * Returns an ExecutionWait that can run this ModelWait.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionWait(this, executor, parent);
	}

	/**
	 * Returns the duration of this ModelWait task.
	 * 
	 * @return the duration of this ModelWait task.
	 */
	public long getDuration() {
		return this.duration;
	}
}
