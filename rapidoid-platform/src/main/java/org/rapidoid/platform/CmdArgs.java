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
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.4.5")
public class CmdArgs extends RapidoidThing {

	static final String CMD_PLATFORM = "platform";

	static final String[] DEV_CMD_ARGS = {
		CMD_PLATFORM,
		"mode=dev",
		"app.services=center",
		"users.admin.password=admin",
		"secret=NONE",
		"/ -> localhost:10000"
	};

	public final List<String> all;
	public final String command;
	public final List<String> args;
	public final List<String> options;
	public final List<String> refs;
	public final List<String> special;

	private CmdArgs(List<String> all, String command, List<String> args, List<String> options, List<String> refs, List<String> special) {
		this.all = all;
		this.command = command;
		this.args = args;
		this.options = options;
		this.refs = refs;
		this.special = special;
	}

	public static CmdArgs from(List<String> all) {

		// replace shortcuts like "dev"
		findAndReplaceDevArg(all);

		List<String> args = U.list(all);

		// extract command
		String command = null;
		if (U.notEmpty(args) && isCommand(args.get(0))) {
			command = args.remove(0);
		}

		// extract options, references and special args
		List<String> opts = U.list();
		List<String> refs = U.list();
		List<String> special = U.list();
		extractOptionsAndRefs(args, opts, refs, special);

		return new CmdArgs(all, command, args, opts, refs, special);
	}

	public static CmdArgs from(String... args) {
		return from(U.list(args));
	}

	private static boolean isCommand(String s) {
		return s.matches("[a-z]+");
	}

	private static void findAndReplaceDevArg(List<String> args) {
		int pos = args.indexOf("dev");

		if (pos >= 0) {
			// replace "dev" with multiple args
			args.remove(pos);
			args.addAll(pos, U.list(DEV_CMD_ARGS));
		}
	}

	private static void extractOptionsAndRefs(List<String> args, List<String> opts, List<String> refs, List<String> special) {
		for (String arg : args) {

			if (arg.startsWith("@")) {
				refs.add(arg.substring(1));

			} else if (arg.contains("=")) {
				opts.add(arg);

			} else if (Msc.isSpecialArg(arg)) {
				special.add(arg);

			} else {
				throw U.rte("Invalid argument: '%s'!", arg);
			}
		}
	}

	public void print() {
		Log.info("Command-line arguments:");

		for (String arg : all) {
			Log.info("  " + arg);
		}

		Log.info("");
	}

}
