/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.emf.ui.part;

import java.util.EventObject;
import java.util.Optional;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;
import org.eclipse.emf.ecp.ui.view.ECPRendererException;
import org.eclipse.emf.ecp.ui.view.swt.ECPSWTViewRenderer;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import net.bhl.opensource.toolbox.emf.EMFStorageHandler;
import net.bhl.opensource.toolbox.emf.ui.provider.BHLLabelProvider;
import net.bhl.opensource.toolbox.ui.swt.BHLColors;

/**
 * This class is the detail view for an EMF model element.
 *
 * @author Marc.Engelmann
 * @since 01.08.2018
 *
 */
public class ModelElementPart extends BHLPart {

	private Composite content;
	private final BHLCommandStackListener commandListener = new BHLCommandStackListener();
	private Optional<EObject> selection = Optional.empty();
	Optional<String> name = Optional.empty();

	private BHLLabelProvider labelProvider = new BHLLabelProvider();

	/**
	 * This class listens for commands which should be executed
	 *
	 * @author Marc.Engelmann
	 *
	 */
	private class BHLCommandStackListener implements CommandStackListener {

		@Override
		public void commandStackChanged(EventObject event) {

			// Get the most recent command
			final BasicCommandStack commandStack = (BasicCommandStack) event.getSource();
			final Command command = commandStack.getMostRecentCommand();

			// Switch depending on type of command
			if (command instanceof SetCommand) {

				// Algorithm works as follows: Analyze the command an then determine the element
				// which should be edited. Then search in the original data structure for the
				// equal selected element and perform the changes on this one instead.

				// Perform set command
				final SetCommand set = (SetCommand) command;

				// Helper for comparison of EMF model elements
				final EqualityHelper equality = new EqualityHelper();
				final TreeIterator<EObject> iter = model.get().eAllContents();

				// Loop through all elements of the model
				while (iter.hasNext()) {

					final EObject obj = iter.next();

					// Search for matches
					if (equality.equals(obj, selection.get())) {

						// Apply changes
						obj.eSet(set.getFeature(), set.getValue());

						// Update the selection
						selection = Optional.of(obj);

						// Refresh UI
						didReceiveModel();

					}
				}
			} else {
				System.err.println("NOT IMPLEMENTED: " + command);
			}

			model.ifPresent(EMFStorageHandler::save);

			// Upate all view parts
			eps.getParts().forEach(mpart -> {
				BHLPart part = (BHLPart) mpart.getObject();
				if (part != null && !(part instanceof ModelElementPart)) {
					part.setModel(model.get());
				}
			});
		}
	}

	/**
	 * Create a new resource set
	 *
	 * @return
	 */
	private ResourceSet createResourceSet() {

		// Create resource set
		final ResourceSet resourceSet = new ResourceSetImpl();

		final AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(
				new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
				new BasicCommandStack(), resourceSet);

		// Add custom command stack listener
		domain.getCommandStack().addCommandStackListener(commandListener);
		resourceSet.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(domain));

		return resourceSet;
	}

	/**
	 * @param model
	 * @param Selection
	 */
	public void submitSelection(EObject model, EObject selection, Optional<String> name) {

		this.selection = Optional.of(selection);
		this.model = Optional.of(model);
		this.name = name;
		didReceiveModel();
	}

	/**
	 * @param model
	 * @param Selection
	 */
	public void submitSelection(EObject model, EObject selection) {

		this.selection = Optional.of(selection);
		this.model = Optional.of(model);
		didReceiveModel();
	}

	@Override
	protected void didReceiveModel() {

		// Clean up content
		if (content != null) {
			content.dispose();
		}

		if (!selection.isPresent()) {
			if (model.isPresent()) {
				selection = model;
			} else {
				return;
			}
		}

		// Copy the selected element
		final EObject detailElement = EcoreUtil.copy(selection.get());

		try {

			// Load the resource
			final Resource resource = createResourceSet().createResource(URI.createURI("VIRTUAL"));
			resource.getContents().add(detailElement);

			// Reload content view
			content = new Composite(parent, SWT.NONE);

			// Create a title for the view
			final Composite title = new Composite(content, SWT.NONE);
			title.setLayout(RowLayoutFactory.fillDefaults().margins(10, 10).create());

			// Title image
			new Label(title, SWT.NONE).setImage(labelProvider.getImage(detailElement));

			// Title label
			new Label(title, SWT.NONE)
					.setText("'" + (name.isPresent() ? name.get() : labelProvider.getText(detailElement)) + "' ["
							+ detailElement.getClass().getSimpleName().replace("Impl", "") + "]");

			title.layout();

			// Apply the desired layout to the title
			content.setLayout(GridLayoutFactory.fillDefaults().margins(10, 10).create());
			content.setLayoutData(GridDataFactory.fillDefaults().create());
			content.setBackground(BHLColors.BACKGROUND);

			// Create the model element view of the specific element 'selection2'
			ECPSWTViewRenderer.INSTANCE.render(content, detailElement);
			content.layout();

		} catch (final ECPRendererException e) {
			e.printStackTrace();
		}

		parent.layout();
	}

	@Override
	protected void init() {

		// Initialize content view
		content = new Composite(parent, SWT.NONE);

		// Load the model
		model = EMFStorageHandler.load();

		didReceiveModel();

	}

	@Override
	public String getURI() {
		return "bundleclass://net.bhl.opensource.toolbox.emf.ui/net.bhl.toolbox.opensource.emf.ui.part.ModelElementPart";
	}
}
