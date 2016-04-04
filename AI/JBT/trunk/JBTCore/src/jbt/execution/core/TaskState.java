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

import java.util.Hashtable;
import java.util.Map;

/**
 * Default implementation of the {@link ITaskState} interface. It provides
 * methods for modifying the set of variables stored by the TaskState.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class TaskState implements ITaskState {
	/** The set of variables. */
	private Map<String, Object> variables;

	/**
	 * Constructs an empty TaskState.
	 */
	public TaskState() {
		this.variables = new Hashtable<String, Object>();
	}

	/**
	 * 
	 * @see jbt.execution.core.ITaskState#getStateVariable(java.lang.String)
	 */
	public Object getStateVariable(String name) {
		return this.variables.get(name);
	}

	/**
	 * Sets the value of a variable. If the value is null, the variable is
	 * cleared.
	 * 
	 * @param name
	 *            the name of the variable.
	 * @param value
	 *            the value of the variable.
	 * @return true if there was a variable with name <code>name</code> before
	 *         calling this method (it is therefore been overwritten), and false
	 *         otherwise.
	 */
	public boolean setStateVariable(String name, Object value) {
		if (value == null) {
			return this.variables.remove(name) == null ? false : true;
		}
		return this.variables.put(name, value) == null ? false : true;
	}

	/**
	 * Clears all the variables of the TaskState.
	 */
	public void clear() {
		this.variables.clear();
	}

	/**
	 * Clears the value of a variable.
	 * 
	 * @param name
	 *            the name of the variable.
	 * @return true if the variable existed before calling this method, and
	 *         false otherwise.
	 */
	public boolean clearStateVariable(String name) {
		return this.variables.remove(name) == null ? false : true;
	}

}
