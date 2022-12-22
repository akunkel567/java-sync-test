/*
 * NewAkEventHandler.java
 *
 * Created on 17. Juli 2006, 15:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import de.complex.activerecord.ActiveRecord;
import de.complex.clxproductsync.soap.ResponseErrorCodes;
import de.complex.clxproductsync.soap.transfer.Snjobweb;
import de.complex.clxproductsync.soap.transfer.SnjobwebList;
import de.complex.clxproductsync.soap.transfer.XmlOut;
import de.complex.database.AbstractClxDatabase;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.eventhandler.socketevent.config.ProcedureConfig;
import de.complex.clxproductsync.eventhandler.socketevent.config.TableConfig;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlCol;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlConverter;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlData;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlRow;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlTable;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.complex.tools.config.ApplicationConfig;
import java.sql.Connection;

/**
 *
 * @author kunkel
 */
public class WebDataEventHandler extends AbstractSocketEventHandler {

    private static Logger logger = LogManager.getLogger(WebDataEventHandler.class);
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
                SnjobwebList snjobwebs = null;

                int limit = Integer.parseInt(ApplicationConfig.getValue("snjobweblimit", "2"));

                do {
                    snjobwebs = SoapHandler.getSnJobWebList(limit);
                    WebDataEventHandler.logger.info("Anzahl snjobwebs: " + snjobwebs.getItem().size());

                    for (Snjobweb snjobweb : snjobwebs.getItem()) {
                        currSnjobWeb = snjobweb;
                        WebDataEventHandler.logger.debug("Snjobweb start - " + snjobweb);

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
                                    WebDataEventHandler.logger.info("Snjobweb done - {}", currSnjobWeb);

                                } catch (SQLException ex) {
                                    WebDataEventHandler.logger.error("save not ok - Rollback SnjobWeb: " + snjobweb.toString(), ex);
                                    if (con != null) {
                                        con.rollback();
                                    }

                                    throw ex;
                                } finally {
                                    FirebirdDb.close(null, null, con);
                                }
                            }
                        } catch (ServerSOAPFaultException rce) {
                            if (!ResponseErrorCodes.isDatasetNotFoundDeleteJobOpen(rce.getFault().getFaultCode())) {
                                throw rce;
                            }

                            WebDataEventHandler.logger.warn("Datensatz nicht mehr vorhanden. Job wird auf done gestellt.");
                            SoapHandler.setSnJobWebOK(snjobweb.getSnjobwebid());
                        }
                    }
                } while (snjobwebs.getItem().size() == limit);

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
