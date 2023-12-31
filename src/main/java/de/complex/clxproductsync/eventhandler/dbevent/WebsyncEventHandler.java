
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.dbevent;

//import de.complex.jclxsync.eventhandler.dbevent.AbstractEventHandler;
import de.complex.activerecord.ActiveRecord;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.config.EventConfig;
import de.complex.clxproductsync.config.FirebirdEventConfig;
import de.complex.clxproductsync.dao.SnJob;
import de.complex.clxproductsync.dao.SnJobDAO;
import de.complex.clxproductsync.exception.EventConfigException;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.clxproductsync.eventhandler.socketevent.xml.XmlConverter;
import de.complex.tools.config.ApplicationConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class WebsyncEventHandler {

    private static Logger logger = LogManager.getLogger(WebsyncEventHandler.class);
    private Properties prop = null;
    private String eventname = null;
    private FirebirdDb db = null;

    public WebsyncEventHandler(FirebirdDb db) {
        this.db = db;
    }

//	public EventConfig getEventConfig(String eventName) {
//		EventConfig evc = this.eventConfigs.get(eventName.toUpperCase());
//		return (EventConfig) this.eventConfigs.get(eventName);
//	}
    public void run(String eventName) {
        WebsyncEventHandler.logger.debug("ActionName: " + eventName + " start");
        ActiveRecord ar = null;
        ActiveRecord[] ars = null;
        String xml = null;

        int limit = Integer.parseInt(ApplicationConfig.getValue("snjoblimit", "1000"));

        if (limit == 0) {
            limit = 1000;
        }

        FirebirdEventConfig eventconfigs = (FirebirdEventConfig) ApplicationConfig.getObject("eventconfig");
        int evcIndex = eventconfigs.getEventConfigs().indexOf(new EventConfig(eventName));
        EventConfig eventconfig = eventconfigs.getEventConfigs().get(evcIndex);

        SnJob[] snJobs = null;
        int debugcount = 0;
        XmlConverter xmlConverter = new XmlConverter();

        SnJobDAO snjobDAO = new SnJobDAO(this.db);
        SnJob currJob = null;
        try {

            do {
                debugcount = 0;
                WebsyncEventHandler.logger.debug("getNextJobs eventname: " + eventName + " limit: " + limit);
                snJobs = snjobDAO.getNextJobs(eventName, limit);
                WebsyncEventHandler.logger.debug("Snjobs limit: " + limit + " aktualCount: " + snJobs.length);
                for (SnJob job : snJobs) {

                    currJob = job;
                    WebsyncEventHandler.logger.debug("Job start - " + job.toString());
                    WebsyncEventHandler.logger.debug("Debugcount: " + debugcount + " snjobCount: " + snJobs.length);
                    debugcount++;

                    if (eventconfig == null) {
                        throw new EventConfigException("EventConfig prüfen: " + job.getEventName() + " ist nicht konfiguriert");
                    }

                    if (job.getSnJobTyp().equalsIgnoreCase("d")) {
                        // Delete

                        ar = new ActiveRecord(this.db, eventconfig.getActionName());

                        SoapHandler.sendXmlData(job, xmlConverter.fromBlankActiveRecord(ar, job.getSnJobFremdId()));
                        snjobDAO.setSnJobDone(job);
                        WebsyncEventHandler.logger.debug("Job done - " + job.toString() + " done");
                    } else {
                        // Insert und Update
                        Connection con = null;
                        try {
                            con = this.db.getConnection();
                            ar = new ActiveRecord(this.db, eventconfig.getActionName(), con);

                            ars = ar.findAllById(job.getSnJobFremdId());
                            if ((ars != null) && (ars.length != 0)) {
                                xml = xmlConverter.fromActiveRecords(ars);

                                SoapHandler.sendXmlData(job, xml);
                                snjobDAO.setSnJobDone(job);
                                WebsyncEventHandler.logger.info(String.format("Job done - snjobid: %d eventname: %s fremdid: %d",job.getSnJobId(), job.getEventName(), job.getSnJobFremdId()));
                            } else {
                                snjobDAO.setSnJobDoneWhileNotExists(job);
                                WebsyncEventHandler.logger.warn("Job done, Datensatz nicht vorhanden - " + job);
                            }
                        } catch (NullPointerException npe) {
                            WebsyncEventHandler.logger.error(npe, npe);
                        } catch (SQLException sqlex) {
                            WebsyncEventHandler.logger.error(sqlex, sqlex);
                        } finally {
                            FirebirdDb.close(null, null, con);
                        }
                    }
                }

                WebsyncEventHandler.logger.debug("vor While snJobs.length: " + snJobs.length + " limit: " + limit);
            } while (snJobs.length == limit);

        } catch (Exception ex) {
            WebsyncEventHandler.logger.error("Job error - " + currJob, ex);
        }

        WebsyncEventHandler.logger.info("ActionName: " + eventName + " ende");
    }
}
