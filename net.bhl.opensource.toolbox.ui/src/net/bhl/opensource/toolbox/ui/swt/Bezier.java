/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * Interpolates given points by a bezier curve. The first and the last two
 * points are interpolated by a quadratic bezier curve; the other points by a
 * cubic bezier curve.
 *
 * Let p a list of given points and b the calculated bezier points, then one get
 * the whole curve by:
 *
 * sharedPath.moveTo(p[0]) sharedPath.quadTo(b[0].x, b[0].getY(), p[1].x,
 * p[1].getY());
 *
 * for(int i = 2; i < p.length - 1; i++ ) { Point b0 = b[2*i-3]; Point b1 =
 * b[2*i-2]; sharedPath.curveTo(b0.x, b0.getY(), b1.x, b1.getY(), p[i].x,
 * p[i].getY()); }
 *
 * sharedPath.quadTo(b[b.length-1].x, b[b.length-1].getY(), p[n - 1].x, p[n -
 * 1].getY());
 *
 * @author krueger
 */

/**
 * @author Marc.Engelmann
 * @since 15.01.2019
 *
 */

public class Bezier {

	private static final float AP = 0.5f;
	private DoubleVector[] bPoints;

	/**
	 * @param gc
	 * @param deltaX
	 * @param deltaY
	 * @param points
	 * @return
	 */
	public static Path toPath(DoubleVector... points) {

		DoubleVector[] bezierPoints = new Bezier(points).getPoints();

		int n = points.length;

		Path path = new Path(Display.getDefault());

		path.moveTo((float) points[0].getX(), (float) points[0].getY());
		path.quadTo((float) bezierPoints[0].getX(), (float) bezierPoints[0].getY(), (float) points[1].getX(),
				(float) points[1].getY());

		for (int indexNew = 2; indexNew < points.length - 1; indexNew++) {
			DoubleVector b0 = bezierPoints[2 * indexNew - 3];
			DoubleVector b1 = bezierPoints[2 * indexNew - 2];
			path.cubicTo((float) b0.getX(), (float) b0.getY(), (float) b1.getX(), (float) b1.getY(),
					(float) points[indexNew].getX(), (float) points[indexNew].getY());
		}

		path.quadTo((float) bezierPoints[bezierPoints.length - 1].getX(),
				(float) bezierPoints[bezierPoints.length - 1].getY(), (float) points[n - 1].getX(),
				(float) points[n - 1].getY());

		return path;
	}

	/**
	 * Creates a new Bezier curve.
	 *
	 * @param points
	 */
	public Bezier(DoubleVector[] points) {
		int n = points.length;
		if (n < 3) {
			// Cannot create bezier with less than 3 points
			return;
		}
		bPoints = new DoubleVector[2 * (n - 2)];
		double paX, paY;
		double pbX = points[0].getX();
		double pbY = points[0].getY();
		double pcX = points[1].getX();
		double pcY = points[1].getY();
		for (int i = 0; i < n - 2; i++) {
			paX = pbX;
			paY = pbY;
			pbX = pcX;
			pbY = pcY;
			pcX = points[i + 2].getX();
			pcY = points[i + 2].getY();
			double abX = pbX - paX;
			double abY = pbY - paY;
			double acX = pcX - paX;
			double acY = pcY - paY;
			double lac = Math.sqrt(acX * acX + acY * acY);
			acX = acX / lac;
			acY = acY / lac;

			double proj = abX * acX + abY * acY;
			proj = proj < 0 ? -proj : proj;
			double apX = proj * acX;
			double apY = proj * acY;

			double p1X = pbX - AP * apX;
			double p1Y = pbY - AP * apY;
			bPoints[2 * i] = new DoubleVector((int) p1X, (int) p1Y);

			acX = -acX;
			acY = -acY;
			double cbX = pbX - pcX;
			double cbY = pbY - pcY;
			proj = cbX * acX + cbY * acY;
			proj = proj < 0 ? -proj : proj;
			apX = proj * acX;
			apY = proj * acY;

			double p2X = pbX - AP * apX;
			double p2Y = pbY - AP * apY;
			bPoints[2 * i + 1] = new DoubleVector((int) p2X, (int) p2Y);
		}
	}

	/**
	 * Returns the calculated bezier points.
	 *
	 * @return the calculated bezier points
	 */
	public DoubleVector[] getPoints() {
		return bPoints;
	}

	/**
	 * Returns the number of bezier points.
	 *
	 * @return number of bezier points
	 */
	public int getPointCount() {
		return bPoints.length;
	}

	/**
	 * Returns the bezier points at position i.
	 *
	 * @param i
	 * @return the bezier point at position i
	 */
	public DoubleVector getPoint(int i) {
		return bPoints[i];
	}

}