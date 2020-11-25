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
import de.complex.clxproductsync.config.EventConfig;
import de.complex.clxproductsync.config.FirebirdEventConfig;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.TablesyncHandler;
import de.complex.clxproductsync.soap.datacheck.DatacheckPort;
import de.complex.clxproductsync.soap.datacheck.DatacheckService;
import de.complex.clxproductsync.soap.datacheck.DatacheckServiceLocator;
import de.complex.clxproductsync.soap.datacheck.Returnnok;
import de.complex.clxproductsync.soap.datacheck.Spoolcheck;
import de.complex.clxproductsync.soap.tablesync.TableIdList;
import de.complex.tools.config.ApplicationConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;

import org.apache.log4j.Logger;

/**
 *
 * @author Kunkel
 */
public class WebTableCheck extends Thread {

    private static Logger logger = Logger.getLogger(WebTableCheck.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd ss:SSS");

    private CommandLine cmdLine = null;

    public static void main(String[] args) throws Exception {
        // TODO code application logic here

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);

        WebTableCheck tableCheck = new WebTableCheck();
        tableCheck.runFromCommandLine(args);
    }

    private void runFromCommandLine(String[] args) throws Exception {
        Options options = new Options();

        Option opt = null;

        opt = OptionBuilder.withDescription("Config-File").hasArg().create("config");
        options.addOption(opt);

        opt = OptionBuilder.withDescription("Eventconfig-File").hasArg().create("eventconfig");
        options.addOption(opt);

        opt = OptionBuilder.withDescription("Zu prüfenden Tabellen").hasArgs().create("tables");
        options.addOption(opt);

        CommandLineParser parser = new GnuParser();

        StringBuilder usage = new StringBuilder();
        usage.append("java -jar WebTableCheck.jar");

        StringBuilder footer = new StringBuilder();
        footer.append("\n\n");

        try {
            cmdLine = parser.parse(options, args);

            if (args.length == 0) {
                throw new org.apache.commons.cli.ParseException("");
            }
        } catch (org.apache.commons.cli.ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(180, usage.toString(), "", options, footer.toString(), true);

            return;
        }

        if (cmdLine.hasOption("config")) {
            ApplicationConfig.loadConfig(cmdLine.getOptionValue("config", ""));
        } else {
            ApplicationConfig.loadConfig("./conf/jClxSync.properties");
        }

        FirebirdEventConfig eventconfig = null;
        if (cmdLine.hasOption("eventconfig")) {
            eventconfig = FirebirdEventConfig.factory(cmdLine.getOptionValue("eventconfig", ""));
        } else {
            if (cmdLine.hasOption("tables")) {
                eventconfig = new FirebirdEventConfig();

                for (String tablename : cmdLine.getOptionValues("tables")) {
                    eventconfig.getEventConfigs().add(new EventConfig(tablename));
                }
            } else {
                ApplicationConfig.loadConfig("./conf/jClxSync.properties");
            }
        }

        ApplicationConfig.setObject("eventconfig", eventconfig);
        FirebirdDbPool.createInstance();

        if (ApplicationConfig.hasValue("truststore")) {
            System.setProperty("javax.net.ssl.trustStore", ApplicationConfig.getValue("truststore", "./conf/jssecacerts"));
        }

        if (ApplicationConfig.hasValue("truststorepassword")) {
            System.setProperty("javax.net.ssl.trustStorePassword", ApplicationConfig.getValue("truststorepassword", "changeit"));
        }

//		CustomTrustManager trust = new CustomTrustManager("./conf/InternetWidgitsPtyLtd.crt");
//		//noch nicht aktiv muß noch getestet werden		, beim nächsten update beim kunden testen		...
//		//Create a trust manager that does not validate certificate chains
//		TrustManager[] trustAllCerts = new TrustManager[]{
//			new X509TrustManager() {
//				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				public void checkClientTrusted(
//						  java.security.cert.X509Certificate[] certs, String authType) {
//				}
//
//				public void checkServerTrusted(
//						  java.security.cert.X509Certificate[] certs, String authType) {
//				}
//			}
//		};
//
//		// Install the all-trusting trust manager
//		SSLContext sc = SSLContext.getInstance("SSL");
//		sc.init(null, trustAllCerts, new java.security.SecureRandom());
//		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        run();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("TableCheck " + sdf.format(new Date()));
        WebTableCheck.logger.info("TableCheck run");
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        FirebirdDb db = null;
        java.sql.Connection c = null;
        java.sql.PreparedStatement pStmt = null;
        java.sql.Statement stmt = null;

        java.sql.ResultSet rs = null;

        try {
            db = FirebirdDbPool.getInstance();
            c = db.getConnection();

            String sql = null;

            FirebirdEventConfig eventconfig = (FirebirdEventConfig) ApplicationConfig.getObject("eventconfig");

            WebTableCheck.logger.info("Anzahl Tabellen: " + eventconfig.getEventConfigs().size());

            List<EventConfig> events = eventconfig.getEventConfigs();

            for (EventConfig event : events) {
                String tablename = event.getEventName().toUpperCase();
                WebTableCheck.logger.info("Tabelle: " + event.getEventName());

                TableIdList tableIdList = TablesyncHandler.getTableIdList(tablename);
                WebTableCheck.logger.info("Anzahl Id's: " + tableIdList.getSize());

                stmt = c.createStatement();
                stmt.executeUpdate("delete from WEBTABLESYNC where upper(WEBTABLESYNC.TABLENAME)=upper('" + tablename + "')");

                sql = "insert into WEBTABLESYNC (TABLENAME, TABLEID) values (?, ?)";
                pStmt = c.prepareStatement(sql);

                String[] ids = tableIdList.getIdList().split(";");

                for (String id : ids) {
                    if (!"".equals(id)) {
                        pStmt.setString(1, tablename);
                        pStmt.setString(2, id);
                        //pStmt.execute();
                        pStmt.addBatch();
                    }
                }
                pStmt.executeBatch();

                stmt = c.createStatement();
                stmt.executeUpdate("delete from WEBTABLESYNCDIFF where upper(WEBTABLESYNCDIFF.TABLENAME)=upper('" + tablename + "')");

                sql = "insert into WEBTABLESYNCDIFF (TABLENAME, TABLEIDOFFLINE, TABLEIDONLINE) values (?, ?, ?)";
                pStmt = c.prepareStatement(sql);

                sql = createDiffSql(tablename);

                //System.out.println("SQL: " + sql);
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    pStmt.setString(1, tablename);

                    pStmt.setInt(2, rs.getInt("TABLEIDOFFLINE"));
                    if (rs.wasNull()) {
                        pStmt.setNull(2, java.sql.Types.INTEGER);
                    }

                    pStmt.setInt(3, rs.getInt("TABLEIDONLINE"));
                    if (rs.wasNull()) {
                        pStmt.setNull(3, java.sql.Types.INTEGER);
                    }

                    pStmt.execute();
                }

                c.commit();
            }

        } catch (Exception e) {
            WebTableCheck.logger.error(e, e);

            try {
                if (c != null && !c.isClosed()) {
                    c.rollback();
                }
            } catch (SQLException ex) {
                WebTableCheck.logger.warn(ex, ex);
            }

        } finally {
            FirebirdDb.close(null, pStmt, c);
        }

