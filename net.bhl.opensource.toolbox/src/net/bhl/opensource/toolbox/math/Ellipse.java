/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * @author Marc.Engelmann
 * @since 03.12.2018
 *
 */
public class Ellipse {

	final public double semiWidth;
	final public double semiHeight;

	/**
	 * @return
	 */
	public double aspectRatio() {
		return semiHeight / semiWidth;
	}

	/**
	 * @return
	 */
	public double equivalentDiameter() {
		return 2.0 * Math.sqrt(semiWidth * semiHeight);
	}

	/**
	 * Circumfence Approximation according to Ramujan.
	 *
	 * Relative error depending on eccentricity
	 *
	 * 0,0000 ≤ error ≤ 0,8820 < 10−9
	 *
	 * 0,8820 < error ≤ 0,9242 < 10−8
	 *
	 * 0,9242 < error ≤ 0,9577 < 10−7
	 *
	 * 0,9577 < error ≤ 0,9812 < 10−6
	 *
	 * 0,9812 < error ≤ 0,9944 < 10−5
	 *
	 * 0,9944 < error ≤ 0,9995 < 10−4
	 *
	 * 0,9995 < error ≤ 1,0000 < 0,000403
	 *
	 * @return
	 */
	public double circumfenceApproximation() {
		return Math.PI * (semiWidth + semiHeight
				+ 3.0 * Math.pow(semiWidth - semiHeight, 2.0) / (10.0 * (semiHeight + semiWidth) + Math
						.sqrt(Math.pow(semiWidth, 2.0) + 14.0 * semiWidth * semiHeight + Math.pow(semiHeight, 2.0))));
	}

	/**
	 * @return
	 */
	public double area() {
		return semiWidth * semiHeight * Math.PI;
	}

	/**
	 * @return
	 */
	public boolean isCircular() {
		return semiWidth == semiHeight;
	}

	/**
	 * @return
	 */
	public double eccentricity() {
		return Math.sqrt(Math.pow(semiMajorAxis(), 2.0) - Math.pow(semiMinorAxis(), 2.0)) / semiMajorAxis();
	}

	/**
	 * @param semiWidth
	 * @param semiHeight
	 */
	public Ellipse(double semiWidth, double semiHeight) {
		this.semiWidth = semiWidth;
		this.semiHeight = semiHeight;
	}

	/**
	 * @return
	 */
	public double width() {
		return semiWidth * 2.0;
	}

	/**
	 * @return
	 */
	public double height() {
		return semiHeight * 2.0;
	}

	/**
	 * @return
	 */
	public double semiMajorAxis() {
		return Math.max(semiHeight, semiWidth);
	}

	/**
	 * @return
	 */
	public double semiMinorAxis() {
		return Math.min(semiHeight, semiWidth);
	}

	/**
	 * Get the position of the x value at a specific y coordinate of an ellipse. The
	 * center of the ellipse is the origin of the coordinate system. <b>Note that
	 * due to the shape of an ellipse there will always be two possible solutions,
	 * the positive and the negative one</b>
	 *
	 *
	 * @param y
	 * @param xDimension
	 * @param yDimension
	 * @return
	 */
	public double getXAt(double y) {
		return semiWidth * Math.sqrt(1 - Math.pow(y / semiHeight, 2));
	}

	/**
	 *
	 * Get the position of the y value at a specific x coordinate of an ellipse. The
	 * center of the ellipse is the origin of the coordinate system. <b>Note that
	 * due to the shape of an ellipse there will always be two possible solutions,
	 * the positive and the negative one</b>
	 *
	 * @param x
	 * @param xDimension
	 * @param yDimension
	 * @return
	 */
	public double getYAt(double x) {
		return semiHeight * Math.sqrt(1 - Math.pow(x / semiWidth, 2));
	}

	/**
	 * Get the coordinate of the ellipse considering an angle.
	 *
	 * @param angle
	 * @return
	 */
	public DoubleVector getPointAtAngle(int angle) {

		// TODO!
		return null;

	}

	@Override
	public String toString() {
		return "Ellipse (semi width = " + semiWidth + ", semi height = " + semiHeight + ")";
	}

}
