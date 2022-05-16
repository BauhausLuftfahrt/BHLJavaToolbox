/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/

package net.bhl.opensource.toolbox.ui.swt;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

import net.bhl.opensource.toolbox.math.vector.IntVector;

/**
 * @author Marc.Engelmann
 *
 */

public abstract class BHLMouseManager implements MouseListener, MouseMoveListener {

	public boolean isMouseDown = false;
	public IntVector position = new IntVector(0, 0);
	public IntVector lastClick = null;

	public abstract void didClick(IntVector position);

	public abstract void didDoubleClick(IntVector position, MouseEvent e);

	public abstract void didMove(IntVector position);

	public abstract void didRelease(IntVector position);

	@Override
	public final void mouseDoubleClick(MouseEvent e) {
		lastClick = new IntVector(e.x, e.y);
		didDoubleClick(new IntVector(e.x, e.y), e);
	}

	@Override
	public final void mouseDown(MouseEvent e) {
		isMouseDown = true;
		lastClick = new IntVector(e.x, e.y);
		didClick(new IntVector(e.x, e.y));
	}

	@Override
	public final void mouseMove(MouseEvent e) {
		position = new IntVector(e.x, e.y);
		didMove(new IntVector(e.x, e.y));
	}

	@Override
	public final void mouseUp(MouseEvent e) {
		isMouseDown = false;
		didRelease(new IntVector(e.x, e.y));
	}
}
