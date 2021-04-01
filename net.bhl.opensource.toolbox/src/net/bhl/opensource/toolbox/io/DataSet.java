/*******************************************************************************
 * <copyright> Copyright (c) 2014 - 2021 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.io;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.bhl.opensource.toolbox.math.vector.DoubleVector;

/**
 * This class is a modified hash map which can contain the following kind of
 * data:
 *
 * - String - Double - Integer - Boolean - ArrayList -
 *
 *
 * @author Marc.Engelmann
 *
 */
public class DataSet extends HashMap<String, Object> {

	private static final long serialVersionUID = 3748914201966800657L;
	private Optional<String> name = Optional.empty();

	/**
	 *
	 */
	public DataSet() {
		super();
	}

	/**
	 * @param name
	 */
	public DataSet(String name) {
		super();
		this.name = Optional.of(name);
	}

	/**
	 * @return
	 */
	public Optional<String> getName() {
		return name;
	}

	@Override
	@Deprecated
	public Object get(Object arg0) {
		return super.get(arg0);
	}

	/**
	 * @param clazz
	 * @param key
	 * @return
	 */
	public <T extends Object> Optional<T> get(Class<T> clazz, String key) {
		Object value = super.get(key);
		return value == null ? Optional.empty() : Optional.of(clazz.cast(value));
	}

	/**
	 * @param key
	 * @return
	 */
	public Optional<Boolean> getBoolean(String key) {
		Boolean value = (Boolean) super.get(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Optional<Double> getDouble(String key) {

		if (super.get(key) == null) {
			return Optional.empty();
		}

		if (super.get(key) instanceof Integer) {
			return Optional.of(((Integer) super.get(key)).doubleValue());
		}

		return Optional.of((Double) super.get(key));
	}

	/**
	 * @param key
	 * @return
	 */
	public Optional<Integer> getInteger(String key) {
		Integer value = (Integer) super.get(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Optional<String> getString(String key) {
		String value = (String) super.get(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(String arg0, Object arg1) {
		if (super.containsKey(arg0)) {
			if (!(super.get(arg0) instanceof List)) {

				Object old = super.get(arg0);
				super.put(arg0, new ArrayList<>());

				((List<Object>) super.get(arg0)).add(old);
				return ((List<Object>) super.get(arg0)).add(arg1);

			} else {
				return ((List<Object>) super.get(arg0)).add(arg1);
			}
		}

		return super.put(arg0, arg1);
	}

	/**
	 * @param key
	 * @return
	 */
	public Optional<DoubleVector> getVector(String key) {
		DoubleVector value = (DoubleVector) super.get(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> Optional<List<T>> getList(String key) {
		List<T> value = (List<T>) super.get(key);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> arg0) {
		arg0.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
	}

	@Override
	public String toString() {

		String string = "\n{\n";

		if (name.isPresent()) {
			string += "\t" + this.getClass().getSimpleName() + " '" + name.get() + "'\n\n";
		}

		for (Map.Entry<String, Object> entry : entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
				.collect(Collectors.toList())) {

			String subType = "";
			if (entry.getValue() instanceof ArrayList) {
				subType = "<" + ((ArrayList<?>) entry.getValue()).get(0).getClass().getSimpleName() + ">";
			}
			String add = "\t";

			if (!subType.isEmpty()) {
				add = "";
			}

			string += "\t[" + entry.getValue().getClass().getSimpleName() + subType + "]\t" + add + entry.getKey()
					+ " = " + entry.getValue() + "\n";

		}
		return string + "}\n";
	}
}
