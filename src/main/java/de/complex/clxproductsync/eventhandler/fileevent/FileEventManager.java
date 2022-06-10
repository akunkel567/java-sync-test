/*
 * EventHandler.java
 *
 * Created on 4. Juli 2006, 15:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.fileevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class FileEventManager extends Thread {

    private static Logger logger = LogManager.getLogger(FileEventManager.class);
    private Properties prop;

    public static int jobCheckInterval = 10 * 1000;

    public FileEventManager() {
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
        FileEventManager.currentThread().setName("FileEventManager" + "_" + sdf.format(new Date()));
        FileEventManager.logger.info("FileEventManager start...!");

        ExecutorService tPool = Executors.newFixedThreadPool(10);
        HashMap<String, Future> futureList = new HashMap<String, Future>();

        boolean firstrun = true;

        try {
            while (!this.isInterrupted()) {
                try {
                    FirebirdDb db = null;

                    try {
                        db = FirebirdDbPool.getInstance();

                        try {
                            // DatenbankEvents ausfuehren
                            FileEventChecker eventChecker = new FileEventChecker(db, "FileEventChecker");

                            if (firstrun) {
                                eventChecker.getEventList().add(FileEventChecker.EVENTNAME);
                                firstrun = false;
                            }

                            if (eventChecker.isEventOccured()) {
                                LinkedList<String> dbEvents = (LinkedList) eventChecker.getEventList().clone();

                                for (String eventname : dbEvents) {
                                    eventname = eventname.toUpperCase();
                                    FileEventManager.logger.debug("next Event is: " + eventname);

                                    Future f = futureList.get(eventname);
                                    FileEventManager.logger.debug("Event Future :" + f);
                                    if (f != null) {
                                        FileEventManager.logger.debug("f: Event Future isDone :" + f.isDone());
                                    }
                                    if ((f == null) || (f.isDone())) {
                                        FileEventManager.logger.debug("Event: " + eventname + " neu erzeugen");
                                        futureList.put(eventname, tPool.submit(new FileEventExecutor(eventname)));
                                        eventChecker.getEventList().remove(eventname);
                                    } else {
                                        FileEventManager.logger.debug("Event :" + eventname + " ist noch aktiv");
                                    }
                                }

                                dbEvents = null;
                            } else {
                                FileEventManager.logger.debug("kein Event occured");
                            }

                        } catch (Exception ex) {
                            FileEventManager.logger.fatal("Hauptschleife Error", ex);
                        }

                    } finally {
                        if (db != null) {
                            //db.shutdown();
                        }
                        db = null;
                    }

                    yield();
                    try {
                        Thread.sleep(jobCheckInterval);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                } catch (Exception ex) {
                    FileEventManager.logger.fatal("Main Hauptschleife Error", ex);
                }

                // SocketEventThread check...
            }

        } finally {
            tPool.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!tPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    tPool.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!tPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        System.err.println("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                tPool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }

        FileEventManager.logger.info("FileEventManager stopped...!");
    }

    public static int getJobCheckInterval() {
        return jobCheckInterval;
    }

    public static void setJobCheckInterval(int jobCheckInterval) {
        FileEventManager.jobCheckInterval = jobCheckInterval;
    }

}
