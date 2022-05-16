/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2022 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui;

import java.util.Optional;

import javax.swing.JOptionPane;

/**
 * @author Marc.Engelmann
 * @since 11.01.2019
 *
 */
public interface IInputCollector {

	String OPTION_TITLE = "User input required";

	/**
	 * @param label
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends Object> Optional<T> get(String label, Class<T> clazz) {
		return get(label, null, clazz);
	}

	/**
	 * @param label
	 * @param placeholder
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T extends Object> Optional<T> get(String label, String placeholder, Class<T> clazz) {

		Object result = JOptionPane.showInputDialog(null, label, OPTION_TITLE, JOptionPane.QUESTION_MESSAGE, null, null,
				placeholder);
		try {
			if (clazz == Integer.class) {
				return Optional.of((T) Integer.valueOf((String) result));
			} else if (clazz == Double.class) {
				return Optional.of((T) Double.valueOf((String) result));
			}
		} catch (Exception e) {
			return Optional.empty();
		}

		return result == null ? Optional.empty() : Optional.of(clazz.cast(result));

	}
}
