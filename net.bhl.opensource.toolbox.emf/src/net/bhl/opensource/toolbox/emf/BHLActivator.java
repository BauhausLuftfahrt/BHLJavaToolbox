/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.emf;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * This is the parent class for all elements being launched during the
 * activation of a plug-in.
 *
 * @author Marc.Engelmann
 * @since 09.12.2019
 *
 */
public abstract class BHLActivator implements BundleActivator {

	private String DEFAULT_LAUNCH_MESSAGE = "Untitled plugin activated.";

	@Override
	public void start(BundleContext context) {

		// Set the ECore model name in order to get the proper XMI file.
		BHLPreferences.MODEL_NAME = getModelName();

		// Run the child class initialization.
		performInit();

		// Output the status.
		System.out.println(getLaunchInfo() == null ? DEFAULT_LAUNCH_MESSAGE : getLaunchInfo());

	}

	@Override
	public void stop(BundleContext context) {
		// TODO: Do nothing yet!
	}

	protected abstract String getModelName();

	protected abstract void performInit();

	protected abstract String getLaunchInfo();
}
