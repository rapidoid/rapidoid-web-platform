package org.rapidoid.goodies;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.group.Manageables;
import org.rapidoid.gui.GUI;
import org.rapidoid.html.Tag;
import org.rapidoid.http.Req;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.http.Resp;
import org.rapidoid.process.ProcessHandle;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.List;

/*
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

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class ProcessDetailsHandler extends GUI implements ReqRespHandler {

	@Override
	public Object execute(Req req, Resp resp) {
		List<Object> info = U.list();

		String id = req.data("id");

		ProcessHandle handle = Manageables.find(ProcessHandle.class, id);
		U.must(handle != null, "Cannot find the process!");

		info.add(h1("Process details"));

		info.add(right(cmd("View all processes").small().go(Msc.specialUri("processes"))));
		info.add(code(U.join(" ", handle.params().command())));

		info.add(h2("Standard output:"));
		info.add(showOutput(handle.out()));

		info.add(h2("Error output:"));
		info.add(showOutput(handle.err()));

		return multi(info);
	}

	public static List<Tag> showOutput(List<String> lines) {
		List<Tag> els = U.list();

		for (String line : lines) {
			line = line.trim();

			els.add(pre(line).class_(getOutputLineClass(line)));
		}

		return els;
	}

	public static String getOutputLineClass(String line) {
		String upperLine = line.toUpperCase();

		if (upperLine.contains("[SEVERE]") || upperLine.contains(" SEVERE ")) return "proc-out proc-out-severe";
		if (upperLine.contains("[FATAL]") || upperLine.contains(" FATAL ")) return "proc-out proc-out-severe";

		if (upperLine.contains("[WARNING]") || upperLine.contains("[WARN]") ||
			upperLine.contains(" WARNING ") || upperLine.contains(" WARN ")) return "proc-out proc-out-warning";

		if (upperLine.contains("[ERROR]") || upperLine.contains(" ERROR ")) return "proc-out proc-out-error";

		return "proc-out proc-out-default";
	}

}
