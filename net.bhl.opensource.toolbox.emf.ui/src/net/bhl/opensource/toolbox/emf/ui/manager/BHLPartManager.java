/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.manager;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import net.bhl.opensource.toolbox.emf.ui.part.BHLPart;

/**
 * Utility functions for part and command features.
 *
 * @author Marc.Engelmann
 *
 */
public interface BHLPartManager {

	/**
	 * @param partClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends BHLPart> Optional<T> getPart(EPartService eps, Class<T> partClass) {

		for (MPart mpart : eps.getParts()) {

			BHLPart part = (BHLPart) mpart.getObject();

			if (partClass.isInstance(part)) {
				return Optional.of((T) part);
			}
		}

		return Optional.empty();
	}
}
