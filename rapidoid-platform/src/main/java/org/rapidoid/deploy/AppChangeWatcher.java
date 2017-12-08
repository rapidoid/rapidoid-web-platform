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
import org.rapidoid.io.watch.Watch;
import org.rapidoid.lambda.Operation;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.Objects;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class AppChangeWatcher extends RapidoidThing implements Operation<String> {

	private volatile boolean active = false;

	private volatile AppDeployment app;

	public void register(AppDeployment app) {
		this.app = Objects.requireNonNull(app);
	}

	public void watch() {
		U.must(app != null, "The app hasn't been registered!");

		active(true);

		if (app.exists()) {
			Log.info("Watching app root for changes...", "app", app.name(), "root", app.path());
			Watch.dir(app.path(), Watch.simpleListener(this));
		}
	}

	@Override
	public void execute(String filename) throws Exception {
		if (Msc.isAppResource(filename)) {
			onAppChanged(filename);
		}
	}

	public AppChangeWatcher active(boolean active) {
		this.active = active;
		return this;
	}

	private synchronized void onAppChanged(String filename) {
		if (active) {
			Log.info("Detected file system changes of the application", "filename", filename);

			app.onAppChanged(filename);
		}
	}

}
