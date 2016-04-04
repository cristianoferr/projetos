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
package jbt.tools.bteditor.views;

import jbt.tools.bteditor.NodesLoader;
import jbt.tools.bteditor.model.ConceptualNodesTree;
import jbt.tools.bteditor.viewers.ConceptualNodesTreeViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This is the ViewPart that shows the list of available nodes, that is, the
 * list of nodes that can be used to build behaviour trees.
 * <p>
 * Internally, this view just contains a {@link ConceptualNodesTreeViewer} that shows the
 * list of nodes that are currently loaded into the application (see
 * {@link NodesLoader}.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class NodesNavigator extends ViewPart {
	public static String ID = "jbt.tools.bteditor.views.NodesNavigator";
	private ConceptualNodesTreeViewer viewer;

	public void createPartControl(Composite parent) {
		/* Load both standard and non-standard nodes. */
		this.viewer = new ConceptualNodesTreeViewer(parent, SWT.NONE);

		this.viewer.addTree(NodesLoader.getStandardNodesTree());

		for (ConceptualNodesTree tree : NodesLoader.getNonStandardNodesTrees()) {
			this.viewer.addTree(tree);
		}
	}

	/**
	 * Adds a {@link ConceptualNodesTree} to the list of trees displayed by this
	 * view. All the nodes of the tree will be displayed.
	 */
	public void addTree(ConceptualNodesTree tree) {
		viewer.addTree(tree);
	}

	/**
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}
}
