/*******************************************************************************
 * <copyright> Copyright (c) since 2014 Bauhaus Luftfahrt e.V.. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU General Public License v3.0 which accompanies this distribution,
 * and is available at https://www.gnu.org/licenses/gpl-3.0.html.en </copyright>
 *******************************************************************************/
package net.bhl.opensource.toolbox.io;

import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.bhl.opensource.toolbox.time.StopWatch;

/**
 * This class generates a structured console output by creating a table view.
 *
 * @author Marc.Engelmann
 *
 */
public interface Log {

	String DIVIDER = "\n=====================================================================================\n";
	String FILLER = "->";
	String DONE = "DONE";
	String TAB = "\t";

	/**
	 * Create a Software Header
	 */
	static void createHead(String name, String version, String autors, int year) {
		System.out.println(DIVIDER);
		System.out.println(TAB + TAB + TAB + "     " + name + " v" + version);
		System.out.println(TAB + TAB + "     Created by " + autors);
		System.out.println(TAB + TAB + TAB + TAB + " © BHL " + year + " - " + Year.now().getValue());
		System.out.println(DIVIDER);
	}

	static void divider() {
		System.out.println(DIVIDER);
	}

	/**
	 * End a process by logging the time needed.
	 *
	 * @param watch
	 */
	static void end(StopWatch watch) {
		System.out.println(TAB + DONE + " (" + watch.getAdaptedTime() + ")");
	}

	/**
	 * End a process but do not mention it in the console output.
	 *
	 * @param watch
	 */
	static void endWithNoStart(StopWatch watch) {
		System.out.println(TAB + TAB + TAB + TAB + TAB + TAB + TAB + TAB + FILLER + TAB + DONE + " ("
				+ watch.getAdaptedTime() + ")");
	}

	/**
	 * Return the name of the class which called the console output method.
	 *
	 * @return
	 */
	static String getCaller() {

		int index = 3;
		String returner = Log.class.getSimpleName();

		while (returner.contentEquals(Log.class.getSimpleName())) {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			StackTraceElement element = stackTrace[index];
			List<String> list = Arrays.asList(element.getClassName().split("\\."));
			Collections.reverse(list);
			returner = list.get(0);
			index++;
		}
		return returner;
	}

	/**
	 * Print a info line containing the class name, the content and a result.
	 *
	 * @param content
	 * @param result
	 */
	static void printInfoLine(String content, String result) {

		if (result == null) {
			printStart(content, false);
			System.out.println();

		} else {
			printStart(content, true);
			System.out.println(TAB + result);
		}
	}

	/**
	 * Print the start statement. Adapt the design depending on the length of the
	 * strings
	 *
	 * @param content
	 * @param withFiller
	 */
	static void printStart(String content, boolean withFiller) {
		String title = getCaller();

		String titleTab = TAB;
		String contentTab = TAB;
		String fill = new String();

		if (title.length() < 24) {
			titleTab += TAB;
		}

		if (title.length() < 16) {
			titleTab += TAB;
		}

		if (title.length() < 8) {
			titleTab += TAB;
		}

		if (content.length() < 25) {
			contentTab += TAB;
		}

		if (content.length() < 16) {
			contentTab += TAB;
		}
		if (content.length() < 8) {
			contentTab += TAB;
		}
		if (withFiller) {
			fill = FILLER;
		}
		System.out.print(title + titleTab + content + contentTab + fill);
	}

	/**
	 * Create a new line representing a process. End this process with end().
	 *
	 * @param title
	 * @param content
	 */
	static void start(String content) {
		printStart(content, true);
	}

	/**
	 * Create a new line representing a process. This process will not have an end.
	 *
	 * @param title
	 * @param content
	 */
	static void startWithNoEnd(String content) {
		printStart(content, false);
		System.out.println();
	}
}
