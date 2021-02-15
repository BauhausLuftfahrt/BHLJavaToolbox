/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math.vector;

/**
 * This class represents an integer vector consisting.
 *
 * @author Marc.Engelmann
 */
public class IntVector {

	private Integer xValue, yValue;

	/**
	 * This method constructs the two dimensional Vector object.
	 *
	 * @param x is the first value
	 * @param y is the second value
	 */
	public IntVector(int x, int y) {
		xValue = x;
		yValue = y;
	}

	@Override
	public boolean equals(Object object) {

		if (!(object instanceof IntVector)) {
			return super.equals(object);
		} else {

			IntVector vec = (IntVector) object;
			return vec != null && xValue == vec.getX() && yValue == vec.getY();
		}
	}

	/**
	 * This method returns the x value.
	 *
	 * @return the x value
	 */
	public int getX() {
		return xValue;
	}

	/**
	 * This method returns the y value.
	 *
	 * @return the y value
	 */
	public int getY() {
		return yValue;
	}

	/**
	 * This method sets the x value.
	 *
	 * @param x the x value
	 */
	public void setX(Integer x) {
		xValue = x;
	}

	/**
	 * This method sets the y value.
	 *
	 * @param y the y value
	 */
	public void setY(Integer y) {
		yValue = y;
	}

	@Override
	public String toString() {
		return "(" + xValue + ", " + yValue + ")";
	}

	/**
	 * @return
	 */
	public DoubleVector toDoubleVector() {
		return new DoubleVector(getX(), getY(), 0);
	}
}
