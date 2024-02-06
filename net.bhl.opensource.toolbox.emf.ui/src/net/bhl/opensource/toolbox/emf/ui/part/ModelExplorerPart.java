/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.part;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.TreeItem;

import net.bhl.opensource.toolbox.emf.EMFStorageHandler;
import net.bhl.opensource.toolbox.emf.ui.manager.BHLPartManager;
import net.bhl.opensource.toolbox.emf.ui.provider.BHLLabelProvider;
import net.bhl.opensource.toolbox.ui.swt.BHLColors;

/**
 * This class contains the model explorer for an EMF model. It is a child of
 * BHLPart.
 *
 * @see BHLPart.class
 * @author Marc.Engelmann
 * @since 01.08.2018
 *
 */

public class ModelExplorerPart extends BHLPart {

	// The tree viewer containing the elements of the EMF model
	private static TreeViewer viewer;
	private static BHLLabelProvider labelProvider = new BHLLabelProvider();

	@Override
	protected void didReceiveModel() {

		model.ifPresent(m -> {

			// Update the tree viewer content
			viewer.setInput(m);
			viewer.refresh();

			// Update the view title
			part.setLabel("Model Explorer - " + labelProvider.getText(m));
		});
	}

	/**
	 * This function gets an EObject out of a selection context.
	 *
	 * @param selection
	 * @return
	 */
	private EObject getEObject(ISelection selection) {
		return (EObject) ((IStructuredSelection) selection).getFirstElement();
	}

	/**
	 * @param newProvider
	 */
	public static void updateLabelProvider(BHLLabelProvider newProvider) {
		labelProvider = newProvider;

		// Update immediately if viewer is already initialized
		if (viewer != null) {
			viewer.setLabelProvider(newProvider);
			viewer.refresh();
		}
	}

	/**
	 *
	 */
	public void resetLabelProvider() {
		viewer.setLabelProvider(new BHLLabelProvider());
		viewer.refresh();
	}

	@Override
	protected void init() {

		// Load the model
		model = EMFStorageHandler.load();

		// Initialize the tree viewer
		viewer = new TreeViewer(parent, SWT.NONE);
		viewer.getTree().setBackground(BHLColors.BACKGROUND);

		// Initialize Adapter Factory for EMF purposes
		final ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		// Set the label and content provider for the tree viewer
		viewer.setContentProvider(new AdapterFactoryContentProvider(composedAdapterFactory));
		viewer.setLabelProvider(labelProvider);

		// Update the input
		viewer.setInput(model);

		// Integrate a key listenrer for content deletion
		viewer.getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.DEL) {

					// Delete content
					for (final TreeItem obj : viewer.getTree().getSelection()) {
						EcoreUtil.delete((EObject) obj.getData());
					}

				} else if (event.keyCode == 'c' && (event.stateMask & SWT.CTRL) != 0) {

					// Delete content
					for (final TreeItem obj : viewer.getTree().getSelection()) {

						EObject original = (EObject) obj.getData();
						EObject copy = EcoreUtil.copy(original);

						EObject eParent = original.eContainer();
						EStructuralFeature eParentFeature = original.eContainingFeature();

						if (eParent != null) {
							if (eParent.eGet(eParentFeature) != null) {
								if (eParent.eGet(eParentFeature) instanceof Collection) {
									((Collection) eParent.eGet(eParentFeature)).add(copy);
								}
							}
						}
					}

				} else {
					return;
				}

				// Save model
				EMFStorageHandler.save(model.get());

				// Upate all view parts
				eps.getParts().forEach(mpart -> {
					BHLPart part = (BHLPart) mpart.getObject();
					part.setModel(model.get());
				});
			}
		});

		viewer.expandAll();

		// Listen for double click in order to open the model element in the detail view
		viewer.addDoubleClickListener(e -> BHLPartManager.getPart(eps, ModelElementPart.class)
				.ifPresent(p -> p.submitSelection(model.get(), EcoreUtil.copy(getEObject(e.getSelection())),
						Optional.of(getEObject(e.getSelection()).eContainingFeature().getName()))));

		didReceiveModel();
	}

	@Override
	public String getURI() {
		return "bundleclass://net.bhl.opensource.toolbox.emf.ui/net.bhl.opensource.toolbox.emf.ui.part.ModelExplorerPart";
	}
}
