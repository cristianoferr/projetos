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

import jbt.tools.bteditor.actions.DialogLoadMMPMDomainAction;
import jbt.tools.bteditor.actions.DialogOpenBTAction;
import jbt.tools.bteditor.actions.NewBTAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchWindow window;

	private IWorkbenchAction openBTAction;
	private Action newBTAction;
	private IWorkbenchAction saveBTAction;
	private IWorkbenchAction saveBTAsAction;
	private IWorkbenchAction loadMMPMDomainAction;
	private IWorkbenchAction aboutAction;

	private IContributionItem viewsList;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		this.window = window;

		this.saveBTAction = ActionFactory.SAVE.create(this.window);
		this.saveBTAsAction = ActionFactory.SAVE_AS.create(this.window);
		this.openBTAction = new DialogOpenBTAction(this.window);
		this.newBTAction = new NewBTAction();
		this.loadMMPMDomainAction = new DialogLoadMMPMDomainAction(this.window);
		this.viewsList = ContributionItemFactory.VIEWS_SHORTLIST.create(this.window);
		this.aboutAction = ActionFactory.ABOUT.create(this.window);

		/* For key bindings and for copy and paste actions... */
		this.register(this.saveBTAction);
		this.register(ActionFactory.COPY.create(this.window));
		this.register(ActionFactory.PASTE.create(this.window));
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("File");
		MenuManager viewMenu = new MenuManager("View");
		MenuManager helpMenu = new MenuManager("Help");

		MenuManager showViewMenu = new MenuManager("Show view");

		fileMenu.add(this.newBTAction);
		fileMenu.add(this.openBTAction);
		fileMenu.add(this.saveBTAction);
		fileMenu.add(this.saveBTAsAction);
		fileMenu.add(this.loadMMPMDomainAction);

		showViewMenu.add(this.viewsList);
		viewMenu.add(showViewMenu);

		helpMenu.add(this.aboutAction);

		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager firstCoolBar = new ToolBarManager(coolBar.getStyle());
		coolBar.add(firstCoolBar);
		firstCoolBar.add(this.newBTAction);
		firstCoolBar.add(this.saveBTAction);
		firstCoolBar.add(this.saveBTAsAction);
		firstCoolBar.add(this.openBTAction);
		firstCoolBar.add(this.loadMMPMDomainAction);
		coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
}
