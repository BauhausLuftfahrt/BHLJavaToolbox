/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import net.bhl.opensource.toolbox.math.BHLMath;
import net.bhl.opensource.toolbox.math.vector.DoubleVector;
import net.bhl.opensource.toolbox.math.vector.IntVector;

/**
 *
 * Custom canvas with auto scaling of content to size
 *
 * @author Marc.Engelmann
 * @since 08.01.2019
 *
 */

public class BHLCanvas extends Canvas {

	public IntVector uiOffset = new IntVector(0, 0);
	public double uiScale = 0;
	private String measure = "1 Meter";
	private int rulerHeight = 15; // pixels
	private IntVector rulerPosition = new IntVector(10, 10);

	private boolean invertX = false;
	private boolean invertY = false;

	private Plane orientation = Plane.XY;

	public enum Plane {
		XY, XZ, YZ;
	}

	private List<BiConsumer<GC, Boolean>> uiFunctions = new ArrayList<>();

	/**
	 * @param parent
	 * @param style
	 */
	public BHLCanvas(Composite parent, int style) {
		super(parent, style);
		addPaintListener(e -> uiFunctions.forEach(f -> f.accept(e.gc, false)));
	}

	/**
	 * This function adapts the scaling factor of the canvas to that a minimum given
	 * margin is maintained.
	 *
	 * @param maximumWidth
	 * @param maximumHeight
	 * @param minimumMarginInPixels
	 */
	public void adaptScaling(double maximumWidth, double maximumHeight, double minimumMarginInPixels) {

		uiScale = (getBounds().width - 2 * minimumMarginInPixels) / maximumWidth;

		if (getBounds().height < maximumHeight * uiScale + 2 * minimumMarginInPixels) {
			uiScale = (getBounds().height - 2 * minimumMarginInPixels) / maximumHeight;
		}

		uiOffset.setX(BHLMath.toInt((getBounds().width - maximumWidth * uiScale) / 2.0));
		uiOffset.setY(BHLMath.toInt((getBounds().height - maximumHeight * uiScale) / 2.0));
	}

	/**
	 * @param plane
	 */
	public void setPlane(Plane plane) {
		orientation = plane;
	}

	/**
	 * @param plane
	 */
	public Plane getPlane() {
		return orientation;
	}

	/**
	 * Set the metrical origin of all coordinates.
	 *
	 * @param position
	 */
	public void setOrigin(double x, double y) {
		uiOffset.setX(uiOffset.getX() + BHLMath.toInt(x * uiScale));
		uiOffset.setY(uiOffset.getY() + BHLMath.toInt(y * uiScale));
	}

	/**
	 * @param gc
	 */
	public void addRuler(GC gc) {

		gc.setBackground(BHLColors.BLACK);
		gc.setForeground(BHLColors.WHITE);

		gc.fillRectangle(rulerPosition.getX(), rulerPosition.getY(), BHLMath.toInt(uiScale), rulerHeight);
		gc.drawText(measure, rulerPosition.getX() + BHLMath.toInt(uiScale / 2.0 - gc.textExtent(measure).x / 2.0),
				rulerPosition.getY() + BHLMath.toInt(rulerHeight / 2.0 - gc.textExtent(measure).y / 2.0), true);
	}

	/**
	 * @param gc
	 */
	public void addCoordinateSystem(GC gc) {

		gc.setBackground(BHLColors.BLACK);
		gc.setForeground(BHLColors.BLACK);

		Font font = new Font(super.getDisplay(), "Lucida Sans Typewriter", 10, SWT.NONE);
		gc.setFont(font);

		int length = 26;

		IntVector origin = new IntVector(20 + (isXInverted() ? length : 0),
				getSize().y - 20 - (isYInverted() ? 0 : length));

		BHLGC.drawArrow(gc, BHLColors.BLACK, origin, length, 6, false, orientation == Plane.YZ ? "y" : "x",
				isXInverted(), 2);
		BHLGC.drawArrow(gc, BHLColors.BLACK, origin, length, 6, true, orientation == Plane.XY ? "y" : "z",
				isYInverted(), 2);

		font.dispose();
	}

