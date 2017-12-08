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

package org.rapidoid.platform;


import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.HTTP;
import org.rapidoid.http.Self;
import org.rapidoid.io.IO;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import static org.rapidoid.test.TestCommons.createTempDir;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class AppReloadTest extends PlatformTestCommons {

	@Test
	public void testAppReload() {
		String appsDir = createTempDir("app");
		String configYml = Msc.path(appsDir, "config.yml");

		PlatformOpts.singleAppPath(appsDir);

		isFalse(Msc.isPlatform());
		isTrue(PlatformOpts.isSingleApp());

		IO.save(configYml, makeAppConfig("my-great-app"));

		runMain("dev");

		// the proxy will wait for the app server to start
		isTrue(Self.get("/").fetch().contains("my-great-app"));

		isFalse(Self.get("/rapidoid/status").fetch().contains("my-great-app"));
		isTrue(HTTP.get("localhost:10000/").fetch().contains("my-great-app"));

		isFalse(Self.get("/rapidoid/status").fetch().contains("my-cool-app"));
		isFalse(HTTP.get("localhost:10000/").fetch().contains("my-cool-app"));

		IO.save(configYml, makeAppConfig("my-cool-app"));

		// give the platform some time to detect the changes and restart the app
		U.sleep(3000);

		// the proxy will wait for the app server to start
		isTrue(Self.get("/").fetch().contains("my-cool-app"));

		isFalse(Self.get("/rapidoid/status").fetch().contains("my-cool-app"));
		isTrue(HTTP.get("localhost:10000/").fetch().contains("my-cool-app"));

		isFalse(Self.get("/rapidoid/status").fetch().contains("my-great-app"));
		isFalse(HTTP.get("localhost:10000/").fetch().contains("my-great-app"));
	}

}
