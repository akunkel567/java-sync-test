/*
 * SocketListener.java
 *
 * Created on 17. Juli 2006, 09:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.jclxsync.eventhandler.socketevent;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class SocketEventListener extends Thread {

	private static Logger logger = Logger.getLogger(SocketEventListener.class);
	//private Logger logger;
	private InputStream inputStream;
	private String line;
	private boolean pong = false;
	private SocketEventManager socketEventManager;
	private boolean socketError = false;

	public SocketEventListener(SocketEventManager socketEventManager, Logger logger) {
		this.socketEventManager = socketEventManager;
		try {
			this.inputStream = socketEventManager.getSocket().getInputStream();
		} catch (UnknownHostException uhe) {
			System.out.println(uhe);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
		SocketEventListener.currentThread().setName("SocketEventListener_" + sdf.format(new Date()));

		SocketEventListener.logger.info("SocketEventListener run...!");

		try {
			String inputLine;
			int len;
			byte[] b = new byte[100];
			while (!this.isInterrupted()) {
				try {
					if ((len = inputStream.read(b)) == -1) {
						break;
					}
					inputLine = new String(b, 0, len).replaceAll("\n", "");
					inputLine = inputLine.replaceAll("\r", "");

					if (inputLine.equalsIgnoreCase(SocketEventTypes.PONG)) {
						SocketEventListener.logger.debug("Pong");
						socketEventManager.pong();
					} else {
						SocketEventListener.logger.debug("addEvent - inputLine :" + inputLine + ":");
						socketEventManager.addEvent(inputLine);
					}
				} catch (InterruptedIOException e) {
					//nochmal versuchen
				} catch (Exception e) {
					SocketEventListener.logger.error(e, e);
				}
			}

			socketEventManager.resetConnection();
		} finally {
			try {
				this.inputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		SocketEventListener.logger.info("SocketEventListener stopped...!");
	}
}
