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

import jbt.execution.core.ExecutionTask;

/**
 * Interface for an entity that is able to receive events from tasks (
 * {@link ExecutionTask}) whose status has changed in a relevant way.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public interface ITaskListener {
	/**
	 * Method called when an important change in the status of a task has taken
	 * place.
	 * 
	 * @param e
	 *            the TaskEvent with all the information about the change in the
	 *            status of the task.
	 */
	public void statusChanged(TaskEvent e);
}
