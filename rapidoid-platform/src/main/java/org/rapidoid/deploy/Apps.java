package org.rapidoid.deploy;

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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.io.IO;
import org.rapidoid.platform.PlatformOpts;
import org.rapidoid.process.Processes;
import org.rapidoid.u.U;

import java.util.Set;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class Apps extends RapidoidThing {

	private static final Deployments DEPLOYMENTS = new Deployments();

	public static Processes processes() {
		return Processes.GROUP;
	}

	public static Deployments deployments() {
		return DEPLOYMENTS;
	}

	public static Set<String> names() {
		return PlatformOpts.isSingleApp()
			? U.set("app")
			: U.set(IO.find("*").folders().in(PlatformOpts.appsPath()).getRelativeNames());
	}

	public static void reload() {
		for (String name : names()) {
			if (!deployments().exists(name)) {
				AppDeployment app = AppDeployment.create(name);
				deployments().add(new ManageableApp(app));
			}
		}
	}
}
