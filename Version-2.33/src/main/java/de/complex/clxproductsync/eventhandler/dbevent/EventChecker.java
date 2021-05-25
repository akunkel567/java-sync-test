/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.dbevent;

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
public class EventChecker {

    private static Logger logger = Logger.getLogger(EventChecker.class);
    private String threadName;
    private FirebirdDb db = null;
    private List<String> eventNames = new ArrayList<String>();
    private SnJobDAO snjobDAO;
    private LinkedList<String> eventList = new LinkedList<String>();

    public EventChecker(FirebirdDb db, String threadName) {
        this.threadName = threadName;
        this.db = db;
        this.snjobDAO = new SnJobDAO(db);
    }

    public EventChecker(FirebirdDb db, String threadName, String[] eventNames) {
        this.threadName = threadName;
        this.db = db;
        this.snjobDAO = new SnJobDAO(db);
        this.addEventNames(eventNames);
    }

    public void addEventName(String eventName) {
        this.eventNames.add(eventName);
        logger.info("Thread: " + this.threadName + " addEventName: " + eventName);
    }

    public void addEventNames(String[] eventNames) {
        this.eventNames.clear();

        for (String eventName : eventNames) {
            this.eventNames.add(eventName);
            //logger.debug("Thread: " + this.threadName + " addEventName: " + eventName);
        }
    }

    public synchronized boolean isEventOccured() {
        if (logger.isDebugEnabled()) {
            logger.debug("isEventOccured - Threadname: " + threadName + "  check Events");
        }
        String events[] = null;
        if ((events = this.snjobDAO.checkEvents(eventNames)) != null) {
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
