/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui;

import java.net.URL;
import java.util.Optional;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

import net.bhl.opensource.toolbox.string.StringConstants;

/**
 *
 * @author Michael.Shamiyeh
 * @since 2017-07-03
 *
 */
public interface IDialogHelper {
	String SHOWFILE_TITLE = "Open...";
	String SHOWFILE_FILTER_DEFAULT = "*.*";

	static Optional<String> showSelectFileDialog(Shell shell) {
		return showSelectFileDialog(shell, SHOWFILE_TITLE);
	}

	static Optional<String> showSelectFileDialog(Shell shell, String title) {
		return showSelectFileDialog(shell, title, new String[] { SHOWFILE_FILTER_DEFAULT });
	}

	static Optional<String> showSelectFileDialog(Shell shell, String title, String[] filters) {
		return showFileDialog(shell, title, filters, StringConstants.EMPTY, SWT.OPEN);
	}

	static Optional<String> showSaveFileDialog(Shell shell) {
		return showSaveFileDialog(shell, SHOWFILE_TITLE);
	}

	static Optional<String> showSaveFileDialog(Shell shell, String title) {
		return showSaveFileDialog(shell, title, new String[] { SHOWFILE_FILTER_DEFAULT }, StringConstants.EMPTY);
	}

	static Optional<String> showSaveFileDialog(Shell shell, String title, String[] filters, String initialFolder) {
		return showFileDialog(shell, title, filters, initialFolder, SWT.SAVE);
	}

	static Optional<String> showFileDialog(Shell shell, String title, String[] filters, String initialFolder,
			int openSelect) {
		FileDialog fd = new FileDialog(shell, openSelect);

		if (!initialFolder.isEmpty()) {
			fd.setFileName(initialFolder);
		}

		fd.setText(title);
		fd.setFilterExtensions(filters);

		String result = fd.open();

		return Optional.of(result);
	}

	static Optional<String> showDirectoryDialog(Shell shell, String title) {
		DirectoryDialog dd = new DirectoryDialog(shell);
		dd.setText(title);

		String result = dd.open();

		return Optional.of(result);
	}

	static ImageDescriptor getImageDescriptor(Bundle bundle, String relativeFileString) {
		URL url = FileLocator.find(bundle, new Path(relativeFileString), null);
		return ImageDescriptor.createFromURL(url);
	}
}
