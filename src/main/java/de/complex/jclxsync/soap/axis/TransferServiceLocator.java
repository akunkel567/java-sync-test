/**
 * TransferServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.axis;

public class TransferServiceLocator extends org.apache.axis.client.Service implements de.complex.jclxsync.soap.axis.TransferService {

    public TransferServiceLocator() {
    }


    public TransferServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransferServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TransferPort
    private java.lang.String TransferPort_address = "http://backend.daibersync.ak/transfer";

    public java.lang.String getTransferPortAddress() {
        return TransferPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TransferPortWSDDServiceName = "TransferPort";

    public java.lang.String getTransferPortWSDDServiceName() {
        return TransferPortWSDDServiceName;
    }

    public void setTransferPortWSDDServiceName(java.lang.String name) {
        TransferPortWSDDServiceName = name;
    }

    public de.complex.jclxsync.soap.axis.TransferPort getTransferPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TransferPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransferPort(endpoint);
    }

    public de.complex.jclxsync.soap.axis.TransferPort getTransferPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            de.complex.jclxsync.soap.axis.TransferBindingStub _stub = new de.complex.jclxsync.soap.axis.TransferBindingStub(portAddress, this);
            _stub.setPortName(getTransferPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransferPortEndpointAddress(java.lang.String address) {
        TransferPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (de.complex.jclxsync.soap.axis.TransferPort.class.isAssignableFrom(serviceEndpointInterface)) {
                de.complex.jclxsync.soap.axis.TransferBindingStub _stub = new de.complex.jclxsync.soap.axis.TransferBindingStub(new java.net.URL(TransferPort_address), this);
                _stub.setPortName(getTransferPortWSDDServiceName());
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
        if ("TransferPort".equals(inputPortName)) {
            return getTransferPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Transfer", "TransferService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:Transfer", "TransferPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TransferPort".equals(portName)) {
            setTransferPortEndpointAddress(address);
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
