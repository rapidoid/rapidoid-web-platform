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

package org.rapidoid.goodies;


import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.group.GroupOf;
import org.rapidoid.group.Groups;
import org.rapidoid.gui.GUI;
import org.rapidoid.gui.Grid;
import org.rapidoid.lambda.Mapper;
import org.rapidoid.process.ProcessHandle;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.List;
import java.util.concurrent.Callable;


@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class ProcessesHandler extends GUI implements Callable<Object> {

	public static final String[] COLUMNS = {
		"id",
		"cmd",
		"args",
		"$.params().in()",
		"alive",
		"exitCode",
		"$.duration() / 1000",
		"startedAt",
		"finishedAt",
		"$.group().kind()",
		"(actions)"
	};

	public static final Object[] COLUMN_NAMES = {
		"ID",
		"Command",
		"Arguments",
		"Location",
		"Is alive?",
		"Exit code",
		"Duration (sec)",
		"Started at",
		"Finished at",
		"Group",
		"Actions",
	};

	@Override
	public Object call() throws Exception {
		List<Object> info = U.list();

		info.add(h3("Managed processes:"));

		List<GroupOf<ProcessHandle>> gr = Groups.find(ProcessHandle.class);

		List<ProcessHandle> processes = U.list();

		for (GroupOf<ProcessHandle> group : gr) {
			processes.addAll(group.items());
		}

		Grid grid = grid(processes)
			.columns(COLUMNS)
			.headers(COLUMN_NAMES)
			.toUri(new Mapper<ProcessHandle, String>() {
				@Override
				public String map(ProcessHandle handle) throws Exception {
					return Msc.specialUri("processes/" + handle.id());
				}
			})
			.pageSize(100);

		info.add(grid);

		info.add(autoRefresh(2000));
		return multi(info);
	}

}
