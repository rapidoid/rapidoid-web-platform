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


import org.junit.Before;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.deploy.AppDeployment;
import org.rapidoid.test.RapidoidIntegrationTest;

import java.io.File;

@Authors("Nikolche Mihajlovski")
@Since("5.4.6")
public class PlatformTestCommons extends RapidoidIntegrationTest {

	@Before
	public final void beforePlatformTest() {
		PlatformOpts.reset(); // FIXME remove when module registration is fixed
	}

	static String appPath(String name) {
		return new File("rapidoid-platform/src/test/" + name).getAbsolutePath();
	}

	static void runMain(String... args) {
		Main.main(args);
	}

	static void makeAppDeployment(String name) {
		AppDeployment app = AppDeployment.create(name);

		app.stage("config.yml", makeAppConfig(name).getBytes());
	}

	static String makeAppConfig(String appId) {
		String config = "" +
			"pages:\n" +
			"  /: ''\n\n" +
			"gui:\n" +
			"  brand: " + appId + "\n\n";

		return config;
	}

}
