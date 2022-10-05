/*
 * BundlePreisUpload.java
 *
 * Created on 2. M�rz 2007, 11:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import de.complex.clxproductsync.MainApp;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.clxproductsync.eventhandler.fileevent.tools.ClxFtp;
import de.complex.clxproductsync.eventhandler.fileevent.tools.SftpTransfer;
import de.complex.clxproductsync.eventhandler.fileevent.tools.SftpUploadException;
import de.complex.database.SQLLog;
import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.tools.config.ApplicationConfig;
import de.complex.util.lang.StringTool;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Kunkel
 */
public class ExcelBestandUpload extends Thread {

    private static Logger logger = LogManager.getLogger(ExcelBestandUpload.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");
    private Properties prop;

    public static final String INVENTUR_DE = "in Inventur";
    public static final String INVENTUR_EN = "in inventory";

    final String GEORDERT_DE = "geordert";
    final String GEORDERT_EN = "ordered";

    final String CIRCA_DE = "ca.";
    final String CIRCA_EN = "approx.";

    /**
     * Creates a new instance of BundlePreisUpload
     */
    public ExcelBestandUpload(Properties prop) {
        this.prop = prop;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("ExcelBestandUpload " + sdf.format(new Date()));
        ExcelBestandUpload.logger.info("ExcelBestandUpload run");
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        ExcelBestandUpload.logger.info("ExcelBestandUpload run A");
        FirebirdDb db = null;

        try {
            db = FirebirdDbPool.getInstance();
            ExcelBestandUpload.logger.debug("FirebirdDb: " + db);

            java.sql.Connection con = null;
            java.sql.Statement stmt = null;
            java.sql.Statement stmtSprachen = null;
            java.sql.ResultSet rs = null;
            java.sql.ResultSet rsSprachen = null;

            try {
                try {
                    con = db.getConnection();
                    con.setAutoCommit(false);
                    ExcelBestandUpload.logger.debug("FirebirdDb Connection:" + con);
                    stmtSprachen = con.createStatement();
                    stmt = con.createStatement();

                    ArrayList<String> warengruppenIdsOhneZulaufinfo = new ArrayList<String>();
                    for (String wgrid : StringTool.getNotNullTrim(ApplicationConfig.getValue("excelbestand.warengruppenids_ohne_zulaufinfo", "")).split(",")) {
                        warengruppenIdsOhneZulaufinfo.add(wgrid);
                    }

                    String exportsprachenConf = StringTool.getNotNullTrim(ApplicationConfig.getValue("excelbestand.exportsprachen", ""));

                    StringBuilder exportsprachen = new StringBuilder();

                    String trenner = ",";

                    if (exportsprachenConf.contains(";")) {
                        trenner = ";";
                    }

                    for (String sprachkuerzel : exportsprachenConf.split(trenner)) {
                        if (!StringTool.isEmpty(sprachkuerzel)) {

                            if (exportsprachen.length() > 0) {
                                exportsprachen.append(",");
                            }

                            exportsprachen.append("'").append(sprachkuerzel.toUpperCase()).append("'");
                        }
                    }

                    StringBuilder sqlBuilder = new StringBuilder("select SPRACHEID, KUERZEL, LOCALE from SPRACHE");

                    if (exportsprachen.length() > 0) {
                        sqlBuilder.append(" where upper(SPRACHE.KUERZEL) IN (").append(exportsprachen).append(")");
                    }

                    sqlBuilder.append(" order by SPRACHEID");

                    ExcelBestandUpload.logger.debug("sprachen sql :" + sqlBuilder.toString());
                    rsSprachen = stmtSprachen.executeQuery(sqlBuilder.toString());

                    if (!rsSprachen.next()) {
                        ExcelBestandUpload.logger.error("Keine Sprachen mit dem Konfig-Wert (excelbestand.exportsprachen) '" + exportsprachenConf + "' gefunden!");
                    } else {
                        do {

                            int spracheid = rsSprachen.getInt(1);
                            String kuerzel = rsSprachen.getString(2);
                            String sLocale = rsSprachen.getString(3);

                            ExcelBestandUpload.logger.debug(String.format("export parameter. spracheid: %d, kuerzel: %s, sLocale: %s", spracheid, kuerzel, sLocale));

//						Parameter Procedure
//						P_MARKENID
//						P_SPRACHEID
//						P_WEBAKTIV
//						P_AUSLAUFARTIKEL
//						P_SONDERPOSTEN
                            String excelbestandShopid = ApplicationConfig.getValue("excelbestand.shopid", "null");
                            if (StringTool.isEmpty(excelbestandShopid)) {
                                excelbestandShopid = "null";
                            }

                            String sql = "SELECT SEL_EXPORT_SKULAGER.*, ag.AUSLAUF as AG_AUSLAUF, af.AUSLAUF as AF_AUSLAUF, a.AUSLAUF as A_AUSLAUF " +
                                    " FROM SEL_EXPORT_SKULAGER(null," + spracheid + ",1,null,null," + excelbestandShopid + ")" +
                                    " inner join ARTGROESSE ag on ag.ARTGROESSEID=SEL_EXPORT_SKULAGER.ARTGROESSEID\n" +
                                    " inner join artfarbe af on af.ARTFARBEID=ag.ARTFARBEID\n" +
                                    " inner join art a on a.artid=af.ARTID";
                            ExcelBestandUpload.logger.debug("sql:" + sql);
                            rs = stmt.executeQuery(sql);

                            File file = new File("bestand_" +  kuerzel + ".csv");

                            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                            osw.write('\uFEFF');

                            ICsvMapWriter writer = new CsvMapWriter(osw, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                            try {
                                final String[] header = createHeader(sLocale);
                                writer.writeHeader(header);

                                int rowIndex = 0;
                                while (rs.next()) {
                                    rowIndex++;

                                    if ((rowIndex == 1) || (rowIndex % 1000 == 0)) {
                                        ExcelBestandUpload.logger.debug("row:" + rowIndex);
                                    }

                                    final HashMap<String, ? super Object> data = new HashMap<String, Object>();

                                    data.put(header[0], rs.getString("ARTGROESSEID") == null ? "" : rs.getString("ARTGROESSEID")); // A
                                    data.put(header[1], rs.getString("FREMDID") == null ? "" : rs.getString("FREMDID"));
                                    data.put(header[2], rs.getString("NEU") == null ? "" : rs.getString("NEU"));
                                    data.put(header[3], rs.getString("MARKE") == null ? "" : rs.getString("MARKE"));
                                    data.put(header[4], rs.getString("ARTIKEL") == null ? "" : rs.getString("ARTIKEL"));    // E
                                    data.put(header[5], rs.getString("FARBE") == null ? "" : rs.getString("FARBE"));
                                    data.put(header[6], rs.getString("FARBKUERZEL") == null ? "" : rs.getString("FARBKUERZEL"));
                                    data.put(header[7], rs.getString("GROESSE") == null ? "" : rs.getString("GROESSE"));
                                    data.put(header[8], rs.getString("ARTIKELBEZECIHNUNG") == null ? "" : rs.getString("ARTIKELBEZECIHNUNG"));
                                    data.put(header[9], rs.getString("KURZBESCHREIBUNG") == null ? "" : rs.getString("KURZBESCHREIBUNG")); // J

                                    DecimalFormat dformat = new DecimalFormat("########0");

/*                                  Spezialfunktion für Livegang von Fare-ERP
                                    Nicht komplett entfernt weil das evtl. für Daiber-Livegang nochmal benötigt wird.

                                    Integer bestandAuslaufartikel = getBestandAuslaufartikel(con, rs.getInt("ARTGROESSEID"));

                                    if ("FARE".equalsIgnoreCase(ApplicationConfig.getValue("customer", "")) && bestandAuslaufartikel != null && bestandAuslaufartikel.intValue() > 0) {
                                        if (!"de_DE".equalsIgnoreCase(sLocale)) {
                                            data.put(header[10], INVENTUR_EN);
                                        } else {
                                            data.put(header[10], INVENTUR_DE);
                                        }
                                    } else {
                                        // K
                                        try {
                                            int lagermenge = rs.getInt("LAGERMENGE");
                                            data.put(header[10], dformat.format(lagermenge));
                                        } catch (Exception e) {
                                            ExcelBestandUpload.logger.error(e, e);
                                            data.put(header[10], "N/A");
                                        }
                                    }
 */

                                    // K
                                    try {
                                        int lagermenge = rs.getInt("LAGERMENGE");
                                        data.put(header[10], dformat.format(lagermenge));
                                    } catch (Exception e) {
                                        ExcelBestandUpload.logger.error(e, e);
                                        data.put(header[10], "N/A");
                                    }

                                    // L und M, je Customer unterschiedlich
                                    if ("FARE".equalsIgnoreCase(ApplicationConfig.getValue("customer", ""))) {
                                        Zulauf naechsterZulauf = getNaechsteZulaufinfo(con, rs.getInt("ARTGROESSEID"));

                                        if (naechsterZulauf != null) {
                                            if (naechsterZulauf.isStatusIndispatch()) {
                                                data.put(header[11], naechsterZulauf.getMenge()); // L
                                                data.put(header[12], naechsterZulauf.getKalenderwoche() + "/" + naechsterZulauf.getJahr()); // M
                                            } else {
                                                data.put(header[11], "");
                                                data.put(header[12], "");
                                            }
                                        } else {
                                            data.put(header[11], "");
                                            data.put(header[12], "");
                                        }

                                    } else if ("MBW".equalsIgnoreCase(ApplicationConfig.getValue("customer", ""))) {
                                        Zulauf naechsterZulauf = getNaechsteZulaufinfo(con, rs.getInt("ARTGROESSEID"));

                                        if (naechsterZulauf != null) {
                                            if (naechsterZulauf.isStatusIndispatch()) {
                                                data.put(header[11], naechsterZulauf.getMenge()); // L
                                                data.put(header[12], naechsterZulauf.getKalenderwoche() + "/" + naechsterZulauf.getJahr()); // M
                                            } else {
                                                data.put(header[11], "");
                                                data.put(header[12], "");
                                            }
                                        } else {
                                            data.put(header[11], "");
                                            data.put(header[12], "");
                                        }
                                    } else if ("DAIBER".equalsIgnoreCase(ApplicationConfig.getValue("customer", ""))) {
                                        Zulauf naechsterZulauf = getNaechsteZulaufinfo(con, rs.getInt("ARTGROESSEID"));
                                        ExcelBestandUpload.logger.debug(String.format("naechster Zulauf Artgroesseid %d Zulauf %s", rs.getInt("ARTGROESSEID"), naechsterZulauf));

                                        if (naechsterZulauf != null) {

                                            if (naechsterZulauf.isStatusIndispatch()) {
                                               data.put(header[11], naechsterZulauf.getMenge()); // L

                                                if (!"de_DE".equalsIgnoreCase(sLocale)) {
                                                    data.put(header[12], CIRCA_EN + " CW " + naechsterZulauf.getKalenderwoche() + "/" + naechsterZulauf.getJahr()); // M
                                                } else {
                                                    data.put(header[12], CIRCA_DE + " KW" + naechsterZulauf.getKalenderwoche() + "/" + naechsterZulauf.getJahr()); // M
                                                }
                                            } else if (naechsterZulauf.isStatusOrdered()) {
                                                data.put(header[11], naechsterZulauf.getMenge()); // L

                                                int quartal = getQuartal(naechsterZulauf.getKalenderwoche(), naechsterZulauf.getJahr());

                                                if (!"de_DE".equalsIgnoreCase(sLocale)) {
                                                    data.put(header[12], CIRCA_EN + " Q" + quartal + "/" + naechsterZulauf.getJahr()); // M
                                                } else {
                                                    data.put(header[12], CIRCA_DE + " Q" + quartal + "/" + naechsterZulauf.getJahr()); // M
                                                }
                                            } else {
                                                data.put(header[11], "");
                                                data.put(header[12], "");
                                            }
                                        } else {
                                            data.put(header[11], "");
                                            data.put(header[12], "");
                                        }
                                    } else {
                                        try {
                                            int zulaufmenge_sh = rs.getInt("ZULAUFMENGE_SH");
                                            if (zulaufmenge_sh != 0) {
                                                data.put(header[11], dformat.format(zulaufmenge_sh));
                                            } else {
                                                data.put(header[11], "");
                                            }
                                        } catch (Exception e) {
                                            ExcelBestandUpload.logger.error(e, e);
                                            data.put(header[11], "N/A");
                                        }

                                        try {
                                            final String STATUS_DE = "Lieferung nicht vor KW";
                                            final String STATUS_EN = "Further addition to stock app. week";

                                            String zulaufstatusSH = rs.getString("ZULAUFSTATUS_SH");

                                            if (!"de_DE".equalsIgnoreCase(sLocale)) {
                                                zulaufstatusSH = zulaufstatusSH.replaceAll(STATUS_DE, STATUS_EN);
                                            }

                                            data.put(header[12], zulaufstatusSH);
                                        } catch (Exception e) {
                                            ExcelBestandUpload.logger.error(e, e);
                                            data.put(header[12], "N/A");
                                        }
                                    }

                                    if (!warengruppenIdsOhneZulaufinfo.isEmpty()) {
                                        Integer warengruppenId = getWarengruppenid(con, rs.getInt("ARTGROESSEID"));

                                        if (warengruppenIdsOhneZulaufinfo.contains(warengruppenId.toString())) {
                                            data.put(header[10], dformat.format(0));
                                            data.put(header[11], "");
                                            data.put(header[12], "");
                                        }
                                    }

                                    Boolean auslauf = (rs.getInt("AG_AUSLAUF") == 1) || (rs.getInt("AF_AUSLAUF") == 1) || (rs.getInt("A_AUSLAUF") == 1);
                                    if (auslauf) {
                                        data.put(header[13], "1");
                                    } else {
                                        data.put(header[13], "");
                                    }

                                    try {
                                        writer.write(data, header);
                                    } catch (Exception e) {
                                        ExcelBestandUpload.logger.error("writer: " + writer + " data: " + data + " header: " + header);
                                        ExcelBestandUpload.logger.error(e, e);

                                    }
                                }
                                ExcelBestandUpload.logger.debug("lastrow:" + rowIndex);
                            } finally {
                                try {
                                    writer.close();
                                } catch (Exception ignore) {
                                    ExcelBestandUpload.logger.error(ignore, ignore);
                                }

                            }
                        } while (rsSprachen.next());
                    }

                    FirebirdDb.close(rsSprachen, null, null);
                    rsSprachen = stmtSprachen.executeQuery(sqlBuilder.toString());

                    if (!rsSprachen.next()) {
                        ExcelBestandUpload.logger.error("Keine Sprachen mit dem Konfig-Wert (excelbestand.exportsprachen) '" + exportsprachenConf + "' gefunden!");
                    } else {
                        do {
                            int spracheid = rsSprachen.getInt(1);
                            logger.debug("do transfer - spracheid: " + spracheid);

                            String kuerzel = rsSprachen.getString(2);
                            String sLocale = rsSprachen.getString(3);
                            File file = new File("bestand_" + kuerzel + ".csv");

                            sendWithSoap(spracheid, file, "bestand.csv");
                            sendWithFtp(kuerzel, file);

                            if ("true".equalsIgnoreCase(ApplicationConfig.getValue("excelbestand.deleteTempFile", "false"))) {
                                logger.debug("deleteFile: " + file);
                                FileUtils.deleteQuietly(file);
                            }
                        } while (rsSprachen.next());
                    }

                    con.commit();
                } catch (java.sql.SQLException e) {
                    ExcelBestandUpload.logger.error("SQL Error", e);
                    logger.error("SQL Error.", e);
                    return;
                }
            } finally {
                FirebirdDb.close(rsSprachen, stmtSprachen, null);
                FirebirdDb.close(rs, stmt, con);
            }
        } catch (Exception ex) {
            ExcelBestandUpload.logger.error(ex, ex);
        }

        ExcelBestandUpload.logger.info("ExcelBestandUpload fertig");
    }

    private void sendWithFtp(String kuerzel, File file) throws SftpUploadException {
        File ftpUploadFile = null;

        if (!ApplicationConfig.getValue("excelbestand.ftphost", "").equals("")) {
            ExcelBestandUpload.logger.info("ftpUpload: " + file.getAbsolutePath());

            ExcelBestandUpload.logger.debug("file absolutePath: " + file.getAbsolutePath());
            ExcelBestandUpload.logger.debug("file path: " + file.getPath());
            ExcelBestandUpload.logger.debug("file name: " + file.getName());

            ftpUploadFile = new File("bestand_" + kuerzel + ".csv");
            ExcelBestandUpload.logger.debug(ftpUploadFile.getAbsolutePath() + " - " + ftpUploadFile.getName());

            ExcelBestandUpload.logger.debug("file: " + file);
            ExcelBestandUpload.logger.debug("ftpUploadFile: " + ftpUploadFile);

            String ftphost = ApplicationConfig.getValue("excelbestand.ftphost", "");
            String username = ApplicationConfig.getValue("excelbestand.username", "");
            String password = ApplicationConfig.getValue("excelbestand.password", "");
            String remotepath = ApplicationConfig.getValue("excelbestand.remotepath", ".");
            boolean passiveMode = Boolean.valueOf(ApplicationConfig.getValue("excelbestand.passivemode", "false"));

            if ("true".equalsIgnoreCase(ApplicationConfig.getValue("excelbestand.useSftp", "false"))){

                SftpTransfer sftp = new SftpTransfer(ftphost, username, password, remotepath, ftpUploadFile);
                sftp.send();
            } else{

                ClxFtp ftp = new ClxFtp(ftphost, username, password, passiveMode);
                if (!ftp.uploadFile(ftpUploadFile, remotepath)) {
                    ExcelBestandUpload.logger.error("ftp Upload fehler");
                }
            }
        } else {
            ExcelBestandUpload.logger.debug("Kein Ftp-Upload. Ftp-Zugang nicht konfiguriert.");
        }
    }

    private void sendWithSoap(int spracheid, File file, String remoteFilename) {
        try {
            ExcelBestandUpload.logger.info("SoapHandler.sendFileData: " + file.getAbsolutePath());
            SoapHandler.sendFileData(null, "bestandexcel", spracheid, file, remoteFilename);
            ExcelBestandUpload.logger.info("SoapHandler.sendFileData: OK");
        } catch (RemoteCallException ex) {
            ExcelBestandUpload.logger.error(ex, ex);
        }
    }

    private void setCellValue(HSSFRow row, int cellIndex, String value) {
        HSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
    }

    private String[] createHeader(String sLocale) throws MalformedURLException {

        String confPath;

        if (MainApp.debug) {
            confPath = "./conf";
        } else {
            confPath = "../conf";
        }

        File exportLabelsBundle = new File(confPath + "/ExportLabelsBundle.properties");
        if (!exportLabelsBundle.exists()) {
            ExcelBestandUpload.logger.warn("ExportLabelsBundle.properties nicht vorhanden. Verwende DefaultHeader. Pfad " + exportLabelsBundle.getAbsolutePath());

            //                   0            1             2      3        4          5        6             7        8                     9                   10            11             12             13
            return new String[]{"clxSKU-ID", "ERP-SKU-ID", "Neu", "Marke", "Artikel", "Farbe", "Farbkürzel", "Größe", "Artikelbezeichnung", "Kurzbeschreibung", "Lagermenge", "Zulaufmenge", "Zulaufstatus", "Auslauf"};
        }

        File conf = new File(confPath);
        URL[] urls = {conf.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);

        Locale locale = new Locale(sLocale);
        ExcelBestandUpload.logger.debug("Locale: " + locale);

        ArrayList<String> hList = new ArrayList<String>();

        for (HeaderKey key : HeaderKey.values()) {
            hList.add(getHeaderValue(locale, key.toString(), loader));
        }

        return hList.toArray(new String[0]);
    }

    private String getHeaderValue(Locale currentLocale, String key, ClassLoader loader) {
        ResourceBundle labels = ResourceBundle.getBundle("ExportLabelsBundle", currentLocale, loader, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));

        return labels.getString(key);
    }

 /*   private Boolean isSkuAuslauf(Connection con, int artgroesseid) throws SQLException {

        Boolean auslauf = null;

        java.sql.PreparedStatement pStmt = null;
        java.sql.ResultSet rs = null;

        String sql = "select ag.auslauf, af.auslauf, a.auslauf"
                + " from artgroesse ag"
                + " inner join artfarbe af on af.artfarbeid = ag.artfarbeid"
                + " inner join art a on a.artid = af.artid"
                + " where ag.artgroesseid = ?";

        try {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, artgroesseid);

            rs = pStmt.executeQuery();

            if (rs.next()) {
                auslauf = (rs.getInt(1) == 1) || (rs.getInt(2) == 1) || (rs.getInt(3) == 1);

                if (rs.wasNull()) {
                    return null;
                }
            }

            return auslauf;
        } finally {
            FirebirdDb.close(rs, pStmt, null);
        }
    }
*/
    private Integer getBestandAuslaufartikel(Connection con, int artgroesseid) throws SQLException {

        Integer bestand = null;

        java.sql.PreparedStatement pStmt = null;
        java.sql.ResultSet rs = null;

        String sql = "select agb.bestand"
                + " from artgroesse ag"
                + " inner join artfarbe af on af.artfarbeid = ag.artfarbeid"
                + " inner join hauptartgroesse hag on hag.hauptartgroesseid = ag.hauptartgroesseid"
                + " inner join art a on a.artid = af.artid"
                + " inner join art a_a on a_a.name = a.name || '_A'"
                + " inner join artfarbe af_a on af_a.farbenid = af.farbenid"
                + " inner join hauptartgroesse hag_a on hag_a.artid = a_a.artid and hag_a.groesseid = hag.groesseid"
                + " inner join artgroesse ag_a on ag_a.hauptartgroesseid = hag_a.hauptartgroesseid and ag_a.artfarbeid = af_a.artfarbeid"
                + " left join artgroessebestand agb on agb.artgroesseid = ag_a.artgroesseid"
                + " where ag.artgroesseid = ?";

        try {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, artgroesseid);

            rs = pStmt.executeQuery();

            if (rs.next()) {
                bestand = rs.getInt(1);

                if (rs.wasNull()) {
                    return null;
                }
            }

            return bestand;
        } finally {
            FirebirdDb.close(rs, pStmt, null);
        }
    }

