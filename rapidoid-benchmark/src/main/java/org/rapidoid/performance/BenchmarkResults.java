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
import org.rapidoid.u.U;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class BenchmarkResults extends RapidoidThing {

	int rounds = 0;

	int errors = 0;

	List<Double> throughputs = U.list();

	public DoubleSummaryStatistics stats() {
		return throughputs.stream()
			.mapToDouble(x -> x)
			.summaryStatistics();
	}

	public int bestThroughput() {
		return (int) Math.round(stats().getMax());
	}

	@Override
	public String toString() {
		return "BenchmarkResults{" +
			"rounds=" + rounds +
			", errors=" + errors +
			", throughputs=" + throughputs +
			'}' + stats();
	}

}
