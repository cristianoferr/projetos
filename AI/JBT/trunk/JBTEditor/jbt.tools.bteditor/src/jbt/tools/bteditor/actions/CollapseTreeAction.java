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

import jbt.tools.bteditor.editor.BTEditor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;

public class CollapseTreeAction extends EditorActionDelegate{
	public void run(IAction action) {
		((BTEditor)this.getEditor()).expandTree(false);
	}

	public void selectionChanged(IAction action, ISelection selection) {

	}
}
