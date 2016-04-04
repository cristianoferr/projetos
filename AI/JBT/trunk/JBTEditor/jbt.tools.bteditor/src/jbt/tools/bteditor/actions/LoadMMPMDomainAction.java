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
import java.util.Vector;

import jbt.tools.bteditor.NodesLoader;
import jbt.tools.bteditor.model.ConceptualNodesTree;
import jbt.tools.bteditor.util.StandardDialogs;
import jbt.tools.bteditor.util.Utilities;
import jbt.tools.bteditor.views.NodesNavigator;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewPart;

/**
 * Action that loads, into the {@link NodesLoader}, the actions and conditions
 * (sensors) present in a MMPM domain file.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class LoadMMPMDomainAction extends Action {
	/** Names of the files to open. */
	private Vector<String> fileNames;

	/**
	 * Constructor.
	 * 
	 * @param fileNames
	 *            the names of the files to load.
	 */
	public LoadMMPMDomainAction(Vector<String> fileNames) {
		this.fileNames = fileNames;
	}

	/**
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Vector<Exception> exceptions = new Vector<Exception>();

		for (String currentFile : this.fileNames) {
			try {
				ConceptualNodesTree newTree = NodesLoader
						.loadNonStandardNodes(currentFile);

				IViewPart view = Utilities.getView(NodesNavigator.class);

				if (view != null) {
					NodesNavigator treeView = (NodesNavigator) view;
					treeView.addTree(newTree);
				}

			} catch (IOException e) {
				exceptions.add(e);
			}
		}

		if (exceptions.size() != 0) {
			StandardDialogs.exceptionDialog("Error loading MMPM domain file",
					"There were errors when opening MMPM domain files",
					exceptions);
		}
	}
}
