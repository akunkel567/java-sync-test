/**
 * DatacheckService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.datacheck;

public interface DatacheckService extends javax.xml.rpc.Service {
    public java.lang.String getDatacheckPortAddress();

    public de.complex.clxproductsync.soap.datacheck.DatacheckPort getDatacheckPort() throws javax.xml.rpc.ServiceException;

    public de.complex.clxproductsync.soap.datacheck.DatacheckPort getDatacheckPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
