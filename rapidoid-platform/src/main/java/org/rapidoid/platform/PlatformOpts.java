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


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.env.Env;
import org.rapidoid.util.LazyInit;
import org.rapidoid.util.Msc;

import java.io.File;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
public class PlatformOpts extends RapidoidThing {

	private static volatile String singleAppPath = "/app";
	private static volatile String appsPath = "/apps";
	private static volatile String platformPath = "/platform";

	private static final LazyInit<Boolean> singleApp = new LazyInit<>(PlatformOpts::hasAppFolder);

	public static void reset() {
		singleAppPath = "/app";
		appsPath = "/apps";
		platformPath = "/platform";
		singleApp.reset();
	}

	public static boolean hasAppFolder() {
		File app = new File(singleAppPath);
		return app.exists() && app.isDirectory();
	}

	public static boolean isMultiApp() {
		return Msc.isPlatform() && !isSingleApp();
	}

	public static boolean isMultiProcess() {
		return Msc.isPlatform() && (!isSingleApp() || Env.dev());
	}

	public static boolean isSingleApp() {
		return singleApp.get();
	}

	public static void singleApp(boolean singleApp) {
		PlatformOpts.singleApp.setValue(singleApp);
	}

	public static String singleAppPath() {
		return singleAppPath;
	}

	public static void singleAppPath(String singleAppPath) {
		PlatformOpts.singleAppPath = singleAppPath;
	}

	public static String appsPath() {
		return appsPath;
	}

	public static void appsPath(String appsPath) {
		PlatformOpts.appsPath = appsPath;
	}

	public static String platformPath() {
		return platformPath;
	}

	public static void platformPath(String platformPath) {
		PlatformOpts.platformPath = platformPath;
	}

}
