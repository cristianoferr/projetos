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
package jbt.model.task.decorator;

import jbt.model.core.ModelTask;

/**
 * This class represents a decorator of a task. A decorator is a task with only
 * one child, whose behavior it modifies. A decorator is used in situations in
 * which we want to execute a particular task but in a little different way.
 * <p>
 * Typical examples of decorators are:
 * 
 * <ul>
 * <li>Filters: they decide whether the child task can continue running or not.
 * Some examples of filters are:
 * <ul>
 * <li>Limit filter: which limits the number of times a task can execute.
 * <li>Until fail filter: which repeats a task until it fails.
 * </ul>
 * <li>Inverter: inverts the status code of a task.
 * <li>Semaphore guard: they are associated to resources. If the resource is
 * currently being used by another task, the new task cannot start running, so
 * it fails.
 * </ul>
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public abstract class ModelDecorator extends ModelTask {
	/**
	 * Constructor.
	 * <p>
	 * Constructs a ModelDecorator with one child.
	 * 
	 * @param guard
	 *            the guard of the ModelDecorator. which may be null.
	 * @param child
	 *            the child of the ModelDecorator.
	 */
	public ModelDecorator(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns the child of this decorator.
	 * 
	 * @return the child of this decorator.
	 */
	public ModelTask getChild() {
		return this.getChildren().get(0);
	}
}
