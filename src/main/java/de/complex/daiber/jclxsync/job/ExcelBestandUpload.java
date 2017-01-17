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
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.clxproductsync.tools.ClxFtp;
import de.complex.tools.config.ApplicationConfig;
import de.complex.util.lang.StringTool;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author Kunkel
 */
public class ExcelBestandUpload extends Thread {

	private static Logger logger = Logger.getLogger(ExcelBestandUpload.class);
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");
	private Properties prop;

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

                    rsSprachen = stmtSprachen.executeQuery("select SPRACHEID, KUERZEL, LOCALE from SPRACHE order by SPRACHEID");

                    while (rsSprachen.next()) {

						int spracheid = rsSprachen.getInt(1);
						String kuerzel = rsSprachen.getString(2);
                                                String sLocale = rsSprachen.getString(3);

//						Parameter Procedure
//						P_MARKENID
//						P_SPRACHEID
//						P_WEBAKTIV
//						P_AUSLAUFARTIKEL
//						P_SONDERPOSTEN
						
                                                String excelbestandShopid = prop.getProperty("excelbestand.shopid", "null");
                                                if(StringTool.isEmpty(excelbestandShopid)){
                                                    excelbestandShopid = "null";
                                                }

                                                String sql = "SELECT * FROM SEL_EXPORT_SKULAGER(null," + spracheid + ",1,null,null," + excelbestandShopid + ")";
						ExcelBestandUpload.logger.debug("sql:" + sql);
                                                rs = stmt.executeQuery(sql);

						File file = new File("bestand.csv");

						OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");

						ICsvMapWriter writer = new CsvMapWriter(osw, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						try {
                                                        final String[] header = createHeader(sLocale);
                                                        writer.writeHeader(header);

							int rowIndex = 0;
							while (rs.next()) {
								rowIndex++;
								ExcelBestandUpload.logger.debug("row:" + rowIndex);

								final HashMap<String, ? super Object> data = new HashMap<String, Object>();

								data.put(header[0], rs.getString("ARTGROESSEID") == null ? "" : rs.getString("ARTGROESSEID"));
								data.put(header[1], rs.getString("FREMDID") == null ? "" : rs.getString("FREMDID"));
								data.put(header[2], rs.getString("NEU") == null ? "" : rs.getString("NEU"));
								data.put(header[3], rs.getString("MARKE") == null ? "" : rs.getString("MARKE"));
								data.put(header[4], rs.getString("ARTIKEL") == null ? "" : rs.getString("ARTIKEL"));
								data.put(header[5], rs.getString("FARBE") == null ? "" : rs.getString("FARBE"));
								data.put(header[6], rs.getString("FARBKUERZEL") == null ? "" : rs.getString("FARBKUERZEL"));
								data.put(header[7], rs.getString("GROESSE") == null ? "" : rs.getString("GROESSE"));
								data.put(header[8], rs.getString("ARTIKELBEZECIHNUNG") == null ? "" : rs.getString("ARTIKELBEZECIHNUNG"));
								data.put(header[9], rs.getString("KURZBESCHREIBUNG") == null ? "" : rs.getString("KURZBESCHREIBUNG"));

								DecimalFormat dformat = new DecimalFormat("########0");
								try {
									int lagermenge = rs.getInt("LAGERMENGE");
									data.put(header[10], dformat.format(lagermenge));
								} catch (Exception e) {
									ExcelBestandUpload.logger.error(e, e);
									data.put(header[10], "N/A");
								}

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
									data.put(header[12], rs.getString("ZULAUFSTATUS_SH"));
								} catch (Exception e) {
									ExcelBestandUpload.logger.error(e, e);
									data.put(header[12], "N/A");
								}

								try {
									writer.write(data, header);
								} catch (Exception e) {
									ExcelBestandUpload.logger.error("writer: " + writer + " data: " + data + " header: " + header);
									ExcelBestandUpload.logger.error(e, e);

								}
							}

						} finally {
							try {
								writer.close();
							} catch (Exception ignore) {
								ExcelBestandUpload.logger.error(ignore, ignore);
							}

						}

						try {
							ExcelBestandUpload.logger.info("SoapHandler.sendFileData: " + file.getAbsolutePath());
							SoapHandler.sendFileData(null, "bestandexcel", spracheid, file);
						} catch (RemoteCallException ex) {
							ExcelBestandUpload.logger.error(ex, ex);
						}

						File ftpUploadFile = null;

						if (!ApplicationConfig.getValue("excelbestand.ftphost", "").equals("")) {
							ExcelBestandUpload.logger.info("ftpUpload: " + file.getAbsolutePath());
							try {
								ExcelBestandUpload.logger.debug("file absolutePath: " + file.getAbsolutePath());
								ExcelBestandUpload.logger.debug("file path: " + file.getPath());
								ExcelBestandUpload.logger.debug("file name: " + file.getName());

								ftpUploadFile = new File("bestand_" + rsSprachen.getString("KUERZEL") + ".csv");
								ExcelBestandUpload.logger.debug(ftpUploadFile.getAbsolutePath() + " - " + ftpUploadFile.getName());

								FileUtils.copyFile(file, ftpUploadFile);

								ExcelBestandUpload.logger.debug("file: " + file);
								ExcelBestandUpload.logger.debug("ftpUploadFile: " + ftpUploadFile);

								String ftphost = ApplicationConfig.getValue("excelbestand.ftphost", "");
								String username = ApplicationConfig.getValue("excelbestand.username", "");
								String password = ApplicationConfig.getValue("excelbestand.password", "");
								String remotepath = ApplicationConfig.getValue("excelbestand.remotepath", ".");

								ClxFtp ftp = new ClxFtp(ftphost, username, password);
								if (!ftp.uploadFile(ftpUploadFile, remotepath)) {
									ExcelBestandUpload.logger.error("ftp Upload fehler");
								}
							} catch (Exception e) {
								ExcelBestandUpload.logger.error(e, e);
							}
						} else {
							ExcelBestandUpload.logger.debug("Kein Ftp-Upload. Ftp-Zugang nicht konfiguriert.");
						}

						try {
							if (file != null && file.exists()) {
								file.delete();
							}
						} catch (Exception ignore) {
						}

						try {
							if (ftpUploadFile != null && ftpUploadFile.exists()) {
								ftpUploadFile.delete();
							}
						} catch (Exception ignore) {
						}

					}

