/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Marc.Engelmann
 * @since 21.04.2020
 *
 */
public interface BHLSWTScreenshot {

	/**
	 * @param display
	 * @param shell
	 * @param exportPath
	 * @param name
	 */
	static void run(Display display, Shell shell, String exportPath, String name) {
		/*
		 * Widget capturing logic
		 */
		GC gc = new GC(display);
		final Image image = new Image(display, shell.getBounds());
		gc.copyArea(image, shell.getBounds().x, shell.getBounds().y);
		gc.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(exportPath + "/" + name + ".png", SWT.IMAGE_PNG);
	}
}
