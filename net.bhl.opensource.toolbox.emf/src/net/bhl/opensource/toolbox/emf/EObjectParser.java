/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;

import net.bhl.opensource.toolbox.io.DataSet;

/**
 * This class reads in a CSV file and tries to map the data to the fields of an
 * EObject. This is done by trial and error and works only if the CSV parameters
 * are spelled exactly like the corresponding fields in the classes.
 *
 * @see Java Reflection
 *
 * @author Marc.Engelmann
 *
 */
public interface EObjectParser {

	/**
	 * Loop through all fields of a specific object and try to apply the map entry
	 * to a field. Allowed variable types are Integer, Double, Enumeration and
	 * Boolean.
	 *
	 * @param key
	 * @param value
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	static boolean applyKnownSetting(Map.Entry<String, Object> pair, EObject object) {

		// Get the class of the EObject
		Class<? extends EObject> clazz = object.getClass();

		// If there are no declared fields in the class, try the superclass instead.
		while (clazz.getDeclaredFields().length == 0) {
			clazz = (Class<? extends EObject>) clazz.getSuperclass();
		}

		try {

			// Load the field of the EOject which is named like the pair key.
			Field field = clazz.getDeclaredField(pair.getKey());
			field.setAccessible(true);

			// Try to apply the pair value depending on the class type.
			if (field.getType() == int.class || field.getType() == boolean.class || field.getType() == double.class
					|| !((Class<?>) field.getType()).isEnum()) {
				field.set(object, pair.getValue());

			} else {
				field.set(object, Enum.valueOf(field.getType().asSubclass(Enum.class), (String) pair.getValue()));

			}

			return true;

		} catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * This function runs the parser.
	 *
	 * @param parameters
	 * @param cabin
	 */
	static List<Map.Entry<String, Object>> execute(DataSet parameters, EObject... objects) {

		// Create a list with the failed map entries
		List<Entry<String, Object>> failures = new ArrayList<>();

		// Loop through all EObjects by loading specific parameter names and values.
		parameters: for (Entry<String, Object> pair : parameters.entrySet()) {

			// Use an index counter to loop through the settings.
			int option = 0;

			// Try to apply a entry pair
			while (!applyKnownSetting(pair, objects[option])) {

				option++;

				// Check if the end of the object array is reached.
				if (option == objects.length) {

					// No matching parameter could be found.
//					Log.printInfoLine("No match found", "'" + pair.getKey() + "'");
					failures.add(pair);
					continue parameters;
				}
			}
		}

		return failures;
	}
}
