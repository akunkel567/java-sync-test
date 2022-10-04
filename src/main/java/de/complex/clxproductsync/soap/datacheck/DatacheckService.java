
package de.complex.clxproductsync.soap.datacheck;

import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 3.0.1
 * Generated source version: 3.0
 * 
 */
@WebServiceClient(name = "DatacheckService", targetNamespace = "urn:Datacheck", wsdlLocation = "/wsdl/datacheck.wsdl")
public class DatacheckService
    extends Service
{

    private final static URL DATACHECKSERVICE_WSDL_LOCATION;
    private final static WebServiceException DATACHECKSERVICE_EXCEPTION;
    private final static QName DATACHECKSERVICE_QNAME = new QName("urn:Datacheck", "DatacheckService");

    static {
        DATACHECKSERVICE_WSDL_LOCATION = de.complex.clxproductsync.soap.datacheck.DatacheckService.class.getResource("/wsdl/datacheck.wsdl");
        WebServiceException e = null;
        if (DATACHECKSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find '/wsdl/datacheck.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        DATACHECKSERVICE_EXCEPTION = e;
    }

    public DatacheckService() {
        super(__getWsdlLocation(), DATACHECKSERVICE_QNAME);
    }

    public DatacheckService(WebServiceFeature... features) {
        super(__getWsdlLocation(), DATACHECKSERVICE_QNAME, features);
    }

    public DatacheckService(URL wsdlLocation) {
        super(wsdlLocation, DATACHECKSERVICE_QNAME);
    }

    public DatacheckService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, DATACHECKSERVICE_QNAME, features);
    }

    public DatacheckService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DatacheckService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns DatacheckPort
     */
    @WebEndpoint(name = "DatacheckPort")
    public DatacheckPort getDatacheckPort() {
        return super.getPort(new QName("urn:Datacheck", "DatacheckPort"), DatacheckPort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DatacheckPort
     */
    @WebEndpoint(name = "DatacheckPort")
    public DatacheckPort getDatacheckPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:Datacheck", "DatacheckPort"), DatacheckPort.class, features);
    }

    private static URL __getWsdlLocation() {
        if (DATACHECKSERVICE_EXCEPTION!= null) {
            throw DATACHECKSERVICE_EXCEPTION;
        }
        return DATACHECKSERVICE_WSDL_LOCATION;
    }

}
