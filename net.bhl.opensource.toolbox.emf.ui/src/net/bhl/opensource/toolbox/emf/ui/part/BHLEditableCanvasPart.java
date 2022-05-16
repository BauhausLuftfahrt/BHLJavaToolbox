/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.part;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;

import net.bhl.opensource.toolbox.emf.EMFStorageHandler;
import net.bhl.opensource.toolbox.emf.ui.manager.BHLEMFInputManager;
import net.bhl.opensource.toolbox.math.BHLMath;
import net.bhl.opensource.toolbox.math.vector.DoubleVector;
import net.bhl.opensource.toolbox.ui.swt.BHLCanvas;
import net.bhl.opensource.toolbox.ui.swt.BHLColors;
import net.bhl.opensource.toolbox.ui.swt.BHLGC;
import net.bhl.opensource.toolbox.ui.swt.BHLTimer;

/**
 * @author Marc.Engelmann
 * @since 13.12.2019
 *
 */
public abstract class BHLEditableCanvasPart extends BHLPart {

	protected boolean useColorfulDesign = true;

	protected BHLCanvas canvas;

	protected abstract DoubleVector getMaximumDimensions();

	protected abstract BHLEMFInputManager getMouseManager();

	protected abstract List<BiConsumer<GC, Boolean>> getAdditionalDrawFunctions();

	private BiConsumer<GC, Boolean> backgroundPaint = (gc, isSnapshot) -> {

		if (model == null) {
			return;
		}

		if (!isSnapshot) {
			canvas.adaptScaling(getMaximumDimensions().getX(), getMaximumDimensions().getY(), 30);
			canvas.setOrigin(0.0, getMaximumDimensions().getY() / 2.0);
		}

		canvas.invertYAxis();

		Font defaultFont = new Font(parent.getDisplay(), "Arial", BHLMath.toInt(canvas.uiScale * 0.1), SWT.BOLD);

		BHLGC.setQuality(gc, false);

		gc.setFont(defaultFont);

		if (isSnapshot) {
			canvas.setBackground(BHLColors.WHITE);

		} else {

			canvas.setBackground(useColorfulDesign ? BHLColors.BACKGROUND : BHLColors.WHITE);
			if (useColorfulDesign) {
				canvas.addRuler(gc);
			}
		}

		canvas.addCoordinateSystem(gc);

		// --------------------------------------------------------------------------------------
		// Meter grid
		if (!useColorfulDesign) {
			canvas.addMeterGrid(gc);
		}

		// --------------------------------------------------------------------------------------
		// Symmetry Line

		final double overlap = 30;
		gc.setLineWidth(2);
		gc.setForeground(BHLColors.GREY(50));
		gc.setLineStyle(SWT.LINE_DASHDOT);
		gc.drawLine(BHLMath.toInt(canvas.uiOffset.getX() - overlap), BHLMath.toInt(canvas.uiOffset.getY()),
				BHLMath.toInt(getMaximumDimensions().getX() * canvas.uiScale + canvas.uiOffset.getX() + overlap),
				BHLMath.toInt(canvas.uiOffset.getY()));

		gc.setForeground(BHLColors.BLACK);
	};

	@Override
	protected final void init() {

		canvas = new BHLCanvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.addDrawFunction(backgroundPaint);

		// Delay loading of model
		BHLTimer.run(100, Void -> EMFStorageHandler.load().ifPresent(this::setModel));

		BHLEMFInputManager manager = getMouseManager();

		canvas.addMouseMoveListener(manager);
		canvas.addMouseListener(manager);
		canvas.addKeyListener(manager.keyManager);

		getAdditionalDrawFunctions().forEach(canvas::addDrawFunction);
	}

	@Override
	public final String getURI() {
		return null;
	}
}
