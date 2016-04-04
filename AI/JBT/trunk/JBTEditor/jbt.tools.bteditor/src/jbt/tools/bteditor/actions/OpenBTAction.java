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

import java.util.Vector;

import jbt.tools.bteditor.editor.BTEditor;
import jbt.tools.bteditor.editor.BTEditorInput;
import jbt.tools.bteditor.util.StandardDialogs;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Action for loading behaviour trees into the application, and opens an editor
 * for every tree that is properly opened. The trees are read from XML files.
 * This action can open several trees simultaneously.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class OpenBTAction extends Action {
	/** Names of the files that contain the trees to open. */
	private Vector<String> fileNames;

	/**
	 * Constructor.
	 * 
	 * @param fileNames
	 *            names of the files that contain the trees to open.
	 */
	public OpenBTAction(Vector<String> fileNames) {
		this.fileNames = fileNames;
	}

	public void run() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getWorkbenchWindows()[0].getActivePage();
		
		Vector<Exception> exceptions=new Vector<Exception>();

		for (String fileName : this.fileNames) {
			BTEditorInput editorInput = new BTEditorInput(fileName, true, false);

			try {
				activePage.openEditor(editorInput, BTEditor.ID);
			} catch (PartInitException e) {
				exceptions.add(e);
			}
		}
		
		if(exceptions.size()!=0){
			StandardDialogs.exceptionDialog("Error opening tree",
					"There was an error when opening the tree", exceptions);
		}
	}
}
