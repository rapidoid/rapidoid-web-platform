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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.collection.Coll;

import java.io.File;
import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class ProcessParams extends RapidoidThing {

	private volatile String id;

	private volatile File in;

	private volatile String[] command;

	private volatile Processes group = Processes.GROUP;

	private volatile boolean printingOutput;

	private volatile String linePrefix = "";

	private volatile int maxLogLines = 10000;

	private volatile int terminationTimeout = 5000;

	private volatile Map<String, String> env;

	public File in() {
		return in;
	}

	public org.rapidoid.process.ProcessParams in(File in) {
		this.in = in;
		return this;
	}

	public org.rapidoid.process.ProcessParams in(String in) {
		return in(new File(in));
	}

	public String[] command() {
		return command;
	}

	public org.rapidoid.process.ProcessParams group(Processes group) {
		this.group = group;
		return this;
	}

	public Processes group() {
		return group;
	}

	public ProcessHandle run(String... command) {
		this.command = command;

		ProcessHandle handle = new ProcessHandle(this);
		handle.startProcess(this);

		return handle;
	}

	public boolean printingOutput() {
		return printingOutput;
	}

	public org.rapidoid.process.ProcessParams printingOutput(boolean printingOutput) {
		this.printingOutput = printingOutput;
		return this;
	}

	public String linePrefix() {
		return linePrefix;
	}

	public org.rapidoid.process.ProcessParams linePrefix(String linePrefix) {
		this.linePrefix = linePrefix;
		return this;
	}

	public String id() {
		return id;
	}

	public org.rapidoid.process.ProcessParams id(String id) {
		this.id = id;
		return this;
	}

	public int maxLogLines() {
		return maxLogLines;
	}

	public org.rapidoid.process.ProcessParams maxLogLines(int maxLogLines) {
		this.maxLogLines = maxLogLines;
		return this;
	}

	public int terminationTimeout() {
		return terminationTimeout;
	}

	public org.rapidoid.process.ProcessParams terminationTimeout(int terminationTimeout) {
		this.terminationTimeout = terminationTimeout;
		return this;
	}

	public Map<String, String> env() {
		return env;
	}

	public org.rapidoid.process.ProcessParams env(Map<String, String> env) {
		this.env = Coll.syncCopy(env);
		return this;
	}
}
