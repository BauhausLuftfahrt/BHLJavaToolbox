/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;
import net.bhl.opensource.toolbox.math.vector.IntVector;

/**
 * The Class Distance.
 *
 * @author Marc.Engelmann
 *
 */
public interface Distance {

	/**
	 * This method calculates the Pythagoras distance between two vectors. Simple
	 * multiplication is about 20 times faster than Math.pow()
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	static double distanceBetween(double x1, double y1, double x2, double y2) {

		/* define the first element of the square root function */
		double first = x2 - x1;

		/* define the second element of the square root function */
		double second = y2 - y1;

		/* calculate the square root */
		return Math.sqrt(first * first + second * second);

	}

	/**
	 * This method calculates the Pythagoras distance between two vectors. Simple
	 * multiplication is about 20 times faster than Math.pow()
	 *
	 * @param vector1 the vector1
	 * @param vector2 the vector2
	 * @return the distance between the nodes
	 */
	static double distanceBetween(IntVector vector1, IntVector vector2) {

		/* define the first element of the square root function */
		double first = vector2.getX() - vector1.getX();

		/* define the second element of the square root function */
		double second = vector2.getY() - vector1.getY();

		/* calculate the square root */
		return Math.sqrt(first * first + second * second);
	}

	/**
	 * This method calculates the Pythagoras distance between two vectors. Simple
	 * multiplication is about 20 times faster than Math.pow()
	 *
	 * @param vector1 the vector1
	 * @param vector2 the vector2
	 * @return the distance between the nodes
	 */
	static double distanceBetween(DoubleVector vector1, DoubleVector vector2) {

		/* define the first element of the square root function */
		double first = vector2.getX() - vector1.getX();

		/* define the second element of the square root function */
		double second = vector2.getY() - vector1.getY();

		/* calculate the square root */
		return Math.sqrt(first * first + second * second);
	}
}
