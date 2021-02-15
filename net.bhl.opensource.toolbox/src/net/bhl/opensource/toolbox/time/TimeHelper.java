/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.time;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

/**
 * Supports formatting various time amounts into HH:mm:ss format.
 *
 * @author Marc.Engelmann, Michael.Schmidt
 *
 */
public interface TimeHelper {

	/**
	 * Delivers the current time in HH:mm:ss format.
	 *
	 * @return the current time as HH:mm:ss String
	 */
	static String getCurrentTimeOfDay() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

	/**
	 * Delivers the current time in HH:mm:ss format.
	 *
	 * @return the current time as HH:mm:ss String
	 */
	static String now(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

//	/**
//	 * Formats a double time to a time string
//	 *
//	 * @param minuteDouble the minutes in double format
//	 * @return the time string in MM:SS
//	 */
//	@Deprecated
//	static String toTimeInMinSecs(double minuteDouble) {
//
//		int minutes = (int) (minuteDouble);
//		int seconds = (int) ((minuteDouble - minutes) * 60.0) - minutes;
//
//		if (seconds < 0) {
//			seconds = 0;
//		}
//
//		if (minutes < 10 && seconds < 10) {
//			return "0" + minutes + ":" + "0" + seconds;
//		} else if (minutes < 10) {
//			return "0" + minutes + ":" + seconds;
//		} else if (seconds < 10) {
//			return minutes + ":" + "0" + seconds;
//		} else {
//			return minutes + ":" + seconds;
//		}
//	}

	/**
	 * @param seconds
	 * @return
	 */
	static String secondsToTime(double seconds) {
		return String.format("%02d:%02d", (int) seconds / 60, (int) seconds % 60);
	}

	/**
	 * Formats time amount in seconds into HH:mm:ss format.
	 *
	 * @param seconds the number of seconds to be formatted
	 * @return the converted HH:mm:ss String
	 */
	static String toTimeOfDay(double seconds) {
		if (seconds >= 0 && seconds <= 86399) {
			return TimeHelper.toTimeOfDay(Math.round(seconds));
		} else {
			return "ERROR IN TimeHelper.java";
		}
	}

	/**
	 * Formats time amount in seconds into HH:mm:ss format.
	 *
	 * @param seconds the number of seconds to be formatted
	 * @return the converted HH:mm:ss String
	 */
	static String toTimeOfDay(int seconds) {
		return TimeHelper.toTimeOfDay((long) seconds);
	}

	/**
	 * Formats time amount in seconds into HH:mm:ss format.
	 *
	 * @param seconds the number of seconds to be formatted
	 * @return the converted HH:mm:ss String
	 */
	static String toTimeOfDay(long seconds) {
		return LocalTime.ofSecondOfDay(seconds).toString();
	}
}