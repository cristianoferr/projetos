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
package jbt.tools.bteditor.actions;

import java.io.IOException;

import jbt.tools.bteditor.BTXMLManager;
import jbt.tools.bteditor.model.BT;

import org.eclipse.jface.action.Action;

/**
 * Action that saves a behaviour tree in a XML file. If there is currently an
 * open behaviour tree coming from a file and with the same name, the saving
 * process fails, and an exception is thrown.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class SaveBTAction extends Action {
	/** The tree to save. */
	private BT btToSave;
	/** The name of the file where the tree must be saved. */
	private String fileName;

	/**
	 * Constructor.
	 * 
	 * @param tree
	 *            the tree to save.
	 * @param fileName
	 *            the name of the file where the tree is going to be saved.
	 */
	public SaveBTAction(BT tree, String fileName) {
		this.btToSave = tree;
		this.fileName = fileName;
	}

	/**
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run(){
		try {
			BTXMLManager.export(this.btToSave, this.fileName);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
