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
import de.complex.database.exception.DbConnectionNotAvailableException;
import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.db.mssql.MssqlDb;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.soap.datacheck.DatacheckPort;
import de.complex.clxproductsync.soap.datacheck.DatacheckService;
import de.complex.clxproductsync.soap.datacheck.DatacheckServiceLocator;
import de.complex.clxproductsync.soap.datacheck.Returnnok;
import de.complex.clxproductsync.soap.datacheck.Spoolcheck;
import de.complex.tools.config.ApplicationConfig;
import de.complex.util.sql.SqlTool;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Kunkel
 */
public class CDHArtBestand extends Thread {

    private static Logger logger = LogManager.getLogger(CDHArtBestand.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");
    private Properties prop;
    ArrayList<SKU> deaktivierteArtfarbeSkus = new ArrayList<SKU>();

    /**
     * Creates a new instance of BundlePreisUpload
     */
    public CDHArtBestand(Properties prop) {
        this.prop = prop;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("CDHArtBestand " + sdf.format(new Date()));
        CDHArtBestand.logger.info("CDHArtBestand run");
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        CDHArtBestand.logger.info("CDHArtBestand run A");
        FirebirdDb db = null;
        MssqlDb msdb = null;

        int commitNach = 500;
        int commitCount = 0;
        CDHArtBestand.logger.info("CDHArtBestand run B");

        String cdh_username = ApplicationConfig.getValue("cdh.username", "");
        String cdh_password = ApplicationConfig.getValue("cdh.password", "");
        String cdh_hostname = ApplicationConfig.getValue("cdh.hostname", "");
        String cdh_database = ApplicationConfig.getValue("cdh.database", "");
        String cdh_instanceName = ApplicationConfig.getValue("cdh.instance", "");

        CDHArtBestand.logger.info("CDHArtBestand run C");

        CDHArtBestand.logger.debug("***** BESTAND UPDATE... **************************************");

        try {
            CDHArtBestand.logger.info("CDHArtBestand run D");
            db = FirebirdDbPool.getInstance();
            CDHArtBestand.logger.debug("FirebirdDb: " + db);
            msdb = new MssqlDb(cdh_hostname, cdh_database, cdh_username, cdh_password, cdh_instanceName);
            CDHArtBestand.logger.debug("Mssql-Db: " + msdb);

            int cdhid = 0;
            int artgroesseid = 0;
            int countGesamt = 0;
            int count = 0;

            String artgroesseSql = "select ARTGROESSE.ARTGROESSEID"
                    + ", ARTGROESSE.FREMDID"
                    + ", ARTGROESSE.AUSLAUF AS ARTGROESSE_AUSLAUF"
                    + ", ARTGROESSE.WEBINAKTIV AS ARTGROESSE_WEBINAKTIV"
                    + ", ARTFARBE.ARTFARBEID AS ARTFARBE_ARTFARBEID"
                    + ", ARTFARBE.WEBINAKTIV AS ARTFARBE_WEBINAKTIV"
                    + ", ARTFARBE.AUSLAUF AS ARTFARBE_AUSLAUF"
                    + ", ARTFARBE.FIRSTVIEWFARBE AS ARTFARBE_FIRSTVIEWFARBE"
                    + ", ARTFARBE.SEOHAUPTFARBE AS ARTFARBE_SEOHAUPTFARBE"
                    + ", ART.ARTID AS ART_ARTID"
                    + ", ART.AUSLAUF AS ART_AUSLAUF"
                    + ", ART.AKTIV AS ART_AKTIV"
                    + " from ARTGROESSE"
                    + " inner join ARTFARBE on (ARTFARBE.ARTFARBEID = ARTGROESSE.ARTFARBEID)"
                    + " inner join ART on (ART.ARTID = ARTFARBE.ARTID)"
                    + " where ARTGROESSE.FREMDID <> 0"
                    + " order by ARTGROESSE.FREMDID ";

            java.sql.Connection con = null;
            java.sql.Statement stmt = null;
            java.sql.ResultSet rs = null;

            java.sql.Connection cdhCon = null;

            try {
                con = db.getConnection();
                con.setAutoCommit(false);
                con.setReadOnly(false);
                CDHArtBestand.logger.debug("FirebirdDb Connection:" + con);
                cdhCon = msdb.getConnection();
                CDHArtBestand.logger.debug("Mssql-Db Connection: " + cdhCon);

                SQLLog.logger.debug("ArtgroesseSql-Query :" + artgroesseSql);

                CDHBestand bestand = null;
                stmt = con.createStatement();

                rs = stmt.executeQuery("SELECT COUNT(ARTGROESSE.ARTGROESSEID) AS COUNT_GESAMT FROM ARTGROESSE WHERE ARTGROESSE.FREMDID <> 0");
                if (rs.next()) {
                    countGesamt = rs.getInt("COUNT_GESAMT");
                    CDHArtBestand.logger.info("Gesamt Datensaetze: " + countGesamt + "************************************************");
                }

                deaktivierteArtfarbeSkus.clear();
                rs = stmt.executeQuery(artgroesseSql);
                while (rs.next()) { // Schleife Artikel A
                    count++;
                    cdhid = rs.getInt("FREMDID");
                    artgroesseid = rs.getInt("ARTGROESSEID");
                    CDHArtBestand.logger.debug(count + "/" + countGesamt + " ArtGroesseid: " + artgroesseid + " CdhId: " + cdhid);
                    bestand = this.getCdhBestand(cdhCon, cdhid);

                    if (bestand != null) {
                        SKU sku = SKU.createSKU(rs);
                        handleSKU(con, sku, bestand);
                    }
                } // Schleife Artikel E
                
                for (SKU s : deaktivierteArtfarbeSkus) {
                    checkSeoHauptfarbe(con, s.getArtfarbeId());
                    checkFirstViewFarbe(con, s.getArtfarbeId());
                }

                String sql = "INSERT INTO BESTANDLOG (SAU) VALUES ('')";
                SQLLog.logger.debug("SQL: " + sql);
                CDHArtBestand.logger.debug("SQL: " + sql);
                stmt.executeUpdate(sql); // daten werden über bevore-Insert-trigger 

                con.commit();

                CDHArtBestand.logger.info("und Fertig...! *************************************************************************");
            } catch (java.sql.SQLException e) {
                CDHArtBestand.logger.error("SQL Error", e);
                SQLLog.logger.error("SQL Error.", e);
                return;
            } finally {
                try {
                    if (cdhCon != null) {
                        cdhCon.close();
                    }

                } catch (java.sql.SQLException e) {
                    SQLLog.logger.error("SQL Error.", e);
                }

                FirebirdDb.close(rs, stmt, con);

            }

        } catch (Exception ex) {
            CDHArtBestand.logger.error(ex, ex);
        }

        CDHArtBestand.logger.info("CDHArtBestand fertig");
    }

    private void handleSKU(Connection con, SKU sku, CDHBestand bestand) throws SQLException {
        this.setBestand(con, sku.getArtgroesseid(), bestand);

        if (bestand.getAktuellerBestand() == 0) {
            CDHArtBestand.logger.debug("artgroesseid: " + sku.getArtgroesseid() + " Bestand ist 0");

            if (sku.getArtAuslauf() != 0 || sku.getArtfarbeAuslauf() != 0 || sku.getArtgroesseAuslauf() != 0) {
                CDHArtBestand.logger.debug(" ArtGroesseid: " + sku.getArtgroesseid() + " inaktiv setzen");

                // wenn noch aktiv, Artgroesse inaktiv schalten
                if (sku.getArtgroesseWebinaktiv() == 0) {
                    setDetailInaktiv(con, Detailtype.artgroesse, sku.getArtgroesseid());
                }

                // keine weiteren Artgroesse aktiv
                if (!hasMoreDetail(con, Detailtype.artgroesse, sku.getArtgroesseid())) {

                    // wenn noch aktiv, Artfarbe inaktiv schalten
                    if (sku.getArtfarbeWebinaktiv() == 0) {
                        setDetailInaktiv(con, Detailtype.artfarbe, sku.getArtfarbeId());
                        
                        if (sku.isArtfarbeFirstviewfarbe() || sku.isArtfarbeSeohauptfarbe()) {
                            deaktivierteArtfarbeSkus.add(sku);
                        }
                    }

                    // keine weitere Artfarbe aktiv
                    if (!hasMoreDetail(con, Detailtype.artfarbe, sku.getArtfarbeId())) {

                        // wenn noch aktiv, Art inaktiv schalten
                        if (sku.getArtAktiv() == 1) {
                            setDetailInaktiv(con, Detailtype.art, sku.getArtid());
                        }
                    }
                }
            } else {
                CDHArtBestand.logger.debug("artgroesseid: " + sku.getArtgroesseid() + ", keine Stufe auf Auslauf, nicht deaktivieren");
            }
        } else {

            // Artikel evtl. wieder reaktivieren, wenn bereits deaktiviert und auf Auslauf
            if (sku.getArtgroesseWebinaktiv() == 1 && (sku.getArtAuslauf() != 0 || sku.getArtfarbeAuslauf() != 0 || sku.getArtgroesseAuslauf() != 0)) {
                setDetailAktiv(con, Detailtype.artgroesse, sku.getArtgroesseid());
                setArtreaktiviert(con, sku.getArtgroesseid(), bestand);
                setDetailAktiv(con, Detailtype.artfarbe, sku.getArtfarbeId());
                setDetailAktiv(con, Detailtype.art, sku.getArtid());
            }
        }
    }

    private CDHBestand getCdhBestand(java.sql.Connection con, int cdhid) {
        java.sql.Statement stmt = null;
        String getSql = "SELECT ARTICLE_MASTER.ACTUAL_STOCK_ART, ARTICLE_MASTER.RESERVED_STOCK FROM ARTICLE_MASTER WHERE ARTICLE_MASTER.ARTICLE_SERIAL_NO = " + cdhid;
        java.sql.ResultSet rs = null;

        CDHBestand bestand = null;

        try {
            try {
                stmt = con.createStatement();
                CDHArtBestand.logger.debug("SQL :" + getSql);
                rs = stmt.executeQuery(getSql);

                if (rs.next()) {
                    bestand = new CDHBestand(rs.getFloat("ACTUAL_STOCK_ART"), rs.getFloat("RESERVED_STOCK"));
                    CDHArtBestand.logger.debug("Bestand :" + bestand);
                }
            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
                return null;
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
            }
        }

        return bestand;
    }

    private void setBestand(java.sql.Connection con, int artgroesseid, CDHBestand bestand) {
        java.sql.Statement stmt = null;
        java.sql.Statement updStmt = null;
        java.sql.ResultSet rs = null;

        try {
            int count = 0;

            try {

                boolean doUpdate = true;
                float curBestand = 0;

                try {
                    String sql = "SELECT ARTGROESSEBESTAND.BESTAND FROM ARTGROESSEBESTAND WHERE ARTGROESSEBESTAND.ARTGROESSEID = " + artgroesseid;
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(sql);

                    if (rs.next()) {
                        curBestand = rs.getFloat("BESTAND");
                        CDHArtBestand.logger.debug("clxProdukt Bestand: " + curBestand + " CDHBestand: " + bestand.getAktuellerBestand());
                        if (curBestand == bestand.getAktuellerBestand()) {
                            doUpdate = false;
                        }
                    } else {
                        CDHArtBestand.logger.debug("kein ClxProdukt Bestand gefunden, Updaten");
                    }
                } catch (java.sql.SQLException e) {
                    SQLLog.logger.error("SQL Error.", e);
                    CDHArtBestand.logger.debug("SQL Error.", e);
                }

                if (doUpdate) {
                    CDHArtBestand.logger.debug("Bestand Updaten");
                    String qSql = "UPDATE OR INSERT INTO ARTGROESSEBESTAND (ARTGROESSEID, BESTAND) VALUES (" + artgroesseid + ", " + bestand.getAktuellerBestand() + ") MATCHING (ARTGROESSEID);";
                    CDHArtBestand.logger.debug("SQL :" + qSql);
                    updStmt = con.createStatement();
                    count = updStmt.executeUpdate(qSql);
                    CDHArtBestand.logger.debug(count + " Datensatz geaendert");
                } else {
                    CDHArtBestand.logger.debug("Bestand gleich, nicht Updaten");
                }

            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
                return;
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (updStmt != null) {
                    updStmt.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
            }
        }
    }

    private void setArtreaktiviert(java.sql.Connection con, int artgroesseid, CDHBestand bestand) throws SQLException {
        java.sql.PreparedStatement pStmt = null;

        try {
            String sql = "insert into ARTREAKTIVIERT (ARTGROESSEID, BESTAND) values (?,?)";
            CDHArtBestand.logger.debug("setArtreaktiviert sql: " + sql + " artgroesseid: " + artgroesseid + " bestand: " + bestand.getAktuellerBestand());
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, artgroesseid);
            pStmt.setFloat(2, bestand.getAktuellerBestand());
            pStmt.executeUpdate();

        } finally {
            FirebirdDb.close(null, pStmt, null);
        }
    }

    /**
     * setDetailInaktiv - Detaildatensatz inaktiv schalten
     *
     * @param con Datenbank-Connection
     * @param detailtype Detailtype
     * @param detailid Datensatzid zum Detailtype
     *
     */
    private void setDetailInaktiv(java.sql.Connection con, Detailtype detailtype, int detailid) throws SQLException {
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            CDHArtBestand.logger.debug("setDetailInaktiv sql: " + detailtype.sqlDeaktivieren + " id: " + detailid);
            pstmt = con.prepareStatement(detailtype.sqlDeaktivieren);
            pstmt.setInt(1, detailid);
            result = pstmt.executeUpdate();
            CDHArtBestand.logger.debug(result + " Datensatz upgedatet");

        } finally {
            FirebirdDb.close(null, pstmt, null);
        }
    }

