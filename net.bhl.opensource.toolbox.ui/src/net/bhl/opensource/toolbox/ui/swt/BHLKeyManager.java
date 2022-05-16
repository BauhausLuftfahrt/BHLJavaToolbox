/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

/**
 * @author Marc.Engelmann
 *
 */
public abstract class BHLKeyManager implements KeyListener {

	public boolean isCtrlActive = false;
	public boolean isShiftActive = false;

	@Override
	public void keyPressed(KeyEvent e) {

		isCtrlActive = (e.stateMask & SWT.CTRL) == SWT.CTRL;
		isShiftActive = (e.stateMask & SWT.SHIFT) == SWT.SHIFT;

		switch (e.keyCode) {

		// Do function keys
		case SWT.CTRL:
			isCtrlActive = true;
			return;

		case SWT.SHIFT:
			isShiftActive = true;

		default:
			customKeyAction(e);
			return;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isCtrlActive = false;
		isShiftActive = false;
	}

	protected abstract void customKeyAction(KeyEvent e);
}
