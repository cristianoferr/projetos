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

import java.util.Iterator;
import java.util.List;

import jbt.execution.context.BasicContext;
import jbt.model.core.ModelTask;

/**
 * The ContextFactory implements the simple factory pattern, and allows clients
 * of the framework to create instances of {@link IContext} objects that can be
 * used when running behaviour trees.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ContextFactory {
	/**
	 * Creates a new empty context (with no variables) that contains all the
	 * behaviour trees specified in <code>library</code>.
	 * 
	 * @param library
	 *            the set of behaviour trees that the returned IContext will
	 *            contain.
	 * @return a new empty context that contains all the behaviour trees
	 *         specified in <code>library</code>.
	 */
	public static IContext createContext(IBTLibrary library) {
		BasicContext result = new BasicContext();
		result.addBTLibrary(library);
		return result;
	}

	/**
	 * Creates a new empty context (with no variables) that contains all the
	 * behaviour trees in the libraries <code>libraries</code>.
	 * 
	 * @param libraries
	 *            the list of libraries whose behaviour trees this context will
	 *            contain.
	 * @return a new empty context that contains all the behaviour trees in the
	 *         libraries <code>libraries</code>.
	 */
	public static IContext createContext(List<IBTLibrary> libraries) {
		BasicContext result = new BasicContext();

		for (IBTLibrary library : libraries) {
			result.addBTLibrary(library);
		}

		return result;
	}

	/**
	 * Creates a new empty context (with no variables in it) that contains all
	 * the behaviour trees in <code>behaviourTrees</code>. The name of the trees
	 * are specified in <code>names</code>, so, for instance, the i-th element
	 * in <code>names</code> represents the name of the i-th tree in
	 * <code>behaviourTrees</code>.
	 * 
	 * @param behaviourTrees
	 *            the list with the trees that the context will contain.
	 * @param names
	 *            the list with the names of the trees.
	 * @return a new empty context that contains all the behaviour trees in the
	 *         list <code>behaviourTrees</code>.
	 */
	public static IContext createContext(List<ModelTask> behaviourTrees, List<String> names) {
		BasicContext result = new BasicContext();

		Iterator<ModelTask> treesIterator = behaviourTrees.iterator();
		Iterator<String> namesIterator = names.iterator();

		while (treesIterator.hasNext()) {
			result.addBT(namesIterator.next(), treesIterator.next());
		}

		return result;
	}

	/**
	 * Creates a new empty context (with no variables in it).
	 * 
	 * @return a new empty context.
	 */
	public static IContext createContext() {
		return new BasicContext();
	}
}
