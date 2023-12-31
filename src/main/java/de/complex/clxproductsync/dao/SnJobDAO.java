/*
 * SnJobDAO.java
 *
 * Created on 3. Juli 2006, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.dao;

import de.complex.database.SQLLog;
import de.complex.database.exception.DbConnectionNotAvailableException;
import de.complex.database.firebird.FirebirdDb;
import de.complex.util.datetime.DateTool;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.logging.log4j.*;
import java.util.Vector;

/**
 *
 * @author kunkel
 */
public class SnJobDAO {

    private static Logger logger = LogManager.getLogger(SnJobDAO.class);
    //private static Logger testlog = LogManager.getLogger("testlog");
    FirebirdDb db;
    private String checkEventQry = null;
    public static final int SNJOBDONE = 1;
    public static final int SNJOBDONEWHILENOTEXISTS = 2;

    /**
     * Creates a new instance of SnJobDAO
     */
    public SnJobDAO(FirebirdDb db) {
        this.db = db;
    }

    public synchronized String[] checkEvent(String eventname) {

        List list = new ArrayList<String>();
        list.add(eventname);

        return this.checkEvents(list);
    }

    public synchronized String[] checkEvents(List<String> eventnames) {

//		if (logger.isDebugEnabled()) {
//			logger.debug("eventname anzahl: " + eventnames.size());
//			for (String s : eventnames) {
//				logger.debug("eventnames:" + s);
//			}
//		}
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;

        if (checkEventQry == null) {
            StringBuilder sbQry = new StringBuilder();

            sbQry.append(" SELECT DISTINCT "
                    + " SNJOB.EVENTNAME"
                    + " FROM SNJOB"
                    + " WHERE SNJOB.DONE = 0");

            if (!eventnames.isEmpty()) {
                sbQry.append(" AND SNJOB.EVENTNAME IN (");

                boolean first = true;
                for (String s : eventnames) {
                    if (first) {
                        first = false;
                        sbQry.append("'").append(s).append("'");
                    } else {
                        sbQry.append(",'").append(s).append("'");
                    }
                }
                sbQry.append(")");
            }

            sbQry.append(" ORDER BY SNJOBID");

            checkEventQry = sbQry.toString();
        }

        Vector vec = new Vector();
        try {
            c = this.db.getConnection();
            if (c != null) {
                stmt = c.createStatement();
                logger.debug(checkEventQry);
                rs = stmt.executeQuery(checkEventQry);

                while (rs.next()) {
                    vec.add(rs.getString("EVENTNAME"));
                }
            }
        } catch (java.sql.SQLException e) {
            logger.error("Query: " + checkEventQry, e);
            return null;
        } finally {
            FirebirdDb.close(rs, stmt, c);
        }

        return (String[]) vec.toArray(new String[0]);
    }

    /**
     * get the Next SnJob from the Database
     */
    public synchronized SnJob getNextJob(String eventname) {
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;

        SnJob snJob = null;

        int artId = 0;

        String qry = "";
        try {
            c = this.db.getConnection();
            if (c != null) {
                stmt = c.createStatement();

                qry = " SELECT FIRST 1"
                        + " SNJOB.SNJOBID"
                        + ", SNJOB.EVENTNAME"
                        + ", SNJOB.FREMDID"
                        + ", SNJOB.JOBTYP"
                        + ", SNJOB.FILENAME"
                        + ", SNJOB.HERKUNFT"
                        + ", SNJOB.SAU "
                        + ", SNJOB.SADT "
                        + ", SNJOB.SCU "
                        + ", SNJOB.SCDT"
                        + " FROM SNJOB"
                        + " WHERE SNJOB.EVENTNAME =  '" + eventname + "'"
                        + " AND SNJOB.DONE = 0"
                        + " ORDER BY SNJOBID";

                logger.debug(qry);
                rs = stmt.executeQuery(qry);

                while (rs.next()) {
                    snJob = new SnJob();

                    snJob.setSnJobId(rs.getInt("SNJOBID"));
                    snJob.setEventName(rs.getString("EVENTNAME"));
                    snJob.setSnJobFremdId(rs.getInt("FREMDID"));
                    snJob.setSnJobTyp(rs.getString("JOBTYP"));
                    snJob.setFileName(rs.getString("FILENAME"));
                    snJob.setHerkunft(rs.getString("HERKUNFT"));
                    snJob.setSau(rs.getString("SAU"));
                    snJob.setSadt(rs.getString("SADT"));
                    snJob.setScu(rs.getString("SCU"));
                    snJob.setScdt(rs.getString("SCDT"));
                }
            }
        } catch (java.sql.SQLException e) {
            FirebirdDb.showSQLException(e, qry, this.getClass().getName());
            return null;
        } finally {
            FirebirdDb.close(rs, stmt, c);

        }

        return snJob;
    }

