/**
 * DatacheckPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.datacheck;

public interface DatacheckPort extends java.rmi.Remote {
    public de.complex.clxproductsync.soap.datacheck.Spoolcheck getSpoolcheck(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException;
    public de.complex.clxproductsync.soap.datacheck.Spoolcheck[] getSpoolcheckList(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException;
    public de.complex.clxproductsync.soap.datacheck.Spoolcheck[] getSnjobwebList(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException;
    public de.complex.clxproductsync.soap.datacheck.Spoolcheck[] getWebSpoolcheckList(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException;
    public de.complex.clxproductsync.soap.datacheck.Returnnok[] setSpoolcheckList(java.lang.String login, java.lang.String password, de.complex.clxproductsync.soap.datacheck.Spoolcheck[] spoolcheck) throws java.rmi.RemoteException;
    public de.complex.clxproductsync.soap.datacheck.Status getStatus(java.lang.String login, java.lang.String password) throws java.rmi.RemoteException;
}
