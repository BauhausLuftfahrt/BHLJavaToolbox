/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * This class is used to rotate an array or part of an array.
 *
 * @author Marc.Engelmann
 *
 */
public class Rotator {

	/**
	 * Round index to point.
	 *
	 * @param index  the index
	 * @param radius the radius
	 * @return the vector
	 */
	private static DoubleVector RoundIndexToPoint(int index, int radius) {
		if (radius == 0) {
			return new DoubleVector(0, 0);
		}
		DoubleVector result = new DoubleVector(-radius, -radius);

		while (index < 0) {
			index = index + radius * 8;
		}
		index = index % (radius * 8);

		int edgeLen = radius * 2;

		if (index < edgeLen) {
			result.setX(result.getX() + index);
		} else if ((index -= edgeLen) < edgeLen) {
			result.setX(radius);
			result.setY(result.getY() + index);
		} else if ((index -= edgeLen) < edgeLen) {
			result.setX(radius - index);
			result.setY(radius);
		} else if ((index -= edgeLen) < edgeLen) {
			result.setY(radius - index);
		}

		return result;
	}

	/**
	 * Rotate45.
	 *
	 * @param array the array
	 * @return the int[][]
	 */
	private static int[][] rotate45(int[][] array) {

		int dim = Math.max(array[0].length, array.length);

		if (dim % 2 == 0) {
			dim += 1;
		}

		int[][] result = new int[dim][dim];

		DoubleVector center = new DoubleVector((result.length - 1) / 2, (result[1].length - 1) / 2);
		DoubleVector center2 = new DoubleVector((array.length - 1) / 2, (array[1].length - 1) / 2);
		for (int r = 0; r <= (dim - 1) / 2; r++) {
			for (int i = 0; i <= r * 8; i++) {

				DoubleVector source = RoundIndexToPoint(i, r);
				DoubleVector target = RoundIndexToPoint(i + r, r);

				if (center2.getX() + source.getX() >= 0 && center2.getY() + source.getY() >= 0
						&& center2.getX() + source.getX() < array.length
						&& center2.getY() + source.getY() < array[1].length) {
					result[BHLMath.toInt(center.getX() + target.getX())][BHLMath.toInt(center.getY()
							+ target.getY())] = array[BHLMath.toInt(center2.getX() + source.getX())][BHLMath
									.toInt(center2.getY() + source.getY())];
				}
			}
		}

		return result;
	}

	/**
	 * Rotate.
	 *
	 * @param degrees the degrees
	 * @param array   the array
	 * @return the int[][]
	 */
	public static int[][] rotate(int degrees, int[][] array) {
		if (degrees == 0 || degrees % 45 != 0) {
			return null;
		}
		for (int i = 0; i < degrees / 45; i++) {
			array = rotate45(array);
		}
		return array;
	}
}
