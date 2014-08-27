/*
 * SocketEventManager.java
 *
 * Created on 17. Juli 2006, 15:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.tools.config.ApplicationConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class SocketEventManager extends Thread {

	private static Logger logger = Logger.getLogger(SocketEventManager.class);
	private HashMap<String, AbstractSocketEventHandler> eventHandlerList;
	private Socket socket;
	private String socketURL;
	private int socketPort;
	private String socketUser;
	private String socketPass;
	private boolean connected = false;
	private boolean error = false;
	private InputStream in;
	private OutputStream out;
	private SocketEventListener socketEventListener;
	private Date connResetTime;
	private LinkedList<String> eventList = null;
	private boolean pong = false;

	/** Creates a new instance of SocketEventManager */
	public SocketEventManager() {
		this.socketURL = ApplicationConfig.getValue("socketURL");
		this.socketPort = new Integer(ApplicationConfig.getValue("socketPort")).intValue();
		this.socketPass = ApplicationConfig.getValue("socketPass");

		this.eventHandlerList = new HashMap();
		this.setConnected(false);

		this.eventList = new LinkedList<String>();
	}

	public void addHandler(AbstractSocketEventHandler aEventHandler) {
		SocketEventManager.logger.info("Add SocketEventHandler :" + aEventHandler.getHandleEventType().toString() + ":");
		this.eventHandlerList.put(aEventHandler.getHandleEventType().toString(), aEventHandler);
	}

	public HashMap<String, AbstractSocketEventHandler> getEventHandlerList() {
		return eventHandlerList;
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
		SocketEventManager.currentThread().setName("SocketEventManager_" + sdf.format(new Date()));
		Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());
		SocketEventManager.logger.info("SocketEventManager start...!");

		String eventnames[] = AbstractSocketEventHandler.getEventNames();
		boolean firstrun = true;

		final int PINGINVERVAL = 30;
		final int CHECKINVERVAL = 120;

		ExecutorService tPool = Executors.newFixedThreadPool(5);
		HashMap<String, Future> futureList = new HashMap<String, Future>();

		try {
			String sockEvent;
			Date nextPing = new Date(System.currentTimeMillis() + PINGINVERVAL * 1000L);
			Date checkAll = new Date(System.currentTimeMillis() + CHECKINVERVAL * 1000L);

			try {
				if (!this.isConnected()) {
					this.openConnection();
				}

				if (!this.isConnected()) {
					this.resetConnection();
				}
			} catch (Exception e) {
				SocketEventManager.logger.error(e, e);
			}

			while (!this.isInterrupted()) {
				try {
					try {
						if (firstrun) {
							addAllEvents();
							firstrun = false;
						}

						if (!this.eventList.isEmpty()) {
							LinkedList<String> socketEvents = (LinkedList) this.eventList.clone();

							for (String eventname : socketEvents) {
								eventname = eventname.toUpperCase();
								SocketEventManager.logger.info("SocketEvent ausfuehren :" + eventname);

								Future f = futureList.get(eventname);
								SocketEventManager.logger.debug("SocketEvent Future :" + f);
								if (f != null) {
									SocketEventManager.logger.debug("f iSocketEvent Future isDone :" + f.isDone());
								}
								if ((f == null) || (f.isDone())) {
									SocketEventManager.logger.debug("SocketEvent " + eventname + " neu gestartet");

									SocketEventManager.logger.debug("Event: " + eventname + " neu erzeugen");
									futureList.put(eventname, tPool.submit(new SocketEventExecutor(eventname)));
									this.eventList.remove(eventname);
								} else {
									SocketEventManager.logger.debug("SocketEvent " + eventname + " lauft noch, spaeter versuchen");
								}
							}

							socketEvents = null;
						}
					} catch (java.lang.NullPointerException npe) {
						logger.error("socketEventListener nicht vorhanden !");
						// nix...! todo
					}

					try {
						if (nextPing.before(new Date())) {
							nextPing = new Date(System.currentTimeMillis() + PINGINVERVAL * 1000L);
							this.ping();
						}
					} catch (Exception e) {
						// nix
					}

					try {
						if (checkAll.before(new Date())) {
							checkAll = new Date(System.currentTimeMillis() + CHECKINVERVAL * 1000L);
							this.addAllEvents();
						}
					} catch (Exception e) {
						// nix
					}

					yield();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						this.interrupt();
					}
				} catch (NullPointerException npe) {
					SocketEventManager.logger.error(npe, npe);
				} catch (Exception e) {
					SocketEventManager.logger.error(e, e);
				}
			}
		} finally {
			tPool.shutdown(); // Disable new tasks from being submitted
			try {
				// Wait a while for existing tasks to terminate
				if (!tPool.awaitTermination(30, TimeUnit.SECONDS)) {
					tPool.shutdownNow(); // Cancel currently executing tasks
					// Wait a while for tasks to respond to being cancelled
					if (!tPool.awaitTermination(30, TimeUnit.SECONDS)) {
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

		SocketEventManager.logger.info("SocketEventManager stopped...!");
	}

	private void addAllEvents() {
		this.addEventInternal(SocketEventTypes.WEBDATA);
	}

	public synchronized void addEvent(String eventName) {
		SocketEventManager.logger.debug("AddSocketEvent from External: " + eventName.toUpperCase());
		this.eventList.add(eventName.toUpperCase());
	}

	private synchronized void addEventInternal(String eventName) {
		SocketEventManager.logger.debug("AddSocketEvent from Internal: " + eventName.toUpperCase());
		this.eventList.add(eventName.toUpperCase());
	}

	public boolean openConnection() {
		SocketEventManager.logger.info("try to open a SocketConnection...");
		SocketEventManager.logger.info(String.format("Connection Info Host:%s Port:%s User:%s Pass:%s", this.socketURL, this.socketPort, this.socketUser, this.socketPass));

		if (connResetTime != null) {
			SocketEventManager.logger.error("SocketConnection Reset Time :" + connResetTime.toString());
			SocketEventManager.logger.error("System time :" + new Date().toString());

			if (connResetTime.getTime() > (new Date().getTime() - 60000)) {
				SocketEventManager.logger.error("connection pause ...");
				return this.isConnected();
			}
		}

		this.connected = false;
		socketEventListener = null;
		boolean ret = false;
		int len;
		byte[] b = new byte[100];

		try {
			this.socket = null;
			this.socket = new Socket(this.socketURL, this.socketPort);
			this.socket.setSoTimeout(1000);
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();

			// Login
			if ((len = in.read(b)) == -1) {
				ret = false;
			}

			out.write(new String("auth: " + this.socketPass).getBytes());
			out.write('\r');
			out.write('\n');

			if ((len = in.read(b)) == -1) {
				ret = false;
				SocketEventManager.logger.error("Socket Login Error");
			}

			if (new String(b, 0, len).equalsIgnoreCase("ok\n")) {
				SocketEventManager.logger.info("Socket logged In");
				this.setConnected(true);
				socketEventListener = new SocketEventListener(this, SocketEventManager.logger);
				socketEventListener.start();
			}

		} catch (UnknownHostException uhe) {
			SocketEventManager.logger.error(uhe, uhe);
			this.resetConnection();
		} catch (IOException ioe) {
			SocketEventManager.logger.error(ioe, ioe);
			this.resetConnection();
		}

		return this.isConnected();
	}

	public boolean ping() {
		if (!this.isConnected()) {
			this.openConnection();
		}

		if (this.isConnected()) {
			SocketEventManager.logger.debug("Ping");
			String line = "ping";

			try {
				out.write(line.getBytes());
				out.write('\r');
				out.write('\n');
			} catch (IOException ioe) {
				SocketEventManager.logger.error("Ping : " + ioe);
				this.resetConnection();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public void pong() {
		this.pong = true;
	}

	public boolean isPongOccured() {
		if (this.pong) {
			this.pong = false;
			return true;
		} else {
			return false;
		}
	}

	public void setConnected(boolean connected) {
		SocketEventManager.logger.info("SocketConnection connected: " + connected);
		this.connected = connected;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public boolean resetConnection() {
		connResetTime = new Date();

		if (this.socketEventListener != null) {
			SocketEventManager.logger.debug("SocketEventListener Interrupt...!");
			this.socketEventListener.interrupt();
		}
		if (this.socket != null) {
			try {
				this.socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			this.socket = null;
		}
		this.setConnected(false);
		// todo
		return true;
	}

	public Socket getSocket() {
		return this.socket;
	}

	public SocketEventListener getSocketEventListener() {
		return this.socketEventListener;
	}
}