    private void checkSeoHauptfarbe(java.sql.Connection con, int artfarbeid) throws SQLException {
        logger.debug("checkSeoHauptfarbe artfarbeid: " + artfarbeid);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sqlIsSeohauptfarbe = "select ARTFARBE.SEOHAUPTFARBE from ARTFARBE where ARTFARBE.ARTFARBEID = ?";
        String setNewSeoHauptfarbe = "update ARTFARBE set ARTFARBE.SEOHAUPTFARBE = 1 where ARTFARBE.ARTFARBEID = ?";

        try {
            // ist es die seohauptfarbe
            pstmt = con.prepareStatement(sqlIsSeohauptfarbe);
            pstmt.setInt(1, artfarbeid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                logger.debug("checkSeoHauptfarbe, seohauptfarbe = " + rs.getInt("SEOHAUPTFARBE"));
                if (rs.getInt("SEOHAUPTFARBE") == 1) {

                    Integer newArtfarbeId = getArtfarbenidMitBestand(con, artfarbeid);
                    if (newArtfarbeId != null) {
                        logger.debug("checkSeoHauptfarbe, neue seohauptfarbe: " + newArtfarbeId);
                        pstmt = con.prepareStatement(setNewSeoHauptfarbe);
                        pstmt.setInt(1, newArtfarbeId);
                        pstmt.executeUpdate();
                    } else {
                        logger.warn("seohauptartikelfarbe wurde deaktiviert, keine weitere farbe mit Bestand! artfarbeid: " + artfarbeid);
                    }
                } else {
                    logger.debug("checkSeoHauptfarbe artfarbeid: " + artfarbeid + " ist keine seohauptfarbe");
                }
            }
        } finally {
            SqlTool.safeClose(rs, pstmt);
        }
    }

