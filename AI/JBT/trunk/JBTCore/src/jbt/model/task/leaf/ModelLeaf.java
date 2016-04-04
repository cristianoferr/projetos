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
package jbt.model.task.leaf;

import jbt.model.core.ModelTask;

/**
 * Base class for all the tasks that have no children.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ModelLeaf extends ModelTask {
	/**
	 * Constructs a ModelLeaf with a guard.
	 * 
	 * @param guard
	 *            the guard, which may be null.
	 */
	public ModelLeaf(ModelTask guard) {
		super(guard);
	}
}
