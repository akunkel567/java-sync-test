/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.db.mssql.MssqlDb;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class MssqlTest {

    private static final SimpleDateFormat TIMESTAMP_FORMATTER
            = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMATTER
            = new SimpleDateFormat("dd-MMM-yyyy");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        new MssqlTest().run();
    }

    public void run() throws IOException {

        //-user sa -url jdbc:sqlserver://192.168.10.199:1433;databaseName=cdh_pc;integratedSecurity=false; -driver com.microsoft.sqlserver.jdbc.SQLServerDriver -table carton -showdata
        Properties prop = new Properties();

        File propFile = new File("./conf/jClxSync.properties");

        if (propFile.exists()) {

            FileReader reader = new FileReader(propFile);

            try {
                prop.load(reader);
            } finally {
                reader.close();
            }

        } else {
            System.out.println("Datei nicht vorhanden. " + propFile.getAbsolutePath());
            System.exit(1);
        }

        //MssqlDb db = new MssqlDb("192.168.10.208:1433", "cdh_pc", "sa", "");
//		MssqlDb db = new MssqlDb(prop.getProperty("cdh.hostname", ""), prop.getProperty("cdh.database", ""), prop.getProperty("cdh.username", ""), prop.getProperty("cdh.password", ""));
//		MssqlDb db = new MssqlDb("192.168.10.111", "cdh_pc", "sa", "sa1234##");
        MssqlDb db = new MssqlDb("192.168.10.111", "cdh_pc", "sa_complex", "complex", "CDH");

        Connection con = null;

        try {
            con = db.getConnection();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

        System.out.println("");
        try {
            Statement adrStmt = con.createStatement();
            ResultSet rstable = adrStmt.executeQuery("SELECT ARTICLE_PHYSICAL_STOCK.ARTICLE_SERIAL_NO as cdhid, ARTICLE_PHYSICAL_STOCK. PHYSICAL_STOCK as bestand from ARTICLE_PHYSICAL_STOCK");
            ResultSetMetaData meta = rstable.getMetaData();

            System.out.println("ColumnCount: " + meta.getColumnCount());
            for (int index = 1; index <= meta.getColumnCount(); index++) {
                int colIndex = index;

                StringBuilder sb = new StringBuilder();

                sb.append("colIndex: " + colIndex);
                sb.append("\tcolName: " + meta.getColumnLabel(colIndex));
                sb.append("\tcolType: " + meta.getColumnType(colIndex));
                sb.append("\tcolTypeName: " + meta.getColumnTypeName(colIndex));
                sb.append("\tcolClassName: " + meta.getColumnClassName(colIndex));
                System.out.println(sb.toString());
            }

//			StringBuilder sb = new StringBuilder();
//			sb.append("\nline: 0").append(" ");
//
//			for (int index = 1; index <= meta.getColumnCount(); index++) {
//				sb.append(meta.getColumnLabel(index)).append("\t");
//			}
//			System.out.println(sb.toString());
//
//			int linecnt = 0;
//			while (rstable.next()) {
//				sb = new StringBuilder();
//				sb.append("line: ").append(++linecnt).append(" ");
//				for (int index = 1; index <= meta.getColumnCount(); index++) {
//
//
//					sb.append(getColumnValue(rstable, meta.getColumnType(index), index)).append("\t");
//
//				}
//
//				System.out.println(sb.toString());
//			}
            rstable.close();
            adrStmt.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Unable to extract database meta data.");
            showSQLException(e);
        }

    }

    private static String getColumnValue(ResultSet rs, int colType, int colIndex)
            throws SQLException, IOException {

        String value = "";

        switch (colType) {
            case Types.BIT:
                Object bit = rs.getObject(colIndex);
                if (bit != null) {
                    value = String.valueOf(bit);
                }
                break;
            case Types.BOOLEAN:
                boolean b = rs.getBoolean(colIndex);
                if (!rs.wasNull()) {
                    value = Boolean.valueOf(b).toString();
                }
                break;
            case Types.CLOB:
                Clob c = rs.getClob(colIndex);
                if (c != null) {
                    value = read(c);
                }
                break;
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.REAL:
            case Types.NUMERIC:
                BigDecimal bd = rs.getBigDecimal(colIndex);
                if (bd != null) {
                    value = "" + bd.doubleValue();
                }
                break;
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
                int intValue = rs.getInt(colIndex);
                if (!rs.wasNull()) {
                    value = "" + intValue;
                }
                break;
            case Types.JAVA_OBJECT:
                Object obj = rs.getObject(colIndex);
                if (obj != null) {
                    value = String.valueOf(obj);
                }
                break;
            case Types.DATE:
                java.sql.Date date = rs.getDate(colIndex);
                if (date != null) {
                    value = DATE_FORMATTER.format(date);
                    ;
                }
                break;
            case Types.TIME:
                Time t = rs.getTime(colIndex);
                if (t != null) {
                    value = t.toString();
                }
                break;
            case Types.TIMESTAMP:
                Timestamp tstamp = rs.getTimestamp(colIndex);
                if (tstamp != null) {
                    value = TIMESTAMP_FORMATTER.format(tstamp);
                }
                break;
            case Types.LONGVARCHAR:
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
                value = rs.getString(colIndex);
                break;
            default:
                value = "";
        }

        if (value == null) {
            value = "";
        }

        return value;

    }

    private static String read(Clob c) throws SQLException, IOException {
        StringBuffer sb = new StringBuffer((int) c.length());
        Reader r = c.getCharacterStream();
        char[] cbuf = new char[2048];
        int n = 0;
        while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
            if (n > 0) {
                sb.append(cbuf, 0, n);
            }
        }
        return sb.toString();
    }

    private static void showSQLException(java.sql.SQLException e) {
        // Notice that a SQLException is actually a chain of SQLExceptions,
        // let's not forget to print all of them...
        java.sql.SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }
}
