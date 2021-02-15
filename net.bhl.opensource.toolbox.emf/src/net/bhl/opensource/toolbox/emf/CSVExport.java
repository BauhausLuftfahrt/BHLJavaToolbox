/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import net.bhl.opensource.toolbox.io.CSVImport;
import net.bhl.opensource.toolbox.io.CSVRawContent;
import net.bhl.opensource.toolbox.io.DataSet;
import net.bhl.opensource.toolbox.io.Log;
import net.bhl.opensource.toolbox.time.StopWatch;
import net.bhl.opensource.toolbox.time.TimeHelper;

/**
 * This command exports data from the EMF meta-model.
 *
 * @author Marc.Engelmann
 *
 *         Workflow:
 *
 *         1) Initialize object and assign path and file name
 *
 *         2) Add head optionally
 *
 *         3) Add data as arrays, single, Hash map or EObject
 *
 *         4) Save file
 */

public class CSVExport {

	private static final String FILE_FORMAT = ".csv";
	private CSVRawContent content;
	private File file;
	private StopWatch watch;

	/**
	 * Instantiates a new excel export.
	 *
	 * @throws IOException
	 */
	public CSVExport(String fileName, String filePath, boolean override) throws IOException {

		content = new CSVRawContent(override);
		watch = new StopWatch();

		Log.start(fileName);

		File directory = new File(filePath);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		file = new File(filePath + fileName + FILE_FORMAT);

		if (override) {
			file.delete();
			file.createNewFile();
		}

		if (file.exists()) {
			for (List<String> list : CSVImport.readDataLines(filePath + fileName + FILE_FORMAT)) {
				content.add(new ArrayList<Object>(list));
			}
		}
	}

	/**
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void saveFile(boolean vertical) throws IOException {

		FileWriter writer = new FileWriter(file, false);

		// Vertical list of variables
		if (vertical) {
			for (List<Object> row : content) {
				for (int i = 0; i < row.size(); i++) {
					writer.append(row.get(i).toString());
					if (i != row.size() - 1) {
						writer.append(";");
					}
				}
				writer.append("\n");
			}

		}
		// Horizontal list of variables
		else {
			for (int i = 0; i < content.get(0).size(); i++) {
				for (List<Object> element : content) {
					writer.append(element.get(i).toString() + ";");
				}
				writer.append("\n");
			}
		}

		writer.flush();
		writer.close();

		watch.stop();
		Log.end(watch);
	}

	/**
	 *
	 * @param name
	 * @param obj
	 */
	public void add(String name, Object obj) {
		content.addSingleValue(name, obj);
	}

	/**
	 * @param name
	 * @param objects
	 */
	public void addArray(String name, double[] array) {
		content.addArray(name, array);
	}

	/**
	 * @param map
	 */
	public void add(DataSet map, boolean sorted) {
		content.addDataSet(map, sorted);
	}

	/**
	 *
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void addAll(Object obj) throws IllegalArgumentException, IllegalAccessException {
		content.addAllFields(obj, EObject.class);
	}

	/**
	 *
	 * @param iteration
	 */
	public void addHead(int iteration) {
		content.addSingleValue("Date", TimeHelper.now("d.MM.YYYY"));
		content.addSingleValue("Time", TimeHelper.now("HH:mm:ss"));
		content.addSingleValue("Iteration", iteration);
	}
}