    private void checkFirstViewFarbe(java.sql.Connection con, int artfarbeid) throws SQLException {
        logger.debug("checkFirstviewFarbe artfarbeid: " + artfarbeid);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sqlIsFirstViewFarbe = "select ARTFARBE.FIRSTVIEWFARBE from ARTFARBE where ARTFARBE.ARTFARBEID = ?";
        String setNewFirstViewFarbe = "update ARTFARBE set ARTFARBE.FIRSTVIEWFARBE = 1 where ARTFARBE.ARTFARBEID = ?";

        try {
            // ist es die firstviewfarbe
            pstmt = con.prepareStatement(sqlIsFirstViewFarbe);
            pstmt.setInt(1, artfarbeid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                logger.debug("checkFirstViewFarbe, firstViewFarbe = " + rs.getInt("FIRSTVIEWFARBE"));
                if (rs.getInt("FIRSTVIEWFARBE") == 1) {

                    Integer newArtfarbeId = getArtfarbenidMitBestand(con, artfarbeid);
                    if (newArtfarbeId != null) {
                        logger.debug("checkSeoHauptfarbe, neue seohauptfarbe: " + newArtfarbeId);
                        pstmt = con.prepareStatement(setNewFirstViewFarbe);
                        pstmt.setInt(1, newArtfarbeId);
                        pstmt.executeUpdate();
                    } else {
                        logger.warn("firstviewfarbe wurde deaktiviert, keine weitere farbe mit Bestand! artfarbeid: " + artfarbeid);
                    }
                } else {
                    logger.debug("checkSeoHauptfarbe artfarbeid: " + artfarbeid + " ist keine seohauptfarbe");
                }
            }
        } finally {
            SqlTool.safeClose(rs, pstmt);
        }
    }

