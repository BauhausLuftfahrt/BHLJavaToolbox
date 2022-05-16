/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

import net.bhl.opensource.toolbox.math.BHLMath;
import net.bhl.opensource.toolbox.math.vector.DoubleVector;
import net.bhl.opensource.toolbox.math.vector.IntVector;
import net.bhl.opensource.toolbox.ui.swt.BHLCanvas.Plane;

/**
 * @author Marc.Engelmann
 * @since 10.01.2019
 *
 */
public interface BHLGC {

	// TODO: only draw what is inside the view!

	int CHART_HEIGHT = 15;
	int LABEL_MARGIN = 1;

	/**
	 * @param e
	 * @param ratios
	 * @param labels
	 * @param colors
	 */
	static void drawChart(PaintEvent e, Double[] ratios, String[] labels, Color[] colors) {
		privateDrawChart(e, Arrays.asList(ratios), Arrays.asList(labels), Arrays.asList(colors));
	}

	/**
	 * @param e
	 * @param ratios
	 * @param labels
	 */
	static void drawChart(PaintEvent e, Double[] ratios, String[] labels) {
		privateDrawChart(e, Arrays.asList(ratios), Arrays.asList(labels), null);
	}

	/**
	 * Draw a bar chart
	 *
	 * @param e
	 * @param ratios
	 * @param labels
	 */
	static void privateDrawChart(PaintEvent e, List<Double> ratios, List<String> labels, List<Color> colors) {

		// Check if they are not equally large
		if (ratios.size() != labels.size()) {
			return;
		}

		setQuality(e.gc, false);
		e.gc.setFont(new Font(Display.getCurrent(), "Arial", 7, SWT.NORMAL));

		int position = 0;

		for (String titleLabel : labels) {

			double ratio = ratios.get(labels.indexOf(titleLabel));

			if (ratio > 0) {

				Color color = BHLColors.getRandomColor();

				if (colors != null) {
					color = colors.get(labels.indexOf(titleLabel));
				}

				e.gc.setBackground(color);

				e.gc.fillRectangle(position, 0, BHLMath.toInt(e.width * ratio), CHART_HEIGHT);

				e.gc.setForeground(BHLColors.adaptToBackground(color));
				e.gc.drawText(titleLabel,
						BHLMath.toInt(position + e.width * ratio / 2.0 - e.gc.textExtent(titleLabel).x / 2.0),
						BHLMath.toInt(CHART_HEIGHT / 2.0 - e.gc.textExtent(titleLabel).y / 2.0));
				e.gc.setForeground(BHLColors.BLACK);

				String valueLabel = new DecimalFormat("#.#").format(ratio * 100.0) + "%";
				e.gc.setBackground(BHLColors.BACKGROUND);
				e.gc.drawText(valueLabel,
						BHLMath.toInt(position + e.width * ratio / 2.0 - e.gc.textExtent(valueLabel).x / 2.0),
						CHART_HEIGHT + LABEL_MARGIN);
			}

			position += e.width * ratio;
		}
	}

	/**
	 * Draw function.
	 *
	 * @param e        the e
	 * @param store    the store
	 * @param gender   the Gender
	 * @param steps    the steps
	 * @param min      the min
	 * @param maximumX the max
	 */
	static void drawGraph(PaintEvent e, Color color, int[] values) {

		setQuality(e.gc, false);

		double maximumY = Arrays.stream(values).max().orElse(0);
		double i = 0, x1 = 0, y1 = CHART_HEIGHT, x2 = 0, y2 = 0;

		e.gc.setLineWidth(2);

		e.gc.setForeground(color);

		for (int value : values) {

			x2 = i * e.width / values.length;
			y2 = CHART_HEIGHT - CHART_HEIGHT * value / maximumY;

			e.gc.drawLine(BHLMath.toInt(x1), BHLMath.toInt(y1), BHLMath.toInt(x2), BHLMath.toInt(y2));

			x1 = x2;
			y1 = y2;
			i++;
		}
	}

