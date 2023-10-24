/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.emf.ui.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * @author Marc.Engelmann
 *
 */

public abstract class BHLModelManager {

	public List<EObject> currentSelection = new ArrayList<>();
	public Map<EObject, DoubleVector> offsets = new HashMap<>();

	private final static double MOVE_INCREMENT = 0.05;
	public static double INITIAL_SIZE = 0.5;

	protected abstract DoubleVector getMinBounds(EObject obj);

	protected abstract DoubleVector getMaxBounds(EObject obj);

	protected abstract DoubleVector getPosition(EObject obj);

	protected abstract DoubleVector getSize(EObject obj);

	protected abstract void setPosition(EObject obj, DoubleVector position);

	protected abstract void setSize(EObject obj, DoubleVector size);

	protected abstract void commandComplete();

//	protected abstract void handleDeletionOfLastElement

	/**
	 *
	 */
	public final void delete() {

		EList<EObject> markedForRemoval = new BasicEList<>();

		for (EObject eo : currentSelection) {

			if (eo.eContainer() != null) {

				Object container = eo.eContainer().eGet(eo.eContainingFeature());

				// TODO

				if (container instanceof EList) {

					if (((EList<?>) container).size() <= 1) {
						markedForRemoval.add(eo.eContainer());
					}
				}
			}

			EcoreUtil.remove(eo);
		}

		currentSelection.clear();

		for (EObject obj : markedForRemoval) {
			EcoreUtil.remove(obj);
		}

		commandComplete();
	}

	/**
	 * @param c
	 */
	public final void align(char code) {

		if (!currentSelection.isEmpty()) {

			DoubleVector guidance = getPosition(currentSelection.get(0));

			for (EObject element : currentSelection) {

				if (code == 'x') {
					move(element, new DoubleVector(guidance.getX(), getPosition(element).getY(), 0));
				}

				if (code == 'y') {
					move(element, new DoubleVector(getPosition(element).getX(), guidance.getY(), 0));
				}
			}
		}
		commandComplete();
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public final void copy() {

		List<EObject> newSelection = new ArrayList<>();

		for (EObject eo : currentSelection) {

			if (eo.eContainer() != null) {

				Object container = eo.eContainer().eGet(eo.eContainingFeature());

				if (container instanceof EList) {

					EObject copy = EcoreUtil.copy(eo);

					newSelection.add(copy);

					((EList<EObject>) container).add(copy);

					setPosition(copy,
							new DoubleVector(getPosition(eo).getX() + INITIAL_SIZE, getPosition(eo).getY(), 0));

					ECollections.sort((EList<EObject>) container, Comparator.comparing(e -> getPosition(e).getX()));
				}
			}
		}

		currentSelection.clear();
		currentSelection.addAll(newSelection);

		commandComplete();
	}

	/**
	 * Calculate the offset
	 *
	 * @param mousePosition
	 */
	public final void calculateOffset(DoubleVector mousePosition) {

//		lastPositions.clear();

		offsets.clear();

		for (EObject element : currentSelection) {

			offsets.put(element, new DoubleVector(mousePosition.getX() - getPosition(element).getX(),
					mousePosition.getY() - getPosition(element).getY()));

//			lastPositions[currentSelection.indexOf(element)] = new DoubleVector(getPosition(element).getX(),
//					getPosition(element).getY());
		}

	}

	/**
	 * Get the element out of a collection of elements which is within the given
	 * position.
	 *
	 * @param available
	 * @return
	 */
	public final Optional<EObject> getSelectedElement(List<EObject> available, DoubleVector position) {

		for (EObject obj : available) {

			if (position.getX() > getPosition(obj).getX()
					&& position.getX() < getPosition(obj).getX() + getSize(obj).getX()) {
				if (position.getY() > getPosition(obj).getY()
						&& position.getY() < getPosition(obj).getY() + getSize(obj).getY()) {
					return Optional.of(obj);
				}
			}
		}

		return Optional.empty();
	}

	/**
	 * @param xMove
	 * @param yMove
	 * @param xSize
	 * @param ySize
	 */
	public final void manipulate(double xMove, double yMove, double xSize, double ySize) {

		for (EObject eo : currentSelection) {

			if (getSize(eo).getX() < MOVE_INCREMENT) {
				xSize = 0;
			}

			if (getSize(eo).getY() < MOVE_INCREMENT) {
				ySize = 0;
			}

			setSize(eo, new DoubleVector(getSize(eo).getX() + MOVE_INCREMENT * xSize,
					getSize(eo).getY() + MOVE_INCREMENT * ySize, getSize(eo).getZ()));

			// Prevent negative values

			move(eo, new DoubleVector(getPosition(eo).getX() + MOVE_INCREMENT * xMove,
					getPosition(eo).getY() + MOVE_INCREMENT * yMove, getPosition(eo).getZ()));

		}

		commandComplete();
	}

	/**
	 * @param eObj
	 * @param newPosition
	 */
	public final void move(EObject eObj, DoubleVector newPosition) {

		if (newPosition.getX() < getMinBounds(eObj).getX()) {
			newPosition.setX(getMinBounds(eObj).getX());
		}

		if (newPosition.getX() > getMaxBounds(eObj).getX() - getSize(eObj).getX()) {
			newPosition.setX(getMaxBounds(eObj).getX() - getSize(eObj).getX());
		}

		if (newPosition.getY() < getMinBounds(eObj).getY()) {
			newPosition.setY(getMinBounds(eObj).getY());
		}

		if (newPosition.getY() > getMaxBounds(eObj).getY() - getSize(eObj).getY()) {
			newPosition.setY(getMaxBounds(eObj).getY() - getSize(eObj).getY());
		}

		setPosition(eObj, newPosition);
	}
}
