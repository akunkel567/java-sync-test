/*
 * NewAkEventHandler.java
 *
 * Created on 17. Juli 2006, 15:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import de.complex.activerecord.ActiveRecord;
import de.complex.database.AbstractClxDatabase;
import de.complex.database.exception.DbConnectionNotAvailableException;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.eventhandler.socketevent.config.ProcedureConfig;
import de.complex.clxproductsync.eventhandler.socketevent.config.TableConfig;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.clxproductsync.soap.axis.Snjobweb;
import de.complex.clxproductsync.soap.axis.XmlOut;
import de.complex.clxproductsync.xml.XmlCol;
import de.complex.clxproductsync.xml.XmlConverter;
import de.complex.clxproductsync.xml.XmlData;
import de.complex.clxproductsync.xml.XmlRow;
import de.complex.clxproductsync.xml.XmlTable;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import de.complex.tools.config.ApplicationConfig;
import java.sql.Connection;
import org.apache.axis.AxisFault;

/**
 *
 * @author kunkel
 */
public class WebDataEventHandler extends AbstractSocketEventHandler {

    private static Logger logger = Logger.getLogger(WebDataEventHandler.class);
    public static final String EVENTTYPE = SocketEventTypes.WEBDATA;
    private AbstractClxDatabase db;

    /**
     * Creates a new instance of NewAkEventHandler
     */
    public WebDataEventHandler(AbstractClxDatabase db) {
        super(SocketEventTypes.WEBDATA);
        this.db = db;
    }

    public void run() {

        Snjobweb currSnjobWeb = null;
        try {

            if (ApplicationConfig.getValue("websync", "true").equals("true")) {
                Snjobweb[] snjobwebs = null;

                int limit = Integer.parseInt(ApplicationConfig.getValue("snjobweblimit", "2"));

                do {
                    snjobwebs = SoapHandler.getSnJobWebList(limit);
                    WebDataEventHandler.logger.info("Anzahl snjobwebs: " + snjobwebs.length);

                    for (Snjobweb snjobweb : snjobwebs) {
                        currSnjobWeb = snjobweb;
                        WebDataEventHandler.logger.info("Snjobweb start - " + snjobweb);

                        try {
                            XmlOut xmlOut = SoapHandler.getXmlData(snjobweb.getSnjobwebid());

                            if (xmlOut != null) {
                                XmlData xmlData = new XmlConverter().xmlDataFromXml(xmlOut.getXml());

                                // Connection holen
                                Connection con = null;
                                try {
                                    con = this.db.getConnection();
                                    con.setReadOnly(false);
                                    con.setAutoCommit(false);

                                    if (snjobweb.getJobtyp().equals("D")) {
                                        WebDataEventHandler.logger.debug("delete Table A");
                                        deleteXmlTable(xmlData.getTable(), con);
                                        WebDataEventHandler.logger.debug("delete Table B");
                                    } else {
                                        WebDataEventHandler.logger.debug("save Table A");
                                        saveXmlTable(xmlData.getTable(), con);
                                        WebDataEventHandler.logger.debug("save Table B");
                                    }
                                    WebDataEventHandler.logger.debug("save/delete Table OK, Commit now");
                                    con.commit();
                                    WebDataEventHandler.logger.debug("save/delete Table after Commit");

                                    SoapHandler.setSnJobWebOK(snjobweb.getSnjobwebid());
                                    WebDataEventHandler.logger.info("Snjobweb done - " + currSnjobWeb);

                                } catch (SQLException ex) {
                                    WebDataEventHandler.logger.error("save not ok - Rollback SnjobWeb: " + snjobweb, ex);
                                    if (con != null) {
                                        con.rollback();
                                    }

                                    throw ex;
                                } finally {
                                    FirebirdDb.close(null, null, con);
                                }
                            }
                        } catch (RemoteCallException rce) {
                            if (rce.getCause() instanceof AxisFault && "410".equalsIgnoreCase(((AxisFault) rce.getCause()).getFaultCode().toString())) {
                                WebDataEventHandler.logger.warn("AxisFault: " + ((AxisFault) rce.getCause()).getFaultString(), rce);

                                // Datensatz nicht mehr vorhanden, wurde bereits gelöscht und noch offener Löschjob vorhanden 
                                SoapHandler.setSnJobWebOK(snjobweb.getSnjobwebid());
                            } else {
                                throw rce;
                            }
                        }
                    }
                } while (snjobwebs.length == limit);

                snjobwebs = null;
            } else {
                WebDataEventHandler.logger.warn("Socketevent: WEBDATA ist deaktiviert...!");
            }
        } catch (Exception e) {
            WebDataEventHandler.logger.error("Snjobweb error - " + currSnjobWeb, e);
        }
    }

    public void deleteXmlTable(XmlTable xmlTable, Connection con) throws SQLException {

        ActiveRecord activeRecord = null;
        ActiveRecord activeRow = null;

        activeRecord = new ActiveRecord(this.db, xmlTable.getName(), con);
        for (XmlRow row : xmlTable.getRows()) {

            activeRow = activeRecord.findOne(Long.parseLong(row.getPkValue()));

            if (activeRow != null) {
                if (!activeRow.delete(con)) {
                    throw new SQLException("Löschen fehlgeschlagen");
                }
            }
        }
    }

    public void saveXmlTable(XmlTable xmlTable, Connection con) throws SQLException {
        WebDataEventHandler.logger.debug("saveXmlTable tableName: " + xmlTable.getName() + " pkname: " + xmlTable.getPkName());

        ActiveRecord activeRecord = null;
        ActiveRecord activeRow = null;

        activeRecord = new ActiveRecord(this.db, xmlTable.getName(), con);
        for (XmlRow row : xmlTable.getRows()) {

            activeRow = activeRecord.findOne(Long.parseLong(row.getPkValue()));
            // wenn nichts gefunden
            if (activeRow == null) {
                activeRow = new ActiveRecord(this.db, xmlTable.getName(), con); //
            }

            for (XmlCol col : row.getCol()) {
                activeRow.setValue(col.getName(), col.getValue());
            }

            activeRow.save();

            for (XmlTable subTable : row.getSubTables()) {
                saveXmlTable(subTable, con);
            }

            // Proceduren aufrufen
            if (this.getTableConfigs().containsKey(xmlTable.getName().toLowerCase())) {
                TableConfig tc = this.getTableConfigs().get(xmlTable.getName().toLowerCase());

                for (ProcedureConfig pc : tc.getProcedureConfigs()) {
                    WebDataEventHandler.logger.debug("saveXmlTable table: " + xmlTable.getName() + " run Procedure: " + pc.getProcedureName() + " param: " + Long.parseLong(row.getPkValue().toString()));
                    this.db.executeProcedure(pc.getProcedureName(), Long.parseLong(row.getPkValue()), con);
                }
            }
        }

        WebDataEventHandler.logger.debug("saveXmlTable table: " + xmlTable.getName());
    }

    @Override
    public String getHandleEventType() {
        return WebDataEventHandler.EVENTTYPE;
    }
}
