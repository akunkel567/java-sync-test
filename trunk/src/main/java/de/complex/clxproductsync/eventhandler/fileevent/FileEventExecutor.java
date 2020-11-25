/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.fileevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class FileEventExecutor extends Thread {

    private static Logger logger = Logger.getLogger(FileEventExecutor.class);
    private String name;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
    String eventname = null;

    public FileEventExecutor(String eventname) {
        this.eventname = eventname;
        this.name = "FileEventExecutor " + eventname + " " + sdf.format(new Date());
        FileEventExecutor.logger.debug("FileEventExecutor new: " + this.name);
    }

    public String getEventname() {
        return eventname;
    }

    @Override
    public synchronized void run() {
        FileEventExecutor.logger.debug("run");

        Thread.currentThread().setName(this.name);
        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        FirebirdDb db = null;
        try {
            db = FirebirdDbPool.getInstance();

            FileEventHandler feh = new FileEventHandler(db);
            try {
                if (feh != null) {
                    FileEventExecutor.logger.debug("Execute :" + feh.getClass().getSimpleName());
                    feh.run();
                } else {
                    FileEventExecutor.logger.fatal("FileEventHandler is null...!");
                }
            } catch (Exception e) {
                FileEventExecutor.logger.error(e, e);
            }
        } finally {
        }

        FileEventExecutor.logger.debug("ende...");
    }
}
