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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.crypto.Crypto;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class PasswordHashTool extends RapidoidThing {

	static void generatePasswordHash() {
		char[] password = readPassword();

		U.print("\nYour salted password hash is:\n");

		String hash = Crypto.passwordHash(password);
		U.must(Crypto.passwordMatches(password, hash), "Password hash verification error!");

		U.print(hash);
		U.print("");
	}

	private static char[] readPassword() {
		char[] password = readPassword("Enter a new password: ");
		char[] password2 = readPassword("Enter the same password again: ");

		if (Arrays.equals(password, password2)) {
			return password;
		} else {
			U.print("[ERROR] The passwords don't match!\n");
			return readPassword();
		}
	}

	private static char[] readPassword(String msg) {
		Console console = System.console();

		if (console != null) {
			return console.readPassword(msg);

		} else {
			U.print(msg);
			return readLine().toCharArray();
		}
	}

	private static String readLine() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			String line = reader.readLine();

			if (line == null) {
				if (Msc.dockerized()) {
					throw U.rte("Couldn't read from the standard input! Please make sure you run the docker command with '-it'");
				} else {
					throw U.rte("Couldn't read from the standard input!");
				}
			}

			return line;

		} catch (IOException e) {
			throw U.rte(e);
		}
	}

}