    /**
     * get an Array of SnJob from the Database
     */
    public synchronized SnJob[] getNextJobs(String eventName, int maxCount) {
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        java.sql.PreparedStatement pStmt = null;
        java.sql.ResultSet rs = null;

        Vector snJobVector = new Vector();

        SnJob snJob = null;

        int artId = 0;

        String qry = "";
        try {
            c = db.getConnection();
            // pause
            if (c != null) {
                stmt = c.createStatement();

                if (maxCount == 0) {
                    qry = " SELECT";
                } else {
                    qry = " SELECT FIRST " + maxCount + " ";
                }

                qry += " SNJOB.SNJOBID"
                        + ", SNJOB.EVENTNAME"
                        + ", SNJOB.FREMDID"
                        + ", SNJOB.JOBTYP"
                        + ", SNJOB.FILENAME"
                        + ", SNJOB.HERKUNFT "
                        + ", SNJOB.SAU "
                        + ", SNJOB.SADT "
                        + ", SNJOB.SCU "
                        + ", SNJOB.SCDT"
                        + " FROM SNJOB"
                        + " WHERE SNJOB.EVENTNAME =  '" + eventName + "'"
                        + " AND SNJOB.DONE = 0"
                        + " ORDER BY SNJOBID";

                logger.debug("SnJob: " + qry);

                rs = stmt.executeQuery(qry);

                snJobVector.clear();
                while (rs.next()) {
                    snJob = new SnJob();

                    snJob.setSnJobId(rs.getInt("SNJOBID"));
                    snJob.setEventName(rs.getString("EVENTNAME"));
                    snJob.setSnJobFremdId(rs.getInt("FREMDID"));
                    snJob.setSnJobTyp(rs.getString("JOBTYP"));
                    snJob.setFileName(rs.getString("FILENAME"));
                    snJob.setHerkunft(rs.getString("HERKUNFT"));
                    snJob.setSau(rs.getString("SAU"));
                    snJob.setSadt(rs.getString("SADT"));
                    snJob.setScu(rs.getString("SCU"));
                    snJob.setScdt(rs.getString("SCDT"));

                    snJobVector.add(snJob);
                }
            }
        } catch (java.sql.SQLException e) {
            FirebirdDb.showSQLException(e, qry, this.getClass().getName());
            return null;
        } finally {
            FirebirdDb.close(rs, stmt, c);
        }

        try {
            return (SnJob[]) snJobVector.toArray(new SnJob[0]);
        } finally {
            snJobVector.clear();
            snJobVector = null;
        }
    }

    /**
     * get an SnJob from the Database with the Parameter snJobID
     */
    public synchronized SnJob getSnJob(int snJobId) {
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;

        SnJob snJob = null;

        int artId = 0;

        String qry = "";
        try {
            c = db.getConnection();
            if (c != null) {
                stmt = c.createStatement();

                qry = " SELECT SNJOB.EVENTNAME"
                        + ", SNJOB.JOBART"
                        + ", SNJOB.FREMDID"
                        + ", SNJOB.JOBTYP"
                        + ", SNJOB.FILENAME"
                        + ", SNJOB.HERKUNFT "
                        + ", SNJOB.SAU "
                        + ", SNJOB.SADT "
                        + ", SNJOB.SCU "
                        + ", SNJOB.SCDT"
                        + " FROM SNJOB"
                        + " WHERE SNJOB.SNJOBID = " + snJobId;

                logger.debug(qry);

                rs = stmt.executeQuery(qry);

                while (rs.next()) {
                    snJob = new SnJob();

                    snJob.setSnJobId(rs.getInt("SNJOBID"));
                    snJob.setEventName(rs.getString("EVENTNAME"));
                    snJob.setSnJobFremdId(rs.getInt("FREMDID"));
                    snJob.setSnJobTyp(rs.getString("JOBTYP"));
                    snJob.setFileName(rs.getString("FILENAME"));
                    snJob.setHerkunft(rs.getString("HERKUNFT"));
                    snJob.setSau(rs.getString("SAU"));
                    snJob.setSadt(rs.getString("SADT"));
                    snJob.setScu(rs.getString("SCU"));
                    snJob.setScdt(rs.getString("SCDT"));
                }
            }
        } catch (java.sql.SQLException e) {
            FirebirdDb.showSQLException(e, qry, this.getClass().getName());
            return null;
        } finally {
            FirebirdDb.close(rs, stmt, c);

        }

        return snJob;
    }

