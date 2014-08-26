
package de.complex.jclxsync.monitor.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "jClxWebSyncWebServices", targetNamespace = "http://server.monitor.jclxwebsync.complex.de/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface JClxWebSyncWebServices {


    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "statusText", partName = "statusText")
    public String getStatusText();

    /**
     * 
     * @param pause
     */
    @WebMethod
    public void setPause(
        @WebParam(name = "pause", mode = WebParam.Mode.INOUT, partName = "pause")
        Holder<Boolean> pause);

    /**
     * 
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(name = "status", partName = "status")
    public boolean getStatus();

}
