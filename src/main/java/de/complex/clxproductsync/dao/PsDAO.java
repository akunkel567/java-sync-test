
/*
 * WebAkDAO.java
 *
 * Created on 2. August 2006, 14:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.dao;

import de.complex.database.SQLLog;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.xml.XmlCol;
import de.complex.clxproductsync.xml.XmlData;
import de.complex.clxproductsync.xml.XmlRow;
import de.complex.clxproductsync.xml.XmlTable;
import org.apache.log4j.*;

/**
 *
 * @author kunkel
 */
public class PsDAO extends AbstractDAO {

	private static Logger logger = Logger.getLogger(PsDAO.class);

	public PsDAO(FirebirdDb db) {
		super(db);
	}

	@Override
	public Integer[] getIdList() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean updatePs(XmlData data) {
		boolean error = true;
		int resultCount = 0;
		String qry = null;

		java.sql.Connection c = null;
		java.sql.Statement stmt = null;
		java.sql.ResultSet rs = null;

		int iPsId = 0;
		int iSession = 0;
		int iDateTime = 0;

		try {
			c = this.db.getConnection();
			XmlTable table = data.getTable();
			PsDAO.logger.debug("XmlData table rowCount: " + table.getRows().size());
			for (XmlRow row : table.getRows()) {
				iPsId = row.getCol().indexOf(new XmlCol("packstueckid"));
				iSession = row.getCol().indexOf(new XmlCol("warenkorb_session"));
				iDateTime = row.getCol().indexOf(new XmlCol("warenkorb_datetime"));

				if (iPsId != -1 && iSession != -1 && iDateTime != -1) {
					qry = "UPDATE PS SET PS.SESSIONID = '" + row.getCol().get(iSession).getValue() + "'"
							  + " , PS.SESSIONTS = " + (row.getCol().get(iDateTime).getValue().equals("0000-00-00 00:00:00") ? "null" : "'" + row.getCol().get(iDateTime).getValue() + "'")
							  + " WHERE PS.ID = " + row.getCol().get(iPsId).getValue();

					try {
						stmt = c.createStatement();
						SQLLog.logger.debug("SQL-Query :" + qry);
						resultCount = stmt.executeUpdate(qry);
					} catch (Exception e) {
						SQLLog.logger.error("", e);
					}
				}
			}

			PsDAO.logger.debug("*** Update PS count:" + resultCount);
			if (resultCount == 1) {
				error = false;
			}
		} finally {
			try {
				if (!error) {
					SQLLog.logger.debug("************ Commit *************");
					c.commit();
				} else {
					SQLLog.logger.error("*** updatePS Rollback **************");
					c.rollback();
				}
			} catch (java.sql.SQLException e) {
				PsDAO.logger.error("", e);
				FirebirdDb.showSQLException(e, "", this.getClass().getName());
			}

			FirebirdDb.close(rs, stmt, c);

			return !error;
		}
	}
}