    private Integer getArtfarbenidMitBestand(java.sql.Connection con, int artfarbeid) throws SQLException {
        logger.debug("getFarbenidMitBestand artfarbeid: " + artfarbeid);
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sqlGetNewArtfarbeid = "select AF2.ARTFARBEID\n"
                + "from ARTFARBE AF1\n"
                + "inner join ARTFARBE AF2 on (AF2.ARTID = AF1.ARTID and\n"
                + "      AF2.ARTFARBEID <> AF1.ARTFARBEID)\n"
                + "inner join ARTGROESSE on (ARTGROESSE.ARTFARBEID = AF2.ARTFARBEID)\n"
                + "inner join ARTGROESSEBESTAND on (ARTGROESSEBESTAND.ARTGROESSEID = ARTGROESSE.ARTGROESSEID)\n"
                + "where AF1.ARTFARBEID = ? and\n"
                + "      AF2.WEBINAKTIV = 0 and\n"
                + "      ARTGROESSE.WEBINAKTIV = 0\n"
                + "group by 1\n"
                + "order by sum(ARTGROESSEBESTAND.BESTAND) desc ";
        try {
            pstmt = con.prepareStatement(sqlGetNewArtfarbeid);
            pstmt.setInt(1, artfarbeid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Integer newArtfarbeid = rs.getInt("ARTFARBEID");
                logger.debug("neue Artfarbe mit Bestand: " + newArtfarbeid);
                return newArtfarbeid;
            } else {
                logger.warn("keine weitere Artfarbe mit Bestand! artfarbeid: " + artfarbeid);
                return null;
            }
        } finally {
            SqlTool.safeClose(rs, pstmt);
        }
    }

