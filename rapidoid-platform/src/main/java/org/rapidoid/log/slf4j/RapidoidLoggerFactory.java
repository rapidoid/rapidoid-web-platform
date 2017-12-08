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

package org.rapidoid.log.slf4j;


import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.collection.Coll;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class RapidoidLoggerFactory extends RapidoidThing implements ILoggerFactory {

	private final Map<String, RapidoidLogger> loggersByName = Coll.autoExpandingMap(RapidoidLogger::new);

	@Override
	public Logger getLogger(String name) {
		return loggersByName.get(name);
	}

}
