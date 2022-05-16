/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.manager;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import net.bhl.opensource.toolbox.emf.EObjectHelper;
import net.bhl.opensource.toolbox.math.vector.DoubleVector;
import net.bhl.opensource.toolbox.math.vector.IntVector;
import net.bhl.opensource.toolbox.ui.swt.BHLKeyManager;
import net.bhl.opensource.toolbox.ui.swt.BHLMouseManager;

/**
 * @author Marc.Engelmann
 * @since 13.12.2019
 *
 */

public abstract class BHLEMFInputManager extends BHLMouseManager {

	public BHLKeyManager keyManager = new BHLKeyManager() {

		@Override
		protected void customKeyAction(KeyEvent e) {
			runCustomKeyAction(e);
		}
	};

	public abstract void runCustomKeyAction(KeyEvent e);

	private final static double DRAG_SELECTION_SEARCH_INCREMENT = 0.20; // m

	public abstract DoubleVector toModelCoordinates(IntVector mousePosition);

	public abstract void updateUI();

	public abstract List<EObject> getClickableElements();

	public abstract BHLModelManager getModelManager();

	public abstract void openInEMFElementView(EObject selection);

	public abstract void runCtrlDoubleClick(EObject object);

	@Override
	public void didClick(IntVector position) {

		DoubleVector modelPosition = toModelCoordinates(position);

		getModelManager().getSelectedElement(getClickableElements(), modelPosition).ifPresentOrElse(element -> {

			if (keyManager.isCtrlActive) {

				if (getModelManager().currentSelection.contains(element)) {
					getModelManager().currentSelection.remove(element);

				} else {
					getModelManager().currentSelection.add(element);
				}

			} else if (getModelManager().currentSelection.size() <= 1
					|| !getModelManager().currentSelection.contains(element)) {

				getModelManager().currentSelection.clear();
				getModelManager().currentSelection.add(element);
			}

			lastClick = null;

		}, () -> {
			getModelManager().currentSelection.clear();
			lastClick = position;
		});

		getModelManager().calculateOffset(modelPosition);

		updateUI();

	}

	@Override
	public void didDoubleClick(IntVector position, MouseEvent e) {

		DoubleVector modelPosition = toModelCoordinates(position);

		getModelManager().currentSelection.clear();

		getModelManager().getSelectedElement(getClickableElements(), modelPosition).ifPresent(element -> {

			if (keyManager.isCtrlActive && e.button == 1) {

				runCtrlDoubleClick(element);

			} else if (keyManager.isShiftActive && e.button == 1) {

				getModelManager().currentSelection
						.addAll(EObjectHelper.getChildren(element.eContainer(), element.getClass()));

			} else {
				openInEMFElementView(element);
			}
		});

		getModelManager().calculateOffset(modelPosition);

	}

	@Override
	public void didMove(IntVector position) {

		if (isMouseDown) {

			for (EObject element : getModelManager().currentSelection) {

				DoubleVector modelPosition = toModelCoordinates(position);

				DoubleVector newPosition = new DoubleVector(
						modelPosition.getX() - getModelManager().offsets.get(element).getX(),
						modelPosition.getY() - getModelManager().offsets.get(element).getY(), 0);

				getModelManager().move(element, newPosition);
			}
			updateUI();
		}

	}

	@Override
	public void didRelease(IntVector position) {

		if (lastClick != null) {

			DoubleVector modelPosition = toModelCoordinates(position);
			DoubleVector lastPosition = toModelCoordinates(lastClick);

			double xMin = Math.min(modelPosition.getX(), lastPosition.getX());
			double xMax = Math.max(modelPosition.getX(), lastPosition.getX());

			double yMin = Math.min(modelPosition.getY(), lastPosition.getY());
			double yMax = Math.max(modelPosition.getY(), lastPosition.getY());

			for (double a = xMin; a <= xMax; a += DRAG_SELECTION_SEARCH_INCREMENT) {
				for (double b = yMin; b <= yMax; b += DRAG_SELECTION_SEARCH_INCREMENT) {

					getModelManager().getSelectedElement(getClickableElements(), new DoubleVector(a, b))
							.ifPresent(element -> {

								if (!getModelManager().currentSelection.contains(element)) {
									getModelManager().currentSelection.add(element);
								}
							});
				}
			}
			lastClick = null;
		}

		updateUI();
	}

}
