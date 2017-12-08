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

package org.rapidoid.benchmark.highlevel;


import org.rapidoid.benchmark.common.Helper;
import org.rapidoid.benchmark.common.Message;
import org.rapidoid.config.Conf;
import org.rapidoid.env.Env;
import org.rapidoid.http.MediaType;
import org.rapidoid.jdbc.JDBC;
import org.rapidoid.jdbc.JdbcClient;
import org.rapidoid.log.Log;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;

public class Main {

	public static void main(String[] args) {
		App.run(args);

		Conf.HTTP.set("maxPipeline", 128);
		Conf.HTTP.set("timeout", 0);
		Conf.HTTP.sub("mandatoryHeaders").set("connection", false);

		On.port(8080);

		setupDbHandlers();
		setupSimpleHandlers();
	}

	private static void setupSimpleHandlers() {
		On.get("/plaintext").managed(false).contentType(MediaType.TEXT_PLAIN).serve("Hello, world!");
		On.get("/json").managed(false).json(() -> new Message("Hello, world!"));
	}

	private static void setupDbHandlers() {
		String dbHost = Conf.ROOT.entry("dbhost").or("localhost");
		Log.info("Database hostname is: " + dbHost);

		JdbcClient jdbc = JDBC.api();

		if (Env.hasProfile("mysql")) {
			jdbc.url("jdbc:mysql://" + dbHost + ":3306/hello_world?" + Helper.MYSQL_CONFIG);

		} else if (Env.hasProfile("postgres")) {
			jdbc.url("jdbc:postgresql://" + dbHost + ":5432/hello_world?" + Helper.POSTGRES_CONFIG);

		} else {
			jdbc.hsql("public");
			jdbc.execute("create table fortune (id int, message varchar(100))");
			jdbc.execute("insert into fortune (id, message) values (10, 'Hello')");
		}

		On.get("/fortunes").managed(false).html(new FortunesHandler(jdbc));
		On.get("/fortunes/multi").html(new FortunesMultiHandler(jdbc));
	}

}
