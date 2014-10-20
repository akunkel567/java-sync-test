/**
 * DatacheckServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.datacheck;

public class DatacheckServiceLocator extends org.apache.axis.client.Service implements de.complex.clxproductsync.soap.datacheck.DatacheckService {

    public DatacheckServiceLocator() {
    }


    public DatacheckServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DatacheckServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DatacheckPort
    private java.lang.String DatacheckPort_address = "http://backend.bjshop.ab/datacheck";

    public java.lang.String getDatacheckPortAddress() {
        return DatacheckPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DatacheckPortWSDDServiceName = "DatacheckPort";

    public java.lang.String getDatacheckPortWSDDServiceName() {
        return DatacheckPortWSDDServiceName;
    }

    public void setDatacheckPortWSDDServiceName(java.lang.String name) {
        DatacheckPortWSDDServiceName = name;
    }

    public de.complex.clxproductsync.soap.datacheck.DatacheckPort getDatacheckPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DatacheckPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDatacheckPort(endpoint);
    }

    public de.complex.clxproductsync.soap.datacheck.DatacheckPort getDatacheckPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.complex.clxproductsync.soap.datacheck.DatacheckBindingStub _stub = new de.complex.clxproductsync.soap.datacheck.DatacheckBindingStub(portAddress, this);
            _stub.setPortName(getDatacheckPortWSDDServiceName());
				
				_stub.setTimeout(Integer.parseInt(System.getProperty("complex.axis.default.timeout")));
								
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDatacheckPortEndpointAddress(java.lang.String address) {
        DatacheckPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.complex.clxproductsync.soap.datacheck.DatacheckPort.class.isAssignableFrom(serviceEndpointInterface)) {
                de.complex.clxproductsync.soap.datacheck.DatacheckBindingStub _stub = new de.complex.clxproductsync.soap.datacheck.DatacheckBindingStub(new java.net.URL(DatacheckPort_address), this);
                _stub.setPortName(getDatacheckPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DatacheckPort".equals(inputPortName)) {
            return getDatacheckPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Datacheck", "DatacheckService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:Datacheck", "DatacheckPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DatacheckPort".equals(portName)) {
            setDatacheckPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
