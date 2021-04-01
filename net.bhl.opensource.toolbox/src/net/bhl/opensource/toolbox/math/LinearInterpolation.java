/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * @author Marc.Engelmann
 *
 */
public interface LinearInterpolation {

	/**
	 * @param y
	 * @param y2
	 * @param y1
	 * @param x2
	 * @param x1
	 * @return
	 */
	static double getXAt(double y, double y2, double y1, double x2, double x1) {

		if (y2 == y1) {
			return (x2 + x1) / 2;
		}

		return x1 + (y - y1) * (x2 - x1) / (y2 - y1);
	}

	/**
	 * @param x
	 * @param y2
	 * @param y1
	 * @param x2
	 * @param x1
	 * @return
	 */
	static double getYAt(double x, double y2, double y1, double x2, double x1) {
		if (x2 == x1) {
			return (y2 + y1) / 2;
		}
		return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
	}

	/**
	 * Perform a linear interpolation calculation.
	 *
	 * @param y2     the upper bound
	 * @param y1     the lower bound
	 * @param deltaX the delta x value
	 * @param x1     the given x value
	 * @return the calculated y value at the position x1
	 */
	static double getYAtX(double y2, double y1, double deltaX, double x1) {

		if (deltaX == 0) {
			return (y2 + y1) / 2;
		}

		// TODO: Compare this function with above one!
		return y2 - (y2 - y1) / deltaX * x1;
	}

	/**
	 * @param x
	 * @param z2
	 * @param z1
	 * @param x2
	 * @param x1
	 * @return
	 */
	static double getZAt(double x, double z2, double z1, double x2, double x1) {

		if (x2 == x1) {
			return (z2 + z1) / 2;
		}

		return z1 + (x - x1) * (z2 - z1) / (x2 - x1);
	}

	/**
	 * This method performs the linear interpolation value using a list as input.
	 * The two surrounding points of the desired x value are determined and a linear
	 * interpolation between these two points is performed.
	 *
	 * @param x    the x value
	 * @param list list of points from which to choose the 2 values for
	 *             interpolation
	 * @return the calculated y value
	 */
	static Double getYUsingClosestPointsAt(double x, List<DoubleVector> list) {

		// Sort out empty list
		if (list.isEmpty()) {
			return null;
		}

		// If there is only one value, the result is this value
		if (list.size() == 1) {
			return list.get(0).getY();
		}

		// Copy the list for manipulation purposes
		List<DoubleVector> cache = new ArrayList<>(list);

		// Sort by distance to demanded value
		Collections.sort(cache, Comparator.comparing(p -> Math.abs(p.getX() - x)));
		DoubleVector closest = cache.get(0);

		// Sort by absolute distance
		Collections.sort(cache, Comparator.comparing(p -> p.getX() - x));
		DoubleVector otherSide;

		// Check if closest point is greater than desired value and not first point
		if (closest.getX() > x && cache.indexOf(closest) != 0 || cache.indexOf(closest) == cache.size() - 1) {

			otherSide = cache.get(cache.indexOf(closest) - 1);

		} else {

			// Other side is the following one
			otherSide = cache.get(cache.indexOf(closest) + 1);
		}

		// Perform regular linear interpolation
		return getYAt(x, otherSide.getY(), closest.getY(), otherSide.getX(), closest.getX());

	}
}
