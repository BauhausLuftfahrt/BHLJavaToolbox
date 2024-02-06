package net.bhl.opensource.toolbox.math;

/**
 * A Unit conversion helper class.
 *
 * @author Michael.Schmidt
 * @author Marc.Engelmann
 *
 */
public class Converter {

	/**
	 * @param number
	 * @return
	 */
	static boolean isInteger(double number) {
		return Math.ceil(number) == Math.floor(number);
	}

	/**
	 * @param perMinutes
	 * @return
	 */
	public static double perMinuteToPerSecond(double perMinutes) {
		return perMinutes / 60;
	}

	/**
	 * @param value
	 * @param digits
	 * @return
	 */
	public static double round(double value, int digits) {
		return Math.round(value * Math.pow(10.0, digits)) / Math.pow(10.0, digits);
	}

	/**
	 * @param value
	 * @return
	 */
	static int toInt(double value) {
		return (int) Math.round(value);
	}
}
