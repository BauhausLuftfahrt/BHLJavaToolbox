/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math;

import java.util.Arrays;

/**
 * @author Marc.Engelmann
 * @since 12.12.2018
 *
 */
public interface BHLMath {

	/**
	 *
	 * Get the maximum value of multiple double values.
	 *
	 * @param values
	 * @return
	 */
	static double max(double... values) {
		return Arrays.stream(values).max().orElse(0);
	}

	/**
	 * Get the maximum value of multiple integer values.
	 *
	 * @param values
	 * @return
	 */
	static int max(int... values) {
		return Arrays.stream(values).max().orElse(0);
	}

	/**
	 * @param value
	 * @return
	 */
	static int toInt(double value) {
		return (int) Math.round(value);
	}

	/**
	 * @param radians
	 * @return
	 */
	static double to0to360Degree(double radians) {

		// transform from radian to degree
		double angle = Math.toDegrees(radians);

		// if degree is smaller than 0, convert it
		if (angle < 0.0) {
			angle += 360.0;
		}

		return angle;
	}

	/**
	 * @param value
	 * @param digits
	 * @return
	 */
	static double round(double value, int digits) {
		return Math.round(value * Math.pow(10.0, digits)) / Math.pow(10.0, digits);
	}

	/**
	 * @param number
	 * @return
	 */
	static boolean isInteger(double number) {
		return Math.ceil(number) == Math.floor(number);
	}
}
