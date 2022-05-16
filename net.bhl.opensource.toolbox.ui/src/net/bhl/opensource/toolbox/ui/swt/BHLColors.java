/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.ui.swt;

import java.util.Random;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

import net.bhl.opensource.toolbox.math.BHLMath;

/**
 * This file stores commonly used colors.
 *
 * @author Marc.Engelmann
 *
 */
public interface BHLColors {

	// Maximum possible color
	int MAX = 255;

	// Minimum possible color
	int MIN = 0;

	// The color displaying device/component.
	Device d = Display.getCurrent();

	// Black to white
	Color BLACK = GREY(0);
	Color BACKGROUND = GREY(240);
	Color WHITE = GREY(MAX);

	Color RED_DARK = new Color(d, 200, MIN, MIN);
	Color RED = new Color(d, MAX, MIN, MIN);
	Color RED_LIGHT = new Color(d, MAX, 51, 51);

	Color ORANGE_LIGHT = new Color(d, MAX, 162, 76);
	Color ORANGE = new Color(d, MAX, 122, MIN);
	Color ORANGE_DARK = new Color(d, 204, 97, MIN);

	Color YELLOW = new Color(d, MAX, MAX, MIN);

	Color GREEN_DARK = new Color(d, MIN, 117, 37);
	Color GREEN = new Color(d, MIN, MAX, MIN);
	Color GREEN_LIGHT = new Color(d, 122, MAX, MIN);

	Color BLUE = new Color(d, MIN, MIN, MAX);
	Color BLUE_LIGHT = new Color(d, 66, 244, 232);
	Color BLUE_VERY_LIGHT = new Color(d, 51, 153, MAX);
	Color BLUE_SAIL = new Color(d, 174, 209, 237);
	Color BLUE_BAYOUX = new Color(d, 99, 118, 125);
	Color BLUE_PRUSSIAN = new Color(d, 2, 32, 70);

	Color PINK_LIGHT = new Color(d, MAX, 153, MAX);
	Color PURPLE = new Color(d, 97, MIN, MAX);

	Color BROWN_GREY = new Color(d, 127, 112, 99);
	Color BROWN_DARK = new Color(d, 127, 61, MIN);

	Color SALMON = new Color(d, MAX, 229, 204);

	/**
	 * Gray shade
	 *
	 * @param value
	 * @return
	 */
	static Color GREY(int value) {

		if (value < MIN) {
			return BLACK;
		} else if (value > MAX) {
			return WHITE;
		}

		return new Color(d, value, value, value);
	}

	/**
	 * Generate a random color.
	 *
	 * @return random SWT Color
	 */
	static Color getRandomColor() {
		Random r = new Random();
		return new Color(d, r.nextInt(256), r.nextInt(256), r.nextInt(256));
	}

	/**
	 * Generate a random color.
	 *
	 * @return random SWT Color
	 */
	static Color getRandomGrey() {
		Random r = new Random();
		int grey = r.nextInt(256);
		return new Color(d, grey, grey, grey);
	}

	/**
	 * Generate a random color.
	 *
	 * @return random SWT Color
	 */
	static Color getRandomGrey(int min, int max) {
		Random r = new Random();
		int grey = r.nextInt(max + 1 - min) + min;
		return new Color(d, grey, grey, grey);
	}

	/**
	 * Generate a color from hex code
	 *
	 * @param hex
	 * @return
	 */
	static Color fromHex(String hex) {
		try {
			java.awt.Color c = java.awt.Color.decode(hex);
			return new Color(d, c.getRed(), c.getGreen(), c.getBlue());

		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Get hex code of a color
	 *
	 * @param color
	 */
	static String toHex(Color color) {

		java.awt.Color awtColor = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());

		String hexColor = Integer.toHexString(awtColor.getRGB() & 0xffffff);
		if (hexColor.length() < 6) {
			hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
		}
		return "#" + hexColor;
	}

	/**
	 * Get the relative color of the gradient ranging from green via yellow, red and
	 * pink to blue. The maximum value will be blue whereas a value of 0 will be
	 * green.
	 *
	 * @param value
	 * @return
	 */
	static Color getGreenToBlueGradient(double current, double maximum) {

		int value = BHLMath.toInt(current * BHLColors.MAX * 4.0 / maximum);

		if (value <= MAX) {

			// Green to Yellow
			return new Color(d, value, MAX, MIN);

		} else if (value <= MAX * 2) {

			// Yellow to Red
			return new Color(d, MAX, 2 * MAX - value, MIN);

		} else if (value <= MAX * 3) {

			// Red to Pink
			return new Color(d, MAX, MIN, value - 2 * MAX);

		} else {

			// Pink to Blue
			return new Color(d, 4 * MAX - value, 0, MAX);
		}
	}

	/**
	 * Get the relative color of the gradient ranging from green via yellow and red
	 * to pink. The maximum value will be pink whereas a value of 0 will be green.
	 *
	 * @param value
	 * @return
	 */
	static Color getGreenToPinkGradient(double current, double maximum) {

		int value = BHLMath.toInt(current * BHLColors.MAX * 3.0 / maximum);

		if (value <= MAX) {

			// Green to Yellow
			return new Color(d, value, MAX, 0);

		} else if (value <= MAX * 2) {

			// Yellow to Red
			return new Color(d, MAX, 2 * MAX - value, MIN);

		} else {

			// Red to Pink
			return new Color(d, MAX, MIN, value - 2 * MAX);
		}
	}

	/**
	 * Get the relative color of the gradient ranging from green via yellow to red.
	 * The maximum value will be red whereas a value of 0 will be green.
	 *
	 * @param value
	 * @return
	 */
	static Color getGreenToRedGradient(double current, double maximum) {

		int value = BHLMath.toInt(current * BHLColors.MAX * 2.0 / maximum);

		if (value <= MAX) {

			// Green to Yellow
			return new Color(d, value, MAX, MIN);

		} else {

			// Yellow to Red
			return new Color(d, MAX, 2 * MAX - value, MIN);
		}
	}

	/**
	 * Check the input color (e.g. representing a background color) and return
	 * either a foreground color (black or white) depending on better contrast and
	 * readability.
	 *
	 * @param color the background color
	 * @return the most readable foreground color
	 */
	static Color adaptToBackground(Color color) {
		if (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114 < 160) {
			return BHLColors.WHITE;
		} else {
			return BHLColors.BLACK;
		}
	}
}
