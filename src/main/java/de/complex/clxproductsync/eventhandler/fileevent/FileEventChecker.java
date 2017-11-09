/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.fileevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.dao.SnJobDAO;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class FileEventChecker {

    private static Logger logger = Logger.getLogger(FileEventChecker.class);
    private String threadName;
    private FirebirdDb db = null;

    public static final String EVENTNAME = "FILEEVENT";

    private SnJobDAO snjobDAO;
    private LinkedList<String> eventList = new LinkedList<String>();

    public FileEventChecker(FirebirdDb db, String threadName) {
        this.threadName = threadName;
        this.db = db;
        this.snjobDAO = new SnJobDAO(db);
    }

    public synchronized boolean isEventOccured() {
        if (logger.isDebugEnabled()) {
            logger.debug("isEventOccured - Threadname: " + threadName + "  check Events");
        }
        String events[] = null;
        if ((events = this.snjobDAO.checkEvent(FileEventChecker.EVENTNAME)) != null) {
            for (String s : events) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Event: " + s.toUpperCase());
                }
                this.eventList.add(s);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("isEventOccured - Threadname: " + threadName + " hasEvent: " + !this.eventList.isEmpty());
        }

        return !this.eventList.isEmpty();
    }

    public String getNextEvent() {
        return this.eventList.remove();
    }

    public boolean hasMoreEvents() {
        return !this.eventList.isEmpty();
    }

    public LinkedList<String> getEventList() {
        return eventList;
    }

}
