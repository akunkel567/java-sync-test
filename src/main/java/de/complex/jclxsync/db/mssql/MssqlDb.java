/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.db.mssql;

import de.complex.util.lang.StringTool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class MssqlDb {

	private static Logger logger = Logger.getLogger(MssqlDb.class);
	public static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String username;
	private String password;
	private String hostname;
	private String database;
	private String instanceName;
	private Connection con = null;

	public MssqlDb(String hostname, String database, String username, String password, String instanceName) {
		this.hostname = hostname;
		this.database = database;
		this.username = username;
		this.password = password;
		this.instanceName = instanceName;

		try {
			Class.forName(MssqlDb.driverName);
		} catch (java.lang.ClassNotFoundException e) {
			logger.fatal("JDBC driver not found in class path", e);
			return;
		}

//			try {
//				d = java.sql.DriverManager.getDriver(databaseURL);
//				System.out.println("JDBC driver version "
//						  + d.getMajorVersion()
//						  + "."
//						  + d.getMinorVersion()
//						  + " registered with driver manager.");
//			} catch (java.sql.SQLException e) {
//				System.out.println("Unable to find JDBC driver among the registered drivers.");
//				showSQLException(e);
//				return;
//			}
	}

	public String getConnectionUrl() {

		String url = "jdbc:sqlserver://" + this.hostname + ";databaseName=" + this.database + ";integratedSecurity=false";
		
		if (!StringTool.isEmpty(this.instanceName, true)) {
			url = url + ";instanceName=" + this.instanceName;
		}
		
		logger.debug("ConnectionUrl: " + url);
		
		return url;
	}

	public Connection getConnection() throws SQLException {

		if (this.con == null || this.con.isClosed()) {
			this.con = java.sql.DriverManager.getConnection(this.getConnectionUrl(), this.username, this.password);
			logger.debug("Connection established.");
		}

		return this.con;
	}

	public void closeConnection() {
		if (this.con != null) {
			try {
				this.con.close();
			} catch (SQLException ex) {
				logger.error(ex, ex);
			}
		}
	}
}
