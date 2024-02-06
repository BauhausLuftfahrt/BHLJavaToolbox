/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.string;

/**
 * The Class StringHelper.
 *
 * @author Marc.Engelmann
 * @since 29.03.2021
 *
 */
public interface StringHelper {

	/**
	 * This method splits camel case strings into normal strings with spaces.
	 *
	 * @param str is the string that should be transformed.
	 * @return returns the transformed string
	 */
	static String splitCamelCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ").substring(1);
	}

//	/**
//	 * @param list
//	 * @param consumer
//	 * @return
//	 */
//	static <T extends Object> List<String> objectsToList(List<T> list, Function<T, String> func) {
//
//		// Turn the rows into a string array
//		final List<String> strings = new ArrayList<>();
//		list.forEach(entry -> strings.add(func.apply(entry)));
//
//		return strings;
//	}

}