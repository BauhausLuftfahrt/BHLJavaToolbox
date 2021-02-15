/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
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
