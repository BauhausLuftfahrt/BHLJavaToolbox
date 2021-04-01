/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math.gaussian;

import java.util.Random;

/**
 * The Class GaussianRandom.
 *
 * @author Marc.Engelmann
 *
 */
public interface Gaussian {

	// Calculates the sigma values for possible values of the GaussianRandom class.
	// he average value is the peak value of the Gaussian distribution. The option
	// is one of the following values: 50%, 90%, 95%, 99%

	public interface Sigma {

		double PERCENT_99 = 2.576;
		double PERCENT_95 = 1.960;
		double PERCENT_90 = 1.645;
		double PERCENT_50 = 0.675;

	}

	/**
	 * Use this function to generate a value according to gaussian normal
	 * distribution.
	 *
	 * @param average   is the average value of the distribution
	 * @param gauss     is the option you want to choose.
	 * @param deviation is the deviation at the chosen option.
	 * @return the double
	 */
	static double random(double average, double sigma, double deviation) {

		double result = new Random().nextGaussian() * (deviation / sigma) + average;

		if (result < 0) {
			return 0;
		}

		return result;
	}

	/**
	 * @param average
	 * @param gauss
	 * @param percentageDeviation
	 * @return
	 */
	static double randomPercentage(double average, double sigma, double percentageDeviation) {

		double result = new Random().nextGaussian() * (average * percentageDeviation / sigma) + average;

		if (result < 0) {
			return 0;
		}

		return result;
	}
}