    /**
     * setDetailAktiv - Detaildatensatz inaktiv schalten
     *
     * @param con Datenbank-Connection
     * @param detailtype Detailtype
     * @param detailid Datensatzid zum Detailtype
     *
     */
    private void setDetailAktiv(java.sql.Connection con, Detailtype detailtype, int detailid) throws SQLException {
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            CDHArtBestand.logger.debug("setDetailAktiv sql: " + detailtype.sqlAktivieren + " id: " + detailid);
            pstmt = con.prepareStatement(detailtype.sqlAktivieren);
            pstmt.setInt(1, detailid);
            result = pstmt.executeUpdate();
            CDHArtBestand.logger.debug(result + " Datensatz upgedatet");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
            }
        }
    }

    /**
     * hasMoreDetail
     *
     * @param con Datenbank-Connection
     * @param detailtype Detailtype
     * @param detailid Datensatzid zum Detailtype
     *
     * @return boolean - true wenn noch Datensätze der gleichen Stufe aktiv sind
     *
     */
    private boolean hasMoreDetail(java.sql.Connection con, Detailtype detailtype, int detailid) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            CDHArtBestand.logger.debug("checkDetail sql: " + detailtype.sqlCheck + " id: " + detailid);
            pstmt = con.prepareStatement(detailtype.sqlCheck);
            pstmt.setInt(1, detailid);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                    if (!rs.wasNull()) {
                        CDHArtBestand.logger.debug("keine weiteren Details aktiv");
                        return false;
                    }
                } else {
                    CDHArtBestand.logger.debug("es sind " + rs.getInt(1) + " weitere Details aktiv");
                }
            }

            return true;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (java.sql.SQLException e) {
                SQLLog.logger.error("SQL Error.", e);
            }
        }
    }

    /**
     *
     * <li>{@link #sqlDeaktivieren}</li>
     * <li>{@link #sqlCheck}</li>
     * <li>{@link #sqlAktivieren}</li>
     *
     */
    private enum Detailtype {

        art("UPDATE ART "
                + "SET ART.AKTIV=0"
                + " WHERE ART.ARTID=?", "", "update ART set ART.AKTIV=1 where ART.ARTID=?"),
        artfarbe("UPDATE ARTFARBE"
                + " SET ARTFARBE.WEBINAKTIV=1"
                + " WHERE ARTFARBE.ARTFARBEID=?", "select count(afdetail.artfarbeid)"
                + " from artfarbe afdetail"
                + " inner join artfarbe afmaster on (afmaster.ARTID=afdetail.ARTID AND AFMASTER.ARTFARBEID<>AFDETAIL.ARTFARBEID)"
                + " where afmaster.ARTFARBEID=?"
                + " and afdetail.webinaktiv=0", "update ARTFARBE set ARTFARBE.WEBINAKTIV=0 where ARTFARBE.ARTFARBEID=?"),
        artgroesse("UPDATE ARTGROESSE"
                + " SET ARTGROESSE.WEBINAKTIV=1"
                + " WHERE ARTGROESSE.ARTGROESSEID=?",
                " select COUNT(AGDETAIL.ARTGROESSEID)"
                + " from ARTGROESSE AGDETAIL"
                + " inner join ARTGROESSE AGMASTER ON (AGMASTER.ARTFARBEID=AGDETAIL.ARTFARBEID AND AGMASTER.ARTGROESSEID<>AGDETAIL.ARTGROESSEID)"
                + " where AGMASTER.ARTGROESSEID=?"
                + " and AGDETAIL.WEBINAKTIV=0", "update ARTGROESSE set ARTGROESSE.WEBINAKTIV=0 where ARTGROESSE.ARTGROESSEID=?");
        /**
         * Update-Sql zum Deaktivieren des Details
         */
        public String sqlDeaktivieren;
        /**
         * Select-Sql zum prüfen ob noch ein gleichwertiger Datensatz aktiv ist
         */
        public String sqlCheck;
        /**
         * Update-Sql zum Aktivieren des Details
         */
        public String sqlAktivieren;

        private Detailtype(String sqlDeaktivieren, String sqlCheck, String sqlAktivieren) {
            this.sqlDeaktivieren = sqlDeaktivieren;
            this.sqlCheck = sqlCheck;
            this.sqlAktivieren = sqlAktivieren;
        }
    }

    public static void main(String[] args) throws IOException, DbConnectionNotAvailableException, SQLException {
        // TODO code application logic here

        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.DEBUG);

        String iniFilename = "./conf/clxProductSync.properties";
        ApplicationConfig.loadConfig(iniFilename);

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(iniFilename)));

        FirebirdDbPool.createInstance();
        Connection con = FirebirdDbPool.getInstance().getConnection();
        con.setReadOnly(false);
        con.setAutoCommit(false);
        CDHArtBestand job = new CDHArtBestand(prop);

        SKU sku = new SKU();
