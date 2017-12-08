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

package org.rapidoid.deploy;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.log.Log;
import org.rapidoid.platform.PlatformOpts;
import org.rapidoid.util.LazyInit;


@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class SingleApp extends RapidoidThing {

	private static final LazyInit<AppDeployment> APP = new LazyInit<>(SingleApp::create);

	private static AppDeployment create() {
		AppDeployment app = AppDeployment.create("app", PlatformOpts.singleAppPath(), 10000);
		Apps.deployments().add(new ManageableApp(app));
		return app;
	}

	public static AppDeployment get() {
		return APP.get();
	}

	public static void deploy() {
		AppDeployment app = get();

		if (!app.isEmpty()) {
			Log.info("Deploying the main application");
			app.start();
		}

		app.watch();
	}

}
