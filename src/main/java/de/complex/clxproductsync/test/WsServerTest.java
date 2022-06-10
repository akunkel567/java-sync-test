/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.monitor.server.WsServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import javax.xml.ws.Endpoint;

/**
 *
 * @author kunkel
 */
public class WsServerTest extends Thread {

    private static Logger logger = LogManager.getLogger(WsServerTest.class);
    private Endpoint endpoint;
    private WsServer wsServer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new WsServerTest();
    }

    public WsServerTest() {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            logger.debug(ex,ex);
        }

        wsServer = new WsServer();
        endpoint = Endpoint.publish("http://192.168.50.104:8088/services", wsServer);
        System.out.println("Endpoint running...");
    }

}
