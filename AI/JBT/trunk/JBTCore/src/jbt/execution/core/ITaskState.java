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

/**
 * The ITaskState interface represents the persistent state of a task in a
 * behaviour tree. This state is represented as a set of variables with name and
 * value.
 * <p>
 * Some tasks in BTs are persistent in the sense that, after finishing, if they
 * are spawned again, they remember past information. Take for example the
 * "limit" task. A "limit" task allows to run its child node only a certain
 * number of times (for example, 5). After being spawned, it has to remember how
 * many times it has been run so far, so that, once the threshold is exceeded,
 * it fails.
 * <p>
 * This interface represents the common functionality for classes that represent
 * the persistent state of a task. It just defines a method for retrieving the
 * value of a variable of the task's state. They way the task's state is
 * populated is not defined.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public interface ITaskState {
	/**
	 * Returns the value of a variable whose name is <code>name</code>, or null
	 * if it is not found.
	 * 
	 * @param name
	 *            the name of the variable to retrieve.
	 * 
	 * @return the value of a variable whose name is <code>name</code>, or null
	 *         if it does not exist.
	 */
	public Object getStateVariable(String name);
}
