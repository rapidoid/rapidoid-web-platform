package org.rapidoid.deploy;

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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.HTTP;
import org.rapidoid.http.HttpClient;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.ByteArrayInputStream;
import java.io.File;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class AppDownloader extends RapidoidThing {

	private static final String REPOSITORY_NAME = "[-\\w]+";
	private static final String GITHUB_REPO_ZIP = "https://github.com/%s/%s/archive/master.zip";

	private static final HttpClient client = HTTP.client().followRedirects(true);

	public static void download(String appRef, String appsFolder) {
		String url = getAppUrl(appRef);

		Log.info("Downloading application", "application", appRef, "url", url);
		byte[] zip = client.get(url).execute().bodyBytes();

		String zipRoot = Msc.detectZipRoot(new ByteArrayInputStream(zip));

		String destination, zipDest;

		if (zipRoot != null) {
			destination = appsFolder + File.separator + zipRoot;
			zipDest = appsFolder;

		} else {
			destination = appsFolder + File.separator + Msc.textToId(appRef);
			zipDest = destination;
		}

		Log.info("Extracting application", "application", appRef, "destination", destination);
		Msc.unzip(new ByteArrayInputStream(zip), zipDest);
	}

	public static String getAppUrl(String appRef) {
		String url;

		if (appRef.matches(REPOSITORY_NAME)) {
			url = U.frmt(GITHUB_REPO_ZIP, "rapidoid", appRef);

		} else if (appRef.matches(REPOSITORY_NAME + "/" + REPOSITORY_NAME)) {
			url = U.frmt(GITHUB_REPO_ZIP, (Object[]) appRef.split("/"));

		} else {
			url = appRef;
		}

		return url;
	}

}
