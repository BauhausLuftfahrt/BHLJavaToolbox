/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.emf.ui.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.emf.ecore.EObject;

import net.bhl.opensource.toolbox.emf.EMFStorageHandler;
import net.bhl.opensource.toolbox.emf.ui.manager.BHLPartManager;
import net.bhl.opensource.toolbox.emf.ui.part.BHLPart;

/**
 * This class is a super class for Command handlers used in Eclipse 4
 * Applications.
 *
 * @author Marc.Engelmann
 *
 */

public abstract class BHLCommandHandler {

	protected boolean success = true;

	protected Map<?, ?> parameters;
	protected Optional<EObject> model = Optional.empty();

	@Inject
	protected EPartService eps;

	@Inject
	private EModelService ems;

	@Inject
	private IEventBroker eventBroker;

	@CanExecute
	protected abstract boolean canExecute();

	protected abstract void didFinish();

	protected abstract void perform();

	protected abstract void updateUI();

	/**
	 * This function is called when the button of the command is clicked in the UI.
	 *
	 * @param epartService
	 */
	@Execute
	private final void execute(EPartService epartService, ParameterizedCommand commandParameters) {

		// Get the parameters of the command
		parameters = commandParameters.getParameterMap();

		// Perform the desired action of the command
		perform();

		// Store the model data to the file system.
		model.ifPresent(EMFStorageHandler::save);

		// Update the UI
		updateUI();

		// Tell the commands to check their activity state
		eventBroker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);

		// Perform desired actions after the command has finished
		didFinish();
	}

	/**
	 * Create a new part within the window. Use the URI of the class as the
	 * identifier.
	 *
	 * @param partClass
	 */
	protected final <T extends BHLPart> void createPart(Class<T> partClass) {

		// Initialize new MPart
		final MPart mPart = ems.createModelElement(MPart.class);

		// Get the URI saved in the corresponding class files
		try {

			mPart.setContributionURI(partClass.getDeclaredConstructor().newInstance().getURI());
			eps.showPart(mPart, PartState.ACTIVATE);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.err.println(
					"BHLCommandHandler: Could not create a new part of class '" + partClass.getCanonicalName() + "'.");
		}
	}

	/**
	 * Update a specific part with the new model file.
	 *
	 * @param partClass
	 */
	protected final <T extends BHLPart> void updatePart(Class<T> partClass) {
		BHLPartManager.getPart(eps, partClass).ifPresent(p -> model.ifPresent(m -> p.setModel(m)));
	}
}