//		sku.setArtgroesseid(85286);
//		sku.setArtgroesseid(85287);
//		sku.setArtgroesseid(85288);
//		sku.setArtgroesseid(85289);
//		sku.setArtgroesseid(85290);
        sku.setArtgroesseid(97391);
        sku.setArtfarbeId(41265);
        sku.setArtid(8310);
        sku.setCdhid(119);

        sku.setArtgroesseAuslauf(1);
        sku.setArtfarbeAuslauf(1);
        sku.setArtAuslauf(1);

        // aktiv
        sku.setArtgroesseWebinaktiv(0);
        sku.setArtfarbeWebinaktiv(0);
        sku.setArtAktiv(1);

        // ínaktiv
        sku.setArtgroesseWebinaktiv(1);
        sku.setArtfarbeWebinaktiv(1);
        sku.setArtAktiv(0);

//		// true = alles aktiv
//		if (false) {
//			sku.setArtgroesseWebinaktiv(0);
//			sku.setArtfarbeWebinaktiv(0);
//			sku.setArtAktiv(1);
//		} else {
//			sku.setArtgroesseWebinaktiv(1);
//			sku.setArtfarbeWebinaktiv(1);
//			sku.setArtAktiv(0);
//		}
//
//		// true = alles Auslauf
//		if (true) {
//			sku.setArtgroesseAuslauf(1);
//			sku.setArtfarbeAuslauf(1);
//			sku.setArtAuslauf(1);
//		} else {
//			sku.setArtgroesseAuslauf(0);
//			sku.setArtfarbeAuslauf(0);
//			sku.setArtAuslauf(0);
//		}
//		CDHBestand bestand = new CDHBestand(0, 0);
        CDHBestand bestand = new CDHBestand(100, 50);

        //job.checkSeoHauptfarbe(con, 33847);
        //job.checkSeoHauptfarbe(con, 33876);
        job.handleSKU(con, sku, bestand);
        con.commit();
        con.close();

        //	job.run();
    }

}
