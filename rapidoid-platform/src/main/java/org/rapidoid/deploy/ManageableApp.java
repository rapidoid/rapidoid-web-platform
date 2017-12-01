package org.rapidoid.deploy;

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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Order;
import org.rapidoid.annotation.Since;
import org.rapidoid.group.*;
import org.rapidoid.gui.GUI;
import org.rapidoid.process.ProcessHandle;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.util.Date;
import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.4.7")
@ManageableBean(kind = "apps")
public class ManageableApp extends AbstractManageable {

	private final AppDeployment app;

	public ManageableApp(AppDeployment app) {
		this.app = app;
	}

	@Override
	public String id() {
		return app.name();
	}

	@Override
	public List<String> getManageableProperties() {
		return U.list("id", "location", "exists", "empty", "alive", "port", "startedAt",
			"finishedAt", "exitCode", "uptime", "details", "open");
	}

	@Action()
	@Order(100)
	public void deploy() {
		app.deploy();
	}

	@ActionCondition
	public boolean canDeploy() {
		return isStaged();
	}

	@Action
	@Order(10)
	public void start() {
		app.start();
	}

	@ActionCondition
	public boolean canStart() {
		return exists() && !isAlive();
	}

	@Action
	@Order(30)
	public void stop() {
		app.stop();
	}

	@ActionCondition
	public boolean canStop() {
		return exists() && isAlive();
	}

	@Action
	@Order(20)
	public void restart() {
		app.restart();
	}

	@ActionCondition
	public boolean canRestart() {
		return exists() && isAlive();
	}

	@Action(name = "!delete")
	@Order(Integer.MAX_VALUE)
	public void delete() {
		app.delete();
	}

	@ActionCondition(name = "!delete")
	public boolean canDelete() {
		return exists();
	}

	public boolean exists() {
		return app.exists();
	}

	public boolean isEmpty() {
		return app.isEmpty();
	}

	public String location() {
		return app.path();
	}

	@Action
	@Order(1)
	public void create() {
		app.create();
	}

	@ActionCondition
	public boolean canCreate() {
		return !exists();
	}

	public boolean isAlive() {
		return app.isAlive();
	}

	public boolean isStaged() {
		return app.isStaged();
	}

	public int port() {
		return app.port();
	}

	public Date startedAt() {
		ProcessHandle proc = app.process();
		return proc != null ? proc.startedAt() : null;
	}

	public Date finishedAt() {
		ProcessHandle proc = app.process();
		return proc != null ? proc.finishedAt() : null;
	}

	public Integer exitCode() {
		ProcessHandle proc = app.process();
		return proc != null ? proc.exitCode() : null;
	}

	public String uptime() {
		ProcessHandle proc = app.process();
		return proc != null ? proc.uptime() : null;
	}

	public Object details() {
		List<Object> links = U.list();

		if (Apps.processes().exists(id())) {
			String processUri = Msc.specialUri("processes/" + id());

			links.add(GUI.btn("View process")
				.class_("btn btn-default btn-xs")
				.go(processUri));
		}

		return GUI.multi(links.toArray());
	}

	public Object open() {
		if (Apps.processes().exists(id())) {

			String appUri = "/" + id();

			return GUI.btn("Open")
				.class_("btn btn-primary btn-xs")
				.go(appUri);

		} else {
			return null;
		}
	}

	@Override
	public GroupOf<? extends Manageable> group() {
		return Apps.deployments();
	}
}
