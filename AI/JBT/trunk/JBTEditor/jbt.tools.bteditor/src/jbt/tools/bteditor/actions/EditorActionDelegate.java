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

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public abstract class EditorActionDelegate implements IEditorActionDelegate {
	private IEditorPart editor;
	private IAction accion;

	public void setActiveEditor(IAction action, IEditorPart targetEditor){
		this.editor = targetEditor;
		this.accion = action;
	}

	public IEditorPart getEditor(){
		return this.editor;
	}

	public IAction getAction(){
		return this.accion;
	}
}
