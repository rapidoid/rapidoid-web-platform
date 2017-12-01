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
import org.rapidoid.deploy.AppDeployment;
import org.rapidoid.deploy.Apps;
import org.rapidoid.io.IO;
import org.rapidoid.io.Upload;
import org.rapidoid.u.U;

import java.util.Set;

import static org.rapidoid.test.TestCommons.createTempDir;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class AppDeploymentTest extends PlatformTestCommons {

	@Test
	public void testAppDeployment() {
		String appsDir = createTempDir("apps");
		PlatformOpts.appsPath(appsDir);

		// 0 APPS

		isTrue(Apps.names().isEmpty());
		eq(ls(), U.set());

		// CREATE foo

		final Upload fooJar = new Upload("foo.jar", new byte[]{1, 2, 3});
		AppDeployment foo = AppDeployment.fromFilename(fooJar.filename());

		isFalse(foo.exists());
		isTrue(foo.isEmpty());

		// STAGE foo

		foo.stage(fooJar.filename(), fooJar.content());

		isTrue(foo.exists());
		isFalse(foo.isEmpty());

		eq(Apps.names(), U.set("foo"));
		eq(ls(), U.set("foo", "foo/foo.jar.staged"));

		// CREATE bar

		Upload barZip = new Upload("bar.zip", new byte[]{4, 5});
		AppDeployment bar = AppDeployment.fromFilename(barZip.filename());

		isFalse(bar.exists());
		isTrue(bar.isEmpty());

		// STAGE bar

		bar.stage(barZip.filename(), barZip.content());

		isTrue(bar.exists());
		isFalse(bar.isEmpty());

		eq(Apps.names(), U.set("foo", "bar"));
		eq(ls(), U.set("foo", "bar", "foo/foo.jar.staged", "bar/bar.zip.staged"));

		// DEPLOY foo

		foo.deploy();

		eq(ls(), U.set("foo", "bar", "foo/foo.jar", "bar/bar.zip.staged"));

		// DEPLOY bar

		bar.deploy();

		eq(ls(), U.set("foo", "bar", "foo/foo.jar", "bar/bar.zip"));

		// 2 APPS (foo and bar)

		eq(Apps.names(), U.set("foo", "bar"));

		// DELETE foo

		foo.delete();

		eq(Apps.names(), U.set("bar"));
		eq(ls(), U.set("bar", "bar/bar.zip"));

		// DELETE bar

		bar.delete();

		eq(Apps.names(), U.set());
		eq(ls(), U.set());
	}

	private Set<String> ls() {
		return U.set(IO.find().recursive().in(PlatformOpts.appsPath()).getRelativeNames());
	}

}
