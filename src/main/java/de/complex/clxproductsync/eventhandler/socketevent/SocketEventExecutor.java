/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class SocketEventExecutor extends Thread {

	private static Logger logger = Logger.getLogger(SocketEventExecutor.class);
	private String name;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:SSS");
	String eventname;

	public SocketEventExecutor(String eventname) {
		this.eventname = eventname;
		this.name = "SocketEventExecutor " + eventname + " " + sdf.format(new Date());
		SocketEventExecutor.logger.debug("EventExecutor new: " + this.name);
	}

	public String getEventname() {
		return eventname;
	}

	@Override
	public void run() {
		SocketEventExecutor.logger.debug("run");

		Thread.currentThread().setName(this.name);

		FirebirdDb db = null;
		try {
			db = FirebirdDbPool.getInstance();

			AbstractSocketEventHandler evh = AbstractSocketEventHandler.socketEventHandlerFactory(this.eventname, db);
			try {
				if (evh != null) {
					SocketEventExecutor.logger.debug("SocketEventExecutor Execute :" + evh.getClass().getSimpleName());
					evh.run();
				} else {
					SocketEventExecutor.logger.fatal("SocketEventHandler is null...!");
				}
			} catch (Exception e) {
				SocketEventExecutor.logger.error(e);
			}
		} finally {
		}

		SocketEventExecutor.logger.debug("ende...");
	}
}
