/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math.random;

import java.util.Random;

/**
 * The Class RandomHelper.
 *
 * @author Marc.Engelmann, Michael.Schmidt
 *
 */
public interface RandomHelper {

	/**
	 * @return
	 */
	static int randomInt() {
		return randomValue(0, Integer.MAX_VALUE);
	}

	/**
	 * Note that the 2 integer values have the following meanings: first integer is
	 * the lower bound, second integer is the upper bound. The upper bound is never
	 * reached! Result is part of: [l,u[
	 *
	 *
	 * @param lowerBound defines the lower end of the random number.
	 * @param upperBound defines the upper bound. <b><i>Note that the upper bound
	 *                   itself is never reached!</i></b>
	 * @return returns the random double generated from the parameters above.
	 */
	static int randomValue(int lowerBound, int upperBound) {
		return new Random().nextInt(upperBound - lowerBound) + lowerBound;
	}
}