    /**
     * set the SnJob in the Database done
     */
    synchronized public void setSnJobDone(SnJob snJob) throws SQLException {
        doSnJobDone(snJob, SNJOBDONE);
    }

    synchronized public void setSnJobDoneWhileNotExists(SnJob snJob) throws SQLException {
        doSnJobDone(snJob, SNJOBDONEWHILENOTEXISTS);
    }

    private void doSnJobDone(SnJob snJob, int doneValue) throws SQLException {
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        int rows = 0;

        String updateQry = "";

        try {
            c = db.getConnection();
            c.setReadOnly(false);
            c.setAutoCommit(false);
            if (c != null) {

                stmt = c.createStatement();

                updateQry = " UPDATE SNJOB SET DONE = " + doneValue
                        + " WHERE SNJOB.SNJOBID = " + snJob.getSnJobId();

                logger.debug(updateQry);
                rows = stmt.executeUpdate(updateQry);

                if (rows != 0) {
                    c.commit();
                } else {
                    c.rollback();
                }
            }
        } finally {
            FirebirdDb.close(null, stmt, c);
        }
    }

    /**
     * set the SnJob's in the Database done
     */
    synchronized public int setSnJobsDone(SnJob[] snJobs) throws SQLException {
        java.sql.Connection c = null;
        java.sql.Statement stmt = null;
        int rowsCount = 0;

        String updateQry = "";
        try {
            c = db.getConnection();
            c.setReadOnly(false);
            c.setAutoCommit(false);

            if (c != null) {
                stmt = c.createStatement();

                for (int i = 0; i < snJobs.length; i++) {
                    updateQry = " UPDATE SNJOB SET DONE = 1"
                            + " WHERE SNJOB.SNJOBID = " + snJobs[i].getSnJobId();

                    logger.debug(updateQry);
                    rowsCount += stmt.executeUpdate(updateQry);
                }

                if (rowsCount == snJobs.length) {
                    c.commit();
                } else {
                    c.rollback();
                }
            }
        } finally {
            FirebirdDb.close(null, stmt, c);
        }

        return rowsCount;
    }

    public Integer[] getIdList() {
        return null;
    }

    public boolean isDeleteJobAvailable(SnJob job) throws SQLException {
        java.sql.Connection c = null;
        java.sql.PreparedStatement pstmt = null;
        java.sql.ResultSet rs = null;

        try {
            c = this.db.getConnection();

            // Sadt umn 2 Minuten erweitern
            Date sadt = DateTool.simpleParse(job.getSadt(), "yyyy-MM-dd HH:mm:ss.SSS");
            Calendar cal = new GregorianCalendar();
            cal.setTime(sadt);
            cal.add(Calendar.MINUTE, +2);

            String qry = " SELECT COUNT(*) AS ANZAHL"
                    + " FROM SNJOB"
                    + " WHERE SNJOB.EVENTNAME =  ?" // 1
                    + " AND SNJOB.FREMDID = ?" // 2
                    + " AND SNJOB.JOBTYP = ?" // 3
                    + " AND SNJOB.FILENAME = ?" // 4
                    + " AND SNJOB.SNJOBID > ?" // 5
                    + " AND SNJOB.SADT < ?"; // 6

            pstmt = c.prepareStatement(qry);

            pstmt.setString(1, job.getEventName());
            pstmt.setInt(2, job.getSnJobFremdId());
            pstmt.setString(3, "D");
            pstmt.setString(4, job.getFileName());
            pstmt.setInt(5, job.getSnJobId());
            pstmt.setDate(6, new java.sql.Date(cal.getTimeInMillis()));

            logger.debug(qry);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("ANZAHL") > 0) {
                    return true;
                }
            }

            return false;
        } finally {
            FirebirdDb.close(rs, pstmt, c);
        }
    }

}
