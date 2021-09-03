/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.io;

import net.bhl.opensource.toolbox.math.BHLMath;

/**
 * @author Marc.Engelmann
 *
 */
public interface Transformator {

	/**
	 * Transform a String into a matching object type.
	 *
	 * @param node
	 * @return
	 */
	static Object toObject(String content) {

		try {

			if (content.contains("D") || content.contains("F")) {
				throw new NumberFormatException(
						"This script does not transform strings with an d or f after a double value into a double. E.g. 1F is not a double of 1 but a '1F' string!");
			}

			Double value = Double.parseDouble(content);

			if (BHLMath.isInteger(value)) {
				return value.intValue();
			} else {
				return value;
			}

		} catch (NumberFormatException e) {

			// Loop through boolean literals and check if there is a match
			if (content.toLowerCase().contains("true") || content.toLowerCase().contains("false")) {
				return Boolean.parseBoolean(content);
			}

			// Else return string
			return content;
		}
	}
}
