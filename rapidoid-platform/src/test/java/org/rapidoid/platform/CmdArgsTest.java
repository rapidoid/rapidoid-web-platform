package org.rapidoid.platform;

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

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.u.U;

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.4.5")
public class CmdArgsTest extends PlatformTestCommons {

	@Test
	public void testEmptyArgs() {
		CmdArgs args = CmdArgs.from(U.list());

		eq(args.all, U.list());
		isNull(args.command);
		eq(args.args, U.list());
		eq(args.options, U.list());
		eq(args.special, U.list());
	}

	@Test
	public void testNoDevArgs() {
		CmdArgs args = CmdArgs.from(U.list("aaa", "bbb=true", "c=123", "/x->foo"));

		eq(args.all, U.list("aaa", "bbb=true", "c=123", "/x->foo"));
		eq(args.command, "aaa");
		eq(args.args, U.list("bbb=true", "c=123", "/x->foo"));
		eq(args.options, U.list("bbb=true", "c=123"));
		eq(args.special, U.list("/x->foo"));
	}

	@Test
	public void testFindAndReplaceDev() {
		CmdArgs args = CmdArgs.from(U.list("dev"));

		eq(args.all, U.list(CmdArgs.DEV_CMD_ARGS));
		eq(args.command, CmdArgs.CMD_PLATFORM);
		eq(args.args, U.list("mode=dev", "app.services=center", "users.admin.password=admin", "secret=NONE", "/ -> localhost:10000"));
		eq(args.options, U.list("mode=dev", "app.services=center", "users.admin.password=admin", "secret=NONE"));
		eq(args.special, U.list("/ -> localhost:10000"));
	}

	@Test
	public void testFindAndReplaceDevWithArgs() {
		CmdArgs args = CmdArgs.from(U.list("dev", "foo=123"));

		List<String> expected = U.list(CmdArgs.DEV_CMD_ARGS);
		expected.add("foo=123");

		eq(args.all, expected);
		eq(args.command, CmdArgs.CMD_PLATFORM);

		eq(args.options, U.list("mode=dev", "app.services=center", "users.admin.password=admin", "secret=NONE", "foo=123"));
		eq(args.special, U.list("/ -> localhost:10000"));
	}

}
