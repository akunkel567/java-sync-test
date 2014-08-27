/*
 * EventHandler.java
 *
 * Created on 4. Juli 2006, 15:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.dbevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.config.FirebirdEventConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class WebsyncEventManager extends Thread {

	private static Logger logger = Logger.getLogger(WebsyncEventManager.class);
	private Properties prop;
	private String[] eventnames;
	private FirebirdEventConfig eventconfig;
	public static int jobCheckInterval = 10 * 1000;
	
	// private AbstractEventHandler eventHandler;
	/** Creates a new instance of EventHandler */
	public WebsyncEventManager(String[] eventnames, FirebirdEventConfig eventconfig) {
		this.eventnames = eventnames;
		this.eventconfig = eventconfig;
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
		WebsyncEventManager.currentThread().setName("EventManager" + "_" + sdf.format(new Date()));
		WebsyncEventManager.logger.info("EventManager start...!");

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
							EventChecker eventChecker = new EventChecker(db, "EventChecker", eventnames);

							if (firstrun) {
								for (String s : eventnames) {
									eventChecker.getEventList().add(s);
								}
								firstrun = false;
							}

							if (eventChecker.isEventOccured()) {
								LinkedList<String> dbEvents = (LinkedList) eventChecker.getEventList().clone();

								for (String eventname : dbEvents) {
									eventname = eventname.toUpperCase();
									WebsyncEventManager.logger.debug("next Event is: " + eventname);

									Future f = futureList.get(eventname);
									WebsyncEventManager.logger.debug("Event Future :" + f);
									if (f != null) {
										WebsyncEventManager.logger.debug("f: Event Future isDone :" + f.isDone());
									}
									if ((f == null) || (f.isDone())) {
										WebsyncEventManager.logger.debug("Event: " + eventname + " neu erzeugen");
										futureList.put(eventname, tPool.submit(new WebsyncEventExecutor(eventname)));
										eventChecker.getEventList().remove(eventname);
									} else {
										WebsyncEventManager.logger.debug("Event :" + eventname + " ist noch aktiv");
									}
								}

								dbEvents = null;
							} else {
								WebsyncEventManager.logger.debug("kein Event occured");
							}

						} catch (Exception ex) {
							WebsyncEventManager.logger.fatal("Hauptschleife Error", ex);
						}

					} finally {
						if (db != null) {
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
					WebsyncEventManager.logger.fatal("Main Hauptschleife Error", ex);
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

		WebsyncEventManager.logger.info("WebsyncEventManager stopped...!");
	}

	public static int getJobCheckInterval() {
		return jobCheckInterval;
	}

	public static void setJobCheckInterval(int jobCheckInterval) {
		WebsyncEventManager.jobCheckInterval = jobCheckInterval;
	}
	
	
}
