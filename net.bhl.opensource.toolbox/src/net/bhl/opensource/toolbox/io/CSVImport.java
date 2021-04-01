/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.bhl.opensource.toolbox.time.StopWatch;

/**
 * This class imports a CSV file and turns it into a list of hash maps. The file
 * structure should be like this:
 *
 * |--variableName--variableOption_0--variableOption_1--...--variableOption_n--|
 *
 * The hash maps will contain all variableNames and the variable option at a
 * specific index.
 *
 * ---- Hash Map @ List index 0: Key: VariableName -> Value: variableOption_0
 * ---- Hash Map @ List index 0: Key: VariableName -> Value: variableOption_1
 * ---- Hash Map @ List index n: Key: VariableName -> Value: variableOption_n
 *
 * @author Marc.Engelmann
 *
 */
public interface CSVImport {

	boolean SHOW_LOG = true;

	/**
	 *
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	static List<DataSet> getRows(String fileName, String filePath) {

		StopWatch watch = new StopWatch();

		if (!fileName.contains(".csv")) {
			fileName += ".csv";
		} else if (fileName.contains(".csv.csv")) {
			fileName.replace(".csv.csv", ".csv");
		}

//		Log.start(fileName);

		try {

			String path = filePath + fileName;

			// Read in all rows
			List<List<String>> variables = readDataLines(path);

			// Transform rows to Maps
			List<DataSet> allVariants = parseAsRows(variables);

//			watch.stop();
//			Log.end(watch);
			return allVariants;

		} catch (IOException e) {

			System.out.println("CSVImporter: IOException: Data import failed!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	static List<DataSet> importAll(String fileName, String filePath) {

		StopWatch watch = new StopWatch();

		if (!fileName.contains(".csv")) {
			fileName += ".csv";
		} else if (fileName.contains(".csv.csv")) {
			fileName.replace(".csv.csv", ".csv");
		}

		Log.start(fileName);

		try {

			String path = filePath + fileName;

			// Read in all rows
			List<List<String>> variables = readDataLines(path);

			// Transform rows to Maps
			List<DataSet> allVariants = parseAsColumns(variables);

			watch.stop();
			Log.end(watch);
			return allVariants;

		} catch (IOException e) {

			System.out.println("CSVImporter: IOException: Data import failed!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param variables
	 * @return
	 */
	static List<DataSet> parseAsRows(List<List<String>> variables) {

		// Transform rows to Maps
		List<DataSet> allVariants = new ArrayList<>();

		for (int i = 1; i < variables.size(); i++) {

			try {
				DataSet variant = new DataSet(String.valueOf(i));

				for (int j = 0; j < variables.get(0).size() - 1; j++) {

//				for (List<String> variable : variables) {

					if (variant.getString(variables.get(0).get(j)).isPresent()) {
						System.err.println("CSVImporter: " + variables.get(0).get(j) + " is defined more than once!");
					} else {
						variant.put(variables.get(0).get(j), Transformator.toObject(variables.get(i).get(j)));
					}
				}

				allVariants.add(variant);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("CSVImporter: Variant skipped! Settings in CSV file not complete! " + e);
			}
		}

		return allVariants;
	}

	/**
	 * @param variables
	 * @return
	 */
	static List<DataSet> parseAsColumns(List<List<String>> variables) {

		// Transform rows to Maps
		List<DataSet> allVariants = new ArrayList<>();

		for (int i = 1; i < variables.get(0).size(); i++) {

			try {
				DataSet variant = new DataSet(String.valueOf(i));

				for (List<String> variable : variables) {

					if (variant.getString(variable.get(0)).isPresent()) {
						System.err.println("CSVImporter: " + variable.get(0) + " is defined more than once!");
					} else {
						variant.put(variable.get(0), Transformator.toObject(variable.get(i)));
					}
				}

				allVariants.add(variant);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("CSVImporter: Variant skipped! Settings in CSV file not complete! " + e);
			}
		}

		return allVariants;
	}

	/**
	 * @throws IOException
	 *
	 */
	static List<List<String>> readDataLines(String absoluteFilePath) throws IOException {

		// Initialize file reader
		BufferedReader reader = new BufferedReader(new FileReader(absoluteFilePath));

		// Read in all rows
		List<List<String>> variables = new ArrayList<>();

		// Loop through all vertical elements
		for (Object line : reader.lines().toArray()) {

			String lineStr = line.toString();

			// Check if line is not empty
			if (lineStr.length() > 0) {

				if (lineStr.charAt(lineStr.length() - 1) == ';') {
					lineStr = lineStr.substring(0, lineStr.length() - 1);
				}

				// Check if the lines are empty or are not complete.
				if (!Pattern.compile("(.)\\1*").matcher(lineStr).matches()
						&& !lineStr.substring(lineStr.length() - 1).contentEquals(";")) {

					variables.add(Arrays.asList(lineStr.split(";")));
				}
			}
		}

		// Close reader
		reader.close();

		return variables;
	}
}