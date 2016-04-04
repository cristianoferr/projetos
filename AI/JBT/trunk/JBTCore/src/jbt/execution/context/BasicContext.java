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
package jbt.execution.context;

import java.util.Hashtable;
import java.util.Map;

import jbt.execution.core.IBTLibrary;
import jbt.execution.core.IContext;
import jbt.model.core.ModelTask;

/**
 * Basic implementation of the IContext interface. This class uses a Hashtable
 * to store the set of variables.
 * <p>
 * Also, since a context must contain a set of behaviour trees, this class
 * defines some methods to add behaviour trees to the context.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class BasicContext implements IContext {
	/**
	 * The set of variables that the context consists of.
	 */
	private Map<String, Object> variables;
	/**
	 * The BT library that is internally used to manage all the trees of the
	 * context.
	 */
	private GenericBTLibrary library;

	/**
	 * Default constructor. Constructs an empty BasicContext.
	 */
	public BasicContext() {
		this.variables = new Hashtable<String, Object>();
		this.library = new GenericBTLibrary();
	}

	/**
	 * 
	 * @see es.ucm.bt.core.IContext#getVariable(java.lang.String)
	 */
	public Object getVariable(String name) {
		return this.variables.get(name);
	}

	/**
	 * 
	 * @see es.ucm.bt.core.IContext#setVariable(java.lang.String,
	 *      java.lang.Object)
	 */
	public boolean setVariable(String name, Object value) {
		if (value == null) {
			return this.variables.remove(name) == null ? false : true;
		}
		return this.variables.put(name, value) == null ? false : true;
	}

	/**
	 * 
	 * @see es.ucm.bt.core.IContext#clear()
	 */
	public void clear() {
		this.variables.clear();
	}

	/**
	 * 
	 * @see jbt.execution.core.IContext#clearVariable(java.lang.String)
	 */
	public boolean clearVariable(String name) {
		return this.variables.remove(name) == null ? false : true;
	}

	/**
	 * Adds all the behaviour trees in <code>library</code> to the set of
	 * behaviour trees stored in the context. If there is already a tree with
	 * the same name as that of one of the trees in <code>library</code>, it is
	 * overwritten.
	 * 
	 * @param library
	 *            the library containing all the behaviour trees to add to this
	 *            context.
	 * @return true if a previously stored behaviour tree has been overwritten,
	 *         and false otherwise.
	 */
	public boolean addBTLibrary(IBTLibrary library) {
		return this.library.addBTLibrary(library);
	}

	/**
	 * Adds the behaviour tree <code>tree</code> to the set of behaviour trees
	 * stored in the context. If there is already a tree with the name
	 * <code>name</code>, then it is overwritten by <code>tree</code>.
	 * 
	 * @param name
	 *            the name that will identify the tree <code>tree</code> in the
	 *            context.
	 * @param tree
	 *            the tree to insert.
	 * @return true if there was already a tree with name <code>name</code>, and
	 *         false otherwise.
	 */
	public boolean addBT(String name, ModelTask tree) {
		return this.library.addBT(name, tree);
	}

	/**
	 * 
	 * @see jbt.execution.core.IContext#getBT(java.lang.String)
	 */
	public ModelTask getBT(String name) {
		return this.library.getBT(name);
	}
}
