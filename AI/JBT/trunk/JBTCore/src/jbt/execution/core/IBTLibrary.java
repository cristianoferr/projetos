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
import jbt.util.Pair;

/**
 * Common interface for all behaviour tree libraries. A behaviour tree library
 * is just a repository from which behaviour trees can be retrieved by name.
 * <p>
 * This is an <i>iterable</i> interface (it extends {@link Iterable}) so that
 * all the behaviour trees of the library can be easily accessed.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public interface IBTLibrary extends Iterable<Pair<String, ModelTask>> {
	/**
	 * Returns the behaviour tree whose name is <code>name</code>. This method
	 * returns the root task of the tree.
	 * 
	 * @param name
	 *            the name of the tree to retrieve.
	 * @return the behaviour tree whose name is <code>name</code>, or null in
	 *         case it does not exist.
	 */
	public ModelTask getBT(String name);
}
