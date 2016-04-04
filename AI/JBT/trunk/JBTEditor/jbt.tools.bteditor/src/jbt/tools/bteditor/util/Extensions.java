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

/**
 * Class that stores the file extensions that are supported by the application.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class Extensions {
	/** Extensions of the files that can contain behaviour trees. */
	private static final String[] BTFileExtensions = new String[] { "xbt",
			"xml" };

	/** Extensions of MMPM domain files. */
	private static final String[] MMPMDomainfileExtensions = new String[] { "xml" };

	/**
	 * Returns an array containing the extensions of the files than can contain
	 * behaviour trees.
	 */
	public static String[] getBTFileExtensions() {
		return BTFileExtensions;
	}

	/**
	 * Returns an array containing the extensions of MMPM domain files.
	 */
	public static String[] getMMPMDomainFileExtensions() {
		return MMPMDomainfileExtensions;
	}

	/**
	 * Given an array with file extensions, this method returns filters for
	 * those extensions to be used in SWT dialogs
	 */
	public static String[] getFiltersFromExtensions(String[] extensions) {
		String[] result = new String[extensions.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = "*." + extensions[i];
		}
		return result;
	}

	/**
	 * Given a set of file extensions, this method returns a single filter for
	 * all of those extensions.
	 */
	public static String getUnifiedFilterFromExtensions(String[] extensions) {
		String result = new String();
		for (int i = 0; i < extensions.length - 1; i++) {
			result += "*." + extensions[i] + ";";
		}
		result += "*." + extensions[extensions.length - 1];
		return result;
	}

	/**
	 * Joins two arrays of String.
	 */
	public static String[] joinArrays(String[] array1, String[] array2) {
		String[] result = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

	/**
	 * Joins a file name and an extension. If the file name ends with "."+
	 * <code>extension</code> , then <code>fileName</code> itself is returned.
	 * Otherwise, it returns <code>fileName+"."+extension</code>.
	 */
	public static String joinFileNameAndExtension(String fileName,
			String extension) {
		if (fileName.endsWith("." + extension)) {
			return fileName;
		} else {
			return fileName + "." + extension;
		}
	}

	private Extensions() {
	}
}