        WebTableCheck.logger.info("TableCheck fertig");
    }

    private String createDiffSql(String tablename) {
        String sql;
        sql = "select TABLEIDOFFLINE, TABLEIDONLINE"
                + " from ("
                + " select A." + tablename + "ID AS TABLEIDOFFLINE, WEBTABLESYNC.TABLEID AS TABLEIDONLINE"
                + " from " + tablename + " A"
                + " left outer join WEBTABLESYNC on (WEBTABLESYNC.TABLEID = A." + tablename + "ID and WEBTABLESYNC.TABLENAME = '" + tablename + "')"
                + " union all"
                + " select B." + tablename + "ID AS TABLEIDOFFLINE, WEBTABLESYNC.TABLEID AS TABLEIDONLINE"
                + " from WEBTABLESYNC"
                + " left outer join " + tablename + " B on (B." + tablename + "ID = WEBTABLESYNC.TABLEID)"
                + " where WEBTABLESYNC.TABLENAME = '" + tablename + "')"
                + " where (TABLEIDOFFLINE is null) or (TABLEIDONLINE is null)";
        return sql;
    }

//	private class CustomTrustManager {
//
//		private String trustedCertPath = null;
//		private X509Certificate trustedCert;
//
//		private CustomTrustManager(String trustedCertPath) throws KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException {
//			this.trustedCertPath = trustedCertPath;
//
//			init();
//		}
//
//		private void init() throws KeyManagementException, NoSuchAlgorithmException, CertificateException, FileNotFoundException {
//
//			final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//			trustedCert = (X509Certificate) certificateFactory.generateCertificate(new FileInputStream(new File(trustedCertPath)));
//
//			final TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
//
//				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException {
//				}
//
//				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException {
//					if (!certs[0].equals(trustedCert)) {
//						throw new CertificateException();
//					}
//				}
//
//			}};
//
//			final SSLContext sslContext = SSLContext.getInstance("SSL");
//			sslContext.init(null, trustManager, new java.security.SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//		}
//	}
}
