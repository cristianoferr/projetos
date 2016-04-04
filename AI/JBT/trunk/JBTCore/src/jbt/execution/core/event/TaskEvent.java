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
package jbt.execution.core.event;

import java.util.EventObject;

import jbt.execution.core.ExecutionTask;
import jbt.execution.core.ExecutionTask.Status;

/**
 * A TaskEvent is an event that is generated by tasks ({@link ExecutionTask}) to
 * signal that a relevant change in the status of a task has taken place.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class TaskEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/** The new status of the task. */
	private Status newStatus;
	/** The previous status of the task. */
	private Status previousStatus;

	/**
	 * Creates a TaskEvent with a particular ExcutionTask as source of the
	 * event. The source (<code>source</code>) is the task whose status has
	 * changed, and <code>newStatus</code> is the new status of the task.
	 * 
	 * @param source
	 *            the task whose status has changed.
	 * @param newStatus
	 *            the new status of the task.
	 */
	public TaskEvent(ExecutionTask source, Status newStatus, Status previousStatus) {
		super(source);
		this.newStatus = newStatus;
		this.previousStatus = previousStatus;
	}

	/**
	 * Returns the new status associated to the task.
	 * 
	 * @return the new status associated to the task.
	 */
	public Status getNewStatus() {
		return this.newStatus;
	}

	/**
	 * Returns the previous status associated to the task.
	 * 
	 * @return the previous status associated to the task.
	 */
	public Status getPreviousStatus() {
		return this.previousStatus;
	}
}
