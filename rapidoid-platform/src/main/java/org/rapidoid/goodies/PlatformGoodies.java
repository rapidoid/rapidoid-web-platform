package org.rapidoid.goodies;

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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.deploy.handler.AppDeploymentHandler;
import org.rapidoid.deploy.handler.AppStagingHandler;
import org.rapidoid.deploy.handler.DeployHandler;
import org.rapidoid.setup.Setup;

@Authors("Nikolche Mihajlovski")
@Since("5.3.3")
public class PlatformGoodies extends Goodies {

	public static void processes(Setup setup) {
		setup.page(uri("processes")).zone(CENTER).mvc(new ProcessesHandler());
		setup.page(uri("processes/{id}")).zone(CENTER).mvc(new ProcessDetailsHandler());
	}

	public static void deployment(Setup setup) {
		setup.page(uri("deployment")).zone(Goodies.CENTER).mvc(new DeployHandler());
		setup.post(uri("stage")).zone(Goodies.CENTER).json(new AppStagingHandler());
		setup.post(uri("deploy")).zone(Goodies.CENTER).json(new AppDeploymentHandler());
	}

	public static void platformAdminCenter(Setup setup) {
		adminCenter(setup);
		deployment(setup);
		processes(setup);
	}

}
