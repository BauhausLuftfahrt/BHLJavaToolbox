/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.time;

import net.bhl.opensource.toolbox.math.BHLMath;

/**
 * This class represents a stop watch.
 *
 * @author Marc.Engelmann, Michael.Schmidt
 */

public class StopWatch {

	private long startTime, stopTime;
	private boolean running;

	/**
	 * This method constructs the StopWatch and initializes the parameters.
	 */
	public StopWatch() {
		stopTime = 0;
		startTime = System.currentTimeMillis();
		running = true;
	}

	/**
	 * Get the time in an adapted format with unit-
	 *
	 * @return
	 */
	public String getAdaptedTime() {
		long elapsed = getElapsedTime();

		// If smaller than 1ms
		if (elapsed < 1.0) {
			return "<1 ms";
		}

		// If smaller than one second
		else if (elapsed < 1000) {
			return elapsed + " ms";
		}

		// If smaller than 2 minutes
		else if (elapsed < 120 * 1000) {
			return Math.round(elapsed / 1000.0 * 10.0) / 10.0 + " s";
		}

		// If time is smaller than 90 minutes
		else if (elapsed < 90 * 60 * 1000) {
			return BHLMath.toInt(Math.floor(elapsed / 1000.0 / 60.0)) + ":" + BHLMath.round(elapsed / 1000.0 % 60.0, 1);
		}

		// Else return hours
		return BHLMath.toInt(Math.floor(elapsed / 1000.0 / 60.0 / 60.0)) + ":"
				+ BHLMath.toInt(BHLMath.round(elapsed / 1000.0 % 3600.0, 1) / 60) + ":"
				+ BHLMath.toInt(BHLMath.round(elapsed / 1000.0 % 60.0, 1)) + " hrs";
	}

	/**
	 * This method returns the elapsed time in milliseconds.
	 *
	 * @return the elapsed time in milliseconds
	 */
	public long getElapsedTime() {
		return (running ? System.currentTimeMillis() : stopTime) - startTime;
	}

	/**
	 * This method returns the elapsed time in seconds.
	 *
	 * @return the elapsed time in seconds
	 */
	public double getElapsedTimeSecs() {
		return getElapsedTime() / 1000.0;
	}

	/**
	 * This method stops the time stopping.
	 */
	public void stop() {
		stopTime = System.currentTimeMillis();
		running = false;
	}
}