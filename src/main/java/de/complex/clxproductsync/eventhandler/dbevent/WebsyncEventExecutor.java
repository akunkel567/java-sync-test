/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.dbevent;

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
public class WebsyncEventExecutor extends Thread {

	private static Logger logger = Logger.getLogger(WebsyncEventExecutor.class);
	private String name;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
	String eventname = null;

	public WebsyncEventExecutor(String eventname) {
		this.eventname = eventname;
		this.name = "WebsyncEventExecutor " + eventname + " " + sdf.format(new Date());
		WebsyncEventExecutor.logger.debug("EventExecutor new: " + this.name);
	}

	public String getEventname() {
		return eventname;
	}

	@Override
	public synchronized void run() {
		WebsyncEventExecutor.logger.debug("run");

		Thread.currentThread().setName(this.name);
		Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

		FirebirdDb db = null;
		try {
			db = FirebirdDbPool.getInstance();

			WebsyncEventHandler wevh = new WebsyncEventHandler(db);
			try {
				if (wevh != null) {
					WebsyncEventExecutor.logger.debug("Execute :" + wevh.getClass().getSimpleName());
					wevh.run(eventname);
				} else {
					WebsyncEventExecutor.logger.fatal("EventHandler is null...!");
				}
			} catch (Exception e) {
				WebsyncEventExecutor.logger.error(e,e);
			}
		} finally {
		}

		WebsyncEventExecutor.logger.debug("ende...");
	}
}
