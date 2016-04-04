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
package jbt.execution.core;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jbt.util.Pair;

/**
 * The TaskStateFactory implements the simple factory pattern, and allows
 * clients of the framework to create instances of {@link ITaskState} objects.
 * The methods provided by this factory allows the client to specify the set of
 * variables that the task state will contain.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class TaskStateFactory {
	/**
	 * Creates an ITaskState that contains the set of variables specified by
	 * <code>variables</code>. Each variable is a Pair whose first element is
	 * the variable's name and the second element is its value.
	 * 
	 * @param variables
	 *            the list of variables that the ITaskState will contain.
	 * @return an ITaskState that contains the set of variables in
	 *         <code>variables</code>.
	 */
	public static ITaskState createTaskState(List<Pair<String, Object>> variables) {
		TaskState taskState = new TaskState();

		for (Pair<String, Object> variable : variables) {
			taskState.setStateVariable(variable.getFirst(), variable.getSecond());
		}

		return taskState;
	}

	/**
	 * Creates an ITaskState that contains the set of variables specified by
	 * <code>variables</code>. Variables are stored in a Map whose keys are
	 * variables' names and whose values are the values of the variables.
	 * 
	 * @param variables
	 *            the list of variables that the ITaskState will contain.
	 * @return an ITaskState that contains the set of variables in
	 *         <code>variables</code>.
	 */
	public static ITaskState createTaskState(Map<String, Object> variables) {
		TaskState taskState = new TaskState();

		for (Entry<String, Object> variable : variables.entrySet()) {
			taskState.setStateVariable(variable.getKey(), variable.getValue());
		}

		return taskState;
	}
}
