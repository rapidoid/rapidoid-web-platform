/*-
 * #%L
 * rapidoid-platform
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

package org.rapidoid.platform;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.RapidoidInfo;
import org.rapidoid.config.ConfigHelp;
import org.rapidoid.io.IO;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class Main extends RapidoidThing {

	public static void main(String[] args) {
		// configure platform mode
		initPlatform();

		// just print basic info if no args were specified
		if (U.isEmpty(args) && !PlatformOpts.isSingleApp()) {
			printWelcome();

		} else {
			ConfigHelp.processHelp(args);

			runMain(args);
		}
	}

	private static void initPlatform() {
		Msc.setPlatform(true);

		Log.options().fancy(!Msc.dockerized());
		Log.options().inferCaller(false);
		Log.options().showThread(false);
	}

	private static void runMain(String[] cliArgs) {
		CmdArgs args = CmdArgs.from(U.list(cliArgs));

		if (args.command == null) {
			Platform.start(args);

		} else {
			PlatformCommands.interpretCommand(args);
		}
	}

	private static void printWelcome() {
		U.print(RapidoidInfo.nameAndInfo() + "\n");

		U.print(IO.load("welcome.txt"));

		System.exit(0);
	}

}
