/*-
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

package org.rapidoid.performance;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.config.Conf;
import org.rapidoid.setup.App;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class BenchmarkCenter extends RapidoidThing {

	public static void main(String[] args) {
//		args = new String[]{"benchmark.run=/fortunes", "benchmark.plan=t4:r3:d15:c256,512", "profiles=mysql", "hikari.maximumPoolSize=150", "jdbc.workers=16"};
		App.run(args);

		run();
	}

	public static void run() {
		BenchmarkForker forker = new BenchmarkForker();
		forker.clear();

		if (forker.hasTarget()) {
			forker.benchmark();

		} else {
			// run built-in benchmarks if no target is specified:
			runBuiltInBenchmarks(forker);
		}

		forker.printResults();
	}

	private static void runBuiltInBenchmarks(BenchmarkForker forker) {

		String run = Conf.BENCHMARK.entry("run").or("/plaintext,/json,/fortunes,/fortunes/multi,http-fast");

		for (String demo : run.split(",")) {
			demo = demo.trim();
			if (!demo.isEmpty()) {
				if (demo.equals("http-fast")) {
					forker.benchmark(org.rapidoid.benchmark.lowlevel.Main.class, "/plaintext");
					forker.benchmark(org.rapidoid.benchmark.lowlevel.Main.class, "/json");
				} else {
					forker.benchmark(org.rapidoid.benchmark.highlevel.Main.class, demo);
				}
			}
		}
	}

}
