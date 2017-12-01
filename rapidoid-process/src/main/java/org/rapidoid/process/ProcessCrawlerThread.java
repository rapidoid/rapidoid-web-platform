package org.rapidoid.process;

/*
 * #%L
 * rapidoid-process
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

import org.rapidoid.activity.AbstractLoopThread;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.collection.Coll;

import java.util.Set;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class ProcessCrawlerThread extends AbstractLoopThread {

	private final Set<ProcessHandle> handles;

	ProcessCrawlerThread(Set<ProcessHandle> handles) {
		super("process-crawler");
		setDaemon(true);

		this.handles = handles;
	}

	@Override
	protected void loop() {
		// use a copy because the handles set is synchronized
		for (ProcessHandle handle : Coll.copyOf(handles)) {
			visit(handle);
		}
	}

	private void visit(ProcessHandle handle) {
		if (!handle.isAlive() && handle.finishedAt() == null) {
			handle.onTerminated();
		}
	}

}
