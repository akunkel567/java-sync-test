/**
 * TransferService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.axis;

public interface TransferService extends javax.xml.rpc.Service {
    public java.lang.String getTransferPortAddress();

    public de.complex.clxproductsync.soap.axis.TransferPort getTransferPort() throws javax.xml.rpc.ServiceException;

    public de.complex.clxproductsync.soap.axis.TransferPort getTransferPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
