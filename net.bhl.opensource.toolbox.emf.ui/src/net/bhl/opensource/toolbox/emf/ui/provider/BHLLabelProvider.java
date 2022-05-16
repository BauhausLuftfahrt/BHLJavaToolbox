/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf.ui.provider;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import net.bhl.opensource.toolbox.string.StringHelper;

/**
 * @author Marc.Engelmann
 * @since 18.11.2019
 *
 */
public class BHLLabelProvider implements ILabelProvider {

	Map<String, BiFunction<EObject, EStructuralFeature, String>> labelmap = Map.of(

			"value", (e, s) -> " = " + e.eGet(s),

			"uID", (e, s) -> " [uID = " + e.eGet(s) + "]"

	);

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof EObject) {
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
					ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
			adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
			return labelProvider.getImage(element);
		}
		return null;
	}

	@Override
	public String getText(Object element) {

		// Check if the element, which's text should be get, is an EOBJECT
		if (element instanceof EObject) {

			// Get the name that this object has in the parent element context.
			EStructuralFeature feature = ((EObject) element).eContainingFeature();

			// Get the base name
			String name = feature == null ? getTextFor((EObject) element)
					: StringHelper.splitCamelCase(feature.getName());

			for (Entry<String, BiFunction<EObject, EStructuralFeature, String>> entry : labelmap.entrySet()) {
				EStructuralFeature subFeature = ((EObject) element).eClass().getEStructuralFeature(entry.getKey());
				if (subFeature != null) {
					return name + entry.getValue().apply((EObject) element, subFeature);
				}
			}

			// Lastly, try to show the number of elements in the list
			for (EStructuralFeature subFeature : ((EObject) element).eClass().getEAllStructuralFeatures()) {

				if (((EObject) element).eGet(subFeature) instanceof Collection<?>) {

					int size = ((List<?>) ((EObject) element).eGet(subFeature)).size();

					return new String(name + (size > 1 ? " (" + size + ")" : ""));
				}
			}

			return name;
		}

		return element.toString();
	}

	/**
	 * @param object
	 * @return
	 */
	protected String getTextFor(EObject object) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);

		return labelProvider.getText(object);
	}

}
