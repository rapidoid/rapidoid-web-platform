package org.rapidoid.performance;

/*
 * #%L
 * rapidoid-benchmark
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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.config.Conf;
import org.rapidoid.env.Env;
import org.rapidoid.io.IO;
import org.rapidoid.log.Log;
import org.rapidoid.process.Proc;
import org.rapidoid.process.ProcessHandle;
import org.rapidoid.scan.ClasspathUtil;
import org.rapidoid.u.U;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class BenchmarkForker extends RapidoidThing {

	private final String resultsFile;
	private final String plan;
	private final String target;

	public BenchmarkForker() {
		try {
			this.resultsFile = File.createTempFile("benchmark", ".txt").getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't create temporary file!", e);
		}

		this.plan = Conf.BENCHMARK.entry("plan").str().getOrNull();
		this.target = Conf.BENCHMARK.entry("target").str().getOrNull();

		Log.info("!Starting benchmark", "!configuration", Conf.BENCHMARK.toMap());
	}

	public void clear() {
		IO.save(resultsFile, "BENCHMARK plan: " + plan + "\n");
	}

	public ProcessHandle benchmark(Class<?> mainClass, String uri) {

		String classpath = U.join(":", ClasspathUtil.getClasspath());
		String runner = BenchmarkRunner.class.getCanonicalName();
		String main = mainClass.getCanonicalName();

		List<String> cmdWithArgs = U.list("java", "-Xms1g", "-Xmx1g", "-Dfile.encoding=UTF-8", "-classpath", classpath, runner);

		cmdWithArgs.add("benchmark.target=" + uri);
		cmdWithArgs.add("benchmark.main=" + main);
		cmdWithArgs.add("benchmark.file=" + resultsFile);

		cmdWithArgs.addAll(Env.args());

		ProcessHandle proc = Proc.printingOutput(true)
			.linePrefix("[BENCHMARK] ")
			.run(U.arrayOf(cmdWithArgs))
			.waitFor();

		return proc;
	}

	void printResults() {
		U.print("");
		U.print(IO.load(resultsFile));
	}

	boolean hasTarget() {
		return target != null;
	}

	public void benchmark() {
		BenchmarkRunner.benchmark(target, plan, resultsFile, target);
	}

}