	/**
	 * Draw an arrow with label
	 *
	 * @param gc
	 * @param x
	 * @param y
	 * @param length
	 * @param tipsize
	 * @param vertical
	 * @param label
	 */
	static void drawArrow(GC gc, Color color, IntVector origin, double lengthd, double tipsized, boolean vertical,
			String label, boolean invert, int linewidth) {

		int x = origin.getX();
		int y = origin.getY();

		int length = BHLMath.toInt(lengthd);
		int tipsize = BHLMath.toInt(tipsized);

		Color backgroundCache = gc.getBackground();
		Color foregroundCache = gc.getForeground();

		setStyle(gc, color, color, 255);
		gc.setLineWidth(BHLMath.toInt(linewidth));

		int[] triangle;
		int[] line;

		if (vertical) {
			triangle = new int[] { x - tipsize, y + length, x + tipsize, y + length, x, y + tipsize + length };
			line = new int[] { x, y, x, y + length };

			if (invert) {
				triangle = new int[] { x - tipsize, y - length, x + tipsize, y - length, x, y - tipsize - length };
				line = new int[] { x, y, x, y - length };
			}

			if (label != null) {
				gc.drawString(label, x - gc.textExtent(label).x - 2,
						y + BHLMath.toInt((length + tipsize + gc.textExtent(label).y) / 2.0 * (invert ? -1.0 : 1.0)),
						true);
			}

		} else {

			triangle = new int[] { x + length, y - tipsize, x + length + tipsize, y, x + length, y + tipsize };
			line = new int[] { x, y, x + length, y };

			if (invert) {
				triangle = new int[] { x - length, y - tipsize, x - tipsize - length, y, x - length, y + tipsize };
				line = new int[] { x, y, x - length, y };
			}

			if (label != null) {
				gc.drawString(label,
						x + BHLMath.toInt((length + tipsize - gc.textExtent(label).x) / 2.0 * (invert ? -1.0 : 1.0)), y,
						true);
			}
		}

		gc.drawPolygon(line);
		gc.fillPolygon(triangle);
		gc.setBackground(BHLColors.BACKGROUND);

		// Reset UI
		gc.setForeground(foregroundCache);
		gc.setBackground(backgroundCache);
		gc.setAlpha(255);
	}

	/**
	 * @param gc
	 * @param text
	 * @param x
	 * @param y
	 */
	static void drawCenteredText(GC gc, BHLCanvas canvas, String text, DoubleVector containerSize,
			DoubleVector containerPosition, boolean adaptTextColor) {

		// Text
		if (text != null) {

			if (text.isEmpty()) {
				return;
			}

			int margin = 8;

			while (gc.textExtent(text).x > containerSize.getX() * canvas.uiScale - margin) {

				if (text.length() < 4) {
					break;
				}

				if (text.contentEquals("..")) {
					break;
				}

				text = text.substring(0, text.length() - 3) + "..";
			}

			// Cache foreground color
			Color foregroundCache = gc.getForeground();

			if (adaptTextColor) {
				// Adapt foreground color to background
				gc.setForeground(BHLColors.adaptToBackground(gc.getBackground()));
			}

			// Draw text
			gc.drawText(text, canvas.uiOffset.getX() + BHLMath
					.toInt(((canvas.getPlane() == Plane.YZ ? containerPosition.getY() : containerPosition.getX())
							+ (canvas.getPlane() == Plane.YZ ? containerSize.getY() : containerSize.getX()) / 2.0)
							* canvas.uiScale * (canvas.isXInverted() ? -1 : 1))
					- BHLMath.toInt(gc.textExtent(text).x / 2.0),
					canvas.uiOffset.getY() + BHLMath.toInt(
							((canvas.getPlane() == Plane.XY ? containerPosition.getY() : containerPosition.getZ())
									+ (canvas.getPlane() == Plane.XY ? containerSize.getY() : containerSize.getZ())
											/ 2.0)
									* canvas.uiScale * (canvas.isYInverted() ? -1 : 1))
							- BHLMath.toInt(gc.textExtent(text).y / 2.0),
					true);

			if (adaptTextColor) {
				gc.setForeground(foregroundCache);
			}
		}
	}

