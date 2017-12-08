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

package org.rapidoid.deploy.handler;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.deploy.Apps;
import org.rapidoid.deploy.ManageableApp;
import org.rapidoid.http.NiceResponse;
import org.rapidoid.http.Req;
import org.rapidoid.http.ReqHandler;
import org.rapidoid.log.Log;
import org.rapidoid.platform.PlatformOpts;
import org.rapidoid.u.U;


@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class AppDeploymentHandler extends RapidoidThing implements ReqHandler {

	@Override
	public Object execute(Req req) {
		try {

			String appName;
			if (PlatformOpts.isSingleApp()) {
				appName = "app";
				U.must(req.param("app", null) == null, "The 'app' parameter is not needed for single-app deployments!");

			} else {
				appName = req.param("app");
			}

			ManageableApp app = Apps.deployments().find(appName);
			U.must(app != null, "Cannot find application with name: '%s'", appName);

			app.deploy();

		} catch (Exception e) {
			Log.error("Deployment failed!", e);
			return NiceResponse.err(req, e);
		}

		return NiceResponse.ok(req, "Successfully deployed the application");
	}

}
