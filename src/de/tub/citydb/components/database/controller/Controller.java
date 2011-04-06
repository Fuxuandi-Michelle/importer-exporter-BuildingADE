package de.tub.citydb.components.database.controller;

import java.sql.Connection;
import java.sql.SQLException;

import de.tub.citydb.components.database.DatabasePlugin;
import de.tub.citydb.components.database.gui.view.components.DatabasePanel;
import de.tub.citydb.database.DBConnectionPool;
import de.tub.citydb.plugin.api.controller.DatabaseController;

public class Controller implements DatabaseController {
	private final DatabasePlugin plugin;
	private final DBConnectionPool dbPool;
	
	public Controller(DatabasePlugin plugin) {
		this.plugin = plugin;
		dbPool = DBConnectionPool.getInstance();
	}
	
	@Override
	public boolean connect() {
		// we make use of a gui component here because we want to automatically
		// print error messages in the gui
		return ((DatabasePanel)plugin.getView().getViewComponent()).connect();
	}

	@Override
	public boolean disconnect() {
		// we make use of a gui component here because we want to automatically
		// print error messages in the gui
		return ((DatabasePanel)plugin.getView().getViewComponent()).disconnect();
	}

	public boolean isConnected() {
		return dbPool.isConnected();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dbPool.getConnection();
	}

}