	/**
	 * @param fill
	 * @param gc
	 * @param canvas
	 * @param position
	 * @param size
	 */
	static void paintRoundedRect(boolean fill, GC gc, BHLCanvas canvas, DoubleVector position, DoubleVector size,
			int radius) {

		int x = canvas.uiOffset.getX()
				+ BHLMath.toInt((canvas.getPlane() == Plane.YZ ? position.getY() : position.getX()) * canvas.uiScale
						* (canvas.isXInverted() ? -1 : 1));
		int y = canvas.uiOffset.getY()
				+ BHLMath.toInt((canvas.getPlane() == Plane.XY ? position.getY() : position.getZ()) * canvas.uiScale)
						* (canvas.isYInverted() ? -1 : 1);
		int width = BHLMath.toInt((canvas.getPlane() == Plane.YZ ? size.getY() : size.getX()) * canvas.uiScale
				* (canvas.isXInverted() ? -1 : 1));
		int height = BHLMath.toInt((canvas.getPlane() == Plane.XY ? size.getY() : size.getZ()) * canvas.uiScale
				* (canvas.isYInverted() ? -1 : 1));

		if (radius == 0) {
			if (fill) {
				gc.fillRectangle(x, y, width, height);
			} else {
				gc.drawRectangle(x, y, width, height);
			}
		} else {
			if (fill) {
				gc.fillRoundRectangle(x, y, width, height, radius, radius);
			} else {
				gc.drawRoundRectangle(x, y, width, height, radius, radius);
			}
		}
	}

	/**
	 *
	 * @param gc
	 * @param text
	 * @param x
	 * @param y
	 */
	static void paintRect(boolean fill, GC gc, BHLCanvas canvas, DoubleVector position, DoubleVector size) {
		paintRoundedRect(fill, gc, canvas, position, size, 0);
	}

	/**
	 * @param statusList
	 */
	static void showStatus(GC gc, List<String> statusList, int x, int y) {

		if (statusList == null) {
			return;
		}

		if (statusList.isEmpty()) {
			return;
		}

		int gap = gc.textExtent(statusList.get(0)).y;

		for (String str : statusList) {
			gc.drawString(str, x, y + statusList.indexOf(str) * gap, true);
		}
	}

	/**
	 * Activate highest graphics settings
	 *
	 * @param gc
	 */
	static void setQuality(GC gc, boolean off) {
		gc.setAntialias(off ? SWT.OFF : SWT.ON);
		gc.setTextAntialias(off ? SWT.OFF : SWT.ON);
		gc.setInterpolation(off ? SWT.OFF : SWT.HIGH);
	}

	/**
	 * Apply a new style.
	 *
	 * @param gc
	 * @param foreground
	 * @param background
	 * @param alpha
	 */
	static void setStyle(GC gc, Color foreground, Color background, Integer alpha) {

		if (gc != null) {

			if (foreground != null) {
				gc.setForeground(foreground);
			}

			if (background != null) {
				gc.setBackground(background);
			}

			if (alpha != null) {
				gc.setAlpha(alpha);
			}
		}
	}

	/**
	 * @param canvas
	 * @param points
	 * @return
	 */
	static void paintBezier(boolean fill, GC gc, BHLCanvas canvas, DoubleVector... points) {

		if (points.length <= 2) {
			drawLine(gc, canvas, points);
			return;
		}

		for (int z = 0; z < points.length; z++) {
			points[z] = new DoubleVector(
					canvas.uiOffset.getX() + (canvas.getPlane() == Plane.YZ ? points[z].getY() : points[z].getX())
							* canvas.uiScale * (canvas.isXInverted() ? -1 : 1),
					canvas.uiOffset.getY() + (canvas.getPlane() == Plane.XY ? points[z].getY() : points[z].getZ())
							* canvas.uiScale * (canvas.isYInverted() ? -1 : 1));
		}

		Path path = Bezier.toPath(points);

		if (fill) {
			gc.fillPath(path);
		} else {
			gc.drawPath(path);
		}
		path.dispose();
	}

