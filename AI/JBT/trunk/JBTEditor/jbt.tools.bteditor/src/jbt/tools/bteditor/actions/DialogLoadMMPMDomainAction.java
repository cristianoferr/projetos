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

import java.io.File;
import java.util.Vector;

import jbt.tools.bteditor.Application;
import jbt.tools.bteditor.util.Extensions;
import jbt.tools.bteditor.util.IconsPaths;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action that loads several MMPM domain files into the application. It first
 * opens a dialog where the user selects the domain files. Then, these domains
 * are loaded into the application.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class DialogLoadMMPMDomainAction extends Action implements
		IWorkbenchAction {
	private IWorkbenchWindow window;

	/**
	 * Constructor.
	 * 
	 * @param window
	 *            the main window.
	 */
	public DialogLoadMMPMDomainAction(IWorkbenchWindow window) {
		this.window = window;
		this.setText("Load MMPM Domain");
		this.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				Application.PLUGIN_ID, IconsPaths.LOAD_MMPM_DOMAIN));
	}

	/**
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		/*
		 * Open dialog for asking the user to enter some file names.
		 */
		FileDialog dialog = new FileDialog(this.window.getShell(), SWT.MULTI);
		String[] individualFilters = Extensions
				.getFiltersFromExtensions(Extensions.getMMPMDomainFileExtensions());
		String[] unifiedFilter = new String[] { Extensions
				.getUnifiedFilterFromExtensions(Extensions
						.getMMPMDomainFileExtensions()) };
		String[] filtersToUse = Extensions.joinArrays(individualFilters,
				unifiedFilter);
		dialog.setFilterExtensions(filtersToUse);
		dialog.setText("Open BT");

		if (dialog.open() != null) {
			/* Get the name of the files (NOT absolute path). */
			String[] singleNames = dialog.getFileNames();

			/*
			 * This vector will store the absolute path of every single selected
			 * file.
			 */
			Vector<String> absolutePath = new Vector<String>();

			for (int i = 0, n = singleNames.length; i < n; i++) {
				StringBuffer buffer = new StringBuffer(dialog.getFilterPath());
				if (buffer.charAt(buffer.length() - 1) != File.separatorChar)
					buffer.append(File.separatorChar);
				buffer.append(singleNames[i]);
				absolutePath.add(buffer.toString());
			}

			new LoadMMPMDomainAction(absolutePath).run();
		}
	}

	/**
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	public void dispose() {
	}
}
