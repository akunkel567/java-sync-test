/*
 * BundlePreisUpload.java
 *
 * Created on 2. M�rz 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import de.complex.database.SQLLog;
import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.db.mssql.MssqlDb;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.tools.config.ApplicationConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Kunkel
 */
public class CDHZulaufinfo extends Thread {

    private static Logger logger = LogManager.getLogger(CDHZulaufinfo.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");
    private Properties prop;

    /**
     * Creates a new instance of BundlePreisUpload
     */
    public CDHZulaufinfo(Properties prop) {
        this.prop = prop;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("CDHZulaufinfo " + sdf.format(new Date()));
        CDHZulaufinfo.logger.info("CDHZulaufinfo run");
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        FirebirdDb db = null;
        MssqlDb msdb = null;

        Date lastupdate = new Date();

        int commitNach = 500;
        int commitCount = 0;

        String cdh_username = ApplicationConfig.getValue("cdh.username", "");
        String cdh_password = ApplicationConfig.getValue("cdh.password", "");
        String cdh_hostname = ApplicationConfig.getValue("cdh.hostname", "");
        String cdh_database = ApplicationConfig.getValue("cdh.database", "");
        String cdh_instanceName = ApplicationConfig.getValue("cdh.instance", "");

        try {
            db = FirebirdDbPool.getInstance();
            CDHZulaufinfo.logger.debug("FirebirdDb: " + db);

            msdb = new MssqlDb(cdh_hostname, cdh_database, cdh_username, cdh_password, cdh_instanceName);
            CDHZulaufinfo.logger.debug("Mssql-Db: " + msdb);

            String cdhSql = "select ORDER_DETAIL.ORDER_NO_REP"
                    + " , ORDER_DETAIL.QUANTITY"
                    + " , ORDER_DETAIL.DELIVERY_WEEK_NO"
                    + " , ORDER_DETAIL.ORDD_RES_FLD_ALPHA12"
                    + " , ORDER_DETAIL.ARTICLE_SERIAL_NO"
                    + " from ORDER_DETAIL"
                    + " where ORDER_DETAIL.ORDER_OFFER_TYPE=8";

            String fbInsert = "INSERT INTO ARTGROESSEZULAUF ("
                    + " ARTGROESSEZULAUF.ARTGROESSEID," //1
                    + " ARTGROESSEZULAUF.MENGE,"//2
                    + " ARTGROESSEZULAUF.LIEFERKW,"//3
                    + " ARTGROESSEZULAUF.STATUS"//4
                    + ") VALUES "
                    + "(?,?,?,?)";

            java.sql.Connection con = null;

            java.sql.PreparedStatement pStmt = null;
            java.sql.Statement stmt = null;
            java.sql.Statement updtStmt = null;
            java.sql.ResultSet rs = null;

            java.sql.Connection cdhCon = null;
            java.sql.Statement cdhStmt = null;
            java.sql.ResultSet cdhRs = null;

            try {
                con = db.getConnection();
                con.setAutoCommit(false);
                con.setReadOnly(false);

                CDHZulaufinfo.logger.debug("FirebirdDb Connection:" + con);
                cdhCon = msdb.getConnection();
                CDHZulaufinfo.logger.debug("Mssql-Db Connection: " + cdhCon);

                // erst mal alles alte löschen
                stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM ARTGROESSEZULAUF");

                SQLLog.logger.debug("CdhSql-Query :" + cdhSql);

                cdhStmt = cdhCon.createStatement();
                cdhRs = cdhStmt.executeQuery(cdhSql);
                while (cdhRs.next()) {
                    int artgroesseid = 0;

                    String sql = "SELECT ARTGROESSE.ARTGROESSEID FROM ARTGROESSE"
                            + " WHERE ARTGROESSE.FREMDID=" + cdhRs.getInt("ARTICLE_SERIAL_NO");

                    stmt = con.createStatement();
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        artgroesseid = rs.getInt(1);
                    }

                    if (artgroesseid != 0) {
                        pStmt = con.prepareStatement(fbInsert);

                        int i = 1;
                        pStmt.setInt(i++, artgroesseid);
                        pStmt.setInt(i++, cdhRs.getInt("QUANTITY"));
                        pStmt.setInt(i++, cdhRs.getInt("DELIVERY_WEEK_NO"));
                        pStmt.setString(i++, cdhRs.getString("ORDD_RES_FLD_ALPHA12"));

                        pStmt.execute();
                    }
                }

                updtStmt = con.createStatement();
                ResultSet udtRs = updtStmt.executeQuery("SELECT OUT FROM UPDATE_ARTGROESSEBESTAND");

                while (udtRs.next()) {
                    CDHZulaufinfo.logger.info("anzahl upgedatet: " + udtRs.getString(1));
                }

                con.commit();

                CDHZulaufinfo.logger.info("und Fertig...!");
            } catch (java.sql.SQLException e) {
                CDHZulaufinfo.logger.error("SQL Error", e);
                SQLLog.logger.error("SQL Error.", e);
                return;
            } catch (Exception ex) {
                CDHZulaufinfo.logger.error(ex, ex);
            } finally {
                try {

                    if (updtStmt != null) {
                        updtStmt.close();
                    }
                    if (pStmt != null) {
                        pStmt.close();
                    }

                    if (cdhCon != null) {
                        cdhCon.close();
                    }

                } catch (java.sql.SQLException e) {
                    SQLLog.logger.error("SQL Error.", e);
                }

                FirebirdDb.close(rs, stmt, con);

            }

        } catch (Exception ex) {
            CDHZulaufinfo.logger.error(ex, ex);
        }

        CDHZulaufinfo.logger.info("CDHZulaufinfo fertig");
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.DEBUG);

        String iniFilename = "./conf/clxProductSync.properties";
        ApplicationConfig.loadConfig(iniFilename);

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(iniFilename)));

        FirebirdDbPool.createInstance();

        CDHZulaufinfo job = new CDHZulaufinfo(prop);

        job.run();

    }
}
