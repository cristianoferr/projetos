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

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * This class is used for overlaying image icons.
 * 
 * @author balajik
 * 
 */
public class OverlayImageIcon extends CompositeImageDescriptor {
	/**
	 * Base image of the object
	 */
	private Image baseImage;

	/**
	 * Size of the base image
	 */
	private Point sizeOfImage;

	/**
	 * Decoration
	 */
	private Image decoration;

	/**
	 * Constructor for overlayImageIcon.
	 */
	public OverlayImageIcon(Image baseImage, Image decoration) {
		// Base image of the object
		this.baseImage = baseImage;
		// Demo Image Object
		this.decoration = decoration;
		this.sizeOfImage = new Point(baseImage.getBounds().width, baseImage.getBounds().height);
	}

	/**
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int,
	 *      int) DrawCompositeImage is called to draw the composite image.
	 * 
	 */
	protected void drawCompositeImage(int arg0, int arg1) {
		// Draw the base image
		drawImage(baseImage.getImageData(), 0, 0);
		ImageData imageData = decoration.getImageData();

		// Draw on bottom right corner
		drawImage(imageData, sizeOfImage.x - imageData.width, sizeOfImage.y - imageData.height);
	}

	/**
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize() get
	 *      the size of the object
	 */
	protected Point getSize() {
		return sizeOfImage;
	}

	/**
	 * Get the image formed by overlaying different images on the base image
	 * 
	 * @return composite image
	 */
	public Image getImage() {
		return createImage();
	}
}
