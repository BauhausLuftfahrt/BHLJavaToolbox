/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import net.bhl.opensource.toolbox.io.CSVRawContent;
import net.bhl.opensource.toolbox.io.DataSet;
import net.bhl.opensource.toolbox.ui.swt.BHLColors;

/**
 * @author Marc.Engelmann
 * @since 21.04.2020
 *
 */
public interface StatusComposite {

	String ITEM_DIVIDER = "#";

	/**
	 * @param cabin
	 * @param status
	 * @param parent
	 * @return
	 */
	static Composite getStatusComposite(List<DataSet> statusList, Composite parent) {

		CSVRawContent csvRawContent = new CSVRawContent(false);
		Composite content = new Composite(parent, SWT.NONE);

		final FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.marginHeight = 10;
		fillLayout.marginWidth = 10;
		content.setLayout(fillLayout);

		final Layout layout = GridLayoutFactory.fillDefaults().margins(5, 5).create();

		List<Composite> values = new ArrayList<>();

		for (DataSet set : statusList) {

			String title = set.getName().isPresent() ? set.getName().get() : "no name set for dataset";
			set.setName(null);

			csvRawContent.addDataSet(set, true);

			Composite valueColumns = new Composite(content, SWT.FILL);
			valueColumns.setLayout(layout);
			values.add(valueColumns);

			// Create the content container in the specific value column
			Composite headlineContainer = new Composite(valueColumns, SWT.NONE);
			headlineContainer.setLayout(layout);
			headlineContainer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 0, 0));

			Label headline = new Label(headlineContainer, SWT.NONE);
			headline.setText(title);
			headline.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		}

		Composite keys = new Composite(content, SWT.FILL);
		keys.setLayout(layout);

		// Create the content container in the specific value column
		Composite empty = new Composite(keys, SWT.NONE);
		empty.setLayout(layout);
		empty.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 0, 0));

		Label headlineEmpty = new Label(empty, SWT.NONE);
		headlineEmpty.setText("");
		headlineEmpty.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));

		// Return here if no data is available
		if (statusList.isEmpty()) {
			headlineEmpty.setText("No dataset available!");
			return content;
		}

		// Loop through all elements of status data set
		for (List<Object> row : csvRawContent) {

			// Create the title container
			Composite titleContainer = new Composite(keys, SWT.NONE);
			titleContainer.setLayout(layout);
			new Label(titleContainer, SWT.NONE).setText(row.get(0).toString());

			// Loop through the different values for each deck
			for (int i = 1; i < row.size(); i++) {

				// Create the content container in the specific value column
				Composite contentContainer = new Composite(values.get(i - 1), SWT.NONE);
				contentContainer.setLayout(layout);
				contentContainer.setBackground(BHLColors.GREY(200));
				contentContainer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 0, 0));

				String contentString = row.get(i).toString();
				String contentStringCache = row.get(i).toString();

				if (contentString.contains("true") || contentString.contains("false")) {
					contentString = contentString.split(ITEM_DIVIDER)[1];

					contentContainer.setBackground(
							contentStringCache.contains("true") ? BHLColors.GREEN_DARK : BHLColors.RED_DARK);

				}

				final Label contentLabel = new Label(contentContainer, SWT.NONE);
				contentLabel.setText(contentString);
				contentLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
				contentLabel.setForeground(BHLColors.adaptToBackground(contentContainer.getBackground()));

			}

			// Add blank elements if length of data set list is inconsistent
			if (row.size() < statusList.size() + 1) {
				for (int i = row.size(); i < statusList.size() + 1; i++) {

					Composite contentContainer = new Composite(values.get(i - 1), SWT.NONE);
					contentContainer.setLayout(layout);
					contentContainer.setBackground(BHLColors.GREY(200));
					contentContainer.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 0, 0));

					final Label contentLabel = new Label(contentContainer, SWT.NONE);
					contentLabel.setText("-");
					contentLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
					contentLabel.setForeground(BHLColors.adaptToBackground(contentContainer.getBackground()));
				}
			}
		}
		return content;
	}
}
