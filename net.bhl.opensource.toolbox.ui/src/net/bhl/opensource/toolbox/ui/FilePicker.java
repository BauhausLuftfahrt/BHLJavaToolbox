/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui;

import java.io.File;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Marc.Engelmann
 *
 */
public interface FilePicker {
	/**
	 * Open a JFileChooser and get a file.
	 *
	 */
	static Optional<File> getFile(String startPath, String format) {

		final JFileChooser picker = new JFileChooser(startPath);
		picker.setMultiSelectionEnabled(false);
		picker.setFileSelectionMode(JFileChooser.FILES_ONLY);
		picker.setAcceptAllFileFilterUsed(false);
		picker.addChoosableFileFilter(new FileNameExtensionFilter(format.toUpperCase() + " Files", format));
		picker.setDialogTitle("Please choose a " + format.toUpperCase() + " file as input.");

		if (picker.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
			return Optional.empty();
		}

		return Optional.of(picker.getSelectedFile());
	}
}
