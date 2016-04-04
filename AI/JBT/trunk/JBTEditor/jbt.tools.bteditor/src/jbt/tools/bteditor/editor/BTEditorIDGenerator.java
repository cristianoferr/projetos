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
package jbt.tools.bteditor.editor;

/**
 * A singleton class that generates IDs for BTEditor objects. Every new BTEditor
 * has an unique ID which is represented as a long value.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class BTEditorIDGenerator {
	/** The only instance of this class. */
	private static BTEditorIDGenerator instance;
	/** Counter for generated IDs. */
	private long counter = 0;

	/**
	 * Returns the only instance of BTEditorIDGenerator.
	 */
	public static BTEditorIDGenerator getInstance() {
		if (instance == null) {
			instance = new BTEditorIDGenerator();
		}
		return instance;
	}

	/**
	 * Returns the next BTEditor ID.
	 */
	public long getNextID() {
		return this.counter++;
	}

	/**
	 * Private constructor to force the singleton pattern.
	 */
	private BTEditorIDGenerator() {
	}
}
