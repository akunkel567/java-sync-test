/**
 * TablesyncPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.tablesync;

public interface TablesyncPort extends java.rmi.Remote {
    public de.complex.jclxsync.soap.tablesync.TableIdList getTableIdList(java.lang.String login, java.lang.String password, java.lang.String tablename) throws java.rmi.RemoteException;
}
