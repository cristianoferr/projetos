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
package jbt.model.task.leaf.condition;

import jbt.model.core.ModelTask;
import jbt.model.task.leaf.ModelLeaf;

/**
 * Class representing an abstract condition to be tested within the game.
 * Conditions are tasks with no children (that is, they are leaves in the
 * behavior tree) and with no connection to any other task in the tree.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ModelCondition extends ModelLeaf {
	/**
	 * Constructs a ModelCondition.
	 * 
	 * @param guard
	 *            the guard of the ModelCondition, which may be null.
	 */
	public ModelCondition(ModelTask guard) {
		super(guard);
	}
}
