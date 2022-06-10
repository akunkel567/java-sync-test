/*
 * SocketListener.java
 *
 * Created on 17. Juli 2006, 09:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.complex.util.lang.StringTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class SocketEventListener extends Thread {

    private static Logger logger = LogManager.getLogger(SocketEventListener.class);
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
            String inputString;
            int len;
            byte[] b = new byte[100];
            while (!this.isInterrupted()) {
                try {
                    if ((len = inputStream.read(b)) == -1) {
                        break;
                    }
                    inputString = new String(b, 0, len);
                    SocketEventListener.logger.debug("inputString :" + inputString + ":");
                    inputString = StringTool.getNotNullTrim(inputString);
                    SocketEventListener.logger.debug("inputString trim :" + inputString + ":");

                    String lines[] = inputString.split("\n");
                    for (String line : lines) {
                        SocketEventListener.logger.debug("line :" + line + ":");
                        line = StringTool.getNotNullTrim(line);

                        if (!StringTool.isEmpty(line)) {
                            if (line.equalsIgnoreCase(SocketEventTypes.PONG)) {
                                SocketEventListener.logger.debug("Pong");
                                socketEventManager.pong();
                            } else {
                                SocketEventListener.logger.debug("addEvent :" + line + ":");
                                socketEventManager.addEvent(line);
                            }
                        }
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