	/**
	 * @param fill
	 * @param gc
	 * @param canvas
	 * @param center
	 * @param size
	 */
	static void paintCenteredOval(boolean fill, GC gc, BHLCanvas canvas, DoubleVector center, DoubleVector size) {

		int x = canvas.uiOffset.getX() + BHLMath.toInt(
				((canvas.getPlane() == Plane.YZ ? center.getY() : center.getX()) * (canvas.isXInverted() ? -1 : 1)
						- (canvas.getPlane() == Plane.YZ ? size.getY() : size.getX()) / 2.0) * canvas.uiScale);

		int y = canvas.uiOffset.getY() + BHLMath.toInt(
				((canvas.getPlane() == Plane.XY ? center.getY() : center.getZ()) * (canvas.isYInverted() ? -1 : 1)
						- (canvas.getPlane() == Plane.XY ? size.getY() : size.getZ()) / 2.0) * canvas.uiScale);

		int width = BHLMath.toInt((canvas.getPlane() == Plane.YZ ? size.getY() : size.getX()) * canvas.uiScale);
		int height = BHLMath.toInt((canvas.getPlane() == Plane.XY ? size.getY() : size.getZ()) * canvas.uiScale);

		if (fill) {
			gc.fillOval(x, y, width, height);
		} else {
			gc.drawOval(x, y, width, height);
		}
	}

	/**
	 * @param fill
	 * @param gc
	 * @param canvas
	 * @param center
	 * @param size
	 */
	static void paintCenteredArc(boolean fill, GC gc, BHLCanvas canvas, DoubleVector center, DoubleVector size,
			int startAngle, int arcAngle) {

		int x = canvas.uiOffset.getX() + BHLMath.toInt(
				((canvas.getPlane() == Plane.YZ ? center.getY() : center.getX()) * (canvas.isXInverted() ? -1 : 1)
						- (canvas.getPlane() == Plane.YZ ? size.getY() : size.getX()) / 2.0) * canvas.uiScale);

		int y = canvas.uiOffset.getY() + BHLMath.toInt(
				((canvas.getPlane() == Plane.XY ? center.getY() : center.getZ()) * (canvas.isYInverted() ? -1 : 1)
						- (canvas.getPlane() == Plane.XY ? size.getY() : size.getZ()) / 2.0) * canvas.uiScale);

		int width = BHLMath.toInt((canvas.getPlane() == Plane.YZ ? size.getY() : size.getX()) * canvas.uiScale);
		int height = BHLMath.toInt((canvas.getPlane() == Plane.XY ? size.getY() : size.getZ()) * canvas.uiScale);

		if (fill) {
			gc.fillArc(x, y, width, height, startAngle, arcAngle);
		} else {
			gc.drawArc(x, y, width, height, startAngle, arcAngle);
		}
	}

	/**
	 * @param canvas
	 * @param points
	 * @return
	 */
	static void paintPolygon(boolean fill, GC gc, BHLCanvas canvas, DoubleVector... vectors) {

		List<Integer> points = new ArrayList<>();

		for (final DoubleVector vec : vectors) {
			points.add(BHLMath.toInt(canvas.uiOffset.getX() + (canvas.getPlane() == Plane.YZ ? vec.getY() : vec.getX())
					* canvas.uiScale * (canvas.isXInverted() ? -1 : 1)));
			points.add(BHLMath.toInt(canvas.uiOffset.getY() + (canvas.getPlane() == Plane.XY ? vec.getY() : vec.getZ())
					* canvas.uiScale * (canvas.isYInverted() ? -1 : 1)));
		}

		if (fill) {
			gc.fillPolygon(points.stream().mapToInt(i -> i).toArray());
		} else {
			gc.drawPolygon(points.stream().mapToInt(i -> i).toArray());
		}
	}

