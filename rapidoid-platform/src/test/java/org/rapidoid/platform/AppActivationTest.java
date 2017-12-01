package org.rapidoid.platform;

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

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.env.Env;
import org.rapidoid.util.Msc;

import static org.rapidoid.test.TestCommons.createTempDir;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class AppActivationTest extends PlatformTestCommons {

	@Test
	public void testAppDeployment() {
		String appsDir = createTempDir("app");
		PlatformOpts.singleAppPath(appsDir);

		isFalse(Msc.isPlatform());
		isTrue(PlatformOpts.isSingleApp());
		isFalse(Env.isInitialized());

		runMain("mode=production");

		isTrue(Env.isInitialized());
		isTrue(Msc.isPlatform());
		isTrue(PlatformOpts.isSingleApp());
		isTrue(Env.production());
	}

}
