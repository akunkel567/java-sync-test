/*
 * BundlePreisUpload.java
 *
 * Created on 2. M�rz 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.job;

import de.complex.clxproductsync.soap.PortFactory;
import de.complex.clxproductsync.soap.datacheck.Spoolcheck;
import de.complex.clxproductsync.soap.datacheck.SpoolcheckList;
import de.complex.clxproductsync.soap.transfer.Returnnok;
import de.complex.clxproductsync.soap.transfer.ReturnnokList;
import de.complex.database.SQLLog;
import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.tools.config.ApplicationConfig;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Kunkel
 */
public class SpoolcheckUpload extends Thread {

    private static Logger logger = LogManager.getLogger(SpoolcheckUpload.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");
    private Properties prop;

    /**
     * Creates a new instance of BundlePreisUpload
     */
    public SpoolcheckUpload(Properties prop) {
        this.prop = prop;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SpoolcheckUpload " + sdf.format(new Date()));
        SpoolcheckUpload.logger.debug("SpoolcheckUpload Upload run");
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        FirebirdDb db = null;
        try {
            db = FirebirdDbPool.getInstance();

            java.sql.Connection c = null;
            java.sql.Statement stmt = null;
            java.sql.ResultSet rs = null;
            String qry = null;

            SpoolcheckList spoolcheckList = new SpoolcheckList();

            try {
                try {
                    c = db.getConnection();
                    stmt = c.createStatement();

                    qry = "select snjob.EVENTNAME, sum(snjob.FREMDID) as IDSUM, count(snjob.snjobid) as ANZAHL from snjob where snjob.DONE = 0 group by 1";
                    SQLLog.logger.debug("SQL-Query :" + qry);

                    rs = stmt.executeQuery(qry);
                    while (rs.next()) { // Schleife Artikel A

                        Spoolcheck sc = new Spoolcheck();
                        sc.setName(rs.getString("EVENTNAME"));
                        sc.setAnzahl(rs.getInt("ANZAHL"));
                        sc.setSadt("");
                        sc.setNow("");
                        sc.setId(0);
                        sc.setIdsum(rs.getInt("IDSUM"));

                        spoolcheckList.getItem().add(sc);

                    } // Schleife Artikel E
                } catch (java.sql.SQLException e) {
                    SQLLog.logger.error("SQL Error.");
                    FirebirdDb.showSQLException(e, qry, this.getClass().getName());
                    return;
                }
            } finally {
                FirebirdDb.close(rs, stmt, c);
            }

            if (spoolcheckList != null) {
                SpoolcheckUpload.logger.debug("Anzahl Spoolchecks: " + spoolcheckList.getItem().size());
            } else {
                SpoolcheckUpload.logger.debug("Anzahl Spoolchecks: keine");
            }

            try {
                new PortFactory().getDatacheckPort().setSpoolcheckList(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), spoolcheckList);
            } catch (Exception re) {
                SpoolcheckUpload.logger.error("SOAP-Error - SpoolcheckUpload Download");
                SpoolcheckUpload.logger.error(re);
            }
        } catch (Exception ex) {
            SpoolcheckUpload.logger.error("SpoolcheckUpload", ex);
        } finally {
            //db.shutdown();
        }

        SpoolcheckUpload.logger.info("SpoolcheckUpload Upload fertig");
    }
}
