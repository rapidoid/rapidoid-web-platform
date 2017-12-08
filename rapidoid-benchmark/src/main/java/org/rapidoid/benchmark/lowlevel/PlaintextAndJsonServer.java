/*-
 * #%L
 * rapidoid-benchmark
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

package org.rapidoid.benchmark.lowlevel;


import org.rapidoid.benchmark.common.Message;
import org.rapidoid.buffer.Buf;
import org.rapidoid.http.AbstractHttpServer;
import org.rapidoid.http.HttpStatus;
import org.rapidoid.http.HttpUtils;
import org.rapidoid.http.MediaType;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.impl.RapidoidHelper;

public class PlaintextAndJsonServer extends AbstractHttpServer {

	private static final byte[] URI_PLAINTEXT = "/plaintext".getBytes();

	private static final byte[] URI_JSON = "/json".getBytes();

	private static final byte[] HELLO_WORLD = "Hello, World!".getBytes();

	public PlaintextAndJsonServer() {
		super("X", "", "", false);
	}

	@Override
	protected HttpStatus handle(Channel ctx, Buf buf, RapidoidHelper data) {

		if (data.isGet.value) {
			if (matches(buf, data.path, URI_PLAINTEXT)) {
				return ok(ctx, data.isKeepAlive.value, HELLO_WORLD, MediaType.TEXT_PLAIN);

			} else if (matches(buf, data.path, URI_JSON)) {
				return serializeToJson(HttpUtils.noReq(), ctx, data.isKeepAlive.value, new Message("Hello, World!"));
			}
		}

		return HttpStatus.NOT_FOUND;
	}

}
