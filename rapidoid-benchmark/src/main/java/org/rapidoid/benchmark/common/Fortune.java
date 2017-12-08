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

package org.rapidoid.benchmark.common;


import org.rapidoid.u.U;

public class Fortune implements Comparable<Fortune> {

	private int id;
	private String message;

	public Fortune() {
	}

	public Fortune(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public Fortune setId(int id) {
		this.id = id;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Fortune setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public String toString() {
		return "Fortune{" +
			"id=" + id +
			", message='" + message + '\'' +
			'}';
	}

	@Override
	public int compareTo(Fortune o) {
		return U.compare(this.message, o.message);
	}
}
