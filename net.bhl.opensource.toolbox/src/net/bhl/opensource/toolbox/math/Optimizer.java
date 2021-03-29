/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math;

import java.util.function.Function;

/**
 *
 * Optimize a given function. See demo method for example.
 *
 * @author Marc.Engelmann
 * @since 03.12.2018
 *
 */

public class Optimizer {

	public static int INTERVALS = 50;
	public static int LOOPS = 20;

	public static void main(String[] args) {
		new Optimizer().demo();
	}

	/**
	 * This is the example.
	 *
	 * @param args
	 */
	public void demo() {

		// Create a new function
		Function<Double, Double> y = x -> Math.pow(x - 1, 2);

		// Optimize and store result within bounds [-2,2]
		double xOptimal = minimize(y, -2, 2);

		// Apply result by using func.apply()
		System.out.println("Minimum y = " + y.apply(xOptimal) + " at x = " + xOptimal);
	}

	/**
	 * Dichotomy Optimizer. Define the maximum and minimum x in order for the solver
	 * to operate properly.
	 *
	 * @param function
	 * @param xMin
	 * @param xMax
	 * @return
	 */
	public double minimize(Function<Double, Double> function, double xLowerBound, double xUpperBound) {

		double xMin = xLowerBound, xMax = xUpperBound;

		// Optimization loops
		for (int i = 0; i < LOOPS; i++) {

			int deltaXLower = INTERVALS, deltaXUpper = INTERVALS;

			double yMin = Integer.MAX_VALUE;
			double yMinOption = Integer.MAX_VALUE;

			for (int interval = 0; interval < INTERVALS; interval++) {

				// Get a value for x between xMin and xMax
				double x = interval * (xMin - xMax) / INTERVALS + xMax;

				// Apply to the function
				double y = function.apply(x);

				// if the current y is smaller than the last one
				if (y < yMin) {

					deltaXUpper = deltaXLower;
					deltaXLower = interval;

					yMinOption = yMin;
					yMin = y;

				} else if (y < yMinOption) {

					deltaXUpper = interval;
					yMinOption = y;
				}
			}

			// Update the interval where optimal x has to be
			if (deltaXLower < deltaXUpper) {

				xMax = (deltaXLower > 0 ? deltaXLower - 1 : INTERVALS) * (xMin - xMax) / INTERVALS + xMax;
				xMin = (deltaXLower < INTERVALS ? deltaXUpper + 1 : INTERVALS) * (xMin - xMax) / INTERVALS + xMax;

			} else {

				xMax = (deltaXUpper > 0 ? deltaXUpper - 1 : INTERVALS) * (xMin - xMax) / INTERVALS + xMax;
				xMin = (deltaXLower < INTERVALS ? deltaXLower + 1 : INTERVALS) * (xMin - xMax) / INTERVALS + xMax;
			}
		}

		// Check if the minimizer reached one of the bounds during optimization.
		// This means that no minimum is found and one of the bounds is the minimum
		if (xMax == xUpperBound || xMin == xLowerBound) {

			System.err.println("Optimizer could not find a minimum within the defined bounds. Bound is minimum.");

			// Check which of the bounds is the minimum
			if (function.apply(xUpperBound) < function.apply(xLowerBound)) {

				// Apply result
				function.apply(xUpperBound);
				return xUpperBound;

			} else {

				// Apply result
				function.apply(xLowerBound);
				return xLowerBound;
			}

		} else {

			// Apply final result
			function.apply(Math.min(xMax, xMin));
			return Math.min(xMax, xMin);
		}
	}
}
