/*-
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

package org.rapidoid.process;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;

import java.io.File;


@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class Proc extends RapidoidThing {

	public static ProcessHandle run(String... command) {
		return new ProcessParams().run(command);
	}

	public static ProcessParams in(File dir) {
		return new ProcessParams().in(dir);
	}

	public static ProcessParams in(String dir) {
		return in(new File(dir));
	}

	public static ProcessParams group(Processes group) {
		return new ProcessParams().group(group);
	}

	public static ProcessParams id(String id) {
		return new ProcessParams().id(id);
	}

	public static ProcessParams printingOutput(boolean printingOutput) {
		return new ProcessParams().printingOutput(printingOutput);
	}

	public static ProcessParams linePrefix(String linePrefix) {
		return new ProcessParams().linePrefix(linePrefix);
	}

}
