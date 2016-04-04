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

import jbt.model.core.ModelTask;

/**
 * Interface defining the context of a behavior tree task. The context of a task
 * is a set of variables, each one consisting of a name and a value. This
 * interface defines some methods for manipulating such variables.
 * <p>
 * A context also contains a set of behaviour trees that can be accessed by
 * tasks using the context. Reusability is very important for behaviour trees.
 * In general, a behaviour tree will be made of some tasks of its own as well as
 * references to other behaviour trees. When tasks need to retrieve references
 * to other behaviour trees, it will be the context that will provide with them.
 * Thus, the context defines a method for retrieving behaviour trees by name.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public interface IContext {
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
	public Object getVariable(String name);

	/**
	 * Sets the value of a variable. If the variable already existed, its value
	 * is overwritten. <code>value</code> may be null in order to clear the
	 * value of the variable.
	 * 
	 * @param name
	 *            the name of the variable.
	 * @param value
	 *            the value for the variable.
	 * @return true if a variable with the same name already existed, and false
	 *         otherwise.
	 */
	public boolean setVariable(String name, Object value);

	/**
	 * Clears the set of all the variables of the context.
	 */
	public void clear();

	/**
	 * Clears the value of a variable. This is equivalent to calling
	 * {@link #setVariable(String, Object)} with a value of null for the second
	 * argument.
	 * 
	 * @param name
	 *            the name of a variable.
	 * @return true if the variable existed, and false otherwise.
	 */
	public boolean clearVariable(String name);

	/**
	 * Returns the behaviour tree whose name is <code>name</code>, or null in
	 * case it does not exist it the context.
	 * 
	 * @param name
	 *            the name of the tree to retrieve.
	 * @return the root of the behaviour tree whose name is <code>name</code>,
	 *         or null in case it does not exist in the context.
	 */
	public ModelTask getBT(String name);
}
