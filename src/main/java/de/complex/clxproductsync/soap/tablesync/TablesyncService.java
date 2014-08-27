/**
 * TablesyncService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.tablesync;

public interface TablesyncService extends javax.xml.rpc.Service {
    public java.lang.String getTablesyncPortAddress();

    public de.complex.clxproductsync.soap.tablesync.TablesyncPort getTablesyncPort() throws javax.xml.rpc.ServiceException;

    public de.complex.clxproductsync.soap.tablesync.TablesyncPort getTablesyncPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
