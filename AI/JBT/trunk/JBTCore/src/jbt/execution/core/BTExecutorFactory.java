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
 * The BTExecutorFactory implements the simple factory pattern, and allows
 * clients of the framework to create instances of {@link IBTExecutor} objects
 * that will run specific behaviour trees.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class BTExecutorFactory {
	/**
	 * Creates an IBTExecutor that is able to run a specific behaviour tree. The
	 * input context is also specified.
	 * 
	 * @param treeToRun
	 *            the behaviour tree that the returned IBTExecutor will run,
	 * @param context
	 *            the input context to be used by the behaviour tree.
	 * @return an IBTExecutor to run the tree <code>treeToRun</code>.
	 */
	public static IBTExecutor createBTExecutor(ModelTask treeToRun,
			IContext context) {
		return new BTExecutor(treeToRun, context);
	}

	/**
	 * Creates an IBTExecutor that is able to run a specific behaviour tree. A
	 * new empty context is created for the tree.
	 * 
	 * @param treeToRun
	 *            the behaviour tree that the returned IBTExecutor will run,
	 * @return an IBTExecutor to run the tree <code>treeToRun</code>.
	 */
	public static IBTExecutor createBTExecutor(ModelTask treeToRun) {
		return new BTExecutor(treeToRun);
	}
}
