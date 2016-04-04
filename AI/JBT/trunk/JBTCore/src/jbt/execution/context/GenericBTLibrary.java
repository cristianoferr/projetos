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
import java.util.Iterator;
import java.util.Map.Entry;

import jbt.execution.core.IBTLibrary;
import jbt.model.core.ModelTask;
import jbt.util.Pair;

/**
 * Simple implementation of the {@link IBTLibrary} interface, which internally
 * uses a Hashtable to map tree names to actual trees. This class also defines
 * methods for adding behaviour trees to the library itself.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class GenericBTLibrary implements IBTLibrary {
	/**
	 * The hashtable that stores all the trees of the library.
	 */
	private Hashtable<String, ModelTask> trees;

	/**
	 * Constructs a GenericBTLibrary containing no trees.
	 */
	public GenericBTLibrary() {
		this.trees = new Hashtable<String, ModelTask>();
	}

	/**
	 * 
	 * @see jbt.execution.core.IBTLibrary#getBT(java.lang.String)
	 */
	public ModelTask getBT(String name) {
		return this.trees.get(name);
	}

	/**
	 * Returns a read-only iterator through the behaviour trees of the library.
	 * While this iterator is being used, the library cannot be modified.
	 * Otherwise, the results are undefined. Note that both trees and their
	 * names can be accessed through this iterator.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Pair<String, ModelTask>> iterator() {
		return new GenericBTLibraryIterator();
	}

	/**
	 * Adds all the behaviour trees in <code>library</code> to the set of
	 * behaviour trees stored in this library. If there is already a tree with
	 * the same name as that of one of the trees in <code>library</code>, it is
	 * overwritten.
	 * 
	 * @param library
	 *            the library containing all the behaviour trees to add to this
	 *            library.
	 * @return true if a previously stored behaviour tree has been overwritten,
	 *         and false otherwise.
	 */
	public boolean addBTLibrary(IBTLibrary library) {
		boolean overwritten = false;

		for (Pair<String, ModelTask> tree : library) {
			if (this.trees.put(tree.getFirst(), tree.getSecond()) != null) {
				overwritten = true;
			}
		}

		return overwritten;
	}

	/**
	 * Adds the behaviour tree <code>tree</code> to the set of behaviour trees
	 * stored in this library. If there is already a tree with the name
	 * <code>name</code>, then it is overwritten by <code>tree</code>.
	 * 
	 * @param name
	 *            the name that will identify the tree <code>tree</code> in the
	 *            library.
	 * @param tree
	 *            the tree to insert.
	 * @return true if there was already a tree with name <code>name</code>, and
	 *         false otherwise.
	 */
	public boolean addBT(String name, ModelTask tree) {
		if (this.trees.put(name, tree) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Implementation of the iterator that GenericBTLibrary uses. This is a
	 * read-only iterator (removal is not supported), and it internally uses an
	 * iterator through the entry set of the hashtable.
	 * 
	 * @author Ricardo Juan Palma Durán
	 * 
	 */
	private class GenericBTLibraryIterator implements Iterator<Pair<String, ModelTask>> {
		private Iterator<Entry<String, ModelTask>> internalIterator;

		public GenericBTLibraryIterator() {
			this.internalIterator = trees.entrySet().iterator();
		}

		public boolean hasNext() {
			return this.internalIterator.hasNext();
		}

		public Pair<String, ModelTask> next() {
			Entry<String, ModelTask> next = this.internalIterator.next();
			return new Pair<String, ModelTask>(next.getKey(), next.getValue());
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"This iterator cannot be used to remove elements");
		}
	}
}
