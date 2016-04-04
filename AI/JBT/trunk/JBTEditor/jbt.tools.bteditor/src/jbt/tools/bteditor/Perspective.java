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

import jbt.tools.bteditor.views.NodeInfo;
import jbt.tools.bteditor.views.NodesNavigator;
import jbt.tools.bteditor.views.NodesSearcher;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {
	private static final String RIGHT_FOLDER_ID = "RightFolder";
	private static final String BOTTOM_RIGHT_FOLDER_ID = "BottomRightFolder";

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);

		layout.addView(NodesNavigator.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());

		IFolderLayout rightFolderLayout = layout.createFolder(RIGHT_FOLDER_ID, IPageLayout.RIGHT,
				0.6f, layout.getEditorArea());
		
		IFolderLayout bottomRightFolderLayout=layout.createFolder(BOTTOM_RIGHT_FOLDER_ID, IPageLayout.BOTTOM, 0.5f, RIGHT_FOLDER_ID);
		
		rightFolderLayout.addView(NodeInfo.ID);
		bottomRightFolderLayout.addView(NodesSearcher.ID);
	}
}
