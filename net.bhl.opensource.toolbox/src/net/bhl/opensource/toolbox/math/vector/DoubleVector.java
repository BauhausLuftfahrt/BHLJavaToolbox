/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math.vector;

/**
 * This class represents a vector consisting of 2 or 3 coordinates.
 *
 * @author Marc.Engelmann
 */
public class DoubleVector {

	private Double xValue, yValue, zValue;

	/**
	 * This method constructs the two dimensional Vector object.
	 *
	 * @param x is the first value
	 * @param y is the second value
	 */
	public DoubleVector(double x, double y) {
		xValue = x;
		yValue = y;
		zValue = null;
	}

	/**
	 * This method constructs the three dimensional Vector object.
	 *
	 * @param x is the first value
	 * @param y is the second value
	 * @param z is the third value
	 */
	public DoubleVector(double x, double y, double z) {
		xValue = x;
		yValue = y;
		zValue = z;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof DoubleVector)) {
			return super.equals(object);
		} else {

			DoubleVector vec = (DoubleVector) object;
			if (zValue == null && vec.getZ() == null) {
				return vec != null && xValue == vec.getX() && yValue == vec.getY();
			} else if (zValue == null && vec.getZ() != null || zValue != null && vec.getZ() == null) {
				return false;
			} else {
				return vec != null && xValue == vec.getX() && yValue == vec.getY() && zValue == vec.getZ();
			}
		}
	}

	/**
	 * This method returns the x value.
	 *
	 * @return the x value
	 */
	public double getX() {
		return xValue;
	}

	/**
	 * This method returns the y value.
	 *
	 * @return the y value
	 */
	public final double getY() {
		return yValue;
	}

	/**
	 * This method returns the z Value.
	 *
	 * @return the z value
	 */
	public final Double getZ() {
		return zValue;
	}

	/**
	 * This method sets the x value.
	 *
	 * @param x the x value
	 */
	public final void setX(double x) {
		xValue = x;
	}

	/**
	 * This method sets the y value.
	 *
	 * @param y the y value
	 */
	public final void setY(double y) {
		yValue = y;
	}

	/**
	 * This method sets the z value.
	 *
	 * @param z the z value
	 */
	public final void setZ(double z) {
		yValue = z;
	}

	@Override
	public String toString() {
		return "(" + xValue + ", " + yValue + ", " + zValue + ")";
	}

	/**
	 * @param vec
	 * @return
	 */
	public static DoubleVector copy(DoubleVector vec) {
		return new DoubleVector(vec.getX(), vec.getY(), vec.getZ());
	}
}
