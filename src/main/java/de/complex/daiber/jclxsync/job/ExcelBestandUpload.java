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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
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
					ExcelBestandUpload.logger.debug("FirebirdDb Connection:" + con);
					stmtSprachen = con.createStatement();
					stmt = con.createStatement();

					rsSprachen = stmtSprachen.executeQuery("select SPRACHEID, KUERZEL from SPRACHE order by SPRACHEID");

					while (rsSprachen.next()) {

						int spracheid = rsSprachen.getInt(1);
						String kuerzel = rsSprachen.getString(2);

//						Parameter Procedure
//						P_MARKENID
//						P_SPRACHEID
//						P_WEBAKTIV
//						P_AUSLAUFARTIKEL
//						P_SONDERPOSTEN
						String sql = "SELECT * FROM SEL_EXPORT_SKULAGER(null," + spracheid + ",1,null,null)";
						rs = stmt.executeQuery(sql);

						File file = new File("bestand.csv");

						OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1");

						ICsvMapWriter writer = new CsvMapWriter(osw, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						try {
//                                                         0        1        2      3        4          5        6             7        8                     9                   10            11             12    
							final String[] header = new String[]{"clxID", "cdhID", "Neu", "Marke", "Artikel", "Farbe", "Farbkürzel", "Größe", "Artikelbezeichnung", "Kurzbeschreibung", "Lagermenge", "Zulaufmenge", "Zulaufstatus"};
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
}
