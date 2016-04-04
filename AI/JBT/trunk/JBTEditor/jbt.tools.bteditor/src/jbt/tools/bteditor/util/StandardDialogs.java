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
package jbt.tools.bteditor.util;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;

/**
 * Class that contains a bunch of standard SWT dialogs to be used by calling
 * simple methods.
 * <p>
 * All these dialogs block when opened.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class StandardDialogs {
	/*
	 * Private constructor.
	 */
	private StandardDialogs() {}

	public static void informationDialog(String title, String informationMessage) {
		MessageDialog.openInformation(null, title, informationMessage);
	}

	public static void errorDialog(String title, String errorMessage) {
		MessageDialog.openError(null, title, errorMessage);
	}

	public static void exceptionDialog(String title, String errorMessage, Exception e) {
		DetailsDialog dialog = new DetailsDialog(title, errorMessage,
				Utilities.stackTraceToString(e), DetailsDialog.ERROR, null, SWT.APPLICATION_MODAL
						| SWT.RESIZE | SWT.MIN | SWT.MAX | SWT.CLOSE);
		dialog.setBlockOnOpen(true);
		dialog.open();
	}

	public static void exceptionDialog(String title, String errorMessage, List<Exception> exceptions) {
		String exceptionMessage = new String();
		for (Exception currentException : exceptions) {
			exceptionMessage += "** Exception **\n\n"
					+ Utilities.stackTraceToString(currentException) + "\n\n";
		}
		DetailsDialog dialog=new DetailsDialog(title, errorMessage, exceptionMessage, DetailsDialog.ERROR, null,
				SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.MIN | SWT.MAX | SWT.CLOSE);
		
		dialog.setBlockOnOpen(true);
		dialog.open();
	}

	public static void warningDialog(String title, String warningMessage) {
		MessageDialog.openWarning(null, title, warningMessage);
	}

	public static boolean confirmationDialog(String title, String confirmationMessage) {
		return MessageDialog.openConfirm(null, title, confirmationMessage);
	}

	public static boolean questionDialog(String title, String question) {
		return MessageDialog.openQuestion(null, title, question);
	}
}
