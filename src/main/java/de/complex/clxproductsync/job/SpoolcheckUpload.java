/*
 * BundlePreisUpload.java
 *
 * Created on 2. M�rz 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.job;

import de.complex.database.SQLLog;
import de.complex.database.exception.DbConnectionNotAvailableException;
import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.soap.datacheck.DatacheckPort;
import de.complex.clxproductsync.soap.datacheck.DatacheckService;
import de.complex.clxproductsync.soap.datacheck.DatacheckServiceLocator;
import de.complex.clxproductsync.soap.datacheck.Returnnok;
import de.complex.clxproductsync.soap.datacheck.Spoolcheck;
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

            Vector vec = new Vector();

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

                        vec.add(sc);

                    } // Schleife Artikel E
                } catch (java.sql.SQLException e) {
                    SQLLog.logger.error("SQL Error.");
                    FirebirdDb.showSQLException(e, qry, this.getClass().getName());
                    return;
                }
            } finally {
                FirebirdDb.close(rs, stmt, c);
            }

            Spoolcheck[] spoolchecks = (Spoolcheck[]) vec.toArray(new Spoolcheck[0]);

            if (spoolchecks != null) {
                SpoolcheckUpload.logger.debug("Anzahl Spoolchecks: " + spoolchecks.length);
            } else {
                SpoolcheckUpload.logger.debug("Anzahl Spoolchecks: keine");
            }

            DatacheckService service = null;
            DatacheckPort stub = null;

            try {
                if (service == null) {
                    service = new DatacheckServiceLocator();
                }
                if (stub == null) {
                    try {
                        stub = service.getDatacheckPort(new URL(ApplicationConfig.getValue("ws_url").trim() + "/datacheck"));
                    } catch (javax.xml.rpc.ServiceException se) {
                        SpoolcheckUpload.logger.error(se);
                    } catch (java.net.MalformedURLException mue) {
                        SpoolcheckUpload.logger.error(mue);
                    }
                }

                Returnnok[] returnnok = stub.setSpoolcheckList(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), spoolchecks);

                if (returnnok != null) {
                    SpoolcheckUpload.logger.debug("Anzahl Returnnok: " + returnnok.length);
                } else {
                    SpoolcheckUpload.logger.debug("Anzahl Returnnok: keine");
                }

            } catch (java.rmi.RemoteException re) {
                SpoolcheckUpload.logger.error("SOAP-Error - SpoolcheckUpload Download");
                SpoolcheckUpload.logger.error(re);

                stub = null;
                service = null;
            }
        } catch (Exception ex) {
            SpoolcheckUpload.logger.error("SpoolcheckUpload", ex);
        } finally {
            //db.shutdown();
        }

        SpoolcheckUpload.logger.info("SpoolcheckUpload Upload fertig");
    }
}
