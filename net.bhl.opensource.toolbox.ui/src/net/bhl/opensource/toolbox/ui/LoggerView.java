/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import net.bhl.opensource.toolbox.io.DataSet;

/**
 * This view displays a data set and updates it on demand.
 *
 * @author Marc.Engelmann
 * @since 26.03.2021
 *
 */
public class LoggerView extends JPanel {

	private static final long serialVersionUID = 2L;
	private static final int BOX_HEIGHT = 600;
	private static final int BOX_WIDTH = 1000;

	private static List<DataSet> status;
	private static List<String> headlines;

	/*
	 * Instantiates a new simulation view.
	 */
	public LoggerView(List<DataSet> status) throws NullPointerException {

		setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
		repaint();
	}

	@Override
	public final synchronized void paintComponent(final Graphics g) {

		if (status == null || status.isEmpty()) {
			return;
		}

		int FONT_SIZE = 14;

		super.paintComponent(g);

		g.setColor(Color.BLACK);

		g.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

		int x = 0;

		for (String headline : headlines) {
			g.drawString(headline, 10 + 10 * FONT_SIZE * x, 20);
			x++;
		}

		int y = 2;

		for (DataSet statusLine : status) {

			x = 0;

			for (int i = 0; i < statusLine.entrySet().size(); i++) {

				g.drawString(statusLine.get(headlines.get(i)).toString(), 10 + 10 * FONT_SIZE * x,
						y * (FONT_SIZE + 10) + 20);

				x++;

			}
			x = 0;
			y++;
		}

		y = 0;
	}

	/**
	 * @param statusSubmit
	 */
	public void updateStatus(List<DataSet> statusSubmit, List<String> headlinesSubmit) {
		status = statusSubmit;
		headlines = headlinesSubmit;
		repaint();
	}
}
