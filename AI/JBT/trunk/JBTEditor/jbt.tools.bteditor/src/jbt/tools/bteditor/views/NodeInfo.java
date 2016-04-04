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

import java.util.List;

import jbt.tools.bteditor.editor.BTEditor;
import jbt.tools.bteditor.model.BTNode;
import jbt.tools.bteditor.util.Utilities;
import jbt.tools.bteditor.viewers.NodeInfoViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * ViewPart that shows the information of the selected node of the currently
 * active editor. Internally, this view just stores a NodeInfoViewer.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class NodeInfo extends ViewPart {
	public static String ID = "jbt.tools.bteditor.views.NodeInfo";
	private NodeInfoViewer nodeInfoViewer;

	public void createPartControl(Composite parent) {
		this.nodeInfoViewer = new NodeInfoViewer(parent, SWT.NONE);

		/* Initialize view's content with the currently selected node. */
		BTEditor activeBTEditor = Utilities.getActiveBTEditor();

		if (activeBTEditor != null) {
			List<BTNode> selectedElements = activeBTEditor.getSelectedElements();
			if (selectedElements.size() != 0) {
				this.nodeInfoViewer.setNode(selectedElements.get(0));
			}
		}
	}

	public void setFocus() {
	}

	/**
	 * Sets the node whose information is being displayed.
	 */
	public void setNode(BTNode node) {
		this.nodeInfoViewer.setNode(node);
	}
}