	/**
	 * @param gc
	 * @param cg
	 * @param diameter
	 */
	public void addCenterOfGravity(GC gc, DoubleVector cg, double diameter) {
		gc.setBackground(BHLColors.BLACK);
		BHLGC.paintCenteredOval(true, gc, this, cg, new DoubleVector(diameter, diameter, diameter));
		gc.setBackground(BHLColors.WHITE);
		BHLGC.paintCenteredArc(true, gc, this, cg, new DoubleVector(diameter, diameter, diameter), 90, 90);
		BHLGC.paintCenteredArc(true, gc, this, cg, new DoubleVector(diameter, diameter, diameter), 270, 90);

	}

	/**
	 *
	 * Draw a 1m x 1m grid
	 *
	 * @param gc
	 */
	public void addMeterGrid(GC gc) {

		gc.setForeground(BHLColors.GREY(200));
		gc.setLineWidth(1);
		gc.setLineStyle(SWT.LINE_DASH);

		for (int i = uiOffset.getX(); i < getSize().x; i += uiScale) {

			if (i == uiOffset.getX()) {
				gc.setLineWidth(2);
			}
			gc.drawLine(i, 0, i, getSize().y);
			gc.setLineWidth(1);
		}

		for (int i = uiOffset.getX(); i > 0; i -= uiScale) {
			if (i == uiOffset.getX()) {
				continue;
			}
			gc.drawLine(i, 0, i, getSize().y);
		}

		for (int i = uiOffset.getY(); i < getSize().y; i += uiScale) {

			if (i == uiOffset.getY()) {
				gc.setLineWidth(2);
			}
			gc.drawLine(0, i, getSize().x, i);
			gc.setLineWidth(1);
		}

		for (int i = uiOffset.getY(); i > 0; i -= uiScale) {
			if (i == uiOffset.getY()) {
				continue;
			}
			gc.drawLine(0, i, getSize().x, i);
		}

		gc.setLineStyle(SWT.LINE_SOLID);
	}

	/**
	 * Take a snapshot of the canvas
	 */
	public void snapshot(String name, String exportPath) {

		Image img = new Image(Display.getCurrent(), BHLMath.toInt(getBounds().width),
				BHLMath.toInt(getBounds().height));

		GC gc = new GC(img);
		uiFunctions.forEach(f -> f.accept(gc, true));

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { img.getImageData() };
		loader.save(exportPath + "/" + name + ".png", SWT.IMAGE_PNG);
		img.dispose();
		gc.dispose();
	}

	/**
	 * Take a snapshot of the canvas
	 */
	public void snapshot(String name) {
		snapshot(name, System.getProperty("user.home") + "/Desktop");
	}

	/**
	 * @param func
	 */
	public void addDrawFunction(BiConsumer<GC, Boolean> func) {
		uiFunctions.add(func);
	}

	/**
	 * @param func
	 */
	public void removeDrawFunction(BiConsumer<GC, Boolean> func) {
		uiFunctions.remove(func);
	}

	/**
	 *
	 */
	public void clearDrawFunctions() {
		uiFunctions.clear();
	}

	/**
	 *
	 */
	public void invertYAxis() {
		invertY = true;
	}

	/**
	 * @return
	 */
	public boolean isYInverted() {
		return invertY;
	}

	/**
	 *
	 */
	public void invertXAxis() {
		invertX = true;
	}

	/**
	 * @return
	 */
	public boolean isXInverted() {
		return invertX;
	}

	/**
	 * Transform canvas position to model coordinate
	 *
	 * @param position
	 * @return
	 */
	public DoubleVector getModelCoordinates(IntVector position) {
		return new DoubleVector((position.getX() - uiOffset.getX()) / uiScale * (isXInverted() ? -1 : 1),
				(position.getY() - uiOffset.getY()) / uiScale * (isYInverted() ? -1 : 1), 0);
	}
}
