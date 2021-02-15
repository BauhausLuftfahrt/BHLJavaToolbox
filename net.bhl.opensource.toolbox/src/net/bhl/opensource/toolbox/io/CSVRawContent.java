/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.io;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import net.bhl.opensource.toolbox.time.TimeHelper;

/**
 * @author Marc.Engelmann
 * @since 22.04.2020
 *
 */

public class CSVRawContent extends ArrayList<List<Object>> {

	private static final long serialVersionUID = -2164167183717393188L;
	private boolean override;

	/**
	 * @param override
	 */
	public CSVRawContent(boolean override) {
		this.override = override;
	}

	/**
	 *
	 * @param name
	 * @param obj
	 */
	public void addSingleValue(String name, Object obj) {

		// Prevent SYLK Excel error
		if (Objects.equals(new String("ID"), new String(name))) {
			throw new IllegalArgumentException("The use of the name 'ID' is not allowed in a .csv");
		}

		// Check if data type already exists
		for (List<Object> list : this) {
			if (list.get(0).toString().contentEquals(name)) {
				list.add(obj);
				return;
			}
		}

		// Else create a new data row
		List<Object> row = new ArrayList<>();
		row.add(name);

		// This could be activated if
		if (size() > 0 && !override) {
			for (int i = 0; i < get(0).size() - 2; i++) {
				row.add("-");
			}
		}

		row.add(obj.toString());
		this.add(row);
	}

	/**
	 * @param name
	 * @param objects
	 */
	public void addArray(String name, double[] array) {
		for (Object obj : array) {
			addSingleValue(name, obj);
		}
	}

	/**
	 * @param map
	 */
	public void addDataSet(DataSet map, boolean sorted) {

		map.getName().ifPresent(name -> addSingleValue("Title", name));

		if (sorted) {
			for (Entry<String, Object> entry : map.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
					.collect(Collectors.toList())) {
				addSingleValue(entry.getKey(), entry.getValue());
			}

		} else {
			for (Entry<String, Object> pair : map.entrySet()) {
				addSingleValue(pair.getKey(), pair.getValue());
			}
		}
	}

	/**
	 *
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public <T extends Object> void addAllFields(Object obj, Class<T> skippClasses)
			throws IllegalArgumentException, IllegalAccessException {

		// Loop through all declared fields of the object
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);

			// Filter EObjects
			if (skippClasses.isAssignableFrom(field.getType())) {
				continue;
			}

			// Filter default values and objects
			if (!field.getName().contains("EDEFAULT") && !field.get(obj).toString().contains("com.paxelerate")) {
				addSingleValue(field.getName(), field.get(obj));
			}
		}
	}

	/**
	 *
	 * @param iteration
	 */
	public void addHead(int iteration) {
		addSingleValue("Date", TimeHelper.now("d.MM.YYYY"));
		addSingleValue("Time", TimeHelper.now("HH:mm:ss"));
		addSingleValue("Iteration", iteration);
	}

}
