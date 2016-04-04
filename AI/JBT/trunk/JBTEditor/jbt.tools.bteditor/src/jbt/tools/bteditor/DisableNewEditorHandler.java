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
package jbt.tools.bteditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Handler for disabling the "New Editor" standard action of editors' context
 * menu.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class DisableNewEditorHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}

	public boolean isEnabled() {
		/*
		 * By doing this, the element "New Editor" in the context menu is
		 * disabled.
		 */
		return false;
	}
}
