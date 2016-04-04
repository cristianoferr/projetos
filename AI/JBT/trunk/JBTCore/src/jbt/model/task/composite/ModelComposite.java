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
package jbt.model.task.composite;

import jbt.model.core.ModelTask;

/**
 * A ModelComposite task is a task with several children, whose evaluation
 * depends on the evaluation of its children.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ModelComposite extends ModelTask {
	/**
	 * Constructor.
	 * <p>
	 * Constructs a ModelComposite with some children. A ModelComposite must
	 * have at least one child.
	 * 
	 * @param guard
	 *            the guard of the ModelComposite.
	 * @param children
	 *            the list of children. Must have at least one element.
	 */
	public ModelComposite(ModelTask guard, ModelTask... children) {
		super(guard, children);
		if (children.length == 0) {
			throw new IllegalArgumentException("The list of children cannot be empty");
		}
	}
}
