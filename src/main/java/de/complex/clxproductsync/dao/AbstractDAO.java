/*
 * AbstractDAO.java
 *
 * Created on 3. Juli 2006, 17:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.dao;

import de.complex.database.SQLLog;
import de.complex.database.exception.DbConnectionNotAvailableException;
import de.complex.database.firebird.FirebirdDb;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.*;

/**
 *
 * @author kunkel
 */
public abstract class AbstractDAO {

	private static Logger logger = Logger.getLogger(AbstractDAO.class);
	protected FirebirdDb db;
	protected Properties prop;

	/**
	 * Creates a new instance of AbstractDAO
	 */
	public AbstractDAO(FirebirdDb db) {
		this.setDatabase(db);
	}

	public void setDatabase(FirebirdDb db) {
		this.db = db;
	}

	protected Integer[] getIdList(String qry, String idFieldName) {
		java.sql.Connection c = null;
		java.sql.Statement stmt = null;
		java.sql.ResultSet rs = null;

		Vector vec = new Vector();

		try {
			c = this.db.getConnection();
			if (c != null) {
				stmt = c.createStatement();

				SQLLog.logger.debug("SQL-Query :" + qry);

				rs = stmt.executeQuery(qry);
				while (rs.next()) { // Schleife Artikel A
					vec.add(new Integer(rs.getInt(idFieldName)));
				} // Schleife Artikel E
			}
		} catch (java.sql.SQLException e) {
			SQLLog.logger.error("SQL Error.");
			FirebirdDb.showSQLException(e, qry, this.getClass().getName());
			return null;
		} finally {
			FirebirdDb.close(rs, stmt, c);
		}

		return (Integer[]) vec.toArray(new Integer[0]);
	}

	public Integer getIntegerValue(String qry, String idFieldName) {
		java.sql.Connection c = null;
		java.sql.Statement stmt = null;
		java.sql.ResultSet rs = null;

		Integer i = null;

		try {
			c = this.db.getConnection();
			if (c != null) {
				stmt = c.createStatement();

				SQLLog.logger.debug("SQL-Query :" + qry);

				rs = stmt.executeQuery(qry);

				while (rs.next()) { // Schleife Artikel A
					i = new Integer(rs.getInt(idFieldName));
				} // Schleife Artikel E
			}
		} catch (java.sql.SQLException e) {
			SQLLog.logger.error("SQL Error.");
			FirebirdDb.showSQLException(e, qry, this.getClass().getName());
			return null;
		} finally {
			FirebirdDb.close(rs, stmt, c);
		}

		return i;
	}

	public abstract Integer[] getIdList();
}
