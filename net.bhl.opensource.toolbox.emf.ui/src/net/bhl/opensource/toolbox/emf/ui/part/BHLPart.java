/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.part;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

import net.bhl.opensource.toolbox.emf.ui.manager.BHLPartManager;

/**
 * This class is a super class for View Parts used in Eclipse 4 Applications.
 *
 * @author Marc.Engelmann
 *
 */
public abstract class BHLPart {

	// The parent composite of the part
	protected Composite parent;

	// The model connected to the view
	protected Optional<EObject> model = Optional.empty();

	@Inject
	protected EPartService eps;

	@Inject
	protected MPart part;

	/**
	 * This function creates the view part
	 *
	 * @param parent
	 */
	@PostConstruct
	private final void createComposite(Composite parent) {

		this.parent = parent;
		init();
	}

	/**
	 * This function needs to be implemented by every subclass and is called after
	 * the model has been updated.
	 */
	protected abstract void didReceiveModel();

	/**
	 * This function needs to be implemented by every subclass and is called once
	 * after the composite containing the view is initially created.
	 */
	protected abstract void init();

	/**
	 * This function needs to be implemented by every subclass and returns the URI
	 * string of the path of the current class.
	 *
	 * @return the URI string
	 */
	public abstract String getURI();

	/**
	 * This function updates the model linked to the view.
	 *
	 * @param model
	 */
	public final void setModel(EObject model) {

		// Check for null
		if (model == null) {
			return;
		}

		// Apply changes
		this.model = Optional.of(model);
		didReceiveModel();
	}

	/**
	 * Save content by marking view as dirty.
	 */
	@Persist
	private final void save() {
		part.setDirty(false);
	}

	/**
	 * This function is called when the view is in focus.
	 */
	@Focus
	private final void setFocus() {
		//
	}

	/**
	 * Dispose the view and all of its contents.
	 */
	@PreDestroy
	private final void dispose() {
		parent.dispose();
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
