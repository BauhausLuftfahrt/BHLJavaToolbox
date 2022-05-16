/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import org.eclipse.swt.widgets.Display;

/**
 * @author Marc.Engelmann
 * @since 13.12.2019
 *
 */
public interface BHLTimer {

	/**
	 * Run a specified function after the timer is finished.
	 *
	 * @param delayInMs
	 */
	static void run(int delayInMs, Consumer<Void> function) {

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(() -> {
					try {

						function.accept(null);

					} catch (final ClassCastException e1) {
						e1.printStackTrace();
					}
					timer.purge();
				});
			}
		}, delayInMs);
	}
}
