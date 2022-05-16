/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * @author Marc.Engelmann
 *
 */
public interface EMFStorageHandler {

	/**
	 * @return
	 */
	static Optional<EObject> load() {
		return Optional.ofNullable(load(BHLPreferences.PATH + BHLPreferences.MODEL_NAME + BHLPreferences.FILE_TYPE));
	}

	/**
	 * @param name
	 * @return
	 */
	static EObject load(String path) {

		// Create and initialize new resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("*", new XMIResourceFactoryImpl());

		// Create new resource
		Resource resource = new ResourceSetImpl().getResource(URI.createFileURI(path), true);
		try {
			System.out.println("EMFStorageHandler: '" + path + " 'loaded.");

			// Return result
//			return Optional.of(resource.getContents().get(0));
			return resource.getContents().get(0);

		} catch (Exception e) {

			// Return empty element
//			return Optional.empty();
			return null;
		}
	}

	/**
	 * @param object
	 */
	static void save(EObject object) {
		save(object, BHLPreferences.PATH + BHLPreferences.MODEL_NAME + BHLPreferences.FILE_TYPE);
	}

	/**
	 * @param object
	 * @param name
	 */
	static void save(EObject object, String path) {

		// Create and initialize new resource factory
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("*", new XMIResourceFactoryImpl());

		// Create resource set
		ResourceSet resSet = new ResourceSetImpl();
		resSet.getResources().clear();

		// Create a resource
		Resource resource = resSet.createResource(URI.createFileURI(path));

		// Clear any exiting model at path
		resource.getContents().clear();
		resource.getContents().add(object);

		Map<String, Object> options = new HashMap<>();
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);

		// Now save the content
		try {
//			resource.save(Collections.EMPTY_MAP);
			resource.save(options);
			System.out.println("EMFStorageHandler: EObject saved to '" + path + "'.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