	/**
	 * @param canvas
	 * @param points
	 * @return
	 */
	static void paintPolygon(boolean fill, GC gc, BHLCanvas canvas, boolean mirrorY, DoubleVector... vectors) {

		List<Integer> points = new ArrayList<>();

		for (final DoubleVector vec : vectors) {
			points.add(BHLMath.toInt(canvas.uiOffset.getX() + (canvas.getPlane() == Plane.YZ ? vec.getY() : vec.getX())
					* canvas.uiScale * (canvas.isXInverted() ? -1 : 1)));
			points.add(BHLMath.toInt(canvas.uiOffset.getY() + (canvas.getPlane() == Plane.XY ? vec.getY() : vec.getZ())
					* canvas.uiScale * (canvas.isYInverted() ? -1 : 1)));
		}

		for (int i = vectors.length - 1; i >= 0; i--) {
			points.add(BHLMath.toInt(
					canvas.uiOffset.getX() + (canvas.getPlane() == Plane.YZ ? vectors[i].getY() : vectors[i].getX())
							* canvas.uiScale * (canvas.isXInverted() ? -1 : 1)));
			points.add(BHLMath.toInt(
					canvas.uiOffset.getY() - (canvas.getPlane() == Plane.XY ? vectors[i].getY() : vectors[i].getZ())
							* canvas.uiScale * (canvas.isYInverted() ? -1 : 1)));
		}

		if (fill) {
			gc.fillPolygon(points.stream().mapToInt(i -> i).toArray());
		} else {
			gc.drawPolygon(points.stream().mapToInt(i -> i).toArray());
		}
	}

	/**
	 * @param gc
	 * @param canvas
	 * @param start
	 * @param end
	 */
	static void drawLine(GC gc, BHLCanvas canvas, DoubleVector... vectors) {

		List<Integer> points = new ArrayList<>();

		for (final DoubleVector vec : vectors) {
			points.add(BHLMath.toInt(canvas.uiOffset.getX() + (canvas.getPlane() == Plane.YZ ? vec.getY() : vec.getX())
					* canvas.uiScale * (canvas.isXInverted() ? -1 : 1)));
			points.add(BHLMath.toInt(canvas.uiOffset.getY() + (canvas.getPlane() == Plane.XY ? vec.getY() : vec.getZ())
					* canvas.uiScale * (canvas.isYInverted() ? -1 : 1)));
		}

		gc.drawPolyline(points.stream().mapToInt(i -> i).toArray());
	}

	/**
	 * @param gc
	 * @param canvas
	 * @param position
	 */
	static void drawCenteredImage(Image image, GC gc, BHLCanvas canvas, DoubleVector position) {

		gc.drawImage(image,
				canvas.uiOffset.getX()
						+ BHLMath.toInt((canvas.getPlane() == Plane.YZ ? position.getY() : position.getX())
								* canvas.uiScale * (canvas.isXInverted() ? -1 : 1) - image.getBounds().width / 2.0),
				canvas.uiOffset.getY()
						+ BHLMath.toInt((canvas.getPlane() == Plane.XY ? position.getY() : position.getZ())
								* canvas.uiScale * (canvas.isYInverted() ? -1 : 1) - image.getBounds().height / 2.0));
	}

	/**
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	static Image resize(Image image, double width, double height) {

		int intWidth = BHLMath.toInt(width);
		int intHeight = BHLMath.toInt(height);

		Image scaled = new Image(Display.getDefault(), intWidth, intHeight);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, intWidth, intHeight);
		gc.dispose();

		// Image data from scaled image and transparent pixel from original

		ImageData imageData = scaled.getImageData();
		ImageData origData = image.getImageData();

		imageData.transparentPixel = imageData.palette.getPixel(origData.palette.getRGB(origData.transparentPixel));

		// Final scaled transparent image
		Image finalImage = new Image(Display.getDefault(), imageData);

		scaled.dispose();

		return finalImage;
	}
}