    private Integer getWarengruppenid(Connection con, int artgroesseid) throws SQLException {

        Integer warengruppenid = null;

        java.sql.PreparedStatement pStmt = null;
        java.sql.ResultSet rs = null;

        String sql = "select ART.WARENGRUPPENID\n" +
                " from ARTGROESSE\n" +
                " inner join ARTFARBE on ARTFARBE.ARTFARBEID = ARTGROESSE.ARTFARBEID\n" +
                " inner join ART on ART.ARTID = ARTFARBE.ARTID\n" +
                " where ARTGROESSE.ARTGROESSEID = ?";

        try {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, artgroesseid);

            rs = pStmt.executeQuery();

            if (rs.next()) {
                warengruppenid = rs.getInt(1);

                if (rs.wasNull()) {
                    return null;
                }
            }

            return warengruppenid;
        } finally {
            FirebirdDb.close(rs, pStmt, null);
        }
    }

    private Zulauf getNaechsteZulaufinfo(Connection con, int artgroesseid) throws SQLException {
        java.sql.PreparedStatement pStmt = null;
        java.sql.ResultSet rs = null;

        String sql = "select AGZ.LIEFERKW" +
                " , IIF(agz.LIEFERKW >= (extract(week from date 'now')), (extract(year from date 'now')), (extract(year from date 'now'))+1) as LIEFERJAHR" +
                " , AGZ.STATUS" +
                " , AGZ.MENGE" +
                " from ARTGROESSEZULAUF AGZ" +
                " where AGZ.ARTGROESSEID = ?" +
                " order by AGZ.STATUS" +
                ", IIF(AGZ.LIEFERKW >= (extract(week from date 'now')), (extract(year from date 'now')), (extract(year from date 'now'))+1)" +
                ", AGZ.LIEFERKW";

        try {
            pStmt = con.prepareStatement(sql);
            pStmt.setInt(1, artgroesseid);

            rs = pStmt.executeQuery();

            if (rs.next()) {
                Zulauf zulauf = new Zulauf();

                zulauf.setStatus(rs.getString("STATUS"));

                if ("ORDERED".equalsIgnoreCase(rs.getString("STATUS"))) {
                    zulauf.setBestellt(true);
                } else {
                    zulauf.setBestellt(false);
                }

                zulauf.setMenge(rs.getInt("MENGE"));
                zulauf.setKalenderwoche(rs.getInt("LIEFERKW"));
                zulauf.setJahr(rs.getInt("LIEFERJAHR"));

                return zulauf;
            }
        } finally {
            FirebirdDb.close(rs, pStmt, null);
        }

        return null;
    }

    private int getWeekNumber() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    private int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private int getYearForWeeknumber(int lieferkw) {

        if (lieferkw < getWeekNumber()) {
            return getYear() + 1;
        } else {
            return getYear();
        }
    }

    private int getQuartal(int kw, int jahr) {

        final GregorianCalendar calendar = new GregorianCalendar(Locale.GERMANY);
        calendar.clear();
        calendar.set(Calendar.YEAR,jahr);
        calendar.set(Calendar.WEEK_OF_YEAR, kw);

        return (calendar.get(Calendar.MONTH) / 3) + 1;
    }

    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here

        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.DEBUG);

        MainApp.debug = true;

//        String iniFilename = "./conf/clxProductSync_fare.properties";
        String iniFilename = "./conf/clxProductSync.properties";
        ApplicationConfig.loadConfig(iniFilename);

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(iniFilename)));

        System.setProperty("complex.axis.default.timeout", String.valueOf(1000 * 60 * 10)); // 10 Minuten

        FirebirdDbPool.createInstance();
        Connection con = FirebirdDbPool.getInstance().getConnection();
        ExcelBestandUpload job = new ExcelBestandUpload(prop);

        System.out.println("Weeknumber " + job.getWeekNumber());
        System.out.println("Year " + job.getYear());

        job.run();

        con.close();

        //	job.run();
    }

}
