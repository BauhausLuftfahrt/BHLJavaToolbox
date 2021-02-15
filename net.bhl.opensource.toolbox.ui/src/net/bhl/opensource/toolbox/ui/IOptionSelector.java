/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JOptionPane;

/**
 * @author Marc.Engelmann
 * @since 09.01.2019
 *
 */
public interface IOptionSelector {

	String TITLE = "Option selector";

	/**
	 * @param options
	 * @param label
	 * @return
	 */
	static <T extends Object> Optional<String> getValue(List<T> options, String label) {
		return getValue(options, label, TITLE);
	}

	/**
	 * @param options
	 * @param label
	 * @param title
	 * @return
	 */
	static <T extends Object> Optional<String> getValue(List<T> options, String label, String title) {

		// Turn list of objects into list of strings
		List<String> content = new ArrayList<>();
		options.forEach(o -> content.add(o.toString()));

		// Retrieve result
		Object result = JOptionPane.showInputDialog(null, label, title, JOptionPane.QUESTION_MESSAGE, null,
				content.toArray(), content.get(0));

		// return correct result
		return result == null ? Optional.empty() : Optional.of((String) result);
	}

	/**
	 * @param options
	 * @param label
	 * @param title
	 * @return
	 */
	static <T extends Object> Optional<Integer> getIndex(List<T> options, String label) {
		return getIndex(options, label, Object::toString);
	}

	/**
	 * @param options
	 * @param label
	 * @param title
	 * @return
	 */
	static <T extends Object> Optional<Integer> getIndex(List<T> options, String label, Function<T, String> modifier) {

		List<String> real = new ArrayList<>();

		options.forEach(v -> real.add(modifier.apply(v)));

		Optional<String> result = getValue(real, label, TITLE);

		return result.isPresent() ? Optional.of(real.indexOf(result.get())) : Optional.empty();
	}

}
