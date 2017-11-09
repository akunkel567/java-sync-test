/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.monitor.server;

import de.complex.clxproductsync.MainApp;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
@WebService(name = "jClxWebSyncWebServices")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WsServer {

    private static Logger logger = Logger.getLogger(WsServer.class);
    private MainApp mainApp = null;
    private boolean pause = false;

    public void WsServer() {
    }

    @WebMethod(operationName = "getStatusText")
    @WebResult(name = "statusText")
    public String getStatusInfo() {
        WsServer.logger.debug("getStatusText");
        if (pause) {
            return "is paused";
        } else {
            return "is running";
        }
    }

    @WebMethod(operationName = "getStatus")
    @WebResult(name = "status")
    public boolean getStatus() {
        WsServer.logger.debug("getStatusInfo");
        if (pause) {
            return true;
        } else {
            return false;
        }
    }

    @WebMethod(operationName = "setPause")
    @WebResult(name = "pause")
    public boolean setPause(@WebParam(name = "pause") boolean pause) {
        this.pause = pause;
        WsServer.logger.info("setPause pause: " + this.pause);

        // MainApp Pause
        if (this.mainApp != null) {
//			this.mainApp.setPause(pause);
        }

        return this.pause;
    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
