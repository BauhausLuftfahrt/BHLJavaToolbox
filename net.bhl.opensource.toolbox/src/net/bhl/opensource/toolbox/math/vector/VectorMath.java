/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.math.vector;

/**
 * @author Marc.Engelmann
 *
 */
public class VectorMath {

	/**
	 * @param vector
	 * @param factor
	 * @return
	 */
	public static void scalarMultiply(DoubleVector vector, double factor) {
		vector.setX(vector.getX() * factor);
		vector.setY(vector.getY() * factor);
		if (vector.getZ() != null) {
			vector.setZ(vector.getZ() * factor);
		}
	}

	/**
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static DoubleVector substract(DoubleVector vec1, DoubleVector vec2) {
		return new DoubleVector(vec1.getX() - vec2.getX(), vec1.getY() - vec2.getY(), vec1.getZ() - vec2.getZ());
	}

	/**
	 * This only makes sense if the vector represents a vector, not a point!
	 *
	 * @param vector
	 * @return
	 */
	public static double getLength(DoubleVector vector) {
		return Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
	}
}
