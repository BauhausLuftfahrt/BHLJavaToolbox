/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * @author Michael.Shamiyeh
 * @since 14.01.2020
 *
 */
public class EObjectInitializer {

	/**
	 * @param <T>
	 * @param clazz
	 * @param iri
	 * @return
	 */
	public <T extends EObject> T instanciate(Class<T> clazz) {

		for (String packageKey : EPackage.Registry.INSTANCE.keySet()) {
			EPackage emfPackage = EPackage.Registry.INSTANCE.getEPackage(packageKey);

			EClassifier classif = emfPackage.getEClassifier(clazz.getSimpleName());

			if (classif != null) {

				T element = clazz.cast(emfPackage.getEFactoryInstance().create((EClass) classif));
				// initialize your object...
				return element;
			}
		}

		return null;
	}
}
