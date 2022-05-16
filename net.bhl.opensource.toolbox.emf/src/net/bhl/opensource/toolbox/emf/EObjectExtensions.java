/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import org.eclipse.emf.ecore.EObject;

import net.bhl.opensource.toolbox.string.StringHelper;

/**
 * @author Marc.Engelmann
 *
 */
public interface EObjectExtensions {

	/**
	 * Return the class name of an ECORE class by removing the IMPL name.
	 *
	 * @param obj
	 * @return
	 */
	static String getName(EObject obj) {
		return StringHelper.splitCamelCase(obj.getClass().getSimpleName().replace("Impl", ""));
	}
}