					con.commit();

				} catch (java.sql.SQLException e) {
					ExcelBestandUpload.logger.error("SQL Error", e);
					SQLLog.logger.error("SQL Error.", e);
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

	private void setCellValue(HSSFRow row, int cellIndex, String value) {
		HSSFCell cell = row.createCell(cellIndex);
		cell.setCellValue(value);
	}

    private String[] createHeader(String sLocale) throws MalformedURLException {

        File exportLabelsBundle = new File("./conf/ExportLabelsBundle.properties");
        if(!exportLabelsBundle.exists()){
            ExcelBestandUpload.logger.warn("./conf/ExportLabelsBundle.properties nicht vorhanden. Verwende DefaultHeader");
            return new String[]{"clxID", "FK int.", "Neu", "Marke", "Artikel", "Farbe", "Farbkürzel", "Größe", "Artikelbezeichnung", "Kurzbeschreibung", "Lagermenge", "Zulaufmenge", "Zulaufstatus"};
        }
        
        File conf = new File("./conf");
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

	public static void main(String[] args) throws IOException, SQLException {
		// TODO code application logic here

		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);

		String iniFilename = "./conf/clxProductSync.properties";
		ApplicationConfig.loadConfig(iniFilename);

		Properties prop = new Properties();
		prop.load(new FileInputStream(new File(iniFilename)));

		System.setProperty("complex.axis.default.timeout", String.valueOf(1000 * 60 * 10)); // 10 Minuten

		FirebirdDbPool.createInstance();
		Connection con = FirebirdDbPool.getInstance().getConnection();
		ExcelBestandUpload job = new ExcelBestandUpload(prop);

		job.run();

		con.close();

		//	job.run();
	}

}
