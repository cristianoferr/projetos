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

import jbt.tools.bteditor.Application;
import jbt.tools.bteditor.editor.BTEditor;
import jbt.tools.bteditor.editor.BTEditorInput;
import jbt.tools.bteditor.util.IconsPaths;
import jbt.tools.bteditor.util.StandardDialogs;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that creates a new BT and opens an editor for it.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class NewBTAction extends Action {
	/** Integer used for generating names for the trees that are created. */
	private static int currentTreeNumber = 1;

	/**
	 * Constructor.
	 */
	public NewBTAction() {
		this.setText("New BT");
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Application.PLUGIN_ID, IconsPaths.NEW_BT));
	}

	/**
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getWorkbenchWindows()[0].getActivePage();

		BTEditorInput editorInput = new BTEditorInput("new "
				+ currentTreeNumber, false, false);

		currentTreeNumber++;

		try {
			activePage.openEditor(editorInput, BTEditor.ID);
		} catch (PartInitException e) {
			StandardDialogs.exceptionDialog("Error when creating new tree",
					"There was an unexpected error when creating the tree", e);
		}
	}
}
