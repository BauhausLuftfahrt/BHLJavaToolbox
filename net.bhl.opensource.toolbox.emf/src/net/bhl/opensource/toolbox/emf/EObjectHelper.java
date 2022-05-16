/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

/**
 * This is a helper class for the Model package.
 *
 * @author Martin.Glas, Otto.von.Wesendonk, Marc.Engelmann
 *
 *         NOTE: This class was extracted from openCDT
 *         (https://github.com/BauhausLuftfahrt/OpenCDT). Parts were removed in
 *         order to make it executable without the need for openCDT.
 *
 */
public interface EObjectHelper {

	/**
	 * This method returns all child Objects of a given Element having a specific
	 * type.
	 *
	 * @param <T>    The Type parameter which should match clazz
	 * @param parent The methods looks in the child objects of this parent Element
	 * @param clazz  The type of Element the method looks for
	 * @return The list of Elements which are found among the child objects of the
	 *         parent Element
	 */

	@SuppressWarnings("unchecked")
	static <T extends EObject> List<T> getChildren(EObject parent, Class<T> clazz) {
		List<T> result = new ArrayList<>();
		if (parent == null) {
			return result;
		}
		TreeIterator<EObject> iterator = parent.eAllContents();
		while (iterator.hasNext()) {
			EObject ob = iterator.next();
			if (clazz.isInstance(ob)) {
				result.add((T) ob);
			}
		}
		return result;
	}

	/**
	 * This method return the first child of a given Element having a specific type.
	 *
	 * @param <T>    The Type parameter which should match clazz
	 * @param parent The methods looks in the child objects of this parent Element
	 * @param clazz  The type of Element the method looks for
	 * @return The list of Elements which are found among the child objects of the
	 *         parent Element
	 */

	static <T extends EObject> Optional<T> getFirstChild(EObject parent, Class<T> clazz) {
		return Optional.of(getChildren(parent, clazz).get(0));
	}

	/**
	 * This method returns all child Objects of a given Element having a specific
	 * type.
	 *
	 * @param <T>    The Type parameter which should match clazz
	 * @param parent The methods looks in the child objects of this parent Element
	 * @param clazz  The type of Element the method looks for
	 * @return The list of Elements which are found among the child objects of the
	 *         parent Element
	 */

	@SuppressWarnings("unchecked")
	static <T extends EObject> EList<T> getChildrenE(EObject parent, Class<T> clazz) {
		EList<T> result = new BasicEList<>();
		if (parent == null) {
			return result;
		}
		TreeIterator<EObject> iterator = parent.eAllContents();
		while (iterator.hasNext()) {
			EObject ob = iterator.next();
			if (clazz.isInstance(ob)) {
				result.add((T) ob);
			}
		}
		return result;
	}

	/**
	 * @param parent
	 * @param clazz
	 * @param object
	 * @return
	 */
	static <T extends EObject> List<T> getFilteredChildren(EObject parent, Class<T> clazz, Predicate<T> filter) {
		return getChildren(parent, clazz).stream().filter(filter).collect(Collectors.toList());
	}

	/**
	 * Get the EContainer that contains the given model element and whose EContainer
	 * is null.
	 *
	 * @param parent the Class of the parent
	 * @param child  the model element whose container should get returned
	 * @param <T>    the type parameter of the generic method
	 * @return the container
	 */
	static <T extends EObject> T getParent(Class<T> parent, EObject child) {
		Set<EObject> seenModelElements = new HashSet<>();
		seenModelElements.add(child);
		return getParent(parent, child, seenModelElements);
	}

	/**
	 * @param parent
	 * @param child
	 * @param seenModelElements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends EObject> T getParent(Class<T> parent, EObject child, Set<EObject> seenModelElements) {
		if (child == null) {
			return null;
		}

		if (seenModelElements.contains(child.eContainer())) {
			throw new IllegalStateException("ModelElement is in a containment cycle");
		}

		if (parent.isInstance(child)) {
			return (T) child;
		} else {
			seenModelElements.add(child);
			return getParent(parent, child.eContainer(), seenModelElements);
		}
	}
}