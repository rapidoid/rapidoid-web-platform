/*-
 * #%L
 * rapidoid-process
 * %%
 * Copyright (C) 2014 - 2017 Nikolche Mihajlovski
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package org.rapidoid.process;


import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Str;
import org.rapidoid.io.IO;
import org.rapidoid.test.TestCommons;
import org.rapidoid.u.U;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class ProcTest extends TestCommons {

	@Test
	public void testProcessExecution() {
		Processes processes = new Processes();

		for (int i = 0; i < 5; i++) {
			ProcessHandle proc = Proc.group(processes).run("java", "-version").waitFor();

			String out = proc.outAndError().toString();
			isTrue(out.contains("Java") || out.contains("JDK") || out.contains("JRE"));

			String out2 = proc.outAndError().toString();
			eq(out2, out);
		}

		eq(processes.size(), 5);
	}

	@Test
	public void testProcessTermination() throws URISyntaxException {
		String jar = counterJar();

		ProcessHandle proc = Proc.run("java", "-cp", jar, "com.example.Main");
		proc.terminate();

		isFalse(proc.isAlive());

		List<String> lines = Proc.run("ps", "aux").waitFor().out();
		List<String> javaPs = Str.grep("counter.jar", lines);

		for (String p : javaPs) {
			U.print(p);
		}

		eq(javaPs.size(), 0);
	}

	private String counterJar() {
		File jar;

		try {
			jar = new File(IO.resource("counter.jar").toURI());
		} catch (URISyntaxException e) {
			throw U.rte(e);
		}

		isTrue(jar.exists());
		return jar.getAbsolutePath();
	}

	@Test
	public void testProcessOutput() {
		ProcessHandle proc = Proc.run("echo", "ABC\nXY").waitFor();

		eq(proc.out(), U.list("ABC", "XY"));
	}

}
