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
import org.rapidoid.cls.Cls;
import org.rapidoid.config.Conf;
import org.rapidoid.io.IO;
import org.rapidoid.log.Log;
import org.rapidoid.setup.App;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.Arrays;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class BenchmarkRunner extends RapidoidThing {

	public static void main(String[] args) {
		Msc.setPlatform(true);
		App.run(args);

		String mainClass = Conf.BENCHMARK.entry("main").str().getOrNull();
		String uri = Conf.BENCHMARK.entry("target").str().getOrNull();
		boolean passive = Conf.BENCHMARK.entry("passive").or(false);

		String desc = mainClass + " " + uri + (passive ? " [PASSIVE]" : "");

		// start the app that will be benchmarked
		Msc.invokeMain(Cls.get(mainClass), args);

		if (!passive) {

			String plan = Conf.BENCHMARK.entry("plan").str().getOrNull();
			String filename = Conf.BENCHMARK.entry("file").str().getOrNull();
			String url = Conf.BENCHMARK.entry("target").str().getOrNull();

			benchmark(desc, plan, filename, url);

			System.exit(0);
		}
	}

	public static void benchmark(String desc, String plan, String filename, String url) {

		WrkSetup wrk = Perf.wrk()
			.url(url);

		for (String part : plan.split(":")) {
			part = part.toLowerCase();

			char flag = part.charAt(0);
			String val = part.substring(1);

			switch (flag) {
				case 't':
					wrk.threads(U.num(val));
					break;

				case 'r':
					wrk.rounds(U.num(val));
					break;

				case 'd':
					int duration = U.num(val);
					wrk.duration(duration);
					if (wrk.warmUp() < 0) wrk.warmUp(duration);
					wrk.pause(duration);
					break;

				case 'c':
					wrk.connections(Arrays.stream(val.split(",")).mapToInt(U::num).toArray());
					break;

				case 'p':
					wrk.pipeline(U.num(val));
					break;

				case 's':
					wrk.showDetails(false).showWarmUpDetails(false);
					break;

				case 'w':
					wrk.warmUp(U.num(val));
					break;

				default:
					throw U.rte("Unknown benchmark option: " + flag);
			}
		}

		Log.info("Running benchmark", "setup", wrk);

		BenchmarkResults results = wrk.run();

		String info = U.frmt("%s => %s", desc, results.bestThroughput());
		U.print(info);

		IO.append(filename, (info + "\n").getBytes());
	}

}
